// Size Management Module (Minimal Version)
// Provides simple quantity control: - [value] + for each size.

// Initialize sizes on page load
function initializeSizes() {
    // Ensure AppState exists and is initialized
    if (!window.AppState) {
        console.error('AppState not available in SizeManager.initialize');
        return;
    }
    
    // Initialize size data for default colors
    const colors = window.AppState.availableColors || [];
    colors.forEach(color => {
        if (!window.AppState.colorSizeData[color]) {
            window.AppState.colorSizeData[color] = ['40']; // Default size for each color
        }
        if (!window.AppState.colorSizeStockData[color]) {
            window.AppState.colorSizeStockData[color] = { '40': 0 };
        }
    });
    updateSizeOptions();
    updateStockSummary();
}

function updateStockSummary() {
    if (!window.AppState) return;
    const el = document.getElementById('stockSummary');
    if (!el) return;
    const currentColor = window.AppState.currentColor;
    let currentTotal = 0;
    let grandTotal = 0;
    if (window.AppState.colorSizeStockData) {
        Object.entries(window.AppState.colorSizeStockData).forEach(([color, stockMap]) => {
            if (!stockMap) return;
            let colorSum = 0;
            Object.values(stockMap).forEach(v => { const n = parseInt(v, 10); if (!isNaN(n)) colorSum += n; });
            if (color === currentColor) currentTotal = colorSum;
            grandTotal += colorSum;
        });
    }
    el.textContent = `Stock: ${currentColor}: ${currentTotal} | All Colors: ${grandTotal}`;
}

// Update size options display with enhanced UX
function updateSizeOptions() {
    const sizeContainer = document.getElementById('sizeOptions');
    if (!sizeContainer) return;

    sizeContainer.innerHTML = '';

    const currentSizes = getCurrentColorSizes();
    const stockMap = window.AppState.colorSizeStockData[window.AppState.currentColor] || {};

    // Create size options with enhanced layout
    currentSizes.forEach(size => {
        const stockValue = (stockMap[size] != null && !isNaN(stockMap[size])) ? stockMap[size] : 0;
        const wrapper = document.createElement('div');
        wrapper.className = 'size-option';
        wrapper.innerHTML = `
            <div class="size-line">
                <span class="size-label">${size}</span>
                <div class="qty-control" data-size="${size}">
                    <button type="button" class="qty-btn qty-minus" data-size="${size}" aria-label="Decrease ${size}">-</button>
                    <input type="number" min="0" class="qty-input" data-size="${size}" value="${stockValue}" aria-label="Stock for size ${size}" />
                    <button type="button" class="qty-btn qty-plus" data-size="${size}" aria-label="Increase ${size}">+</button>
                </div>
                <button type="button" class="remove-size" onclick="SizeManager.remove('${size}')" title="Remove size ${size}">×</button>
            </div>`;
        sizeContainer.appendChild(wrapper);
    });

    attachQuantityEvents();
}

// Attach quantity adjustment events
function attachQuantityEvents() {
    // Plus buttons
    // Plus buttons
    document.querySelectorAll('.qty-plus').forEach(btn => {
        btn.onclick = () => adjustQty(btn.dataset.size, 1);
    });
    // Minus buttons
    document.querySelectorAll('.qty-minus').forEach(btn => {
        btn.onclick = () => adjustQty(btn.dataset.size, -1);
    });
    // Direct input
    document.querySelectorAll('.qty-input').forEach(inp => {
        inp.oninput = () => {
            let v = parseInt(inp.value, 10);
            if (isNaN(v) || v < 0) v = 0;
            if (!window.AppState.colorSizeStockData[window.AppState.currentColor]) {
                window.AppState.colorSizeStockData[window.AppState.currentColor] = {};
            }
            window.AppState.colorSizeStockData[window.AppState.currentColor][inp.dataset.size] = v;
            inp.value = v;
        };
    });
}

