package vn.demo.nike.features.admin.page.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.admin.page.dto.AdminCategoryListItemResponse;
import vn.demo.nike.features.admin.page.dto.AdminDashboardResponse;
import vn.demo.nike.features.admin.page.dto.AdminOrderListItemResponse;
import vn.demo.nike.features.admin.page.dto.AdminProductInventoryItemResponse;
import vn.demo.nike.features.catalog.category.domain.Category;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;
import vn.demo.nike.features.catalog.product.domain.Product;
import vn.demo.nike.features.catalog.product.domain.ProductColor;
import vn.demo.nike.features.catalog.product.domain.ProductImage;
import vn.demo.nike.features.catalog.product.domain.ProductVariant;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.order.repository.OrderRepository;
import vn.demo.nike.shared.util.ProductImageUrlResolver;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPageDataService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int LOW_STOCK_THRESHOLD = 10;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    public List<AdminProductInventoryItemResponse> listProductInventory() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate")).stream()
                .map(this::toInventoryItem)
                .toList();
    }

    public List<AdminCategoryListItemResponse> listCategories() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(this::toCategoryItem)
                .toList();
    }

    public List<AdminOrderListItemResponse> listOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate")).stream()
                .map(this::toOrderItem)
                .toList();
    }

    public AdminDashboardResponse getDashboard() {
        List<AdminProductInventoryItemResponse> products = listProductInventory();
        List<Order> orders = orderRepository.findAll();

        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long lowStockProductCount = products.stream()
                .filter(item -> item.stock() > 0 && item.stock() <= LOW_STOCK_THRESHOLD)
                .count();

        return new AdminDashboardResponse(
                products.size(),
                categoryRepository.count(),
                orders.size(),
                lowStockProductCount,
                totalRevenue
        );
    }

    private AdminProductInventoryItemResponse toInventoryItem(Product product) {
        List<ProductColor> sortedColors = product.getColors().stream()
                .sorted(Comparator.comparing(color -> color.getDisplayOrder() == null ? Integer.MAX_VALUE : color.getDisplayOrder()))
                .toList();

        String imageUrl = sortedColors.stream()
                .flatMap(color -> color.getImages().stream()
                        .sorted(Comparator.comparing(image -> image.getOrderIndex() == null ? Integer.MAX_VALUE : image.getOrderIndex())))
                .filter(image -> Boolean.TRUE.equals(image.getIsMainForColor()))
                .map(ProductImage::getPath)
                .findFirst()
                .orElseGet(() -> sortedColors.stream()
                        .flatMap(color -> color.getImages().stream())
                        .map(ProductImage::getPath)
                        .findFirst()
                        .orElse(null));

        int stock = sortedColors.stream()
                .flatMap(color -> color.getVariants().stream())
                .map(ProductVariant::getStock)
                .filter(value -> value != null)
                .reduce(0, Integer::sum);

        List<String> colorNames = sortedColors.stream()
                .map(ProductColor::getColorName)
                .toList();

        List<String> sizes = sortedColors.stream()
                .flatMap(color -> color.getVariants().stream())
                .map(ProductVariant::getSize)
                .distinct()
                .sorted()
                .toList();

        return new AdminProductInventoryItemResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory() != null ? product.getCategory().getName() : "Chưa phân loại",
                toProductStatusLabel(product),
                ProductImageUrlResolver.toPublicUrl(imageUrl),
                product.getPrice(),
                product.getSalePrice(),
                stock,
                colorNames,
                sizes
        );
    }

    private AdminCategoryListItemResponse toCategoryItem(Category category) {
        return new AdminCategoryListItemResponse(
                category.getId(),
                category.getName(),
                category.getProducts() == null ? 0 : category.getProducts().size()
        );
    }

    private AdminOrderListItemResponse toOrderItem(Order order) {
        return new AdminOrderListItemResponse(
                order.getId(),
                order.getCreateDate() == null ? "" : DATE_FORMATTER.format(order.getCreateDate()),
                order.getUser() != null ? order.getUser().getUsername() : "Khách vãng lai",
                order.getShippingRecipientName(),
                order.getShippingPhone(),
                toOrderStatusLabel(order),
                order.getTotal(),
                order.getShippingMethod() == null ? "Tiêu chuẩn" : order.getShippingMethod().label(),
                order.getItems() == null ? 0 : order.getItems().size(),
                buildDestinationLabel(order),
                order.getPaymentMethod() == null ? "Chưa xác định" : toPaymentLabel(order.getPaymentMethod().name())
        );
    }

    private String buildDestinationLabel(Order order) {
        String city = order.getShippingCity() == null ? "" : order.getShippingCity().trim();
        String province = order.getShippingProvince() == null ? "" : order.getShippingProvince().trim();
        if (!city.isBlank() && !province.isBlank()) {
            return city + ", " + province;
        }
        return !city.isBlank() ? city : province;
    }

    private String toProductStatusLabel(Product product) {
        if (product.getProductStatus() == null) {
            return "Chưa rõ";
        }
        return switch (product.getProductStatus()) {
            case ACTIVE -> "Đang bán";
            case DRAFT -> "Nháp";
            case FEW_LEFT -> "Sắp hết hàng";
            case OUT_OF_STOCK -> "Hết hàng";
            case DISCONTINUED -> "Ngừng kinh doanh";
        };
    }

    private String toOrderStatusLabel(Order order) {
        if (order.getOrderStatus() == null) {
            return "Chưa rõ";
        }
        return switch (order.getOrderStatus()) {
            case PENDING_PAYMENT -> "Chờ thanh toán";
            case PAID -> "Đã thanh toán";
            case PAYMENT_FAILED -> "Thanh toán lỗi";
            case PROCESSING -> "Đang xử lý";
            case SHIPPING -> "Đang giao";
            case DELIVERED -> "Đã giao";
            case CANCELLED -> "Đã hủy";
        };
    }

    private String toPaymentLabel(String paymentMethod) {
        return switch (paymentMethod.toUpperCase(Locale.ROOT)) {
            case "COD" -> "Thanh toán khi nhận hàng";
            case "VNPAY" -> "VNPay";
            default -> paymentMethod;
        };
    }
}
