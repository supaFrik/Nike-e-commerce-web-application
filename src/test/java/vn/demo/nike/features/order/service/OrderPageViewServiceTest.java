package vn.demo.nike.features.order.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.category.entity.Category;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.entity.ProductImage;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.identity.user.entity.User;
import vn.demo.nike.features.identity.user.enums.Role;
import vn.demo.nike.features.order.entity.Order;
import vn.demo.nike.features.order.entity.OrderItem;
import vn.demo.nike.features.order.enums.OrderStatus;
import vn.demo.nike.features.order.enums.ShippingMethod;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.payment.entity.PaymentTransaction;
import vn.demo.nike.features.payment.enums.PaymentMethod;
import vn.demo.nike.features.payment.enums.PaymentProvider;
import vn.demo.nike.features.payment.enums.PaymentStatus;
import vn.demo.nike.features.payment.repository.PaymentTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderPageViewServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderPageViewService orderPageViewService;

    @Test
    void getOrderPage_mapsItemImageFromProductAndFormatsPaymentTime() {
        Order order = sampleOrder();
        Product product = sampleProduct(order.getItems().get(0).getProductId(), "Khaki");
        PaymentTransaction paymentTransaction = samplePaymentTransaction(order);

        when(orderRepository.findByIdAndUser_Id(order.getId(), order.getUser().getId()))
                .thenReturn(Optional.of(order));
        when(paymentTransactionRepository.findByOrder_IdOrderByCreateDateDesc(order.getId()))
                .thenReturn(List.of(paymentTransaction));
        when(productRepository.findById(order.getItems().get(0).getProductId()))
                .thenReturn(Optional.of(product));

        var result = orderPageViewService.getOrderPage(order.getId(), order.getUser().getId());

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getImageUrl())
                .isEqualTo("https://res.cloudinary.com/demo/image/upload/nike-test/khaki/main.avif");
        assertThat(result.getPlacedAtLabel()).isEqualTo("02/05/2026 16:00");
        assertThat(result.getPayment().getPaymentTimeLabel()).isEqualTo("02/05/2026 16:45");
        assertThat(result.getPaymentHistory()).hasSize(1);
        assertThat(result.getPaymentHistory().get(0).getTimestampLabel()).isEqualTo("02/05/2026 16:45");
    }

    private Order sampleOrder() {
        User user = User.builder()
                .username("tester")
                .email("tester@example.com")
                .password("secret")
                .role(Role.USER)
                .enabled(true)
                .locked(false)
                .build();
        user.setId(99L);

        OrderItem item = OrderItem.builder()
                .productId(10L)
                .variantId(20L)
                .productName("Nike Air Max")
                .sku("SKU-42")
                .size("42")
                .color("Khaki")
                .unitPrice(BigDecimal.valueOf(2500000))
                .quantity(1)
                .lineTotal(BigDecimal.valueOf(2500000))
                .build();

        Order order = new Order();
        order.setId(5001L);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setShippingMethod(ShippingMethod.STANDARD);
        order.setShippingRecipientName("Nguyen Van A");
        order.setShippingPhone("0909000000");
        order.setShippingLine1("12 Nguyen Trai");
        order.setShippingCity("Ho Chi Minh");
        order.setShippingCountry("Viet Nam");
        order.setPaymentMethod(PaymentMethod.VNPAY);
        order.setSubtotal(BigDecimal.valueOf(2500000));
        order.setDiscount(BigDecimal.ZERO);
        order.setShippingCost(BigDecimal.valueOf(50000));
        order.setTotal(BigDecimal.valueOf(2550000));
        order.setCreateDate(LocalDateTime.of(2026, 5, 2, 16, 0));
        order.setItems(new ArrayList<>(List.of(item)));
        item.setOrder(order);
        return order;
    }

    private Product sampleProduct(Long productId, String colorName) {
        Category category = new Category();
        category.setName("Running");

        Product product = new Product();
        product.setId(productId);
        product.setName("Nike Air Max");
        product.setCategory(category);

        ProductColor matchingColor = new ProductColor();
        matchingColor.setColorName(colorName);
        matchingColor.setProduct(product);

        ProductImage matchingImage = new ProductImage();
        matchingImage.setUrl("https://res.cloudinary.com/demo/image/upload/nike-test/khaki/main.avif");
        matchingImage.setProviderPublicId("nike-test/khaki/main");
        matchingImage.setIsMainForColor(true);
        matchingImage.setOrderIndex(0);
        matchingImage.setColor(matchingColor);
        matchingColor.setImages(new ArrayList<>(List.of(matchingImage)));

        ProductColor otherColor = new ProductColor();
        otherColor.setColorName("Black");
        otherColor.setProduct(product);

        ProductImage otherImage = new ProductImage();
        otherImage.setUrl("https://res.cloudinary.com/demo/image/upload/nike-test/black/main.avif");
        otherImage.setProviderPublicId("nike-test/black/main");
        otherImage.setIsMainForColor(true);
        otherImage.setOrderIndex(0);
        otherImage.setColor(otherColor);
        otherColor.setImages(new ArrayList<>(List.of(otherImage)));

        product.setColors(new ArrayList<>(List.of(otherColor, matchingColor)));
        return product;
    }

    private PaymentTransaction samplePaymentTransaction(Order order) {
        PaymentTransaction transaction = PaymentTransaction.builder()
                .order(order)
                .provider(PaymentProvider.VNPAY)
                .txnRef("ORD5001T12345678")
                .amount(order.getTotal())
                .status(PaymentStatus.SUCCESS)
                .transactionNo("TXN123")
                .responseCode("00")
                .build();
        transaction.setCreateDate(LocalDateTime.of(2026, 5, 2, 16, 30));
        transaction.setPayDate(LocalDateTime.of(2026, 5, 2, 16, 45));
        return transaction;
    }
}
