<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="adminSidebarSubtitle" value="Taxonomy control shell" />
<c:set var="adminSidebarFooterTitle" value="Taxonomy discipline" />
<c:set var="adminSidebarFooterCopy" value="Keep category language sharp. Product intent should stay obvious at a glance." />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Categories</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="category-list">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Catalog / Categories</div>
                    <h1 class="page-title">Categories</h1>
                    <p class="page-subtitle">Quản lý danh mục.</p>
                </div>
                <div class="action-row">
                    <a class="btn btn-light" href="${env}/admin/product/list">Inventory</a>
                    <a class="btn btn-dark" href="${env}/admin/category/add">Add category</a>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <section class="panel-grid">
                <article class="panel span-8">
                    <div class="panel-header">
                        <div>
                            <h3>Bảng danh mục</h3>
                            <p>Tìm kiếm theo tên danh mục và số lượng sản phẩm thực tế.</p>
                        </div>
                    </div>
                    <input class="input" id="categorySearch" type="search" placeholder="Tìm theo tên danh mục">
                    <div class="table-wrap" style="margin-top:16px;">
                        <table class="table">
                            <thead>
                            <tr><th>ID</th><th>Tên danh mục</th><th>Số sản phẩm</th></tr>
                            </thead>
                            <tbody id="categoryRows"></tbody>
                        </table>
                    </div>
                </article>
                <aside class="panel span-4">
                    <div class="panel-header">
                        <div>
                            <h3>Thao tác</h3>
                            <p>Dùng trang thêm hoặc sửa riêng để làm việc với dữ liệu thật.</p>
                        </div>
                    </div>
                    <div class="list-stack">
                        <div class="list-item">
                            <h4>Thêm danh mục mới</h4>
                            <p>Đi tới form đầy đủ để tạo danh mục bằng dữ liệu thật.</p>
                            <a class="btn btn-dark" href="${env}/admin/category/add">Mở trang thêm</a>
                        </div>
                        <div class="list-item">
                            <h4>Lưu ý</h4>
                            <p>Danh mục hiện tại chỉ có dữ liệu thật gồm tên và số sản phẩm liên kết. Không hiển thị slug hoặc trạng thái giả.</p>
                        </div>
                    </div>
                </aside>
            </section>
            <div class="footer-space"></div>
        </div>
    </main>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/categories.js"></script>
</body>
</html>
