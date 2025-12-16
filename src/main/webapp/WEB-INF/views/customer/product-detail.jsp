<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                    <h2 id="product-images-title" class="sr-only">Ảnh sản phẩm</h2>
                    <div class="image-preview">
                        <c:choose>
                            <c:when test="${not empty colors}">
                                <div class="thumbnail-nav" role="list" aria-label="Ảnh thu nhỏ sản phẩm">
                                    <c:forEach begin="1" end="10" var="i">
                                        <div class="thumbnail-item${i == 1 ? ' active' : ''}" data-index="${i - 1}" role="listitem" tabindex="${i}" style="display: none;">
                                            <img src="${env}/images/products/${product.name}/${colors[0].folderPath}/${colors[0].baseImage}-${i}.avif"
                                                 alt="Ảnh sản phẩm ${i}"
                                                 onload="this.parentElement.style.display = 'block';"
                                                 onerror="this.parentElement.style.display = 'none';">
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="main-image-container" role="region" aria-labelledby="main-image-title" aria-live="polite">
                                    <h3 id="main-image-title" class="sr-only">Ảnh chính sản phẩm</h3>
                                    <div class="main-image">
                                        <img id="currentImage" src="${env}/images/products/${product.name}/${colors[0].folderPath}/${colors[0].baseImage}-1.avif" alt="${product.name}">
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="main-image-container" role="region" aria-labelledby="main-image-title" aria-live="polite">
                                    <h3 id="main-image-title" class="sr-only">Ảnh chính sản phẩm</h3>
                                    <div class="main-image">
                                        <img id="currentImage" src="${env}/images/products/default-product.avif" alt="${product.name}">
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="product-info" role="region" aria-labelledby="product-title">
                    <div class="product-header">
                        <h1 class="product-title" id="product-title">${product.name}</h1>
                        <p class="product-subtitle" id="product-subtitle">${product.categoryName}</p>
                        <div class="product-price">
                            <c:choose>
                                <c:when test="${not empty product.variants}">
                                    <fmt:formatNumber value="${product.variants[0].price}" type="currency" currencySymbol="₫"/>
                                </c:when>
                                <c:otherwise>
                                    Giá chưa có sẵn
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Color Selection -->
                    <div class="color-selection" role="group" aria-labelledby="color-selection-title">
                        <div class="color-header">
                            <h4 id="color-selection-title">Chọn màu</h4>
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
                            <h4 id="size-selection-title">Chọn kích thước</h4>
                            <a href="https://www.nike.com/ph/size-fit/unisex-footwear-mens-based" class="size-guide" aria-label="Open size guide">
                                <i class="fas fa-ruler" aria-hidden="true"></i>
                                Hướng dẫn kích thước
                            </a>
                        </div>
                        <div class="size-options" id="size-options" role="radiogroup" aria-labelledby="size-selection-title" aria-describedby="size-help">
                            <span id="size-help" class="sr-only">Chọn kích thước giày của bạn. Kích thước không có sẵn sẽ được đánh dấu.</span>

                        </div>
                    </div>
                    
                    <div class="product-actions" role="group" aria-label="Product actions">
                        <button class="btn btn-primary btn-full add-to-cart" onclick="addToCart(${product.id})" aria-describedby="add-to-cart-desc" disabled>
                            Thêm vào giỏ
                        </button>
                        <span id="add-to-cart-desc" class="sr-only">Thêm sản phẩm này vào giỏ hàng của bạn</span>
                        <button class="btn btn-outline btn-full wishlist-btn" onclick="addToWishlist(${product.id})" aria-describedby="wishlist-desc">
                            <i class="far fa-heart" aria-hidden="true"></i>
                            Yêu thích
                        </button>
                        <span id="wishlist-desc" class="sr-only">Thêm sản phẩm này vào danh sách yêu thích của bạn</span>
                    </div>

                    <script>
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
                                    sizeButton.setAttribute('aria-label', 'Kích thước ' + variant.size + ' - Hết hàng');
                                    sizeButton.title = 'Hết hàng';
                                } else {
                                    sizeButton.setAttribute('aria-label', 'Kích thước ' + variant.size + ' - Còn hàng');
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

                                this.clearSizeSelection();

                                sizeElement.classList.add('selected');
                                sizeElement.setAttribute('aria-checked', 'true');
                                sizeElement.setAttribute('tabindex', '0');

                                var allSizes = this.sizeContainer.querySelectorAll('.size-option:not(.selected)');
                                allSizes.forEach(function(option) {
                                    option.setAttribute('tabindex', '-1');
                                });

                                this.selectedSize = {
                                    size: sizeElement.getAttribute('data-size'),
                                    price: parseFloat(sizeElement.getAttribute('data-price')),
                                    stock: parseInt(sizeElement.getAttribute('data-stock')),
                                    variantId: sizeElement.getAttribute('data-variant-id')
                                };

                                this.updatePriceDisplay(this.selectedSize.price);

                                this.updateAddToCartButton(true);

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
                                    priceElement.setAttribute('aria-label', 'Giá: ' + formattedPrice);

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
                                    this.sizeContainer.innerHTML = '<div class="size-loading" aria-live="polite">Đang tải kích thước...</div>';
                                    this.sizeContainer.setAttribute('aria-busy', 'true');
                                } else {
                                    this.sizeContainer.setAttribute('aria-busy', 'false');
                                }
                            },

                            showNoSizesMessage: function() {
                                this.sizeContainer.innerHTML = '<div class="no-sizes-message" role="status">Không có kích thước cho màu này</div>';
                            },

                            showErrorMessage: function() {
                                this.sizeContainer.innerHTML = '<div class="size-error-message" role="alert">Lỗi khi tải kích thước. Vui lòng thử lại.</div>';
                            },

                            announceSelection: function(size) {
                                var announcement = document.createElement('div');
                                announcement.setAttribute('aria-live', 'polite');
                                announcement.setAttribute('aria-atomic', 'true');
                                announcement.className = 'sr-only';
                                announcement.textContent = 'Kích thước ' + size + ' đã được chọn';

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

                        // Initialize after DOM ready
                        document.addEventListener('DOMContentLoaded', function() {
                            SizeSelectionManager.init();
                        });
                    </script>

                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/customer/layout/footer.jsp" />

    <script>
        function selectColor(btn) {
            var colorName = btn.getAttribute('data-color-name');
            SizeSelectionManager.renderSizesForColor(colorName);
            // update main image
            var imgUrl = btn.getAttribute('data-color-image');
            if(imgUrl) document.getElementById('currentImage').src = envPath + imgUrl;
        }

        function addToCart(productId) {
            var selected = SizeSelectionManager.getSelectedSize();
            if(!selected) {
                alert('Vui lòng chọn kích thước');
                return;
            }
            // ... existing AJAX add to cart logic ...
        }

        function addToWishlist(productId) {
            // ... existing AJAX wishlist logic ...
        }
    </script>

</body>
</html>
