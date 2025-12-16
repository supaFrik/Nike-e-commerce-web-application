<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - Nike</title>

    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/checkout.jsp" />
    <link rel="stylesheet" href="${env}/css/customer/checkout/checkout.css">
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
                        <h1 class="checkout-title">Thanh toán</h1>

                        <!-- Shipping Information -->
                        <div class="section-card">
                            <h2 class="section-title">Thông tin vận chuyển</h2>
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
                                                                <c:when test="${sm.deliveryDaysMin == 0 && sm.deliveryDaysMax <= 1}">Có sẵn hôm nay</c:when>
                                                                <c:otherwise>
                                                                    <c:out value='${sm.deliveryDaysMin}'/>-<c:out value='${sm.deliveryDaysMax}'/> ngày làm việc
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
                                        <div style="padding:0.75rem;color:#c00;font-weight:600;">Không có phương thức vận chuyển khả dụng.</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <!-- Shipping Form -->
                            <form class="shipping-form" id="completeCheckoutForm" action="${env}/checkout/complete" method="post">
                                <input type="hidden" name="shippingMethod" id="shippingMethodHidden" value="${shippingMethod}" />
                                <c:if test="${not empty _csrf}">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                </c:if>
                                <input type="hidden" name="paymentMethod" id="paymentMethodHidden" value="card" />
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="firstName">Họ <span class="required">*</span></label>
                                        <input type="text" id="firstName" name="firstName" value="${customer.address != null && customer.address.recipientName != null ? fn:split(customer.address.recipientName,' ')[0] : customer.username}" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="lastName">Tên <span class="required">*</span></label>
                                        <input type="text" id="lastName" name="lastName" value="${customer.address != null && customer.address.recipientName != null && fn:length(fn:split(customer.address.recipientName,' '))>1 ? fn:split(customer.address.recipientName,' ')[fn:length(fn:split(customer.address.recipientName,' '))-1] : ''}" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="email">Email <span class="required">*</span></label>
                                    <input type="email" id="email" name="email" value="${customer.credential != null ? customer.credential.email : ''}" required>
                                </div>
                                <div class="form-group">
                                    <label for="phone">Số điện thoại <span class="required">*</span></label>
                                    <div class="phone-input">
                                        <select class="country-code" id="countryCodeSelect">
                                            <option value="+84" ${customer.address != null && customer.address.phone != null && customer.address.phone.startsWith('+84') ? 'selected' : ''}>🇻🇳 +84</option>
                                            <option value="+1">🇺🇸 +1</option>
                                            <option value="+44">🇬🇧 +44</option>
                                        </select>
                                        <input type="tel" id="phone" name="phone" placeholder="Nhập số điện thoại" value="${customer.address != null ? customer.address.phone : ''}" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="country">Quốc gia <span class="required">*</span></label>
                                    <select id="country" name="country" required>
                                        <c:set var="countryVal" value="${customer.address != null ? customer.address.country : ''}" />
                                        <option value="">Chọn quốc gia</option>
                                        <option value="Vietnam" ${countryVal == 'Vietnam' ? 'selected' : ''}>Việt Nam</option>
                                        <option value="United States" ${countryVal == 'United States' ? 'selected' : ''}>United States</option>
                                        <option value="United Kingdom" ${countryVal == 'United Kingdom' ? 'selected' : ''}>United Kingdom</option>
                                        <option value="Canada" ${countryVal == 'Canada' ? 'selected' : ''}>Canada</option>
                                    </select>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="city">Thành phố <span class="required">*</span></label>
                                        <input type="text" id="city" name="city" value="${customer.address != null ? customer.address.city : ''}" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="state">Tỉnh/Thành</label>
                                        <input type="text" id="state" name="state" value="${customer.address != null ? customer.address.province : ''}">
                                    </div>
                                    <div class="form-group">
                                        <label for="zipCode">Mã bưu điện</label>
                                        <input type="text" id="zipCode" name="zipCode" value="${customer.address != null ? customer.address.postalCode : ''}">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="address">Địa chỉ <span class="required">*</span></label>
                                    <input type="text" id="address" name="address" placeholder="Địa chỉ dòng 1" value="${customer.address != null ? customer.address.line1 : ''}" required>
                                </div>
                                <div class="form-group">
                                    <label for="address2">Địa chỉ dòng 2</label>
                                    <input type="text" id="address2" name="address2" placeholder="Căn hộ, tầng, v.v." value="${customer.address != null ? customer.address.line2 : ''}">
                                </div>
                                <!-- Removed terms and submit button from shipping form; moved to payment section -->
                            </form>
                        </div>

                        <!-- Payment Methods -->
                            <section class="section-card page-section">
                              <div class="container">
                                <div class="content">
                                  <div class="layout">
                                    <!-- Form -->
                                    <form action="#" class="payment-form">
                                      <!-- Payment method selection -->
                                      <fieldset>
                                        <legend>Phương thức thanh toán</legend>
                                        <div class="options">
                                          <label class="option">
                                            <span class="label-text">
                                              <img src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/visa.svg" class="icon-sm" alt="Visa"/>
                                              <img src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/mastercard.svg" class="icon-sm" alt="Mastercard"/>
                                              <span class="text-sm">Thẻ (Visa/Mastercard)</span>
                                            </span>
                                            <input type="radio" name="pay-method" id="pm-card" value="card" class="radio" checked />
                                          </label>

                                          <label class="option">
                                            <span class="label-text">
                                              <img src="${env}/images/Bank/VNPay.png" class="icon-sm" alt="VNPay"/>
                                              <span class="text-sm">VNPay QR</span>
                                            </span>
                                            <input type="radio" name="pay-method" id="pm-vnpay" value="vnpay" class="radio" />
                                          </label>

                                          <label class="option">
                                            <span class="label-text">
                                              <img src="${env}/images/Bank/NAPAS.png" class="icon-sm" alt="Napas"/>
                                              <span class="text-sm">NAPAS</span>
                                            </span>
                                            <input type="radio" name="pay-method" id="pm-napas" value="napas" class="radio" />
                                          </label>

                                          <label class="option">
                                            <span class="label-text">
                                              <svg class="icon-sm" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M5 6h14a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2m3 8h8"/></svg>
                                              <span class="text-sm">Thanh toán khi nhận hàng</span>
                                            </span>
                                            <input type="radio" name="pay-method" id="pm-cod" value="cod" class="radio" />
                                          </label>
                                        </div>
                                      </fieldset>
                                      <!-- Default (Card-like) fields -->
                                      <div id="card-fields" class="fields-grid">
                                        <div class="field">
                                          <label for="full_name" class="label"> Họ và tên (như trên thẻ)* </label>
                                          <input type="text" id="full_name" class="input" placeholder="NGUYEN VAN A" required />
                                        </div>

                                        <div class="field">
                                          <label for="card-number-input" class="label"> Số thẻ* </label>
                                          <input type="text" id="card-number-input" class="input" placeholder="xxxx-xxxx-xxxx-xxxx" inputmode="numeric" pattern="[0-9\- ]{12,19}" required />
                                        </div>

                                        <div class="field input-with-icon">
                                          <label for="card-expiration-input" class="label">Ngày hết hạn thẻ* </label>
                                          <div class="icon-start" aria-hidden="true">
                                            <svg class="icon-xs" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
                                              <path fill-rule="evenodd" d="M5 5a1 1 0 0 0 1-1 1 1 0 1 1 2 0 1 1 0 0 0 1 1h1a1 1 0 0 0 1-1 1 1 0 1 1 2 0 1 1 0 0 0 1 1h1a1 1 0 0 0 1-1 1 1 0 1 1 2 0 1 1 0 0 0 1 1 2 2 0 0 1 2 2v1a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V7a2 2 0 0 1 2-2ZM3 19v-7a1 1 0 0 1 1-1h16a1 1 0 0 1 1 1v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2Zm6.01-6a1 1 0 1 0-2 0 1 1 0 0 0 2 0Zm2 0a1 1 0 1 1 2 0 1 1 0 0 1-2 0Zm6 0a1 1 0 1 0-2 0 1 1 0 0 0 2 0Zm-10 4a1 1 0 1 1 2 0 1 1 0 0 1-2 0Zm6 0a1 1 0 1 0-2 0 1 1 0 0 0 2 0Zm2 0a1 1 0 1 1 2 0 1 1 0 0 1-2 0Z" clip-rule="evenodd" />
                                            </svg>
                                          </div>
                                          <input id="card-expiration-input" type="text" class="input input-pl" placeholder="12/23" required />
                                        </div>

                                        <div class="field">
                                          <label for="cvv-input" class="label label-inline">
                                            CVV*
                                            <button type="button" class="help-trigger" aria-describedby="cvv-desc">
                                              <svg class="icon-xs" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                                                <path fill-rule="evenodd" d="M2 12C2 6.477 6.477 2 12 2s10 4.477 10 10-4.477 10-10 10S2 17.523 2 12Zm9.408-5.5a1 1 0 1 0 0 2h.01a1 1 0 1 0 0-2h-.01ZM10 10a1 1 0 1 0 0 2h1v3h-1a1 1 0 1 0 0 2h4a1 1 0 1 0 0-2h-1v-4a1 1 0 0 0-1-1h-2Z" clip-rule="evenodd" />
                                              </svg>
                                            </button>
                                            <span id="cvv-desc" class="tooltip" role="tooltip">3 số cuối ở mặt sau thẻ</span>
                                          </label>
                                          <input type="text" id="cvv-input" inputmode="numeric" maxlength="4" class="input" placeholder="•••" required />
                                        </div>
                                      </div>

                                      <!-- VNPay QR fields (hidden by default) -->
                                      <div id="vnpay-fields" class="fields-grid hidden">
                                        <div class="field">
                                          <label for="vnpay-full-name" class="label">Họ và tên*</label>
                                          <input type="text" id="vnpay-full-name" class="input" placeholder="NGUYEN VAN A" />
                                        </div>
                                        <div class="field">
                                          <label for="bank-name" class="label">Ngân hàng*</label>
                                          <select id="bank-name" class="input">
                                            <option value="" selected disabled>Chọn ngân hàng</option>
                                            <option value="Vietcombank">Vietcombank</option>
                                            <option value="BIDV">BIDV</option>
                                            <option value="VietinBank">VietinBank</option>
                                            <option value="Agribank">Agribank</option>
                                            <option value="Techcombank">Techcombank</option>
                                            <option value="MB">MBBank</option>
                                            <option value="ACB">ACB</option>
                                            <option value="Sacombank">Sacombank</option>
                                            <option value="TPBank">TPBank</option>
                                            <option value="VPBank">VPBank</option>
                                            <option value="VIB">VIB</option>
                                            <option value="SHB">SHB</option>
                                            <option value="NCB">NCB</option>
                                            <option value="Bac A Bank">Bac A Bank</option>
                                          </select>
                                        </div>
                                        <div class="field">
                                          <label for="bank-account" class="label">Số thẻ/Tài khoản*</label>
                                          <input type="text" id="bank-account" inputmode="numeric" class="input" placeholder="9704*************" />
                                        </div>
                                      </div>

                                      <div id="card-brands" class="card-brands">
                                        <img class="brand" src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/paypal.svg" alt="PayPal" />
                                        <img class="brand" src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/visa.svg" alt="VISA" />
                                        <img class="brand" src="https://flowbite.s3.amazonaws.com/blocks/e-commerce/brand-logos/mastercard.svg" alt="Mastercard" />
                                      </div>

                                      <!-- Terms moved here -->
                                      <div class="checkbox-group">
                                        <input type="checkbox" id="terms" name="terms" required>
                                        <label for="terms">
                                            Tôi đã đọc và đồng ý với <a href="${env}/terms" class="link">Điều khoản và Điều kiện</a>
                                        </label>
                                      </div>

                                      <!-- Primary submit -->
                                      <button id="primary-submit" type="submit" class="btn btn-primary" data-order-url="${env}/order">Hoàn tất thanh toán</button>
                                    </form>

                                    <!-- VNPay QR Modal -->
                                    <div id="vnpay-modal" tabindex="-1" aria-hidden="true" class="modal hidden">
                                      <div class="modal-backdrop"></div>
                                      <div class="modal-content">
                                        <div class="modal-header">
                                          <h3>Thanh toán qua ứng dụng Mobile Banking</h3>
                                          <button type="button" class="icon-button" data-modal-hide="vnpay-modal" aria-label="Đóng">
                                            <svg class="icon-xs" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
                                          </button>
                                        </div>
                                        <div class="modal-body">
                                          <!-- QR + Price -->
                                          <div class="qr-box">
                                            <div class="qr-image-wrap">
                                              <img id="qr-image" src="https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=VNPAY-QR" alt="VNPay QR" class="qr-image" />
                                            </div>
                                            <div class="qr-info">
                                              <p id="order-id" class="muted">Thanh toán mã đơn hàng #100204</p>
                                              <p id="order-amount" class="amount">181.450 VND</p>
                                              <a href="#" class="link">Hướng dẫn thanh toán?</a>
                                            </div>
                                            <div class="qr-actions">
                                              <button id="vnpay-confirm" type="button" class="btn btn-primary">Xác nhận</button>
                                              <button type="button" class="btn btn-secondary" data-modal-hide="vnpay-modal">Hủy</button>
                                            </div>
                                          </div>
                                          <!-- Supported banks grid -->
                                          <div class="banks-box">
                                            <h4 class="banks-title">Sử dụng Mobile Banking hỗ trợ VNPAY-QR</h4>
                                            <div class="bank-grid">
                                              <div class="bank"><img src="${env}/images/Bank/VietcomBank logo-01.png" class="bank-logo" alt="Vietcombank"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/BIDV.png" class="bank-logo" alt="BIDV"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/Agribank.png" class="bank-logo" alt="Agribank"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/Techcombank.png" class="bank-logo" alt="Techcombank"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/NCB.png" class="bank-logo" alt="NCB"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/TPBank.png" class="bank-logo" alt="TPBank"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/VietinBank.png" class="bank-logo" alt="VietinBank"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/VPBank.png" class="bank-logo" alt="VPBank"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/VIB.png" class="bank-logo" alt="VIB"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/SHB.png" class="bank-logo" alt="SHB"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/Sacombank.png" class="bank-logo" alt="Sacombank"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/MBBank.png" class="bank-logo" alt="MB"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/ACB.png" class="bank-logo" alt="ACB"/></div>
                                              <div class="bank"><img src="${env}/images/Bank/Bac A Bank.png" class="bank-logo" alt="Bac A Bank"/></div>
                                            </div>
                                          </div>
                                        </div>
                                      </div>
                                    </div>

                                    <!-- Summary (removed - not used) -->
                                  </div>
                                </div>
                              </div>
                            </section>
                    </div>
                </div>

                <!-- Right Column - Order Review -->
                <div class="checkout-right">
                    <div class="order-summary">
                        <h2 class="section-title">Kiểm tra đơn hàng</h2>

                        <!-- Order Items -->
                        <div class="order-items">
                            <c:choose>
                                <c:when test="${empty items}">
                                    <div style="padding:1rem;color:#d00;font-weight:bold;">Giỏ hàng của bạn đang trống. <a href="${env}/products">Mua sắm</a>.</div>
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
                                                    Danh mục: <c:out value="${item.product.category != null ? item.product.category.name : ''}"/>
                                                </p>
                                                <p class="item-specs">Màu: ${item.color}</p>
                                                <p class="item-size">Kích thước: ${item.size}</p>
                                                <p class="item-quantity">Số lượng: ${item.quantity}</p>
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
                                <input type="text" placeholder="Mã giảm giá" class="discount-code">
                                <button type="button" class="apply-btn">Áp dụng</button>
                            </div>
                        </div>
                        
                        <!-- Order Totals -->
                        <div class="order-totals">
                            <input type="hidden" id="subtotalValue" value="${subtotal}" />
                            <input type="hidden" id="discountValue" value="${discount}" />
                            <div class="total-row">
                                <span>Tạm tính</span>
                                <span id="subtotalDisplay"><fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="₫"/></span>
                            </div>
                            <div class="total-row">
                                <span>Vận chuyển</span>
                                <span id="shippingCostDisplay"><fmt:formatNumber value="${shippingCost}" type="currency" currencySymbol="₫"/></span>
                            </div>
                            <div class="total-row">
                                <span>Giảm giá</span>
                                <span class="discount" id="discountDisplay">-<fmt:formatNumber value="${discount}" type="currency" currencySymbol="₫"/></span>
                            </div>
                            <div class="total-row final-total">
                                <span>Tổng</span>
                                <span id="totalDisplay"><fmt:formatNumber value="${total}" type="currency" currencySymbol="₫"/></span>
                            </div>
                        </div>
                        
                        <!-- Payment Button -->
                        <!-- Security Info -->
                        <div class="security-info">
                            <i class="fas fa-shield-alt"></i>
                            <div class="security-text">
                                <strong>Thanh toán an toàn - Mã hóa SSL</strong>
                                <p>Thông tin thanh toán và thông tin cá nhân luôn an toàn.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script src="${env}/customer/scripts/pages/main.js"></script>
    <script src="${env}/customer/scripts/pages/checkout.js"></script>
    <script src="${env}/js/customer/pages/checkout-validation.js"></script>
    <script src="${env}/js/customer/pages/checkout-shipping.js"></script>
    <script src="${env}/js/customer/pages/checkout-payment.js"></script>
</body>
</html>
