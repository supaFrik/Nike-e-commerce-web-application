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
                  padding:0 6px;
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
  const selectedSizeElem = document.querySelector('.size-option.selected');
  const selectedColorElem = document.querySelector('.color-option.selected');

  if (!selectedSizeElem || !selectedColorElem) {
    showToast('Please select size and color first.');
    return;
  }

  const size = selectedSizeElem.getAttribute('data-size');
  const color = selectedColorElem.getAttribute('data-color-name'); // Updated to use data-color-name
  const quantity = 1;

  const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
  const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
  const headers = {
    'Content-Type': 'application/x-www-form-urlencoded'
  };
  if (csrfTokenMeta && csrfHeaderMeta) {
    headers[csrfHeaderMeta.getAttribute('content')] = csrfTokenMeta.getAttribute('content');
  }

  const body = new URLSearchParams({
    quantity: quantity,
    size: size,
    color: color
  }).toString();

  fetch('/api/cart/add/' + productId, {
    method: 'POST',
    headers: headers,
    body: body,
    credentials: 'same-origin'
  })
  .then(res => {
    if (!res.ok) {
      return res.json().then(err => Promise.reject(err));
    }
    return res.json();
  })
  .then(data => {
    if (data.success) {
      const badge = document.getElementById('cartBadge');
      if (badge && data.itemCount != null) {
        badge.innerText = data.itemCount;
      }
      showToast('Added to cart — ' + data.itemCount + ' items');
    } else {
      showToast('Add to cart failed: ' + (data.error || JSON.stringify(data)));
    }
  })
  .catch(err => {
    console.error('Add to cart error', err);
    const msg = err && err.error ? err.error : (err && err.message ? err.message : 'Unknown error');
    showToast('Add to cart error: ' + msg);
  });
}
