<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
Backend contract TODO:
- Controller should render this page at GET /orders/{orderId}
- Model attribute: orderPage
  - orderId
  - orderCode
  - successHeadline
  - successDescription
  - placedAtLabel
  - orderStatusLabel
  - paymentStatusLabel
  - paymentMethodLabel
  - paymentSummaryLabel
  - total
  - timelineSteps[{ label, description, completed, current }]
  - items[{ imageUrl, productName, productUrl, sku, size, color, unitPrice, quantity, lineTotal }]
  - shipping{ recipientName, phone, fullAddress, shippingMethodLabel }
  - payment{ online, providerLabel, transactionNo, paymentTimeLabel, txnRef, description }
  - pricing{ subtotal, shippingFee, discount, total }
  - actions[{ label, href, kind, available, hint }]
  - paymentHistory[{ title, description, timestampLabel, success }]
--%>

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
            <section class="order-hero">
                <article class="order-hero-card">
                    <span class="order-pill">Order confirmed</span>
                    <h1 class="order-hero-title">
                        <c:out value="${empty orderPage.successHeadline ? 'Đặt hàng thành công.' : orderPage.successHeadline}" />
                    </h1>
                    <p class="order-hero-copy">
                        <c:out value="${empty orderPage.successDescription ? 'Trang này trả lời ngay bạn đã mua gì, đã thanh toán chưa, đơn đang ở đâu và bước tiếp theo là gì.' : orderPage.successDescription}" />
                    </p>

                    <div class="action-list" style="margin: 0 0 24px; max-width: 280px;">
                        <a href="${env}/" class="action-link secondary">
                            <div class="action-copy">
                                <strong>Quay về trang chính</strong>
                                <span>Tiếp tục khám phá sản phẩm và ưu đãi mới.</span>
                            </div>
                            <span>←</span>
                        </a>
                    </div>

                    <div class="order-questions">
                        <div class="order-question">
                            <span class="order-question-label">Tôi đã mua gì?</span>
                            <div class="order-question-value">
                                <c:out value="${fn:length(orderPage.items)}" /> sản phẩm trong đơn
                                <c:out value="${orderPage.orderCode}" />
                            </div>
                        </div>
                        <div class="order-question">
                            <span class="order-question-label">Tôi đã trả tiền chưa?</span>
                            <div class="order-question-value">
                                <c:out value="${orderPage.paymentSummaryLabel}" />
                            </div>
                        </div>
                        <div class="order-question">
                            <span class="order-question-label">Đơn đang ở đâu?</span>
                            <div class="order-question-value">
                                <c:out value="${orderPage.orderStatusLabel}" />
                            </div>
                        </div>
                        <div class="order-question">
                            <span class="order-question-label">Tôi có thể làm gì tiếp theo?</span>
                            <div class="order-question-value">
                                <c:choose>
                                    <c:when test="${not empty orderPage.actions}">
                                        <c:out value="${orderPage.actions[0].label}" />
                                    </c:when>
                                    <c:otherwise>Kiểm tra lịch sử và trạng thái đơn hàng</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </article>

                <aside class="order-summary-card">
                    <div>
                        <p class="order-summary-label">Đơn hàng của bạn</p>
                        <h2 class="order-summary-code">#<c:out value="${orderPage.orderCode}" /></h2>
                    </div>

                    <dl class="order-summary-list">
                        <div class="order-summary-row">
                            <dt>Ngày đặt</dt>
                            <dd><c:out value="${orderPage.placedAtLabel}" /></dd>
                        </div>
                        <div class="order-summary-row">
                            <dt>Trạng thái đơn</dt>
                            <dd><c:out value="${orderPage.orderStatusLabel}" /></dd>
                        </div>
                        <div class="order-summary-row">
                            <dt>Trạng thái thanh toán</dt>
                            <dd><c:out value="${orderPage.paymentStatusLabel}" /></dd>
                        </div>
                        <div class="order-summary-row">
                            <dt>Phương thức thanh toán</dt>
                            <dd><c:out value="${orderPage.paymentMethodLabel}" /></dd>
                        </div>
                    </dl>

                    <div class="order-summary-total">
                        <p class="order-summary-label">Tổng thanh toán</p>
                        <span class="amount">
                            <fmt:formatNumber value="${orderPage.total}" type="number" maxFractionDigits="0" />₫
                        </span>
                    </div>
                </aside>
            </section>

            <section class="order-grid">
                <div class="order-main">
                    <article class="order-section-card">
                        <span class="section-eyebrow">Order Overview</span>
                        <h2 class="section-title">Thông tin tổng quan đơn hàng</h2>
                        <p class="section-copy">Phần đầu tiên phải trả lời ngay đơn này là gì, đã thanh toán ra sao và tổng giá trị hiện tại.</p>

                        <div class="overview-grid">
                            <div class="overview-card">
                                <span class="label">Mã đơn hàng</span>
                                <div class="value">#<c:out value="${orderPage.orderCode}" /></div>
                            </div>
                            <div class="overview-card">
                                <span class="label">Ngày đặt</span>
                                <div class="value"><c:out value="${orderPage.placedAtLabel}" /></div>
                            </div>
                            <div class="overview-card">
                                <span class="label">Tổng tiền</span>
                                <div class="value"><fmt:formatNumber value="${orderPage.total}" type="number" maxFractionDigits="0" />₫</div>
                            </div>
                            <div class="overview-card">
                                <span class="label">Trạng thái đơn hàng</span>
                                <div class="value"><c:out value="${orderPage.orderStatusLabel}" /></div>
                            </div>
                            <div class="overview-card">
                                <span class="label">Trạng thái thanh toán</span>
                                <div class="value"><c:out value="${orderPage.paymentStatusLabel}" /></div>
                            </div>
                            <div class="overview-card">
                                <span class="label">Phương thức thanh toán</span>
                                <div class="value"><c:out value="${orderPage.paymentMethodLabel}" /></div>
                            </div>
                        </div>
                    </article>

                    <article class="order-section-card">
                        <span class="section-eyebrow">Timeline</span>
                        <h2 class="section-title">Đơn đang ở đâu</h2>
                        <p class="section-copy">Khách không nên phải đoán trạng thái. Timeline phải cho biết luồng xử lý đang đứng ở bước nào.</p>

                        <div class="timeline">
                            <c:forEach var="step" items="${orderPage.timelineSteps}" varStatus="status">
                                <div class="timeline-step ${step.completed ? 'completed' : ''} ${step.current ? 'current' : ''}">
                                    <div class="timeline-marker">${status.index + 1}</div>
                                    <div class="timeline-content">
                                        <h3 class="timeline-title"><c:out value="${step.label}" /></h3>
                                        <p class="timeline-text"><c:out value="${step.description}" /></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </article>

                    <article class="order-section-card">
                        <span class="section-eyebrow">Items</span>
                        <h2 class="section-title">Danh sách sản phẩm</h2>
                        <p class="section-copy">Snapshot của item phải hiện rõ ảnh, SKU, size, color, đơn giá, số lượng và thành tiền.</p>

                        <c:choose>
                            <c:when test="${empty orderPage.items}">
                                <div class="empty-state">Backend chưa trả item snapshot cho đơn hàng này.</div>
                            </c:when>
                            <c:otherwise>
                                <div class="item-list">
                                    <c:forEach var="item" items="${orderPage.items}">
                                        <div class="item-card">
                                            <div class="item-image">
                                                <c:choose>
                                                    <c:when test="${not empty item.imageUrl}">
                                                        <img src="${item.imageUrl}" alt="${item.productName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${env}/images/products/default-product.avif" alt="${item.productName}">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                            <div>
                                                <h3 class="item-title">
                                                    <c:choose>
                                                        <c:when test="${not empty item.productUrl}">
                                                            <a href="${item.productUrl}"><c:out value="${item.productName}" /></a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${item.productName}" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </h3>

                                                <div class="item-meta">
                                                    <div class="item-meta-block">
                                                        <span class="label">SKU</span>
                                                        <span class="value"><c:out value="${item.sku}" /></span>
                                                    </div>
                                                    <div class="item-meta-block">
                                                        <span class="label">Size</span>
                                                        <span class="value"><c:out value="${item.size}" /></span>
                                                    </div>
                                                    <div class="item-meta-block">
                                                        <span class="label">Color</span>
                                                        <span class="value"><c:out value="${item.color}" /></span>
                                                    </div>
                                                    <div class="item-meta-block">
                                                        <span class="label">Số lượng</span>
                                                        <span class="value"><c:out value="${item.quantity}" /></span>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="item-pricing">
                                                <div class="item-price-row">
                                                    <span>Đơn giá</span>
                                                    <strong><fmt:formatNumber value="${item.unitPrice}" type="number" maxFractionDigits="0" />₫</strong>
                                                </div>
                                                <div class="item-price-row">
                                                    <span>Thành tiền</span>
                                                    <strong><fmt:formatNumber value="${item.lineTotal}" type="number" maxFractionDigits="0" />₫</strong>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </article>
                </div>

                <aside class="order-side">
                    <article class="order-section-card">
                        <span class="section-eyebrow">Shipping</span>
                        <h2 class="section-title">Thông tin giao hàng</h2>
                        <div class="detail-stack">
                            <div class="detail-block">
                                <h4>Người nhận</h4>
                                <p><c:out value="${orderPage.shipping.recipientName}" /></p>
                            </div>
                            <div class="detail-block">
                                <h4>Số điện thoại</h4>
                                <p><c:out value="${orderPage.shipping.phone}" /></p>
                            </div>
                            <div class="detail-block">
                                <h4>Địa chỉ đầy đủ</h4>
                                <p><c:out value="${orderPage.shipping.fullAddress}" /></p>
                            </div>
                            <div class="detail-block">
                                <h4>Phương thức vận chuyển</h4>
                                <p><c:out value="${orderPage.shipping.shippingMethodLabel}" /></p>
                            </div>
                        </div>
                    </article>

                    <article class="order-section-card">
                        <span class="section-eyebrow">Payment</span>
                        <h2 class="section-title">Thông tin thanh toán</h2>
                        <div class="detail-stack">
                            <c:choose>
                                <c:when test="${orderPage.payment.online}">
                                    <div class="detail-block">
                                        <h4>Nhà cung cấp</h4>
                                        <p><c:out value="${orderPage.payment.providerLabel}" /></p>
                                    </div>
                                    <div class="detail-block">
                                        <h4>Mã giao dịch</h4>
                                        <p><c:out value="${orderPage.payment.transactionNo}" /></p>
                                    </div>
                                    <div class="detail-block">
                                        <h4>Thời gian thanh toán</h4>
                                        <p><c:out value="${orderPage.payment.paymentTimeLabel}" /></p>
                                    </div>
                                    <div class="detail-block">
                                        <h4>Transaction Reference</h4>
                                        <p><c:out value="${orderPage.payment.txnRef}" /></p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="detail-block">
                                        <h4>Phương thức thanh toán</h4>
                                        <p>Thanh toán khi nhận hàng</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <c:if test="${not empty orderPage.payment.description}">
                                <div class="detail-block">
                                    <h4>Ghi chú thanh toán</h4>
                                    <p><c:out value="${orderPage.payment.description}" /></p>
                                </div>
                            </c:if>
                        </div>
                    </article>

                    <article class="order-section-card">
                        <span class="section-eyebrow">Pricing</span>
                        <h2 class="section-title">Chi tiết giá</h2>
                        <div class="price-breakdown">
                            <div class="price-row">
                                <span>Tạm tính</span>
                                <strong><fmt:formatNumber value="${orderPage.pricing.subtotal}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                            <div class="price-row">
                                <span>Phí vận chuyển</span>
                                <strong><fmt:formatNumber value="${orderPage.pricing.shippingFee}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                            <div class="price-row discount">
                                <span>Giảm giá</span>
                                <strong>-<fmt:formatNumber value="${orderPage.pricing.discount}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                            <div class="price-row total">
                                <span>Tổng thanh toán</span>
                                <strong><fmt:formatNumber value="${orderPage.pricing.total}" type="number" maxFractionDigits="0" />₫</strong>
                            </div>
                        </div>
                    </article>

                    <article class="order-section-card">
                        <span class="section-eyebrow">Next Actions</span>
                        <h2 class="section-title">Bạn có thể làm gì tiếp theo</h2>
                        <div class="action-list">
                            <c:choose>
                                <c:when test="${empty orderPage.actions}">
                                    <div class="empty-state">Backend chưa map action khả dụng theo state machine của đơn hàng.</div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="action" items="${orderPage.actions}">
                                        <a href="${action.available ? action.href : '#'}" class="action-link ${empty action.kind ? 'secondary' : action.kind} ${action.available ? '' : 'disabled'}">
                                            <div class="action-copy">
                                                <strong><c:out value="${action.label}" /></strong>
                                                <span><c:out value="${action.hint}" /></span>
                                            </div>
                                            <span>→</span>
                                        </a>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </article>

                    <article class="order-section-card">
                        <span class="section-eyebrow">Audit Trail</span>
                        <h2 class="section-title">Lịch sử giao dịch</h2>
                        <div class="history-list">
                            <c:choose>
                                <c:when test="${empty orderPage.paymentHistory}">
                                    <div class="empty-state">Backend chưa trả lịch sử payment transaction cho order này.</div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="event" items="${orderPage.paymentHistory}">
                                        <div class="history-item ${event.success ? 'success' : ''}">
                                            <div class="history-dot"></div>
                                            <div class="history-card">
                                                <h4><c:out value="${event.title}" /></h4>
                                                <p><c:out value="${event.description}" /></p>
                                                <span class="history-time"><c:out value="${event.timestampLabel}" /></span>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </article>
                </aside>
            </section>
        </c:otherwise>
    </c:choose>
</main>
<script src="${env}/js/customer/pages/order.js"></script>
</body>
</html>
