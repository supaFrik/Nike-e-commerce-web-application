// Sort By dropdown animation
// Refactored: Add event delegation for sort options, highlight selected, and keep structure minimal
const sortToggle = document.getElementById('sort-toggle');
const sortOptions = document.getElementById('sort-options');
const sortArrow = document.getElementById('sort-arrow');
const sortByBtn = sortToggle?.closest('.sort-by-btn');
let sortOpen = false;
let selectedSort = null;

// Ensure sortArrow has transition for transform
if (sortArrow) {
    sortArrow.style.transition = 'transform 0.3s cubic-bezier(0.4,0,0.2,1)';
}

function setSortDropdown(open) {
    if (!sortOptions || !sortToggle || !sortArrow || !sortByBtn) return;
    sortOpen = open;
    if (open) {
        sortOptions.classList.add('show');
        sortByBtn.classList.add('expanded');
        sortArrow.style.transform = 'rotate(180deg)';
    } else {
        sortOptions.classList.remove('show');
        sortByBtn.classList.remove('expanded');
        sortArrow.style.transform = 'rotate(0deg)';
    }
}

sortToggle?.addEventListener('click', function(e) {
    e.stopPropagation();
    setSortDropdown(!sortOpen);
});

// Event delegation for sort options
sortOptions?.addEventListener('click', function(e) {
    const li = e.target.closest('.sort-option');
    if (li) {
        // Remove active from all
        sortOptions.querySelectorAll('.sort-option').forEach(opt => opt.classList.remove('active'));
        li.classList.add('active');
        selectedSort = li.textContent.trim();
        setSortDropdown(false);
        // Optionally, trigger sorting logic here
    }
});

// Optional: close dropdown when clicking outside
window.addEventListener('click', function(e) {
    if (!sortToggle?.contains(e.target) && !sortOptions?.contains(e.target)) {
        setSortDropdown(false);
    }
});

// Hide sidebar filter and categories when 'Hide Filters' is pressed
const filterToggle = document.getElementById('filter-toggle');

filterToggle?.addEventListener('click', function() {
    var sidebar = document.getElementById('left-nav');
    var categories = document.querySelector('.left-nav-categories');
    if (sidebar) {
        sidebar.classList.toggle('hide');
    }
    if (categories) {
        categories.style.display = categories.style.display === 'none' ? '' : 'none';
    }
});