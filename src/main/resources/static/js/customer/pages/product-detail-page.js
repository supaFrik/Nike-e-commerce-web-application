(function () {
  function escapeHtml(value) {
    return String(value || "")
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#39;");
  }

  function resolveInitialImage(images) {
    if (!images || images.length === 0) {
      return null;
    }
    return images.find((image) => image.isMainForColor) || images[0];
  }

  function updateImageCount(images) {
    const imageCountLabel = document.getElementById("image-count-label");
    if (!imageCountLabel) {
      return;
    }
    const total = images && images.length ? images.length : 1;
    imageCountLabel.textContent = total + (total === 1 ? " view" : " views");
  }

  function loadPayload() {
    const payload = document.getElementById("productDetailPayload");
    if (!payload) {
      throw new Error("Missing product detail payload");
    }
    return JSON.parse(payload.innerHTML.trim());
  }

  const payload = loadPayload();
  const defaultProductImage = payload.defaultProductImage;
  const defaultProductName = payload.defaultProductName;
  window.productDetailState = {
    colors: payload.colors || [],
    selectedColorIndex: 0,
    selectedVariantId: null,
    selectedImageIndex: 0
  };

  function setActiveImage(index) {
    const color = window.productDetailState.colors[window.productDetailState.selectedColorIndex];
    if (!color || !color.images || color.images.length === 0) {
      return;
    }

    const boundedIndex = ((index % color.images.length) + color.images.length) % color.images.length;
    const image = color.images[boundedIndex];
    const currentImage = document.getElementById("currentImage");
    if (!currentImage) {
      return;
    }

    window.productDetailState.selectedImageIndex = boundedIndex;
    currentImage.src = image.path;
    currentImage.alt = image.altText || image.title || defaultProductName;

    document.querySelectorAll(".thumbnail-item").forEach((item, itemIndex) => {
      item.classList.toggle("active", itemIndex === boundedIndex);
    });
  }

  function bindThumbnailEvents() {
    const currentImage = document.getElementById("currentImage");
    document.querySelectorAll(".thumbnail-item").forEach((button) => {
      button.addEventListener("click", () => {
        const imageIndex = Number(button.dataset.thumbnailIndex);
        if (!Number.isNaN(imageIndex)) {
          setActiveImage(imageIndex);
          return;
        }

        document.querySelectorAll(".thumbnail-item").forEach((item) => item.classList.remove("active"));
        button.classList.add("active");
        currentImage.src = button.dataset.imagePath;
        currentImage.alt = button.dataset.altText || defaultProductName;
      });
    });
  }

  function bindVariantEvents() {
    document.querySelectorAll(".size-option:not(.disabled)").forEach((button) => {
      button.addEventListener("click", () => {
        document.querySelectorAll(".size-option").forEach((item) => {
          item.classList.remove("selected");
          item.setAttribute("aria-checked", "false");
        });

        button.classList.add("selected");
        button.setAttribute("aria-checked", "true");
        window.productDetailState.selectedVariantId = button.dataset.variantId;
        updateAddToCartButton();
      });
    });
  }

  function renderImages(images) {
    const thumbnailNav = document.getElementById("thumbnail-nav");
    const currentImage = document.getElementById("currentImage");
    if (!thumbnailNav || !currentImage) {
      return;
    }

    updateImageCount(images);
    if (!images || images.length === 0) {
      thumbnailNav.innerHTML = "";
      currentImage.src = defaultProductImage;
      currentImage.alt = defaultProductName;
      return;
    }

    const initialImage = resolveInitialImage(images);
    const initialIndex = images.findIndex((image) => initialImage && image.id === initialImage.id);
    window.productDetailState.selectedImageIndex = initialIndex >= 0 ? initialIndex : 0;
    currentImage.src = initialImage.path;
    currentImage.alt = initialImage.altText || initialImage.title || defaultProductName;

    const hasMainImage = images.some((image) => image.isMainForColor);
    thumbnailNav.innerHTML = images.map((image, index) => {
      const isActive = image.isMainForColor || (!hasMainImage && index === 0);
      const altText = image.altText || image.title || "Product image";
      return `<button type="button" class="thumbnail-item ${isActive ? "active" : ""}" data-thumbnail-index="${index}" data-image-path="${escapeHtml(image.path)}" data-alt-text="${escapeHtml(altText)}" aria-label="View image ${index + 1}"><img src="${escapeHtml(image.path)}" alt="${escapeHtml(altText)}"></button>`;
    }).join("");

    bindThumbnailEvents();
    if (window.ThumbnailGallery && typeof window.ThumbnailGallery.refresh === "function") {
      window.ThumbnailGallery.refresh();
    }
  }

  function renderVariants(variants) {
    const sizeOptions = document.getElementById("size-options");
    if (!sizeOptions) {
      return;
    }

    if (!variants || variants.length === 0) {
      sizeOptions.innerHTML = '<div class="no-sizes-message" role="status">No sizes available.</div>';
      return;
    }

    sizeOptions.innerHTML = variants.map((variant) => {
      const disabled = variant.stock <= 0 || !variant.active;
      const stockLabel = disabled ? "Sold out" : (variant.stock <= 3 ? "Low stock" : "In stock");
      return `<button type="button" class="size-btn size-option ${disabled ? "disabled out-of-stock" : ""}" data-variant-id="${variant.id}" data-size="${escapeHtml(variant.size)}" data-stock="${variant.stock}" role="radio" aria-checked="false"${disabled ? ' disabled="disabled"' : ""}><span class="size-label">${escapeHtml(variant.size)}</span><span class="size-stock">${stockLabel}</span></button>`;
    }).join("");

    bindVariantEvents();
  }

  function updateAddToCartButton() {
    const button = document.getElementById("add-to-cart-button");
    if (!button) {
      return;
    }
    button.disabled = !window.productDetailState.selectedVariantId;
    button.textContent = "Add to Bag";
  }

  function renderColor(index) {
    const color = window.productDetailState.colors[index];
    if (!color) {
      return;
    }

    window.productDetailState.selectedColorIndex = index;
    window.productDetailState.selectedVariantId = null;

    const selectedColorLabel = document.getElementById("selected-color-label");
    if (selectedColorLabel) {
      selectedColorLabel.textContent = color.colorName;
    }

    const selectedColorDetail = document.getElementById("selected-color-detail");
    if (selectedColorDetail) {
      selectedColorDetail.textContent = color.colorName;
    }

    document.querySelectorAll(".color-option").forEach((button, buttonIndex) => {
      const isSelected = buttonIndex === index;
      button.classList.toggle("active", isSelected);
      button.classList.toggle("selected", isSelected);
      button.setAttribute("aria-pressed", isSelected ? "true" : "false");
    });

    renderImages(color.images);
    renderVariants(color.variants);
    updateAddToCartButton();
  }

  document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".color-option").forEach((button) => {
      button.addEventListener("click", () => renderColor(Number(button.dataset.colorIndex)));
    });

    document.getElementById("add-to-cart-button")?.addEventListener("click", () => {
      if (typeof window.addToCart === "function") {
        window.addToCart();
      }
    });

    document.getElementById("prev-image-button")?.addEventListener("click", () => {
      setActiveImage(window.productDetailState.selectedImageIndex - 1);
    });
    document.getElementById("next-image-button")?.addEventListener("click", () => {
      setActiveImage(window.productDetailState.selectedImageIndex + 1);
    });

    const deliveryToggle = document.getElementById("delivery-returns-toggle");
    const deliveryPanel = document.getElementById("delivery-returns-panel");
    if (deliveryToggle && deliveryPanel) {
      deliveryToggle.addEventListener("click", () => {
        const expanded = deliveryToggle.getAttribute("aria-expanded") === "true";
        deliveryToggle.setAttribute("aria-expanded", expanded ? "false" : "true");
        deliveryPanel.hidden = expanded;
      });
    }

    bindThumbnailEvents();
    bindVariantEvents();
    updateImageCount(window.productDetailState.colors[0] ? window.productDetailState.colors[0].images : []);
    updateAddToCartButton();
  });
})();
