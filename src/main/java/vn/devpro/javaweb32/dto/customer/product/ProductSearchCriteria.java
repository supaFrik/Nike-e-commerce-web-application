package vn.devpro.javaweb32.dto.customer.product;

import java.math.BigDecimal;

public class ProductSearchCriteria {
    private String category;
    private Boolean saleOnly;
    private String size; // variant size
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sort; // newest | price_asc | price_desc
    private int page = 1;
    private int pageSize = 24;
    private String q;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Boolean getSaleOnly() { return saleOnly; }
    public void setSaleOnly(Boolean saleOnly) { this.saleOnly = saleOnly; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = Math.max(1, page); }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { if (pageSize > 0 && pageSize <= 200) this.pageSize = pageSize; }
    public String getQ() { return q; }
    public void setQ(String q) { this.q = q; }
}
