package vn.demo.nike.features.payment.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.catalog.cart.repository.CartItemRepository;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductVariantRepository;
import vn.demo.nike.features.order.entity.Order;
import vn.demo.nike.features.order.entity.OrderItem;
import vn.demo.nike.features.order.enums.OrderStatus;
import vn.demo.nike.features.order.exception.InvalidOrderStateException;
import vn.demo.nike.features.order.exception.OrderIdAndUserIdNotFoundException;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.payment.config.VNPayProperties;
import vn.demo.nike.features.payment.entity.PaymentTransaction;
import vn.demo.nike.features.payment.enums.PaymentMethod;
import vn.demo.nike.features.payment.enums.PaymentProvider;
import vn.demo.nike.features.payment.enums.PaymentStatus;
import vn.demo.nike.features.payment.dto.VNPayCreatePaymentResponse;
import vn.demo.nike.features.payment.dto.VNPayIpnResponse;
import vn.demo.nike.features.payment.dto.VNPayReturnResponse;
import vn.demo.nike.features.payment.exception.InvalidPaymentMethodException;
import vn.demo.nike.features.payment.repository.PaymentTransactionRepository;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static vn.demo.nike.features.payment.enums.PaymentStatus.PENDING;

@Slf4j
@Service
@RequiredArgsConstructor
public class VNPayPaymentService {
    private static final int SAFE_AUDIT_TEXT_LENGTH = 240;
    private static final ZoneId VNPAY_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final VNPayProperties vnPayProperties;
    private final VNPaySignatureService vnPaySignatureService;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final OrderRepository orderRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public VNPayCreatePaymentResponse createPaymentUrl(Long orderId, HttpServletRequest request, Long currentUserId) {
        Order order = loadOrderForVNPay(orderId, currentUserId);
        validateGatewayConfiguration();

        PaymentTransaction transaction = createPendingTransaction(order, request);

        long amount = order.getTotal().multiply(new BigDecimal(100)).longValue();

        Map<String, String> params = new HashMap<>();

        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnPayProperties.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");

        params.put("vnp_TxnRef", transaction.getTxnRef());
        params.put("vnp_OrderInfo", "Thanh toan don hang " + order.getId());
        params.put("vnp_OrderType", "200000");

        String locale = resolveLocale(request.getParameter("language"));
        params.put("vnp_Locale", locale);

        String bankCode = trimToNull(request.getParameter("bankCode"));
        if (bankCode != null) {
            params.put("vnp_BankCode", bankCode);
        }

        params.put("vnp_ReturnUrl", vnPayProperties.getReturnUrl());
        params.put("vnp_IpAddr", normalizeGatewayIpAddress(vnPaySignatureService.getIpAddress(request)));

        LocalDateTime now = LocalDateTime.now(VNPAY_ZONE);
        params.put("vnp_CreateDate", formatVnPayDate(now));

        LocalDateTime expire = now.plusMinutes(15);
        params.put("vnp_ExpireDate", formatVnPayDate(expire));

        transaction.setExpireDate(expire);
        transaction.setStatus(PENDING);
        transaction.setRawRequestPayload(toSafeAuditText(buildEncodedQuery(params)));
        paymentTransactionRepository.save(transaction);

        String hashData = buildHashData(params);
        String query = buildEncodedQuery(params);

        String secureHash = vnPaySignatureService.hmacSHA512(
                vnPayProperties.getHashSecret(),
                hashData
        );

        String paymentUrl = vnPayProperties.getPayUrl()
                + "?" + query
                + "&vnp_SecureHash=" + secureHash;

        log.info("VNPay paymentUrl={}", paymentUrl);

        return new VNPayCreatePaymentResponse(
                order.getId(),
                transaction.getTxnRef(),
                paymentUrl
        );
    }

