<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cfmt" uri="/WEB-INF/tlds/currency.tld" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="selectedGenders" value="," />
<c:if test="${not empty paramValues.genders}">
    <c:set var="selectedGenders" value=",${fn:join(paramValues.genders, ',')}," />
</c:if>
<c:set var="selectedSizes" value="," />
<c:if test="${not empty paramValues.sizes}">
    <c:set var="selectedSizes" value=",${fn:join(paramValues.sizes, ',')}," />
</c:if>
<c:set var="selectedColors" value="," />
<c:if test="${not empty paramValues.colors}">
    <c:set var="selectedColors" value=",${fn:join(paramValues.colors, ',')}," />
</c:if>
<c:set var="selectedShoeHeights" value="," />
<c:if test="${not empty paramValues.shoeHeights}">
    <c:set var="selectedShoeHeights" value=",${fn:join(paramValues.shoeHeights, ',')}," />
</c:if>
<c:set var="selectedWidths" value="," />
<c:if test="${not empty paramValues.widths}">
    <c:set var="selectedWidths" value=",${fn:join(paramValues.widths, ',')}," />
</c:if>
<c:set var="selectedSports" value="," />
<c:if test="${not empty paramValues.sports}">
    <c:set var="selectedSports" value=",${fn:join(paramValues.sports, ',')}," />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${not empty selectedCategoryName}">
                <c:out value="${selectedCategoryName}" /> | Nike VN
            </c:when>
            <c:otherwise>Men's Shoes | Nike VN</c:otherwise>
        </c:choose>
    </title>

    <jsp:include page="/WEB-INF/views/user/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/user/imported/product-list.jsp" />

</head>

