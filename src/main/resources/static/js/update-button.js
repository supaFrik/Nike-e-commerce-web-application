const cartUpdateInFlight = new Set();

async function updateCart(productId, quantity, size, color) {
    if (quantity <= 0) quantity = 1;
    if (cartUpdateInFlight.has(productId)) return;
    cartUpdateInFlight.add(productId);
    toggleRowLoading(productId, true);
    try {
        const response = await fetch(`/api/cart/update/${productId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ quantity, size, color })
        });
        const data = await response.json();
        if (data.success) {
            const qtyInput = document.getElementById(`qty-${productId}`);
            if (qtyInput) qtyInput.value = quantity;
            applyCartSummary(data);
        } else {
            alert(data.error || 'Error updating cart');
        }
    } catch (e) {
        console.error(e);
        alert('Failed to update cart');
    } finally {
        cartUpdateInFlight.delete(productId);
        toggleRowLoading(productId, false);
    }
}

async function removeCart(productId, size, color) {
    if (cartUpdateInFlight.has(productId)) return; // prevent overlap
    cartUpdateInFlight.add(productId);
    toggleRowLoading(productId, true);
    try {
        const response = await fetch(`/api/cart/remove/${productId}?size=${size || ''}&color=${color || ''}`, { method: 'DELETE' });
        const data = await response.json();
        if (data.success) {
            const itemEl = document.querySelector(`[data-product-id="${productId}"]`);
            if (itemEl) itemEl.remove();
            applyCartSummary(data);
            if (data.itemCount === 0) showEmptyCartState();
        } else {
            alert(data.error || 'Error removing item');
        }
    } catch (e) {
        console.error(e);
        alert('Failed to remove item');
    } finally {
        cartUpdateInFlight.delete(productId);
        toggleRowLoading(productId, false);
    }
}

function formatCurrency(value) {
    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD"
    }).format(value);
}

function updateItemCountDisplay(count) {
    const text = count + " product" + (count === 1 ? "" : "s");
    const inner = document.getElementById("cartTotalCount");
    if (inner) inner.textContent = text;
    else {
        const outer = document.getElementById("itemCount");
        if (outer) outer.textContent = text;
    }
}

function applyCartSummary(data) {
    if (typeof data.itemCount === 'number') updateItemCountDisplay(data.itemCount);
    if (typeof data.subtotal === 'number') {
        const subtotalEl = document.getElementById('subtotalAmount');
        if (subtotalEl) subtotalEl.innerText = formatCurrency(data.subtotal);
    }
    if (typeof data.shipping === 'number') {
        const shippingEl = document.getElementById('shippingAmount');
        if (shippingEl) shippingEl.innerText = formatCurrency(data.shipping);
    }
    if (typeof data.tax === 'number') {
        const taxEl = document.getElementById('taxAmount');
        if (taxEl) taxEl.innerText = formatCurrency(data.tax);
    }
    if (typeof data.discount === 'number') {
        const discountRow = document.getElementById('discountRow');
        const discountEl = document.getElementById('discountAmount');
        if (discountEl) discountEl.innerText = formatCurrency(data.discount);
        if (discountRow) discountRow.style.display = data.discount > 0 ? 'flex' : 'none';
    }
    if (typeof data.total === 'number') {
        const totalEl = document.getElementById('totalAmount');
        if (totalEl) totalEl.innerText = formatCurrency(data.total);
    }
    const checkoutBtn = document.getElementById('checkoutBtn');
    if (checkoutBtn) checkoutBtn.disabled = (data.itemCount || 0) === 0;
}

function showEmptyCartState() {
    const cartLayout = document.getElementById('cartLayout');
    if (cartLayout) cartLayout.style.display = 'none';

    let emptyDiv = document.getElementById('emptyCart');
    if (!emptyDiv) {
        emptyDiv = document.createElement('div');
        emptyDiv.id = 'emptyCart';
        emptyDiv.className = 'empty-cart';
        emptyDiv.innerHTML = `
            <div class="empty-cart-icon"><i class="fas fa-shopping-bag"></i></div>
            <h2>Your bag is empty</h2>
            <p>Once you add something to your bag - it will appear here. Ready to get started?</p>
            <a href="/products" class="btn btn-primary">Get Started</a>
        `;
        const cartContent = document.getElementById('cartContent');
        if (cartContent) cartContent.prepend(emptyDiv);
    } else {
        emptyDiv.style.display = 'block';
    }
}

function changeQuantity(productId, delta, size, color) {
    if (cartUpdateInFlight.has(productId)) return;
    const qtyInput = document.getElementById(`qty-${productId}`);
    let current = parseInt(qtyInput && qtyInput.value, 10);
    if (isNaN(current) || current < 1) current = 1;
    let next = current + delta;
    if (next < 1) next = 1; // prevent dropping to zero via +/-
    if (next === current) return;
    updateCart(productId, next, size, color);
}

function toggleRowLoading(productId, isLoading) {
    const row = document.querySelector(`[data-product-id="${productId}"]`);
    if (!row) return;
    const buttons = row.querySelectorAll('.quantity-btn');
    buttons.forEach(btn => btn.disabled = isLoading);
    const removeBtn = row.querySelector('.remove-btn');
    if (removeBtn) removeBtn.style.pointerEvents = isLoading ? 'none' : 'auto';
    row.classList.toggle('updating', isLoading);
}
