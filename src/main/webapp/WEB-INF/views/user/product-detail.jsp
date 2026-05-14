<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cfmt" uri="/WEB-INF/tlds/currency.tld" %>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<c:set var="hasColors" value="${not empty product.colors}" />
<c:set var="hasSale" value="${not empty product.salePrice and product.salePrice lt product.price}" />
<c:if test="${hasSale}">
    <c:set var="discountAmount" value="${product.price - product.salePrice}" />
    <c:set var="discountPercent" value="${((product.price - product.salePrice) * 100) / product.price}" />
</c:if>

<c:if test="${hasColors}">
    <c:set var="selectedColor" value="${product.colors[0]}" />
    <c:set var="selectedImages" value="${selectedColor.images}" />
    <c:set var="selectedVariants" value="${selectedColor.variants}" />
    <!-- Images are now pre-sorted: main image first, then by order -->
    <c:set var="selectedMainImage" value="${selectedImages[0]}" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - Nike</title>

    <jsp:include page="/WEB-INF/views/user/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/user/imported/product-detail.jsp" />
</head>
<body class="product-detail-page is-loading">
<jsp:include page="/WEB-INF/views/user/layout/header.jsp" />

<section class="pdp-skeleton" aria-hidden="true">
    <div class="container">
        <div class="pdp-skeleton-layout">
            <div class="pdp-skeleton-gallery">
                <div class="pdp-skeleton-thumbs">
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
                <div class="pdp-skeleton-main"></div>
            </div>
            <div class="pdp-skeleton-info">
                <span class="pdp-skeleton-line short"></span>
                <span class="pdp-skeleton-line title"></span>
                <span class="pdp-skeleton-line medium"></span>
                <span class="pdp-skeleton-line price"></span>
                <div class="pdp-skeleton-swatches">
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
                <div class="pdp-skeleton-sizes">
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
                <span class="pdp-skeleton-button"></span>
                <span class="pdp-skeleton-button light"></span>
            </div>
        </div>
    </div>
</section>

