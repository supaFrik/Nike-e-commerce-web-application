<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - Nike</title>
    
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/product-detail.jsp" />

</head>
<body>

    <jsp:include page="/WEB-INF/views/customer/layout/header.jsp" />
    
    <main class="product-detail" role="main" aria-labelledby="product-title">
        <div class="container">
            <div class="product-layout">
                <div class="product-images" role="region" aria-labelledby="product-images-title">
                    <h2 id="product-images-title" class="sr-only">Product Images</h2>
                    <div class="image-preview">
                        <div class="thumbnail-nav" role="list" aria-label="Product image thumbnails">
                            <c:forEach begin="1" end="10" var="i">
                                <div class="thumbnail-item${i == 1 ? ' active' : ''}" data-index="${i - 1}" role="listitem" tabindex="${i}" style="display: none;">
                                    <img src="${env}/images/products/${product.name}/${colors[0].folderPath}/${colors[0].baseImage}-${i}.avif"
                                         alt="Product image ${i}"
                                         onload="this.parentElement.style.display = 'block';"
                                         onerror="this.parentElement.style.display = 'none';">
                                </div>
                            </c:forEach>
                        </div>
                        <div class="main-image-container" role="region" aria-labelledby="main-image-title" aria-live="polite">
                            <h3 id="main-image-title" class="sr-only">Main Product Image</h3>
                            <div class="main-image">
                                <img id="currentImage" src="${env}/images/products/${product.name}/${colors[0].folderPath}/${colors[0].baseImage}-1.avif" alt="${product.name}">
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="product-info" role="region" aria-labelledby="product-title">
                    <div class="product-header">
                        <h1 class="product-title" id="product-title">${product.name}</h1>
                        <p class="product-subtitle" id="product-subtitle">${product.category != null ? product.category.name : ''}</p>
                        <div class="product-price">
                            <c:choose>
                                <c:when test="${not empty product.variants}">
                                    <fmt:formatNumber value="${product.variants[0].price}" type="currency" currencySymbol="₫"/>
                                </c:when>
                                <c:otherwise>
                                    Price not available
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Color Selection -->
                    <div class="color-selection" role="group" aria-labelledby="color-selection-title">
                        <div class="color-header">
                            <h4 id="color-selection-title">Select Color</h4>
                        </div>
                        <div id="color-options">
                            <c:forEach var="c" items="${colors}">
                                <button type="button" class="color-btn color-option" data-color-id="${c.id}" data-color-name="${fn:escapeXml(c.colorName)}" data-color-image="${c.imageUrl}" onclick="selectColor(this)">
                                    <img src="${env}${c.imageUrl}"
                                         alt="${fn:escapeXml(c.colorName)}" />
                                </button>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Size selection -->
                    <div class="size-selection" role="group" aria-labelledby="size-selection-title">
                        <div class="size-header">
                            <h4 id="size-selection-title">Select Size</h4>
                            <a href="https://www.nike.com/ph/size-fit/unisex-footwear-mens-based" class="size-guide" aria-label="Open size guide">
                                <i class="fas fa-ruler" aria-hidden="true"></i>
                                Size Guide
                            </a>
                        </div>
                        <div class="size-options" id="size-options" role="radiogroup" aria-labelledby="size-selection-title" aria-describedby="size-help">
                            <span id="size-help" class="sr-only">Select your shoe size. Unavailable sizes are marked as such.</span>

                        </div>
                    </div>
                    
                    <div class="product-actions" role="group" aria-label="Product actions">
                        <button class="btn btn-primary btn-full add-to-cart" onclick="addToCart(${product.id})" aria-describedby="add-to-cart-desc" disabled>
                            Add to Bag
                        </button>
                        <span id="add-to-cart-desc" class="sr-only">Add this product to your shopping bag</span>
                        <button class="btn btn-outline btn-full wishlist-btn" onclick="addToWishlist(${product.id})" aria-describedby="wishlist-desc">
                            <i class="far fa-heart" aria-hidden="true"></i>
                            Favourite
                        </button>
                        <span id="wishlist-desc" class="sr-only">Add this product to your wishlist</span>
                    </div>

                    <script>
                        // Product data initialization
                        var productColors = [
                            <c:forEach var="c" items="${colors}" varStatus="status">
                            {
                                id: ${c.id},
                                colorName: '${fn:escapeXml(c.colorName)}',
                                folderPath: '${fn:escapeXml(c.folderPath)}',
                                baseImage: '${fn:escapeXml(c.baseImage)}',
                                imageUrl: '${c.imageUrl}'
                            }<c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ];
                        var productName = '${fn:escapeXml(product.name)}';
                        var envPath = '${env}';
                        var productVariants = <c:out value='${variantsJson}' escapeXml='false'/>;

                        // Size Selection Management
                        var SizeSelectionManager = {
                            sizeContainer: null,
                            selectedSize: null,
                            selectedColor: null,

                            init: function() {
                                this.sizeContainer = document.getElementById('size-options');
                                if (!this.sizeContainer) {
                                    console.error('Size options container not found');
                                    return;
                                }
                                this.bindEvents();
                            },

                            bindEvents: function() {
                                // Keyboard navigation for sizes
                                this.sizeContainer.addEventListener('keydown', this.handleKeyboardNavigation.bind(this));
                            },

                            handleKeyboardNavigation: function(event) {
                                var sizeOptions = this.sizeContainer.querySelectorAll('.size-option:not([disabled])');
                                var currentIndex = Array.from(sizeOptions).findIndex(function(option) {
                                    return option === document.activeElement;
                                });

                                switch (event.key) {
                                    case 'ArrowRight':
                                    case 'ArrowDown':
                                        event.preventDefault();
                                        var nextIndex = currentIndex < sizeOptions.length - 1 ? currentIndex + 1 : 0;
                                        sizeOptions[nextIndex].focus();
                                        break;
                                    case 'ArrowLeft':
                                    case 'ArrowUp':
                                        event.preventDefault();
                                        var prevIndex = currentIndex > 0 ? currentIndex - 1 : sizeOptions.length - 1;
                                        sizeOptions[prevIndex].focus();
                                        break;
                                    case 'Enter':
                                    case ' ':
                                        event.preventDefault();
                                        if (document.activeElement.classList.contains('size-option')) {
                                            this.selectSize(document.activeElement);
                                        }
                                        break;
                                }
                            },

                            renderSizesForColor: function(colorName) {
                                if (!colorName || !this.sizeContainer) {
                                    console.error('Invalid color name or size container not found');
                                    return;
                                }

                                this.selectedColor = colorName;
                                this.setLoadingState(true);

                                try {
                                    // Clear existing sizes
                                    this.sizeContainer.innerHTML = '';

                                    // Filter variants for the selected color - using colorName property from DTO
                                    var colorVariants = productVariants.filter(function(variant) {
                                        return variant.colorName === colorName;
                                    });

                                    // Sort sizes in logical order
                                    var sortedVariants = this.sortSizesByOrder(colorVariants);

                                    // Create size elements
                                    var fragment = document.createDocumentFragment();
                                    var self = this;

                                    sortedVariants.forEach(function(variant, index) {
                                        var sizeElement = self.createSizeElement(variant, index === 0);
                                        fragment.appendChild(sizeElement);
                                    });

                                    this.sizeContainer.appendChild(fragment);

                                    // Auto-select first available size
                                    this.autoSelectFirstSize();

                                } catch (error) {
                                    console.error('Error rendering sizes:', error);
                                    this.showErrorMessage();
                                } finally {
                                    this.setLoadingState(false);
                                }
                            },

                            createSizeElement: function(variant, isFirst) {
                                var sizeButton = document.createElement('button');
                                sizeButton.type = 'button';
                                sizeButton.className = 'size-btn size-option' + (variant.stock === 0 ? ' disabled out-of-stock' : '');
                                sizeButton.textContent = variant.size;

                                // Set data attributes
                                sizeButton.setAttribute('data-size', variant.size);
                                sizeButton.setAttribute('data-price', variant.price);
                                sizeButton.setAttribute('data-stock', variant.stock);
                                sizeButton.setAttribute('data-variant-id', variant.id || '');

                                // Accessibility attributes
                                sizeButton.setAttribute('role', 'radio');
                                sizeButton.setAttribute('aria-checked', 'false');
                                sizeButton.setAttribute('tabindex', isFirst && variant.stock > 0 ? '0' : '-1');

                                if (variant.stock === 0) {
                                    sizeButton.disabled = true;
                                    sizeButton.setAttribute('aria-disabled', 'true');
                                    sizeButton.setAttribute('aria-label', 'Size ' + variant.size + ' - Out of stock');
                                    sizeButton.title = 'Out of stock';
                                } else {
                                    sizeButton.setAttribute('aria-label', 'Size ' + variant.size + ' - In stock');
                                    var self = this;
                                    sizeButton.addEventListener('click', function() {
                                        self.selectSize(this);
                                    });
                                }

                                return sizeButton;
                            },

                            sortSizesByOrder: function(variants) {
                                return variants.sort(function(a, b) {
                                    var sizeA = a.size;
                                    var sizeB = b.size;

                                    // Check if sizes are numeric
                                    var numA = parseFloat(sizeA);
                                    var numB = parseFloat(sizeB);

                                    if (!isNaN(numA) && !isNaN(numB)) {
                                        return numA - numB;
                                    }

                                    // If one is numeric and other is not, numeric comes first
                                    if (!isNaN(numA) && isNaN(numB)) return -1;
                                    if (isNaN(numA) && !isNaN(numB)) return 1;

                                    // Both are strings, sort alphabetically
                                    return sizeA.localeCompare(sizeB);
                                });
                            },

                            selectSize: function(sizeElement) {
                                if (!sizeElement || sizeElement.disabled) {
                                    return;
                                }

                                // Clear previous selection
                                this.clearSizeSelection();

                                // Mark as selected
                                sizeElement.classList.add('selected');
                                sizeElement.setAttribute('aria-checked', 'true');
                                sizeElement.setAttribute('tabindex', '0');

                                // Update other elements
                                var allSizes = this.sizeContainer.querySelectorAll('.size-option:not(.selected)');
                                allSizes.forEach(function(option) {
                                    option.setAttribute('tabindex', '-1');
                                });

                                // Store selected size data
                                this.selectedSize = {
                                    size: sizeElement.getAttribute('data-size'),
                                    price: parseFloat(sizeElement.getAttribute('data-price')),
                                    stock: parseInt(sizeElement.getAttribute('data-stock')),
                                    variantId: sizeElement.getAttribute('data-variant-id')
                                };

                                // Update price display
                                this.updatePriceDisplay(this.selectedSize.price);

                                // Enable add to cart button
                                this.updateAddToCartButton(true);

                                // Announce to screen readers
                                this.announceSelection(this.selectedSize.size);
                            },

                            clearSizeSelection: function() {
                                if (!this.sizeContainer) return;

                                var sizeOptions = this.sizeContainer.querySelectorAll('.size-option');
                                sizeOptions.forEach(function(option) {
                                    option.classList.remove('selected');
                                    option.setAttribute('aria-checked', 'false');
                                    option.setAttribute('tabindex', '-1');
                                });

                                this.selectedSize = null;
                                this.updateAddToCartButton(false);
                            },

                            updatePriceDisplay: function(price) {
                                var priceElement = document.querySelector('.product-price');
                                if (!priceElement || !price) return;

                                try {
                                    var formattedPrice = new Intl.NumberFormat('vi-VN', {
                                        style: 'currency',
                                        currency: 'VND'
                                    }).format(price);

                                    priceElement.innerHTML = formattedPrice;
                                    priceElement.setAttribute('aria-label', 'Price: ' + formattedPrice);

                                    // Add visual feedback
                                    priceElement.classList.add('price-updated');
                                    setTimeout(function() {
                                        priceElement.classList.remove('price-updated');
                                    }, 300);

                                } catch (error) {
                                    console.error('Error formatting price:', error);
                                    priceElement.innerHTML = price + '₫';
                                }
                            },

                            updateAddToCartButton: function(enabled) {
                                var addToCartButton = document.querySelector('.add-to-cart');
                                if (addToCartButton) {
                                    addToCartButton.disabled = !enabled;
                                    addToCartButton.setAttribute('aria-disabled', (!enabled).toString());
                                }
                            },

                            autoSelectFirstSize: function() {
                                var firstAvailableSize = this.sizeContainer.querySelector('.size-option:not(.disabled)');
                                if (firstAvailableSize) {
                                    this.selectSize(firstAvailableSize);
                                }
                            },

                            setLoadingState: function(isLoading) {
                                if (isLoading) {
                                    this.sizeContainer.innerHTML = '<div class="size-loading" aria-live="polite">Loading sizes...</div>';
                                    this.sizeContainer.setAttribute('aria-busy', 'true');
                                } else {
                                    this.sizeContainer.setAttribute('aria-busy', 'false');
                                }
                            },

                            showNoSizesMessage: function() {
                                this.sizeContainer.innerHTML = '<div class="no-sizes-message" role="status">No sizes available for this color</div>';
                            },

                            showErrorMessage: function() {
                                this.sizeContainer.innerHTML = '<div class="size-error-message" role="alert">Error loading sizes. Please try again.</div>';
                            },

                            announceSelection: function(size) {
                                var announcement = document.createElement('div');
                                announcement.setAttribute('aria-live', 'polite');
                                announcement.setAttribute('aria-atomic', 'true');
                                announcement.className = 'sr-only';
                                announcement.textContent = 'Size ' + size + ' selected';

                                document.body.appendChild(announcement);

                                setTimeout(function() {
                                    if (announcement.parentNode) {
                                        document.body.removeChild(announcement);
                                    }
                                }, 1000);
                            },

                            getSelectedSize: function() {
                                return this.selectedSize;
                            }
                        };

                        // Utility functions for color selection and thumbnails
                        function clearSelection(selector) {
                            document.querySelectorAll(selector).forEach(function(el) {
                                el.classList.remove('selected');
                                el.setAttribute('aria-checked', 'false');
                            });
                        }

                        function selectColor(element) {
                            clearSelection('.color-option');
                            element.classList.add('selected');
                            element.setAttribute('aria-checked', 'true');

                            var colorName = element.getAttribute('data-color-name');
                            if (!colorName) {
                                console.error('Color name not found');
                                return;
                            }

                            updateThumbnailsForColor(colorName);
                            SizeSelectionManager.renderSizesForColor(colorName);
                        }

                        function updateThumbnailsForColor(colorName) {
                            var thumbnailContainer = document.querySelector('.thumbnail-nav');
                            if (!thumbnailContainer) return;

                            thumbnailContainer.innerHTML = '';

                            var selectedColor = productColors.find(function(color) {
                                return color.colorName === colorName;
                            });

                            if (!selectedColor) {
                                console.error('Color data not found for:', colorName);
                                return;
                            }

                            var maxImages = 10;
                            for (var i = 1; i <= maxImages; i++) {
                                var thumbnailItem = document.createElement('div');
                                thumbnailItem.className = 'thumbnail-item' + (i === 1 ? ' active' : '');
                                thumbnailItem.setAttribute('data-index', (i - 1).toString());
                                thumbnailItem.setAttribute('role', 'listitem');
                                // Removed onclick handler - let thumbnail-scope.js handle this
                                thumbnailItem.style.display = 'none';

                                var img = document.createElement('img');
                                img.src = envPath + '/images/products/' + productName + '/' + selectedColor.folderPath + '/' + selectedColor.baseImage + '-' + i + '.avif';
                                img.alt = 'Product image ' + i;

                                img.onload = function() {
                                    this.parentElement.style.display = 'block';
                                };

                                img.onerror = function() {
                                    this.parentElement.style.display = 'none';
                                };

                                thumbnailItem.appendChild(img);
                                thumbnailContainer.appendChild(thumbnailItem);
                            }

                            var mainImage = document.getElementById('currentImage');
                            if (mainImage && selectedColor) {
                                mainImage.src = envPath + '/images/products/' + productName + '/' + selectedColor.folderPath + '/' + selectedColor.baseImage + '-1.avif';
                                mainImage.alt = productName + ' - ' + colorName;
                            }
                        }

                        // Removed selectThumbnail function - thumbnail-scope.js handles this now

                        // Initialize on DOM ready
                        document.addEventListener('DOMContentLoaded', function() {
                            SizeSelectionManager.init();

                            var firstColorBtn = document.querySelector('.color-option');
                            if (firstColorBtn) {
                                selectColor(firstColorBtn);
                            }
                        });
                    </script>

                    <div class="exclusion-notice" role="note" aria-label="Promotion exclusion notice">
                        <p>This product is excluded from site promotions and discounts.</p>
                    </div>
                    
                    <div class="product-description" role="region" aria-labelledby="product-description-title">
                        <h3 id="product-description-title" class="sr-only">Product Description</h3>
                        <p>${product.description}</p>
                    </div>
                </div>
                
                <div class="product-sections" role="region" aria-label="Additional product information">
                    <div class="expandable-section">
                        <button class="section-header" onclick="toggleSection('delivery')" 
                                aria-expanded="false" aria-controls="delivery" 
                                aria-describedby="delivery-header-desc">
                            <span>Free Delivery and Returns</span>
                            <i class="fas fa-chevron-down" aria-hidden="true"></i>
                        </button>
                        <span id="delivery-header-desc" class="sr-only">Click to expand delivery information</span>
                        <div class="section-content" id="delivery" aria-labelledby="delivery-title">
                            <h4 id="delivery-title" class="sr-only">Delivery Information</h4>
                            <p>Your order of S$75 or more gets free standard delivery.</p>
                            
                            <ul class="delivery-details" aria-label="Delivery options and timeframes">
                                <li>Standard delivered 1-3 Business Days</li>
                                <li>Express delivered 0-2 Business Days</li>
                            </ul>
                            
                            <p>Orders are processed and delivered Monday-Friday (excluding public holidays).</p>
                            
                            <p>Nike Members enjoy <a href="#" class="link-underline" aria-label="Learn about free returns for Nike members">free returns</a>. <a href="#" class="link-underline" aria-label="View return policy exclusions">Exclusions apply</a>.</p>
                        </div>
                    </div>
                    
                    <div class="expandable-section">
                        <button class="section-header" onclick="toggleSection('reviews')" 
                                aria-expanded="false" aria-controls="reviews"
                                aria-describedby="reviews-header-desc">
                            <span>Reviews (1)</span>
                            <div class="rating-stars-inline" role="img" aria-label="5 out of 5 stars">
                                <i class="fas fa-star" aria-hidden="true"></i>
                                <i class="fas fa-star" aria-hidden="true"></i>
                                <i class="fas fa-star" aria-hidden="true"></i>
                                <i class="fas fa-star" aria-hidden="true"></i>
                                <i class="fas fa-star" aria-hidden="true"></i>
                            </div>
                            <i class="fas fa-chevron-down" aria-hidden="true"></i>
                        </button>
                        <span id="reviews-header-desc" class="sr-only">Click to expand customer reviews</span>
                        <div class="section-content" id="reviews" aria-labelledby="reviews-title">
                            <h4 id="reviews-title" class="sr-only">Customer Reviews</h4>
                            <div class="reviews-header">
                                <div class="overall-rating">
                                    <div class="rating-stars" role="img" aria-label="Overall rating: 5 out of 5 stars">
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                    </div>
                                    <span class="rating-text">5 stars</span>
                                </div>
                                <button class="write-review-btn" aria-label="Write a review for this product">Write a review</button>
                            </div>
                            
                            <div class="review-item" role="article" aria-labelledby="review-1-title">
                                <div class="review-header">
                                    <h4 class="review-title" id="review-1-title">thank you so much nike</h4>
                                </div>
                                <div class="review-rating">
                                    <div class="rating-stars" role="img" aria-label="Customer rating: 5 out of 5 stars">
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                        <i class="fas fa-star" aria-hidden="true"></i>
                                    </div>
                                    <span class="review-author">losbañoslagunad309176653 - 05 Aug 2025</span>
                                </div>
                                <p class="review-text">nike shoes is just amazing and it has different design.i love nike shoes and other products</p>
                            </div>
                            
                            <button class="more-reviews-btn" aria-label="Load more customer reviews">More Reviews</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- How Others Are Wearing It Section -->
        <div class="social-section" role="region" aria-labelledby="social-section-title">
            <div class="container">
                <h2 id="social-section-title">How Others Are Wearing It</h2>
                <p>Upload your photo or mention @nike on Instagram for a chance to be featured.</p>
                <button class="btn btn-outline upload-btn" aria-describedby="upload-desc">
                    Upload Your Photo
                </button>
                <span id="upload-desc" class="sr-only">Share how you style this product for a chance to be featured</span>
            </div>
        </div>

        <!-- You Might Also Like Section -->
        <section class="recommendations-section" aria-labelledby="recommendations-title">
            <div class="container">
                <h2 class="section-title" id="recommendations-title">You Might Also Like</h2>
                <div class="recommendations-carousel" role="region" aria-labelledby="recommendations-title" aria-describedby="recommendations-desc">
                    <span id="recommendations-desc" class="sr-only">Carousel of recommended products similar to the current item</span>
                    <button class="carousel-btn prev" id="recommendationsPrevBtn" 
                            aria-label="Previous recommendations" aria-controls="recommendationsTrack">
                        <i class="fas fa-chevron-left" aria-hidden="true"></i>
                    </button>
                    
                    <div class="recommendations-wrapper">
                        <div class="recommendations-track" id="recommendationsTrack" role="list" aria-live="polite">
                            <div class="recommendation-card" role="listitem" aria-labelledby="rec-1-title" aria-describedby="rec-1-desc">
                                <div class="product-image-slider" role="region" aria-label="Product image gallery">
                                    <div class="product-image-track" id="productSlider0">
                                        <img src="${env}/images/products/AIR+FORCE+1+'07.avif" alt="Nike Air Force 1 '07" class="product-slide active">
                                        <img src="${env}/images/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Force 1 '07 Side" class="product-slide">
                                        <img src="${env}/images/products/AIR+MAX+90+G.avif" alt="Nike Air Force 1 '07 Detail" class="product-slide">
                                    </div>
                                    <div class="slider-dots" role="tablist" aria-label="Product image navigation">
                                        <span class="dot active" onclick="currentSlide(1, 0)" role="tab" 
                                              aria-label="View first image" tabindex="0" aria-selected="true"></span>
                                        <span class="dot" onclick="currentSlide(2, 0)" role="tab" 
                                              aria-label="View second image" tabindex="-1" aria-selected="false"></span>
                                        <span class="dot" onclick="currentSlide(3, 0)" role="tab" 
                                              aria-label="View third image" tabindex="-1" aria-selected="false"></span>
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title" id="rec-1-title">Nike Air Force 1 '07</h3>
                                    <p class="product-type" id="rec-1-desc">Men's Shoes</p>
                                    <p class="product-price" aria-label="Price: 90 dollars">$90</p>
                                </div>
                            </div>
                            <div class="recommendation-card" role="listitem" aria-labelledby="rec-2-title" aria-describedby="rec-2-desc">
                                <div class="product-image-slider" role="region" aria-label="Product image gallery">
                                    <div class="product-image-track" id="productSlider1">
                                        <img src="${env}/images/products/AIR+JORDAN+1+LOW.avif" alt="Air Jordan 1 Low" class="product-slide active">
                                        <img src="${env}/images/products/AIR+MAX+DN8.avif" alt="Air Jordan 1 Low Side" class="product-slide">
                                        <img src="${env}/images/products/BLAZER+MID+'77+VNTG.avif" alt="Air Jordan 1 Low Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title" id="rec-2-title">Air Jordan 1 Low</h3>
                                    <p class="product-type" id="rec-2-desc">Men's Shoes</p>
                                    <p class="product-price" aria-label="Price: 110 dollars">$110</p>
                                </div>
                            </div>
                            <div class="recommendation-card" role="listitem" aria-labelledby="rec-3-title" aria-describedby="rec-3-desc">
                                <div class="product-image-slider" role="region" aria-label="Product image gallery">
                                    <div class="product-image-track" id="productSlider2">
                                        <img src="${env}/images/products/AIR+MAX+90+G.avif" alt="Nike Air Max 90" class="product-slide active">
                                        <img src="${env}/images/products/AIR+MAX+DN8.avif" alt="Nike Air Max 90 Side" class="product-slide">
                                        <img src="${env}/images/products/AIR+FORCE+1+'07.avif" alt="Nike Air Max 90 Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title" id="rec-3-title">Nike Air Max 90</h3>
                                    <p class="product-type" id="rec-3-desc">Men's Shoes</p>
                                    <p class="product-price" aria-label="Price: 120 dollars">$120</p>
                                </div>
                            </div>
                            <div class="recommendation-card" role="listitem" aria-labelledby="rec-4-title" aria-describedby="rec-4-desc">
                                <div class="product-image-slider" role="region" aria-label="Product image gallery">
                                    <div class="product-image-track" id="productSlider3">
                                        <img src="${env}/images/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Blazer Mid '77 Vintage" class="product-slide active">
                                        <img src="${env}/images/products/AIR+JORDAN+1+LOW.avif" alt="Nike Blazer Mid '77 Side" class="product-slide">
                                        <img src="${env}/images/products/AIR+MAX+DN8.avif" alt="Nike Blazer Mid '77 Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title" id="rec-4-title">Nike Blazer Mid '77 Vintage</h3>
                                    <p class="product-type" id="rec-4-desc">Men's Shoes</p>
                                    <p class="product-price" aria-label="Price: 100 dollars">$100</p>
                                </div>
                            </div>
                            <div class="recommendation-card" role="listitem" aria-labelledby="rec-5-title" aria-describedby="rec-5-desc">
                                <div class="product-image-slider" role="region" aria-label="Product image gallery">
                                    <div class="product-image-track" id="productSlider4">
                                        <img src="${env}/images/products/AIR+MAX+DN8.avif" alt="Nike Air Max DN" class="product-slide active">
                                        <img src="${env}/images/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Max DN Side" class="product-slide">
                                        <img src="${env}/images/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Air Max DN Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title" id="rec-5-title">Nike Air Max DN</h3>
                                    <p class="product-type" id="rec-5-desc">Men's Shoes</p>
                                    <p class="product-price" aria-label="Price: 150 dollars">$150</p>
                                </div>
                            </div>
                            <div class="recommendation-card" role="listitem" aria-labelledby="rec-6-title" aria-describedby="rec-6-desc">
                                <div class="product-image-slider" role="region" aria-label="Product image gallery">
                                    <div class="product-image-track" id="productSlider5">
                                        <img src="${env}/images/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Force 1 '07 LV8" class="product-slide active">
                                        <img src="${env}/images/products/AIR+JORDAN+1+LOW.avif" alt="Nike Air Force 1 LV8 Side" class="product-slide">
                                        <img src="${env}/images/products/AIR+MAX+90+G.avif" alt="Nike Air Force 1 LV8 Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title" id="rec-6-title">Nike Air Force 1 '07 LV8</h3>
                                    <p class="product-type" id="rec-6-desc">Men's Shoes</p>
                                    <p class="product-price" aria-label="Price: 110 dollars">$110</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <button class="carousel-btn next" id="recommendationsNextBtn" 
                            aria-label="Next recommendations" aria-controls="recommendationsTrack">
                        <i class="fas fa-chevron-right" aria-hidden="true"></i>
                    </button>
                </div>
            </div>
        </section>
    </main>
    
    <jsp:include page="/WEB-INF/views/customer/layout/footer.jsp" />

</body>
</html>
