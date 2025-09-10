async function updateCart(productId, quantity, size, color) {
    if (quantity <= 0) {
        return removeCart(productId, size, color);
    }

    try {
        const response = await fetch(`/api/cart/add/${productId}?quantity=${quantity}&size=${size || ''}&color=${color || ''}`, {
            method: "POST"
        });

        const data = await response.json();

        if (data.success) {
            // cập nhật số lượng trong input
            document.getElementById(`qty-${productId}`).value = quantity;

            // cập nhật lại số lượng tổng và subtotal
            document.getElementById("itemCount").innerText = data.itemCount + " products";
            document.getElementById("subtotalAmount").innerText = formatCurrency(data.subtotal);
            document.getElementById("totalAmount").innerText = formatCurrency(data.subtotal + 5 + data.subtotal * 0.08);
        } else {
            alert(data.error || "Error updating cart");
        }
    } catch (err) {
        console.error(err);
        alert("Failed to update cart");
    }
}

async function removeCart(productId, size, color) {
    try {
        const response = await fetch(`/api/cart/remove/${productId}?size=${size || ''}&color=${color || ''}`, {
            method: "DELETE"
        });

        const data = await response.json();

        if (data.success) {
            // Xoá phần tử khỏi DOM
            document.querySelector(`[data-product-id="${productId}"]`).remove();

            // Cập nhật lại số lượng và subtotal
            document.getElementById("itemCount").innerText = data.itemCount + " products";
            document.getElementById("subtotalAmount").innerText = formatCurrency(data.subtotal);
            document.getElementById("totalAmount").innerText = formatCurrency(data.subtotal + 5 + data.subtotal * 0.08);
        } else {
            alert(data.error || "Error removing item");
        }
    } catch (err) {
        console.error(err);
        alert("Failed to remove item");
    }
}

function formatCurrency(value) {
    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD"
    }).format(value);
}
