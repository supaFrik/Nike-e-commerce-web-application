/**
 * Product Add Page - JavaScript
 * Handles color variants, sizes, images, and inventory management
 */

// Variants Management - Global State
let colorVariants = [];
let sizeVariants = []; // Global sizes still available
let colorSizes = {}; // Store sizes per color: { colorName: [sizes] }
let productImagesData = {}; // Store images by color: { colorName: [images] }
let variantStockData = {}; // Store stock by variant: { "color-size": quantity }
let imageIdCounter = 0;

// DOM Elements - Cache references
let colorInput, addColorBtn, colorsList, noColorsForSize, sizesInputSection;
let colorForSize, sizeInput, addSizeBtn, sizesByColorContainer, noColorsMessage;
let imageUploadSection, selectColorForUpload, imageUploadArea, productImagesInput;
let imagesByColorContainer, noVariantsMessage, inventorySection, variantStockContainer;

/**
 * Initialize DOM elements and event listeners
 */
function initializeProductAdd() {
  // Get DOM element references
  colorInput = document.getElementById('colorInput');
  addColorBtn = document.getElementById('addColorBtn');
  colorsList = document.getElementById('colorsList');
  noColorsForSize = document.getElementById('noColorsForSize');
  sizesInputSection = document.getElementById('sizesInputSection');
  colorForSize = document.getElementById('colorForSize');
  sizeInput = document.getElementById('sizeInput');
  addSizeBtn = document.getElementById('addSizeBtn');
  sizesByColorContainer = document.getElementById('sizesByColorContainer');
  noColorsMessage = document.getElementById('noColorsMessage');
  imageUploadSection = document.getElementById('imageUploadSection');
  selectColorForUpload = document.getElementById('selectColorForUpload');
  imageUploadArea = document.getElementById('imageUploadArea');
  productImagesInput = document.getElementById('productImages');
  imagesByColorContainer = document.getElementById('imagesByColorContainer');
  noVariantsMessage = document.getElementById('noVariantsMessage');
  inventorySection = document.getElementById('inventorySection');
  variantStockContainer = document.getElementById('variantStockContainer');

  if (imageUploadArea) {
    imageUploadArea.classList.add('disabled');
  }

  // Set up event listeners
  setupEventListeners();

  console.log('Product add page initialized');
}

/**
 * Set up all event listeners
 */
function setupEventListeners() {
  // Color variant events
  if (addColorBtn) {
    addColorBtn.addEventListener('click', handleAddColor);
  }

  if (colorInput) {
    colorInput.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        e.preventDefault();
        addColorBtn.click();
      }
    });
  }

  // Size variant events
  if (colorForSize) {
    colorForSize.addEventListener('change', handleColorForSizeChange);
  }

  if (addSizeBtn) {
    addSizeBtn.addEventListener('click', handleAddSize);
  }

  if (sizeInput) {
    sizeInput.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        e.preventDefault();
        addSizeBtn.click();
      }
    });
  }

  // Image upload events
  if (selectColorForUpload) {
    selectColorForUpload.addEventListener('change', handleColorForUploadChange);
  }

  if (imageUploadArea) {
    imageUploadArea.addEventListener('click', () => {
      console.log('Upload area clicked. Input disabled?', productImagesInput?.disabled);
      if (productImagesInput && !productImagesInput.disabled) {
        productImagesInput.click();
        console.log('File input clicked');
      } else {
        console.warn('Image input is disabled. Please select a color first.');
        if (!selectColorForUpload.value) {
          alert('Please select a color first before uploading images');
        }
      }
    });
  }

  if (productImagesInput) {
    productImagesInput.addEventListener('change', handleImageUpload);
    console.log('Image upload handler attached');
  }

  // Save product button
  const saveProductBtn = document.getElementById('saveProductBtn');
  if (saveProductBtn) {
    saveProductBtn.addEventListener('click', handleSaveProduct);
  }
}

/**
 * Add Color Variant
 */
function handleAddColor() {
  const color = colorInput.value.trim();
  if (color && !colorVariants.includes(color)) {
    colorVariants.push(color);
    productImagesData[color] = [];
    colorSizes[color] = [];
    renderColors();
    updateColorSelector();
    updateColorSizeSelector();
    updateInventorySection();
    colorInput.value = '';

    // Show size input section when first color is added
    if (colorVariants.length === 1) {
      noColorsForSize.classList.add('hidden');
      sizesInputSection.classList.remove('hidden');
    }

    // Show image upload section when first color is added
    if (colorVariants.length === 1) {
      noColorsMessage.classList.add('hidden');
      imageUploadSection.classList.remove('hidden');
    }
  }
}

