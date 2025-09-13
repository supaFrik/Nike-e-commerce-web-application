<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Men's Shoes. Nike VN</title>
        
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/product-list.jsp" />

</head>

<body>
    <jsp:include page="/WEB-INF/views/customer/layout/header.jsp" />

    <!-- Main Content -->
    <main class="main" role="main" aria-labelledby="page-title">
        <div class="product-listing">
            <div class="sidebar" role="complementary" aria-labelledby="filters-title">
                <!-- Mobile close button -->
                <button class="mobile-filter-close" onclick="toggleMobileFilters()" style="display: none;" 
                        aria-label="Close filters">
                    <i class="fas fa-times" aria-hidden="true"></i>
                    Close Filters
                </button>
                
                <!-- Nike-style sidebar categories -->
                <div class="left-nav-categories" role="navigation" aria-labelledby="categories-title">
                    <div class="left-nav-container">
                        <div class="categories__content">
                            <h2 id="categories-title" class="sr-only">Product Categories</h2>
                            <a href="${env}/products?category=lifestyle" class="categories__item" aria-describedby="lifestyle-desc">
                                Lifestyle
                                <span id="lifestyle-desc" class="sr-only">Browse lifestyle shoes collection</span>
                            </a>
                            <a href="${env}/products?category=jordan" class="categories__item" aria-describedby="jordan-desc">
                                Jordan
                                <span id="jordan-desc" class="sr-only">Browse Jordan shoes collection</span>
                            </a>
                            <a href="${env}/products?category=running" class="categories__item" aria-describedby="running-desc">
                                Running
                                <span id="running-desc" class="sr-only">Browse running shoes collection</span>
                            </a>
                            <a href="${env}/products?category=basketball" class="categories__item" aria-describedby="basketball-desc">
                                Basketball
                                <span id="basketball-desc" class="sr-only">Browse basketball shoes collection</span>
                            </a>
                            <a href="${env}/products?category=american-football" class="categories__item" aria-describedby="american-football-desc">
                                American Football
                                <span id="american-football-desc" class="sr-only">Browse American football shoes collection</span>
                            </a>
                            <a href="${env}/products?category=football" class="categories__item" aria-describedby="football-desc">
                                Football
                                <span id="football-desc" class="sr-only">Browse football shoes collection</span>
                            </a>
                            <a href="${env}/products?category=training" class="categories__item" aria-describedby="training-desc">
                                Training & Gym
                                <span id="training-desc" class="sr-only">Browse training and gym shoes collection</span>
                            </a>
                            <a href="${env}/products?category=skateboarding" class="categories__item" aria-describedby="skateboarding-desc">
                                Skateboarding
                                <span id="skateboarding-desc" class="sr-only">Browse skateboarding shoes collection</span>
                            </a>
                            <a href="${env}/products?category=golf" class="categories__item" aria-describedby="golf-desc">
                                Golf
                                <span id="golf-desc" class="sr-only">Browse golf shoes collection</span>
                            </a>
                            <a href="${env}/products?category=tennis" class="categories__item" aria-describedby="tennis-desc">
                                Tennis
                                <span id="tennis-desc" class="sr-only">Browse tennis shoes collection</span>
                            </a>
                            <a href="${env}/products?category=athletics" class="categories__item" aria-describedby="athletics-desc">
                                Athletics
                                <span id="athletics-desc" class="sr-only">Browse athletics shoes collection</span>
                            </a>
                            <a href="${env}/products?category=walking" class="categories__item" aria-describedby="walking-desc">
                                Walking
                                <span id="walking-desc" class="sr-only">Browse walking shoes collection</span>
                            </a>
                        </div>
                    </div>
                </div>
                
                <aside class="filters" id="left-nav" role="region" aria-labelledby="filters-title">
                    <div class="filters-inner">
                        <nav aria-label="Product filters">
                            <h2 id="filters-title" class="sr-only">Filter Products</h2>
                            <div class="filter-group" role="group" aria-label="Product filtering options">
                                <div class="filters-group__wrapper">
                                    <div class="filters-group__close">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by gender" role="button" 
                                                 class="trigger-content" tabindex="0" aria-describedby="gender-filter-desc">
                                                <div class="trigger-content__label">
                                                    Gender <span class="filter-group_count">(1)</span>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg" 
                                                     fill="#000000" transform="rotate(180)" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="gender-filter-desc" class="sr-only">Click to expand gender filter options</span>
                                        </span>
                                    </div>
                                    <div class="filters-group__close">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by price range" role="button" 
                                                 class="trigger-content" tabindex="0" aria-describedby="price-filter-desc">
                                                <div class="trigger-content__label">
                                                    Shop By Price
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg" 
                                                     fill="#000000" transform="rotate(180)" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="price-filter-desc" class="sr-only">Click to expand price filter options</span>
                                        </span>
                                    </div>
                                    <div class="filters-group__close">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by shoe size" role="button" 
                                                 class="trigger-content" tabindex="0" aria-describedby="size-filter-desc">
                                                <div class="trigger-content__label">
                                                    Size
                                                    <div class="filter-group_count">
    
                                                    </div>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg" 
                                                     fill="#000000" transform="rotate(180)" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="size-filter-desc" class="sr-only">Click to expand size filter options</span>
                                        </span>
                                    </div>
                                    <div class="filters-group__close">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by shoe height" role="button" 
                                                 class="trigger-content" tabindex="0" aria-describedby="height-filter-desc">
                                                <div class="trigger-content__label">
                                                    Shoe Height
                                                    <div class="filter-group_count">
    
                                                    </div>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg" 
                                                     fill="#000000" transform="rotate(180)" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="height-filter-desc" class="sr-only">Click to expand shoe height filter options</span>
                                        </span>
                                    </div>
                                    <div class="filters-group__close">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by collections" role="button" 
                                                 class="trigger-content" tabindex="0" aria-describedby="collections-filter-desc">
                                                <div class="trigger-content__label">
                                                    Collections
                                                    <div class="filter-group_count">
    
                                                    </div>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg" 
                                                     fill="#000000" transform="rotate(180)" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="collections-filter-desc" class="sr-only">Click to expand collections filter options</span>
                                        </span>
                                    </div>
                                    <div class="filters-group__close">
                                        <span class="filters-group-btn">
                                            <div aria-expanded="false" aria-label="Filter by shoe width" role="button" 
                                                 class="trigger-content" tabindex="0" aria-describedby="width-filter-desc">
                                                <div class="trigger-content__label">
                                                    Width
                                                    <div class="filter-group_count">
    
                                                    </div>
                                                </div>
                                                <svg viewBox="0 0 1024 1024" class="icon" xmlns="http://www.w3.org/2000/svg" 
                                                     fill="#000000" transform="rotate(180)" aria-hidden="true">
                                                    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                                                    <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                                                    <g id="SVGRepo_iconCarrier">
                                                        <path fill="#000000" d="M488.832 344.32l-339.84 356.672a32 32 0 000 44.16l.384.384a29.44 29.44 0 0042.688 0l320-335.872 319.872 335.872a29.44 29.44 0 0042.688 0l.384-.384a32 32 0 000-44.16L535.168 344.32a32 32 0 00-46.336 0z"></path>
                                                    </g>
                                                </svg>
                                            </div>
                                            <span id="width-filter-desc" class="sr-only">Click to expand width filter options</span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </nav>
                    </div>
                </aside>
            </div>
            <div class="container">
                <!-- Product List header -->
                <div class="product-header" role="region" aria-labelledby="page-title">
                    <div class="product-title-section">
                        <h1 class="product-title" id="page-title">Men's Shoes <c:out value="${totalProducts}" default="0"/></h1>
                    </div>
                    <div class="header-controls" role="group" aria-label="Page controls">
                        <button class="mobile-filter-btn" onclick="toggleMobileFilters()" style="display: none;" 
                                aria-label="Show filters" aria-describedby="mobile-filter-desc">
                            <i class="fas fa-filter" aria-hidden="true"></i>
                            Filters
                        </button>
                        <span id="mobile-filter-desc" class="sr-only">Open filter sidebar to narrow product selection</span>
                        <button class="hide-filters-btn" aria-label="Toggle filter visibility" 
                                aria-describedby="hide-filters-desc">
                            Hide Filters
                            <svg aria-hidden="true" class="icon-filter-ds" focusable="false" viewBox="0 0 24 24" role="img" width="24px" height="24px" fill="none">
    <path stroke="currentColor" stroke-width="1.5" d="M21 8.25H10m-5.25 0H3"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M7.5 6v0a2.25 2.25 0 100 4.5 2.25 2.25 0 000-4.5z" clip-rule="evenodd"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M3 15.75h10.75m5 0H21"></path>
    <path stroke="currentColor" stroke-width="1.5" d="M16.5 13.5v0a2.25 2.25 0 100 4.5 2.25 2.25 0 000-4.5z" clip-rule="evenodd"></path>
  </svg>
                        </button>
                        <span id="hide-filters-desc" class="sr-only">Hide or show the product filters panel</span>
                        <div class="sort-by-btn" id="sortByBtn">
                            <button id="sort-toggle" class="sort-btn" type="button" aria-haspopup="listbox" aria-expanded="false" aria-controls="sort-options">
                                Sort By
                                <svg id="sort-arrow" width="20" height="20" class="sort-arrow-icon" viewBox="0 0 20 20"><path d="M6 8l4 4 4-4" stroke="currentColor" stroke-width="2" fill="none"/></svg>
                            </button>
                            <form id="sort-form" method="GET">
                                <ul id="sort-options" class="sort-options" role="listbox" tabindex="-1">
                                    <li class="sort-option${param.sort == 'favourites' || empty param.sort ? ' active' : ''}" data-value="favourites" role="option" aria-selected="${param.sort == 'favourites' || empty param.sort ? 'true' : 'false'}">Favourites</li>
                                    <li class="sort-option${param.sort == 'newest' ? ' active' : ''}" data-value="newest" role="option" aria-selected="${param.sort == 'newest' ? 'true' : 'false'}">Newest</li>
                                    <li class="sort-option${param.sort == 'price_desc' ? ' active' : ''}" data-value="price_desc" role="option" aria-selected="${param.sort == 'price_desc' ? 'true' : 'false'}">Price: High-Low</li>
                                    <li class="sort-option${param.sort == 'price_asc' ? ' active' : ''}" data-value="price_asc" role="option" aria-selected="${param.sort == 'price_asc' ? 'true' : 'false'}">Price: Low-High</li>
                                </ul>
                                <input type="hidden" name="sort" id="sort-input" value="${param.sort != null ? param.sort : 'favourites'}" />
                                <c:if test="${not empty selectedCategory}">
                                    <input type="hidden" name="category" value="${selectedCategory}" />
                                </c:if>
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
                                    No products found. (Debug: products list is empty)
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="product" items="${products}" varStatus="status">
                                    <div class="product-card product-grid__card" data-testid="product-card"
                                         role="article" aria-labelledby="product-${status.index + 1}-title">
                                        <div class="product-card__body" data-testid="product-card__body">
                                            <figure>
                                                <a aria-label="${product.name}" href="${env}/product-detail?id=${product.id}"
                                                   class="product-card__img-link-overlay" data-testid="product-card-img-link-overlay"
                                                   aria-describedby="product-${status.index + 1}-desc">
                                                        <div class="wall-image-loader content-card__image" data-testid="wall-image-loader">
                                                        <c:choose>
                                                            <c:when test="${not empty product.imageUrl}">
                                                                <img src="${env}${product.imageUrl}"
                                                                     alt="${product.name}" loading="lazy"
                                                                     aria-describedby="product-${status.index + 1}-desc">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img src="${env}/images/products/default-product.avif"
                                                                     alt="${product.name}" loading="lazy"
                                                                     aria-describedby="product-${status.index + 1}-desc">
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </a>
                                                <div class="product-card__info">
                                                    <div class="product_msg_info">
                                                        <div class="product-card__messaging" role="status" aria-label="Product status">
                                                            ${product.status}
                                                        </div>
                                                        <div class="product-card__titles">
                                                            <div class="product-card__title" id="product-${status.index + 1}-title">
                                                                ${product.name}
                                                            </div>
                                                            <div class="product-card__subtitle" id="product-${status.index + 1}-desc">
                                                                ${product.type}'s Shoes
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
                                                        <div class="product-card__price" data-testid="product-card__price"
                                                             aria-label="Price: <fmt:formatNumber value='${product.price}' type='number' maxFractionDigits='0'/> VND">
                                                            <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/>₫
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
                </section>
            </div>
        </div>

        <!-- Related Stories Section -->
        <section class="related-stories-section" aria-labelledby="related-stories-title">
            <div class="container">
                <div class="related-stories-header">
                    <h2 class="section-title" id="related-stories-title">Related Stories</h2>
                    <div class="carousel-controls" role="group" aria-label="Story carousel navigation">
                        <button class="story-nav-btn prev" id="storyPrev"
                                aria-label="Previous stories" aria-controls="storySliderTrack">
                            <i class="fas fa-chevron-left" aria-hidden="true"></i>
                        </button>
                        <button class="story-nav-btn next" id="storyNext"
                                aria-label="Next stories" aria-controls="storySliderTrack">
                            <i class="fas fa-chevron-right" aria-hidden="true"></i>
                        </button>
                    </div>
                </div>

                <div class="story-slider-container" role="region" aria-labelledby="related-stories-title"
                     aria-describedby="stories-desc">
                    <span id="stories-desc" class="sr-only">Carousel of Nike-related articles and buying guides</span>
                    <div class="story-slider-track" id="storySliderTrack" role="list" aria-live="polite">
                        <div class="story-slide" data-story="winter-sneakers" role="listitem"
                             aria-labelledby="story-1-title" tabindex="0">
                            <img src="${env}/images/the-best-nike-sneakers-to-wear-in-the-winter.jpg"
                                 alt="The Best Nike Sneakers to Wear in Winter" aria-describedby="story-1-desc">
                            <div class="story-button" role="button" aria-describedby="story-1-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Buying guide</span>
                                    <span class="story-title" id="story-1-title">The Best Nike Sneakers to Wear in Winter</span>
                                </div>
                            </div>
                            <span id="story-1-desc" class="sr-only">Read our guide to the best Nike sneakers for winter weather</span>
                        </div>

                        <div class="story-slide" data-story="cycling-gifts" role="listitem"
                             aria-labelledby="story-2-title" tabindex="0">
                            <img src="${env}/images/the-11-best-nike-gifts-for-cyclists.jpg"
                                 alt="The 11 Best Nike Gifts for Cyclists" aria-describedby="story-2-desc">
                            <div class="story-button" role="button" aria-describedby="story-2-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Buying guide</span>
                                    <span class="story-title" id="story-2-title">The 11 Best Nike Gifts for Cyclists</span>
                                </div>
                            </div>
                            <span id="story-2-desc" class="sr-only">Discover the best Nike gear and gifts for cycling enthusiasts</span>
                        </div>

                        <div class="story-slide" data-story="winter-running" role="listitem"
                             aria-labelledby="story-3-title" tabindex="0">
                            <img src="${env}/images/the-best-nike-running-shoes-for-winter.jpg"
                                 alt="The Best Nike Running Shoes for Winter" aria-describedby="story-3-desc">
                            <div class="story-button" role="button" aria-describedby="story-3-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Buying guide</span>
                                    <span class="story-title" id="story-3-title">The Best Nike Running Shoes for Winter</span>
                                </div>
                            </div>
                            <span id="story-3-desc" class="sr-only">Find the perfect Nike running shoes for cold weather training</span>
                        </div>

                        <div class="story-slide" data-story="standing-shoes" role="listitem"
                             aria-labelledby="story-4-title" tabindex="0">
                            <img src="${env}/images/the-best-shoes-for-standing-all-day.jpg"
                                 alt="The Best Shoes for Standing All Day" aria-describedby="story-4-desc">
                            <div class="story-button" role="button" aria-describedby="story-4-desc">
                                <div class="story-content">
                                    <span class="story-category" aria-label="Article category">Buying guide</span>
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
                                    <span class="story-category" aria-label="Article category">Style guide</span>
                                    <span class="story-title" id="story-5-title">The Best Shoes to Wear with Jeans</span>
                                </div>
                            </div>
                            <span id="story-5-desc" class="sr-only">Style guide for pairing Nike shoes with jeans</span>
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
                                    <span class="story-category" aria-label="Article category">Buying guide</span>
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
                                    <span class="story-category" aria-label="Article category">Buying guide</span>
                                    <span class="story-title" id="story-8-title">13 Nike Tennis Gifts for Players</span>
                                </div>
                            </div>
                            <span id="story-8-desc" class="sr-only">Discover Nike tennis gifts suitable for players of all skill levels</span>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="/WEB-INF/views/customer/layout/footer.jsp" />

    <!-- Cart Sidebar -->
    <div class="cart-sidebar" id="cartSidebar" role="dialog" aria-labelledby="cart-title"
         aria-describedby="cart-description">
        <div class="cart-header">
            <h3 id="cart-title">Bag</h3>
            <button class="cart-close" onclick="toggleCart()" aria-label="Close shopping bag">
                <i class="fas fa-times" aria-hidden="true"></i>
            </button>
        </div>

        <div class="cart-content" id="cart-description" aria-live="polite">
            <div class="cart-empty" id="cartEmpty" aria-label="Empty cart state">
                <i class="fas fa-shopping-bag" aria-hidden="true"></i>
                <p>Your bag is empty</p>
                <a href="${env}/product-list" class="btn btn-primary" role="button"
                   aria-label="Start shopping to add items to your bag">Shop Now</a>
            </div>

            <div class="cart-items" id="cartItems" aria-label="Items in your shopping bag">
            </div>
        </div>

        <div class="cart-footer" id="cartFooter" style="display: none;" role="region" aria-labelledby="cart-total-title">
            <div class="cart-total">
                <h4 id="cart-total-title" class="sr-only">Order Summary</h4>
                <div class="total-row">
                    <span>Subtotal</span>
                    <span id="cartSubtotal" aria-label="Subtotal amount">$0.00</span>
                </div>
                <div class="total-row">
                    <span>Estimated Delivery & Handling</span>
                    <span aria-label="Delivery cost">Free</span>
                </div>
                <div class="total-row total-final">
                    <span>Total</span>
                    <span id="cartTotal" aria-label="Total amount">$0.00</span>
                </div>
            </div>
            <button class="btn btn-primary btn-full" aria-describedby="member-checkout-desc">
                Member Checkout
            </button>
            <span id="member-checkout-desc" class="sr-only">Checkout as a Nike member for exclusive benefits</span>
            <button class="btn btn-outline btn-full" aria-describedby="guest-checkout-desc">
                Guest Checkout
            </button>
            <span id="guest-checkout-desc" class="sr-only">Checkout as a guest without creating an account</span>
        </div>
    </div>

    <!-- Include JavaScript -->
    <jsp:include page="/WEB-INF/views/customer/layout/js.jsp" />
</body>
</html>

