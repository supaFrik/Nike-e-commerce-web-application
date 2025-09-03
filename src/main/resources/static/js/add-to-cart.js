function selectSize(element) {
    // Remove 'selected' from all size options
    document.querySelectorAll('.size-option').forEach(opt => opt.classList.remove('selected'));
    // Add 'selected' to the clicked element if not unavailable
    if (!element.classList.contains('unavailable')) {
        element.classList.add('selected');
        element.setAttribute('aria-checked', 'true');
    }
}

// Add to cart logic
function addToCart(productId) {
    var selectedSizeElem = document.querySelector('.size-option.selected');
    if (!selectedSizeElem) {
        alert('Senpai please select a size before adding to cart.');
        return;
    }
    var size = selectedSizeElem.getAttribute('data-size');
    var quantity = 1;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/add/' + ${product.id}, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                alert('Product added to cart successfully!');
            } else {
                alert('Failed to add product to cart. Please try again.');
            }
        }
    };
    var data = 'quantity=' + encodeURIComponent(quantity) + '&size=' + encodeURIComponent(size);
    xhr.send(data);
}
