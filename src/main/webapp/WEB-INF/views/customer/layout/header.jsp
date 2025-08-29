<%@ include file="/WEB-INF/views/common/variables.jsp" %>

<!-- Header -->
<header class="header" id="header">
    <div class="header-top">
        <div class="container">
            <div class="header-top-content">
                <div class="header-top-left">
                    <a href="${env}/" class="header-brand">
                        <img src="${env}/images/air-jordan-logo.png" alt="Jordan" width="24" height="24">
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
                        <a href="${env}/auth" class="header-link">Sign In</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <nav class="navbar">
        <div class="container">
            <div class="nav-content">
                <div class="nav-left">
                    <a href="${env}/" class="logo">
                        <img src="${env}/images/icons/e0891c394d4f7b7c09e783e29df07505.png" alt="Nike" width="60" height="22">
                    </a>
                </div>
                
                <div class="nav-center">
                    <ul class="nav-menu">
                        <li class="nav-item">
                            <a href="${env}/products" class="nav-link">New & Featured</a>
                        </li>
                        <li class="nav-item">
                            <a href="${env}/products?category=men" class="nav-link">Men</a>
                        </li>
                        <li class="nav-item">
                            <a href="${env}/products?category=women" class="nav-link">Women</a>
                        </li>
                        <li class="nav-item">
                            <a href="${env}/products?category=kids" class="nav-link">Kids</a>
                        </li>
                        <li class="nav-item">
                            <a href="${env}/products?sale=true" class="nav-link">Sale</a>
                        </li>
                        <li class="nav-item">
                            <a href="${env}/products?category=snkrs" class="nav-link">SNKRS</a>
                        </li>
                    </ul>
                </div>
                
                <div class="nav-right">
                    <div class="search-container">
                        <div class="search-wrapper">
                            <div class="search-input-wrapper">
                                <input type="text" placeholder="Search" class="search-input" id="searchInput">
                                <img src="${env}/images/icons/search-interface-symbol.png" alt="Search" class="search-icon" style="width: 20px;">
                            </div>
                        </div>
                    </div>
                    
                    <div class="nav-actions">
                        <button class="icon-btn wishlist-btn" aria-label="Favorites">
                            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
                            </svg>
                        </button>
                        <a href="${env}/cart" class="icon-btn cart-btn" aria-label="Bag">
                            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path>
                                <line x1="3" y1="6" x2="21" y2="6"></line>
                                <path d="M16 10a4 4 0 0 1-8 0"></path>
                            </svg>
                            <span class="cart-count" id="cartCount">0</span>
                        </a>
                    </div>
                </div>
                
                <!-- Mobile Actions Container -->
                <div class="mobile-actions">
                    <button class="icon-btn mobile-search-btn" aria-label="Search">
                        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="11" cy="11" r="8"></circle>
                            <path d="m21 21-4.35-4.35"></path>
                        </svg>
                    </button>
                    <button class="icon-btn mobile-profile-btn" aria-label="Profile">
                        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                            <circle cx="12" cy="7" r="4"></circle>
                        </svg>
                    </button>
                    <a href="${env}/cart" class="icon-btn mobile-cart-btn" aria-label="Bag">
                        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path>
                            <line x1="3" y1="6" x2="21" y2="6"></line>
                            <path d="M16 10a4 4 0 0 1-8 0"></path>
                        </svg>
                        <span class="cart-count" id="mobileCartCount">0</span>
                    </a>
                    <button class="mobile-menu-btn" onclick="toggleMobileMenu()" aria-label="Menu">
                        <span></span>
                        <span></span>
                        <span></span>
                    </button>
                </div>
            </div>
        </div>
        
        <!-- Mobile Menu Overlay -->
        <div class="mobile-menu-overlay" id="mobileMenuOverlay">
            <div class="mobile-menu-content">
                <button class="mobile-menu-close" onclick="toggleMobileMenu()" aria-label="Close menu">
                    ✕
                </button>
                
                <ul class="mobile-nav-menu">
                    <li class="mobile-nav-item">
                        <a href="${env}/products" class="mobile-nav-link">New & Featured</a>
                    </li>
                    <li class="mobile-nav-item">
                        <a href="${env}/products?category=men" class="mobile-nav-link">Men</a>
                    </li>
                    <li class="mobile-nav-item">
                        <a href="${env}/products?category=women" class="mobile-nav-link">Women</a>
                    </li>
                    <li class="mobile-nav-item">
                        <a href="${env}/products?category=kids" class="mobile-nav-link">Kids</a>
                    </li>
                    <li class="mobile-nav-item">
                        <a href="${env}/products?sale=true" class="mobile-nav-link">Sale</a>
                    </li>
                    <li class="mobile-nav-item">
                        <a href="${env}/products?category=snkrs" class="mobile-nav-link">SNKRS</a>
                    </li>
                </ul>
                
                <!-- Jordan Brand Section -->
                <div class="mobile-jordan-section">
                    <a href="${env}/" class="mobile-jordan-brand">
                        <img src="${env}/images/icons/air-jordan-logo.png" alt="Jordan" class="mobile-jordan-logo">
                        Jordan
                    </a>
                </div>
                
                <!-- Nike Member Section -->
                <div class="mobile-member-section">
                    <p class="mobile-member-text">
                        Become a Nike Member for the best products, inspiration and stories in sport. 
                        <a href="#" style="color: #111; text-decoration: underline;">Learn more</a>
                    </p>
                    <div class="mobile-member-actions">
                        <a href="#" class="mobile-member-btn primary">Join Us</a>
                        <a href="${env}/auth" class="mobile-member-btn secondary">Sign In</a>
                    </div>
                </div>
                
                <div class="mobile-menu-footer">
                    <div class="mobile-menu-links">
                        <a href="#" class="mobile-menu-link">
                            <svg viewBox="0 0 24 24" fill="none">
                                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                            </svg>
                            Help
                        </a>
                        <a href="${env}/checkout" class="mobile-menu-link">
                            <svg viewBox="0 0 24 24" fill="none">
                                <path d="M19 7h-3V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v2H5a1 1 0 0 0-1 1v11a3 3 0 0 0 3 3h10a3 3 0 0 0 3-3V8a1 1 0 0 0-1-1zM10 5h4v2h-4V5zm8 15a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V9h2v1a1 1 0 0 0 2 0V9h4v1a1 1 0 0 0 2 0V9h2v11z"/>
                            </svg>
                            Bag
                        </a>
                        <a href="#" class="mobile-menu-link">
                            <svg viewBox="0 0 24 24" fill="none">
                                <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
                            </svg>
                            Orders
                        </a>
                        <a href="#" class="mobile-menu-link">
                            <svg viewBox="0 0 24 24" fill="none">
                                <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/>
                            </svg>
                            Find a Store
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</header>