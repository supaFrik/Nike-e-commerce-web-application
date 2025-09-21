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

// Add to cart logic - Updated to work with refactored size selection
function addToCart(productId) {
  // Get selected size from the SizeSelectionManager if it exists
  let selectedSize = null;
  let selectedColor = null;

  // Check if SizeSelectionManager exists (from refactored system)
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

  // Show loading state
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

    // Get content type to check what we're actually receiving
    const contentType = res.headers.get('content-type');
    console.log('Content-Type:', contentType);

    // Check if response is ok first
    if (!res.ok) {
      // Handle specific error status codes
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

      // Try to parse as JSON, but handle HTML error pages
      if (contentType && contentType.includes('application/json')) {
        return res.json().then(err => Promise.reject(err));
      } else {
        // It's likely an HTML error page
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
      // We got HTML instead of JSON - this indicates a server-side issue
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
