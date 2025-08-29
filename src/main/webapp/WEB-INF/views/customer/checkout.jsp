<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Nike</title>

    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/checkout.jsp" />
</head>
<body>
    <!-- Checkout Progress -->
    <div class="checkout-progress">
        <div class="container">
            <div class="progress-steps">
                <div class="step completed">
                    <a href="${env}/cart">
                        <div class="step-icon">
                            <span>1</span>
                        </div>
                        <span class="step-label">Cart</span>
                    </a>
                </div>
                <div class="step active">
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

    <!-- Main Checkout Content -->
    <main class="checkout-main">
        <div class="container">
            <div class="checkout-content">
                <!-- Left Column - Shipping Information -->
                <div class="checkout-left">
                    <div class="checkout-section">
                        <h1 class="checkout-title">Checkout</h1>
                        
                        <!-- Shipping Information -->
                        <div class="section-card">
                            <h2 class="section-title">Shipping Information</h2>
                            
                            <!-- Delivery Options -->
                            <div class="delivery-options">
                                <div class="delivery-option selected">
                                    <input type="radio" name="delivery" id="delivery-standard" checked>
                                    <label for="delivery-standard">
                                        <i class="fas fa-truck"></i>
                                        <div class="delivery-info">
                                            <span class="delivery-type">Standard Delivery</span>
                                            <span class="delivery-time">5-7 business days</span>
                                        </div>
                                        <span class="delivery-price">$5.00</span>
                                    </label>
                                </div>
                                
                                <div class="delivery-option">
                                    <input type="radio" name="delivery" id="delivery-express">
                                    <label for="delivery-express">
                                        <i class="fas fa-bolt"></i>
                                        <div class="delivery-info">
                                            <span class="delivery-type">Express Delivery</span>
                                            <span class="delivery-time">2-3 business days</span>
                                        </div>
                                        <span class="delivery-price">$15.00</span>
                                    </label>
                                </div>
                                
                                <div class="delivery-option">
                                    <input type="radio" name="delivery" id="pickup">
                                    <label for="pickup">
                                        <i class="fas fa-store"></i>
                                        <div class="delivery-info">
                                            <span class="delivery-type">Pick up</span>
                                            <span class="delivery-time">Available today</span>
                                        </div>
                                        <span class="delivery-price">Free</span>
                                    </label>
                                </div>
                            </div>

                            <!-- Shipping Form -->
                            <form class="shipping-form">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="firstName">First name <span class="required">*</span></label>
                                        <input type="text" id="firstName" name="firstName" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="lastName">Last name <span class="required">*</span></label>
                                        <input type="text" id="lastName" name="lastName" required>
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label for="email">Email address <span class="required">*</span></label>
                                    <input type="email" id="email" name="email" required>
                                </div>
                                
                                <div class="form-group">
                                    <label for="phone">Phone number <span class="required">*</span></label>
                                    <div class="phone-input">
                                        <select class="country-code">
                                            <option value="+1">🇺🇸 +1</option>
                                            <option value="+84">🇻🇳 +84</option>
                                            <option value="+44">🇬🇧 +44</option>
                                        </select>
                                        <input type="tel" id="phone" name="phone" placeholder="Enter phone number" required>
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label for="country">Country <span class="required">*</span></label>
                                    <select id="country" name="country" required>
                                        <option value="">Choose country</option>
                                        <option value="US">United States</option>
                                        <option value="VN">Vietnam</option>
                                        <option value="UK">United Kingdom</option>
                                        <option value="CA">Canada</option>
                                    </select>
                                </div>
                                
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="city">City <span class="required">*</span></label>
                                        <input type="text" id="city" name="city" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="state">State</label>
                                        <input type="text" id="state" name="state">
                                    </div>
                                    <div class="form-group">
                                        <label for="zipCode">ZIP Code</label>
                                        <input type="text" id="zipCode" name="zipCode">
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label for="address">Street Address <span class="required">*</span></label>
                                    <input type="text" id="address" name="address" placeholder="Enter your address" required>
                                </div>
                                
                                <div class="checkbox-group">
                                    <input type="checkbox" id="terms" name="terms" required>
                                    <label for="terms">
                                        I have read and agree to the <a href="${env}/terms" class="link">Terms and Conditions</a>
                                    </label>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Right Column - Order Review -->
                <div class="checkout-right">
                    <div class="order-summary">
                        <h2 class="section-title">Review your order</h2>
                        
                        <!-- Order Items -->
                        <div class="order-items">
                            <div class="order-item">
                                <div class="item-image">
                                    <img src="${env}/customer/img/products/AIR+MAX+90+G.avif" alt="Nike Air Max 90" onclick="viewProduct('air-max-90')">
                                </div>
                                <div class="item-details">
                                    <h3 class="item-name">
                                        <a href="${env}/product-detail" class="product-link">Nike Air Max 90</a>
                                    </h3>
                                    <p class="item-category">Men's Shoes</p>
                                    <p class="item-specs">White/Dusty Cactus/Hyper Pink</p>
                                    <p class="item-size">Size: US M 8 / W 9.5</p>
                                    <p class="item-quantity">Quantity: 1</p>
                                </div>
                                <div class="item-price">$120.00</div>
                            </div>
                            
                            <div class="order-item">
                                <div class="item-image">
                                    <img src="${env}/customer/img/products/AIR+FORCE+1+'07.avif" alt="Nike Air Force 1" onclick="viewProduct('air-force-1')">
                                </div>
                                <div class="item-details">
                                    <h3 class="item-name">
                                        <a href="${env}/product-detail" class="product-link">Nike Air Force 1 '07</a>
                                    </h3>
                                    <p class="item-category">Men's Shoes</p>
                                    <p class="item-specs">White/White</p>
                                    <p class="item-size">Size: US M 9</p>
                                    <p class="item-quantity">Quantity: 1</p>
                                </div>
                                <div class="item-price">$110.00</div>
                            </div>
                        </div>
                        
                        <!-- Discount Code -->
                        <div class="discount-section">
                            <div class="discount-input">
                                <input type="text" placeholder="Discount code" class="discount-code">
                                <button type="button" class="apply-btn">Apply</button>
                            </div>
                        </div>
                        
                        <!-- Order Totals -->
                        <div class="order-totals">
                            <div class="total-row">
                                <span>Subtotal</span>
                                <span>$230.00</span>
                            </div>
                            <div class="total-row">
                                <span>Shipping</span>
                                <span>$5.00</span>
                            </div>
                            <div class="total-row">
                                <span>Discount</span>
                                <span class="discount">-$0.00</span>
                            </div>
                            <div class="total-row final-total">
                                <span>Total</span>
                                <span>$235.00</span>
                            </div>
                        </div>
                        
                        <!-- Payment Button -->
                        <button class="pay-now-btn" onclick="proceedToPayment()">
                            <i class="fas fa-lock"></i>
                            Pay Now
                        </button>
                        
                        <!-- Security Info -->
                        <div class="security-info">
                            <i class="fas fa-shield-alt"></i>
                            <div class="security-text">
                                <strong>Secure Checkout - SSL Encrypted</strong>
                                <p>Your payment and personal information are always safe.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script src="${env}/customer/scripts/pages/main.js"></script>
    <script src="${env}/customer/scripts/pages/checkout.js"></script>
</body>
</html>