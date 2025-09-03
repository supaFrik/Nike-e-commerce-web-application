<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                <span class="item-count" id="itemCount">${fn:length(cartItems)} item${fn:length(cartItems) != 1 ? 's' : ''}</span>
            </div>

            <div class="cart-content" id="cartContent">
                <!-- Empty Cart State -->
                <div class="empty-cart" id="emptyCart" style="display: none;">
                    <div class="empty-cart-icon">
                        <i class="fas fa-shopping-bag"></i>
                    </div>
                    <h2>Your bag is empty</h2>
                    <p>Once you add something to your bag - it will appear here. Ready to get started?</p>
                    <a href="${env}/" class="btn btn-primary">Get Started</a>
                </div>

                <!-- Cart Items -->
                <div class="cart-layout" id="cartLayout" style="display: block;">
                    <div class="cart-items">
                        <div class="cart-items-list" id="cartItemsList">
                            <c:forEach var="item" items="${cartItems}">
                                <div class="cart-item">
                                    <div class="item-image" onclick="viewProduct('${item.product.slug}')">
                                        <img src="${env}/customer/img/products/${item.product.image}" alt="${item.product.name}">
                                    </div>
                                    <div class="item-details">
                                        <div class="item-header">
                                            <div class="item-info">
                                                <h3>${item.product.name}</h3>
                                                <p class="item-category">${item.product.category.name}</p>
                                                <p class="item-size">Size: ${item.size}</p>
                                                <p class="item-color">Color: ${item.color}</p>
                                            </div>
                                            <div class="item-price">
                                                <fmt:formatNumber value="${item.product.price}" type="currency" />
                                            </div>
                                        </div>
                                        <div class="item-actions">
                                            <div class="quantity-controls">
                                                <form action="${env}/cart/update/${item.product.id}" method="post">
                                                    <button type="submit" name="quantity" value="${item.quantity - 1}" class="quantity-btn">
                                                        <i class="fas fa-minus"></i>
                                                    </button>
                                                    <input type="text" class="quantity-input" value="${item.quantity}" readonly>
                                                    <button type="submit" name="quantity" value="${item.quantity + 1}" class="quantity-btn">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                </form>
                                            </div>
                                            <a href="${env}/cart/remove/${item.product.id}" class="remove-btn">
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

    <script src="${env}/customer/scripts/pages/main.js"></script>
    <script src="${env}/customer/scripts/pages/cart.js"></script>
</body>
</html>