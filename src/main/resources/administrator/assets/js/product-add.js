// Variants Management
let colorVariants = [];
let sizeVariants = []; // Global sizes still available
let colorSizes = {}; // Store sizes per color: { colorName: [sizes] }
let productImagesData = {}; // Store images by color: { colorName: [images] }
let variantStockData = {}; // Store stock by variant: { "color-size": quantity }
let imageIdCounter = 0;

// DOM Elements
const colorInput = document.getElementById('colorInput');
const addColorBtn = document.getElementById('addColorBtn');
const colorsList = document.getElementById('colorsList');
const noColorsForSize = document.getElementById('noColorsForSize');
const sizesInputSection = document.getElementById('sizesInputSection');
const colorForSize = document.getElementById('colorForSize');
const sizeInput = document.getElementById('sizeInput');
const addSizeBtn = document.getElementById('addSizeBtn');
const sizesByColorContainer = document.getElementById('sizesByColorContainer');
const noColorsMessage = document.getElementById('noColorsMessage');
const imageUploadSection = document.getElementById('imageUploadSection');
const selectColorForUpload = document.getElementById('selectColorForUpload');
const imageUploadArea = document.getElementById('imageUploadArea');
const productImagesInput = document.getElementById('productImages');
const imagesByColorContainer = document.getElementById('imagesByColorContainer');
const noVariantsMessage = document.getElementById('noVariantsMessage');
const inventorySection = document.getElementById('inventorySection');
const variantStockContainer = document.getElementById('variantStockContainer');

// Add Color Variant
addColorBtn.addEventListener('click', () => {
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
});

colorInput.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') {
    e.preventDefault();
    addColorBtn.click();
  }
});

// Enable/disable size input based on color selection
colorForSize.addEventListener('change', (e) => {
  if (e.target.value) {
    sizeInput.disabled = false;
    addSizeBtn.disabled = false;
  } else {
    sizeInput.disabled = true;
    addSizeBtn.disabled = true;
  }
});

// Add Size Variant
addSizeBtn.addEventListener('click', () => {
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
});

sizeInput.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') {
    e.preventDefault();
    addSizeBtn.click();
  }
});

// Remove Color
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

// Remove Size from specific color
function removeSize(color, size) {
  colorSizes[color] = colorSizes[color].filter(s => s !== size);
  // Remove stock data for this color-size
  const variantKey = `${color}-${size}`;
  delete variantStockData[variantKey];
  renderSizesByColor();
  updateInventorySection();
}

// Render Colors
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

// Render Sizes by Color
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

// Update Color Selector for Sizes
function updateColorSizeSelector() {
  colorForSize.innerHTML = '<option value="">Select color</option>' +
    colorVariants.map(color => `<option value="${color}">${color}</option>`).join('');
}

// Update Inventory Section
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

// Update Variant Stock
function updateVariantStock(variantKey, quantity) {
  variantStockData[variantKey] = parseInt(quantity) || 0;
}

// Update Color Selector
function updateColorSelector() {
  selectColorForUpload.innerHTML = '<option value="">Choose a color</option>' +
    colorVariants.map(color => `<option value="${color}">${color}</option>`).join('');
}

// Enable/Disable Image Upload based on color selection
selectColorForUpload.addEventListener('change', (e) => {
  if (e.target.value) {
    productImagesInput.disabled = false;
  } else {
    productImagesInput.disabled = true;
  }
});

// Image Upload
imageUploadArea.addEventListener('click', () => {
  if (!productImagesInput.disabled) {
    productImagesInput.click();
  }
});

productImagesInput.addEventListener('change', (e) => {
  const selectedColor = selectColorForUpload.value;
  if (!selectedColor) {
    alert('Please select a color first');
    return;
  }

  const files = Array.from(e.target.files);
  files.forEach(file => {
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader();
      reader.onload = (e) => {
        const imageData = {
          id: imageIdCounter++,
          src: e.target.result,
          color: selectedColor,
          isHero: productImagesData[selectedColor].length === 0
        };
        productImagesData[selectedColor].push(imageData);
        renderImagesByColor();
      };
      reader.readAsDataURL(file);
    }
  });
  productImagesInput.value = '';
});

// Set Hero Image for a specific color
function setHeroImage(color, imageId) {
  productImagesData[color].forEach(img => {
    img.isHero = img.id === imageId;
  });
  renderImagesByColor();
}

// Remove Image
function removeImage(color, imageId) {
  const removedImage = productImagesData[color].find(img => img.id === imageId);
  productImagesData[color] = productImagesData[color].filter(img => img.id !== imageId);

  // If removed image was hero and there are other images, make the first one hero
  if (removedImage && removedImage.isHero && productImagesData[color].length > 0) {
    productImagesData[color][0].isHero = true;
  }

  renderImagesByColor();
}

// Render Images grouped by Color
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

// Save Product Functionality
document.getElementById('saveProductBtn').addEventListener('click', (e) => {
  e.preventDefault();

  // Basic form validation
  const productName = document.getElementById('productName').value;
  const productDescription = document.getElementById('productDescription').value;
  const productSKU = document.getElementById('productSKU').value;
  const regularPrice = document.getElementById('regularPrice').value;
  const stockQuantity = document.getElementById('stockQuantity')?.value; // optional if not present
  const category = document.getElementById('category').value;
  const status = document.getElementById('status').value;

  if (!productName || !productDescription || !productSKU || !regularPrice || !category) {
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

  // Collect form data
  const formData = {
    name: productName,
    description: productDescription,
    sku: productSKU,
    barcode: document.getElementById('productBarcode').value,
    regularPrice: regularPrice,
    salePrice: document.getElementById('salePrice').value,
    costPrice: document.getElementById('costPrice').value,
    taxRate: document.getElementById('taxRate').value,
    stockQuantity: stockQuantity,
    lowStockThreshold: document.getElementById('lowStockThreshold')?.value,
    trackInventory: document.getElementById('trackInventory')?.checked,
    weight: document.getElementById('weight')?.value,
    length: document.getElementById('length')?.value,
    width: document.getElementById('width')?.value,
    height: document.getElementById('height')?.value,
    category: category,
    brand: document.getElementById('brand').value,
    tags: document.getElementById('tags').value,
    status: status,
    visibility: document.getElementById('visibility').value,
    featured: document.getElementById('featured').checked,
    colorVariants: colorVariants,
    colorSizes: colorSizes,
    images: productImagesData,
    variantStock: variantStockData
  };

  console.log('Product Data:', formData);

  // Show success message
  alert('Product saved successfully!');

  // Redirect to product list
  // window.location.href = './product-list.html';
});

