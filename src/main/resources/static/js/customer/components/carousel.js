// Simple Minimalistic Carousel for Nike Website
// Handles all carousel sections including Shop by Sport

document.addEventListener('DOMContentLoaded', function() {
    initializeSportCarousel();
    initializeShopIconsCarousel();
    initializeShopRunningCarousel();
});

// Shop By Sport Carousel
function initializeSportCarousel() {
    const track = document.getElementById('sportSliderTrack');
    const prevBtn = document.getElementById('sportPrev');
    const nextBtn = document.getElementById('sportNext');
    const wrapper = track?.parentElement; // The scrollable wrapper
    
    if (!track || !prevBtn || !nextBtn || !wrapper) return;
    
    const slides = track.querySelectorAll('.sport-slide');
    if (slides.length === 0) return;
    
    function getScrollDistance() {
        // Calculate how much to scroll based on slide width + gap
        const slideWidth = slides[0].offsetWidth;
        const computedStyle = window.getComputedStyle(track);
        const gap = parseFloat(computedStyle.gap) || 20;
        return slideWidth + gap;
    }
    
    function updateButtonStates() {
        const scrollLeft = wrapper.scrollLeft;
        const maxScroll = wrapper.scrollWidth - wrapper.clientWidth;
        
        // Update button states based on scroll position
        prevBtn.style.opacity = scrollLeft <= 0 ? '0.5' : '1';
        nextBtn.style.opacity = scrollLeft >= maxScroll - 1 ? '0.5' : '1';
        prevBtn.style.pointerEvents = scrollLeft <= 0 ? 'none' : 'auto';
        nextBtn.style.pointerEvents = scrollLeft >= maxScroll - 1 ? 'none' : 'auto';
    }
    
    // Next slide function
    nextBtn.addEventListener('click', () => {
        const scrollDistance = getScrollDistance();
        wrapper.scrollBy({
            left: scrollDistance,
            behavior: 'smooth'
        });
    });
    
    // Previous slide function
    prevBtn.addEventListener('click', () => {
        const scrollDistance = getScrollDistance();
        wrapper.scrollBy({
            left: -scrollDistance,
            behavior: 'smooth'
        });
    });
    
    // Add click handlers for sport slides
    slides.forEach(slide => {
        slide.addEventListener('click', function() {
            const sport = this.getAttribute('data-sport');
            if (sport) {
                // Navigate to product list with sport filter
                window.location.href = `./product-list.html?sport=${sport}`;
            }
        });
        
        // Add keyboard navigation
        slide.setAttribute('tabindex', '0');
        slide.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });
    });
    
    // Update button states on scroll
    wrapper.addEventListener('scroll', updateButtonStates);
    
    // Update button states on resize
    window.addEventListener('resize', () => {
        setTimeout(updateButtonStates, 100);
    });
    
    // Touch/swipe support for mobile
    let startX = 0;
    let isDragging = false;
    
    wrapper.addEventListener('touchstart', (e) => {
        startX = e.touches[0].clientX;
        isDragging = true;
    }, { passive: true });
    
    wrapper.addEventListener('touchmove', (e) => {
        if (!isDragging) return;
        e.preventDefault();
        
        const currentX = e.touches[0].clientX;
        const diff = startX - currentX;
        wrapper.scrollLeft += diff;
        startX = currentX;
    });
    
    wrapper.addEventListener('touchend', () => {
        isDragging = false;
    }, { passive: true });
    
    // Keyboard navigation for carousel
    document.addEventListener('keydown', function(e) {
        if (e.target.closest('.shop-by-sport-section')) {
            if (e.key === 'ArrowLeft') {
                e.preventDefault();
                const scrollDistance = getScrollDistance();
                wrapper.scrollBy({
                    left: -scrollDistance,
                    behavior: 'smooth'
                });
            } else if (e.key === 'ArrowRight') {
                e.preventDefault();
                const scrollDistance = getScrollDistance();
                wrapper.scrollBy({
                    left: scrollDistance,
                    behavior: 'smooth'
                });
            }
        }
    });
    
    // Initial button state
    setTimeout(updateButtonStates, 100);
}

