// Dropdown Module
// Handles category dropdown functionality

function toggleDropdown() {
    const categoryDropdown = document.querySelector('.custom-dropdown');
    const dropdown = document.getElementById('dropdown-options');
    const arrow = document.querySelector('.custom-dropdown .dropdown-arrow');
    const toggleEl = document.getElementById('dropdownToggle');
    if (dropdown && arrow && categoryDropdown) {
        const isCurrentlyOpen = dropdown.classList.contains('show');

        // Close all other dropdowns first
        closeAllDropdowns();

        // Toggle this dropdown
        if (!isCurrentlyOpen) {
            categoryDropdown.classList.add('open');
            dropdown.classList.add('show');
            arrow.classList.add('rotated');
            if(toggleEl) toggleEl.setAttribute('aria-expanded','true');
        } else {
            if(toggleEl) toggleEl.setAttribute('aria-expanded','false');
        }
    }
}

// Helper function to close all dropdowns
function closeAllDropdowns() {
    const categoryDropdown = document.querySelector('.custom-dropdown');
    const categoryOptions = document.getElementById('dropdown-options');
    const categoryArrow = document.querySelector('.custom-dropdown .dropdown-arrow');
    const toggleEl = document.getElementById('dropdownToggle');
    if (categoryDropdown) categoryDropdown.classList.remove('open');
    if (categoryOptions) categoryOptions.classList.remove('show');
    if (categoryArrow) categoryArrow.classList.remove('rotated');
    if (toggleEl) toggleEl.setAttribute('aria-expanded','false');
}

function selectCategory(category) {
    window.AppState.setSelectedCategory(category);
    const span = document.getElementById('selected-category');
    if (span) span.textContent = category;

    // map category name to ID via data attribute if present
    const option = Array.from(document.querySelectorAll('#dropdown-options .dropdown-option'))
        .find(o => o.textContent.trim() === category);
    if (option && option.dataset.id) {
        const hid = document.getElementById('categoryIdHidden');
        if (hid) hid.value = option.dataset.id;
    }

    // Close the dropdown
    closeAllDropdowns();
}

// Initialize dropdown event listeners
function initializeDropdown() {
    // Close dropdowns when clicking outside
    document.addEventListener('click', function(event) {
        const categoryDropdown = document.querySelector('.custom-dropdown');
        const isInsideCategoryDropdown = categoryDropdown && categoryDropdown.contains(event.target);
        if (!isInsideCategoryDropdown) {
            closeAllDropdowns();
        }
    });

    // Toggle element handlers (click & keyboard)
    const toggleEl = document.getElementById('dropdownToggle');
    if(toggleEl){
        const openHandler = function(e){ e.preventDefault(); toggleDropdown(); };
        toggleEl.addEventListener('click', openHandler);
        toggleEl.addEventListener('keydown', function(e){ if(e.key==='Enter' || e.key===' '){ openHandler(e); }});
        toggleEl.setAttribute('aria-expanded','false');
    }

    // Delegate option selection
    const optionsContainer = document.getElementById('dropdown-options');
    if(optionsContainer){
        optionsContainer.addEventListener('click', function(e){
            const opt = e.target.closest('.dropdown-option[data-id]');
            if(!opt || opt.style.opacity==='0.6') return;
            const name = opt.getAttribute('data-name') || opt.textContent.trim();
            selectCategory(name);
        });
    }
}

// Export for global access
window.Dropdown = {
    toggle: toggleDropdown,
    closeAll: closeAllDropdowns,
    selectCategory: selectCategory,
    initialize: initializeDropdown
};