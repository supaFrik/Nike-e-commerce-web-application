function showToast(message) {
  let toast = document.getElementById('toast');
  if (!toast) {
    toast = document.createElement('div');
    toast.id = 'toast';
    toast.className = 'toast';
    toast.setAttribute('role', 'status');
    toast.setAttribute('aria-live', 'polite');
    toast.innerHTML = `
      <i class="fas fa-exclamation-circle" aria-hidden="true"></i>
      <p class="toast-text"></p>
      <button class="toast-close" id="close-toast" aria-label="Dismiss notification">&times;</button>
    `;
    document.body.appendChild(toast);

    if (!document.getElementById('toast-style')) {
      const style = document.createElement('style');
      style.id = 'toast-style';
      style.textContent = `
        .toast {
          position: fixed;
          bottom: 1.25rem;
          right: 1.25rem;
          background: #111;
          color: #fff;
          padding: 1rem 1.25rem;
          border-radius: 8px;
          box-shadow: 0 6px 28px rgba(0,0,0,.35);
          font-size: .95rem;
          z-index: 1000;
          opacity: 0;
          visibility: hidden;
          transform: translateY(20px) scale(.995);
          transition: opacity .38s cubic-bezier(.2,.9,.2,1), transform .38s cubic-bezier(.2,.9,.2,1), visibility 0s linear .38s;
          pointer-events: none;
          display: flex;
          align-items: center;
          gap: 8px;
        }
        .toast.active {
          opacity: 1;
          display: flex;
          justify-content: center;
          align-items: center;
          visibility: visible;
          transform: translateY(0) scale(1);
          transition: opacity .38s cubic-bezier(.2,.9,.2,1), transform .38s cubic-bezier(.2,.9,.2,1), visibility 0s linear 0s;
          pointer-events: auto;
        }
        .toast i:first-child { color: #fff; font-size: 18px; margin-right: 8px; }
        .toast .toast-text { margin: 0; padding: 0 6px; text-transform: none; color: #fff; line-height: 1.2; }
        .toast .toast-close { background: transparent; border: none; color: #ccc; cursor: pointer; transition: color .2s; margin-left: 6px; font-size: 18px; }
        .toast .toast-close:hover, .toast .toast-close:focus { color: #fff; outline: none; }
      `;
      document.head.appendChild(style);
    }

    const closeBtn = toast.querySelector('#close-toast');
    if (closeBtn) {
      closeBtn.addEventListener('click', function() {
        toast.classList.remove('active');
      });
      closeBtn.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          toast.classList.remove('active');
        }
      });
    }
  }

  toast.querySelector('.toast-text').textContent = message;
  toast.classList.add('active');
  setTimeout(function() {
    toast.classList.remove('active');
  }, 5000);
}

function resolveSelectedVariant() {
  if (typeof productDetailState !== 'undefined' && productDetailState.selectedVariantId) {
    const selectedColor = productDetailState.colors?.[productDetailState.selectedColorIndex];
    const selectedVariant = selectedColor?.variants?.find(function(variant) {
      return String(variant.id) === String(productDetailState.selectedVariantId);
    });

    if (selectedVariant) {
      return selectedVariant;
    }
  }

  const selectedSizeElem = document.querySelector('.size-option.selected');
  if (!selectedSizeElem) {
    return null;
  }

  return {
    id: selectedSizeElem.getAttribute('data-variant-id'),
    size: selectedSizeElem.getAttribute('data-size')
  };
}

function buildCartHeaders() {
  const headers = {
    'Content-Type': 'application/json'
  };

  const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
  const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');

  if (!csrfTokenMeta || !csrfHeaderMeta) {
    return headers;
  }

  const csrfToken = csrfTokenMeta.getAttribute('content');
  const csrfHeader = csrfHeaderMeta.getAttribute('content');
  if (csrfToken && csrfHeader) {
    headers[csrfHeader] = csrfToken;
  }

  return headers;
}

function addToCart() {
  const selectedVariant = resolveSelectedVariant();
  if (!selectedVariant || !selectedVariant.id) {
    showToast('Please select a size first.');
    return;
  }

  const requestData = {
    variantId: Number(selectedVariant.id),
    quantity: 1
  };

  const addToCartBtn = document.querySelector('.add-to-cart');
  const originalText = addToCartBtn ? addToCartBtn.textContent : '';
  if (addToCartBtn) {
    addToCartBtn.disabled = true;
    addToCartBtn.textContent = 'Adding...';
  }

  fetch('/api/cart/items', {
    method: 'POST',
    headers: buildCartHeaders(),
    body: JSON.stringify(requestData),
    credentials: 'same-origin'
  })
    .then(function(res) {
      const contentType = res.headers.get('content-type') || '';

      if (!res.ok) {
        if (contentType.includes('application/json')) {
          return res.json().then(function(err) {
            return Promise.reject(err);
          });
        }

        if (res.status === 401) {
          return Promise.reject({ message: 'Please log in to add items to cart.' });
        }
        if (res.status === 403) {
          return Promise.reject({ message: 'Access denied. Please try again.' });
        }
        if (res.status === 404) {
          return Promise.reject({ message: 'Selected variant was not found.' });
        }

        return Promise.reject({ message: 'Add to cart failed.' });
      }

      if (!contentType.includes('application/json')) {
        return Promise.reject({ message: 'Server returned unexpected response format.' });
      }

      return res.json();
    })
    .then(function(data) {
      if (!data.success) {
        showToast(data.message || 'Add to cart failed.');
        return;
      }

      const badges = document.querySelectorAll('.cart-count');
      if (badges.length && data.itemCount != null) {
        badges.forEach(function(badge) {
          badge.innerText = data.itemCount;
          badge.style.display = data.itemCount > 0 ? 'inline-flex' : 'none';
        });
      }

      const productName = document.querySelector('.product-title')?.textContent || 'Item';
      showToast(productName + ' (Size ' + selectedVariant.size + ') added to cart - ' + data.itemCount + ' items total');

      if (addToCartBtn) {
        addToCartBtn.textContent = 'Added!';
        addToCartBtn.style.background = '#4CAF50';
        setTimeout(function() {
          addToCartBtn.textContent = originalText;
          addToCartBtn.style.background = '';
        }, 2000);
      }
    })
    .catch(function(err) {
      const message = err?.message || err?.error || 'Unknown error';
      showToast('Add to cart error: ' + message);
    })
    .finally(function() {
      if (addToCartBtn) {
        addToCartBtn.disabled = false;
        if (addToCartBtn.textContent === 'Adding...') {
          addToCartBtn.textContent = originalText;
        }
      }
    });
}
