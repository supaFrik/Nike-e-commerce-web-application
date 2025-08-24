<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nike Air Max 90 - Nike</title>
    
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
                            <c:forEach var="img" items="${product.images}" varStatus="status">
                                <div class="thumbnail-item${status.index == 0 ? ' active' : ''}" data-index="${status.index}" role="listitem" tabindex="${status.index + 1}">
                                    <img src="${env}/customer/img/products/${product.name}/${img.url}" alt="Product image ${status.index + 1}">
                                </div>
                            </c:forEach>
                        </div>
                        <div class="main-image-container" role="region" aria-labelledby="main-image-title" aria-live="polite">
                            <h3 id="main-image-title" class="sr-only">Main Product Image</h3>
                            <div class="main-image">
                                <img id="currentImage" src="${env}/customer/img/products/${product.images[0].url}" alt="${product.name}">
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="product-info" role="region" aria-labelledby="product-title">
                    <div class="product-header">
                        <h1 class="product-title" id="product-title">${product.name}</h1>
                        <p class="product-subtitle" id="product-subtitle">${product.category != null ? product.category.name : ''}</p>
                        <div class="product-price">${product.price}đ</div>
                    </div>
                    
                    <div class="size-selection" role="group" aria-labelledby="size-selection-title">
                        <div class="size-header">
                            <h4 id="size-selection-title">Select Size</h4>
                            <a href="#" class="size-guide" aria-label="Open size guide">
                                <i class="fas fa-ruler" aria-hidden="true"></i>
                                Size Guide
                            </a>
                        </div>
                        <div class="size-options" role="radiogroup" aria-labelledby="size-selection-title" aria-describedby="size-help">
                            <span id="size-help" class="sr-only">Select your shoe size. Unavailable sizes are marked as such.</span>
                            <c:forEach var="variant" items="${product.variants}">
                                <div class="size-option${variant.stock == 0 ? ' unavailable' : ''}" data-size="${variant.size}" onclick="selectSize(this)" role="radio" aria-checked="false" aria-disabled="${variant.stock == 0}" aria-label="VN Size ${variant.size}${variant.stock == 0 ? ' - Unavailable' : ' - Available'}" tabindex="${variant.stock == 0 ? '-1' : '0'}">US ${variant.size}</div>
                            </c:forEach>
                        </div>
                    </div>
                    
                    <div class="product-actions" role="group" aria-label="Product actions">
                        <button class="btn btn-primary btn-full add-to-cart" onclick="addToCart()" aria-describedby="add-to-cart-desc">
                            Add to Bag
                        </button>
                        <span id="add-to-cart-desc" class="sr-only">Add this product to your shopping bag</span>
                        <button class="btn btn-outline btn-full wishlist-btn" onclick="addToWishlist()" aria-describedby="wishlist-desc">
                            <i class="far fa-heart" aria-hidden="true"></i>
                            Favourite
                        </button>
                        <span id="wishlist-desc" class="sr-only">Add this product to your wishlist</span>
                    </div>
                    
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
                                        <img src="${env}/customer/img/products/AIR+FORCE+1+'07.avif" alt="Nike Air Force 1 '07" class="product-slide active">
                                        <img src="${env}/customer/img/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Force 1 '07 Side" class="product-slide">
                                        <img src="${env}/customer/img/products/AIR+MAX+90+G.avif" alt="Nike Air Force 1 '07 Detail" class="product-slide">
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
                                        <img src="${env}/customer/img/products/AIR+JORDAN+1+LOW.avif" alt="Air Jordan 1 Low" class="product-slide active">
                                        <img src="${env}/customer/img/products/AIR+MAX+DN8.avif" alt="Air Jordan 1 Low Side" class="product-slide">
                                        <img src="${env}/customer/img/products/BLAZER+MID+'77+VNTG.avif" alt="Air Jordan 1 Low Detail" class="product-slide">
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
                                        <img src="${env}/customer/img/products/AIR+MAX+90+G.avif" alt="Nike Air Max 90" class="product-slide active">
                                        <img src="${env}/customer/img/products/AIR+MAX+DN8.avif" alt="Nike Air Max 90 Side" class="product-slide">
                                        <img src="${env}/customer/img/products/AIR+FORCE+1+'07.avif" alt="Nike Air Max 90 Detail" class="product-slide">
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
                                        <img src="${env}/customer/img/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Blazer Mid '77 Vintage" class="product-slide active">
                                        <img src="${env}/customer/img/products/AIR+JORDAN+1+LOW.avif" alt="Nike Blazer Mid '77 Side" class="product-slide">
                                        <img src="${env}/customer/img/products/AIR+MAX+DN8.avif" alt="Nike Blazer Mid '77 Detail" class="product-slide">
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
                                        <img src="${env}/customer/img/products/AIR+MAX+DN8.avif" alt="Nike Air Max DN" class="product-slide active">
                                        <img src="${env}/customer/img/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Max DN Side" class="product-slide">
                                        <img src="${env}/customer/img/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Air Max DN Detail" class="product-slide">
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
                                        <img src="${env}/customer/img/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Force 1 '07 LV8" class="product-slide active">
                                        <img src="${env}/customer/img/products/AIR+JORDAN+1+LOW.avif" alt="Nike Air Force 1 LV8 Side" class="product-slide">
                                        <img src="${env}/customer/img/products/AIR+MAX+90+G.avif" alt="Nike Air Force 1 LV8 Detail" class="product-slide">
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

    <!-- JavaScript Files -->
    <jsp:include page="/WEB-INF/views/customer/layout/js.jsp" />

</body>
</html>
