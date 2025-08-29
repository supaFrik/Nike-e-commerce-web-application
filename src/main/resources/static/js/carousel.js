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
