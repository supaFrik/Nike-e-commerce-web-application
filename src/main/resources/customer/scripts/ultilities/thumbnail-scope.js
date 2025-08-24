// Thumbnail navigation for product detail page
// Assumes: main image has id 'mainProductImage', thumbnails have class 'product-thumbnail'
document.addEventListener('DOMContentLoaded', function() {
    const mainImage = document.getElementById('currentImage');
    const thumbnails = document.querySelectorAll('.thumbnail-item img');
    if (!mainImage || thumbnails.length === 0) return;
    thumbnails.forEach(thumb => {
        thumb.addEventListener('mouseenter', function() {
            mainImage.src = thumb.src;
            mainImage.alt = thumb.alt;
            // Update active class
            document.querySelectorAll('.thumbnail-item').forEach(item => item.classList.remove('active'));
            thumb.parentElement.classList.add('active');
        });
    });
});
