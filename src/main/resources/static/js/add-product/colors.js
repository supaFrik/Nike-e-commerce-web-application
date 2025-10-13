// Color Management Module
// Handles color selection and management functionality

// Initialize colors on page load
function initializeColors() {
    // Ensure AppState exists and is initialized
    if (!window.AppState) {
        console.error('AppState not available in ColorManager.initialize');
        return;
    }
    
    // Initialize color data for default colors
    const colors = window.AppState.availableColors || [];
    colors.forEach(color => {
        if (!window.AppState.colorImageData[color]) {
            window.AppState.colorImageData[color] = [];
        }
        if (!window.AppState.colorSizeData[color]) {
            window.AppState.colorSizeData[color] = ['40']; // Default size for each color
        }
    });
    updateColorOptions();
}

// Update color options display
function updateColorOptions() {
    const colorContainer = document.getElementById('colorOptions');
    if (!colorContainer) return;
    
    // Check if AppState is available
    if (!window.AppState || !window.AppState.availableColors) {
        console.error('AppState or availableColors not available in updateColorOptions');
        return;
    }
    
    colorContainer.innerHTML = '';

    window.AppState.availableColors.forEach(color => {
        const colorDiv = document.createElement('div');
        colorDiv.className = `color-option ${color === window.AppState.currentColor ? 'active' : ''} removable`;
        
        // Get image count for this color
        const colorImages = window.AppState.colorImageData[color] || [];
        const imageCount = colorImages.length;
        
        colorDiv.innerHTML = `
            ${color} <span class="image-count">(${imageCount})</span>
            <span class="remove-color" onclick="ColorManager.remove('${color}')" title="Remove ${color}">×</span>
        `;
        colorDiv.onclick = (e) => {
            if (!e.target.classList.contains('remove-color')) {
                switchToColor(color);
            }
        };
        colorContainer.appendChild(colorDiv);
    });

    updateColorInfo();
}

// Update color information in UI
function updateColorInfo() {
    // Update color info (with null checks)
    const currentColorNameEl = document.getElementById('currentColorName');
    if (currentColorNameEl) {
        currentColorNameEl.textContent = window.AppState.currentColor;
    }
    
    // Update size section labels (with null checks)
    const sizeColorNameEl = document.getElementById('sizeColorName');
    if (sizeColorNameEl) {
        sizeColorNameEl.textContent = window.AppState.currentColor;
    }
    
    const addSizeColorNameEl = document.getElementById('addSizeColorName');
    if (addSizeColorNameEl) {
        addSizeColorNameEl.textContent = window.AppState.currentColor;
    }
    
    // Update image counter for current color
    const currentImages = window.AppState.colorImageData[window.AppState.currentColor] || [];
    const imageCounterEl = document.getElementById('imageCounter');
    if (imageCounterEl) {
        imageCounterEl.textContent = `${currentImages.length} / ${window.AppState.maxImages} images uploaded for ${window.AppState.currentColor}`;
    }
}

// Switch to a different color
function switchToColor(color) {
    window.AppState.setCurrentColor(color);
    window.AppState.setCurrentImageIndex(0);
    updateColorOptions();
    
    // Update sizes and images when switching colors
    if (window.SizeManager) {
        window.SizeManager.updateOptions();
        window.SizeManager.updateStockSummary(); // Ensure stock summary updates for new color
    }
    if (window.ImageManager) {
        window.ImageManager.updateDisplay();
    }

    // Add visual feedback for color switching
    if (window.Toast) {
        window.Toast.show(`Switched to ${color}`);
    }
}

// Add new color
function addNewColor() {
    const input = document.getElementById('newColorInput');
    if (!input) return;
    
    const newColor = input.value.trim();
    
    if (!newColor) {
        window.Toast.show('Please enter a color name');
        return;
    }

    if (newColor.length > 20) {
        window.Toast.show('Color name must be 20 characters or less');
        return;
    }

    // Check for duplicate (case-insensitive)
    if (window.AppState.availableColors.some(color => color.toLowerCase() === newColor.toLowerCase())) {
        window.Toast.show('This color already exists');
        return;
    }

    window.AppState.availableColors.push(newColor);
    window.AppState.colorImageData[newColor] = []; // Initialize empty image array for new color
    window.AppState.colorSizeData[newColor] = ['40']; // Initialize with default size for new color
    // defaultImageData[newColor] will be set when first image is uploaded
    updateColorOptions();
    input.value = '';
    
    // Automatically switch to the new color
    switchToColor(newColor);
}

// Remove color
function removeColor(color) {
    if (window.AppState.availableColors.length <= 1) {
        window.Toast.show('You must have at least one color');
        return;
    }
    
    // Confirm deletion if color has images
    const colorImages = window.AppState.colorImageData[color] || [];
    if (colorImages.length > 0) {
        if (!confirm(`This color has ${colorImages.length} images. Are you sure you want to delete it?`)) {
            return;
        }
    }
    
    window.AppState.availableColors = window.AppState.availableColors.filter(c => c !== color);
    delete window.AppState.colorImageData[color];
    delete window.AppState.colorSizeData[color]; // Also remove size data
    delete window.AppState.defaultImageData[color]; // Also remove default image data
    
    // Switch to first available color if current color was deleted
    if (window.AppState.currentColor === color) {
        window.AppState.setCurrentColor(window.AppState.availableColors[0]);
        window.AppState.setCurrentImageIndex(0);
    }
    
    updateColorOptions();
    
    // Update sizes and images after color removal
    if (window.SizeManager) {
        window.SizeManager.updateOptions();
        if (window.SizeManager.updateStockSummary) window.SizeManager.updateStockSummary();
    }
    if (window.ImageManager) {
        window.ImageManager.updateDisplay();
    }
}

// Get current color images
function getCurrentColorImages() {
    return window.AppState.colorImageData[window.AppState.currentColor] || [];
}

// Initialize color event listeners
function initializeColorListeners() {
    // Add color button event listener
    const addColorBtn = document.getElementById('addColorBtn');
    if (addColorBtn) {
        addColorBtn.addEventListener('click', addNewColor);
    }
    
    // Add color input enter key listener
    const newColorInput = document.getElementById('newColorInput');
    if (newColorInput) {
        newColorInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                addNewColor();
            }
        });
    }
}

// Export for global access
window.ColorManager = {
    initialize: initializeColors,
    initializeListeners: initializeColorListeners,
    updateOptions: updateColorOptions,
    updateInfo: updateColorInfo,
    switchTo: switchToColor,
    add: addNewColor,
    remove: removeColor,
    getCurrentImages: getCurrentColorImages
};