// Shop Icons Carousel
function initializeShopIconsCarousel() {
    const track = document.getElementById('shopIconsTrack');
    const prevBtn = document.getElementById('shopIconsPrevBtn');
    const nextBtn = document.getElementById('shopIconsNextBtn');
    const wrapper = track?.parentElement; // The scrollable wrapper
    
    if (!track || !prevBtn || !nextBtn || !wrapper) return;
    
    const cards = track.querySelectorAll('.shop-icon-card');
    if (cards.length === 0) return;
    
    function getScrollDistance() {
        // Calculate how much to scroll based on card width + gap
        const cardWidth = cards[0].offsetWidth;
        const computedStyle = window.getComputedStyle(track);
        const gap = parseFloat(computedStyle.gap) || 20;
        return cardWidth + gap;
    }
    
    function updateButtonStates() {
        const scrollLeft = wrapper.scrollLeft;
        const maxScroll = wrapper.scrollWidth - wrapper.clientWidth;
        
        // Update button states based on scroll position
        prevBtn.style.opacity = scrollLeft <= 0 ? '0.5' : '1';
        nextBtn.style.opacity = scrollLeft >= maxScroll - 1 ? '0.5' : '1';
        prevBtn.style.pointerEvents = scrollLeft <= 0 ? 'none' : 'auto';
        nextBtn.style.pointerEvents = scrollLeft >= maxScroll - 1 ? 'none' : 'auto';
    }
    
    // Next slide function
    nextBtn.addEventListener('click', () => {
        const scrollDistance = getScrollDistance();
        wrapper.scrollBy({
            left: scrollDistance,
            behavior: 'smooth'
        });
    });
    
    // Previous slide function
    prevBtn.addEventListener('click', () => {
        const scrollDistance = getScrollDistance();
        wrapper.scrollBy({
            left: -scrollDistance,
            behavior: 'smooth'
        });
    });
    
    // Update button states on scroll
    wrapper.addEventListener('scroll', updateButtonStates);
    
    // Update button states on resize
    window.addEventListener('resize', () => {
        setTimeout(updateButtonStates, 100);
    });
    
    // Initial button state
    setTimeout(updateButtonStates, 100);
}

// Shop Running/Golf Carousel
function initializeShopRunningCarousel() {
    const track = document.getElementById('shopRunningTrack');
    const prevBtn = document.getElementById('shopRunningPrevBtn');
    const nextBtn = document.getElementById('shopRunningNextBtn');
    const wrapper = track?.parentElement; // The scrollable wrapper
    
    if (!track || !prevBtn || !nextBtn || !wrapper) return;
    
    const cards = track.querySelectorAll('.running-card');
    if (cards.length === 0) return;
    
    function getScrollDistance() {
        // Calculate how much to scroll based on card width + gap
        const cardWidth = cards[0].offsetWidth;
        const computedStyle = window.getComputedStyle(track);
        const gap = parseFloat(computedStyle.gap) || 20;
        return cardWidth + gap;
    }
    
    function updateButtonStates() {
        const scrollLeft = wrapper.scrollLeft;
        const maxScroll = wrapper.scrollWidth - wrapper.clientWidth;
        
        // Update button states based on scroll position
        prevBtn.style.opacity = scrollLeft <= 0 ? '0.5' : '1';
        nextBtn.style.opacity = scrollLeft >= maxScroll - 1 ? '0.5' : '1';
        prevBtn.style.pointerEvents = scrollLeft <= 0 ? 'none' : 'auto';
        nextBtn.style.pointerEvents = scrollLeft >= maxScroll - 1 ? 'none' : 'auto';
    }
    
    // Next slide function
    nextBtn.addEventListener('click', () => {
        const scrollDistance = getScrollDistance();
        wrapper.scrollBy({
            left: scrollDistance,
            behavior: 'smooth'
        });
    });
    
    // Previous slide function
    prevBtn.addEventListener('click', () => {
        const scrollDistance = getScrollDistance();
        wrapper.scrollBy({
            left: -scrollDistance,
            behavior: 'smooth'
        });
    });
    
    // Update button states on scroll
    wrapper.addEventListener('scroll', updateButtonStates);
    
    // Update button states on resize
    window.addEventListener('resize', () => {
        setTimeout(updateButtonStates, 100);
    });
    
    // Initial button state
    setTimeout(updateButtonStates, 100);
}
class CarouselManager {
    constructor() {
        this.carousels = new Map();
        this.init();
    }

