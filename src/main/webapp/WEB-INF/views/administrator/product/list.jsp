<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="adminSidebarSubtitle" value="Catalog operations shell" />
<c:set var="adminSidebarFooterTitle" value="Inventory cockpit" />
<c:set var="adminSidebarFooterCopy" value="Fast scan, clear category filters, and a steady quick-view panel for product decisions." />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Inventory</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="product-list">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Catalog / Product inventory</div>
                    <h1 class="page-title">Product Inventory</h1>
                    <p class="page-subtitle">Tra cứu tồn kho, giá bán, màu sắc và kích cỡ từ dữ liệu thật trong hệ thống.</p>
                </div>
                <div class="action-row">
                    <a class="btn btn-light" href="${env}/admin/category/list">Categories</a>
                    <a class="btn btn-dark" href="${env}/admin/product/add">Add product</a>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <section class="panel">
                <div class="panel-header">
                    <div>
                            <h3>Bộ lọc kho hàng</h3>
                            <p>Lọc và xem nhanh theo dữ liệu thật lấy từ backend quản trị.</p>
                    </div>
                    <span class="pill pill-red" id="inventoryTotal">0 products</span>
                </div>
                <div class="toolbar">
                    <div class="chip-row" style="flex:1 1 420px;">
                        <input class="input" id="searchInventory" type="search" placeholder="Search by product name, category, or status">
                    </div>
                    <div class="chip-row">
                        <span class="pill pill-success">124 SKUs</span>
                        <span class="pill pill-warn">12 low stock</span>
                    </div>
                </div>
                    <div class="chip-row" id="categoryFilters" style="margin-top:18px;"></div>
            </section>

            <section class="panel-grid">
                <article class="panel span-8">
                    <div class="panel-header">
                        <div>
                            <h3>Danh sách sản phẩm</h3>
                            <p>Chọn sản phẩm để xem nhanh tồn kho, giá và biến thể thực tế.</p>
                        </div>
                    </div>
                    <div class="product-grid" id="productGrid"></div>
                </article>
                <aside class="panel span-4 quick-view" id="quickView"></aside>
            </section>
            <div class="footer-space"></div>
        </div>
    </main>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/product-list.js"></script>
</body>
</html>