<main class="product-detail" role="main" aria-labelledby="product-title">
    <div class="container">
        <div class="product-layout">
            <section class="product-images" role="region" aria-labelledby="product-images-title">
                <h2 id="product-images-title" class="sr-only">Hình ảnh sản phẩm</h2>

                <div class="image-preview">
                    <div class="thumbnail-nav" id="thumbnail-nav" role="list" aria-label="Product thumbnails">
                        <c:choose>
                            <c:when test="${not empty selectedImages}">
                                <c:forEach var="image" items="${selectedImages}" varStatus="status">
                                    <button type="button"
                                            class="thumbnail-item<c:if test='${selectedMainImage != null and image.id == selectedMainImage.id}'> active</c:if>"
                                            data-thumbnail-index="${status.index}"
                                            data-image-path="${image.path}"
                                            data-alt-text="${fn:escapeXml(image.altText)}"
                                            role="listitem"
                                            aria-label="View image ${status.count}">
                                        <img src="${image.path}" alt="${fn:escapeXml(image.altText)}">
                                    </button>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="thumbnail-item active" role="listitem">
                                    <img src="${env}/images/products/default-product.avif" alt="Default product image">
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="main-image-container" role="region" aria-labelledby="main-image-title" aria-live="polite">
                        <h3 id="main-image-title" class="sr-only">Ảnh chính</h3>
                        <div class="main-image">
                            <div class="image-badge">
                                <i class="fas fa-star" aria-hidden="true"></i>
                                Được đánh giá cao
                            </div>
                            <c:choose>
                                <c:when test="${selectedMainImage != null}">
                                    <img id="currentImage"
                                         src="${selectedMainImage.path}"
                                         alt="${fn:escapeXml(selectedMainImage.altText)}">
                                </c:when>
                                <c:otherwise>
                                    <img id="currentImage"
                                         src="${env}/images/products/default-product.avif"
                                         alt="${fn:escapeXml(product.name)}">
                                </c:otherwise>
                            </c:choose>

                            <div class="image-nav-controls" aria-label="Gallery navigation">
                                <button type="button" class="image-nav-btn" id="prev-image-button" aria-label="Previous image">
                                    <i class="fas fa-chevron-left" aria-hidden="true"></i>
                                </button>
                                <button type="button" class="image-nav-btn" id="next-image-button" aria-label="Next image">
                                    <i class="fas fa-chevron-right" aria-hidden="true"></i>
                                </button>
                            </div>
                        </div>

                        <div class="image-meta-strip sr-only">
                            <span class="image-meta-pill">
                                <i class="fas fa-images" aria-hidden="true"></i>
                                <span id="image-count-label">
                                    <c:choose>
                                        <c:when test="${not empty selectedImages}">${fn:length(selectedImages)} ảnh</c:when>
                                        <c:otherwise>1 ảnh</c:otherwise>
                                    </c:choose>
                                </span>
                            </span>
                            <span class="image-meta-copy">Vuốt hoặc chọn hình thu nhỏ để xem chi tiết.</span>
                        </div>
                    </div>
                </div>
            </section>

            <section class="product-info" role="region" aria-labelledby="product-title">
                <div class="product-info-card">
                    <div class="product-header">
                        <div class="product-kicker-row">
                            <span class="product-kicker"><c:out value="${hasSale ? 'Member Exclusive' : 'Recycled Materials'}" /></span>
                        </div>

                        <h1 class="product-title" id="product-title">${product.name}</h1>
                        <p class="product-subtitle">${product.categoryName}</p>

                        <div class="product-price-block" aria-label="Product price">
                            <div class="product-price">
                                <c:choose>
                                    <c:when test="${hasSale}">
                                        <span class="sale-price">
                                            ${cfmt:format(product.salePrice)}
                                        </span>
                                        <span class="original-price">
                                            ${cfmt:format(product.price)}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="regular-price">
                                            ${cfmt:format(product.price)}
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <c:if test="${hasSale}">
                                <div class="price-summary">
                                    <span class="price-saving">
                                        Tiết kiệm ${cfmt:format(discountAmount)}
                                    </span>
                                    <span class="price-discount">
                                        <fmt:formatNumber value="${discountPercent}" type="number" maxFractionDigits="0"/>% giảm
                                    </span>
                                </div>
                            </c:if>
                        </div>

                    </div>

                    <div class="selection-stack">
                        <div class="color-selection" role="group" aria-labelledby="color-selection-title">
                            <div class="selection-head color-header">
                                <div>
                                     <h4 id="color-selection-title">Chọn màu</h4>
                                </div>
                                <span class="selection-value" id="selected-color-label">
                                    <c:if test="${not empty selectedColor}">${selectedColor.colorName}</c:if>
                                </span>
                            </div>

                            <div id="color-options" class="color-options-grid">
                                <c:forEach var="color" items="${product.colors}" varStatus="status">
                                    <c:set var="colorSoldOut" value="true" />
                                    <c:forEach var="variant" items="${color.variants}">
                                        <c:if test="${variant.active and variant.stock > 0}">
                                            <c:set var="colorSoldOut" value="false" />
                                        </c:if>
                                    </c:forEach>
                                    <c:set var="colorPreviewImage" value="${null}" />
                                    <c:if test="${not empty color.images}">
                                        <c:forEach var="colorImage" items="${color.images}">
                                            <c:if test="${colorPreviewImage == null and colorImage.isMainForColor}">
                                                <c:set var="colorPreviewImage" value="${colorImage}" />
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${colorPreviewImage == null}">
                                            <c:set var="colorPreviewImage" value="${color.images[0]}" />
                                        </c:if>
                                    </c:if>
                                    <button type="button"
                                            class="color-btn color-option${status.first ? ' active selected' : ''}${colorSoldOut ? ' sold-out' : ''}"
                                            data-color-id="${color.id}"
                                            data-color-index="${status.index}"
                                            data-color-name="${fn:escapeXml(color.colorName)}"
                                            data-sold-out="${colorSoldOut}"
                                            aria-pressed="${status.first ? 'true' : 'false'}"
                                            aria-label="Select color ${fn:escapeXml(color.colorName)}${colorSoldOut ? ' sold out' : ''}">
                                        <c:choose>
                                            <c:when test="${colorPreviewImage != null}">
                                                <img class="color-option-image"
                                                     src="${colorPreviewImage.path}"
                                                     alt="${fn:escapeXml(color.colorName)} preview">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="color-swatch-wrap">
                                                    <span class="color-swatch"
                                                          style="background-color:${fn:escapeXml(color.hexCode)};"
                                                          aria-hidden="true"></span>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </button>
                                </c:forEach>
                                    <button type="button" class="color-btn color-option design-your-own" aria-label="Thiết kế màu riêng">
                                    <span class="design-ring" aria-hidden="true"></span>
                                    <span class="design-copy">Thiết kế<br>riêng</span>
                                </button>
                            </div>
                                <div class="sold-out-colour-alert" id="sold-out-colour-alert" role="status" hidden>
                                <strong>Hết hàng:</strong> Màu này hiện không có sẵn.
                            </div>
                        </div>

                        <div class="size-selection" role="group" aria-labelledby="size-selection-title">
                            <div class="selection-head size-header">
                                <div>
                                     <h4 id="size-selection-title">Chọn size</h4>
                                </div>
                                 <button type="button" class="size-guide-btn" aria-label="Mở hướng dẫn chọn size">
                                     <i class="fas fa-ruler-combined" aria-hidden="true"></i>
                                     Hướng dẫn chọn size
                                 </button>
                            </div>

                            <div class="size-options" id="size-options" role="radiogroup" aria-labelledby="size-selection-title">
                                <c:choose>
                                    <c:when test="${not empty selectedVariants}">
                                        <c:forEach var="variant" items="${selectedVariants}">
                                            <button type="button"
                                                    class="size-btn size-option<c:if test='${variant.stock <= 0 or not variant.active}'> disabled out-of-stock</c:if>"
                                                    data-variant-id="${variant.id}"
                                                    data-size="${fn:escapeXml(variant.size)}"
                                                    data-stock="${variant.stock}"
                                                    role="radio"
                                                    aria-checked="false"
                                                    <c:if test="${variant.stock <= 0 or not variant.active}">disabled="disabled"</c:if>>
                                                <span class="size-label">${variant.size}</span>
                                                <span class="size-stock">
                                                    <c:choose>
                                                         <c:when test="${variant.stock <= 0 or not variant.active}">Hết hàng</c:when>
                                                         <c:when test="${variant.stock <= 3}">Còn ít</c:when>
                                                         <c:otherwise>Còn hàng</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </button>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                           <div class="no-sizes-message" role="status">Không có size.</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <div class="product-actions" role="group" aria-label="Product actions">
                        <button id="add-to-cart-button"
                                class="btn btn-primary btn-full add-to-cart"
                                type="button">
                            Thêm vào giỏ hàng
                        </button>
                        <button id="wishlist-button"
                                class="btn btn-outline btn-full wishlist-btn"
                                type="button">
                            <i class="far fa-heart" aria-hidden="true"></i>
                            Yêu thích
                        </button>
                    </div>

                    <div class="product-info-note">
                        Sản phẩm này không áp dụng cho chương trình khuyến mãi và giảm giá.
                    </div>

                    <c:if test="${not empty product.description}">
                        <div class="product-description-block">
                            <p class="product-description">${product.description}</p>
                        </div>
                    </c:if>

                    <div class="product-summary">
                        <ul class="product-bullets">
                            <li>
                                Colour Shown:
                                <span id="selected-color-detail">
                                    <c:if test="${not empty selectedColor}">${selectedColor.colorName}</c:if>
                                </span>
                            </li>
                            <li>Style: ${product.id}</li>
                            <li>Country/Region of Origin: Indonesia</li>
                        </ul>

                        <a href="#product-story" class="product-details-link">Xem chi tiết sản phẩm</a>
                    </div>

                    <div class="product-accordion-list">
                        <button type="button"
                                class="product-accordion-row"
                                id="delivery-returns-toggle"
                                aria-expanded="false"
                                aria-controls="delivery-returns-panel">
                            <span>Miễn phí giao hàng và trả hàng</span>
                            <i class="fas fa-chevron-down" aria-hidden="true"></i>
                        </button>
                        <div class="product-accordion-panel"
                             id="delivery-returns-panel"
                             role="region"
                             aria-labelledby="delivery-returns-toggle"
                             hidden>
                            <p>Đơn hàng từ 5,000,000₫ trở lên được miễn phí vận chuyển tiêu chuẩn.</p>
                            <p>Giao tiêu chuẩn trong 4-5 ngày làm việc</p>
                            <p>Giao nhanh trong 2-4 ngày làm việc</p>
                            <p>Đơn hàng được xử lý và giao từ Thứ Hai đến Thứ Sáu (không bao gồm ngày lễ)</p>
                            <p>Thành viên Nike được miễn phí trả hàng.</p>
                        </div>
                        <button type="button" class="product-accordion-row">
                            <span>Đánh giá (24)</span>
                            <span class="accordion-review-meta">
                                <span class="review-stars" aria-hidden="true">★★★★★</span>
                                <i class="fas fa-chevron-down" aria-hidden="true"></i>
                            </span>
                        </button>
                    </div>
                </div>
            </section>
        </div>
    </div>
