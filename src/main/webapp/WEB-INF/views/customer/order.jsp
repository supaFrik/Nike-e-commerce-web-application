<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Order Confirmation - Nike</title>
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
</head>
<body>
<div class="checkout-progress">
    <div class="container">
        <div class="progress-steps">
            <div class="step completed"><a href="${env}/cart"><div class="step-icon"><span>1</span></div><span class="step-label">Cart</span></a></div>
            <div class="step completed"><a href="${env}/checkout"><div class="step-icon"><span>2</span></div><span class="step-label">Checkout</span></a></div>
            <div class="step active"><div class="step-icon"><span>3</span></div><span class="step-label">Order</span></div>
        </div>
    </div>
</div>
<main class="order-main">
    <div class="container">
        <h1>Your Order</h1>
        <c:if test="${!hasItems}">
            <p>No items found for this order. <a href='${env}/products'>Browse products</a>.</p>
        </c:if>
        <c:if test="${hasItems}">
            <p>Thank you! Your order has been received.</p>
            <div class="order-summary">
                <!-- Placeholder: integrate real order details after persistence implemented -->
                <p>This is a placeholder order confirmation page. Implement order persistence to show real data.</p>
            </div>
        </c:if>
        <div style="margin-top:2rem;">
            <a href='${env}/' class='btn btn-primary'>Continue Shopping</a>
        </div>
    </div>
</main>
</body>
</html>