/**
 * Handle color selection for size input
 */
function handleColorForSizeChange(e) {
  if (e.target.value) {
    sizeInput.disabled = false;
    addSizeBtn.disabled = false;
  } else {
    sizeInput.disabled = true;
    addSizeBtn.disabled = true;
  }
}

/**
 * Add Size Variant
 */
function handleAddSize() {
  const selectedColor = colorForSize.value;
  const size = sizeInput.value.trim();

  if (!selectedColor) {
    alert('Please select a color first');
    return;
  }

  if (size && !colorSizes[selectedColor].includes(size)) {
    colorSizes[selectedColor].push(size);
    // Sort sizes numerically
    colorSizes[selectedColor].sort((a, b) => parseFloat(a) - parseFloat(b));
    renderSizesByColor();
    updateInventorySection();
    sizeInput.value = '';
  }
}

/**
 * Remove Color
 */
function removeColor(color) {
  colorVariants = colorVariants.filter(c => c !== color);
  delete productImagesData[color];
  delete colorSizes[color];

  // Remove stock data for this color
  Object.keys(variantStockData).forEach(key => {
    if (key.startsWith(color + '-')) {
      delete variantStockData[key];
    }
  });

  renderColors();
  updateColorSelector();
  updateColorSizeSelector();
  renderImagesByColor();
  updateInventorySection();

  // Hide sections if no colors left
  if (colorVariants.length === 0) {
    noColorsForSize.classList.remove('hidden');
    sizesInputSection.classList.add('hidden');
    noColorsMessage.classList.remove('hidden');
    imageUploadSection.classList.add('hidden');
    selectColorForUpload.value = '';
    productImagesInput.disabled = true;
  }
}

/**
 * Remove Size from specific color
 */
function removeSize(color, size) {
  colorSizes[color] = colorSizes[color].filter(s => s !== size);

  // Remove stock data for this color-size
  const variantKey = `${color}-${size}`;
  delete variantStockData[variantKey];

  renderSizesByColor();
  updateInventorySection();
}

/**
 * Render Colors
 */
function renderColors() {
  colorsList.innerHTML = colorVariants.map(color => `
    <div class="variant-tag">
      <span>${color}</span>
      <button type="button" onclick="removeColor('${color}')" class="variant-remove">
        <i class="ti ti-x"></i>
      </button>
    </div>
  `).join('');
}

/**
 * Render Sizes by Color
 */
function renderSizesByColor() {
  let html = '';
  colorVariants.forEach(color => {
    const sizes = colorSizes[color] || [];
    if (sizes.length > 0) {
      html += `
        <div class="color-sizes-group">
          <h5 class="text-xs font-semibold text-gray-700 mb-2">${color}</h5>
          <div class="flex flex-wrap gap-2">
      `;
      sizes.forEach(size => {
        html += `
          <div class="variant-tag">
            <span>Size ${size}</span>
            <button type="button" onclick="removeSize('${color}', '${size}')" class="variant-remove">
              <i class="ti ti-x"></i>
            </button>
          </div>
        `;
      });
      html += `
          </div>
        </div>
      `;
    }
  });
  sizesByColorContainer.innerHTML = html || '<p class="text-center text-gray-400 text-sm py-2">No sizes added yet</p>';
}

/**
 * Update Color Selector for Sizes
 */
function updateColorSizeSelector() {
  colorForSize.innerHTML = '<option value="">Select color</option>' +
    colorVariants.map(color => `<option value="${color}">${color}</option>`).join('');
}

/**
 * Update Inventory Section
 */
