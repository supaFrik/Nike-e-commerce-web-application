function selectSize(element) {
    // Remove 'selected' from all size options
    document.querySelectorAll('.size-option').forEach(opt => opt.classList.remove('selected'));
    // Add 'selected' to the clicked element if not unavailable
    if (!element.classList.contains('unavailable')) {
        element.classList.add('selected');
    }
}