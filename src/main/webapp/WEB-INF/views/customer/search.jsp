<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả tìm kiếm</title>
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <link rel="stylesheet" href="${env}/css/customer/search/search.css">
    <link rel="stylesheet" href="${env}/css/common/base.css">
</head>
<body class="search-page">
<div class="container search-page-container">
    <!-- Search Header -->
    <div class="search-header">
        <!-- Close (X) Button -->
        <button type="button" class="search-close-btn" id="closeSearchBtn" aria-label="Đóng tìm kiếm và quay lại" title="Đóng">&times;</button>
        <div class="search-info">
            <div class="search-input-wrapper">
                <form action="${env}/search" method="get" id="searchForm">
                    <input type="text" class="search-input" id="liveSearchInput" name="q" placeholder="Tìm kiếm sản phẩm..." value="${fn:escapeXml(query)}" autocomplete="off" />
                    <c:if test="${not empty category}">
                        <input type="hidden" name="category" value="${category}" />
                    </c:if>
                    <c:if test="${not empty sort}">
                        <input type="hidden" name="sort" value="${sort}" />
                    </c:if>
                </form>
                <!-- Live search suggestion -->
                <div class="live-search-status" id="liveSearchStatus" aria-live="polite" aria-atomic="true"></div>
            </div>
            <p class="search-results-count" id="resultsCount">
                <c:choose>
                    <c:when test="${searchResult.total == 0}">Không có kết quả</c:when>
                    <c:otherwise>Hiển thị ${searchResult.total} kết quả</c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>

    <!-- Filters Row -->
    <div class="category-filter-row">
        <div class="category-scroll">
            <ul class="category-list">
                <li class="category-item"><a href="${env}/search?q=${fn:escapeXml(query)}" class="category-link ${empty category ? 'active' : ''}">Tất cả</a></li>
                <c:forEach var="cat" items="${categories}">
                    <c:set var="catName" value="${cat.name}" />
                    <li class="category-item">
                        <a href="${env}/search?q=${fn:escapeXml(query)}&category=${fn:escapeXml(catName)}"
                           class="category-link ${catName==category ? 'active' : ''}">${catName}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="sort-wrapper ml-auto">
            <button type="button" class="sort-btn" id="sortToggleBtn" aria-haspopup="true" aria-expanded="false">
                Sắp xếp
                <span class="sort-chevron" aria-hidden="true"></span>
            </button>
            <div class="sort-dropdown" id="sortMenu" role="menu" aria-label="Sắp xếp sản phẩm">
                <button class="sort-option ${sort=='newest' or empty sort ? 'active' : ''}" data-sort="newest" role="menuitem" type="button">Mới nhất</button>
                <button class="sort-option ${sort=='price-low' ? 'active' : ''}" data-sort="price-low" role="menuitem" type="button">Giá: Thấp -> Cao</button>
                <button class="sort-option ${sort=='price-high' ? 'active' : ''}" data-sort="price-high" role="menuitem" type="button">Giá: Cao -> Thấp</button>
                <button class="sort-option ${sort=='featured' ? 'active' : ''}" data-sort="featured" role="menuitem" type="button">Nổi bật</button>
            </div>
            <form id="sortHiddenForm" action="${env}/search" method="get" style="display:none;">
                <input type="hidden" name="q" value="${fn:escapeXml(query)}" />
                <c:if test="${not empty category}">
                    <input type="hidden" name="category" value="${category}" />
                </c:if>
                <input type="hidden" name="sort" id="sortHiddenInput" value="${sort}" />
            </form>
        </div>
    </div>

    <!-- Product Grid -->
    <div class="product-grid" id="productGrid">
        <c:choose>
            <c:when test="${empty products}">
                <div class="no-products">Không tìm thấy sản phẩm phù hợp với tìm kiếm của bạn.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="p" items="${products}">
                    <div class="product-card" onclick="location.href='${env}/product-detail?id=${p.id}'">
                        <div class="product-image-wrapper">
                            <c:choose>
                                <c:when test="${not empty p.images and p.images[0] ne null and not empty p.images[0].url}">
                                    <c:set var="imgUrl" value="${p.images[0].url}" />
                                    <c:if test="${fn:startsWith(imgUrl,'//')}">
                                        <c:set var="imgUrl" value="${fn:substring(imgUrl, 1, fn:length(imgUrl))}" />
                                    </c:if>
                                    <c:choose>
                                        <c:when test="${fn:startsWith(imgUrl,'/images/products/')}">
                                            <c:set var="resolvedImg" value="${imgUrl}" />
                                        </c:when>
                                        <c:when test="${fn:startsWith(imgUrl,'images/products/')}">
                                            <c:set var="resolvedImg" value="/${imgUrl}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="resolvedImg" value="/images/products/${imgUrl}" />
                                        </c:otherwise>
                                    </c:choose>
                                    <img src="${env}${resolvedImg}" alt="${p.name}" class="product-image" />
                                </c:when>
                                <c:otherwise>
                                    <img src="${env}/images/products/default-product.avif" alt="${p.name}" class="product-image" />
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="product-info">
                            <c:if test="${p.hasSale}">
                                <p class="product-badge">Giảm giá</p>
                            </c:if>
                            <h3 class="product-name">${p.name}</h3>
                            <p class="product-category">${p.categoryName}</p>
                            <p class="product-price">
                                <c:choose>
                                    <c:when test="${p.hasSale}">
                                        <span class="sale-price"><fmt:formatNumber value="${p.salePrice}" type="number" maxFractionDigits="0"/>₫</span>
                                        <span class="old-price"><fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>₫</span>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/>₫
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Pagination -->
    <c:if test="${searchResult.totalPages > 1}">
        <div class="pagination" id="paginationContainer">
            <c:forEach var="i" begin="1" end="${searchResult.totalPages}">
                <c:url var="pageUrl" value="/search">
                    <c:param name="page" value="${i}" />
                    <c:param name="q" value="${query}" />
                    <c:if test="${not empty category}"><c:param name="category" value="${category}" /></c:if>
                    <c:if test="${not empty sort}"><c:param name="sort" value="${sort}" /></c:if>
                </c:url>
                <a href="${env}${pageUrl}" class="page-link${i==searchResult.page? ' active' : ''}">${i}</a>
            </c:forEach>
        </div>
    </c:if>
</div>

<script>
    window.__env = '${env}';
</script>

<script src="${env}/js/customer/pages/search.js"></script>
</body>
</html>
