/**
 * Admin Product List Page - JavaScript
 * Handles product rendering, filtering, pagination, and quick view panel
 */

(function() {
  'use strict';

  // Global state
  let selectedProduct = null;
  let currentCategory = 'all';
  let currentPage = 1;
  const itemsPerPage = 8;

  let productGrid, template, quickViewPanel, categoryTabs, quickViewTabs, discardBtn;
  let deleteBtn;

  /**
   * Initialize the page
   */
  function init() {
    // Cache DOM elements
    productGrid = document.getElementById('productGrid');
    template = document.getElementById('productCardTemplate');
    quickViewPanel = document.getElementById('quickViewPanel');
    categoryTabs = document.querySelectorAll('.category-tab');
    quickViewTabs = document.querySelectorAll('.quick-view-tab');
    discardBtn = document.getElementById('discardBtn');
    deleteBtn = document.getElementById('deleteBtn');

    if (!productGrid || !template) {
      console.error('[Product List] Missing required DOM elements');
      return;
    }

    // Bind event listeners
    bindEvents();

    // Initial render
    renderProducts();
  }

  /**
   * Bind all event listeners
   */
  function bindEvents() {
    // Category tabs
    categoryTabs.forEach(tab => {
      tab.addEventListener('click', handleCategoryClick);
    });

    // Quick view tabs
    quickViewTabs.forEach(tab => {
      tab.addEventListener('click', handleQuickViewTabClick);
    });

    // Discard button
    if (discardBtn) {
      discardBtn.addEventListener('click', handleDiscard);
    }
    if (deleteBtn) {
      deleteBtn.addEventListener('click', handleDeleteSelected);
    }

    // Update button
    const updateBtn = document.getElementById('updateBtn');
    if (updateBtn) {
      updateBtn.addEventListener('click', handleUpdate);
    }

    // Search input
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
      searchInput.addEventListener('input', handleSearch);
    }

    // Pagination buttons
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    if (prevBtn) {
      prevBtn.addEventListener('click', handlePrevPage);
    }
    if (nextBtn) {
      nextBtn.addEventListener('click', handleNextPage);
    }
  }

  /**
   * Format currency in Vietnamese Dong
   */
  function formatCurrency(amount) {
    return '₫' + amount.toLocaleString('vi-VN');
  }

  /**
   * Render products based on category and page
   */
  function renderProducts(category = 'all', page = 1) {
    if (!productGrid || !template) return;

    productGrid.innerHTML = '';
    let products = window.__PRODUCTS__ || [];

    // Filter by category
    if (category !== 'all') {
      const categoryId = parseInt(category);
      if (!isNaN(categoryId)) {
        products = products.filter(p => p.category === categoryId);
      }
    }

    // Pagination
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedProducts = products.slice(startIndex, endIndex);

    // Update pagination info
    const pageStartEl = document.getElementById('pageStart');
    const pageEndEl = document.getElementById('pageEnd');
    const totalItemsEl = document.getElementById('totalItems');

    if (pageStartEl) pageStartEl.textContent = products.length > 0 ? startIndex + 1 : 0;
    if (pageEndEl) pageEndEl.textContent = Math.min(endIndex, products.length);
    if (totalItemsEl) totalItemsEl.textContent = products.length;

    // Show message if no products
    if (paginatedProducts.length === 0) {
      productGrid.innerHTML = '<div class="col-span-full text-center py-12"><div class="text-gray-400 text-lg">No products found</div><div class="text-gray-500 text-sm mt-2">Try changing the category filter or adding new products</div></div>';
      return;
    }

    // Render each product
    paginatedProducts.forEach(product => {
      const clone = template.content.cloneNode(true);
      const card = clone.querySelector('.product-card');
      if (!card) return;

      // Set product data
      card.dataset.productId = product.id;
      const imgEl = clone.querySelector('[data-ref="image"]');
      if (imgEl) {
        imgEl.src = product.image;
        imgEl.alt = product.name;
      }
      const titleEl = clone.querySelector('[data-ref="title"]');
      if (titleEl) titleEl.textContent = product.name;
      const priceEl = clone.querySelector('[data-ref="price"]');
      if (priceEl) priceEl.textContent = formatCurrency(product.salePrice > 0 ? product.salePrice : product.price);
      const stockEl = clone.querySelector('[data-ref="stock"]');
      if (stockEl) stockEl.textContent = product.stock;
      const soldEl = clone.querySelector('[data-ref="sold"]');
      if (soldEl) soldEl.textContent = product.sold;

      // Status indicator
      const statusDot = clone.querySelector('[data-ref="statusDot"]');
      if (statusDot) {
        if (product.active) {
          statusDot.classList.add('pulse');
          statusDot.style.background = '#10b981';
        } else {
          statusDot.classList.remove('pulse');
          statusDot.style.background = '#ef4444';
        }
      }

      // Color dots
      const colorsContainer = clone.querySelector('[data-ref="colors"]');
      if (colorsContainer && product.colors && product.colors.length > 0) {
        product.colors.slice(0, 3).forEach(color => {
          const colorDot = document.createElement('div');
          colorDot.className = 'color-dot';
          colorDot.style.background = color;
          colorsContainer.appendChild(colorDot);
        });
        if (product.colors.length > 3) {
          const colorCount = document.createElement('span');
          colorCount.className = 'text-xs text-gray-600 ml-1';
          colorCount.textContent = `+${product.colors.length - 3}`;
          colorsContainer.appendChild(colorCount);
        }
      }

      // Card click -> select/open quick view
      card.addEventListener('click', function(e) {
        // Prevent card click when pressing delete button on card
        if ((e.target.closest && e.target.closest('.btn-card-delete'))) return;
        document.querySelectorAll('.product-card').forEach(c => {
          c.classList.remove('border-blue-500');
          c.style.boxShadow = '';
        });
        card.classList.add('border-blue-500');
        card.style.boxShadow = '0 0 0 1px #3b82f6';
        openQuickView(product);
      });

      // Delete overlay button
      const deleteOverlayBtn = clone.querySelector('[data-ref="deleteBtn"]');
      if (deleteOverlayBtn) {
        deleteOverlayBtn.addEventListener('click', (ev) => {
          ev.stopPropagation();
          confirmAndDelete(product.id, product.name);
        });
      }

      productGrid.appendChild(clone);
    });
  }

  /**
   * Open quick view panel for a product
   */
  function openQuickView(product) {
    selectedProduct = product;
    if (quickViewPanel) {
      quickViewPanel.classList.add('visible');
    }

    // Store product ID in hidden input
    const productIdInput = document.getElementById('selectedProductId');
    if (productIdInput) {
      productIdInput.value = product.id;
    }

    // Update the full view link
    const fullViewLink = document.getElementById('fullViewLink');
    if (fullViewLink) {
      fullViewLink.href = window.APP_CTX + '/admin/product/edit/' + product.id;
    }

    // Populate form
    const quickViewImage = document.getElementById('quickViewImage');
    if (quickViewImage) {
      quickViewImage.src = product.image;
    }

    const productName = document.getElementById('productName');
    if (productName) {
      productName.value = product.name;
    }

    const productDescription = document.getElementById('productDescription');
    if (productDescription) {
      productDescription.value = product.description || '';
    }

    // Set category
    const categorySelect = document.getElementById('productCategory');
    if (categorySelect) {
      categorySelect.value = product.category;
    }

    const currentStock = document.getElementById('currentStock');
    if (currentStock) {
      currentStock.textContent = product.stock;
    }

    const totalSold = document.getElementById('totalSold');
    if (totalSold) {
      totalSold.textContent = product.sold || 0;
    }

    const productPrice = document.getElementById('productPrice');
    if (productPrice) {
      productPrice.value = formatCurrency(product.salePrice > 0 ? product.salePrice : product.price);
    }

    const finalPrice = document.getElementById('finalPrice');
    if (finalPrice) {
      finalPrice.value = formatCurrency(product.salePrice > 0 ? product.salePrice : product.price);
    }
  }

  /**
   * Handle category tab click
   */
  function handleCategoryClick(e) {
    const tab = e.currentTarget;

    // Update tab styling
    categoryTabs.forEach(t => {
      t.classList.remove('active', 'bg-black', 'text-white');
      t.classList.add('bg-gray-50', 'text-gray-600');
    });
    tab.classList.remove('bg-gray-50', 'text-gray-600');
    tab.classList.add('active', 'bg-black', 'text-white');

    // Update state and re-render
    currentCategory = tab.dataset.category;
    currentPage = 1;
    renderProducts(currentCategory, currentPage);
  }

  /**
   * Handle quick view tab click
   */
  function handleQuickViewTabClick(e) {
    const tab = e.currentTarget;

    // Update tab styling
    quickViewTabs.forEach(t => {
      t.classList.remove('active', 'text-gray-800', 'border-black');
      t.classList.add('text-gray-600', 'border-transparent');
    });
    tab.classList.remove('text-gray-600', 'border-transparent');
    tab.classList.add('active', 'text-gray-800', 'border-black');

    // Switch tab content
    const tabName = tab.dataset.tab;
    document.querySelectorAll('[id$="Tab"]').forEach(content => {
      content.classList.add('hidden');
      content.classList.remove('block');
    });

    const targetTab = document.getElementById(tabName + 'Tab');
    if (targetTab) {
      targetTab.classList.remove('hidden');
      targetTab.classList.add('block');
    }
  }

  /**
   * Handle discard button click
   */
  function handleDiscard() {
    if (quickViewPanel) {
      quickViewPanel.classList.remove('visible');
    }

    // Clear selection
    document.querySelectorAll('.product-card').forEach(c => {
      c.classList.remove('border-blue-500');
      c.style.boxShadow = '';
    });

    selectedProduct = null;
  }

  /**
   * Handle update button click
   */
  function handleUpdate() {
    const productId = document.getElementById('selectedProductId')?.value;
    if (!productId) {
      alert('No product selected');
      return;
    }

    // Collect form data
    const updateData = {
      name: document.getElementById('productName')?.value,
      description: document.getElementById('productDescription')?.value,
      categoryId: parseInt(document.getElementById('productCategory')?.value),
    };

    // Parse price from formatted string
    const priceText = document.getElementById('productPrice')?.value.replace(/[₫,]/g, '').trim();
    const priceValue = parseFloat(priceText);
    if (!isNaN(priceValue)) {
      updateData.salePrice = priceValue;
    }

    // Get stock update if provided
    const stockInput = document.getElementById('updateStock');
    if (stockInput && stockInput.value) {
      updateData.stock = parseInt(stockInput.value);
    }

    // Get discount if provided
    const discountInput = document.getElementById('productDiscount');
    if (discountInput && discountInput.value) {
      updateData.discount = parseFloat(discountInput.value);
    }

    // Make API call to update product
    fetch(window.APP_CTX + '/admin/api/products/' + productId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updateData)
    })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => Promise.reject(err));
      }
      return response.json();
    })
    .then(data => {
      alert('Product updated successfully!');
      // Reload the page to see changes
      window.location.reload();
    })
    .catch(error => {
      console.error('Error updating product:', error);
      alert('Error updating product: ' + (error.error || error.message || 'Unknown error'));
    });
  }

  /**
   * Handle search input
   */
  function handleSearch(e) {
    const searchTerm = e.target.value.toLowerCase();
    const products = window.__PRODUCTS__ || [];
    const filtered = products.filter(p =>
      p.name.toLowerCase().includes(searchTerm) ||
      (p.description && p.description.toLowerCase().includes(searchTerm))
    );

    if (searchTerm) {
      productGrid.innerHTML = '';
      filtered.forEach(product => {
        const clone = template.content.cloneNode(true);
        const card = clone.querySelector('.product-card');

        card.dataset.productId = product.id;
        clone.querySelector('[data-ref="image"]').src = product.image;
        clone.querySelector('[data-ref="image"]').alt = product.name;
        clone.querySelector('[data-ref="title"]').textContent = product.name;
        clone.querySelector('[data-ref="price"]').textContent = formatCurrency(product.price);
        clone.querySelector('[data-ref="stock"]').textContent = product.stock;
        clone.querySelector('[data-ref="sold"]').textContent = product.sold;

        const colorsContainer = clone.querySelector('[data-ref="colors"]');
        if (colorsContainer && product.colors) {
          product.colors.forEach((color, index) => {
            if (index < 3) {
              const colorDot = document.createElement('div');
              colorDot.className = 'color-dot';
              colorDot.style.background = color;
              colorsContainer.appendChild(colorDot);
            }
          });
        }

        card.addEventListener('click', function() {
          document.querySelectorAll('.product-card').forEach(c => {
            c.classList.remove('border-blue-500');
            c.style.boxShadow = '';
          });
          card.classList.add('border-blue-500');
          card.style.boxShadow = '0 0 0 1px #3b82f6';
          openQuickView(product);
        });

        productGrid.appendChild(clone);
      });
    } else {
      renderProducts(currentCategory, currentPage);
    }
  }

  /**
   * Handle previous page button click
   */
  function handlePrevPage() {
    if (currentPage > 1) {
      currentPage--;
      renderProducts(currentCategory, currentPage);
    }
  }

  /**
   * Handle next page button click
   */
  function handleNextPage() {
    const products = window.__PRODUCTS__ || [];
    const maxPages = Math.ceil(products.length / itemsPerPage);
    if (currentPage < maxPages) {
      currentPage++;
      renderProducts(currentCategory, currentPage);
    }
  }

  function handleDeleteSelected() {
    const productId = document.getElementById('selectedProductId')?.value;
    if (!productId) {
      alert('Chưa chọn sản phẩm');
      return;
    }
    const product = (window.__PRODUCTS__ || []).find(p => String(p.id) === String(productId));
    const name = product ? product.name : ('#' + productId);
    confirmAndDelete(productId, name);
  }

  function confirmAndDelete(id, name) {
    if (!id) return;
    const ok = confirm(`Xóa sản phẩm "${name}"? Hành động này không thể hoàn tác.`);
    if (!ok) return;
    fetch(window.APP_CTX + '/admin/api/products/' + id, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) return res.json().then(err => Promise.reject(err));
        return res.json();
      })
      .then(() => {
        // Remove from local list and re-render
        window.__PRODUCTS__ = (window.__PRODUCTS__ || []).filter(p => String(p.id) !== String(id));
        selectedProduct = null;
        if (quickViewPanel) quickViewPanel.classList.remove('visible');
        renderProducts(currentCategory, currentPage);
      })
      .catch(err => {
        console.error('Delete failed', err);
        alert('Xóa thất bại: ' + (err.error || err.message || 'Lỗi không xác định'));
      });
  }

  // Start the page when DOM is ready
  document.addEventListener('DOMContentLoaded', init);

})();
