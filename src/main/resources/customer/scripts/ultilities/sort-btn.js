// Sort By dropdown animation
const sortToggle = document.getElementById('sort-toggle');
const sortOptions = document.getElementById('sort-options');
const sortArrow = document.getElementById('sort-arrow');
const sortByBtn = document.getElementById('sortByBtn');
const sortForm = document.getElementById('sort-form');
const sortInput = document.getElementById('sort-input');
let sortOpen = false;

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
        // Set hidden input and submit form
        if (sortInput) sortInput.value = li.getAttribute('data-value');
        if (sortForm) sortForm.submit();
        setSortDropdown(false);
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

////Hiding buggy sortBtn
//const sortToggle = document.getElementById('sort-toggle');
//const sortOptions = document.getElementById('sort-options');
//const sortByBtn = document.getElementById('sortByBtn');
//
//function setSortDropdown(open) {
//    if (open) {
//        sortOptions.classList.add('show');
//        sortToggle.setAttribute('aria-expanded', 'true');
//    } else {
//        sortOptions.classList.remove('show');
//        sortToggle.setAttribute('aria-expanded', 'false');
//    }
//}
//
//let sortOpen = false;
//
//sortToggle.addEventListener('click', function(e) {
//    e.stopPropagation();
//    sortOpen = !sortOpen;
//    setSortDropdown(sortOpen);
//    if (sortOpen) sortOptions.querySelector('select').focus();
//});
//
//window.addEventListener('click', function(e) {
//    if (!sortByBtn.contains(e.target)) {
//        sortOpen = false;
//        setSortDropdown(false);
//    }
//});