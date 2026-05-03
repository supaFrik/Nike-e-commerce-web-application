package vn.demo.nike.features.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.order.domain.OrderItem;
import vn.demo.nike.features.order.domain.enums.OrderStatus;
import vn.demo.nike.features.order.dto.*;
import vn.demo.nike.features.order.exception.OrderIdAndUserIdNotFoundException;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.payment.domain.PaymentTransaction;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;
import vn.demo.nike.features.payment.domain.enums.PaymentStatus;
import vn.demo.nike.features.payment.repository.PaymentTransactionRepository;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderPageViewService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final OrderRepository orderRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final ProductRepository productRepository;

    public OrderPageView getOrderPage(Long orderId, Long userId) {
        validateInput(orderId, userId);

        Order order = loadOrder(orderId, userId);
        List<PaymentTransaction> transactions = loadPaymentTransactions(order.getId());
        PaymentTransaction latestTransaction = transactions.isEmpty() ? null : transactions.get(0);
        return OrderPageView.builder()
                .orderId(order.getId())
                .orderCode(String.valueOf(order.getId()))
                .successHeadline(buildSuccessHeadline(order))
                .successDescription(buildSuccessDescription(order))
                .placedAtLabel(formatDateTime(order.getCreateDate()))
                .orderStatusLabel(toOrderStatusLabel(order.getOrderStatus()))
                .paymentStatusLabel(toPaymentStatusLabel(latestTransaction))
                .paymentMethodLabel(toPaymentMethodLabel(order.getPaymentMethod()))
                .paymentSummaryLabel(buildPaymentSummary(order, latestTransaction))
                .total(order.getTotal())
                .timelineSteps(buildTimeline(order))
                .items(buildItems(order))
                .shipping(buildShipping(order))
                .payment(buildPayment(order, latestTransaction))
                .pricing(buildPricing(order))
                .actions(buildActions(order))
                .paymentHistory(buildPaymentHistory(transactions))
                .build();
    }

    private void validateInput(Long orderId, Long userId) {
        if (orderId == null || userId == null) {
            throw new OrderIdAndUserIdNotFoundException();
        }
    }

    private Order loadOrder(Long orderId, Long userId) {
        return orderRepository.findByIdAndUser_Id(orderId, userId)
                .orElseThrow(OrderIdAndUserIdNotFoundException::new);
    }

    private List<PaymentTransaction> loadPaymentTransactions(Long orderId) {
        return paymentTransactionRepository.findByOrder_IdOrderByCreateDateDesc(orderId);
    }

    private String buildSuccessHeadline(Order order) {
        return order.getOrderStatus() == OrderStatus.PAYMENT_FAILED
                ? "Đơn hàng đã được tạo nhưng chưa được thanh toán."
                : "Đơn hàng đã được đặt thành công !";
    }
    private String buildSuccessDescription(Order order) {
        if(order.getPaymentMethod() == PaymentMethod.COD) {
            return "Đơn hàng đã được ghi nhận. Bạn sẽ thanh toán khi nhận hàng";
        }
        return "Đơn hàng đã được ghi nhận và đang chờ kết qua thanh toán từ cổng thanh toán.";
    }

    private String toOrderStatusLabel(OrderStatus orderStatus) {
        if(orderStatus == null) {
            return "Unknown";
        }
        return switch (orderStatus) {
            case PENDING_PAYMENT -> "Đang chờ thanh toán";
            case PAID -> "Đã thanh toán";
            case PAYMENT_FAILED -> "Đang chờ thanh toán";
            case PROCESSING -> "Đang chờ xử lý đơn hàng";
            case SHIPPING -> "Đang giao";
            case DELIVERED -> "Đã được giao";
            case CANCELLED -> "Đã hủy";
        };
    }

    private String toPaymentStatusLabel(PaymentTransaction paymentTransaction) {
        if(paymentTransaction == null || paymentTransaction.getStatus() == null) {
            return "Đang chờ";
        }
        return switch(paymentTransaction.getStatus()) {
            case INIT -> "Đang khởi tạo";
            case PENDING -> "Đang chờ";
            case SUCCESS -> "Đã thanh toán";
            case FAILED -> "Thất bại";
            case CANCELLED -> "Đã hủy";
            case EXPIRED -> "Đã hết thời gian";
        };
    }

    private String toPaymentMethodLabel(PaymentMethod paymentMethod) {
        if(paymentMethod == null) {
            return "Unknown";
        }
        return switch(paymentMethod) {
            case COD -> "COD";
            case VNPAY -> "VNPay";
        };
    }

    private String buildPaymentSummary(Order order, PaymentTransaction paymentTransaction) {
        if(order.getPaymentMethod() == PaymentMethod.COD) {
            return "Thanh toán khi nhận hàng";
        }
        return paymentTransaction != null && paymentTransaction.getStatus() == PaymentStatus.SUCCESS
                ? "Thanh toán qua VNPay"
                : "Đang chờ xác nhận từ VNPay";
    }

    private List<OrderTimelineStepView> buildTimeline(Order order) {
        OrderStatus status = order.getOrderStatus();
        return List.of(
                timeLineStep("Đang chờ thanh toán", "Đơn đã được ghi nhận và chờ xác nhân thanh toán,", true, status == OrderStatus.PENDING_PAYMENT),
                timeLineStep("Đã thanh toán thành công", "Đơn đã được thanh toán hoặc đã ghi nhận COD", status != OrderStatus.PENDING_PAYMENT && status != OrderStatus.PAYMENT_FAILED, status == OrderStatus.PAID),
                timeLineStep("Đang chờ xác nhận", "Đơn đang được chuẩn bị,", isAtLeast(status, OrderStatus.PROCESSING), status == OrderStatus.PROCESSING),
                timeLineStep("Đang được vận chuyển", "Đơn đang được giao tới bạn", isAtLeast(status, OrderStatus.SHIPPING), status == OrderStatus.SHIPPING),
                timeLineStep("Đã được giao thành công", "Đơn hàng đã được giao thành công", status == OrderStatus.DELIVERED, status == OrderStatus.DELIVERED)
        );
    }

    private OrderTimelineStepView timeLineStep(String label, String description, boolean completed, boolean current) {
        return OrderTimelineStepView.builder()
                .label(label)
                .description(description)
                .completed(completed)
                .current(current)
                .build();
    }

    private boolean isAtLeast(OrderStatus actual, OrderStatus expected) {
        if (actual == expected) {
            return false;
        }
        return actual.ordinal() >= expected.ordinal();
    }

    private List<OrderItemView> buildItems(Order order) {
        return order.getItems().stream()
                .map(this::toOrderItemView)
        .toList();
    }

    private OrderItemView toOrderItemView(OrderItem orderItem) {
        return OrderItemView.builder()
                .imageUrl(resolveOrderItemImage(orderItem))
                .productName(orderItem.getProductName())
                .productUrl(null)
                .sku(orderItem.getSku())
                .size(orderItem.getSize())
                .color(orderItem.getColor())
                .unitPrice(orderItem.getUnitPrice())
                .quantity(orderItem.getQuantity())
                .lineTotal(orderItem.getLineTotal())
                .build();
    }

    private OrderShippingView buildShipping(Order order) {
        return OrderShippingView.builder()
                .recipientName(order.getShippingRecipientName())
                .phone(order.getShippingPhone())
                .fullAddress(buildFullAddress(order))
                .shippingMethodLabel(order.getShippingMethod() == null ? "Standard Shipping" : order.getShippingMethod().label())
                .build();
    }

    private String buildFullAddress(Order order) {
        return joinNonBlank(
             order.getShippingLine1(),
             order.getShippingLine2(),
             order.getShippingCity(),
             order.getShippingProvince(),
             order.getShippingPostalCode(),
             order.getShippingCountry()
        );
    }

    private String joinNonBlank(String... parts) {
        return Arrays.stream(parts)
                .filter(value -> value != null && !value.isBlank())
                .reduce((left, right) -> left + ", " + right)
                .orElse("");
    }

    private OrderPaymentView buildPayment(Order order, PaymentTransaction paymentTransaction) {
        // Map COD and VNPay differently. Do not expose raw entity directly to JSP.
        Boolean online = order.getPaymentMethod() == PaymentMethod.VNPAY;
        return OrderPaymentView.builder()
                .online(online)
                .providerLabel(online ? "VNPay" : null)
                .transactionNo(paymentTransaction != null ? paymentTransaction.getTransactionNo() : null)
                .paymentTimeLabel(formatDateTime(resolvePaymentTime(paymentTransaction)))
                .txnRef(paymentTransaction != null ? paymentTransaction.getTxnRef() : null)
                .description(online ? buildPaymentSummary(order, paymentTransaction) : "Thanh toán khi nhận hàng")
                .build();
    }

    private String resolveOrderItemImage(OrderItem orderItem) {
        if (orderItem.getProductId() == null) {
            return null;
        }

        Product product = productRepository.findById(orderItem.getProductId()).orElse(null);
        if (product == null || product.getColors() == null || product.getColors().isEmpty()) {
            return null;
        }

        String requestedColor = normalize(orderItem.getColor());
        if (requestedColor != null) {
            String matchedColorImage = product.getColors().stream()
                    .filter(color -> requestedColor.equalsIgnoreCase(normalize(color.getColorName())))
                    .findFirst()
                    .map(this::resolveColorImage)
                    .orElse(null);
            if (matchedColorImage != null) {
                return matchedColorImage;
            }
        }

        return product.getColors().stream()
                .map(this::resolveColorImage)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(null);
    }

    private String resolveColorImage(ProductColor color) {
        List<ProductImage> images = safeImages(color);
        if (images.isEmpty()) {
            return null;
        }

        return images.stream()
                .filter(image -> Boolean.TRUE.equals(image.getIsMainForColor()))
                .sorted(Comparator.comparing(ProductImage::getOrderIndex, Comparator.nullsLast(Integer::compareTo)))
                .map(ProductImage::getPath)
                .map(ProductImageUrlResolver::toPublicUrl)
                .findFirst()
                .orElseGet(() -> images.stream()
                        .sorted(Comparator.comparing(ProductImage::getOrderIndex, Comparator.nullsLast(Integer::compareTo)))
                        .map(ProductImage::getPath)
                        .map(ProductImageUrlResolver::toPublicUrl)
                        .findFirst()
                        .orElse(null));
    }

    private List<ProductImage> safeImages(ProductColor color) {
        return color.getImages() == null ? List.of() : color.getImages();
    }

    private LocalDateTime resolvePaymentTime(PaymentTransaction paymentTransaction) {
        if (paymentTransaction == null) {
            return null;
        }
        return paymentTransaction.getPayDate() != null
                ? paymentTransaction.getPayDate()
                : paymentTransaction.getCreateDate();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private OrderPricingView buildPricing(Order order) {
        return OrderPricingView.builder()
                .subtotal(order.getSubtotal())
                .shippingFee(order.getShippingCost())
                .discount(order.getDiscount())
                .total(order.getTotal())
                .build();
    }

    private List<OrderActionView> buildActions(Order order) {
        return List.of(
                OrderActionView.builder()
                        .label(order.getOrderStatus() == OrderStatus.PAYMENT_FAILED ? "Thanh toán lại" : "Theo dõi đơn hàng")
                        .href("#")
                        .kind("primary")
                        .available(true)
                        .hint(order.getOrderStatus() == OrderStatus.PAYMENT_FAILED
                                ? "Hoàn tất thanh toán để đơn hàng được xử lý."
                                : "Xem trạng thái giao hàng mới nhất.")
                        .build(),
                OrderActionView.builder()
                        .label("Mua lại")
                        .href("#")
                        .kind("secondary")
                        .available(true)
                        .hint("Thêm lại các sản phẩm này vào giỏ hàng.")
                        .build()
        );
    }

    private List<PaymentHistoryView> buildPaymentHistory(List<PaymentTransaction> transactions) {
        if(transactions.isEmpty()) {
            return List.of(
                    PaymentHistoryView.builder()
                            .title("Hiện tại chưa có giao dịch nào")
                            .description("Chưa có thanh toán nào cho đơn này.")
                            .timestampLabel("Đang chờ")
                            .success(false)
                            .build()
            );
        }
        return transactions.stream()
                .map(this::toPaymentHistoryView)
                .toList();
    }

    private PaymentHistoryView toPaymentHistoryView(PaymentTransaction transaction) {
        return PaymentHistoryView.builder()
                .title(titleCase(transaction.getStatus().name()))
                .description(buildTransactionDescription(transaction))
                .timestampLabel(formatDateTime(transaction.getPayDate() != null ? transaction.getPayDate() : transaction.getCreateDate()))
                .success(transaction.getStatus() == PaymentStatus.SUCCESS)
                .build();
    }

    private String buildTransactionDescription(PaymentTransaction transaction) {
        String txnRef = transaction.getTxnRef() == null ? "N/A" : transaction.getTxnRef();
        String responseCode = transaction.getResponseCode() == null ? "N/A" : transaction.getResponseCode();
        return "TxnRef: " + txnRef + ", responseCode: " + responseCode;
    }

    private String titleCase(String raw) {
        String normalized = raw.toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
    }

    private String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.format(DATE_TIME_FORMATTER);
    }
}