function updateInventorySection() {
  // Check if any color has at least one size
  let hasSizes = colorVariants.some(color => colorSizes[color] && colorSizes[color].length > 0);

  if (colorVariants.length === 0 || !hasSizes) {
    noVariantsMessage.classList.remove('hidden');
    inventorySection.classList.add('hidden');
    return;
  }

  noVariantsMessage.classList.add('hidden');
  inventorySection.classList.remove('hidden');

  let html = '';
  colorVariants.forEach(color => {
    const sizes = colorSizes[color] || [];
    if (sizes.length > 0) {
      html += `
        <div class="variant-stock-group">
          <h4 class="text-sm font-semibold text-gray-800 mb-3">${color}</h4>
          <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-3">
      `;

      sizes.forEach(size => {
        const variantKey = `${color}-${size}`;
        const currentStock = variantStockData[variantKey] || 0;

        html += `
          <div class="variant-stock-item">
            <label class="block text-xs font-medium text-gray-600 mb-1">Size ${size}</label>
            <input
              type="number"
              class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:border-blue-500"
              placeholder="0"
              min="0"
              value="${currentStock}"
              onchange="updateVariantStock('${variantKey}', this.value)"
            />
          </div>
        `;
      });

      html += `
          </div>
        </div>
      `;
    }
  });

  variantStockContainer.innerHTML = html;
}

/**
 * Update Variant Stock
 */
function updateVariantStock(variantKey, quantity) {
  variantStockData[variantKey] = parseInt(quantity) || 0;
}

/**
 * Update Color Selector for image upload
 */
function updateColorSelector() {
  selectColorForUpload.innerHTML = '<option value="">Choose a color</option>' +
    colorVariants.map(color => `<option value="${color}">${color}</option>`).join('');
}

/**
 * Handle color selection for image upload
 */
function handleColorForUploadChange(e) {
  console.log('Color selected for upload:', e.target.value);
  if (e.target.value) {
    productImagesInput.disabled = false;
    imageUploadArea.classList.remove('disabled');
    imageUploadArea.classList.add('ready');
    console.log('Image input enabled');
  } else {
    productImagesInput.disabled = true;
    imageUploadArea.classList.remove('ready');
    imageUploadArea.classList.add('disabled');
    console.log('Image input disabled');
  }
}

/**
 * Image Upload Handler
 */
function handleImageUpload(e) {
  const selectedColor = selectColorForUpload.value;
  console.log('Image upload triggered for color:', selectedColor);

  if (!selectedColor) {
    alert('Please select a color first');
    return;
  }

  const files = Array.from(e.target.files);
  console.log('Files selected:', files.length);

  files.forEach(file => {
    if (file && file.type.startsWith('image/')) {
      console.log('Processing image:', file.name);
      const reader = new FileReader();
      reader.onload = (e) => {
        const imageData = {
          id: imageIdCounter++,
          src: e.target.result,
          color: selectedColor,
          isHero: productImagesData[selectedColor].length === 0
        };
        productImagesData[selectedColor].push(imageData);
        console.log('Image added to color:', selectedColor, 'Total images:', productImagesData[selectedColor].length);
        renderImagesByColor();
      };
      reader.readAsDataURL(file);
    } else {
      console.warn('Invalid file type:', file.type);
    }
  });
  productImagesInput.value = '';
}

/**
 * Set Hero Image for a specific color
 */
function setHeroImage(color, imageId) {
  productImagesData[color].forEach(img => {
    img.isHero = img.id === imageId;
  });
  renderImagesByColor();
}

/**
 * Remove Image
 */
function removeImage(color, imageId) {
  const removedImage = productImagesData[color].find(img => img.id === imageId);
  productImagesData[color] = productImagesData[color].filter(img => img.id !== imageId);

  // If removed image was hero and there are other images, make the first one hero
  if (removedImage && removedImage.isHero && productImagesData[color].length > 0) {
    productImagesData[color][0].isHero = true;
  }

  renderImagesByColor();
}

/**
 * Render Images grouped by Color
 */
function renderImagesByColor() {
  let html = '';

  colorVariants.forEach(color => {
    const images = productImagesData[color] || [];
    if (images.length > 0) {
      html += `
        <div class="color-images-group">
          <h4 class="color-group-title">${color} (${images.length} image${images.length > 1 ? 's' : ''})</h4>
          <div class="images-slide-container">
            ${images.map(img => `
              <div class="image-slide-item ${img.isHero ? 'hero-selected' : ''}">
                <div class="image-slide-thumb" onclick="setHeroImage('${color}', ${img.id})">
                  <img src="${img.src}" alt="${color}" />
                  ${img.isHero ? '<span class="hero-badge"><i class="ti ti-star-filled"></i></span>' : ''}
                </div>
                <button
                  type="button"
                  class="remove-btn-bottom"
                  onclick="removeImage('${color}', ${img.id})"
                  title="Remove image"
                >
                  <i class="ti ti-trash"></i> Remove
                </button>
              </div>
            `).join('')}
          </div>
        </div>
      `;
    }
  });

  imagesByColorContainer.innerHTML = html || '<p class="text-center text-gray-400 text-sm py-4">No images uploaded yet</p>';
}

