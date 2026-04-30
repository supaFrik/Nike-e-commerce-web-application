<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<c:set var="adminSidebarSubtitle" value="Bảng điều khiển quản trị" />
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="adminSidebarFooterTitle" value="Production-first shell" />
<c:set var="adminSidebarFooterCopy" value="Pure JSP frontend, CSS, and local JS. The structure stays close to a real admin without backend coupling yet." />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nike Admin Suite</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="dashboard">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Overview / Dashboard</div>
                    <h1 class="page-title">Admin Dashboard</h1>
                    <p class="page-subtitle">Theo dõi số liệu thật về sản phẩm, đơn hàng và danh mục trong cùng một màn hình.</p>
                </div>
                <div class="action-row">
                    <a class="btn btn-light" href="${env}/admin/product/list">Open inventory</a>
                    <a class="btn btn-dark" href="${env}/admin/product/add">Create product</a>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <section class="hero hero-single">
                <div class="hero-visual">
                    <img src="${env}/images/admin/feature/nike-just-do-it.webp" alt="Nike campaign hero">
                </div>
                <div class="hero-overlay"></div>
                <div class="hero-copy">
                    <div class="hero-kicker">Nike back office</div>
                    <h2>Yesterday you said tomorrow.</h2>
                    <p>Admin Dashboard Demo</p>
                    <div class="hero-actions">
                        <a class="btn btn-dark" href="${env}/admin/product/add">Create product</a>
                        <a class="btn btn-light" href="${env}/admin/order/list">Review orders</a>
                        <a class="btn btn-light" href="${env}/admin/category/list">Manage taxonomy</a>
                    </div>
                    <div class="kpi-grid hero-inline-stats" id="heroStats"></div>
                </div>
            </section>

            <section class="panel-grid">
                <article class="panel span-6">
                    <div class="panel-header">
                        <div>
                            <h3>Đi nhanh tới tác vụ chính</h3>
                            <p>Mở nhanh các màn hình thao tác chính trong admin.</p>
                        </div>
                    </div>
                    <div class="list-stack" id="priorityActions"></div>
                </article>
                <article class="panel span-6">
                    <div class="panel-header">
                        <div>
                            <h3>Tóm tắt hệ thống</h3>
                            <p>Số liệu tổng hợp lấy trực tiếp từ backend.</p>
                        </div>
                    </div>
                    <div class="kpi-grid" id="dashboardSummary"></div>
                </article>
            </section>
            <div class="footer-space"></div>
        </div>
    </main>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/dashboard.js"></script>
</body>
</html>
