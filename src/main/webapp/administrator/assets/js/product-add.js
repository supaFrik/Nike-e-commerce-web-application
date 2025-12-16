// See src/main/resources/administrator/assets/js/product-add.js for the full logic.
// Keeping a copy under webapp assets so the JSP relative path ../assets/js/product-add.js resolves correctly.

// Variants Management
let colorVariants = [];
let sizeVariants = [];
let colorSizes = {};
let productImagesData = {};
let variantStockData = {};
let imageIdCounter = 0;

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

    if (colorVariants.length === 1) {
      noColorsForSize.classList.add('hidden');
      sizesInputSection.classList.remove('hidden');
      noColorsMessage.classList.add('hidden');
      imageUploadSection.classList.remove('hidden');
    }
  }
});

colorInput.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') { e.preventDefault(); addColorBtn.click(); }
});

colorForSize.addEventListener('change', (e) => {
  const enabled = !!e.target.value;
  sizeInput.disabled = !enabled;
  addSizeBtn.disabled = !enabled;
});

addSizeBtn.addEventListener('click', () => {
  const selectedColor = colorForSize.value;
  const size = sizeInput.value.trim();
  if (!selectedColor) { alert('Please select a color first'); return; }
  if (size && !colorSizes[selectedColor].includes(size)) {
    colorSizes[selectedColor].push(size);
    colorSizes[selectedColor].sort((a, b) => parseFloat(a) - parseFloat(b));
    renderSizesByColor();
    updateInventorySection();
    sizeInput.value = '';
  }
});

sizeInput.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') { e.preventDefault(); addSizeBtn.click(); }
});

function removeColor(color) {
  colorVariants = colorVariants.filter(c => c !== color);
  delete productImagesData[color];
  delete colorSizes[color];
  Object.keys(variantStockData).forEach(key => { if (key.startsWith(color + '-')) delete variantStockData[key]; });
  renderColors();
  updateColorSelector();
  updateColorSizeSelector();
  renderImagesByColor();
  updateInventorySection();
  if (colorVariants.length === 0) {
    noColorsForSize.classList.remove('hidden');
    sizesInputSection.classList.add('hidden');
    noColorsMessage.classList.remove('hidden');
    imageUploadSection.classList.add('hidden');
    selectColorForUpload.value = '';
    productImagesInput.disabled = true;
  }
}

function removeSize(color, size) {
  colorSizes[color] = (colorSizes[color] || []).filter(s => s !== size);
  delete variantStockData[`${color}-${size}`];
  renderSizesByColor();
  updateInventorySection();
}

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

function updateColorSizeSelector() {
  colorForSize.innerHTML = '<option value="">Select color</option>' +
    colorVariants.map(color => `<option value="${color}">${color}</option>`).join('');
}

function updateInventorySection() {
  const hasSizes = colorVariants.some(color => (colorSizes[color] || []).length > 0);
  if (colorVariants.length === 0 || !hasSizes) { noVariantsMessage.classList.remove('hidden'); inventorySection.classList.add('hidden'); return; }
  noVariantsMessage.classList.add('hidden'); inventorySection.classList.remove('hidden');
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
            <input type="number" class="w-full px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:border-blue-500" placeholder="0" min="0" value="${currentStock}" onchange="updateVariantStock('${variantKey}', this.value)" />
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

function updateVariantStock(variantKey, quantity) { variantStockData[variantKey] = parseInt(quantity) || 0; }

function updateColorSelector() {
  selectColorForUpload.innerHTML = '<option value="">Choose a color</option>' +
    colorVariants.map(color => `<option value="${color}">${color}</option>`).join('');
}

selectColorForUpload.addEventListener('change', (e) => { productImagesInput.disabled = !e.target.value; });

imageUploadArea.addEventListener('click', () => { if (!productImagesInput.disabled) productImagesInput.click(); });

productImagesInput.addEventListener('change', (e) => {
  const selectedColor = selectColorForUpload.value;
  if (!selectedColor) { alert('Please select a color first'); return; }
  const files = Array.from(e.target.files);
  files.forEach(file => {
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader();
      reader.onload = (ev) => {
        const imageData = { id: imageIdCounter++, src: ev.target.result, color: selectedColor, isHero: productImagesData[selectedColor].length === 0 };
        productImagesData[selectedColor].push(imageData);
        renderImagesByColor();
      };
      reader.readAsDataURL(file);
    }
  });
  productImagesInput.value = '';
});

function setHeroImage(color, imageId) { productImagesData[color].forEach(img => { img.isHero = img.id === imageId; }); renderImagesByColor(); }

function removeImage(color, imageId) {
  const removedImage = (productImagesData[color] || []).find(img => img.id === imageId);
  productImagesData[color] = (productImagesData[color] || []).filter(img => img.id !== imageId);
  if (removedImage && removedImage.isHero && productImagesData[color].length > 0) { productImagesData[color][0].isHero = true; }
  renderImagesByColor();
}

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
                <button type="button" class="remove-btn-bottom" onclick="removeImage('${color}', ${img.id})" title="Remove image"><i class="ti ti-trash"></i> Remove</button>
              </div>
            `).join('')}
          </div>
        </div>
      `;
    }
  });
  imagesByColorContainer.innerHTML = html || '<p class="text-center text-gray-400 text-sm py-4">No images uploaded yet</p>';
}

document.getElementById('saveProductBtn').addEventListener('click', (e) => {
  e.preventDefault();
  const productName = document.getElementById('productName').value;
  const productDescription = document.getElementById('productDescription').value;
  const productSKU = document.getElementById('productSKU').value;
  const regularPrice = document.getElementById('regularPrice').value;
  const stockQuantity = document.getElementById('stockQuantity')?.value;
  const category = document.getElementById('category').value;
  const status = document.getElementById('status').value;
  if (!productName || !productDescription || !productSKU || !regularPrice || !category) { alert('Please fill in all required fields marked with *'); return; }
  if (colorVariants.length === 0) { alert('Please add at least one color variant'); return; }
  let missingSize = false;
  colorVariants.forEach(color => { if (!colorSizes[color] || colorSizes[color].length === 0) { alert(`Please add at least one size for ${color}`); missingSize = true; } });
  if (missingSize) return;
  let hasAllImages = true;
  colorVariants.forEach(color => { if (!productImagesData[color] || productImagesData[color].length === 0) { alert(`Please upload at least one image for ${color}`); hasAllImages = false; } });
  if (!hasAllImages) return;
  let hasAllStock = true;
  colorVariants.forEach(color => { (colorSizes[color] || []).forEach(size => { const variantKey = `${color}-${size}`; if (!variantStockData[variantKey] || variantStockData[variantKey] <= 0) { alert(`Please set stock quantity for ${color} - Size ${size}`); hasAllStock = false; } }); });
  if (!hasAllStock) return;
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
  alert('Product saved successfully!');
  // window.location.href = './product-list.html';
});