/**
 * Save Product Handler
 */
function handleSaveProduct(e) {
  e.preventDefault();

  // Basic form validation
  const productName = document.getElementById('productName')?.value;
  const productDescription = document.getElementById('productDescription')?.value;
  const productSKU = document.getElementById('productSKU')?.value;
  const regularPrice = document.getElementById('regularPrice')?.value;
  const stockQuantity = document.getElementById('stockQuantity')?.value;
  const category = document.getElementById('category')?.value;

  if (!productName || !productDescription || !productSKU || !regularPrice || !stockQuantity || !category) {
    alert('Please fill in all required fields marked with *');
    return;
  }

  // Validate colors
  if (colorVariants.length === 0) {
    alert('Please add at least one color variant');
    return;
  }

  // Validate that all colors have at least one size
  let missingSize = false;
  colorVariants.forEach(color => {
    if (!colorSizes[color] || colorSizes[color].length === 0) {
      alert(`Please add at least one size for ${color}`);
      missingSize = true;
    }
  });

  if (missingSize) {
    return;
  }

  // Validate images for each color
  let hasAllImages = true;
  colorVariants.forEach(color => {
    if (!productImagesData[color] || productImagesData[color].length === 0) {
      alert(`Please upload at least one image for ${color}`);
      hasAllImages = false;
    }
  });

  if (!hasAllImages) {
    return;
  }

  // Validate stock quantities
  let hasAllStock = true;
  colorVariants.forEach(color => {
    const sizes = colorSizes[color] || [];
    sizes.forEach(size => {
      const variantKey = `${color}-${size}`;
      if (!variantStockData[variantKey] || variantStockData[variantKey] <= 0) {
        alert(`Please set stock quantity for ${color} - Size ${size}`);
        hasAllStock = false;
      }
    });
  });

  if (!hasAllStock) {
    return;
  }

  // Prepare data for backend submission matching ProductCreateRequest DTO
  const productData = {
    name: productName,
    description: productDescription,
    price: parseFloat(regularPrice),
    salePrice: document.getElementById('salePrice')?.value ? parseFloat(document.getElementById('salePrice').value) : null,
    type: document.getElementById('productType')?.value || 'UNISEX',
    categoryId: parseInt(category),
    seo: productSKU.toLowerCase().replace(/\s+/g, '-'),
    defaultStock: parseInt(stockQuantity) || 0,

    // Colors array matching ColorData structure
    colors: colorVariants.map(color => {
      const sizes = colorSizes[color] || [];
      const images = productImagesData[color] || [];

      // Find hero image index
      const heroIndex = images.findIndex(img => img.isHero);

      return {
        name: color,
        sizes: sizes,
        images: images.map(img => img.src), // Array of base64 data URLs
        defaultImageIndex: heroIndex >= 0 ? heroIndex : 0,
        defaultStock: 0, // Not used per-color, we use sizeStocks
        sizeStocks: sizes.map(size => {
          const variantKey = `${color}-${size}`;
          return variantStockData[variantKey] || 0;
        })
      };
    })
  };

  console.log('Submitting product data:', productData);

  // Show loading state
  const saveBtn = document.getElementById('saveProductBtn');
  const originalText = saveBtn.textContent;
  saveBtn.disabled = true;
  saveBtn.textContent = 'Saving...';

  // Get CSRF token
  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

  // Submit to backend via AJAX
  fetch(window.APP_CTX + '/admin/api/products', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      [csrfHeader]: csrfToken
    },
    body: JSON.stringify(productData)
  })
  .then(response => {
    if (!response.ok) {
      return response.json().then(err => {
        throw new Error(err.error || 'Failed to save product');
      });
    }
    return response.json();
  })
  .then(data => {
    console.log('Product saved successfully:', data);
    alert('Product saved successfully!');

    // Redirect to product list
    if (data.redirectUrl) {
      window.location.href = window.APP_CTX + data.redirectUrl;
    } else {
      window.location.href = window.APP_CTX + '/admin/product/list';
    }
  })
  .catch(error => {
    console.error('Error saving product:', error);
    alert('Error saving product: ' + error.message);

    // Restore button state
    saveBtn.disabled = false;
    saveBtn.textContent = originalText;
  });
}

// Initialize when DOM is ready
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initializeProductAdd);
} else {
  initializeProductAdd();
}

