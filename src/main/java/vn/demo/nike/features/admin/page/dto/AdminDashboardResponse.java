package vn.demo.nike.features.admin.page.dto;

import java.math.BigDecimal;

public record AdminDashboardResponse(
        long productCount,
        long categoryCount,
        long orderCount,
        long lowStockProductCount,
        BigDecimal totalRevenue
) {
}
