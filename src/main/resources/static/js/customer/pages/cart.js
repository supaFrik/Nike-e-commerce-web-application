const cartUpdateInFlight = new Set();
let isApplyingCoupon = false;
let appliedCouponCode = null;

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
    }
  }

  toast.querySelector('.toast-text').textContent = message;
  toast.classList.add('active');
  setTimeout(function() {
    toast.classList.remove('active');
  }, 4000);
}

function cartHeaders() {
  const headers = {
    Accept: "application/json",
    "Content-Type": "application/json"
  };

  const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute("content");
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute("content");

  if (csrfToken && csrfHeader) {
    headers[csrfHeader] = csrfToken;
  }

  return headers;
}

function cartPageEnv() {
  return (window.APP_CTX || "").replace(/\/$/, "");
}

function resolveCartImageUrl(imageUrl) {
  if (!imageUrl) {
    return "";
  }

  const normalized = String(imageUrl).trim();
  if (!normalized) {
    return "";
  }

  if (/^\/https?:\/\//i.test(normalized)) {
    return normalized.slice(1);
  }

  if (/^https?:\/\//i.test(normalized)) {
    return normalized;
  }

  return normalized.startsWith("/") ? `${cartPageEnv()}${normalized}` : `${cartPageEnv()}/${normalized}`;
}

function formatCurrency(value) {
  const n = Math.round(Number(value || 0));
  return n.toLocaleString('en-US') + '₫';
}

function escapeHtml(value) {
  return String(value ?? "")
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

function updateItemCountDisplay(count) {
  const countEl = document.getElementById("cartTotalCount");
  if (countEl) {
    countEl.textContent = `${count} sản phẩm`;
  }
}

function syncCartBadge(count) {
  document.querySelectorAll(".cart-count").forEach(function (badge) {
    badge.textContent = count;
    badge.style.display = count > 0 ? "inline-flex" : "none";
  });
}

function applyCartSummary(summary) {
  updateItemCountDisplay(summary.itemCount || 0);
  syncCartBadge(summary.itemCount || 0);

  const subtotalEl = document.getElementById("subtotalAmount");
  if (subtotalEl) {
    subtotalEl.textContent = formatCurrency(summary.subtotal);
  }

  const discountEl = document.getElementById("discountAmount");
  if (discountEl) {
    discountEl.textContent = formatCurrency(summary.discount);
  }

  const discountRow = document.getElementById("discountRow");
  if (discountRow) {
    discountRow.style.display = Number(summary.discount || 0) > 0 ? "flex" : "none";
  }

  const totalEl = document.getElementById("totalAmount");
  if (totalEl) {
    totalEl.textContent = formatCurrency(summary.total);
  }

  const checkoutBtn = document.getElementById("checkoutBtn");
  if (checkoutBtn) {
    checkoutBtn.disabled = (summary.itemCount || 0) === 0;
  }
}

function renderEmptyState() {
  const emptyCart = document.getElementById("emptyCart");
  const cartLayout = document.getElementById("cartLayout");

  if (emptyCart) {
    emptyCart.style.display = "block";
  }
  if (cartLayout) {
    cartLayout.style.display = "none";
  }
}

function renderFilledState() {
  const emptyCart = document.getElementById("emptyCart");
  const cartLayout = document.getElementById("cartLayout");

  if (emptyCart) {
    emptyCart.style.display = "none";
  }
  if (cartLayout) {
    cartLayout.style.display = "grid";
  }
}

function renderCartItems(items) {
  const list = document.getElementById("cartItemsList");
  if (!list) {
    return;
  }

  if (!items || items.length === 0) {
    list.innerHTML = "";
    renderEmptyState();
    return;
  }

  renderFilledState();

  list.innerHTML = items.map(function (item) {
    const imageUrl = resolveCartImageUrl(item.imageUrl);
    const minusDisabled = item.quantity <= 1 ? "disabled" : "";
    const plusDisabled = !item.active || item.quantity >= (item.stock || 0) ? "disabled" : "";
    const stockText = item.active ? `Tồn kho: ${item.stock ?? 0}` : "Biến thể không còn hoạt động";

    return `
      <div class="cart-item" data-cart-item-id="${item.cartItemId}">
        <div class="item-image" data-view-product="${item.productId}">
          <img src="${escapeHtml(imageUrl)}" alt="${escapeHtml(item.productName)}">
        </div>
        <div class="item-details">
          <div class="item-header">
            <div class="item-info">
              <h3>${escapeHtml(item.productName)}</h3>
              <p class="item-category">${escapeHtml(item.categoryName)}</p>
              <p class="item-size">Kích thước: ${escapeHtml(item.size)}</p>
              <p class="item-color">Màu: ${escapeHtml(item.colorName)}</p>
              <p class="item-color">Đơn giá: ${formatCurrency(item.unitPrice)}</p>
              <p class="item-color">${escapeHtml(stockText)}</p>
            </div>
            <div class="item-price">${formatCurrency(item.lineTotal)}</div>
          </div>
          <div class="item-actions">
            <div class="quantity-controls">
              <button type="button" class="quantity-btn" data-change-quantity="${item.cartItemId}" data-quantity-delta="-1" ${minusDisabled}>
                <i class="fas fa-minus"></i>
              </button>
              <input type="text" class="quantity-input" value="${item.quantity}" id="qty-${item.cartItemId}" readonly>
              <button type="button" class="quantity-btn" data-change-quantity="${item.cartItemId}" data-quantity-delta="1" ${plusDisabled}>
                <i class="fas fa-plus"></i>
              </button>
            </div>
            <button type="button" class="remove-btn" title="Xóa sản phẩm" data-remove-cart-item="${item.cartItemId}">
              <i class="fas fa-trash-alt"></i>
            </button>
          </div>
        </div>
      </div>
    `;
  }).join("");
}

function applyCartState(summary) {
  renderCartItems(summary.items || []);
  applyCartSummary(summary);
}

async function updateCartItem(cartItemId, quantity) {
  if (cartUpdateInFlight.has(cartItemId)) {
    return;
  }

  cartUpdateInFlight.add(cartItemId);
  toggleRowLoading(cartItemId, true);

  try {
    const response = await fetch(`/api/cart/items/${cartItemId}`, {
      method: "PATCH",
      headers: cartHeaders(),
      body: JSON.stringify({ quantity }),
      credentials: "same-origin"
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || "Failed to update cart");
    }

    applyCartState(data);
  } catch (error) {
    showToast(error.message || "Failed to update cart");
  } finally {
    cartUpdateInFlight.delete(cartItemId);
    toggleRowLoading(cartItemId, false);
  }
}

async function removeCartItem(cartItemId) {
  if (cartUpdateInFlight.has(cartItemId)) {
    return;
  }

  cartUpdateInFlight.add(cartItemId);
  toggleRowLoading(cartItemId, true);

  try {
    const response = await fetch(`/api/cart/items/${cartItemId}`, {
      method: "DELETE",
      headers: cartHeaders(),
      credentials: "same-origin"
    });

    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || "Failed to remove item");
    }

    applyCartState(data);
  } catch (error) {
    showToast(error.message || "Failed to remove item");
  } finally {
    cartUpdateInFlight.delete(cartItemId);
    toggleRowLoading(cartItemId, false);
  }
}

