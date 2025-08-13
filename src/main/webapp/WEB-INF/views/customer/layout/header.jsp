<%@ include file="/WEB-INF/views/common/variables.jsp" %>

<!-- Header -->
    <header class="header" id="header">
        <div class="header-top">
            <div class="container">
                <div class="header-top-content">
                    <div class="header-top-left">
                        <a href="#" class="header-brand">
                            <img src="${classpath}/customer/img/air-jordan-logo.png" alt="Jordan" width="24" height="24">
                        </a>
                    </div>
                    <div class="header-top-right">
                        <div class="header-links">
                            <a href="#" class="header-link">Find a Store</a>
                            <span class="separator">|</span>
                            <a href="#" class="header-link">Help</a>
                            <span class="separator">|</span>
                            <a href="#" class="header-link">Join Us</a>
                            <span class="separator">|</span>
                            <a href="${classpath}/customer/auth" class="header-link">Sign In</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <nav class="navbar">
            <div class="container">
                <div class="nav-content">
                    <div class="nav-left">
                        <a href="${classpath}/" class="logo">
                            <img src="${classpath}/customer/img/e0891c394d4f7b7c09e783e29df07505.png" alt="Nike" width="60" height="22">
                        </a>
                    </div>
                    
                    <div class="nav-right">
                        <div class="search-container">
                            <div class="search-wrapper">
                                <div class="search-input-wrapper">
                                    <input type="text" placeholder="Search" class="search-input" id="searchInput">
                                    <img src="${classpath}/customer/img/icons/search-interface-symbol.png" alt="Search" class="search-icon" style="width: 20px;">
                                </div>
                            </div>
                        </div>
                        
                        <div class="nav-actions">
                            <button class="icon-btn wishlist-btn" aria-label="Favorites">
                                <img src="${classpath}/customer/img/icons/heart-3510.svg" alt="Favorites" class="nav-icon">
                            </button>
                            <button class="icon-btn cart-btn" onclick="toggleCart()" aria-label="Bag">
                                <img src="${classpath}/customer/img/icons/shopping-bag-e-commerce-cut-out-by-Vexels.png" alt="Bag" class="nav-icon">
                                <span class="cart-count" id="cartCount">0</span>
                            </button>
                        </div>
                    </div>
                    <button class="mobile-menu-btn" onclick="toggleMobileMenu()" aria-label="Menu">
                        <span></span>
                        <span></span>
                        <span></span>
                    </button>
                </div>
                    <div class="nav-center">
                        <ul class="nav-menu">
                            <li class="nav-item">
                                <a href="${classpath}/customer/product-list" class="nav-link">New & Featured</a>
                            </li>
                            <li class="nav-item">
                                <a href="${classpath}/customer/product-list" class="nav-link">Men</a>
                            </li>
                            <li class="nav-item">
                                <a href="${classpath}/customer/product-list" class="nav-link">Women</a>
                            </li>
                            <li class="nav-item">
                                <a href="${classpath}/customer/product-list" class="nav-link">Kids</a>
                            </li>
                            <li class="nav-item">
                                <a href="${classpath}/customer/product-list" class="nav-link">Sale</a>
                            </li>
                            <li class="nav-item">
                                <a href="${classpath}/customer/product-list" class="nav-link">SNKRS</a>
                            </li>
                        </ul>
                    </div>
            </div>
            
            <!-- Mobile Menu Overlay -->
            <div class="mobile-menu-overlay" id="mobileMenuOverlay">
                <div class="mobile-menu-content">
                    <ul class="mobile-nav-menu">
                        <li class="mobile-nav-item">
                            <a href="${classpath}/customer/product-list" class="mobile-nav-link">New & Featured</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="${classpath}/customer/product-list" class="mobile-nav-link">Men</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="${classpath}/customer/product-list" class="mobile-nav-link">Women</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="${classpath}/customer/product-list" class="mobile-nav-link">Kids</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="${classpath}/customer/product-list" class="mobile-nav-link">Sale</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="${classpath}/customer/product-list" class="mobile-nav-link">SNKRS</a>
                        </li>
                    </ul>
                    
                    <div class="mobile-menu-footer">
                        <div class="mobile-search-container">
                            <input type="text" placeholder="Search" class="mobile-search-input">
                            <button class="mobile-search-btn" aria-label="Search">
                                <img src="${classpath}/customer/img/icons/search-interface-symbol.png" alt="Search" style="width: 20px;">
                            </button>
                        </div>
                        <div class="mobile-menu-links">
                            <a href="#" class="mobile-menu-link">Find a Store</a>
                            <a href="#" class="mobile-menu-link">Help</a>
                            <a href="#" class="mobile-menu-link">Join Us</a>
                            <a href="${classpath}/customer/auth" class="mobile-menu-link">Sign In</a>
                        </div>
                    </div>
                </div>
            </div>
            
            </div>
        </nav>
    </header>