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
                            <div class="thumbnail-item active" data-index="0" role="listitem" 
                                 aria-label="Main view thumbnail" tabindex="0" 
                                 aria-describedby="main-view-desc">
                                <img src="${env}/customer/img/product-detail/NIKE GATO MAIN.avif" alt="Nike shoe main view" aria-describedby="main-view-desc">
                                <span id="main-view-desc" class="sr-only">Click to view main product image</span>
                            </div>
                            <div class="thumbnail-item" data-index="1" role="listitem" 
                                 aria-label="Side view thumbnail" tabindex="0"
                                 aria-describedby="side-view-1-desc">
                                <img src="${env}/customer/img/product-detail/NIKE GATO SIDE !.avif" alt="Nike shoe side view" aria-describedby="side-view-1-desc">
                                <span id="side-view-1-desc" class="sr-only">Click to view side product image</span>
                            </div>
                            <div class="thumbnail-item" data-index="2" role="listitem" 
                                 aria-label="Side view 2 thumbnail" tabindex="0"
                                 aria-describedby="side-view-2-desc">
                                <img src="${env}/customer/img/product-detail/NIKE GATO SIDE 2.avif" alt="Nike shoe side view 2" aria-describedby="side-view-2-desc">
                                <span id="side-view-2-desc" class="sr-only">Click to view alternative side product image</span>
                            </div>
                            <div class="thumbnail-item" data-index="3" role="listitem" 
                                 aria-label="Top view thumbnail" tabindex="0"
                                 aria-describedby="top-view-desc">
                                <img src="${env}/customer/img/product-detail/NIKE GATO UP.avif" alt="Nike shoe top view" aria-describedby="top-view-desc">
                                <span id="top-view-desc" class="sr-only">Click to view top product image</span>
                            </div>
                            <div class="thumbnail-item" data-index="4" role="listitem" 
                                 aria-label="Bottom view thumbnail" tabindex="0"
                                 aria-describedby="bottom-view-desc">
                                <img src="${env}/customer/img/product-detail/NIKE GATO DOWN.avif" alt="Nike shoe bottom view" aria-describedby="bottom-view-desc">
                                <span id="bottom-view-desc" class="sr-only">Click to view bottom product image</span>
                            </div>
                            <div class="thumbnail-item" data-index="5" role="listitem" 
                                 aria-label="Side view 3 thumbnail" tabindex="0"
                                 aria-describedby="side-view-3-desc">
                                <img src="${env}/customer/img/product-detail/NIKE GATO SIDE 3.avif" alt="Nike shoe side view 3" aria-describedby="side-view-3-desc">
                                <span id="side-view-3-desc" class="sr-only">Click to view third side product image</span>
                            </div>
                            <div class="thumbnail-item" data-index="6" role="listitem" 
                                 aria-label="Detail view thumbnail" tabindex="0"
                                 aria-describedby="detail-view-desc">
                                <img src="${env}/customer/img/product-detail/NIKE+GATO+LV8.avif" alt="Nike shoe detail view" aria-describedby="detail-view-desc">
                                <span id="detail-view-desc" class="sr-only">Click to view detailed product image</span>
                            </div>
                        </div>
                        <div class="main-image-container" role="region" aria-labelledby="main-image-title" aria-live="polite">
                            <h3 id="main-image-title" class="sr-only">Main Product Image</h3>
                            <div class="main-image">
                                <img id="currentImage" src="${env}/customer/img/product-detail/NIKE GATO MAIN.avif" 
                                     alt="Nike Air Max 90" aria-describedby="current-image-desc">
                                <span id="current-image-desc" class="sr-only">Currently viewing main product image</span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="product-info" role="region" aria-labelledby="product-title">
                    <div class="product-header">
                        <h1 class="product-title" id="product-title">JA 3 'Light Show' EP</h1>
                        <p class="product-subtitle" id="product-subtitle">Basketball Shoes</p>
                        <div class="product-price" aria-label="Price: 3,449,000 Vietnamese Dong">3,449,000đ</div>
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
                            <div class="size-option unavailable" data-size="6" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 6 - Unavailable" tabindex="-1">US 6</div>
                            <div class="size-option unavailable" data-size="6.5" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 6.5 - Unavailable" tabindex="-1">US 6.5</div>
                            <div class="size-option unavailable" data-size="7" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 7 - Unavailable" tabindex="-1">US 7</div>
                            <div class="size-option unavailable" data-size="7.5" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 7.5 - Unavailable" tabindex="-1">US 7.5</div>
                            <div class="size-option" data-size="8" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="false" 
                                 aria-label="US Men's Size 8 / Women's Size 9.5 - Available" tabindex="0">US M 8 / W 9.5</div>
                            <div class="size-option unavailable" data-size="8.5" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 8.5 - Unavailable" tabindex="-1">US 8.5</div>
                            <div class="size-option unavailable" data-size="9" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 9 - Unavailable" tabindex="-1">US 9</div>
                            <div class="size-option unavailable" data-size="9.5" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 9.5 - Unavailable" tabindex="-1">US 9.5</div>
                            <div class="size-option unavailable" data-size="10" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 10 - Unavailable" tabindex="-1">US 10</div>
                            <div class="size-option unavailable" data-size="10.5" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 10.5 - Unavailable" tabindex="-1">US 10.5</div>
                            <div class="size-option unavailable" data-size="11" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 11 - Unavailable" tabindex="-1">US 11</div>
                            <div class="size-option unavailable" data-size="11.5" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 11.5 - Unavailable" tabindex="-1">US 11.5</div>
                            <div class="size-option unavailable" data-size="12" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 12 - Unavailable" tabindex="-1">US 12</div>
                            <div class="size-option unavailable" data-size="12.5" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 12.5 - Unavailable" tabindex="-1">US 12.5</div>
                            <div class="size-option unavailable" data-size="13" onclick="selectSize(this)" 
                                 role="radio" aria-checked="false" aria-disabled="true" 
                                 aria-label="US Size 13 - Unavailable" tabindex="-1">US 13</div>
                        </div>
                    </div>
                    
                    <div class="product-actions" role="group" aria-label="Product actions">
                        <button class="btn btn-primary btn-full add-to-cart" onclick="addToCart()" 
                                aria-describedby="add-to-cart-desc">
                            Add to Bag
                        </button>
                        <span id="add-to-cart-desc" class="sr-only">Add this product to your shopping bag</span>
                        <button class="btn btn-outline btn-full wishlist-btn" onclick="addToWishlist()" 
                                aria-describedby="wishlist-desc">
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
                        <p>Every time Ja steps on the court, it's showtime. To help him elevate his game, we worked with him to make the theoretical JA signature shoe so far. Light, low-to-the-ground and super responsive, it's built around his rise time and his distance, for the full-length Zoom X foam. "Zoom is so responsive," he says, "I just feel it that in!" We call it bad news for his defenders. With its ultra-durable rubber outsole, this version gives you traction for outdoor courts.</p>
                        
                        <ul class="product-features" aria-label="Product features and details">
                            <li><strong>Colour Shown:</strong> White/Dusty Cactus/Hyper Pink/Star Blue</li>
                            <li><strong>Style:</strong> HF2714-100</li>
                            <li><strong>Country/Region of Origin:</strong> China</li>
                        </ul>
                        
                        <div class="product-details-link">
                            <a href="#" class="details-link" aria-label="View complete product details">View Product Details</a>
                        </div>
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
    <script src="${env}/customer/slick/jquery-3.7.1.js"></script>
    <script src="${env}/customer/slick/slick.min.js"></script>
    <script src="${env}/customer/scripts/product-detail.js"></script>

</body>
</html>