<body>
    <jsp:include page="/WEB-INF/views/user/layout/header.jsp" />

    <!-- Main Content -->
    <main class="main" role="main" aria-labelledby="page-title">
        <div class="catalog-shell">
            <div class="product-header" role="region" aria-labelledby="page-title">
                <div class="product-title-section">
                    <h1 class="product-title" id="page-title">
                        <span id="page-title-label">
                            <c:choose>
                                <c:when test="${not empty selectedCategoryName}">
                                    <c:out value="${selectedCategoryName}"/>
                                </c:when>
                                <c:otherwise>All Shoes</c:otherwise>
                            </c:choose>
                        </span>
                        <span id="page-title-count"><c:out value=" ${totalProducts}"/></span>
                    </h1>
                    <div class="mobile-category-tabs" role="navigation" aria-label="Product categories">
                        <c:forEach var="cat" items="${categories}">
                            <a href="${env}/products/list?categoryId=${cat.id}<c:if test='${not empty sort}'>&amp;sort=${sort}</c:if>"
                               data-category-link="true"
                               data-category-id="${cat.id}"
                               class="mobile-category-tab<c:if test='${cat.id == selectedCategoryId}'> active</c:if>">
                                <c:out value="${cat.name}"/>
                            </a>
                        </c:forEach>
                    </div>
                </div>
                <div class="header-controls" role="group" aria-label="Page controls">
                    <div class="mobile-filter-chip-track" aria-label="Mobile sort and filter chips">
                    <button class="mobile-filter-btn" type="button" data-mobile-filters-toggle="open" style="display: none;"
                            aria-label="Hiá»ƒn thá»‹ bá»™ lá»c" aria-describedby="mobile-filter-desc">
                        <i class="fas fa-filter" aria-hidden="true"></i>
                        Bá»™ lá»c
                    </button>
                    <span id="mobile-filter-desc" class="sr-only">Má»Ÿ thanh bá»™ lá»c Ä‘á»ƒ thu háº¹p danh sÃ¡ch sáº£n pháº©m</span>
                    <button class="hide-filters-btn" type="button" aria-label="Chuyá»ƒn Ä‘á»•i hiá»ƒn thá»‹ bá»™ lá»c"
                            aria-describedby="hide-filters-desc">
                        <span class="hide-filters-label">Ẩn bộ lọc</span>
                        <svg aria-hidden="true" class="icon-filter-ds" focusable="false" viewBox="0 0 24 24" role="img" width="24px" height="24px" fill="none">
    <path stroke="currentColor" stroke-width="1.5" d="M21 8.25H10m-5.25 0H3"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M7.5 6v0a2.25 2.25 0 100 4.5 2.25 2.25 0 000-4.5z" clip-rule="evenodd"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M3 15.75h10.75m5 0H21"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M16.5 13.5v0a2.25 2.25 0 100 4.5 2.25 2.25 0 000-4.5z" clip-rule="evenodd"></path>
  </svg>
                    </button>
                    <span id="hide-filters-desc" class="sr-only">áº¨n hoáº·c hiá»‡n báº£ng bá»™ lá»c sáº£n pháº©m</span>
                    <div class="sort-by-btn" id="sortByBtn">
                        <button id="sort-toggle" class="sort-btn" type="button" aria-haspopup="listbox" aria-expanded="false" aria-controls="sort-options">
                            Sắp xếp theo
                            <svg id="sort-arrow" width="20" height="20" class="sort-arrow-icon" viewBox="0 0 20 20"><path d="M6 8l4 4 4-4" stroke="currentColor" stroke-width="2" fill="none"/></svg>
                        </button>
                        <form id="sort-form" method="GET" action="${env}/products/list">
                            <ul id="sort-options" class="sort-options" role="listbox" tabindex="-1">
                                <li class="sort-option${param.sort == 'newest' || empty param.sort ? ' active' : ''}" data-value="newest" role="option" aria-selected="${param.sort == 'newest' || empty param.sort ? 'true' : 'false'}">Mới nhất</li>
                                <li class="sort-option${param.sort == 'price_desc' ? ' active' : ''}" data-value="price_desc" role="option" aria-selected="${param.sort == 'price_desc' ? 'true' : 'false'}">Giá cao nhất</li>
                                <li class="sort-option${param.sort == 'price_asc' ? ' active' : ''}" data-value="price_asc" role="option" aria-selected="${param.sort == 'price_asc' ? 'true' : 'false'}">Giá thấp nhất</li>
                            </ul>
                            <input type="hidden" name="sort" id="sort-input" value="${not empty sort ? sort : 'newest'}" />
                            <c:if test="${not empty selectedCategoryId}">
                                <input type="hidden" name="categoryId" value="${selectedCategoryId}" />
                            </c:if>
                            <c:forEach var="segment" items="${paramValues.segments}">
                                <input type="hidden" name="segments" value="${segment}" />
                            </c:forEach>
                            <c:forEach var="gender" items="${paramValues.genders}">
                                <input type="hidden" name="genders" value="${gender}" />
                            </c:forEach>
                            <c:if test="${param.sale == 'true'}">
                                <input type="hidden" name="sale" value="true" />
                            </c:if>
                            <c:forEach var="size" items="${paramValues.sizes}">
                                <input type="hidden" name="sizes" value="${size}" />
                            </c:forEach>
                            <c:forEach var="color" items="${paramValues.colors}">
                                <input type="hidden" name="colors" value="${color}" />
                            </c:forEach>
                            <c:forEach var="shoeHeight" items="${paramValues.shoeHeights}">
                                <input type="hidden" name="shoeHeights" value="${shoeHeight}" />
                            </c:forEach>
                            <c:forEach var="width" items="${paramValues.widths}">
                                <input type="hidden" name="widths" value="${width}" />
                            </c:forEach>
                            <c:forEach var="sport" items="${paramValues.sports}">
                                <input type="hidden" name="sports" value="${sport}" />
                            </c:forEach>
                        </form>
                    </div>
                    <button class="mobile-filter-chip" type="button" data-mobile-filter-target="gender">Gender</button>
                    <button class="mobile-filter-chip" type="button" data-mobile-filter-target="sale">Sale</button>
                    <button class="mobile-filter-chip" type="button" data-mobile-filter-target="size">Size</button>
                    <button class="mobile-filter-chip" type="button" data-mobile-filter-target="colour">Colour</button>
                    <button class="mobile-filter-chip" type="button" data-mobile-filter-target="sports">Sports</button>
                    </div>
                    <span id="sort-desc" class="sr-only">Change the order products are displayed</span>
                </div>
            </div>
            <div class="product-listing">
            <div class="sidebar" role="complementary" aria-labelledby="filters-title">
                <!-- Nike-style sidebar categories -->
                <div class="left-nav-categories" role="navigation" aria-labelledby="categories-title">
                    <div class="left-nav-container">
                        <div class="categories__content">
                            <h2 id="categories-title" class="sr-only">Product Categories</h2>
                            <!-- Dynamic categories list -->
                            <c:forEach var="cat" items="${categories}">
                                <a href="${env}/products/list?categoryId=${cat.id}<c:if test='${not empty sort}'>&amp;sort=${sort}</c:if>"
                                   data-category-link="true"
                                   data-category-id="${cat.id}"
                                   class="categories__item<c:if test='${cat.id == selectedCategoryId}'> active</c:if>"
                                   aria-describedby="${cat.id}-desc">
                                    <c:out value="${cat.name}"/>
                                    <span id="${cat.id}-desc" class="sr-only">Browse <c:out value='${cat.name}'/> shoes collection</span>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <aside class="filters" id="left-nav" role="region" aria-labelledby="filters-title">
                    <div class="filters-inner">
                        <form id="catalog-filter-form" method="GET" action="${env}/products/list" aria-label="Product filters">
                            <h2 id="filters-title" class="sr-only">Filter Products</h2>
                            <input type="hidden" name="sort" value="${not empty sort ? sort : 'newest'}" id="catalog-sort-input" />
                            <c:if test="${not empty selectedCategoryId}">
                                <input type="hidden" name="categoryId" value="${selectedCategoryId}" />
                            </c:if>
                            <c:forEach var="segment" items="${paramValues.segments}">
                                <input type="hidden" name="segments" value="${segment}" />
                            </c:forEach>
                            <div class="filter-group" role="group" aria-label="Product filtering options">
                                <div class="filters-group__wrapper">
                                    <div class="filters-group__close" data-mobile-filter-key="gender" aria-expanded="false">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by gender" role="button"
                                                 class="trigger-content" tabindex="0" aria-describedby="gender-filter-desc">
                                                <div class="trigger-content__label">
                                                    Gender
                                                    <span class="filter-group_count"></span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg"
                                                     fill="#000000" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="gender-filter-desc" class="sr-only">Click to expand gender filter options</span>
                                        </span>
                                        <div class="filter-panel" data-filter-panel hidden>
                                            <label class="filter-option">
                                                <input type="checkbox" name="genders" value="MEN"
                                                       <c:if test="${fn:contains(selectedGenders, ',MEN,')}">checked</c:if>>
                                                <span>Men</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="genders" value="WOMEN"
                                                       <c:if test="${fn:contains(selectedGenders, ',WOMEN,')}">checked</c:if>>
                                                <span>Women</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="genders" value="UNISEX"
                                                       <c:if test="${fn:contains(selectedGenders, ',UNISEX,')}">checked</c:if>>
                                                <span>Unisex</span>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="filters-group__close" data-mobile-filter-key="sale" aria-expanded="false">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by sale offers" role="button"
                                                 class="trigger-content" tabindex="0" aria-describedby="sale-filter-desc">
                                                <div class="trigger-content__label">
                                                    Sale &amp; Offers
                                                    <span class="filter-group_count"></span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg"
                                                     fill="#000000" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="sale-filter-desc" class="sr-only">Click to expand sale filter options</span>
                                        </span>
                                        <div class="filter-panel" data-filter-panel hidden>
                                            <label class="filter-option">
                                                <input type="checkbox" name="sale" value="true"
                                                       <c:if test="${param.sale == 'true'}">checked</c:if>>
                                                <span>Sale</span>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="filters-group__close" data-mobile-filter-key="size" aria-expanded="false">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by shoe size" role="button"
                                                 class="trigger-content" tabindex="0" aria-describedby="size-filter-desc">
                                                <div class="trigger-content__label">
                                                    Size
                                                    <span class="filter-group_count"></span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg"
                                                     fill="#000000" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="size-filter-desc" class="sr-only">Click to expand size filter options</span>
                                        </span>
                                        <div class="filter-panel" data-filter-panel hidden>
                                            <c:choose>
                                                <c:when test="${not empty availableSizes}">
                                                    <c:forEach var="sizeOption" items="${availableSizes}">
                                                        <c:set var="sizeToken" value=",${sizeOption}," />
                                                        <label class="filter-option">
                                                            <input type="checkbox" name="sizes" value="${sizeOption}"
                                                                   <c:if test="${fn:contains(selectedSizes, sizeToken)}">checked</c:if>>
                                                            <span><c:out value="${sizeOption}" /></span>
                                                        </label>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <p class="filter-note">Waiting for backend size options.</p>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="filters-group__close" data-mobile-filter-key="colour" aria-expanded="false">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by colour" role="button"
                                                 class="trigger-content" tabindex="0" aria-describedby="colour-filter-desc">
                                                <div class="trigger-content__label">
                                                    Colour
                                                    <span class="filter-group_count"></span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg"
                                                     fill="#000000" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="colour-filter-desc" class="sr-only">Click to expand colour filter options</span>
                                        </span>
                                        <div class="filter-panel" data-filter-panel hidden>
                                            <c:choose>
                                                <c:when test="${not empty availableColors}">
                                                    <c:forEach var="colorOption" items="${availableColors}">
                                                        <c:set var="colorToken" value=",${colorOption}," />
                                                        <label class="filter-option">
                                                            <input type="checkbox" name="colors" value="${colorOption}"
                                                                   <c:if test="${fn:contains(selectedColors, colorToken)}">checked</c:if>>
                                                            <span><c:out value="${colorOption}" /></span>
                                                        </label>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <p class="filter-note">Waiting for backend colour options.</p>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                    <div class="filters-group__close" data-mobile-filter-key="height" aria-expanded="false">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by shoe height" role="button"
                                                 class="trigger-content" tabindex="0" aria-describedby="height-filter-desc">
                                                <div class="trigger-content__label">
                                                    Shoe Height
                                                    <span class="filter-group_count"></span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg"
                                                     fill="#000000" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="height-filter-desc" class="sr-only">Click to expand shoe height filter options</span>
                                        </span>
                                        <div class="filter-panel" data-filter-panel hidden>
                                            <label class="filter-option">
                                                <input type="checkbox" name="shoeHeights" value="LOW_TOP"
                                                       <c:if test="${fn:contains(selectedShoeHeights, ',LOW_TOP,')}">checked</c:if>>
                                                <span>Low top</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="shoeHeights" value="MID_TOP"
                                                       <c:if test="${fn:contains(selectedShoeHeights, ',MID_TOP,')}">checked</c:if>>
                                                <span>Mid top</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="shoeHeights" value="HIGH_TOP"
                                                       <c:if test="${fn:contains(selectedShoeHeights, ',HIGH_TOP,')}">checked</c:if>>
                                                <span>High top</span>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="filters-group__close" data-mobile-filter-key="width" aria-expanded="false">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by shoe width" role="button"
                                                 class="trigger-content" tabindex="0" aria-describedby="width-filter-desc">
                                                <div class="trigger-content__label">
                                                    Width
                                                    <span class="filter-group_count"></span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg"
                                                     fill="#000000" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="width-filter-desc" class="sr-only">Click to expand width filter options</span>
                                        </span>
                                        <div class="filter-panel" data-filter-panel hidden>
                                            <label class="filter-option">
                                                <input type="checkbox" name="widths" value="REGULAR"
                                                       <c:if test="${fn:contains(selectedWidths, ',REGULAR,')}">checked</c:if>>
                                                <span>Regular</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="widths" value="WIDE"
                                                       <c:if test="${fn:contains(selectedWidths, ',WIDE,')}">checked</c:if>>
                                                <span>Wide</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="widths" value="EXTRA_WIDE"
                                                       <c:if test="${fn:contains(selectedWidths, ',EXTRA_WIDE,')}">checked</c:if>>
                                                <span>Extra wide</span>
                                            </label>
                                        </div>
                                    </div>
                                    <div class="filters-group__close" data-mobile-filter-key="sports" aria-expanded="false">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by sport" role="button"
                                                 class="trigger-content" tabindex="0" aria-describedby="sports-filter-desc">
                                                <div class="trigger-content__label">
                                                    Sports
                                                    <span class="filter-group_count"></span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg"
                                                     fill="#000000" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="sports-filter-desc" class="sr-only">Click to expand sports filter options</span>
                                        </span>
                                        <div class="filter-panel" data-filter-panel hidden>
                                            <label class="filter-option">
                                                <input type="checkbox" name="sports" value="LIFESTYLE"
                                                       <c:if test="${fn:contains(selectedSports, ',LIFESTYLE,')}">checked</c:if>>
                                                <span>Lifestyle</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="sports" value="RUNNING"
                                                       <c:if test="${fn:contains(selectedSports, ',RUNNING,')}">checked</c:if>>
                                                <span>Running</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="sports" value="TRAINING_GYM"
                                                       <c:if test="${fn:contains(selectedSports, ',TRAINING_GYM,')}">checked</c:if>>
                                                <span>Training &amp; gym</span>
                                            </label>
                                            <label class="filter-option">
                                                <input type="checkbox" name="sports" value="BASKETBALL"
                                                       <c:if test="${fn:contains(selectedSports, ',BASKETBALL,')}">checked</c:if>>
                                                <span>Basketball</span>
                                            </label>
                                            <label class="filter-option filter-option--more" hidden>
                                                <input type="checkbox" name="sports" value="FOOTBALL"
                                                       <c:if test="${fn:contains(selectedSports, ',FOOTBALL,')}">checked</c:if>>
                                                <span>Football</span>
                                            </label>
                                            <label class="filter-option filter-option--more" hidden>
                                                <input type="checkbox" name="sports" value="TENNIS"
                                                       <c:if test="${fn:contains(selectedSports, ',TENNIS,')}">checked</c:if>>
                                                <span>Tennis</span>
                                            </label>
                                            <label class="filter-option filter-option--more" hidden>
                                                <input type="checkbox" name="sports" value="SKATEBOARDING"
                                                       <c:if test="${fn:contains(selectedSports, ',SKATEBOARDING,')}">checked</c:if>>
                                                <span>Skateboarding</span>
                                            </label>
                                            <button class="filter-more-btn" type="button" data-sports-toggle="false">+More</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </aside>
            </div>
            <div class="product-list-content">
                <!-- Product List header -->
                <div class="product-header product-header-legacy" hidden aria-hidden="true" role="region" aria-labelledby="page-title-legacy">
                    <div class="product-title-section">
                        <h1 class="product-title" id="page-title-legacy">
                            <span id="page-title-label-legacy">
                                <c:choose>
                                    <c:when test="${not empty selectedCategoryName}">
                                        <c:out value="${selectedCategoryName}"/>
                                    </c:when>
                                    <c:otherwise>All Shoes</c:otherwise>
                                </c:choose>
                            </span>
                            <span id="page-title-count-legacy"><c:out value=" ${totalProducts}"/></span>
                        </h1>
                    </div>
                    <div class="header-controls" role="group" aria-label="Page controls">
                        <button class="mobile-filter-btn" type="button" data-mobile-filters-toggle="open" style="display: none;"
                                aria-label="Hiển thị bộ lọc" aria-describedby="mobile-filter-desc">
                            <i class="fas fa-filter" aria-hidden="true"></i>
                            Bộ lọc
                        </button>
                        <span id="mobile-filter-desc" class="sr-only">Mở thanh bộ lọc để thu hẹp danh sách sản phẩm</span>
                        <button class="hide-filters-btn" type="button" aria-label="Chuyển đổi hiển thị bộ lọc"
                                aria-describedby="hide-filters-desc">
                            <span class="hide-filters-label">Ẩn bộ lọc</span>
                            <svg aria-hidden="true" class="icon-filter-ds" focusable="false" viewBox="0 0 24 24" role="img" width="24px" height="24px" fill="none">
    <path stroke="currentColor" stroke-width="1.5" d="M21 8.25H10m-5.25 0H3"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M7.5 6v0a2.25 2.25 0 100 4.5 2.25 2.25 0 000-4.5z" clip-rule="evenodd"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M3 15.75h10.75m5 0H21"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M16.5 13.5v0a2.25 2.25 0 100 4.5 2.25 2.25 0 000-4.5z" clip-rule="evenodd"></path>
  </svg>
                        </button>
                        <span id="hide-filters-desc" class="sr-only">Ẩn hoặc hiện bảng bộ lọc sản phẩm</span>
                        <div class="sort-by-btn" id="sortByBtnLegacy">
                            <button id="sort-toggle-legacy" class="sort-btn" type="button" aria-haspopup="listbox" aria-expanded="false" aria-controls="sort-options-legacy">
                                Sắp xếp theo
                                <svg id="sort-arrow-legacy" width="20" height="20" class="sort-arrow-icon" viewBox="0 0 20 20"><path d="M6 8l4 4 4-4" stroke="currentColor" stroke-width="2" fill="none"/></svg>
                            </button>
                            <form id="sort-form-legacy" method="GET" action="${env}/products/list">
                                <ul id="sort-options-legacy" class="sort-options" role="listbox" tabindex="-1">
                                    <li class="sort-option${param.sort == 'newest' || empty param.sort ? ' active' : ''}" data-value="newest" role="option" aria-selected="${param.sort == 'newest' || empty param.sort ? 'true' : 'false'}">Mới nhất</li>
                                    <li class="sort-option${param.sort == 'price_desc' ? ' active' : ''}" data-value="price_desc" role="option" aria-selected="${param.sort == 'price_desc' ? 'true' : 'false'}">Giá: Cao → Thấp</li>
                                    <li class="sort-option${param.sort == 'price_asc' ? ' active' : ''}" data-value="price_asc" role="option" aria-selected="${param.sort == 'price_asc' ? 'true' : 'false'}">Giá: Thấp → Cao</li>
                                </ul>
                                <input type="hidden" name="sort" id="sort-input-legacy" value="${not empty sort ? sort : 'newest'}" />
                                <c:if test="${not empty selectedCategoryId}">
                                    <input type="hidden" name="categoryId" value="${selectedCategoryId}" />
                                </c:if>
                                <c:forEach var="segment" items="${paramValues.segments}">
                                    <input type="hidden" name="segments" value="${segment}" />
                                </c:forEach>
                                <c:forEach var="gender" items="${paramValues.genders}">
                                    <input type="hidden" name="genders" value="${gender}" />
                                </c:forEach>
                                <c:if test="${param.sale == 'true'}">
                                    <input type="hidden" name="sale" value="true" />
                                </c:if>
                                <c:forEach var="size" items="${paramValues.sizes}">
                                    <input type="hidden" name="sizes" value="${size}" />
                                </c:forEach>
                                <c:forEach var="color" items="${paramValues.colors}">
                                    <input type="hidden" name="colors" value="${color}" />
                                </c:forEach>
                                <c:forEach var="shoeHeight" items="${paramValues.shoeHeights}">
                                    <input type="hidden" name="shoeHeights" value="${shoeHeight}" />
                                </c:forEach>
                                <c:forEach var="width" items="${paramValues.widths}">
                                    <input type="hidden" name="widths" value="${width}" />
                                </c:forEach>
                                <c:forEach var="sport" items="${paramValues.sports}">
                                    <input type="hidden" name="sports" value="${sport}" />
                                </c:forEach>
                            </form>
                        </div>
                        <span id="sort-desc" class="sr-only">Change the order products are displayed</span>
                    </div>
                </div>

                <section aria-labelledby="products-section-title">
                    <h2 id="products-section-title" class="sr-only">Product List</h2>
                    <!-- Product Grid -->
                    <div class="product-grid__items" id="skip-to-content" role="region"
                         aria-labelledby="products-section-title" aria-live="polite">
                        <c:choose>
                            <c:when test="${empty products}">
                                <div style="padding:2rem; text-align:center; color:red; font-weight:bold;">
                                    Không tìm thấy sản phẩm.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="product" items="${products}" varStatus="status">
                                    <div class="product-card product-grid__card" data-testid="product-card"
                                         data-product-card="true"
                                         data-price="${product.hasSale ? product.salePrice : product.price}"
                                         data-has-sale="${product.hasSale}"
                                         role="article" aria-labelledby="product-${status.index + 1}-title">
                                        <div class="product-card__body" data-testid="product-card__body">
                                            <figure>
                                                <a aria-label="${product.name}" href="${env}/product-detail?id=${product.id}"
                                                   class="product-card__img-link-overlay" data-testid="product-card-img-link-overlay"
                                                   aria-describedby="product-${status.index + 1}-desc">
                                                        <div class="wall-image-loader content-card__image" data-testid="wall-image-loader">
                                                        <c:choose>
                                                            <c:when test="${not empty product.heroImg}">
                                                                <img src="${product.heroImg}"
                                                                     alt="${product.name}" loading="lazy"
                                                                     aria-describedby="product-${status.index + 1}-desc">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img src="${env}/images/products/product-1.jpg"
                                                                     alt="${product.name}" loading="lazy"
                                                                     aria-describedby="product-${status.index + 1}-desc">
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </a>
                                                <div class="product-card__info">
                                                    <div class="product_msg_info">
                                                        <div class="product-card__messaging" role="status" aria-label="Product status">
                                                            <c:out value="${product.status.displayName}" />
                                                        </div>
                                                        <div class="product-card__titles">
                                                            <div class="product-card__title" id="product-${status.index + 1}-title">
                                                                ${product.name}
                                                            </div>
                                                            <div class="product-card__subtitle" id="product-${status.index + 1}-desc">
                                                                <c:out value="${product.categoryName}" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="product-card__count-wrapper" data-testid="product-card__count-wrapper">
                                                    <div class="product-card__count-item">
                                                        <button class="product-card__colorway-btn" type="button"
                                                                aria-label="View color options for ${product.name}">
                                                            <div aria-label="Available in ${product.colorCount} color" class="product-card__product-count">
                                                                ${product.colorCount} Colour<c:if test="${product.colorCount > 1}">s</c:if>
                                                            </div>
                                                        </button>
                                                    </div>
                                                </div>
                                                <div class="product-card__price-wrapper">
                                                    <div class="product-card__price-container">
                                                        <div class="product-card__price" data-testid="product-card__price">
                                                            <c:choose>
                                                                <c:when test="${product.hasSale}">
                                                                    <span class="sale-price">
                                                                        ${cfmt:format(product.salePrice)}
                                                                    </span>
                                                                    <span class="orig-price" style="text-decoration:line-through; color:#777; margin-left:4px;">
                                                                        ${cfmt:format(product.price)}
                                                                    </span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${cfmt:format(product.price)}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </div>
                                                </div>
                                            </figure>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div id="client-filter-empty" class="filter-empty-state" hidden>
                        Không có sản phẩm nào phù hợp với bộ lọc hiện tại.
                    </div>
                </section>
            </div>
        </div>
        </div>

        <!-- Related Stories Section -->
        <section class="related-stories-section" aria-labelledby="related-stories-title">
            <div class="container">
                <div class="related-stories-header">
                    <h2 class="section-title" id="related-stories-title">Bài viết liên quan</h2>
                    <div class="carousel-controls" role="group" aria-label="Story carousel navigation">
                        <button class="story-nav-btn prev" id="storyPrev"
                                aria-label="Bài trước" aria-controls="storySliderTrack">
                            <i class="fas fa-chevron-left" aria-hidden="true"></i>
                        </button>
                        <button class="story-nav-btn next" id="storyNext"
                                aria-label="Bài tiếp theo" aria-controls="storySliderTrack">
                            <i class="fas fa-chevron-right" aria-hidden="true"></i>
                        </button>
                    </div>
                </div>

                <div class="story-slider-container" role="region" aria-labelledby="related-stories-title"
                     aria-describedby="stories-desc">
                    <span id="stories-desc" class="sr-only">Băng chuyền các bài viết và hướng dẫn mua sắm liên quan đến Nike</span>
                    <div class="story-slider-track" id="storySliderTrack" role="list" aria-live="polite">
                        <div class="story-slide" data-story="winter-sneakers" role="listitem"
                             aria-labelledby="story-1-title" tabindex="0">
                            <img src="${env}/images/the-best-nike-sneakers-to-wear-in-the-winter.jpg"
                                 alt="The Best Nike Sneakers to Wear in Winter" aria-describedby="story-1-desc">
                            <div class="story-button" role="button" aria-describedby="story-1-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Hướng dẫn mua</span>
                                    <span class="story-title" id="story-1-title">The Best Nike Sneakers to Wear in Winter</span>
                                </div>
                            </div>
                            <span id="story-1-desc" class="sr-only">Đọc hướng dẫn của chúng tôi về những đôi sneaker Nike tốt nhất cho mùa đông</span>
                        </div>

                        <div class="story-slide" data-story="cycling-gifts" role="listitem"
                             aria-labelledby="story-2-title" tabindex="0">
                            <img src="${env}/images/the-11-best-nike-gifts-for-cyclists.jpg"
                                 alt="The 11 Best Nike Gifts for Cyclists" aria-describedby="story-2-desc">
                            <div class="story-button" role="button" aria-describedby="story-2-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Hướng dẫn mua</span>
                                    <span class="story-title" id="story-2-title">The 11 Best Nike Gifts for Cyclists</span>
                                </div>
                            </div>
                            <span id="story-2-desc" class="sr-only">Khám phá trang bị và quà tặng Nike tốt nhất cho người đam mê đạp xe</span>
                        </div>

                        <div class="story-slide" data-story="winter-running" role="listitem"
                             aria-labelledby="story-3-title" tabindex="0">
                            <img src="${env}/images/the-best-nike-running-shoes-for-winter.jpg"
                                 alt="The Best Nike Running Shoes for Winter" aria-describedby="story-3-desc">
                            <div class="story-button" role="button" aria-describedby="story-3-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Hướng dẫn mua</span>
                                    <span class="story-title" id="story-3-title">The Best Nike Running Shoes for Winter</span>
                                </div>
                            </div>
                            <span id="story-3-desc" class="sr-only">Tìm đôi giày chạy Nike hoàn hảo cho việc tập luyện trong thời tiết lạnh</span>
                        </div>

                        <div class="story-slide" data-story="standing-shoes" role="listitem"
                             aria-labelledby="story-4-title" tabindex="0">
                            <img src="${env}/images/the-best-shoes-for-standing-all-day.jpg"
                                 alt="The Best Shoes for Standing All Day" aria-describedby="story-4-desc">
                            <div class="story-button" role="button" aria-describedby="story-4-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Hướng dẫn mua</span>
                                    <span class="story-title" id="story-4-title">The Best Shoes for Standing All Day</span>
                                </div>
                            </div>
                            <span id="story-4-desc" class="sr-only">Learn about the most comfortable Nike shoes for long periods of standing</span>
                        </div>

                        <div class="story-slide" data-story="jeans-shoes" role="listitem"
                             aria-labelledby="story-5-title" tabindex="0">
                            <img src="${env}/images/the-best-shoes-to-wear-with-jeans.jpg"
                                 alt="The Best Shoes to Wear with Jeans" aria-describedby="story-5-desc">
                            <div class="story-button" role="button" aria-describedby="story-5-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Hướng dẫn phối đồ</span>
                                    <span class="story-title" id="story-5-title">The Best Shoes to Wear with Jeans</span>
                                </div>
                            </div>
                            <span id="story-5-desc" class="sr-only">Hướng dẫn phối đồ: kết hợp giày Nike với quần jeans</span>
                        </div>

                        <div class="story-slide" data-story="air-max-history" role="listitem"
                             aria-labelledby="story-6-title" tabindex="0">
                            <img src="${env}/images/the-history-of-the-air-max-97.jpg"
                                 alt="The History of the Air Max 97" aria-describedby="story-6-desc">
                            <div class="story-button" role="button" aria-describedby="story-6-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Feature story</span>
                                    <span class="story-title" id="story-6-title">The History of the Air Max 97</span>
                                </div>
                            </div>
                            <span id="story-6-desc" class="sr-only">Explore the fascinating history and design of the iconic Air Max 97</span>
                        </div>

                        <div class="story-slide" data-story="winter-gear" role="listitem"
                             aria-labelledby="story-7-title" tabindex="0">
                            <img src="${env}/images/the-best-winter-running-gear-by-nike-to-shop-now.jpg"
                                 alt="The Best Winter Running Gear by Nike" aria-describedby="story-7-desc">
                            <div class="story-button" role="button" aria-describedby="story-7-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Hướng dẫn mua</span>
                                    <span class="story-title" id="story-7-title">The Best Winter Running Gear by Nike</span>
                                </div>
                            </div>
                            <span id="story-7-desc" class="sr-only">Complete guide to Nike winter running gear and apparel</span>
                        </div>

                        <div class="story-slide" data-story="tennis-gifts" role="listitem"
                             aria-labelledby="story-8-title" tabindex="0">
                            <img src="${env}/images/13-nike-tennis-gifts-for-players-of-all-levels.jpg"
                                 alt="13 Nike Tennis Gifts for Players" aria-describedby="story-8-desc">
                            <div class="story-button" role="button" aria-describedby="story-8-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Hướng dẫn mua</span>
                                    <span class="story-title" id="story-8-title">Gợi ý 13 Quà tặng Nike Tennis</span>
                                </div>
                            </div>
                            <span id="story-8-desc" class="sr-only">Khám phá set  Nike tennis phù hợp cho người chơi với mọi trình độ</span>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <div class="mobile-filter-hub-backdrop" data-mobile-hub-backdrop hidden></div>
    <section class="mobile-filter-hub" data-mobile-filter-hub hidden aria-hidden="true"
             role="dialog" aria-modal="true" aria-labelledby="mobile-filter-hub-title">
        <div class="mobile-filter-hub__panel">
            <div class="mobile-filter-hub__header">
                <h2 class="mobile-filter-hub__title" id="mobile-filter-hub-title">Filter</h2>
                <button class="mobile-filter-hub__close" type="button" data-mobile-hub-close aria-label="Close filter hub">
                    <i class="fas fa-times" aria-hidden="true"></i>
                </button>
            </div>
            <div class="mobile-filter-hub__content" data-mobile-hub-content></div>
            <div class="mobile-filter-hub__footer">
                <button class="mobile-filter-hub__apply" type="button" data-mobile-hub-apply disabled>Apply</button>
            </div>
        </div>
    </section>

    <div class="mobile-filter-sheet-backdrop" data-mobile-sheet-backdrop hidden></div>
    <section class="mobile-filter-sheet" data-mobile-filter-sheet hidden aria-hidden="true"
             role="dialog" aria-modal="true" aria-labelledby="mobile-filter-sheet-title">
        <div class="mobile-filter-sheet__panel">
            <div class="mobile-filter-sheet__header">
                <h2 class="mobile-filter-sheet__title" id="mobile-filter-sheet-title" data-mobile-sheet-title>Filters</h2>
                <button class="mobile-filter-sheet__close" type="button" data-mobile-sheet-close aria-label="Close filter sheet">
                    <i class="fas fa-times" aria-hidden="true"></i>
                </button>
            </div>
            <div class="mobile-filter-sheet__content" data-mobile-sheet-content></div>
            <div class="mobile-filter-sheet__footer">
                <button class="mobile-filter-sheet__apply" type="button" data-mobile-sheet-apply disabled>Apply</button>
            </div>
        </div>
    </section>

    <jsp:include page="/WEB-INF/views/user/layout/footer.jsp" />

    <!-- Cart Sidebar -->
    <div class="cart-sidebar" id="cartSidebar" role="dialog" aria-labelledby="cart-title"
         aria-describedby="cart-description">
        <div class="cart-header">
            <h3 id="cart-title">Giỏ hàng</h3>
            <button class="cart-close" type="button" data-cart-toggle="close" aria-label="Đóng giỏ hàng">
                <i class="fas fa-times" aria-hidden="true"></i>
            </button>
        </div>

        <div class="cart-content" id="cart-description" aria-live="polite">
            <div class="cart-empty" id="cartEmpty" aria-label="Empty cart state">
                <i class="fas fa-shopping-bag" aria-hidden="true"></i>
                <p>Giỏ hàng của bạn đang trống</p>
                <a href="${env}/products/list" class="btn btn-primary" role="button"
                   aria-label="Bắt đầu mua sắm để thêm sản phẩm vào giỏ hàng">Mua ngay</a>
            </div>

            <div class="cart-items" id="cartItems" aria-label="Items in your shopping bag">
            </div>
        </div>

        <div class="cart-footer" id="cartFooter" style="display: none;" role="region" aria-labelledby="cart-total-title">
                    <div class="cart-total">
                <h4 id="cart-total-title" class="sr-only">Tóm tắt đơn hàng</h4>
                    <div class="total-row">
                    <span>Tạm tính</span>
                    <span id="cartSubtotal" aria-label="Số tiền tạm tính">${cfmt:format(0)}</span>
                </div>
                <div class="total-row">
                    <span>Phí vận chuyển ước tính</span>
                    <span aria-label="Delivery cost">Miễn phí</span>
                </div>
                <div class="total-row total-final">
                    <span>Tổng</span>
                    <span id="cartTotal" aria-label="Total amount">${cfmt:format(0)}</span>
                </div>
            </div>
            <button class="btn btn-primary btn-full" aria-describedby="member-checkout-desc">
                Thanh toán thành viên
            </button>
            <span id="member-checkout-desc" class="sr-only">Thanh toán như thành viên Nike để nhận ưu đãi</span>
            <button class="btn btn-outline btn-full" aria-describedby="guest-checkout-desc">
                Thanh toán cho khách
            </button>
            <span id="guest-checkout-desc" class="sr-only">Thanh toán không cần tạo tài khoản</span>
        </div>
    </div>

    <!-- JavaScript -->
    <jsp:include page="/WEB-INF/views/user/layout/js.jsp" />
    <script src="${env}/js/customer/components/carousel.js" defer></script>
    <script src="${env}/js/customer/pages/product-list-filters.js" defer></script>
</body>
</html>
