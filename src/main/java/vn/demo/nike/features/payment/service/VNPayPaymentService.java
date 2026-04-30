package vn.demo.nike.features.payment.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.order.domain.enums.OrderStatus;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.payment.config.VNPayProperties;
import vn.demo.nike.features.payment.domain.PaymentTransaction;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;
import vn.demo.nike.features.payment.domain.enums.PaymentProvider;
import vn.demo.nike.features.payment.domain.enums.PaymentStatus;
import vn.demo.nike.features.payment.dto.VNPayCreatePaymentResponse;
import vn.demo.nike.features.payment.dto.VNPayIpnResponse;
import vn.demo.nike.features.payment.dto.VNPayReturnResponse;
import vn.demo.nike.features.payment.repository.PaymentTransactionRepository;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static vn.demo.nike.features.payment.domain.enums.PaymentStatus.PENDING;

@Slf4j
@Service
@RequiredArgsConstructor
public class VNPayPaymentService {
    private static final int SAFE_AUDIT_TEXT_LENGTH = 240;

    private final VNPayProperties vnPayProperties;
    private final VNPaySignatureService vnPaySignatureService;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public VNPayCreatePaymentResponse createPaymentUrl(Long orderId, HttpServletRequest request) {
        Order order = loadOrderForVNPay(orderId);
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

        LocalDateTime now = LocalDateTime.now();
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

    @Transactional(readOnly = true)
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
                .findByTxnRef(txnRef)
                .orElse(null);

        Long orderId = (txn != null) ? txn.getOrder().getId() : null;

        PaymentStatus suggestedStatus;

        boolean paymentSuccess =
                signatureValid
                        && "00".equals(responseCode)
                        && "00".equals(transactionStatus);

        suggestedStatus = paymentSuccess
                ? PaymentStatus.SUCCESS
                : PaymentStatus.FAILED;

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
                .findByTxnRef(txnRef)
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

        long gatewayAmount;
        try {
            if (amountRaw == null || amountRaw.isBlank()) {
                log.warn("VNPay IPN missing amount, txnRef={}", txnRef);
                return VNPayIpnResponse.builder()
                        .RspCode("04")
                        .Message("Invalid amount")
                        .build();
            }

            gatewayAmount = Long.parseLong(amountRaw);
        } catch (NumberFormatException ex) {
            log.warn("VNPay IPN invalid amount format, txnRef={}, amount={}",
                    txnRef, amountRaw);
            return VNPayIpnResponse.builder()
                    .RspCode("04")
                    .Message("Invalid amount")
                    .build();
        }

        long expectedAmount = transaction.getAmount()
                .longValueExact() * 100L;

        if (gatewayAmount != expectedAmount) {
            log.warn(
                    "VNPay IPN amount mismatch, txnRef={}, expected={}, actual={}",
                    txnRef,
                    expectedAmount,
                    gatewayAmount
            );

            return VNPayIpnResponse.builder()
                    .RspCode("04")
                    .Message("Invalid amount")
                    .build();
        }

        boolean paymentSuccess =
                "00".equals(responseCode) && "00".equals(transactionStatus);

        PaymentStatus paymentStatus = paymentSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        OrderStatus orderStatus = paymentSuccess ? OrderStatus.PROCESSING : OrderStatus.PAYMENT_FAILED;

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

        transaction.setStatus(paymentStatus);
        transaction.setResponseCode(responseCode);
        transaction.setTransactionNo(transactionNo);
        transaction.setBankCode(bankCode);
        transaction.setTransactionStatus(transactionStatus);
        transaction.setPayDate(parseVnPayDate(payDateRaw));
        transaction.setRawResponsePayload(toSafeAuditText(new TreeMap<>(params).toString()));
        transaction.setIpnProcessed(true);

        if (!paymentSuccess) {
            transaction.setFailureReason(
                    toSafeAuditText("VNPay payment failed: responseCode=" + responseCode
                            + ", txnRef=" + txnRef
                            + ", transactionStatus=" + transactionStatus)
            );
        } else {
            transaction.setFailureReason(null);
        }

        order.setOrderStatus(orderStatus);

        paymentTransactionRepository.save(transaction);
        orderRepository.save(order);

        log.info(
                "VNPay IPN processed successfully, txnRef={}, paymentStatus={}, orderStatus={}",
                txnRef,
                paymentStatus,
                orderStatus
        );
        return VNPayIpnResponse.builder()
                .RspCode("00")
                .Message("Confirm success")
                .build();
    }

    private Order loadOrderForVNPay(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        if (order.getPaymentMethod() != PaymentMethod.VNPAY) {
            throw new IllegalStateException(
                    "Order is not using VNPay: " + orderId
            );
        }

        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Order is not waiting for payment: " + orderId);
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
