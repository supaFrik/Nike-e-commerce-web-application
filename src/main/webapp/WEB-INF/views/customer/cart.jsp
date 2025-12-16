<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - Nike</title>

    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/cart.jsp" />
    
</head>
<body>
    <!-- Order Process -->
    <jsp:include page="/WEB-INF/views/customer/layout/order-process.jsp" />

    <!-- Main Cart Content -->
    <main class="cart-main">
        <div class="container">
            <div class="cart-header">
                <h1 class="cart-title">Giỏ hàng</h1>
                <span class="item-count" id="itemCount">
                    <span id="cartTotalCount">${fn:length(cartItems)} sản phẩm</span>
                </span>
            </div>

            <div class="cart-content" id="cartContent">
                <!-- Empty Cart State -->
                <c:if test="${empty cartItems}">
                    <div class="empty-cart" id="emptyCart">
                        <div class="empty-cart-icon">
                            <i class="fas fa-shopping-bag"></i>
                        </div>
                        <h2>Giỏ hàng của bạn đang trống</h2>
                        <p>Khi bạn thêm sản phẩm vào giỏ — chúng sẽ hiển thị ở đây. Bắt đầu mua sắm ngay bây giờ?</p>
                        <a href="${env}/products" class="btn btn-primary">Bắt đầu</a>
                    </div>
                </c:if>

                <!-- Cart -->
                <div class="cart-layout" id="cartLayout" style="display: block;">
                    <div class="cart-items">
                        <div class="cart-items-list" id="cartItemsList">
                            <c:forEach var="cartItem" items="${cartItems}">
                                <div class="cart-item" data-product-id="${cartItem.product.id}">
                                    <div class="item-image" onclick="viewProduct('${cartItem.product.id}')">
                                        <img src="${env}${cartItem.product.imageUrl}" alt="${cartItem.product.name}">
                                    </div>
                                    <div class="item-details">
                                        <div class="item-header">
                                            <div class="item-info">
                                                <h3>${cartItem.product.name}</h3>
                                                <p class="item-category">${cartItem.product.category.name}</p>
                                                <p class="item-size">Kích thước: ${cartItem.size}</p>
                                                <p class="item-color">Màu: ${cartItem.color}</p>
                                            </div>
                                            <div class="item-price">
                                                <fmt:formatNumber value="${cartItem.product.price}" type="currency" />
                                            </div>
                                        </div>
                                        <div class="item-actions">
                                            <div class="quantity-controls">
                                                    <button type="button" class="quantity-btn"
                                                    onclick="changeQuantity(${cartItem.product.id}, -1, '${cartItem.size}', '${cartItem.color}')">
                                                        <i class="fas fa-minus"></i>
                                                    </button>

                                                    <input type="text" class="quantity-input" value="${cartItem.quantity}" id="qty-${cartItem.product.id}" readonly>

                                                    <button type="button" class="quantity-btn"
                                                    onclick="changeQuantity(${cartItem.product.id}, 1, '${cartItem.size}', '${cartItem.color}')">
                                                        <i class="fas fa-plus"></i>
                                                    </button>

                                            </div>
                                            <a href="javascript:void(0)" class="remove-btn" title="Xóa sản phẩm"
                                            onclick="removeCart(${cartItem.product.id}, '${cartItem.size}', '${cartItem.color}')">
                                                <i class="fas fa-trash-alt"></i>
                                            </a>
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

                    <!-- Cart Summary -->
                    <div class="cart-summary">
                        <h3 class="summary-title">Tóm tắt đơn hàng</h3>

                        <!-- Promo Code Section -->
                        <div class="promo-section">
                            <div class="promo-input">
                                <input type="text" placeholder="Mã giảm giá" class="promo-code" id="promoCode">
                                <button type="button" class="apply-promo-btn" onclick="applyPromoCode()">Áp dụng</button>
                            </div>
                        </div>

                        <!-- Order Totals -->
                        <div class="order-summary">
                            <div class="summary-row">
                                <span>Tạm tính</span>
                                <span id="subtotalAmount"><fmt:formatNumber value="${subtotal}" type="currency"/></span>
                            </div>
                            <div class="summary-row discount-row" id="discountRow" style="display: none;">
                                <span>Giảm giá</span>
                                <span id="discountAmount"><fmt:formatNumber value="${discount}" type="currency"/></span>
                            </div>
                            <div class="summary-row total-row">
                                <span>Tổng</span>
                                <span id="totalAmount"><fmt:formatNumber value="${total}" type="currency"/></span>
                            </div>
                        </div>
                        <div class="cart-note" style="font-size:0.85rem;color:#666;margin-top:6px;">
                            Phí vận chuyển và thuế sẽ được tính tại bước thanh toán.
                        </div>

                        <!-- Checkout Button -->
                        <a href="${env}/checkout">
                            <button class="checkout-btn" onclick="proceedToCheckout()" id="checkoutBtn">
                                Thanh toán
                            </button>
                        </a>
                        <!-- Payment Methods -->
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

                        <!-- Security Info -->
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

    <script src="${env}/js/customer/components/add-to-cart.js"></script>
    <script src="${env}/js/admin/components/update-button.js"></script>
    <script>
    // Fetch and update cart total count from backend
    function updateCartCount() {
        fetch('/api/cart/count')
            .then(res => res.json())
            .then(data => {
                if (data.itemCount !== undefined) {
                    document.getElementById('cartTotalCount').innerHTML = data.itemCount + ' sản phẩm';
                }
            });
    }
    // Call on page load
    updateCartCount();
    // Optionally, call updateCartCount() after cart actions (add, update, remove)
    </script>
</body>
</html>