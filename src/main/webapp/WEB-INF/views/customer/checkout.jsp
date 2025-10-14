<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <!-- Order Process -->
    <jsp:include page="/WEB-INF/views/customer/layout/order-process.jsp" />

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
                                <c:choose>
                                    <c:when test="${not empty shippingMethods}">
                                        <c:forEach var="sm" items="${shippingMethods}">
                                            <c:set var="checked" value="${shippingMethod == sm.name() ? 'checked' : ''}" />
                                            <c:set var="cost" value="${sm.cost}" />
                                            <c:set var="isFree" value="${cost == 0}" />
                                            <div class="delivery-option ${checked eq 'checked' ? 'selected' : ''}">
                                                <c:choose>
                                                    <c:when test="${sm.name() == 'EXPRESS'}"><c:set var="iconClass" value="fa-bolt"/></c:when>
                                                    <c:when test="${sm.name() == 'PICKUP'}"><c:set var="iconClass" value="fa-store"/></c:when>
                                                    <c:otherwise><c:set var="iconClass" value="fa-truck"/></c:otherwise>
                                                </c:choose>
                                                <input type="radio" name="shippingMethod" id="delivery-${sm.name()}" value="${sm.name()}" data-cost="${cost}" ${checked}>
                                                <label for="delivery-${sm.name()}">
                                                    <i class="fas ${iconClass}"></i>
                                                    <div class="delivery-info">
                                                        <span class="delivery-type"><c:out value='${sm.displayName}'/></span>
                                                        <span class="delivery-time">
                                                            <c:choose>
                                                                <c:when test="${sm.deliveryDaysMin == 0 && sm.deliveryDaysMax <= 1}">Available today</c:when>
                                                                <c:otherwise>
                                                                    <c:out value='${sm.deliveryDaysMin}'/>-<c:out value='${sm.deliveryDaysMax}'/> business days
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </div>
                                                    <span class="delivery-price">
                                                        <c:choose>
                                                            <c:when test="${isFree}">Miễn phí</c:when>
                                                            <c:otherwise><fmt:formatNumber value="${cost}" type="currency" currencySymbol="₫"/></c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </label>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="padding:0.75rem;color:#c00;font-weight:600;">No shipping methods available.</div>
                                    </c:otherwise>
                                </c:choose>
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
                                    <label for="terms" You must agree to the terms>
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
                            <c:choose>
                                <c:when test="${empty items}">
                                    <div style="padding:1rem;color:#d00;font-weight:bold;">Your cart is empty. <a href="${env}/products">Go shopping</a>.</div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${items}">
                                        <div class="order-item">
                                            <div class="item-image">
                                                <c:choose>
                                                    <c:when test="${not empty item.product.imageUrl}">
                                                        <img src="${env}${item.product.imageUrl}" alt="${item.product.name}" onclick="viewProduct('${item.product.id}')">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${env}/images/products/default-product.avif" alt="${item.product.name}" onclick="viewProduct('${item.product.id}')">
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="item-details">
                                                <h3 class="item-name">
                                                    <a href="${env}/product-detail?id=${item.product.id}" class="product-link">${item.product.name}</a>
                                                </h3>
                                                <p class="item-category">
                                                    Category: <c:out value="${item.product.category != null ? item.product.category.name : ''}"/>
                                                </p>
                                                <p class="item-specs">Color: ${item.color}</p>
                                                <p class="item-size">Size: ${item.size}</p>
                                                <p class="item-quantity">Quantity: ${item.quantity}</p>
                                            </div>
                                            <div class="item-price">
                                                <c:set var="unitPrice" value="${item.product.salePrice != null ? item.product.salePrice : item.product.price}"/>
                                                <fmt:formatNumber value="${unitPrice}" type="currency" currencySymbol="₫"/>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
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
                            <input type="hidden" id="subtotalValue" value="${subtotal}" />
                            <input type="hidden" id="discountValue" value="${discount}" />
                            <div class="total-row">
                                <span>Subtotal</span>
                                <span id="subtotalDisplay"><fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="₫"/></span>
                            </div>
                            <div class="total-row">
                                <span>Shipping</span>
                                <span id="shippingCostDisplay"><fmt:formatNumber value="${shippingCost}" type="currency" currencySymbol="₫"/></span>
                            </div>
                            <div class="total-row">
                                <span>Discount</span>
                                <span class="discount" id="discountDisplay">-<fmt:formatNumber value="${discount}" type="currency" currencySymbol="₫"/></span>
                            </div>
                            <div class="total-row final-total">
                                <span>Total</span>
                                <span id="totalDisplay"><fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/></span>
                            </div>
                        </div>
                        
                        <!-- Payment Button -->
                        <form action="${env}/checkout/complete" method="post" style="margin-top:1rem;" id="completeCheckoutForm">
                            <input type="hidden" name="shippingMethod" id="shippingMethodHidden" value="${shippingMethod}" />
                            <c:if test="${not empty _csrf}">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            </c:if>
                            <button class="pay-now-btn" type="submit">
                                <i class="fas fa-lock"></i>
                                Complete Checkout
                            </button>
                        </form>
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
    <script src="${env}/js/checkout-validation.js"></script>
    <script src="${env}/js/checkout-shipping.js"></script>
</body>
</html>