</main>

<section class="product-story" id="product-story" aria-labelledby="product-story-title">
    <div class="container">
        <h2 class="product-story-title" id="product-story-title">Responsive full-length cushioning sculpted to energise an icon.</h2>

        <div class="story-feature-grid">
            <div class="story-feature-item">
                <div class="story-feature-icon"><i class="fas fa-road" aria-hidden="true"></i></div>
                <span class="story-feature-label">Engineered For</span>
                <strong class="story-feature-value">${product.categoryName}</strong>
            </div>
            <div class="story-feature-item">
                <div class="story-feature-icon"><i class="fas fa-shoe-prints" aria-hidden="true"></i></div>
                <span class="story-feature-label">Cushioning</span>
                <strong class="story-feature-value">Responsive</strong>
            </div>
            <div class="story-feature-item">
                <div class="story-feature-icon"><i class="fas fa-feather-alt" aria-hidden="true"></i></div>
                <span class="story-feature-label">Shoe Weight</span>
                <strong class="story-feature-value">Approx. 243g</strong>
            </div>
            <div class="story-feature-item">
                <div class="story-feature-icon"><i class="fas fa-ruler-vertical" aria-hidden="true"></i></div>
                <span class="story-feature-label">Heel-to-Toe Drop</span>
                <strong class="story-feature-value">10mm</strong>
            </div>
        </div>

        <button type="button" class="story-spec-toggle">
            <span>Tech Specs</span>
            <i class="fas fa-chevron-down" aria-hidden="true"></i>
        </button>

        <div class="story-copy">
            <h3>What's New?</h3>
            <ul>
                <li>Greater toe spring than previous models due to an updated silhouette that increases the curve of the toe box.</li>
                <li>Full-length Air Zoom unit compared to forefoot and heel units in previous models.</li>
                <li>Designed to keep transitions smooth and energetic throughout everyday mileage.</li>
            </ul>
        </div>
    </div>