    @Transactional
    public VNPayReturnResponse handleReturn(Map<String, String> params) {
        String secureHash = params.get("vnp_SecureHash");

        Map<String, String> fields = copyAndStripSignatureFields(params);

        String hashData = buildHashData(fields);

        String calculatedHash = vnPaySignatureService.hmacSHA512(
                vnPayProperties.getHashSecret(),
                hashData
        );

        boolean signatureValid = calculatedHash.equals(secureHash);

        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionStatus = params.get("vnp_TransactionStatus");

        PaymentTransaction txn = paymentTransactionRepository
                .findByTxnRefForUpdate(txnRef)
                .orElse(null);

        Long orderId = (txn != null) ? txn.getOrder().getId() : null;

        PaymentStatus suggestedStatus;

        boolean amountValid = txn != null && isGatewayAmountValid(txn, params.get("vnp_Amount"));
        boolean paymentSuccess =
                signatureValid
                        && amountValid
                        && "00".equals(responseCode)
                        && "00".equals(transactionStatus);

        suggestedStatus = paymentSuccess
                ? PaymentStatus.SUCCESS
                : PaymentStatus.FAILED;

        if (txn != null && signatureValid && txn.getOrder().getOrderStatus() == OrderStatus.PENDING_PAYMENT) {
            applyGatewayResult(txn, params, paymentSuccess, responseCode, transactionStatus);
        }

        log.info("VNPay return txnRef={}, responseCode={}, transactionStatus={}, valid={}", txnRef, responseCode, transactionStatus, signatureValid);

        return new VNPayReturnResponse(
                orderId,
                txnRef,
                signatureValid,
                responseCode,
                transactionStatus,
                suggestedStatus
        );
    }

    @Transactional
    public VNPayIpnResponse handleIpn(Map<String, String> params) {
        String txnRef = params.get("vnp_TxnRef");
        String amountRaw = params.get("vnp_Amount");
        String responseCode = params.get("vnp_ResponseCode");
        String transactionStatus = params.get("vnp_TransactionStatus");
        String transactionNo = params.get("vnp_TransactionNo");
        String bankCode = params.get("vnp_BankCode");
        String payDateRaw = params.get("vnp_PayDate");
        String secureHash = params.get("vnp_SecureHash");

        Map<String, String> fields = copyAndStripSignatureFields(params);

        String hashData = buildHashData(fields);
        String calculatedHash = vnPaySignatureService.hmacSHA512(
                vnPayProperties.getHashSecret(),
                hashData
        );

        if (!Objects.equals(calculatedHash, secureHash)) {
            log.warn("VNPay IPN checksum invalid, txnRef={}", txnRef);
            return VNPayIpnResponse.builder()
                    .RspCode("97")
                    .Message("Invalid checksum")
                    .build();
        }

        PaymentTransaction transaction = paymentTransactionRepository
                .findByTxnRefForUpdate(txnRef)
                .orElse(null);

        if (transaction == null) {
            log.warn("VNPay IPN transaction not found, txnRef={}", txnRef);
            return VNPayIpnResponse.builder()
                    .RspCode("01")
                    .Message("Order not found")
                    .build();
        }

        if (transaction.isIpnProcessed()) {
            log.info("VNPay IPN already processed, txnRef={}", txnRef);
            return VNPayIpnResponse.builder()
                    .RspCode("02")
                    .Message("Order already confirmed")
                    .build();
        }

        if (!isGatewayAmountValid(transaction, amountRaw)) {
            log.warn(
                    "VNPay IPN amount invalid, txnRef={}, expected={}, actual={}",
                    txnRef,
                    transaction.getAmount().longValueExact() * 100L,
                    amountRaw
            );

            return VNPayIpnResponse.builder()
                    .RspCode("04")
                    .Message("Invalid amount")
                    .build();
        }

        boolean paymentSuccess =
                "00".equals(responseCode) && "00".equals(transactionStatus);

        Order order = transaction.getOrder();
        if (order.getPaymentMethod() != PaymentMethod.VNPAY) {
            log.warn("VNPay IPN received for non-VNPay order, txnRef={}, orderId={}", txnRef, order.getId());
            return VNPayIpnResponse.builder()
                    .RspCode("01")
                    .Message("Order not found")
                    .build();
        }

        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            log.info(
                    "VNPay IPN order already moved out of pending state, txnRef={}, orderId={}, currentStatus={}",
                    txnRef,
                    order.getId(),
                    order.getOrderStatus()
            );
            return VNPayIpnResponse.builder()
                    .RspCode("02")
                    .Message("Order already confirmed")
                    .build();
        }