    init() {
        // Initialize all carousels on the page
        this.initializeSportCarousel();
        this.initializeShopIconsCarousel();
        this.initializeShopRunningCarousel();
        this.initializeFavouritesCarousel();
        this.initializeBenefitsCarousel();
    }

    // Generic carousel initialization
    createCarousel(config) {
        const {
            trackId,
            prevBtnId,
            nextBtnId,
            slideSelector,
            onSlideClick,
            enableKeyboard = true,
            enableTouch = true
        } = config;

        const track = document.getElementById(trackId);
        const prevBtn = document.getElementById(prevBtnId);
        const nextBtn = document.getElementById(nextBtnId);
        const wrapper = track?.parentElement;

        if (!track || !prevBtn || !nextBtn || !wrapper) {
            console.warn(`Carousel elements not found for ${trackId}`);
            return null;
        }

        const slides = track.querySelectorAll(slideSelector);
        if (slides.length === 0) return null;

        const carousel = {
            track,
            wrapper,
            prevBtn,
            nextBtn,
            slides,
            isDragging: false,
            startX: 0,
            scrollStartLeft: 0
        };

        this.setupCarouselNavigation(carousel, config);
        if (enableKeyboard) this.setupKeyboardNavigation(carousel, config);
        if (enableTouch) this.setupTouchNavigation(carousel);
        this.setupSlideInteractions(carousel, onSlideClick);

        this.carousels.set(trackId, carousel);
        return carousel;
    }

    setupCarouselNavigation(carousel, config) {
        const { wrapper, prevBtn, nextBtn } = carousel;

        const getScrollDistance = () => {
            const slide = carousel.slides[0];
            const slideWidth = slide.offsetWidth;
            const computedStyle = window.getComputedStyle(carousel.track);
            const gap = parseFloat(computedStyle.gap) || 20;
            return slideWidth + gap;
        };

        const updateButtonStates = () => {
            const scrollLeft = wrapper.scrollLeft;
            const maxScroll = wrapper.scrollWidth - wrapper.clientWidth;

            // Update button states
            this.updateButtonState(prevBtn, scrollLeft <= 0);
            this.updateButtonState(nextBtn, scrollLeft >= maxScroll - 1);
        };

        // Navigation handlers
        nextBtn.addEventListener('click', () => {
            this.scrollCarousel(wrapper, getScrollDistance(), 'forward');
        });

        prevBtn.addEventListener('click', () => {
            this.scrollCarousel(wrapper, getScrollDistance(), 'backward');
        });

        // Update states on scroll and resize
        wrapper.addEventListener('scroll', updateButtonStates);
        window.addEventListener('resize', () => {
            setTimeout(updateButtonStates, 100);
        });

        // Initial state
        setTimeout(updateButtonStates, 100);
    }

    updateButtonState(button, isDisabled) {
        button.style.opacity = isDisabled ? '0.5' : '1';
        button.style.pointerEvents = isDisabled ? 'none' : 'auto';
        button.setAttribute('aria-disabled', isDisabled.toString());
    }

    scrollCarousel(wrapper, scrollDistance, direction) {
        const scrollAmount = direction === 'forward' ? scrollDistance : -scrollDistance;

        wrapper.scrollBy({
            left: scrollAmount,
            behavior: 'smooth'
        });
    }

