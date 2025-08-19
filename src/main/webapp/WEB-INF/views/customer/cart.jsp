<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - Nike</title>
        <link rel="icon" href="${env}/customer/img/e0891c394d4f7b7c09e783e29df07505.png">
    
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
                <span class="item-count" id="itemCount">2 items</span>
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
                            <!-- Sample Cart Item 1 -->
                            <div class="cart-item" data-index="0">
                                <div class="item-image" onclick="viewProduct('air-jordan-1-low')">
                                    <img src="${env}/customer/img/products/AIR+JORDAN+1+LOW.avif" alt="Air Jordan 1 Low">
                                </div>
                                <div class="item-details">
                                    <div class="item-header">
                                        <div class="item-info">
                                            <h3>Air Jordan 1 Low</h3>
                                            <p class="item-category">Men's Shoes</p>
                                            <p class="item-size">Size: US M 10</p>
                                            <p class="item-color">Color: White/Black</p>
                                        </div>
                                        <div class="item-price">$110.00</div>
                                    </div>
                                    <div class="item-actions">
                                        <div class="quantity-controls">
                                            <button class="quantity-btn" onclick="updateQuantity(0, -1)">
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <input type="text" class="quantity-input" value="1" 
                                                   onchange="setQuantity(0, this.value)" readonly>
                                            <button class="quantity-btn" onclick="updateQuantity(0, 1)">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                        <button class="remove-btn" onclick="removeItem(0)" title="Remove item">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <!-- Sample Cart Item 2 -->
                            <div class="cart-item" data-index="1">
                                <div class="item-image" onclick="viewProduct('nike-blazer-mid-77')">
                                    <img src="${env}/customer/img/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Blazer Mid '77 Vintage">
                                </div>
                                <div class="item-details">
                                    <div class="item-header">
                                        <div class="item-info">
                                            <h3>Nike Blazer Mid '77 Vintage</h3>
                                            <p class="item-category">Men's Shoes</p>
                                            <p class="item-size">Size: US M 9.5</p>
                                            <p class="item-color">Color: White/Black</p>
                                        </div>
                                        <div class="item-price">$100.00</div>
                                    </div>
                                    <div class="item-actions">
                                        <div class="quantity-controls">
                                            <button class="quantity-btn" onclick="updateQuantity(1, -1)">
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <input type="text" class="quantity-input" value="2" 
                                                   onchange="setQuantity(1, this.value)" readonly>
                                            <button class="quantity-btn" onclick="updateQuantity(1, 1)">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                        <button class="remove-btn" onclick="removeItem(1)" title="Remove item">
                                            <i class="fas fa-trash-alt"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
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
                                <span id="subtotalAmount">$310.00</span>
                            </div>
                            <div class="summary-row">
                                <span>Estimated Shipping</span>
                                <span id="shippingAmount">$5.00</span>
                            </div>
                            <div class="summary-row">
                                <span>Estimated Tax</span>
                                <span id="taxAmount">$24.80</span>
                            </div>
                            <div class="summary-row discount-row" id="discountRow" style="display: none;">
                                <span>Discount</span>
                                <span id="discountAmount">-$0.00</span>
                            </div>
                            <div class="summary-row total-row">
                                <span>Total</span>
                                <span id="totalAmount">$339.80</span>
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