        PaymentStatus paymentStatus = paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        applyGatewayResult(transaction, params, paymentSuccess, responseCode, transactionStatus);
        transaction.setTransactionNo(transactionNo);
        transaction.setBankCode(bankCode);
        transaction.setPayDate(parseVnPayDate(payDateRaw));
        transaction.setIpnProcessed(true);

        paymentTransactionRepository.save(transaction);
        orderRepository.save(order);

        log.info(
                "VNPay IPN processed successfully, txnRef={}, paymentStatus={}, orderStatus={}",
                txnRef,
                paymentStatus,
                order.getOrderStatus()
        );
        return VNPayIpnResponse.builder()
                .RspCode("00")
                .Message("Confirm success")
                .build();
    }

    private void finalizeSuccessfulPaymentOrder(Order order) {
        deductStock(order);
        clearPurchasedCartItems(order);
        order.setOrderStatus(OrderStatus.PROCESSING);
    }

    private void applyGatewayResult(PaymentTransaction transaction,
                                    Map<String, String> params,
                                    boolean paymentSuccess,
                                    String responseCode,
                                    String transactionStatus) {
        transaction.setStatus(paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        transaction.setResponseCode(responseCode);
        transaction.setTransactionNo(params.get("vnp_TransactionNo"));
        transaction.setBankCode(params.get("vnp_BankCode"));
        transaction.setTransactionStatus(transactionStatus);
        transaction.setPayDate(parseVnPayDate(params.get("vnp_PayDate")));
        transaction.setRawResponsePayload(toSafeAuditText(new TreeMap<>(params).toString()));

        if (paymentSuccess) {
            transaction.setFailureReason(null);
            finalizeSuccessfulPaymentOrder(transaction.getOrder());
            return;
        }

        transaction.setFailureReason(
                toSafeAuditText("VNPay payment failed: responseCode=" + responseCode
                        + ", txnRef=" + transaction.getTxnRef()
                        + ", transactionStatus=" + transactionStatus)
        );
        transaction.getOrder().setOrderStatus(OrderStatus.PAYMENT_FAILED);
    }

    private boolean isGatewayAmountValid(PaymentTransaction transaction, String amountRaw) {
        if (transaction == null || amountRaw == null || amountRaw.isBlank()) {
            return false;
        }

        try {
            long gatewayAmount = Long.parseLong(amountRaw);
            long expectedAmount = transaction.getAmount().longValueExact() * 100L;
            return gatewayAmount == expectedAmount;
        } catch (ArithmeticException | NumberFormatException ex) {
            return false;
        }
    }

    private void deductStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Long variantId = item.getVariantId();
            ProductVariant variant = productVariantRepository.findByIdForUpdate(variantId)
                    .orElseThrow(() -> new InvalidOrderStateException(order.getId(), order.getOrderStatus(), OrderStatus.PROCESSING));

            Integer requestedQuantity = item.getQuantity();
            Integer currentStock = variant.getStock();
            if (!Boolean.TRUE.equals(variant.getActive()) || currentStock == null || currentStock < requestedQuantity) {
                throw new InvalidOrderStateException(order.getId(), order.getOrderStatus(), OrderStatus.PROCESSING);
            }

            variant.setStock(currentStock - requestedQuantity);
        }
    }

    private void clearPurchasedCartItems(Order order) {
        Long userId = order.getUser().getId();
        List<Long> variantIds = order.getItems().stream()
                .map(OrderItem::getVariantId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (!variantIds.isEmpty()) {
            cartItemRepository.deleteByUser_IdAndVariant_IdIn(userId, variantIds);
        }
    }

    private Order loadOrderForVNPay(Long orderId, Long currentUserId) {
        Order order = orderRepository.findByIdAndUser_Id(orderId, currentUserId)
                .orElseThrow(OrderIdAndUserIdNotFoundException::new);

        if (order.getPaymentMethod() != PaymentMethod.VNPAY) {
            throw new InvalidPaymentMethodException(PaymentMethod.VNPAY);
        }

        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderStateException(orderId, order.getOrderStatus(), OrderStatus.PENDING_PAYMENT);
        }
        return order;
    }

    private PaymentTransaction createPendingTransaction(Order order, HttpServletRequest request) {
        PaymentTransaction transaction = PaymentTransaction.builder()
                .order(order)
                .provider(PaymentProvider.VNPAY)
                .txnRef(generateTxnRef(order.getId()))
                .amount(order.getTotal())
                .status(PaymentStatus.INIT)
                .ipAddress(normalizeGatewayIpAddress(vnPaySignatureService.getIpAddress(request)))
                .rawRequestPayload(null)
                .rawResponsePayload(null)
                .failureReason(null)
                .ipnProcessed(false)
                .build();

        return paymentTransactionRepository.save(transaction);
    }

    public String formatVnPayDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public LocalDateTime parseVnPayDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(raw, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public Map<String, String> copyAndStripSignatureFields(Map<String, String> params) {
        Map<String, String> fields = new HashMap<>(params);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        return fields;
    }

    private String generateTxnRef(Long orderId) {
        return "ORD" + orderId + "T" + vnPaySignatureService.getRandomNumber(8);
    }

    private String resolveLocale(String language) {
        String normalized = trimToNull(language);
        return normalized == null ? "vn" : normalized;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void validateGatewayConfiguration() {
        if (trimToNull(vnPayProperties.getTmnCode()) == null) {
            throw new IllegalStateException("VNPay tmnCode is missing");
        }
        if (trimToNull(vnPayProperties.getHashSecret()) == null) {
            throw new IllegalStateException("VNPay hashSecret is missing");
        }
        if (trimToNull(vnPayProperties.getPayUrl()) == null) {
            throw new IllegalStateException("VNPay payUrl is missing");
        }
        if (trimToNull(vnPayProperties.getReturnUrl()) == null) {
            throw new IllegalStateException("VNPay returnUrl is missing");
        }
    }

    private String normalizeGatewayIpAddress(String ipAddress) {
        String normalized = trimToNull(ipAddress);
        if (normalized == null) {
            return "127.0.0.1";
        }
        if ("0:0:0:0:0:0:0:1".equals(normalized) || "::1".equals(normalized)) {
            return "127.0.0.1";
        }
        return normalized;
    }

    private String toSafeAuditText(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            return null;
        }
        if (normalized.length() <= SAFE_AUDIT_TEXT_LENGTH) {
            return normalized;
        }
        return normalized.substring(0, SAFE_AUDIT_TEXT_LENGTH);
    }

    private String buildHashData(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue == null || fieldValue.isEmpty()) {
                continue;
            }

            if (!hashData.isEmpty()) {
                hashData.append('&');
            }
            hashData.append(fieldName)
                    .append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
        }
        return hashData.toString();
    }

    private String buildEncodedQuery(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue == null || fieldValue.isEmpty()) {
                continue;
            }

            if (!query.isEmpty()) {
                query.append('&');
            }
            query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
        }
        return query.toString();
    }

    @SuppressWarnings("unused")
    private BigDecimal readInternalAmount(PaymentTransaction transaction) {
        return transaction.getAmount();
    }
}
