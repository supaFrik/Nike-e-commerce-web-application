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
    <title>Thanh toán - Nike</title>

    <jsp:include page="/WEB-INF/views/user/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/user/imported/checkout.jsp" />
</head>
<body>
<jsp:include page="/WEB-INF/views/user/layout/order-process.jsp" />

<main class="checkout-main">
    <div class="container">
        <div class="checkout-content">
            <div class="checkout-left">
                <div class="checkout-section">
                    <h1 class="checkout-title">Thanh toán</h1>

                    <div class="section-card">
                        <h2 class="section-title">Phương thức vận chuyển</h2>
                        <div class="delivery-options">
                            <c:forEach var="sm" items="${shippingMethods}">
                                <c:set var="checked" value="${selectedShippingMethod.name() == sm.name()}" />
                                <div class="delivery-option ${checked ? 'selected' : ''}">
                                    <input
                                            type="radio"
                                            name="shippingMethod"
                                            id="delivery-${sm.name()}"
                                            value="${sm.name()}"
                                            data-cost="${sm.cost}"
                                            ${checked ? 'checked' : ''}>
                                    <label for="delivery-${sm.name()}">
                                        <i class="fas ${sm.name() == 'EXPRESS' ? 'fa-bolt' : (sm.name() == 'PICKUP' ? 'fa-store' : 'fa-truck')}"></i>
                                        <div class="delivery-info">
                                            <span class="delivery-type"><c:out value="${sm.displayName}" /></span>
                                            <span class="delivery-time">
                                                <c:choose>
                                                    <c:when test="${sm.deliveryDaysMin == 0 && sm.deliveryDaysMax <= 1}">
                                                        Cả ngày hôm nay
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${sm.deliveryDaysMin}" />-<c:out value="${sm.deliveryDaysMax}" /> ngày làm việc
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                        <span class="delivery-price">
                                            <c:choose>
                                                <c:when test="${sm.cost == 0}">Miễn phí</c:when>
                                                <c:otherwise><fmt:formatNumber value="${sm.cost}" type="currency" currencySymbol="â‚«" /></c:otherwise>
                                            </c:choose>
                                        </span>
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <form class="shipping-form" id="completeCheckoutForm">
                        <div class="section-card">
                            <h2 class="section-title">Thông tin giao hàng</h2>

                            <c:choose>
                                <c:when test="${empty addresses}">
                                    <div class="security-info" style="margin-top: 0;">
                                        <i class="fas fa-map-marker-alt"></i>
                                        <div class="security-text">
                                            <strong>Bạn chưa có địa chỉ giao hàng</strong>
                                            <p>Bạn vẫn có thể nhập thông tin giao hàng trực tiếp bên dưới. Hệ thống sẽ tạo địa chỉ mới cho tài khoản khi checkout.</p>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-group">
                                        <label for="addressId">Chọn địa chỉ nhận hàng</label>
                                        <select id="addressId" name="addressId">
                                            <option value="">Nhập địa chỉ mới</option>
                                            <c:forEach var="address" items="${addresses}">
                                                <option
                                                        value="${address.id}"
                                                        data-recipient="${empty address.recipientName ? currentUser.username : address.recipientName}"
                                                        data-line1="${address.line1}"
                                                        data-line2="${address.line2}"
                                                        data-city="${address.city}"
                                                        data-province="${address.province}"
                                                        data-postal="${address.postalCode}"
                                                        data-country="${address.country}"
                                                        data-phone="${address.phone}"
                                                        ${selectedAddress != null && selectedAddress.id == address.id ? 'selected' : ''}>
                                                    <c:out value="${empty address.recipientName ? currentUser.username : address.recipientName}" />
                                                    -
                                                    <c:out value="${address.line1}" />
                                                    <c:if test="${not empty address.city}">, <c:out value="${address.city}" /></c:if>
                                                    <c:if test="${not empty address.province}">, <c:out value="${address.province}" /></c:if>
                                                    <c:if test="${address.primaryAddress}"> (Mặc định)</c:if>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <div class="form-row">
                                <div class="form-group">
                                    <label>Tài khoản</label>
                                    <input type="text" value="${currentUser.username}" readonly>
                                </div>
                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="email" value="${currentUser.email}" readonly>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="recipientName">Tên người nhận <span class="required">*</span></label>
                                    <input type="text" id="recipientName" value="${selectedAddress != null && not empty selectedAddress.recipientName ? selectedAddress.recipientName : currentUser.username}" required>
                                </div>
                                <div class="form-group">
                                    <label for="phone">Số điện thoại<span class="required">*</span></label>
                                    <input type="text" id="phone" value="${selectedAddress != null ? selectedAddress.phone : ''}" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="line1">Địa chỉ<span class="required">*</span></label>
                                <input type="text" id="line1" value="${selectedAddress != null ? selectedAddress.line1 : ''}" required>
                            </div>

                            <div class="form-group">
                                <label for="line2">Địa chỉ bổ sung</label>
                                <input type="text" id="line2" value="${selectedAddress != null ? selectedAddress.line2 : ''}">
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="city">Thành phố<span class="required">*</span></label>
                                    <input type="text" id="city" value="${selectedAddress != null ? selectedAddress.city : ''}" required>
                                </div>
                                <div class="form-group">
                                    <label for="province">Tỉnh thành</label>
                                    <input type="text" id="province" value="${selectedAddress != null ? selectedAddress.province : ''}">
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="postalCode">Mã bưu điện</label>
                                    <input type="text" id="postalCode" value="${selectedAddress != null ? selectedAddress.postalCode : ''}">
                                </div>
                                <div class="form-group">
                                    <label for="country">Quốc gia<span class="required">*</span></label>
                                    <input type="text" id="country" value="${selectedAddress != null ? selectedAddress.country : ''}" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Địa chỉ đang chọn</label>
                                <div class="security-info" style="margin-top: 0;">
                                    <i class="fas fa-home"></i>
                                    <div class="security-text" id="selectedAddressSummary">
                                        <c:choose>
                                            <c:when test="${selectedAddress != null}">
                                                <strong><c:out value="${empty selectedAddress.recipientName ? currentUser.username : selectedAddress.recipientName}" /></strong>
                                                <p>
                                                    <c:out value="${selectedAddress.line1}" />
                                                    <c:if test="${not empty selectedAddress.line2}">, <c:out value="${selectedAddress.line2}" /></c:if>
                                                    <c:if test="${not empty selectedAddress.city}">, <c:out value="${selectedAddress.city}" /></c:if>
                                                    <c:if test="${not empty selectedAddress.province}">, <c:out value="${selectedAddress.province}" /></c:if>
                                                    <c:if test="${not empty selectedAddress.postalCode}">, <c:out value="${selectedAddress.postalCode}" /></c:if>
                                                    <c:if test="${not empty selectedAddress.country}">, <c:out value="${selectedAddress.country}" /></c:if>
                                                </p>
                                                <p><c:out value="${selectedAddress.phone}" /></p>
                                            </c:when>
                                            <c:otherwise>
                                                <strong>Địa chỉ giao hàng mới</strong>
                                                <p>Thông tin sẽ được lấy từ form bạn đã nhập</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="note">Ghi chú đơn hàng</label>
                                <textarea id="note" name="note" rows="4" maxlength="500"></textarea>
                            </div>

                            <div class="form-group">
                                <div id="checkoutError" class="field-error-msg" style="display:none;"></div>
                            </div>
                        </div>
                    </form>

                    <section class="section-card page-section">
                        <div class="container">
                            <div class="content">
                                <div class="layout">
                                    <div class="payment-form">
                                        <fieldset>
                                            <legend>Phương thức thanh toán</legend>
                                            <div class="options">
                                                <label class="option hidden">
                                                    <span class="label-text">
                                                        <img src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/visa.svg" class="icon-sm" alt="Visa" />
                                                        <img src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/mastercard.svg" class="icon-sm" alt="Mastercard" />
                                                        <span class="text-sm">Thẻ (demo UI)</span>
                                                    </span>
                                                <input type="radio" name="pay-method" id="pm-card" value="card" class="radio">
                                                </label>

                                                <label class="option">
                                                    <span class="label-text">
                                                        <img src="${env}/images/Bank/VNPay.png" class="icon-sm" alt="VNPay" />
                                                        <span class="text-sm">VNPay</span>
                                                    </span>
                                                    <input type="radio" name="pay-method" id="pm-vnpay" value="vnpay" class="radio">
                                                </label>

                                                <label class="option">
                                                    <span class="label-text">
                                                        <svg class="icon-sm" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M5 6h14a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2m3 8h8" />
                                                        </svg>
                                                        <span class="text-sm">Thanh toán khi nhận hàng</span>
                                                    </span>
                                                    <input type="radio" name="pay-method" id="pm-cod" value="cod" class="radio" checked>
                                                </label>
                                            </div>
                                        </fieldset>

                                        <div id="card-fields" class="fields-grid hidden">
                                            <div class="field">
                                                <label for="full_name" class="label">Họ và tên</label>
                                                <input type="text" id="full_name" class="input" value="${currentUser.username}" placeholder="NGUYEN VAN A">
                                            </div>
                                            <div class="field">
                                                <label for="card-number-input" class="label">Số thẻ</label>
                                                <input type="text" id="card-number-input" class="input" placeholder="xxxx-xxxx-xxxx-xxxx">
                                            </div>
                                        </div>

                                        <div id="vnpay-fields" class="fields-grid hidden">
                                            <div class="field">
                                                <label for="bank-code" class="label">Kênh thanh toán</label>
                                                <select id="bank-code" class="input">
                                                    <option value="" selected>Cổng thanh toán VNPay</option>
                                                    <option value="VNPAYQR">Ứng dung hỗ trợ VNPayQR</option>
                                                    <option value="VNBANK">ATM / Tài khoản nội địa</option>
                                                    <option value="INTCARD">Thẻ quốc tế</option>
                                                </select>
                                            </div>
                                            <div class="field">
                                                <label for="payment-language" class="label">Ngôn ngữ cổng thanh toán</label>
                                                <select id="payment-language" class="input">
                                                    <option value="vn" selected>Tiếng Việt</option>
                                                    <option value="en">English</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div id="card-brands" class="card-brands hidden">
                                            <img class="brand" src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/paypal.svg" alt="PayPal" />
                                            <img class="brand" src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/visa.svg" alt="Visa" />
                                            <img class="brand" src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/mastercard.svg" alt="Mastercard" />
                                        </div>

                                        <div class="checkbox-group">
                                            <input type="checkbox" id="terms" name="terms">
                                            <label for="terms">
                                                Tôi đã đọc và đồng ý với <a href="${env}/terms" class="link">Điều khoản và Điều kiện</a>
                                            </label>
                                        </div>

                                        <button
                                                id="primary-submit"
                                                type="button"
                                                class="btn btn-primary"
                                                <c:if test="${cart.itemCount == 0}">disabled</c:if>>
                                            Hoàn tất thanh toán
                                        </button>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>

            <div class="checkout-right">
                <div class="order-summary">
                    <h2 class="section-title">Kiểm tra đơn hàng</h2>

                    <div class="order-items">
                        <c:choose>
                            <c:when test="${empty cart.items}">
                                <div style="padding:1rem;color:#d00;font-weight:bold;">
                                    Giỏ hàng của bạn đang trống. <a href="${env}/products/list">Mua sắm</a>.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="item" items="${cart.items}">
                                    <div class="order-item">
                                        <div class="item-image">
                                            <c:choose>
                                                <c:when test="${not empty item.imageUrl}">
                                                    <img src="${env}${item.imageUrl}" alt="${item.productName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${env}/images/products/default-product.avif" alt="${item.productName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="item-details">
                                            <h3 class="item-name">
                                                <a href="${env}/product-detail?id=${item.productId}" class="product-link">${item.productName}</a>
                                            </h3>
                                            <p class="item-category">Danh mục: <c:out value="${item.categoryName}" /></p>
                                            <p class="item-specs">Màu: <c:out value="${item.colorName}" /></p>
                                            <p class="item-size">Kích thước: <c:out value="${item.size}" /></p>
                                            <p class="item-quantity">Số lượng: <c:out value="${item.quantity}" /></p>
                                        </div>
                                        <div class="item-price">
                                            <fmt:formatNumber value="${item.lineTotal}" type="currency" currencySymbol="đ" />
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="discount-section">
                        <div class="discount-input">
                            <input type="text" placeholder="Mã giảm giá" class="discount-code" disabled>
                            <button type="button" class="apply-btn" disabled>Áp dụng</button>
                        </div>
                    </div>

                    <div class="order-totals">
                        <input type="hidden" id="subtotalValue" value="${cart.subtotal}" />
                        <input type="hidden" id="discountValue" value="${cart.discount}" />
                        <div class="total-row">
                            <span>Tạm tính</span>
                            <span id="subtotalDisplay"><fmt:formatNumber value="${cart.subtotal}" type="currency" currencySymbol="đ" /></span>
                        </div>
                        <div class="total-row">
                            <span>Vận chuyển</span>
                            <span id="shippingCostDisplay"><fmt:formatNumber value="${selectedShippingMethod.cost}" type="currency" currencySymbol="đ" /></span>
                        </div>
                        <div class="total-row">
                            <span>Giảm giá</span>
                            <span class="discount" id="discountDisplay">-<fmt:formatNumber value="${cart.discount}" type="currency" currencySymbol="đ" /></span>
                        </div>
                        <div class="total-row final-total">
                            <span>Tổng</span>
                            <span id="totalDisplay"><fmt:formatNumber value="${cart.total + selectedShippingMethod.cost}" type="currency" currencySymbol="đ" /></span>
                        </div>
                    </div>

                    <div class="security-info">
                        <i class="fas fa-shield-alt"></i>
                        <div class="security-text">
                            <strong>Checkout đang dùng form giao hàng thật</strong>
                            <p>Thông tin người nhận và địa chỉ trên form sẽ được bảo mật tuyệt đối.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    window.checkoutPageConfig = {
        env: '${env}'
    };

    (function () {
        const shippingRadios = document.querySelectorAll('input[name="shippingMethod"]');
        const paymentRadios = document.querySelectorAll('input[name="pay-method"]');
        const subtotalValue = parseFloat(document.getElementById('subtotalValue')?.value || '0') || 0;
        const discountValue = parseFloat(document.getElementById('discountValue')?.value || '0') || 0;
        const shippingCostDisplay = document.getElementById('shippingCostDisplay');
        const totalDisplay = document.getElementById('totalDisplay');
        const primarySubmit = document.getElementById('primary-submit');
        const terms = document.getElementById('terms');
        const addressSelect = document.getElementById('addressId');
        const recipientNameInput = document.getElementById('recipientName');
        const phoneInput = document.getElementById('phone');
        const line1Input = document.getElementById('line1');
        const line2Input = document.getElementById('line2');
        const cityInput = document.getElementById('city');
        const provinceInput = document.getElementById('province');
        const postalCodeInput = document.getElementById('postalCode');
        const countryInput = document.getElementById('country');
        const noteInput = document.getElementById('note');
        const errorBox = document.getElementById('checkoutError');
        const cardFields = document.getElementById('card-fields');
        const vnpayFields = document.getElementById('vnpay-fields');
        const cardBrands = document.getElementById('card-brands');
        const bankCodeInput = document.getElementById('bank-code');
        const paymentLanguageInput = document.getElementById('payment-language');
        const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;
        const formatter = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' });

        function currentShippingRadio() {
            return document.querySelector('input[name="shippingMethod"]:checked');
        }

        function updateShippingSummary() {
            const selected = currentShippingRadio();
            const shippingCost = parseFloat(selected?.dataset.cost || '0') || 0;
            const total = subtotalValue + shippingCost - discountValue;

            document.querySelectorAll('.delivery-option').forEach((option) => option.classList.remove('selected'));
            selected?.closest('.delivery-option')?.classList.add('selected');

            if (shippingCostDisplay) {
                shippingCostDisplay.textContent = formatter.format(shippingCost);
            }
            if (totalDisplay) {
                totalDisplay.textContent = formatter.format(total);
            }
        }

        function currentPaymentMethod() {
            return document.querySelector('input[name="pay-method"]:checked')?.value || 'cod';
        }

        function updateSelectedAddressSummary() {
            const summary = document.getElementById('selectedAddressSummary');
            const selectedOption = addressSelect?.selectedOptions?.[0];
            if (!summary) {
                return;
            }

            if (selectedOption && selectedOption.value) {
                recipientNameInput.value = selectedOption.dataset.recipient || '';
                phoneInput.value = selectedOption.dataset.phone || '';
                line1Input.value = selectedOption.dataset.line1 || '';
                line2Input.value = selectedOption.dataset.line2 || '';
                cityInput.value = selectedOption.dataset.city || '';
                provinceInput.value = selectedOption.dataset.province || '';
                postalCodeInput.value = selectedOption.dataset.postal || '';
                countryInput.value = selectedOption.dataset.country || '';
            }

            const parts = [
                line1Input.value,
                line2Input.value,
                cityInput.value,
                provinceInput.value,
                postalCodeInput.value,
                countryInput.value
            ].filter(Boolean);

            summary.innerHTML = ''
                + '<strong>' + (recipientNameInput.value || 'Äá»‹a chá»‰ giao hÃ ng') + '</strong>'
                + '<p>' + (parts.join(', ') || 'ThÃ´ng tin sáº½ Ä‘Æ°á»£c láº¥y tá»« form báº¡n Ä‘ang nháº­p.') + '</p>'
                + '<p>' + (phoneInput.value || '') + '</p>';
        }

        function switchPaymentMode() {
            const mode = currentPaymentMethod();

            if (cardFields) {
                cardFields.classList.add('hidden');
            }
            if (vnpayFields) {
                vnpayFields.classList.toggle('hidden', mode !== 'vnpay');
            }
            if (cardBrands) {
                cardBrands.classList.add('hidden');
            }
            if (primarySubmit) {
                if (mode === 'vnpay') {
                    primarySubmit.textContent = 'Thanh toÃ¡n báº±ng VNPay';
                    return;
                }
                primarySubmit.textContent = mode === 'vnpay' ? 'Thanh toÃ¡n báº±ng VNPay QR' : 'HoÃ n táº¥t thanh toÃ¡n';
            }
        }

        function showError(message) {
            if (!errorBox) {
                return;
            }
            errorBox.textContent = message;
            errorBox.style.display = 'block';
        }

        function clearError() {
            if (!errorBox) {
                return;
            }
            errorBox.textContent = '';
            errorBox.style.display = 'none';
        }

        function validateShippingForm() {
            if (!recipientNameInput.value.trim()) return 'Vui lÃ²ng nháº­p ngÆ°á»i nháº­n.';
            if (!phoneInput.value.trim()) return 'Vui lÃ²ng nháº­p sá»‘ Ä‘iá»‡n thoáº¡i.';
            if (!line1Input.value.trim()) return 'Vui lÃ²ng nháº­p Ä‘á»‹a chá»‰.';
            if (!cityInput.value.trim()) return 'Vui lÃ²ng nháº­p thÃ nh phá»‘.';
            if (!countryInput.value.trim()) return 'Vui lÃ²ng nháº­p quá»‘c gia.';
            if (!terms?.checked) return 'Báº¡n cáº§n Ä‘á»“ng Ã½ Ä‘iá»u khoáº£n trÆ°á»›c khi Ä‘áº·t hÃ ng.';
            return null;
        }

        async function initiateVNPayPayment(orderId) {
            const headers = {};
            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            const formData = new URLSearchParams();
            if (bankCodeInput?.value) {
                formData.set('bankCode', bankCodeInput.value);
            }
            formData.set('language', paymentLanguageInput?.value || 'vn');

            const response = await fetch('${env}/api/payments/vnpay/orders/' + orderId, {
                method: 'POST',
                headers,
                body: formData
            });

            const data = await response.json().catch(() => ({}));
            if (!response.ok) {
                throw new Error(data.message || 'Khong the tao VNPay payment URL.');
            }

            if (!data.paymentUrl) {
                throw new Error('VNPay payment URL khong hop le.');
            }

            window.location.href = data.paymentUrl;
        }

        async function placeOrder() {
            clearError();

            const shippingMethod = currentShippingRadio()?.value;
            const paymentMethod = currentPaymentMethod();
            if (!shippingMethod) {
                showError('Vui lÃ²ng chá»n phÆ°Æ¡ng thá»©c váº­n chuyá»ƒn.');
                return;
            }

            const validationMessage = validateShippingForm();
            if (validationMessage) {
                showError(validationMessage);
                return;
            }

            if (primarySubmit) {
                primarySubmit.disabled = true;
            }

            try {
                const headers = {
                    'Content-Type': 'application/json'
                };
                if (csrfToken && csrfHeader) {
                    headers[csrfHeader] = csrfToken;
                }

                const response = await fetch('${env}/api/checkout', {
                    method: 'POST',
                    headers,
                    body: JSON.stringify({
                        paymentMethod,
                        shippingMethod,
                        addressId: addressSelect?.value ? Number(addressSelect.value) : null,
                        recipientName: recipientNameInput.value.trim(),
                        phone: phoneInput.value.trim(),
                        line1: line1Input.value.trim(),
                        line2: line2Input.value.trim(),
                        city: cityInput.value.trim(),
                        province: provinceInput.value.trim(),
                        postalCode: postalCodeInput.value.trim(),
                        country: countryInput.value.trim(),
                        note: noteInput?.value?.trim() || null
                    })
                });

                const data = await response.json().catch(() => ({}));
                if (!response.ok) {
                    showError(data.message || 'KhÃ´ng thá»ƒ hoÃ n táº¥t checkout.');
                    return;
                }

                if (!data.paymentRequired) {
                    if (!data.orderId) {
                        showError('Checkout thÃ nh cÃ´ng nhÆ°ng khÃ´ng nháº­n Ä‘Æ°á»£c mÃ£ Ä‘Æ¡n hÃ ng.');
                        return;
                    }
                    window.location.href = '${env}/orders/' + data.orderId;
                    return;
                }
                if (data.paymentRequired) {
                    if (paymentMethod !== 'vnpay') {
                        showError('He thong tra ve trang thai thanh toan khong phu hop.');
                        return;
                    }

                    if (data.paymentUrl) {
                        window.location.href = data.paymentUrl;
                        return;
                    }

                    await initiateVNPayPayment(data.orderId);
                    return;
                }
            } catch (error) {
                showError('KhÃ´ng thá»ƒ káº¿t ná»‘i tá»›i dá»‹ch vá»¥ checkout. Vui lÃ²ng thá»­ láº¡i.');
            } finally {
                if (primarySubmit) {
                    primarySubmit.disabled = false;
                }
            }
        }

        shippingRadios.forEach((radio) => radio.addEventListener('change', updateShippingSummary));
        paymentRadios.forEach((radio) => radio.addEventListener('change', switchPaymentMode));
        addressSelect?.addEventListener('change', updateSelectedAddressSummary);

        [
            recipientNameInput,
            phoneInput,
            line1Input,
            line2Input,
            cityInput,
            provinceInput,
            postalCodeInput,
            countryInput
        ].forEach((input) => input?.addEventListener('input', updateSelectedAddressSummary));

        primarySubmit?.addEventListener('click', function () {
            clearError();
            placeOrder();
        });

        updateShippingSummary();
        updateSelectedAddressSummary();
        switchPaymentMode();
    })();
</script>
</body>
</html>
