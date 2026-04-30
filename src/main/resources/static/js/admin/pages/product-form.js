(function () {
  const formType = document.body.dataset.formType || "add";
  const productId = document.body.dataset.productId || "";
  const state = {
    categories: [],
    colors: []
  };
  const sliderIndexByColor = {};

  const colorList = document.getElementById("colorList");
  const sizeContainer = document.getElementById("sizeMap");
  const uploadPreview = document.getElementById("uploadPreview");
  const uploadColor = document.getElementById("uploadColor");
  const uploadDropzone = document.getElementById("uploadDropzone");
  const uploadDropzoneCopy = document.getElementById("uploadDropzoneCopy");
  const productImageInput = document.getElementById("productImageInput");
  const stockContainer = document.getElementById("variantStock");
  const colorInput = document.getElementById("colorInput");
  const addColorButton = document.getElementById("addColorButton");
  const sizeInput = document.getElementById("sizeInput");
  const sizeColor = document.getElementById("sizeColor");
  const addSizeButton = document.getElementById("addSizeButton");
  const saveLabel = document.getElementById("saveLabel");
  const saveButtons = Array.from(document.querySelectorAll("[data-save-mode]"));
  const formTitle = document.getElementById("formTitle");
  const formCopy = document.getElementById("formCopy");
  const imageFrame = document.getElementById("mainProductImage");
  const previewProductName = document.getElementById("previewProductName");
  const previewDescription = document.getElementById("previewDescription");
  const summaryCategory = document.getElementById("summaryCategory");
  const summaryPrice = document.getElementById("summaryPrice");
  const summaryColors = document.getElementById("summaryColors");
  const summaryVariants = document.getElementById("summaryVariants");
  const readinessPill = document.getElementById("readinessPill");
  const launchStatusBadge = document.getElementById("launchStatusBadge");
  const mobileReadinessText = document.getElementById("mobileReadinessText");
  const mobilePreviewState = document.getElementById("mobilePreviewState");
  const checklist = document.getElementById("launchChecklist");
  const stepMeter = document.getElementById("stepMeter");
  const productNameInput = document.getElementById("productName");
  const productDescriptionInput = document.getElementById("productDescription");
  const productSkuInput = document.getElementById("productSku");
  const productTypeInput = document.getElementById("productType");
  const productCategoryInput = document.getElementById("productCategory");
  const regularPriceInput = document.getElementById("regularPrice");
  const salePriceInput = document.getElementById("salePrice");
  const productStatusInput = document.getElementById("productStatus");
  const editStatusBadge = document.getElementById("editStatusBadge");

  function ctx() {
    return (window.APP_CTX || "").replace(/\/$/, "");
  }

  function resolveImagePath(path) {
    if (!path) {
      return "";
    }
    return /^(blob:|data:|https?:)/.test(path) ? path : window.AdminSuite.asset(path);
  }

  function normalizeSlug(value) {
    return String(value || "")
      .trim()
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, "-")
      .replace(/^-+|-+$/g, "");
  }

  function buildVariantSku(baseSku, color, size, index) {
    const skuRoot = (baseSku || productNameInput?.value || "nike-product")
      .trim()
      .toUpperCase()
      .replace(/[^A-Z0-9]+/g, "-");
    const safeColor = normalizeSlug(color).toUpperCase() || `COLOR${index + 1}`;
    const safeSize = String(size).trim().replace(/[^0-9A-Za-z]+/g, "");
    return `${skuRoot}-${safeColor}-${safeSize}`;
  }

  function buildClientKey(color, index) {
    const safeColor = normalizeSlug(color) || "color";
    return `${safeColor}-${Date.now()}-${index}-${Math.random().toString(36).slice(2, 8)}`;
  }

  function buildCsrfHeaders() {
    const token = window.APP_CSRF?.token;
    const headerName = window.APP_CSRF?.headerName;
    return token && headerName ? { [headerName]: token } : {};
  }

  async function fetchJson(url) {
    const response = await fetch(url, { headers: { Accept: "application/json", ...buildCsrfHeaders() } });
    if (!response.ok) {
      throw new Error(`Request failed with status ${response.status}`);
    }
    return response.json();
  }

  function idleLabel(mode) {
    if (mode === "draft") {
      return "Lưu bản nháp";
    }
    return formType === "edit" ? "Cập nhật sản phẩm" : "Lưu sản phẩm";
  }

  function savingLabel(mode) {
    if (mode === "draft") {
      return "Đang lưu nháp...";
    }
    return formType === "edit" ? "Đang cập nhật..." : "Đang lưu...";
  }

  function syncActionLabels() {
    saveButtons.forEach((button) => {
      if (!button) {
        return;
      }
      const mode = button.dataset.saveMode || "publish";
      button.dataset.idleLabel = idleLabel(mode);
      button.textContent = button.dataset.idleLabel;
    });
  }

  function setSavingState(isSaving) {
    saveButtons.forEach((button) => {
      if (!button) {
        return;
      }
      button.disabled = isSaving;
      const mode = button.dataset.saveMode || "publish";
      button.textContent = isSaving
        ? savingLabel(mode)
        : (button.dataset.idleLabel || idleLabel(mode));
    });
    return;
    saveButtons.forEach((button) => {
      if (!button) {
        return;
      }
      button.disabled = isSaving;
      button.textContent = isSaving
        ? (formType === "edit" ? "Đang cập nhật..." : "Đang lưu...")
        : (formType === "edit" ? "Cập nhật sản phẩm" : "Lưu sản phẩm");
    });
  }

  function getColorState(colorName) {
    return state.colors.find((item) => item.colorName === colorName);
  }

  function activeUploadColor() {
    if (uploadColor?.value) {
      return uploadColor.value;
    }
    return state.colors[0]?.colorName || "";
  }

  function sortVariants(variants) {
    variants.sort((left, right) => {
      const leftValue = Number(left.size);
      const rightValue = Number(right.size);
      if (Number.isFinite(leftValue) && Number.isFinite(rightValue)) {
        return leftValue - rightValue;
      }
      return String(left.size).localeCompare(String(right.size));
    });
  }

  function totalVariantPairs() {
    return state.colors.reduce((sum, color) => sum + color.variants.length, 0);
  }

  function ensureMainImage(color) {
    if (!color || !color.images.length) {
      return;
    }
    if (!color.images.some((image) => image.isMainForColor)) {
      color.images[0].isMainForColor = true;
    }
  }

  function populateCategoryOptions(selectedCategoryId) {
    if (!productCategoryInput) {
      return;
    }

    productCategoryInput.innerHTML = state.categories
      .map((category) => {
        const selected = String(category.id) === String(selectedCategoryId ?? "");
        return `<option value="${category.id}" ${selected ? "selected" : ""}>${category.name}</option>`;
      })
      .join("");
  }

  function defaultImage() {
    return `${ctx()}/images/admin/products/air-max-dn8-hero.avif`;
  }

  function activeMainImage() {
    const activeColor = getColorState(activeUploadColor()) || state.colors[0];
    if (!activeColor || !activeColor.images.length) {
      return "";
    }
    return activeColor.images.find((image) => image.isMainForColor)?.path || activeColor.images[0].path;
  }

  function syncSelects() {
    if (sizeColor) {
      sizeColor.innerHTML = state.colors
        .map((color) => `<option value="${color.colorName}">${color.colorName}</option>`)
        .join("");
    }

    if (uploadColor) {
      uploadColor.innerHTML = state.colors
        .map((color) => `<option value="${color.colorName}">${color.colorName}</option>`)
        .join("");
    }
  }

  function setPreviewImage(imagePath, altText) {
    if (!imageFrame) {
      return;
    }
    imageFrame.src = resolveImagePath(imagePath || defaultImage());
    imageFrame.alt = altText || "Ảnh xem trước sản phẩm";
  }

  function updatePreview() {
    if (formTitle && formType === "edit" && productNameInput?.value.trim()) {
      formTitle.textContent = productNameInput.value.trim();
    }

    if (previewProductName) {
      previewProductName.textContent = productNameInput?.value.trim() || "Sản phẩm chưa có tên";
    }
    if (previewDescription) {
      previewDescription.textContent = productDescriptionInput?.value.trim() || "Chưa có mô tả sản phẩm.";
    }
    if (summaryCategory) {
      const selected = productCategoryInput?.selectedOptions?.[0]?.textContent;
      summaryCategory.textContent = selected || "Chưa chọn";
    }
    if (summaryPrice) {
      summaryPrice.textContent = regularPriceInput?.value.trim()
        ? window.AdminSuite.currency(Number(regularPriceInput.value))
        : "Chưa nhập";
    }
    if (summaryColors) {
      summaryColors.textContent = `${state.colors.length} màu`;
    }
    if (summaryVariants) {
      const variants = totalVariantPairs();
      summaryVariants.textContent = `${variants} biến thể`;
    }

    const statusText = productStatusInput?.selectedOptions?.[0]?.textContent || "Nháp";
    if (launchStatusBadge) {
      launchStatusBadge.textContent = statusText;
      launchStatusBadge.className = `badge ${window.AdminSuite.badgeClass(statusText)}`;
    }
    if (mobilePreviewState) {
      mobilePreviewState.textContent = statusText;
    }
    if (editStatusBadge) {
      editStatusBadge.textContent = statusText;
      editStatusBadge.className = `badge ${window.AdminSuite.badgeClass(statusText)}`;
    }
  }

  function updateReadiness() {
    const checks = [
      Boolean(productNameInput?.value.trim() && productDescriptionInput?.value.trim()),
      Boolean(regularPriceInput?.value.trim()),
      state.colors.length > 0 && totalVariantPairs() > 0,
      Boolean(productCategoryInput?.value && productStatusInput?.value)
    ];

    if (checklist) {
      checklist.querySelectorAll(".checklist-item").forEach((item, index) => {
        const indicator = item.querySelector(".check-indicator");
        if (!indicator) {
          return;
        }
        indicator.classList.toggle("is-done", checks[index]);
      });
    }

    if (stepMeter) {
      stepMeter.querySelectorAll(".step-card").forEach((item, index) => {
        item.classList.toggle("is-ready", checks[index]);
      });
    }

    const readyCount = checks.filter(Boolean).length;
    if (readinessPill) {
      readinessPill.textContent = `${readyCount} / 4 ready`;
    }
    if (mobileReadinessText) {
      mobileReadinessText.textContent = `${readyCount} / 4 ready`;
    }
  }

  function renderColors() {
    if (!colorList) {
      return;
    }

    colorList.innerHTML = state.colors.length
      ? state.colors.map((color) => `
          <span class="swatch">
            <span class="swatch-dot"></span>${color.colorName}
            <button class="swatch-remove" type="button" data-remove-color="${color.colorName}" aria-label="Remove ${color.colorName} colorway">&times;</button>
          </span>
        `).join("")
      : `<div class="empty-state">Chưa có màu sắc nào.</div>`;
  }

  function renderSizes() {
    if (!sizeContainer) {
      return;
    }

    sizeContainer.innerHTML = state.colors.length
      ? state.colors.map((color) => `
          <div class="inventory-item">
            <div>
              <strong>${color.colorName}</strong>
              <span>Size coverage</span>
            </div>
            <div class="chip-row">
              ${color.variants.map((variant) => `
                <span class="chip chip-with-action">
                  ${variant.size}
                  <button class="chip-remove" type="button" data-remove-size data-color="${color.colorName}" data-size="${variant.size}" aria-label="Remove size ${variant.size} from ${color.colorName}">&times;</button>
                </span>
              `).join("")}
            </div>
          </div>
        `).join("")
      : `<div class="empty-state">Thêm kích cỡ để tạo biến thể tồn kho.</div>`;
  }

  function renderUploads() {
    if (!uploadPreview) {
      return;
    }

    const color = getColorState(activeUploadColor());
    if (!color) {
      uploadPreview.innerHTML = `<div class="empty-state">Hãy thêm màu sắc trước rồi mới tải ảnh lên.</div>`;
      return;
    }

    const images = color.images;
    if (!images.length) {
      uploadPreview.innerHTML = `
        <div class="media-card">
          <div class="copy">
            <h4>${color.colorName}</h4>
            <p>Màu này chưa có ảnh nào.</p>
          </div>
        </div>
      `;
      return;
    }

    const activeIndex = Math.min(sliderIndexByColor[color.colorName] || 0, images.length - 1);
    const activeImage = images[activeIndex];
    const mainIndex = Math.max(images.findIndex((image) => image.isMainForColor), 0);

    uploadPreview.innerHTML = `
      <div class="media-card">
        <div class="copy">
          <h4>${color.colorName}</h4>
          <p>${images.length} ảnh đang gắn với màu này.</p>
          <div class="upload-slider" style="margin-top:12px;">
            <div class="upload-stage">
              <img src="${resolveImagePath(activeImage.path)}" alt="${color.colorName} preview">
              <div class="upload-stage-actions">
                <button class="slider-arrow" type="button" data-slider-action="prev" data-color="${color.colorName}" ${images.length <= 1 ? "disabled" : ""} aria-label="Ảnh trước">&#8249;</button>
                <button class="slider-arrow" type="button" data-slider-action="next" data-color="${color.colorName}" ${images.length <= 1 ? "disabled" : ""} aria-label="Ảnh sau">&#8250;</button>
              </div>
            </div>
            <div class="upload-meta-bar">
              <span class="main-image-pill">Ảnh chính ${mainIndex + 1}</span>
              <button class="btn btn-light" type="button" data-set-main data-color="${color.colorName}" data-index="${activeIndex}">Đặt làm ảnh chính</button>
            </div>
            <div class="upload-thumbs">
              ${images.map((image, index) => `
                <div class="upload-thumb-card ${index === activeIndex ? "is-active" : ""}">
                  <button class="upload-thumb ${index === activeIndex ? "is-active" : ""}" type="button" data-thumb-index="${index}" data-color="${color.colorName}" aria-label="${color.colorName} image ${index + 1}">
                    <img src="${resolveImagePath(image.path)}" alt="${color.colorName} ${index + 1}">
                  </button>
                  <button class="upload-thumb-remove" type="button" data-remove-image data-color="${color.colorName}" data-index="${index}" aria-label="Remove ${color.colorName} image ${index + 1}">&times;</button>
                </div>
              `).join("")}
            </div>
          </div>
        </div>
      </div>
    `;

    if (uploadDropzoneCopy) {
      uploadDropzoneCopy.textContent = `${images.length} ảnh cho màu ${color.colorName}. Ảnh chính: ${mainIndex + 1}.`;
    }
  }

  function renderStock() {
    if (!stockContainer) {
      return;
    }

    const rows = state.colors.flatMap((color) => color.variants.map((variant, index) => `
      <div class="inventory-item">
        <div>
          <strong>${color.colorName} / ${variant.size}</strong>
          <span>${variant.sku || buildVariantSku(productSkuInput?.value, color.colorName, variant.size, index)}</span>
        </div>
        <div class="inventory-actions">
          <input
            class="input"
            type="number"
            min="0"
            value="${variant.stock ?? 0}"
            data-stock-input
            data-color="${color.colorName}"
            data-size="${variant.size}"
            style="max-width:120px;"
          >
          <button class="btn btn-light btn-inline-remove" type="button" data-remove-size data-color="${color.colorName}" data-size="${variant.size}">Xóa</button>
        </div>
      </div>
    `));

    stockContainer.innerHTML = rows.length ? rows.join("") : `<div class="empty-state">Tồn kho biến thể sẽ xuất hiện khi đã có màu và kích cỡ.</div>`;
  }

  function refreshAll() {
    syncSelects();
    renderColors();
    renderSizes();
    renderUploads();
    renderStock();
    setPreviewImage(activeMainImage(), `${activeUploadColor() || "product"} main preview`);
    updatePreview();
    updateReadiness();
  }

  function handleUploadedFiles(fileList) {
    const color = getColorState(activeUploadColor());
    if (!color || !fileList?.length) {
      return;
    }

    const uploaded = Array.from(fileList)
      .filter((file) => file.type.startsWith("image/"))
      .map((file, index) => ({
        kind: "new",
        existingImageId: null,
        clientKey: buildClientKey(color.colorName, index),
        path: URL.createObjectURL(file),
        file,
        title: `${productNameInput?.value.trim() || "Product"} ${color.colorName} image ${color.images.length + index + 1}`,
        altText: `${productNameInput?.value.trim() || "Product"} ${color.colorName}`,
        orderIndex: color.images.length + index,
        isMainForColor: color.images.length === 0 && index === 0
      }));

    if (!uploaded.length) {
      return;
    }

    color.images.push(...uploaded);
    sliderIndexByColor[color.colorName] = color.images.length - uploaded.length;
    refreshAll();
    if (productImageInput) {
      productImageInput.value = "";
    }
  }

  function removeColor(colorName) {
    if (!window.confirm(`Xóa màu "${colorName}" cùng toàn bộ size, ảnh và tồn kho của màu này?`)) {
      return;
    }
    state.colors = state.colors.filter((color) => color.colorName !== colorName);
    delete sliderIndexByColor[colorName];
    refreshAll();
  }

  function removeSize(colorName, size) {
    if (!window.confirm(`Xóa size "${size}" khỏi màu "${colorName}"?`)) {
      return;
    }
    const color = getColorState(colorName);
    if (!color) {
      return;
    }
    color.variants = color.variants.filter((variant) => variant.size !== size);
    refreshAll();
  }

  function removeImage(colorName, index) {
    const color = getColorState(colorName);
    if (!color || index < 0 || index >= color.images.length) {
      return;
    }
    color.images.splice(index, 1);
    ensureMainImage(color);
    if ((sliderIndexByColor[colorName] || 0) >= color.images.length) {
      sliderIndexByColor[colorName] = Math.max(color.images.length - 1, 0);
    }
    refreshAll();
  }

  function buildProductRequest() {
    return {
      name: productNameInput?.value.trim() || "",
      description: productDescriptionInput?.value.trim() || "",
      type: productTypeInput?.value || "UNISEX",
      categoryId: Number(productCategoryInput?.value),
      productStatus: productStatusInput?.value || "ACTIVE",
      price: Number.parseFloat(regularPriceInput?.value.trim() || ""),
      salePrice: salePriceInput?.value.trim() ? Number.parseFloat(salePriceInput.value.trim()) : null,
      colors: state.colors.map((color, colorIndex) => ({
        colorName: color.colorName,
        hexCode: color.hexCode || null,
        displayOrder: colorIndex,
        images: color.images.map((image, imageIndex) => ({
          existingImageId: image.kind === "existing" ? image.existingImageId : null,
          clientKey: image.kind === "new" ? image.clientKey : null,
          title: image.title || `${productNameInput?.value.trim() || "Product"} ${color.colorName} image ${imageIndex + 1}`,
          altText: image.altText || `${productNameInput?.value.trim() || "Product"} ${color.colorName}`,
          orderIndex: imageIndex,
          isMainForColor: Boolean(image.isMainForColor)
        })),
        variants: color.variants.map((variant, variantIndex) => ({
          sku: variant.sku || buildVariantSku(productSkuInput?.value, color.colorName, variant.size, variantIndex),
          size: variant.size,
          stock: Number(variant.stock) || 0,
          active: variant.active !== false
        }))
      }))
    };
  }

  function validateBeforeSubmit() {
    if (!productNameInput?.value.trim()) {
      throw new Error("Tên sản phẩm là bắt buộc.");
    }
    if (!productDescriptionInput?.value.trim()) {
      throw new Error("Mô tả sản phẩm là bắt buộc.");
    }
    if (!regularPriceInput?.value.trim()) {
      throw new Error("Giá gốc là bắt buộc.");
    }
    if (!productCategoryInput?.value) {
      throw new Error("Hãy chọn danh mục hợp lệ trước khi lưu.");
    }
    if (state.colors.length === 0) {
      throw new Error("Hãy thêm ít nhất một màu trước khi lưu.");
    }

    state.colors.forEach((color) => {
      if (!color.images.length) {
        throw new Error(`Màu ${color.colorName} phải có ít nhất một ảnh.`);
      }
      if (!color.variants.length) {
        throw new Error(`Màu ${color.colorName} phải có ít nhất một kích cỡ.`);
      }
      if (!color.images.some((image) => image.isMainForColor)) {
        throw new Error(`Hãy chọn một ảnh chính cho màu ${color.colorName}.`);
      }
    });
  }

  async function submitProduct(saveMode) {
    const previousStatus = productStatusInput?.value;
    if (saveMode === "draft" && productStatusInput) {
      productStatusInput.value = "DRAFT";
      refreshAll();
    }
    validateBeforeSubmit();

    const formData = new FormData();
    formData.append("productData", new Blob([JSON.stringify(buildProductRequest())], { type: "application/json" }));

    state.colors.forEach((color) => {
      color.images
        .filter((image) => image.kind === "new" && image.file)
        .forEach((image) => {
          formData.append("files", image.file, image.file.name);
          formData.append("fileClientKeys", image.clientKey);
        });
    });

    const url = formType === "edit"
      ? `${ctx()}/admin/api/products/${productId}`
      : `${ctx()}/admin/api/products`;
    const method = formType === "edit" ? "PUT" : "POST";

    setSavingState(true);
    try {
      const response = await fetch(url, {
        method,
        headers: buildCsrfHeaders(),
        body: formData
      });

      if (!response.ok) {
        let message = formType === "edit"
          ? "Không thể cập nhật sản phẩm."
          : "Không thể lưu sản phẩm.";
        try {
          const errorBody = await response.json();
          message = errorBody.message || errorBody.error || message;
        } catch (error) {
          // Keep the generic error.
        }
        throw new Error(message);
      }

      await response.json();
      window.location.href = `${ctx()}/admin/product/list`;
    } catch (error) {
      if (saveMode === "draft" && productStatusInput && previousStatus) {
        productStatusInput.value = previousStatus;
        refreshAll();
      }
      throw error;
    } finally {
      setSavingState(false);
    }
  }

  function hydrateForm(product) {
    if (productNameInput) {
      productNameInput.value = product.name || "";
    }
    if (productDescriptionInput) {
      productDescriptionInput.value = product.description || "";
    }
    if (productTypeInput) {
      productTypeInput.value = product.type || "UNISEX";
    }
    if (productStatusInput) {
      productStatusInput.value = product.productStatus || "ACTIVE";
    }
    if (regularPriceInput) {
      regularPriceInput.value = product.price ?? "";
    }
    if (salePriceInput) {
      salePriceInput.value = product.salePrice ?? "";
    }

    populateCategoryOptions(product.categoryId);
    state.colors = (product.colors || []).map((color) => ({
      colorName: color.colorName,
      hexCode: color.hexCode,
      displayOrder: color.displayOrder,
      images: (color.images || []).map((image) => ({
        kind: "existing",
        existingImageId: image.existingImageId,
        clientKey: null,
        path: image.path,
        file: null,
        title: image.title,
        altText: image.altText,
        orderIndex: image.orderIndex,
        isMainForColor: Boolean(image.isMainForColor)
      })),
      variants: (color.variants || []).map((variant) => ({
        sku: variant.sku,
        size: variant.size,
        stock: variant.stock ?? 0,
        active: variant.active !== false
      }))
    }));

    state.colors.forEach((color) => {
      sortVariants(color.variants);
      sliderIndexByColor[color.colorName] = 0;
    });

    if (formTitle) {
      formTitle.textContent = product.name || "Chỉnh sửa sản phẩm";
    }
    if (formCopy) {
      formCopy.textContent = "Xem dữ liệu hiện có, điều chỉnh tồn kho biến thể và lưu lại toàn bộ thay đổi.";
    }
  }

  async function bootstrap() {
    state.categories = await fetchJson(`${ctx()}/admin/api/products/form-options`);
    populateCategoryOptions();

    if (formType === "edit") {
      if (!productId) {
        throw new Error("Thiếu product id cho chế độ chỉnh sửa.");
      }
      const product = await fetchJson(`${ctx()}/admin/api/products/${productId}`);
      hydrateForm(product);
    } else {
      if (formCopy) {
        formCopy.textContent = "Nhập dữ liệu thật cho sản phẩm mới và thiết lập tồn kho theo từng biến thể.";
      }
      setPreviewImage(defaultImage(), "Ảnh xem trước sản phẩm");
    }

    refreshAll();
  }

  [productNameInput, productDescriptionInput, regularPriceInput, salePriceInput, productSkuInput]
    .filter(Boolean)
    .forEach((field) => field.addEventListener("input", () => {
      state.colors.forEach((color) => {
        color.variants.forEach((variant, index) => {
          variant.sku = buildVariantSku(productSkuInput?.value, color.colorName, variant.size, index);
        });
      });
      refreshAll();
    }));

  [productCategoryInput, productStatusInput, productTypeInput]
    .filter(Boolean)
    .forEach((field) => field.addEventListener("change", refreshAll));

  saveButtons.forEach((button) => {
    button?.addEventListener("click", async (event) => {
      event.preventDefault();
      try {
        await submitProduct(button.dataset.saveMode || "publish");
      } catch (error) {
        alert(error.message || "Không thể lưu sản phẩm.");
      }
    });
  });

  addColorButton?.addEventListener("click", () => {
    const value = colorInput?.value.trim();
    if (!value || getColorState(value)) {
      return;
    }
    state.colors.push({
      colorName: value,
      hexCode: null,
      displayOrder: state.colors.length,
      images: [],
      variants: []
    });
    sliderIndexByColor[value] = 0;
    if (colorInput) {
      colorInput.value = "";
    }
    refreshAll();
  });

  colorList?.addEventListener("click", (event) => {
    const removeButton = event.target.closest("[data-remove-color]");
    if (!removeButton) {
      return;
    }
    removeColor(removeButton.dataset.removeColor);
  });

  addSizeButton?.addEventListener("click", () => {
    const color = getColorState(sizeColor?.value);
    const size = sizeInput?.value.trim();
    if (!color || !size) {
      return;
    }
    if (!color.variants.some((variant) => variant.size === size)) {
      color.variants.push({
        sku: buildVariantSku(productSkuInput?.value, color.colorName, size, color.variants.length),
        size,
        stock: 0,
        active: true
      });
      sortVariants(color.variants);
    }
    if (sizeInput) {
      sizeInput.value = "";
    }
    refreshAll();
  });

  sizeContainer?.addEventListener("click", (event) => {
    const removeButton = event.target.closest("[data-remove-size]");
    if (!removeButton) {
      return;
    }
    removeSize(removeButton.dataset.color, removeButton.dataset.size);
  });

  uploadColor?.addEventListener("change", refreshAll);

  uploadPreview?.addEventListener("click", (event) => {
    const thumb = event.target.closest("[data-thumb-index]");
    if (thumb) {
      sliderIndexByColor[thumb.dataset.color] = Number(thumb.dataset.thumbIndex);
      renderUploads();
      return;
    }

    const sliderButton = event.target.closest("[data-slider-action]");
    if (sliderButton) {
      const color = getColorState(sliderButton.dataset.color);
      if (!color) {
        return;
      }
      const current = sliderIndexByColor[color.colorName] || 0;
      sliderIndexByColor[color.colorName] = sliderButton.dataset.sliderAction === "next"
        ? (current + 1) % color.images.length
        : (current - 1 + color.images.length) % color.images.length;
      renderUploads();
      return;
    }

    const mainButton = event.target.closest("[data-set-main]");
    if (mainButton) {
      const color = getColorState(mainButton.dataset.color);
      if (!color) {
        return;
      }
      const index = Number(mainButton.dataset.index);
      color.images.forEach((image, imageIndex) => {
        image.isMainForColor = imageIndex === index;
      });
      refreshAll();
      return;
    }

    const removeButton = event.target.closest("[data-remove-image]");
    if (removeButton) {
      removeImage(removeButton.dataset.color, Number(removeButton.dataset.index));
    }
  });

  stockContainer?.addEventListener("input", (event) => {
    const input = event.target.closest("[data-stock-input]");
    if (!input) {
      return;
    }
    const color = getColorState(input.dataset.color);
    if (!color) {
      return;
    }
    const variant = color.variants.find((item) => item.size === input.dataset.size);
    if (variant) {
      variant.stock = Math.max(0, Number(input.value) || 0);
    }
  });

  stockContainer?.addEventListener("click", (event) => {
    const removeButton = event.target.closest("[data-remove-size]");
    if (!removeButton) {
      return;
    }
    removeSize(removeButton.dataset.color, removeButton.dataset.size);
  });

  if (uploadDropzone && productImageInput) {
    uploadDropzone.addEventListener("click", () => {
      if (activeUploadColor()) {
        productImageInput.click();
      }
    });
    uploadDropzone.addEventListener("keydown", (event) => {
      if ((event.key === "Enter" || event.key === " ") && activeUploadColor()) {
        event.preventDefault();
        productImageInput.click();
      }
    });
    ["dragenter", "dragover"].forEach((eventName) => {
      uploadDropzone.addEventListener(eventName, (event) => {
        event.preventDefault();
        if (activeUploadColor()) {
          uploadDropzone.classList.add("is-dragover");
        }
      });
    });
    ["dragleave", "dragend", "drop"].forEach((eventName) => {
      uploadDropzone.addEventListener(eventName, (event) => {
        event.preventDefault();
        uploadDropzone.classList.remove("is-dragover");
      });
    });
    uploadDropzone.addEventListener("drop", (event) => {
      if (activeUploadColor()) {
        handleUploadedFiles(event.dataTransfer.files);
      }
    });
    productImageInput.addEventListener("change", (event) => {
      handleUploadedFiles(event.target.files);
    });
  }

  if (saveLabel) {
    saveLabel.textContent = formType === "edit" ? "Cập nhật sản phẩm" : "Lưu sản phẩm";
  }

  syncActionLabels();

  bootstrap().catch((error) => {
    console.error("Failed to bootstrap product form", error);
    alert(error.message || "Không thể tải form sản phẩm.");
  });
})();
