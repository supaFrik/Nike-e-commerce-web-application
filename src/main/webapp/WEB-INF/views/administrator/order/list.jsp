<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="adminSidebarSubtitle" value="Fulfillment operations shell" />
<c:set var="adminSidebarFooterTitle" value="Fulfillment board" />
<c:set var="adminSidebarFooterCopy" value="The interface should stay calm even when the order volume does not." />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="orders">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Commerce / Order management</div>
                    <h1 class="page-title">Quản lý đơn hàng</h1>
                    <p class="page-subtitle">Theo dõi hàng đợi đơn hàng từ dữ liệu thật của hệ thống.</p>
                </div>
                <div class="action-row">
                    <a class="btn btn-light" href="${env}/admin/product/list">Inventory</a>
                    <a class="btn btn-dark" href="${env}/admin">Về dashboard</a>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <section class="stat-grid" id="orderKpis"></section>

            <section class="order-layout">
                <article class="panel">
                    <div class="panel-header">
                        <div>
                            <h3>Hàng đợi đơn hàng</h3>
                            <p>Lọc danh sách, chọn đơn và xem nhanh thông tin giao nhận thực tế.</p>
                        </div>
                        <span class="pill pill-red" id="orderCountPill">0 visible</span>
                    </div>
                    <div class="toolbar">
                        <div class="chip-row">
                            <button class="chip is-active" type="button" data-status="Tất cả">Tất cả</button>
                            <button class="chip" type="button" data-status="Chờ thanh toán">Chờ thanh toán</button>
                            <button class="chip" type="button" data-status="Đang xử lý">Đang xử lý</button>
                            <button class="chip" type="button" data-status="Đang giao">Đang giao</button>
                            <button class="chip" type="button" data-status="Đã giao">Đã giao</button>
                            <button class="chip" type="button" data-status="Đã hủy">Đã hủy</button>
                        </div>
                        <input class="input" id="orderSearch" type="search" placeholder="Tìm mã đơn, khách hàng, trạng thái" style="max-width:340px;">
                    </div>
                    <div class="priority-list" id="priorityList" style="margin-top:18px;"></div>
                    <div class="table-wrap" style="margin-top:18px;">
                        <table class="table">
                            <thead>
                            <tr><th>Mã đơn</th><th>Thời gian</th><th>Khách hàng</th><th>Trạng thái</th><th>Tổng tiền</th><th>Vận chuyển</th><th>Số dòng</th><th>Điểm đến</th></tr>
                            </thead>
                            <tbody id="orderRows"></tbody>
                        </table>
                    </div>
                </article>
                <aside class="order-detail">
                    <div class="panel panel-dark">
                        <div class="panel-header">
                            <div>
                                <h3>Đơn hàng đang chọn</h3>
                                <p>Thông tin giao nhận và thanh toán của đơn hiện tại.</p>
                            </div>
                            <span class="badge neutral" id="selectedOrderBadge">Đang tải</span>
                        </div>
                        <div id="selectedOrderCard"></div>
                    </div>
                    <div class="panel">
                        <div class="panel-header">
                            <div>
                                <h3>Operator notes</h3>
                                <p>Static helper prompts for the current fulfillment step.</p>
                            </div>
                        </div>
                        <div class="priority-list">
                            <div class="priority-item"><strong>Verify address before label creation</strong><span>Double-check express orders first to prevent avoidable reroutes.</span></div>
                            <div class="priority-item"><strong>Escalate returned orders visibly</strong><span>Refund-related items should remain separated from the normal processing queue.</span></div>
                            <div class="priority-item"><strong>Keep packaging promises realistic</strong><span>Do not move to shipped status before the parcel is actually handed off.</span></div>
                        </div>
                    </div>
                </aside>
            </section>
            <div class="footer-space"></div>
        </div>
    </main>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/orders.js"></script>
</body>
</html>
