// Mobile Navigation Utility
// Handles mobile menu sidebar functionality

document.addEventListener('DOMContentLoaded', function() {
    initializeMobileNav();
});

function initializeMobileNav() {
    const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
    const mobileMenuOverlay = document.getElementById('mobileMenuOverlay');
    const body = document.body;
    
    if (!mobileMenuBtn || !mobileMenuOverlay) return;
    
    // Toggle mobile menu
    window.toggleMobileMenu = function() {
        const isOpen = mobileMenuOverlay.classList.contains('active');
        
        if (isOpen) {
            closeMobileMenu();
        } else {
            openMobileMenu();
        }
    };
    
    function openMobileMenu() {
        mobileMenuOverlay.classList.add('active');
        mobileMenuBtn.classList.add('active');
        body.classList.add('menu-open');
        
        // Add escape key listener
        document.addEventListener('keydown', handleEscapeKey);
        
        // Add click outside listener
        setTimeout(() => {
            document.addEventListener('click', handleClickOutside);
        }, 100);
    }
    
    function closeMobileMenu() {
        mobileMenuOverlay.classList.remove('active');
        mobileMenuBtn.classList.remove('active');
        body.classList.remove('menu-open');
        
        // Remove event listeners
        document.removeEventListener('keydown', handleEscapeKey);
        document.removeEventListener('click', handleClickOutside);
    }
    
    function handleEscapeKey(e) {
        if (e.key === 'Escape') {
            closeMobileMenu();
        }
    }
    
    function handleClickOutside(e) {
        const mobileMenuContent = document.querySelector('.mobile-menu-content');
        if (!mobileMenuContent.contains(e.target) && !mobileMenuBtn.contains(e.target)) {
            closeMobileMenu();
        }
    }
    
    // Handle mobile search functionality
    const mobileSearchBtn = document.querySelector('.mobile-search-btn');
    const mobileSearchInput = document.querySelector('.mobile-search-input');
    
    if (mobileSearchBtn && mobileSearchInput) {
        mobileSearchBtn.addEventListener('click', function(e) {
            e.preventDefault();
            const query = mobileSearchInput.value.trim();
            if (query) {
                // Handle search functionality here
                console.log('Mobile search for:', query);
                // You can redirect to search results page
                // window.location.href = `/search?q=${encodeURIComponent(query)}`;
            }
        });
        
        mobileSearchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                mobileSearchBtn.click();
            }
        });
    }
    
    // Handle mobile menu links
    const mobileNavLinks = document.querySelectorAll('.mobile-nav-link');
    mobileNavLinks.forEach(link => {
        link.addEventListener('click', function() {
            // Close menu when link is clicked
            closeMobileMenu();
        });
    });
    
    // Handle mobile profile and cart buttons
    const mobileProfileBtn = document.querySelector('.mobile-profile-btn');
    const mobileCartBtn = document.querySelector('.mobile-cart-btn');
    
    if (mobileProfileBtn) {
        mobileProfileBtn.addEventListener('click', function() {
            // Handle profile action
            window.location.href = './auth.html';
        });
    }
    
    // Handle window resize
    let resizeTimeout;
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(() => {
            // Close mobile menu if screen becomes desktop size
            if (window.innerWidth > 768) {
                closeMobileMenu();
            }
        }, 150);
    });
    
    // Prevent body scroll when menu is open
    function preventBodyScroll() {
        const scrollY = window.scrollY;
        body.style.position = 'fixed';
        body.style.top = `-${scrollY}px`;
        body.style.width = '100%';
    }
    
    function restoreBodyScroll() {
        const scrollY = body.style.top;
        body.style.position = '';
        body.style.top = '';
        body.style.width = '';
        window.scrollTo(0, parseInt(scrollY || '0') * -1);
    }
    
    // Update the open/close functions to handle body scroll
    const originalOpenMobileMenu = openMobileMenu;
    const originalCloseMobileMenu = closeMobileMenu;
    
    openMobileMenu = function() {
        originalOpenMobileMenu();
        preventBodyScroll();
    };
    
    closeMobileMenu = function() {
        originalCloseMobileMenu();
        restoreBodyScroll();
    };
    
    // Update global functions
    window.toggleMobileMenu = function() {
        const isOpen = mobileMenuOverlay.classList.contains('active');
        
        if (isOpen) {
            closeMobileMenu();
        } else {
            openMobileMenu();
        }
    };
}

// Export functions for external use
window.openMobileMenu = function() {
    const event = new CustomEvent('openMobileMenu');
    document.dispatchEvent(event);
};

window.closeMobileMenu = function() {
    const event = new CustomEvent('closeMobileMenu');
    document.dispatchEvent(event);
};
