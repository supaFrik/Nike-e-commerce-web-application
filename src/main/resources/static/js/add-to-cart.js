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

// Toast logic
function showToast(message) {
    let toast = document.getElementById('toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        toast.className = 'toast';
        toast.innerHTML = `
            <i class="fas fa-exclamation-circle"></i>
            <p class="toast-text"></p>
            <i class="fas fa-close" id="close-toast"></i>
        `;
        document.body.appendChild(toast);

        // Add CSS only once
        if (!document.getElementById('toast-style')) {
            const style = document.createElement('style');
            style.id = 'toast-style';
            style.textContent = `
                .toast {
                  position: fixed;
                  bottom: 25px;
                  right: 25px;
                  width: 375px;
                  background: #FFF;
                  padding: 25px;
                  display: flex;
                  align-items: center;
                  justify-content: space-between;
                  border-radius: 12px;
                  border-left: 3px solid black;
                  overflow: hidden;
                  transform: translateY(calc(100% + 25px));
                  transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.35);
                  z-index: 9999;
                }
                .toast.active {
                  transform: translateY(0);
                }
                .toast i:first-child {
                  color: black;
                  font-size: 20px;
                }
                .toast-text {
                  margin: 0;
                  font-size: .8125rem;
                  text-transform: uppercase;
                }
                .toast i:last-child {
                  color: #ccc;
                  cursor: pointer;
                  transition: 350ms;
                }
                .toast i:last-child:hover {
                  color: #333;
                }
            `;
            document.head.appendChild(style);
        }

        toast.querySelector('#close-toast').addEventListener('click', () => {
            toast.classList.remove('active');
        });
    }
    toast.querySelector('.toast-text').textContent = message;
    toast.classList.add('active');
    setTimeout(() => {
        toast.classList.remove('active');
    }, 5000);
}

// Add to cart logic
function addToCart(productId) {
    var selectedSizeElem = document.querySelector('.size-option.selected');
    if (!selectedSizeElem) {
        showToast('Please select a size before adding to cart.');
        return;
    }
    var size = selectedSizeElem.getAttribute('data-size');
    var selectedColorElem = document.querySelector('.color-option.selected');
    if (!selectedColorElem) {
        showToast('Please select a color before adding to cart.');
        return;
    }
    var color = selectedColorElem.getAttribute('data-color');
    var quantity = 1;

    fetch('/api/cart/add/' + productId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [document.querySelector('meta[name="_csrf_header"]').content]:
                document.querySelector('meta[name="_csrf"]').content
        },
        body: 'quantity=' + encodeURIComponent(quantity) + '&size=' + encodeURIComponent(size) + '&color=' + encodeURIComponent(color)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            showToast('Added! Cart count: ' + data.itemCount);
        }
    })
    .catch(err => console.error('Error:', err));
}
