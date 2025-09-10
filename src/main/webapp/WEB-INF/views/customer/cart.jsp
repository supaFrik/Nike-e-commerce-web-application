<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - Nike</title>

    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/cart.jsp" />
    
</head>
<body>
    <!-- Cart Progress -->
    <div class="cart-progress">
        <div class="container">
            <div class="progress-steps">
                <div class="step active">
                    <a href="${env}/cart">
                        <div class="step-icon">
                            <span>1</span>
                        </div>
                        <span class="step-label">Cart</span>
                    </a>
                </div>
                <div class="step">
                    <a href="${env}/checkout">
                        <div class="step-icon">
                            <span>2</span>
                        </div>
                        <span class="step-label">Checkout</span>
                    </a>
                </div>
                <div class="step">
                    <a href="${env}/order">
                        <div class="step-icon">
                            <span>3</span>
                        </div>
                        <span class="step-label">Order</span>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Main Cart Content -->
    <main class="cart-main">
        <div class="container">
            <div class="cart-header">
                <h1 class="cart-title">Shopping Cart</h1>
                <span class="item-count" id="itemCount">
                    ${fn:length(cartItems)} product
                    <c:if test="${fn:length(cartItems) > 1}">s</c:if>
                </span>
            </div>

            <div class="cart-content" id="cartContent">
                <!-- Empty Cart State -->
                <c:if test="${empty cartItems}">
                    <div class="empty-cart" id="emptyCart">
                        <div class="empty-cart-icon">
                            <i class="fas fa-shopping-bag"></i>
                        </div>
                        <h2>Your bag is empty</h2>
                        <p>Once you add something to your bag - it will appear here. Ready to get started?</p>
                        <a href="${env}/products" class="btn btn-primary">Get Started</a>
                    </div>
                </c:if>

                <!-- Cart -->
                <div class="cart-layout" id="cartLayout" style="display: block;">
                    <div class="cart-items">
                        <div class="cart-items-list" id="cartItemsList">
                            <c:forEach var="cartItem" items="${cartItems}">
                                <div class="cart-item" data-product-id="${cartItem.product.id}">
                                    <div class="item-image" onclick="viewProduct('${cartItem.product.id}')">
                                        <img src="${env}/images/products/${cartItem.product.imageUrl}" alt="${cartItem.product.name}">
                                    </div>
                                    <div class="item-details">
                                        <div class="item-header">
                                            <div class="item-info">
                                                <h3>${cartItem.product.name}</h3>
                                                <p class="item-category">${cartItem.product.category.name}</p>
                                                <p class="item-size">Size: ${cartItem.size}</p>
                                                <p class="item-color">Color: ${cartItem.color}</p>
                                            </div>
                                            <div class="item-price">
                                                <fmt:formatNumber value="${cartItem.product.price}" type="currency" />
                                            </div>
                                        </div>
                                        <div class="item-actions">
                                            <div class="quantity-controls">
                                                    <button type="button" name="quantity" value="${cartItem.quantity - 1}" class="quantity-btn"
                                                    onclick="updateCart(${cartItem.product.id}, ${cartItem.quantity - 1}, '${cartItem.size}', '${cartItem.color}')">
                                                        <i class="fas fa-minus"></i>
                                                    </button>

                                                    <input type="text" class="quantity-input" value="${cartItem.quantity}" id="qty-${cartItem.product.id}" readonly>

                                                    <button type="button" name="quantity" value="${cartItem.quantity + 1}" class="quantity-btn"
                                                    onclick="updateCart(${cartItem.product.id}, ${cartItem.quantity + 1}, '${cartItem.size}', '${cartItem.color}')">
                                                        <i class="fas fa-plus"></i>
                                                    </button>

                                            </div>
                                            <a href="javascript:void(0)" class="remove-btn" title="Remove item"
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
                                Continue Shopping
                            </a>
                        </div>
                    </div>

                    <!-- Cart Summary -->
                    <div class="cart-summary">
                        <h3 class="summary-title">Order Summary</h3>
                        
                        <!-- Promo Code Section -->
                        <div class="promo-section">
                            <div class="promo-input">
                                <input type="text" placeholder="Promo code" class="promo-code" id="promoCode">
                                <button type="button" class="apply-promo-btn" onclick="applyPromoCode()">Apply</button>
                            </div>
                        </div>

                        <!-- Order Totals -->
                        <div class="order-summary">
                            <div class="summary-row">
                                <span>Subtotal</span>
                                <span id="subtotalAmount"><fmt:formatNumber value="${subtotal}" type="currency"/></span>
                            </div>
                            <div class="summary-row">
                                <span>Estimated Shipping</span>
                                <span id="shippingAmount"><fmt:formatNumber value="${shipping}" type="currency"/></span>
                            </div>
                            <div class="summary-row">
                                <span>Estimated Tax</span>
                                <span id="taxAmount"><fmt:formatNumber value="${tax}" type="currency"/></span>
                            </div>
                            <div class="summary-row discount-row" id="discountRow" style="display: none;">
                                <span>Discount</span>
                                <span id="discountAmount"><fmt:formatNumber value="${discount}" type="currency"/></span>
                            </div>
                            <div class="summary-row total-row">
                                <span>Total</span>
                                <span id="totalAmount"><fmt:formatNumber value="${total}" type="currency"/></span>
                            </div>
                        </div>

                        <!-- Checkout Button -->
                        <a href="${env}/checkout">
                            <button class="checkout-btn" onclick="proceedToCheckout()" id="checkoutBtn">
                                Checkout
                            </button>
                        </a>
                        <!-- Payment Methods -->
                        <div class="payment-methods">
                            <p>We accept:</p>
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
                                <strong>Secure Checkout</strong>
                                <p>Your payment information is encrypted and secure.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>   
        </div>
    </main>

    <script src="${env}/js/add-to-cart.js"></script>
    <script src="${env}/js/update-button.js"></script>
</body>
</html>