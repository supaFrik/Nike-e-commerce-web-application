<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="currentStep" value="3" />
<c:set var="orderAccessible" value="true" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng - Nike</title>

    <jsp:include page="/WEB-INF/views/user/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/user/imported/order.jsp" />
</head>
<body class="order-page-body">
<jsp:include page="/WEB-INF/views/user/layout/order-process.jsp" />

<main class="order-page">
    <c:choose>
        <c:when test="${empty orderPage}">
            <section class="order-section-card">
                <span class="section-eyebrow">Order Detail</span>
                <h1 class="section-title">Backend cho trang order chưa được nối</h1>
                <p class="section-copy">
                    JSP này đã sẵn sàng cho luồng mới <strong>/orders/{orderId}</strong>. Phần controller/service/repository cần trả
                    model <code>orderPage</code> đúng contract ở đầu file để trang render đầy đủ.
                </p>
                <div class="empty-state">
                    Gợi ý: sau khi checkout thành công hoặc VNPay return, hãy redirect sang
                    <strong>${env}/orders/{orderId}</strong> thay vì quay lại checkout.
                </div>
            </section>
        </c:when>
        <c:otherwise>
            <c:set var="customerName" value="${empty orderPage.shipping.recipientName ? 'Khách hàng Nike' : orderPage.shipping.recipientName}" />

            <section class="receipt-shell">
                <div class="receipt-header">
                    <div class="receipt-header-copy">
                        <p class="receipt-eyebrow">Order confirmed</p>
                        <h1 class="receipt-title">
                            <c:out value="${empty orderPage.successHeadline ? 'Thank you for your order' : orderPage.successHeadline}" />
                        </h1>
                    </div>

                    <a href="${env}/" class="receipt-home-link">Về trang chủ</a>
                </div>

                <div class="receipt-layout">
                    <section class="receipt-main-card">
                        <div class="receipt-meta-bar">
                            <div class="receipt-meta-item">
                                <span>Order Number</span>
                                <strong>V<c:out value="${orderPage.orderCode}" /></strong>
                            </div>
                            <div class="receipt-meta-item">
                                <span>Order Date</span>
                                <strong><c:out value="${orderPage.placedAtLabel}" /></strong>
                            </div>
                            <div class="receipt-meta-item">
                                <span>Customer</span>
                                <strong><c:out value="${customerName}" /></strong>
                            </div>
                        </div>

                        <p class="receipt-intro">
                            <c:out value="${empty orderPage.successDescription ? 'Đơn hàng của bạn đã được ghi nhận. Bạn có thể dùng các thông tin dưới đây để đối chiếu khi cần hỗ trợ.' : orderPage.successDescription}" />
                        </p>

                        <div class="receipt-info-grid">
                            <article class="receipt-info-block">
                                <h2>Shipping Address</h2>
                                <p class="receipt-info-strong"><c:out value="${customerName}" /></p>
                                <p><c:out value="${orderPage.shipping.fullAddress}" /></p>
                                <c:if test="${not empty orderPage.shipping.phone}">
                                    <p><c:out value="${orderPage.shipping.phone}" /></p>
                                </c:if>
                            </article>

                            <article class="receipt-info-block">
                                <h2>Payment Method</h2>
                                <p class="receipt-info-strong">
                                    <c:choose>
                                        <c:when test="${orderPage.payment.online}">
                                            <c:out value="${orderPage.payment.providerLabel}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${orderPage.paymentMethodLabel}" />
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <p><c:out value="${orderPage.paymentSummaryLabel}" /></p>
                                <c:if test="${not empty orderPage.payment.transactionNo}">
                                    <p>Mã giao dịch: <c:out value="${orderPage.payment.transactionNo}" /></p>
                                </c:if>
                            </article>

                            <article class="receipt-info-block">
                                <h2>Billing Address</h2>
                                <p class="receipt-info-strong"><c:out value="${customerName}" /></p>
                                <p><c:out value="${orderPage.shipping.fullAddress}" /></p>
                                <c:if test="${not empty orderPage.shipping.phone}">
                                    <p><c:out value="${orderPage.shipping.phone}" /></p>
                                </c:if>
                            </article>

                            <article class="receipt-info-block">
                                <h2>Shipping Method</h2>
                                <p class="receipt-info-strong"><c:out value="${orderPage.shipping.shippingMethodLabel}" /></p>
                                <p>Trạng thái đơn: <c:out value="${orderPage.orderStatusLabel}" /></p>
                                <p>Trạng thái thanh toán: <c:out value="${orderPage.paymentStatusLabel}" /></p>
                            </article>
                        </div>
                    </section>

                    <aside class="receipt-summary-card">
                        <div class="receipt-summary-head">
                            <h2>Order Summary</h2>
                        </div>

                        <div class="receipt-summary-list">
                            <div class="receipt-summary-row">
                                <span>SUBTOTAL:</span>
                                <strong><fmt:formatNumber value="${orderPage.pricing.subtotal}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                            <div class="receipt-summary-row">
                                <span>SHIPPING:</span>
                                <strong><fmt:formatNumber value="${orderPage.pricing.shippingFee}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                            <div class="receipt-summary-row">
                                <span>DISCOUNTS:</span>
                                <strong>-<fmt:formatNumber value="${orderPage.pricing.discount}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                            <div class="receipt-summary-row receipt-summary-total">
                                <span>ORDER TOTAL:</span>
                                <strong><fmt:formatNumber value="${orderPage.pricing.total}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                        </div>

                        <p class="receipt-summary-note">Thuế được tính trong quá trình thanh toán.</p>

                        <div class="receipt-items-section">
                            <h3>Items Ordered</h3>

                            <c:choose>
                                <c:when test="${empty orderPage.items}">
                                    <div class="empty-state">Backend chưa trả item snapshot cho đơn hàng này.</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="receipt-item-list">
                                        <c:forEach var="item" items="${orderPage.items}">
                                            <article class="receipt-item">
                                                <div class="receipt-item-image">
                                                    <c:choose>
                                                        <c:when test="${not empty item.imageUrl}">
                                                            <img src="${item.imageUrl}" alt="${item.productName}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${env}/images/products/default-product.avif" alt="${item.productName}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>

                                                <div class="receipt-item-copy">
                                                    <h4>
                                                        <c:choose>
                                                            <c:when test="${not empty item.productUrl}">
                                                                <a href="${item.productUrl}"><c:out value="${item.productName}" /></a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:out value="${item.productName}" />
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </h4>
                                                    <p>SKU: <c:out value="${item.sku}" /></p>
                                                    <c:if test="${not empty item.size or not empty item.color}">
                                                        <p>
                                                            <c:if test="${not empty item.size}">Size <c:out value="${item.size}" /></c:if>
                                                            <c:if test="${not empty item.size and not empty item.color}">
                                                                <span class="receipt-inline-separator">|</span>
                                                            </c:if>
                                                            <c:if test="${not empty item.color}">
                                                                <c:out value="${item.color}" />
                                                            </c:if>
                                                        </p>
                                                    </c:if>
                                                    <p>QTY <c:out value="${item.quantity}" /></p>
                                                </div>

                                                <div class="receipt-item-price">
                                                    <strong><fmt:formatNumber value="${item.lineTotal}" type="number" maxFractionDigits="0" />₫</strong>
                                                </div>
                                            </article>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </aside>
                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main>
<script src="${env}/js/customer/pages/order.js"></script>
</body>
</html>