    setupKeyboardNavigation(carousel, config) {
        const { wrapper, trackId } = carousel;
        const sectionClass = config.sectionClass || `.${trackId}-section`;

        document.addEventListener('keydown', (e) => {
            if (!e.target.closest(sectionClass)) return;

            const slideWidth = carousel.slides[0]?.offsetWidth || 0;
            const gap = parseFloat(window.getComputedStyle(carousel.track).gap) || 20;
            const scrollDistance = slideWidth + gap;

            switch (e.key) {
                case 'ArrowLeft':
                    e.preventDefault();
                    this.scrollCarousel(wrapper, scrollDistance, 'backward');
                    break;
                case 'ArrowRight':
                    e.preventDefault();
                    this.scrollCarousel(wrapper, scrollDistance, 'forward');
                    break;
                case 'Home':
                    e.preventDefault();
                    wrapper.scrollTo({ left: 0, behavior: 'smooth' });
                    break;
                case 'End':
                    e.preventDefault();
                    wrapper.scrollTo({ left: wrapper.scrollWidth, behavior: 'smooth' });
                    break;
            }
        });
    }

    setupTouchNavigation(carousel) {
        const { wrapper } = carousel;

        // Touch events
        wrapper.addEventListener('touchstart', (e) => {
            carousel.startX = e.touches[0].clientX;
            carousel.scrollStartLeft = wrapper.scrollLeft;
            carousel.isDragging = true;
        }, { passive: true });

        wrapper.addEventListener('touchmove', (e) => {
            if (!carousel.isDragging) return;

            const currentX = e.touches[0].clientX;
            const deltaX = carousel.startX - currentX;
            wrapper.scrollLeft = carousel.scrollStartLeft + deltaX;
        }, { passive: false });

        wrapper.addEventListener('touchend', () => {
            carousel.isDragging = false;
        }, { passive: true });

        // Mouse drag events
        wrapper.addEventListener('mousedown', (e) => {
            carousel.startX = e.clientX;
            carousel.scrollStartLeft = wrapper.scrollLeft;
            carousel.isDragging = true;
            wrapper.style.cursor = 'grabbing';
            wrapper.style.userSelect = 'none';
        });

        wrapper.addEventListener('mousemove', (e) => {
            if (!carousel.isDragging) return;

            e.preventDefault();
            const deltaX = carousel.startX - e.clientX;
            wrapper.scrollLeft = carousel.scrollStartLeft + deltaX;
        });

        wrapper.addEventListener('mouseup', () => {
            carousel.isDragging = false;
            wrapper.style.cursor = 'grab';
            wrapper.style.userSelect = 'auto';
        });

        wrapper.addEventListener('mouseleave', () => {
            carousel.isDragging = false;
            wrapper.style.cursor = 'grab';
            wrapper.style.userSelect = 'auto';
        });
    }

