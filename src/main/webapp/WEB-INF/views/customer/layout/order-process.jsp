<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/WEB-INF/views/common/variables.jsp" />

<c:choose>
    <c:when test="${not empty currentStep}">
        <c:set var="step" value="${currentStep}" />
    </c:when>
    <c:when test="${fn:contains(pageContext.request.requestURI,'/order')}">
        <c:set var="step" value="3" />
    </c:when>
    <c:when test="${fn:contains(pageContext.request.requestURI,'/checkout')}">
        <c:set var="step" value="2" />
    </c:when>
    <c:otherwise>
        <c:set var="step" value="1" />
    </c:otherwise>
</c:choose>

<!-- Compute CSS classes -->
<c:set var="cartClass" value="${step == 1 ? 'active' : (step > 1 ? 'completed' : '')}" />
<c:set var="checkoutClass" value="${step == 2 ? 'active' : (step > 2 ? 'completed' : (hasItems ? '' : 'disabled'))}" />
<c:set var="orderClass" value="${step == 3 ? (orderAccessible ? 'active' : 'disabled') : (step > 3 ? 'completed' : (orderAccessible ? '' : 'disabled'))}" />

<div class="checkout-progress">
    <div class="container">
        <div class="progress-steps">
            <!-- Step 1: Cart -->
            <div class="step ${cartClass}">
                <c:choose>
                    <c:when test="${step > 1}">
                        <a href="${env}/cart" aria-label="Go to cart step" class="step-link">
                            <div class="step-icon"><span>1</span></div>
                            <span class="step-label">Cart</span>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <div class="step-icon"><span>1</span></div>
                        <span class="step-label">Cart</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Step 2: Checkout -->
            <div class="step ${checkoutClass}">
                <c:choose>
                    <c:when test="${hasItems}">
                        <c:choose>
                            <c:when test="${step >= 2}">
                                <a href="${env}/checkout" aria-label="Go to checkout step" class="step-link">
                                    <div class="step-icon"><span>2</span></div>
                                    <span class="step-label">Checkout</span>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="step-icon" style="opacity:.5"><span>2</span></div>
                                <span class="step-label" style="opacity:.5;">Checkout</span>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <div class="step-icon" style="opacity:.5;cursor:not-allowed"><span>2</span></div>
                        <span class="step-label" style="opacity:.5;">Checkout</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Step 3: Order -->
            <div class="step ${orderClass}">
                <c:choose>
                    <c:when test="${orderAccessible}">
                        <c:choose>
                            <c:when test="${step == 3}">
                                <div class="step-icon"><span>3</span></div>
                                <span class="step-label">Order</span>
                            </c:when>
                            <c:otherwise>
                                <a href="${env}/order" aria-label="Go to order confirmation step" class="step-link">
                                    <div class="step-icon"><span>3</span></div>
                                    <span class="step-label">Order</span>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <div class="step-icon" style="opacity:.5;cursor:not-allowed"><span>3</span></div>
                        <span class="step-label" style="opacity:.5;">Order</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<style>
/* Progress Steps */
.cart-progress {
    background: var(--color-bg-primary);
    padding: var(--size-spacing-l) 0;
    border-bottom: var(--size-border-width-s) solid var(--color-border-tertiary);
}

.progress-steps {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: var(--size-spacing-xxxl);
    max-width: 500px;
    margin: 0 auto;
}

.step {
    display: flex;
    align-items: center;
    gap: var(--size-spacing-m);
    opacity: 0.4;
    transition: opacity var(--transition-duration-normal) var(--transition-timing-function-ease);
}

.step.active {
    opacity: 1;
}

.step a {
    display: flex;
    align-items: center;
    gap: var(--size-spacing-m);
    text-decoration: none;
    color: inherit;
    transition: all var(--transition-duration-normal) var(--transition-timing-function-ease);
}

.step a:hover {
    opacity: 0.8;
}

.step-icon {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: var(--font-size-xxs);
    font-weight: var(--font-weight-medium);
    background: var(--color-bg-secondary);
    color: var(--color-text-secondary);
    transition: all var(--transition-duration-normal) var(--transition-timing-function-ease);
}

.step.active .step-icon {
    background: var(--color-text-primary);
    color: var(--color-bg-primary);
}

.step-label {
    font-size: var(--font-size-xxs);
    font-weight: var(--font-weight-medium);
    color: var(--color-text-primary);
    text-transform: uppercase;
    letter-spacing: 0.5px;
}
/* Progress Steps */
.checkout-progress {
    background: var(--color-bg-primary);
    padding: var(--size-spacing-l) 0;
    border-bottom: var(--size-border-width-s) solid var(--color-border-tertiary);
}

.progress-steps {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: var(--size-spacing-xxxl);
    max-width: 500px;
    margin: 0 auto;
}

.step {
    display: flex;
    align-items: center;
    gap: var(--size-spacing-m);
    opacity: 0.4;
    transition: opacity var(--transition-duration-normal) var(--transition-timing-function-ease);
}

.step.completed,
.step.active {
    opacity: 1;
}

.step a {
    display: flex;
    align-items: center;
    gap: var(--size-spacing-m);
    text-decoration: none;
    color: inherit;
    transition: all var(--transition-duration-normal) var(--transition-timing-function-ease);
}

.step a:hover {
    opacity: 0.8;
}

.step-icon {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: var(--font-size-xxs);
    font-weight: var(--font-weight-medium);
    background: var(--color-bg-secondary);
    color: var(--color-text-secondary);
    transition: all var(--transition-duration-normal) var(--transition-timing-function-ease);
}

.step.active .step-icon,
.step.completed .step-icon {
    background: var(--color-text-primary);
    color: var(--color-bg-primary);
}

.step-label {
    font-size: var(--font-size-xxs);
    font-weight: var(--font-weight-medium);
    color: var(--color-text-primary);
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

</style>