</section>

<section class="social-section pdp-wearing-section" aria-labelledby="wearing-title">
    <div class="container">
        <div class="pdp-section-head pdp-section-head--stacked">
            <div>
                <h2 id="wearing-title">Xem Người khác diện sản phẩm này</h2>
                <p>Tải ảnh của bạn hoặc tag @Nike trên Instagram để có cơ hội được giới thiệu.</p>
            </div>
            <button type="button" class="upload-btn">Tải ảnh lên</button>
        </div>
        <div class="wearing-grid">
            <article class="wearing-card">
                <img src="${env}/images/social/Andre F.png" alt="Khách hàng phối sản phẩm Nike">
                <span>@andre_f</span>
            </article>
            <article class="wearing-card">
                <img src="${env}/images/social/Browskin.png" alt="Khách hàng mang giày Nike">
                <span>@browskin</span>
            </article>
            <article class="wearing-card">
                <img src="${env}/images/social/Leeho.png" alt="Street style with Nike sneakers">
                <span>@leeho</span>
            </article>
            <article class="wearing-upload-card">
                <strong>Be one of the first to show your style.</strong>
                <span aria-hidden="true">+</span>
            </article>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/user/layout/footer.jsp" />

<template id="productDetailPayload">
{
  "defaultProductImage": "${env}/images/products/default-product.avif",
  "defaultProductName": "${fn:escapeXml(product.name)}",
  "colors": [
    <c:forEach var="color" items="${product.colors}" varStatus="colorStatus">
    <c:set var="payloadColorSoldOut" value="true" />
    <c:forEach var="variant" items="${color.variants}">
      <c:if test="${variant.active and variant.stock > 0}">
        <c:set var="payloadColorSoldOut" value="false" />
      </c:if>
    </c:forEach>
    {
      "id": ${color.id},
      "colorName": "${fn:escapeXml(color.colorName)}",
      "hexCode": "${fn:escapeXml(color.hexCode)}",
      "soldOut": ${payloadColorSoldOut},
      "images": [
        <c:forEach var="image" items="${color.images}" varStatus="imageStatus">
        {
          "id": ${image.id},
          "path": "${fn:escapeXml(image.path)}",
          "title": "${fn:escapeXml(image.title)}",
          "altText": "${fn:escapeXml(image.altText)}",
          "isMainForColor": ${image.isMainForColor},
          "orderIndex": ${image.orderIndex}
        }<c:if test="${!imageStatus.last}">,</c:if>
        </c:forEach>
      ],
      "variants": [
        <c:forEach var="variant" items="${color.variants}" varStatus="variantStatus">
        {
          "id": ${variant.id},
          "sku": "${fn:escapeXml(variant.sku)}",
          "size": "${fn:escapeXml(variant.size)}",
          "stock": ${variant.stock},
          "active": ${variant.active}
        }<c:if test="${!variantStatus.last}">,</c:if>
        </c:forEach>
      ]
    }<c:if test="${!colorStatus.last}">,</c:if>
    </c:forEach>
  ]
}
</template>
<script src="${env}/js/customer/pages/product-detail-page.js"></script>
</body>
</html>