    setupSlideInteractions(carousel, onSlideClick) {
        if (!onSlideClick) return;

        carousel.slides.forEach((slide, index) => {
            slide.addEventListener('click', (e) => {
                onSlideClick(slide, index, e);
            });

            // Keyboard accessibility
            slide.setAttribute('tabindex', '0');
            slide.setAttribute('role', 'button');

            slide.addEventListener('keypress', (e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    onSlideClick(slide, index, e);
                }
            });
        });
    }

    // Specific carousel initializations
    initializeSportCarousel() {
        this.createCarousel({
            trackId: 'sportSliderTrack',
            prevBtnId: 'sportPrev',
            nextBtnId: 'sportNext',
            slideSelector: '.sport-slide',
            sectionClass: '.shop-by-sport-section',
            onSlideClick: (slide) => {
                const sport = slide.getAttribute('data-sport');
                if (sport) {
                    window.location.href = `./product-list.html?sport=${sport}`;
                }
            }
        });
    }

    initializeShopIconsCarousel() {
        this.createCarousel({
            trackId: 'shopIconsTrack',
            prevBtnId: 'shopIconsPrevBtn',
            nextBtnId: 'shopIconsNextBtn',
            slideSelector: '.shop-icon-card',
            sectionClass: '.shop-icons-section',
            onSlideClick: (slide) => {
                const category = slide.getAttribute('data-category');
                if (category) {
                    window.location.href = `./product-list.html?category=${category}`;
                }
            }
        });
    }

    initializeShopRunningCarousel() {
        this.createCarousel({
            trackId: 'shopRunningTrack',
            prevBtnId: 'shopRunningPrevBtn',
            nextBtnId: 'shopRunningNextBtn',
            slideSelector: '.running-card',
            sectionClass: '.shop-running-section',
            onSlideClick: (slide) => {
                const productUrl = slide.getAttribute('data-url');
                const productId = slide.getAttribute('data-product-id');

                if (productUrl) {
                    window.location.href = productUrl;
                } else if (productId) {
                    window.location.href = `./product-detail.html?id=${productId}`;
                }
            }
        });
    }

    initializeFavouritesCarousel() {
        this.createCarousel({
            trackId: 'favouritesTrack',
            prevBtnId: 'favouritesPrevBtn',
            nextBtnId: 'favouritesNextBtn',
            slideSelector: '.favourite-card',
            sectionClass: '.favourites-section',
            onSlideClick: (slide) => {
                // Try to navigate to a product if data attributes exist
                const productUrl = slide.getAttribute('data-url');
                const productId = slide.getAttribute('data-product-id');

                if (productUrl) {
                    window.location.href = productUrl;
                } else if (productId) {
                    window.location.href = `./product-detail.html?id=${productId}`;
                }
            }
        });
    }

    initializeBenefitsCarousel() {
        this.createCarousel({
            trackId: 'benefitsTrack',
            prevBtnId: 'benefitsPrevBtn',
            nextBtnId: 'benefitsNextBtn',
            slideSelector: '.benefit-card',
            sectionClass: '.benefits-section',
            onSlideClick: (slide) => {
                // Optional: Add specific behavior for benefit cards
                // For now, we'll just have them be interactive without navigation
                console.log('Benefit card clicked:', slide);
            }
        });
    }

    // Public API methods
    getCarousel(trackId) {
        return this.carousels.get(trackId);
    }

    scrollToSlide(trackId, slideIndex) {
        const carousel = this.carousels.get(trackId);
        if (!carousel || !carousel.slides[slideIndex]) return;

        const slide = carousel.slides[slideIndex];
        const slideWidth = slide.offsetWidth;
        const gap = parseFloat(window.getComputedStyle(carousel.track).gap) || 20;
        const scrollPosition = slideIndex * (slideWidth + gap);

        carousel.wrapper.scrollTo({
            left: scrollPosition,
            behavior: 'smooth'
        });
    }

    next(trackId) {
        const carousel = this.carousels.get(trackId);
        if (!carousel) return;

        carousel.nextBtn.click();
    }

    previous(trackId) {
        const carousel = this.carousels.get(trackId);
        if (!carousel) return;

        carousel.prevBtn.click();
    }

    destroy() {
        this.carousels.clear();
    }
}

// Initialize carousel manager
let carouselManager;
document.addEventListener('DOMContentLoaded', () => {
    carouselManager = new CarouselManager();
});

// Global functions for backward compatibility
function initializeSportCarousel() {
    if (carouselManager) carouselManager.initializeSportCarousel();
}

function initializeShopIconsCarousel() {
    if (carouselManager) carouselManager.initializeShopIconsCarousel();
}

function initializeShopRunningCarousel() {
    if (carouselManager) carouselManager.initializeShopRunningCarousel();
}

function initializeFavouritesCarousel() {
    if (carouselManager) carouselManager.initializeFavouritesCarousel();
}

function initializeBenefitsCarousel() {
    if (carouselManager) carouselManager.initializeBenefitsCarousel();
}
