<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order detail - Nike </title>
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/order.jsp" />

</head>
<body>
    <!-- Order Process -->
    <jsp:include page="/WEB-INF/views/customer/layout/order-process.jsp" />

    <div class="container">
        <!-- Left Section - Order Details (2/3) -->
        <main class="order-section">
            <!-- Header -->
            <div class="order-header">
                <button class="back-btn" onclick="history.back()">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                        <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                </button>
                <div class="order-title-group">
                    <h1 class="order-number">Order #${order.id}</h1>
                    <div class="order-badges">
                        <span class="badge badge-paid">${order.shippingMethod}</span>
                        <span class="badge badge-unfulfilled">${order.orderStatus}</span>
                    </div>
                </div>
                <div class="order-actions">
                    <button class="btn-secondary">Report</button>
                    <button class="btn-secondary">Duplicate</button>
                    <button class="btn-secondary">Share</button>
                </div>
            </div>

            <!-- Order Meta Info -->
            <div class="order-meta">
                <div class="meta-item">
                    <span class="meta-label">Order date</span>
                    <span class="meta-value"><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">Order From</span>
                    <span class="meta-value">Bagus Fikri</span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">Mua qua</span>
                    <span class="meta-value">Cửa hàng trực tuyến</span>
                </div>
                <div class="meta-item">
                    <span class="meta-label">Total</span>
                    <span class="meta-value">12,567 / 32,068</span>
                </div>
            </div>

            <!-- Shipping Info -->
            <div class="shipping-info-card">
                <div class="shipping-destination">
                    <div class="destination-icon">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                            <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </div>
                    <div>
                        <p class="destination-label">From</p>
                        <p class="destination-value">Nike Store</p>
                    </div>
                </div>
                <div class="shipping-estimate">
                    <span class="estimate-label">Estimated</span>
                    <span class="estimate-value">1 đến 3 Tháng 2</span>
                </div>
            </div>

            <!-- Order Progress -->
            <div class="order-progress">
                <div class="progress-step active">
                    <div class="step-content">
                        <div class="step-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M9 11l3 3L22 4"/>
                                <path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11"/>
                            </svg>
                        </div>
                        <p class="step-title">Review order</p>
                    </div>
                    <div class="step-underline"></div>
                </div>
                <div class="progress-step">
                    <div class="step-content">
                        <div class="step-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                                <line x1="9" y1="9" x2="15" y2="9"/>
                                <line x1="9" y1="15" x2="15" y2="15"/>
                            </svg>
                        </div>
                        <p class="step-title">Preparing order</p>
                    </div>
                    <div class="step-underline"></div>
                </div>
                <div class="progress-step">
                    <div class="step-content">
                        <div class="step-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <rect x="1" y="3" width="15" height="13"/>
                                <polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/>
                                <circle cx="5.5" cy="18.5" r="2.5"/>
                                <circle cx="18.5" cy="18.5" r="2.5"/>
                            </svg>
                        </div>
                        <p class="step-title">Shipping</p>
                    </div>
                    <div class="step-underline"></div>
                </div>
                <div class="progress-step">
                    <div class="step-content">
                        <div class="step-icon">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <polyline points="9 11 12 14 22 4"/>
                                <path d="M21 12v7a2 2 0 01-2 2H5a2 2 0 01-2-2V5a2 2 0 012-2h11"/>
                            </svg>
                        </div>
                        <p class="step-title">Delivered</p>
                    </div>
                    <div class="step-underline"></div>
                </div>
            </div>

            <div class="action-buttons">
                <button class="btn-outline">Hủy Đơn Hàng</button>
                <button class="btn-primary">Tạo Nhãn Vận Chuyển</button>
            </div>

            <!-- Products Section -->
            <div class="products-section">
                <div class="section-header">
                    <h2 class="section-title">Sản Phẩm</h2>
                    <span class="badge badge-unfulfilled">${order.orderStatus}</span>
                </div>
                <c:forEach var="it" items="${order.items}">
                    <div class="product-item">
                        <img src="${env}${it.product.imageUrl}" alt="${it.productName}" class="product-image">
                        <div class="product-details">
                            <h3 class="product-name">${it.productName}</h3>
                            <p class="product-sku">ID: ${it.product.id}</p>
                            <div class="product-variants">
                                <span class="variant">${it.color}</span>
                                <span class="variant-separator">•</span>
                                <span class="variant">Size: ${it.size}</span>
                                <span class="variant-separator">•</span>
                                <span class="variant">SL: ${it.quantity}</span>
                            </div>
                        </div>
                        <div class="product-price">
                            <p class="price"><fmt:formatNumber value="${it.lineTotal}" type="currency"/></p>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Payment Details -->
            <div class="payment-section">
                <div class="section-header">
                    <h2 class="section-title">Chi Tiết Thanh Toán</h2>
                    <span class="badge badge-paid">Đã Thanh Toán</span>
                </div>

                <div class="payment-method">
                    <span class="payment-label">Payment Method</span>
                    <div class="payment-value">
                        <svg width="32" height="20" viewBox="0 0 48 32" fill="none">
                            <rect width="48" height="32" rx="4" fill="#1434CB"/>
                            <rect x="12" y="8" width="24" height="16" rx="2" fill="#EB001B"/>
                            <rect x="20" y="8" width="16" height="16" rx="2" fill="#F79E1B"/>
                        </svg>
                        <span>#3634</span>
                    </div>
                </div>

                <div class="payment-breakdown">
                    <div class="breakdown-row">
                        <span class="breakdown-label">Subtotal</span>
                        <span class="breakdown-value">${fn:length(order.items)} sản phẩm • <fmt:formatNumber value="${order.subtotal}" type="currency"/></span>
                    </div>
                    <div class="breakdown-row shipping-type">
                        <span class="breakdown-label">Shipping Method</span>
                        <span class="breakdown-value">${order.shippingMethod}</span>
                    </div>
                    <div class="breakdown-row">
                        <span class="breakdown-label">Shipping Fee</span>
                        <span class="breakdown-value"><fmt:formatNumber value="${order.shippingCost}" type="currency"/></span>
                    </div>
                    <div class="breakdown-row total">
                        <span class="breakdown-label">Tổng cộng</span>
                        <span class="breakdown-value"><fmt:formatNumber value="${order.total}" type="currency"/></span>
                    </div>
                </div>
            </div>

            <!-- Order Note -->
            <div class="order-note-section">
                <div class="section-header">
                    <h2 class="section-title">Ghi Chú Đơn Hàng</h2>
                    <button class="btn-icon">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                            <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M18.5 2.5C18.8978 2.1022 19.4374 1.87868 20 1.87868C20.5626 1.87868 21.1022 2.1022 21.5 2.5C21.8978 2.8978 22.1213 3.43739 22.1213 4C22.1213 4.56261 21.8978 5.1022 21.5 5.5L12 15L8 16L9 12L18.5 2.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                </div>
                <p class="note-text">Vui lòng gói hộp với bọc nilon, để chữ trên hộp không bị đọc được, đây là quà sinh nhật</p>
            </div>
        </main>

        <!-- Right Section - Customer Details (1/3) -->
        <aside class="customer-section">
            <!-- Customer Info -->
            <div class="customer-card">
                <div class="customer-header">
                    <h2 class="section-title">Khách Hàng</h2>
                </div>
                <div class="customer-profile">
                    <img src="https://ui-avatars.com/api/?name=Bagus+Fikri&background=000&color=fff&size=128" alt="Customer" class="customer-avatar">
                    <div class="customer-info">
                        <h3 class="customer-name">${order.customer.username}</h3>
                        <p class="customer-orders">Tổng: 1 đơn hàng</p>
                    </div>
                    <button class="btn-icon-circle">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                            <path d="M21 15C21 15.5304 20.7893 16.0391 20.4142 16.4142C20.0391 16.7893 19.5304 17 19 17H7L3 21V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H19C19.5304 3 20.0391 3.21071 20.4142 3.58579C20.7893 3.96086 21 4.46957 21 5V15Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                </div>
            </div>

            <!-- Shipping Address -->
            <div class="address-card">
                <div class="address-header">
                    <h2 class="section-title">Address</h2>
                    <button class="btn-icon">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                            <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M18.5 2.5C18.8978 2.1022 19.4374 1.87868 20 1.87868C20.5626 1.87868 21.1022 2.1022 21.5 2.5C21.8978 2.8978 22.1213 3.43739 22.1213 4C22.1213 4.56261 21.8978 5.1022 21.5 5.5L12 15L8 16L9 12L18.5 2.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                </div>

                <!-- Google Map -->
                <div id="map" class="address-map"></div>

                <div class="address-details">
                    <p class="address-name"><c:out value="${order.customer.address != null && order.customer.address.recipientName != null ? order.customer.address.recipientName : order.customer.username}"/></p>
                    <c:choose>
                        <c:when test="${order.customer.address != null}">
                            <p class="address-text">
                                <c:out value='${order.customer.address.line1}'/>
                                <c:if test='${not empty order.customer.address.line2}'>, <c:out value='${order.customer.address.line2}'/></c:if>,<br/>
                                <c:if test='${not empty order.customer.address.city}'><c:out value='${order.customer.address.city}'/>, </c:if>
                                <c:out value='${order.customer.address.province}'/>, <c:out value='${order.customer.address.postalCode}'/><br/>
                                <c:if test='${not empty order.customer.address.country}'><c:out value='${order.customer.address.country}'/></c:if>
                            </p>
                            <a href="https://www.google.com/maps/search/?api=1&query=${order.customer.address.line1}${order.customer.address.line2 != null ? ('+'+order.customer.address.line2) : ''}+${order.customer.address.city}+${order.customer.address.province}+${order.customer.address.postalCode}" target="_blank" class="view-map-link">Check map</a>
                        </c:when>
                        <c:otherwise>
                            <p class="address-text">No address on file.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Contact Information -->
            <div class="contact-card">
                <div class="contact-header">
                    <h2 class="section-title">Contact</h2>
                    <button class="btn-icon">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                            <path d="M11 4H4C3.46957 4 2.96086 4.21071 2.58579 4.58579C2.21071 4.96086 2 5.46957 2 6V20C2 20.5304 2.21071 21.0391 2.58579 21.4142C2.96086 21.7893 3.46957 22 4 22H18C18.5304 22 19.0391 21.7893 19.4142 21.4142C19.7893 21.0391 20 20.5304 20 20V13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M18.5 2.5C18.8978 2.1022 19.4374 1.87868 20 1.87868C20.5626 1.87868 21.1022 2.1022 21.5 2.5C21.8978 2.8978 22.1213 3.43739 22.1213 4C22.1213 4.56261 21.8978 5.1022 21.5 5.5L12 15L8 16L9 12L18.5 2.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                </div>
                <div class="contact-list">
                    <c:set var="custEmail" value="${order.customer.credential != null ? order.customer.credential.email : ''}"/>
                    <a href="mailto:${custEmail}" class="contact-item" style="${empty custEmail ? 'pointer-events:none;opacity:.5;' : ''}">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                            <path d="M4 4H20C21.1 4 22 4.9 22 6V18C22 19.1 21.1 20 20 20H4C2.9 20 2 19.1 2 18V6C2 4.9 2.9 4 4 4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M22 6L12 13L2 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        <span><c:out value="${custEmail}"/></span>
                    </a>
                    <c:set var="custPhone" value="${order.customer.address != null ? order.customer.address.phone : ''}"/>
                    <c:choose>
                        <c:when test="${empty custPhone}">
                            <c:set var="phoneHref" value="#"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="phoneHref" value="tel:${custPhone}"/>
                        </c:otherwise>
                    </c:choose>
                    <a href="${phoneHref}" class="contact-item" style="${empty custPhone ? 'pointer-events:none;opacity:.5;' : ''}">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                            <path d="M22 16.92V19.92C22.0011 20.1985 21.9441 20.4742 21.8325 20.7293C21.7209 20.9845 21.5573 21.2136 21.3521 21.4019C21.1469 21.5901 20.9046 21.7335 20.6407 21.8227C20.3769 21.9119 20.0974 21.9451 19.82 21.92C16.7428 21.5856 13.787 20.5341 11.19 18.85C8.77382 17.3147 6.72533 15.2662 5.18999 12.85C3.49997 10.2412 2.44824 7.27099 2.11999 4.18C2.095 3.90347 2.12787 3.62476 2.21649 3.36162C2.30512 3.09849 2.44756 2.85669 2.63476 2.65162C2.82196 2.44655 3.0498 2.28271 3.30379 2.17052C3.55777 2.05833 3.83233 2.00026 4.10999 2H7.10999C7.5953 1.99522 8.06579 2.16708 8.43376 2.48353C8.80173 2.79999 9.04207 3.23945 9.10999 3.72C9.23662 4.68007 9.47144 5.62273 9.80999 6.53C9.94454 6.88792 9.97366 7.27691 9.8939 7.65088C9.81415 8.02485 9.62886 8.36811 9.35999 8.64L8.08999 9.91C9.51355 12.4135 11.5864 14.4864 14.09 15.91L15.36 14.64C15.6319 14.3711 15.9751 14.1858 16.3491 14.1061C16.7231 14.0263 17.1121 14.0555 17.47 14.19C18.3773 14.5286 19.3199 14.7634 20.28 14.89C20.7658 14.9585 21.2094 15.2032 21.5265 15.5775C21.8437 15.9518 22.0122 16.4296 22 16.92Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        <span><c:out value="${custPhone}"/></span>
                    </a>
                </div>
            </div>

            <!-- Tags -->
            <div class="tags-card">
                <div class="tags-header">
                    <h2 class="section-title">Tags</h2>
                </div>
                <div class="tags-list">
                    <span class="tag">VIP</span>
                    <span class="tag">Nike Shoes</span>
                    <span class="tag">Quà tặng</span>
                    <button class="tag-add">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                            <path d="M12 5V19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M5 12H19" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        Thêm tag
                    </button>
                </div>
            </div>
        </aside>
    </div>

    <!-- Google Maps API -->
    <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initMap" async defer></script>
    <script>
        function initMap() {
            const location = { lat: 41.0732, lng: -73.4013 };

            const map = new google.maps.Map(document.getElementById('map'), {
                zoom: 15,
                center: location,
                styles: [
                    {
                        "featureType": "all",
                        "elementType": "geometry",
                        "stylers": [{"color": "#f5f5f5"}]
                    },
                    {
                        "featureType": "all",
                        "elementType": "labels.text.fill",
                        "stylers": [{"color": "#616161"}]
                    },
                    {
                        "featureType": "all",
                        "elementType": "labels.text.stroke",
                        "stylers": [{"color": "#f5f5f5"}]
                    },
                    {
                        "featureType": "water",
                        "elementType": "geometry",
                        "stylers": [{"color": "#e9e9e9"}]
                    }
                ],
                disableDefaultUI: true,
                zoomControl: true
            });

            const marker = new google.maps.Marker({
                position: location,
                map: map,
                title: 'Bagus Fikri',
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    scale: 10,
                    fillColor: '#FF6A00',
                    fillOpacity: 1,
                    strokeColor: '#ffffff',
                    strokeWeight: 3
                }
            });
        }
    </script>
</body>
</html>
