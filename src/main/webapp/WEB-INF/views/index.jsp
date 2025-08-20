<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- import common variables -->
<jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> Nike - Just Do It</title>
    <link rel="icon" href="${env}/customer/img/e0891c394d4f7b7c09e783e29df07505.png">

    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/landing-page.jsp" />

</head>

<body>
    <jsp:include page="/WEB-INF/views/customer/layout/header.jsp" />

<!-- Hero Section -->
<section class="hero" aria-label="Hero banner section">
    <div class="video-container">
        <video autoplay loop muted playsinline class="video-placeholder"
               poster="${env}/customer/video/stairs-nike-poster.jpg"
               aria-label="Nike promotional video about Mars Yard sneakers"
               aria-describedby="hero-description">
            <source src="${env}/customer/vid/Stairs%20Nike%20-%20Nike%20(1080p,%20h264).mp4" type="video/mp4">
            Your browser does not support the video tag.
        </video>
    </div>
</section>

    <div class="hero-banner-container" role="banner" aria-labelledby="hero-main-title" aria-describedby="hero-description">
        <div class="hero-title">
            <h1 id="hero-main-title" aria-label="Main hero title">
                too easy
            </h1>
        </div>
        <div class="hero-logo">
            <img src="${env}/customer/img/air-jordan-logo.png" alt="Air Jordan Logo" aria-describedby="hero-description">
        </div>
        <div class="hero-subtiles">
            <span id="hero-description" aria-label="Hero subtitle">
                Tiny greats make greatness look easy.
            </span>
        </div>
        <div class="hero-button">
            <a href="${env}/customer/product-list" aria-label="Shop Nike products" role="button">
                <span>
                    Shop
                </span>
            </a>
        </div>
    </div>
    <!-- Featured -->
    <section class="featured" aria-labelledby="featured-title">
        <div class="featured-container">
            <div class="container">
                <h2 class="section-title" id="featured-title">Featured</h2>
            </div>
            <div class="featured-grid" role="grid" aria-labelledby="featured-title">
                <div class="featured-item" role="gridcell" aria-labelledby="featured-1-title" aria-describedby="featured-1-desc">
                    <img src="${env}/customer/img/feature section_landing page/nike-just-do-it.webp" alt="Nike Air Superfly promotional image" aria-describedby="featured-1-desc">
                    <div class="featured-content">
                        <div class="featured-category" id="featured-1-desc">Spikes Off, Style On</div>
                        <h3 class="featured-title" id="featured-1-title">Nike Air Superfly</h3>
                        <a href="#" class="btn btn-outline" aria-label="Get notified about Nike Air Superfly" role="button">Get Notified</a>
                    </div>
                </div>

                <div class="featured-item" role="gridcell" aria-labelledby="featured-2-title" aria-describedby="featured-2-desc">
                    <img src="${env}/customer/img/feature section_landing page/nike-just-do-it2.webp" alt="Nike Football promotional image" aria-describedby="featured-2-desc">
                    <div class="featured-content">
                        <div class="featured-category" id="featured-2-desc">Nike Football</div>
                        <h3 class="featured-title" id="featured-2-title">Introducing Scary Good Pack</h3>
                        <a href="${env}/products?category=football" class="btn btn-outline" aria-label="Shop Nike Football products" role="button">Shop</a>
                    </div>
                </div>

                <div class="featured-item" role="gridcell" aria-labelledby="featured-3-title" aria-describedby="featured-3-desc">
                    <img src="${env}/customer/img/feature section_landing page/nike-just-do-it.3webp" alt="Tennis promotional image" aria-describedby="featured-3-desc">
                    <div class="featured-content">
                        <div class="featured-category" id="featured-3-desc">Tennis</div>
                        <h3 class="featured-title" id="featured-3-title">Serve Up Style</h3>
                        <a href="${env}/products?category=tennis" class="btn btn-outline" aria-label="Shop Tennis products" role="button">Shop</a>
                    </div>
                </div>

                <div class="featured-item" role="gridcell" aria-labelledby="featured-4-title" aria-describedby="featured-4-desc">
                    <img src="${env}/customer/img/feature section_landing page/nike-just-do-it4.webp" alt="Jordan Streetwear promotional image" aria-describedby="featured-4-desc">
                    <div class="featured-content">
                        <div class="featured-category" id="featured-4-desc">Jordan Streetwear</div>
                        <h3 class="featured-title" id="featured-4-title">Real Ones Know</h3>
                        <a href="${env}/products?category=jordan" class="btn btn-outline" aria-label="Shop Jordan Apparel" role="button">Shop Apparel</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Shop Icons Section -->
    <section class="shop-icons-section" aria-labelledby="shop-icons-title">
        <div class="container">
            <h2 class="section-title" id="shop-icons-title">Shop by Icons</h2>
            <div class="shop-icons-carousel" role="region" aria-labelledby="shop-icons-title" aria-describedby="shop-icons-desc">
                <span id="shop-icons-desc" class="sr-only">Carousel of iconic Nike shoe styles</span>
                <button class="carousel-btn prev" id="shopIconsPrevBtn" aria-label="Previous icons" aria-controls="shopIconsTrack">
                    <i class="fas fa-chevron-left" aria-hidden="true"></i>
                </button>

                <div class="shop-icons-wrapper">
                    <div class="shop-icons-track" id="shopIconsTrack" role="list" aria-live="polite">
                        <div class="shop-icon-card" data-id="airforce1" role="listitem" aria-label="Blazer shoe icon">
                            <img src="${env}/customer/img/icons/blazer.webp" alt="Air Force 1" aria-describedby="blazer-desc">
                            <span id="blazer-desc" class="sr-only">Click to shop Blazer sneakers</span>
                        </div>
                        <div class="shop-icon-card" data-id="airmax" role="listitem" aria-label="Air Max shoe icon">
                            <img src="${env}/customer/img/icons/airmaxDN.webp" alt="Air Max" aria-describedby="airmax-desc">
                            <span id="airmax-desc" class="sr-only">Click to shop Air Max sneakers</span>
                        </div>
                        <div class="shop-icon-card" data-id="dunk" role="listitem" aria-label="Cortez shoe icon">
                            <img src="${env}/customer/img/icons/cortez.webp" alt="Cortez" aria-describedby="cortez-desc">
                            <span id="cortez-desc" class="sr-only">Click to shop Cortez sneakers</span>
                        </div>
                        <div class="shop-icon-card" data-id="cortez" role="listitem" aria-label="Dunk shoe icon">
                            <img src="${env}/customer/img/icons/dunk.webp" alt="Dunk" aria-describedby="dunk-desc">
                            <span id="dunk-desc" class="sr-only">Click to shop Dunk sneakers</span>
                        </div>
                        <div class="shop-icon-card" data-id="blazer" role="listitem" aria-label="Jordan 1 shoe icon">
                            <img src="${env}/customer/img/icons/jordan1.webp" alt="Blazer" aria-describedby="jordan1-desc">
                            <span id="jordan1-desc" class="sr-only">Click to shop Jordan 1 sneakers</span>
                        </div>
                        <div class="shop-icon-card" data-id="pegasus" role="listitem" aria-label="Metcon shoe icon">
                            <img src="${env}/customer/img/icons/metcon.webp" alt="Pegasus" aria-describedby="metcon-desc">
                            <span id="metcon-desc" class="sr-only">Click to shop Metcon training shoes</span>
                        </div>
                        <div class="shop-icon-card" data-id="jordan1" role="listitem" aria-label="V2K shoe icon">
                            <img src="${env}/customer/img/icons/v2k.webp" alt="Air Jordan 1" aria-describedby="v2k-desc">
                            <span id="v2k-desc" class="sr-only">Click to shop V2K sneakers</span>
                        </div>
                        <div class="shop-icon-card" data-id="jordan4" role="listitem" aria-label="Air Max DN shoe icon">
                            <img src="${env}/customer/img/icons/airmaxDN.webp" alt="Air Jordan 4" aria-describedby="airmaxdn-desc">
                            <span id="airmaxdn-desc" class="sr-only">Click to shop Air Max DN sneakers</span>
                        </div>
                    </div>
                </div>

                <button class="carousel-btn next" id="shopIconsNextBtn" aria-label="Next icons" aria-controls="shopIconsTrack">
                    <i class="fas fa-chevron-right" aria-hidden="true"></i>
                </button>
            </div>
        </div>
    </section>

    <!-- Shop Golf Section -->
    <section class="shop-running-section" aria-labelledby="shop-golf-title">
        <div class="container">
            <h2 class="section-title" id="shop-golf-title">Shop Golf</h2>
            <div class="shop-running-carousel" role="region" aria-labelledby="shop-golf-title" aria-describedby="shop-golf-desc">
                <span id="shop-golf-desc" class="sr-only">Carousel of Nike golf shoes and equipment</span>
                <button class="carousel-btn prev" id="shopRunningPrevBtn" aria-label="Previous running items" aria-controls="shopRunningTrack">
                    <i class="fas fa-chevron-left" aria-hidden="true"></i>
                </button>

                <div class="shop-running-wrapper">
                    <div class="shop-running-track" id="shopRunningTrack" role="list" aria-live="polite">
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-1-title" aria-describedby="golf-1-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Victory Pro 4" aria-describedby="golf-1-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-1-title">Nike Victory Pro 4</h3>
                                <p class="product-type" id="golf-1-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 4,409,000 Vietnamese Dong">4,409,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-2-title" aria-describedby="golf-2-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+FORCE+1+'07.avif" alt="Nike Victory Tour 4" aria-describedby="golf-2-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-2-title">Nike Victory Tour 4</h3>
                                <p class="product-type" id="golf-2-desc">Golf Shoes (Wide)</p>
                                <p class="product-price" aria-label="Price: 5,589,000 Vietnamese Dong">5,589,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-3-title" aria-describedby="golf-3-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+JORDAN+1+LOW.avif" alt="Nike Air Max 90 G" aria-describedby="golf-3-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-3-title">Nike Air Max 90 G</h3>
                                <p class="product-type" id="golf-3-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 4,409,000 Vietnamese Dong">4,409,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-4-title" aria-describedby="golf-4-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+MAX+90+G.avif" alt="Nike Victory G Lite" aria-describedby="golf-4-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-4-title">Nike Victory G Lite</h3>
                                <p class="product-type" id="golf-4-desc">Men's Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 1,919,000 Vietnamese Dong">1,919,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-5-title" aria-describedby="golf-5-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+MAX+DN8 (1).avif" alt="Nike Air Max 270 G" aria-describedby="golf-5-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-5-title">Nike Air Max 270 G</h3>
                                <p class="product-type" id="golf-5-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 3,829,000 Vietnamese Dong">3,829,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-6-title" aria-describedby="golf-6-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+MAX+DN8 (2).avif" alt="Nike Air Jordan 1 Low G" aria-describedby="golf-6-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-6-title">Nike Air Jordan 1 Low G</h3>
                                <p class="product-type" id="golf-6-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 4,409,000 Vietnamese Dong">4,409,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-7-title" aria-describedby="golf-7-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+MAX+DN8 (3).avif" alt="Nike Infinity Tour" aria-describedby="golf-7-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-7-title">Nike Infinity Tour</h3>
                                <p class="product-type" id="golf-7-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 5,589,000 Vietnamese Dong">5,589,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-8-title" aria-describedby="golf-8-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+MAX+DN8+AMD.avif" alt="Nike React Infinity Pro" aria-describedby="golf-8-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-8-title">Nike React Infinity Pro</h3>
                                <p class="product-type" id="golf-8-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 4,969,000 Vietnamese Dong">4,969,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-9-title" aria-describedby="golf-9-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/AIR+MAX+DN8.avif" alt="Nike Air Zoom Victory" aria-describedby="golf-9-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-9-title">Nike Air Zoom Victory</h3>
                                <p class="product-type" id="golf-9-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 3,269,000 Vietnamese Dong">3,269,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-10-title" aria-describedby="golf-10-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Blazer Mid '77 Golf" aria-describedby="golf-10-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-10-title">Nike Blazer Mid '77 Golf</h3>
                                <p class="product-type" id="golf-10-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 2,929,000 Vietnamese Dong">2,929,000₫</p>
                            </div>
                        </div>
                        <div class="running-card" data-category="golf-shoes" role="listitem" aria-labelledby="golf-11-title" aria-describedby="golf-11-desc">
                            <div class="product-image">
                                <img src="${env}/customer/img/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Cortez Golf" aria-describedby="golf-11-desc">
                            </div>
                            <div class="product-info">
                                <h3 class="product-title" id="golf-11-title">Nike Cortez Golf</h3>
                                <p class="product-type" id="golf-11-desc">Golf Shoes</p>
                                <p class="product-price" aria-label="Price: 2,479,000 Vietnamese Dong">2,479,000₫</p>
                            </div>
                        </div>
                    </div>
                </div>

                <button class="carousel-btn next" id="shopRunningNextBtn" aria-label="Next running items" aria-controls="shopRunningTrack">
                    <i class="fas fa-chevron-right" aria-hidden="true"></i>
                </button>
            </div>
        </div>
    </section>

    <!-- Shop By Sport -->
    <section class="shop-by-sport-section" aria-labelledby="sport-section-title">
        <div class="container">
            <div class="shop-by-sport-header">
                <h2 class="section-title" id="sport-section-title">Shop By Sport</h2>
                <div class="carousel-controls" role="group" aria-label="Sport carousel navigation">
                    <button class="sport-nav-btn prev" id="sportPrev" aria-label="Previous sports" aria-controls="sportSliderTrack">
                        <i class="fas fa-chevron-left" aria-hidden="true"></i>
                    </button>
                    <button class="sport-nav-btn next" id="sportNext" aria-label="Next sports" aria-controls="sportSliderTrack">
                        <i class="fas fa-chevron-right" aria-hidden="true"></i>
                    </button>
                </div>
            </div>

            <div class="sport-slider-container" role="region" aria-labelledby="sport-section-title" aria-describedby="sport-desc">
                <span id="sport-desc" class="sr-only">Browse Nike products by different sports categories</span>
                <div class="sport-slider-track" id="sportSliderTrack" role="list" aria-live="polite">
                    <div class="sport-slide" data-sport="running" role="listitem" aria-label="Running category">
                        <img src="${env}/customer/img/sports/running.webp" alt="Running" aria-describedby="running-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="running-desc">
                            <span>Running</span>
                        </div>
                        <span id="running-desc" class="sr-only">Shop Nike running shoes and apparel</span>
                    </div>
                    <div class="sport-slide" data-sport="football" role="listitem" aria-label="Football category">
                        <img src="${env}/customer/img/sports/football.webp" alt="Football" aria-describedby="football-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="football-desc">
                            <span>Football</span>
                        </div>
                        <span id="football-desc" class="sr-only">Shop Nike football cleats and gear</span>
                    </div>
                    <div class="sport-slide" data-sport="basketball" role="listitem" aria-label="Basketball category">
                        <img src="${env}/customer/img/sports/dunk.webp" alt="Basketball" aria-describedby="basketball-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="basketball-desc">
                            <span>Basketball</span>
                        </div>
                        <span id="basketball-desc" class="sr-only">Shop Nike basketball shoes and apparel</span>
                    </div>
                    <div class="sport-slide" data-sport="gym" role="listitem" aria-label="Gym & Training category">
                        <img src="${env}/customer/img/sports/gymnastics.webp" alt="Gym & Training" aria-describedby="gym-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="gym-desc">
                            <span>Gym & Training</span>
                        </div>
                        <span id="gym-desc" class="sr-only">Shop Nike gym and training equipment</span>
                    </div>
                    <div class="sport-slide" data-sport="yoga" role="listitem" aria-label="Yoga category">
                        <img src="${env}/customer/img/sports/yoga.webp" alt="Yoga" aria-describedby="yoga-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="yoga-desc">
                            <span>Yoga</span>
                        </div>
                        <span id="yoga-desc" class="sr-only">Shop Nike yoga apparel and accessories</span>
                    </div>
                    <div class="sport-slide" data-sport="skateboarding" role="listitem" aria-label="Skateboarding category">
                        <img src="${env}/customer/img/sports/skateboarding.webp" alt="Skateboarding" aria-describedby="skateboarding-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="skateboarding-desc">
                            <span>Skateboarding</span>
                        </div>
                        <span id="skateboarding-desc" class="sr-only">Shop Nike skateboarding shoes and apparel</span>
                    </div>
                    <div class="sport-slide" data-sport="dance" role="listitem" aria-label="Dance category">
                        <img src="${env}/customer/img/sports/dance.webp" alt="Dance" aria-describedby="dance-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="dance-desc">
                            <span>Dance</span>
                        </div>
                        <span id="dance-desc" class="sr-only">Shop Nike dance shoes and apparel</span>
                    </div>
                    <div class="sport-slide" data-sport="badminton" role="listitem" aria-label="Badminton category">
                        <img src="${env}/customer/img/sports/badminton.webp" alt="Badminton" aria-describedby="badminton-desc">
                        <div class="sport-button" role="button" tabindex="0" aria-describedby="badminton-desc">
                            <span>Badminton</span>
                        </div>
                        <span id="badminton-desc" class="sr-only">Shop Nike badminton shoes and equipment</span>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="/WEB-INF/views/customer/layout/footer.jsp" />

    <!-- Cart Sidebar -->
    <div class="cart-sidebar" id="cartSidebar" role="dialog" aria-labelledby="cart-title" aria-describedby="cart-content-desc">
        <div class="cart-header">
            <h3 id="cart-title">Bag</h3>
            <button class="cart-close" onclick="toggleCart()" aria-label="Close cart">
                <i class="fas fa-times" aria-hidden="true"></i>
            </button>
        </div>

        <div class="cart-content" id="cart-content-desc" aria-live="polite">
            <div class="cart-empty" id="cartEmpty" aria-label="Empty cart state">
                <i class="fas fa-shopping-bag" aria-hidden="true"></i>
                <p>Your bag is empty</p>
                <a href="${env}/products" class="btn btn-primary" role="button" aria-label="Shop now to add items to your bag">Shop Now</a>
            </div>

            <div class="cart-items" id="cartItems" aria-label="Cart items list">
                <!-- Cart items will be populated by JavaScript -->
            </div>
        </div>
    </div>

    <!--  JS-->
    <jsp:include page="/WEB-INF/views/customer/layout/js.jsp" />
</body>
</html>
