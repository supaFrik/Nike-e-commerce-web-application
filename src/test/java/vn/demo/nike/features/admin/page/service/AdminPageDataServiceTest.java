package vn.demo.nike.features.admin.page.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import vn.demo.nike.features.catalog.category.domain.Category;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.catalog.product.domain.enums.ProductStatus;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.features.identity.user.domain.enums.Role;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.order.domain.OrderItem;
import vn.demo.nike.features.order.domain.enums.OrderStatus;
import vn.demo.nike.features.order.domain.enums.ShippingMethod;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.features.payment.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminPageDataServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private AdminPageDataService adminPageDataService;

    @Test
    void listProductInventory_mapsRealStockFromVariants() {
        Product product = sampleProduct();
        when(productRepository.findAll(any(Sort.class))).thenReturn(List.of(product));

        var items = adminPageDataService.listProductInventory();

        assertThat(items).hasSize(1);
        assertThat(items.get(0).stock()).isEqualTo(9);
        assertThat(items.get(0).colorNames()).containsExactly("Đen");
        assertThat(items.get(0).sizes()).containsExactly("41", "42");
    }

    @Test
    void listCategories_usesRealProductCountsOnly() {
        Category category = sampleCategory();
        category.setProducts(new ArrayList<>(List.of(new Product())));
        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(List.of(category));

        var categories = adminPageDataService.listCategories();

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).name()).isEqualTo("Chạy bộ");
        assertThat(categories.get(0).productCount()).isEqualTo(1);
    }

    @Test
    void getDashboard_aggregatesCountsFromRepositories() {
        Product product = sampleProduct();
        Category category = sampleCategory();
        Order order = sampleOrder();
        when(productRepository.findAll(any(Sort.class))).thenReturn(List.of(product));
        when(categoryRepository.count()).thenReturn(1L);
        when(orderRepository.findAll()).thenReturn(List.of(order));

        var dashboard = adminPageDataService.getDashboard();

        assertThat(dashboard.productCount()).isEqualTo(1);
        assertThat(dashboard.categoryCount()).isEqualTo(1);
        assertThat(dashboard.orderCount()).isEqualTo(1);
        assertThat(dashboard.totalRevenue()).isEqualByComparingTo("250.00");
    }

    private Product sampleProduct() {
        Category category = sampleCategory();

        Product product = new Product();
        product.setId(10L);
        product.setName("Nike Air Max");
        product.setDescription("Mô tả thật");
        product.setType("MEN");
        product.setPrice(BigDecimal.valueOf(300));
        product.setSalePrice(BigDecimal.valueOf(250));
        product.setProductStatus(ProductStatus.ACTIVE);
        product.setCategory(category);
        product.setCreateDate(LocalDateTime.of(2026, 4, 29, 10, 0));

        ProductColor color = new ProductColor();
        color.setColorName("Đen");
        color.setHexCode("#111111");
        color.setDisplayOrder(0);
        color.setProduct(product);

        ProductImage image = new ProductImage();
        image.setPath("nike-air-max/den/main.webp");
        image.setIsMainForColor(true);
        image.setOrderIndex(0);
        image.setColor(color);

        ProductVariant v41 = new ProductVariant();
        v41.setSize("41");
        v41.setSku("SKU-41");
        v41.setStock(5);
        v41.setActive(true);
        v41.setColor(color);

        ProductVariant v42 = new ProductVariant();
        v42.setSize("42");
        v42.setSku("SKU-42");
        v42.setStock(4);
        v42.setActive(true);
        v42.setColor(color);

        color.setImages(new ArrayList<>(List.of(image)));
        color.setVariants(new ArrayList<>(List.of(v41, v42)));
        product.setColors(new ArrayList<>(List.of(color)));
        category.setProducts(new ArrayList<>(List.of(product)));
        return product;
    }

    private Category sampleCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Chạy bộ");
        category.setProducts(new ArrayList<>());
        return category;
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
        order.setPaymentMethod(PaymentMethod.COD);
        order.setTotal(BigDecimal.valueOf(250));
        order.setCreateDate(LocalDateTime.of(2026, 4, 30, 9, 15));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(10L);
        item.setProductName("Nike Air Max");
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(125));
        item.setLineTotal(BigDecimal.valueOf(250));

        order.setItems(new ArrayList<>(List.of(item)));
        return order;
    }
}
