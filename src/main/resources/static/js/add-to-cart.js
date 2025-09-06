// Size selection logic
function selectSize(element) {
    document.querySelectorAll('.size-option').forEach(opt => {
        opt.classList.remove('selected');
        opt.setAttribute('aria-checked', 'false');
    });

    if (!element.classList.contains('unavailable')) {
        element.classList.add('selected');
        element.setAttribute('aria-checked', 'true');
    }
}

// Add to cart logic
function addToCart(productId) {
    var selectedSizeElem = document.querySelector('.size-option.selected');
    if (!selectedSizeElem) {
        alert('Please select a size before adding to cart.');
        return;
    }
    var size = selectedSizeElem.getAttribute('data-size');
    var quantity = 1;

    fetch('/api/cart/add/' + productId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [document.querySelector('meta[name="_csrf_header"]').content]:
                document.querySelector('meta[name="_csrf"]').content
        },
        body: 'quantity=' + encodeURIComponent(quantity) + '&size=' + encodeURIComponent(size)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('Added! Cart count: ' + data.itemCount + ', Subtotal: $' + data.subtotal);
        }
    })
    .catch(err => console.error('Error:', err));
}