// Adjust quantity for a size
function adjustQty(size, delta) {
    if (!window.AppState.colorSizeStockData[window.AppState.currentColor]) {
        window.AppState.colorSizeStockData[window.AppState.currentColor] = {};
    }
    const stockMap = window.AppState.colorSizeStockData[window.AppState.currentColor];
    let current = stockMap[size] || 0;
    current += delta;
    if (current < 0) current = 0;
    stockMap[size] = current;
    const input = document.querySelector(`.qty-input[data-size="${size}"]`);
    if (input) input.value = current;
    updateStockSummary();
}

// Get current color sizes
function getCurrentColorSizes() {
    return window.AppState.colorSizeData[window.AppState.currentColor] || [];
}

// Set current color sizes
function setCurrentColorSizes(sizes) {
    window.AppState.colorSizeData[window.AppState.currentColor] = sizes;
}

// Add new size with enhanced validation
function addNewSize() {
    const input = document.getElementById('newSizeInput');
    if (!input) return;

    const newSize = input.value.trim().toUpperCase();

    if (!newSize) {
        window.Toast.show('Please enter a size');
        return;
    }

    if (newSize.length > 4) {
        window.Toast.show('Size must be 4 characters or less');
        return;
    }

    // Enhanced validation
    if (!/^[A-Z0-9.\/\-]+$/.test(newSize)) {
        window.Toast.show('Size can only contain letters, numbers, dots, slashes, and hyphens');
        return;
    }

    const currentSizes = getCurrentColorSizes();
    if (currentSizes.includes(newSize)) {
        window.Toast.show(`Size ${newSize} already exists for ${window.AppState.currentColor}`);
        return;
    }

    const updatedSizes = [...currentSizes, newSize];
    setCurrentColorSizes(updatedSizes);

    if (!window.AppState.colorSizeStockData[window.AppState.currentColor]) {
        window.AppState.colorSizeStockData[window.AppState.currentColor] = {};
    }
    window.AppState.colorSizeStockData[window.AppState.currentColor][newSize] = 0;

    updateSizeOptions();
    updateStockSummary();
    input.value = '';

    if (window.Toast) {
        window.Toast.show(`Added size ${newSize}`);
    }
}

// Remove size with confirmation
function removeSize(size) {
    const sizes = getCurrentColorSizes();
    if (sizes.length <= 1) {
        window.Toast.show(`You must have at least one size for ${window.AppState.currentColor}`);
        return;
    }

    const stockMap = window.AppState.colorSizeStockData[window.AppState.currentColor] || {};
    const stock = stockMap[size] || 0;

    // Show confirmation if size has stock
    if (stock > 0) {
        if (!confirm(`Size ${size} has ${stock} stock. Are you sure you want to remove it?`)) {
            return;
        }
    }

    const updatedSizes = sizes.filter(s => s !== size);
    setCurrentColorSizes(updatedSizes);

    if (window.AppState.colorSizeStockData[window.AppState.currentColor]) {
        delete window.AppState.colorSizeStockData[window.AppState.currentColor][size];
    }

    updateSizeOptions();
    updateStockSummary();

    if (window.Toast) {
        window.Toast.show(`Removed size ${size}`);
    }
}

// Toggle size selection (for future multi-selection feature)
function toggleSize(size) {


}

// Initialize size event listeners
function initializeSizeListeners() {
    // Add size button event listener
    const addSizeBtn = document.getElementById('addSizeBtn');
    if (addSizeBtn) {
        addSizeBtn.addEventListener('click', addNewSize);
    }

    // Add size input enter key listener with enhanced UX
    const newSizeInput = document.getElementById('newSizeInput');
    if (newSizeInput) {
        newSizeInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                addNewSize();
            }
        });
    }
}

// Export for global access
window.SizeManager = {
    initialize: initializeSizes,
    initializeListeners: initializeSizeListeners,
    updateOptions: updateSizeOptions,
    updateStockSummary: updateStockSummary,
    getCurrentSizes: getCurrentColorSizes,
    setCurrentSizes: setCurrentColorSizes,
    add: addNewSize,
    remove: removeSize,
    toggle: toggleSize
};