function changeQuantity(cartItemId, delta) {
  const qtyInput = document.getElementById(`qty-${cartItemId}`);
  let current = parseInt(qtyInput?.value, 10);

  if (Number.isNaN(current) || current < 1) {
    current = 1;
  }

  const next = current + delta;
  if (next < 1 || next === current) {
    return;
  }

  updateCartItem(cartItemId, next);
}

function toggleRowLoading(cartItemId, isLoading) {
  const row = document.querySelector(`[data-cart-item-id="${cartItemId}"]`);
  if (!row) {
    return;
  }

  row.classList.toggle("updating", isLoading);
  row.querySelectorAll(".quantity-btn").forEach(function (button) {
    if (isLoading) {
      button.dataset.wasDisabled = button.disabled ? "true" : "false";
      button.disabled = true;
      return;
    }

    button.disabled = button.dataset.wasDisabled === "true";
    delete button.dataset.wasDisabled;
  });

  const removeBtn = row.querySelector(".remove-btn");
  if (removeBtn) {
    removeBtn.style.pointerEvents = isLoading ? "none" : "auto";
  }
}

function viewProduct(productId) {
  window.location.href = `${cartPageEnv()}/product-detail?id=${productId}`;
}

async function applyCouponCode() {
  const promoInput = document.getElementById("promoCode");
  const code = promoInput ? promoInput.value.trim().toUpperCase() : "";

  if (!code) {
    showToast("Vui lòng nhập mã giảm giá");
    return;
  }

  if (isApplyingCoupon) {
    showToast("Hệ thống đang xử lý, vui lòng chờ trong giây lát...");
    return;
  }

  if (appliedCouponCode === code) {
    showToast(`Mã ${code} đã được sử dụng`);
    return;
  }

  isApplyingCoupon = true;
  const applyBtn = document.getElementById("applyPromoBtn");
  if (applyBtn) {
    applyBtn.disabled = true;
    applyBtn.textContent = "Đang xử lý...";
  }

  try {
    const subtotalText = document.getElementById("subtotalAmount")?.textContent || "0";
    const orderAmount = parseFloat(subtotalText.replace(/[^0-9]/g, "")) || 0;

    const response = await fetch("/api/coupons/apply", {
      method: "POST",
      headers: cartHeaders(),
      body: JSON.stringify({ code, orderAmount })
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.message || "Mã giảm giá không hợp lệ");
    }

    appliedCouponCode = code;
    sessionStorage.setItem('appliedCouponCode', code);

    const discountEl = document.getElementById("discountAmount");
    if (discountEl) discountEl.textContent = formatCurrency(data.discountAmount);

    const discountRow = document.getElementById("discountRow");
    if (discountRow) discountRow.style.display = "flex";

    const totalEl = document.getElementById("totalAmount");
    if (totalEl) totalEl.textContent = formatCurrency(data.finalAmount);

    showToast("Áp dụng mã giảm giá thành công!");

  } catch (error) {
    showToast(error.message || "Không thể áp dụng mã giảm giá");
  } finally {
    isApplyingCoupon = false;
    if (applyBtn) {
      applyBtn.disabled = false;
      applyBtn.textContent = "Áp dụng";
    }
  }
}

function bindCartActions() {
  document.addEventListener("click", function (event) {
    const productLink = event.target.closest("[data-view-product]");
    if (productLink) {
      viewProduct(productLink.dataset.viewProduct);
      return;
    }

    const quantityButton = event.target.closest("[data-change-quantity]");
    if (quantityButton) {
      changeQuantity(Number(quantityButton.dataset.changeQuantity), Number(quantityButton.dataset.quantityDelta));
      return;
    }

    const removeButton = event.target.closest("[data-remove-cart-item]");
    if (removeButton) {
      removeCartItem(Number(removeButton.dataset.removeCartItem));
      return;
    }

    if (event.target.closest("#applyPromoBtn")) {
      applyCouponCode();
    }
  });
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", bindCartActions);
} else {
  bindCartActions();
}
