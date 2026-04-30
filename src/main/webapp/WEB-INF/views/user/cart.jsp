<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <title>Giỏ hàng - Nike</title>

    <jsp:include page="/WEB-INF/views/user/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/user/imported/cart.jsp" />
</head>
<body>
<div id="appRuntime" hidden data-app-ctx="${env}" data-csrf-token="${_csrf.token}" data-csrf-header="${_csrf.headerName}"></div>
<jsp:include page="/WEB-INF/views/user/layout/order-process.jsp" />

<main class="cart-main">
    <div class="container">
        <div class="cart-header">
            <h1 class="cart-title">Giỏ hàng</h1>
            <span class="item-count" id="itemCount">
                <span id="cartTotalCount">${cart.itemCount} sản phẩm</span>
            </span>
        </div>

        <div class="cart-content" id="cartContent">
            <div class="empty-cart" id="emptyCart" style="${empty cart.items ? 'display:block;' : 'display:none;'}">
                <div class="empty-cart-icon">
                    <i class="fas fa-shopping-bag"></i>
                </div>
                <h2>Giỏ hàng của bạn đang trống</h2>
                <p>Khi bạn thêm sản phẩm vào giỏ, chúng sẽ hiển thị ở đây. Bắt đầu mua sắm ngay bây giờ?</p>
                <a href="${env}/products/list" class="btn btn-primary">Bắt đầu</a>
            </div>

            <div class="cart-layout" id="cartLayout" style="${empty cart.items ? 'display:none;' : 'display:grid;'}">
                <div class="cart-items">
                    <div class="cart-items-list" id="cartItemsList">
                        <c:forEach var="cartItem" items="${cart.items}">
                            <div class="cart-item" data-cart-item-id="${cartItem.cartItemId}">
                                <div class="item-image" data-view-product="${cartItem.productId}">
                                    <img src="${env}${cartItem.imageUrl}" alt="${cartItem.productName}">
                                </div>
                                <div class="item-details">
                                    <div class="item-header">
                                        <div class="item-info">
                                            <h3>${cartItem.productName}</h3>
                                            <p class="item-category">${cartItem.categoryName}</p>
                                            <p class="item-size">Kích thước: ${cartItem.size}</p>
                                            <p class="item-color">Màu: ${cartItem.colorName}</p>
                                            <p class="item-color">Đơn giá: <fmt:formatNumber value="${cartItem.unitPrice}" type="currency" /></p>
                                            <p class="item-color">
                                                <c:choose>
                                                    <c:when test="${cartItem.active}">Tồn kho: ${cartItem.stock}</c:when>
                                                    <c:otherwise>Biến thể không còn hoạt động</c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                        <div class="item-price">
                                            <fmt:formatNumber value="${cartItem.lineTotal}" type="currency" />
                                        </div>
                                    </div>
                                    <div class="item-actions">
                                        <div class="quantity-controls">
                                            <button
                                                    type="button"
                                                    class="quantity-btn"
                                                    data-change-quantity="${cartItem.cartItemId}"
                                                    data-quantity-delta="-1"
                                                    <c:if test="${cartItem.quantity <= 1}">disabled</c:if>>
                                                <i class="fas fa-minus"></i>
                                            </button>

                                            <input
                                                    type="text"
                                                    class="quantity-input"
                                                    value="${cartItem.quantity}"
                                                    id="qty-${cartItem.cartItemId}"
                                                    readonly>

                                            <button
                                                    type="button"
                                                    class="quantity-btn"
                                                    data-change-quantity="${cartItem.cartItemId}"
                                                    data-quantity-delta="1"
                                                    <c:if test="${!cartItem.active || cartItem.quantity >= cartItem.stock}">disabled</c:if>>
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                        <button
                                                type="button"
                                                class="remove-btn"
                                                title="Xóa sản phẩm"
                                                data-remove-cart-item="${cartItem.cartItemId}">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="cart-actions">
                        <a href="${env}/" class="back-to-shop">
                            <i class="fas fa-arrow-left"></i>
                            Tiếp tục mua sắm
                        </a>
                    </div>
                </div>

                <div class="cart-summary">
                    <h3 class="summary-title">Tóm tắt đơn hàng</h3>

                    <div class="promo-section">
                        <div class="promo-input">
                            <input type="text" placeholder="Mã giảm giá" class="promo-code" id="promoCode">
                            <button type="button" class="apply-promo-btn" id="applyPromoBtn">Áp dụng</button>
                        </div>
                    </div>

                    <div class="order-summary">
                        <div class="summary-row">
                            <span>Tạm tính</span>
                            <span id="subtotalAmount"><fmt:formatNumber value="${cart.subtotal}" type="currency"/></span>
                        </div>
                        <div class="summary-row discount-row" id="discountRow" style="${cart.discount > 0 ? 'display:flex;' : 'display:none;'}">
                            <span>Giảm giá</span>
                            <span id="discountAmount"><fmt:formatNumber value="${cart.discount}" type="currency"/></span>
                        </div>
                        <div class="summary-row total-row">
                            <span>Tổng</span>
                            <span id="totalAmount"><fmt:formatNumber value="${cart.total}" type="currency"/></span>
                        </div>
                    </div>

                    <div class="cart-note" style="font-size:0.85rem;color:#666;margin-top:6px;">
                        Phí vận chuyển và thuế sẽ được tính tại bước thanh toán.
                    </div>

                    <a href="${env}/checkout" id="checkoutLink">
                        <button class="checkout-btn" id="checkoutBtn" <c:if test="${cart.itemCount == 0}">disabled</c:if>>
                            Thanh toán
                        </button>
                    </a>

                    <div class="payment-methods">
                        <p>Chúng tôi chấp nhận:</p>
                        <div class="payment-icons">
                            <i class="fab fa-cc-visa"></i>
                            <i class="fab fa-cc-mastercard"></i>
                            <i class="fab fa-cc-amex"></i>
                            <i class="fab fa-cc-paypal"></i>
                            <i class="fab fa-apple-pay"></i>
                        </div>
                    </div>

                    <div class="security-info">
                        <i class="fas fa-shield-alt"></i>
                        <div class="security-text">
                            <strong>Thanh toán an toàn</strong>
                            <p>Thông tin thanh toán của bạn được mã hóa và an toàn.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="${env}/js/common/runtime.js"></script>
<script src="${env}/js/customer/pages/cart.js"></script>
</body>
</html>
