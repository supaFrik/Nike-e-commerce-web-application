package vn.devpro.javaweb32.dto.administrator;

import java.util.Date;

public class ProductSearch {

    private String keyword;
    private String status;
    private Integer categoryId;
    private Date beginDate;
    private Date endDate;

    // Pagination fields
    private int currentPage = 1;
    private int itemOnPage = 10;
    private int totalItems = 0;
    private int totalPages = 0;

    // Constructors
    public ProductSearch() {
        this.status = "In Order";
        this.categoryId = 0;
        this.currentPage = 1;
        this.itemOnPage = 10;
    }

    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getItemOnPage() {
        return itemOnPage;
    }

    public void setItemOnPage(int itemOnPage) {
        this.itemOnPage = itemOnPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / itemOnPage);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getOffset() {
        return (currentPage - 1) * itemOnPage;
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }

    @Override
    public String toString() {
        return "ProductSearch{" +
                "keyword='" + keyword + '\'' +
                ", status=" + status +
                ", categoryId=" + categoryId +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", currentPage=" + currentPage +
                ", itemOnPage=" + itemOnPage +
                ", totalItems=" + totalItems +
                ", totalPages=" + totalPages +
                '}';
    }
}
