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
                .toast .toast-text { margin: 0; padding: 0 6px; text-transform: none; color: #fff; line-height:1.2 }
                .toast .toast-close { background: transparent; border: none; color: #ccc; cursor: pointer; transition: color .2s; margin-left: 6px; font-size: 18px; }
                .toast .toast-close:hover, .toast .toast-close:focus { color: #fff; outline: none; }
            `;
            document.head.appendChild(style);
        }

        const closeBtn = toast.querySelector('#close-toast');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => { toast.classList.remove('active'); });
            closeBtn.addEventListener('keydown', (e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); toast.classList.remove('active'); } });
        }
    }
    toast.querySelector('.toast-text').textContent = message;
    toast.classList.add('active');
    setTimeout(() => {
        toast.classList.remove('active');
    }, 5000);
}

function addToCart(productId) {
  let selectedSize = null;
  let selectedColor = null;

  if (typeof SizeSelectionManager !== 'undefined' && SizeSelectionManager.getSelectedSize) {
    const sizeData = SizeSelectionManager.getSelectedSize();
    if (sizeData) {
      selectedSize = {
        size: sizeData.size,
        variantId: sizeData.variantId,
        price: sizeData.price,
        stock: sizeData.stock
      };
    }
  } else {
    // Fallback to old size selection method
    const selectedSizeElem = document.querySelector('.size-option.selected');
    if (selectedSizeElem) {
      selectedSize = {
        size: selectedSizeElem.getAttribute('data-size'),
        variantId: selectedSizeElem.getAttribute('data-variant-id'),
        price: parseFloat(selectedSizeElem.getAttribute('data-price')),
        stock: parseInt(selectedSizeElem.getAttribute('data-stock'))
      };
    }
  }

  // Get selected color
  const selectedColorElem = document.querySelector('.color-option.selected');
  if (selectedColorElem) {
    selectedColor = {
      name: selectedColorElem.getAttribute('data-color-name'),
      id: selectedColorElem.getAttribute('data-color-id')
    };
  }

  // Validation
  if (!selectedSize) {
    showToast('Please select a size first.');
    return;
  }

  if (!selectedColor) {
    showToast('Please select a color first.');
    return;
  }

  // Check stock availability
  if (selectedSize.stock <= 0) {
    showToast('Selected size is out of stock.');
    return;
  }

  // Prepare request data as JSON (matching AddToCartRequest DTO)
  const requestData = {
    quantity: 1,
    size: selectedSize.size,
    color: selectedColor.name
  };

  // Prepare headers with CSRF token - Fixed CSRF handling
  const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
  const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');

  const headers = {
    'Content-Type': 'application/json'
  };

  // Add CSRF token to headers if available
  if (csrfTokenMeta && csrfHeaderMeta) {
    const csrfToken = csrfTokenMeta.getAttribute('content');
    const csrfHeader = csrfHeaderMeta.getAttribute('content');

    if (csrfToken && csrfHeader) {
      headers[csrfHeader] = csrfToken;
      console.log('CSRF token added:', csrfHeader, '=', csrfToken);
    } else {
      console.warn('CSRF meta tags found but content is empty');
    }
  } else {
    console.warn('CSRF meta tags not found - this may cause authentication issues');
  }

  const addToCartBtn = document.querySelector('.add-to-cart');
  const originalText = addToCartBtn ? addToCartBtn.textContent : '';
  if (addToCartBtn) {
    addToCartBtn.disabled = true;
    addToCartBtn.textContent = 'Adding...';
  }

  console.log('Sending add to cart request:', {
    url: '/api/cart/add/' + productId,
    headers: headers,
    body: requestData
  });

  fetch('/api/cart/add/' + productId, {
    method: 'POST',
    headers: headers,
    body: JSON.stringify(requestData),
    credentials: 'same-origin'
  })
  .then(res => {
    console.log('Response status:', res.status, res.statusText);
    console.log('Response headers:', Object.fromEntries(res.headers.entries()));

    const contentType = res.headers.get('content-type');
    console.log('Content-Type:', contentType);

    if (!res.ok) {
      if (res.status === 403) {
        return Promise.reject({
          error: 'Access denied. Please make sure you are logged in and try again.'
        });
      } else if (res.status === 401) {
        return Promise.reject({
          error: 'Please log in to add items to cart.'
        });
      } else if (res.status === 404) {
        return Promise.reject({
          error: 'Product not found or cart service unavailable.'
        });
      }

      if (contentType && contentType.includes('application/json')) {
        return res.json().then(err => Promise.reject(err));
      } else {
        return res.text().then(html => {
          console.error('Received HTML response instead of JSON:', html.substring(0, 500) + '...');
          return Promise.reject({
            error: `Server error (${res.status}): Please check your login status and try again.`
          });
        });
      }
    }

    // Response is OK (200), but check if we're getting JSON or HTML
    if (contentType && contentType.includes('application/json')) {
      return res.json();
    } else if (contentType && (contentType.includes('text/html') || contentType.includes('text/plain'))) {
      return res.text().then(html => {
        console.error('Expected JSON but received HTML/text response:', html.substring(0, 500) + '...');
        return Promise.reject({
          error: 'Server returned unexpected response format. Please try again or contact support.'
        });
      });
    } else {
      // Unknown content type, try JSON first
      return res.json().catch(() => {
        // If JSON parsing fails, get the text content for debugging
        return res.text().then(text => {
          console.error('Failed to parse response as JSON:', text.substring(0, 500) + '...');
          return Promise.reject({
            error: 'Server returned invalid response format. Please try again.'
          });
        });
      });
    }
  })
  .then(data => {
    if (data.success) {
      // Update cart badge
      const badge = document.getElementById('cartBadge');
      if (badge && data.itemCount != null) {
        badge.innerText = data.itemCount;
        badge.style.display = data.itemCount > 0 ? 'inline' : 'none';
      }

      // Show success message with product details
      const productName = document.querySelector('.product-title')?.textContent || 'Item';
      showToast(`${productName} (Size ${selectedSize.size}) added to cart — ${data.itemCount} items total`);

      // Optional: Brief visual feedback on the button
      if (addToCartBtn) {
        addToCartBtn.textContent = 'Added!';
        addToCartBtn.style.background = '#4CAF50';
        setTimeout(() => {
          addToCartBtn.textContent = originalText;
          addToCartBtn.style.background = '';
        }, 2000);
      }
    } else {
      showToast('Add to cart failed: ' + (data.error || 'Unknown error'));
    }
  })
  .catch(err => {
    console.error('Add to cart error', err);
    let msg = 'Unknown error';

    if (err && typeof err === 'object') {
      if (err.error) {
        msg = err.error;
      } else if (err.message) {
        msg = err.message;
      }
    } else if (typeof err === 'string') {
      msg = err;
    }

    showToast('Add to cart error: ' + msg);
  })
  .finally(() => {
    // Restore button state
    if (addToCartBtn) {
      addToCartBtn.disabled = false;
      if (addToCartBtn.textContent === 'Adding...') {
        addToCartBtn.textContent = originalText;
      }
    }
  });
}
