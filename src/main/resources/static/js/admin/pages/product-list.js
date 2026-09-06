(function () {
  const grid = document.getElementById("productGrid");
  const quickView = document.getElementById("quickView");
  const searchInput = document.getElementById("searchInventory");
  const totalCount = document.getElementById("inventoryTotal");
  const filterHost = document.getElementById("categoryFilters");
  const pagination = document.getElementById("pagination");
  let products = [];
  let activeCategory = "Tất cả";
  let selectedId = null;
  const PAGE_SIZE = 20;
  let currentPage = 1;
  const pages = (n) => Math.max(1, Math.ceil(n / PAGE_SIZE));
  const clampPage = (total) => { if (currentPage > total) currentPage = total; if (currentPage < 1) currentPage = 1; };

  function ctx() {
    return (window.APP_CTX || "").replace(/\/$/, "");
  }

  function buildCsrfHeaders() {
    const token = window.APP_CSRF?.token;
    const headerName = window.APP_CSRF?.headerName;
    return token && headerName ? { [headerName]: token } : {};
  }

  async function loadProducts(options = {}) {
    const cacheBust = options.cacheBust ? `?t=${Date.now()}` : "";
    const response = await fetch(`${ctx()}/admin/api/page-data/products${cacheBust}`, {
      headers: { Accept: "application/json" },
      cache: "no-store"
    });
    if (!response.ok) {
      throw new Error("Không thể tải dữ liệu kho sản phẩm.");
    }
    products = await response.json();
    const all = filteredProducts();
    const totalPages = pages(all.length);
    clampPage(totalPages);
    const preferredSelectedId = options.preferredSelectedId ?? selectedId;
    selectedId = all.some((product) => product.id === preferredSelectedId)
      ? preferredSelectedId
      : (all.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE)[0]?.id || all[0]?.id || null);
    renderCategoryFilters();
    renderGrid();
  }

  function filteredProducts() {
    const query = (searchInput?.value || "").trim().toLowerCase();
    return products.filter((product) => {
      const matchesCategory = activeCategory === "Tất cả" || product.categoryName === activeCategory;
      const haystack = [product.name, product.categoryName, product.description, product.statusLabel].join(" ").toLowerCase();
      return matchesCategory && haystack.includes(query);
    });
  }

  function renderCategoryFilters() {
    if (!filterHost) {
      return;
    }
    const categories = ["Tất cả", ...new Set(products.map((product) => product.categoryName).filter(Boolean))];
    filterHost.innerHTML = categories.map((category) => `
      <button class="chip ${category === activeCategory ? "is-active" : ""}" type="button" data-category="${category}">${category}</button>
    `).join("");

    filterHost.querySelectorAll("[data-category]").forEach((button) => {
      button.addEventListener("click", () => {
        activeCategory = button.dataset.category;
        currentPage = 1;
        renderCategoryFilters();
        renderGrid();
      });
    });
  }

  async function removeProduct(productId) {
    const product = products.find((item) => item.id === productId);
    const name = product?.name || `#${productId}`;
    if (!window.confirm(`Xóa sản phẩm "${name}" khỏi kho? Hành động này không thể hoàn tác.`)) {
      return;
    }

    products = products.filter((item) => item.id !== productId);
    const allAfter = filteredProducts();
    clampPage(pages(allAfter.length));
    if (selectedId === productId) {
      const pageSlice = allAfter.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE);
      selectedId = pageSlice[0]?.id || allAfter[0]?.id || null;
    }
    renderCategoryFilters();
    renderGrid();

    const response = await fetch(`${ctx()}/admin/api/products/${productId}`, {
      method: "DELETE",
      headers: buildCsrfHeaders()
    });

    if (!response.ok) {
      let message = "Không thể xóa sản phẩm.";
      try {
        const errorBody = await response.json();
        message = errorBody.message || errorBody.error || message;
      } catch (error) {
        // Keep generic message.
      }
      await loadProducts({ preferredSelectedId: productId, cacheBust: true });
      throw new Error(message);
    }

    await loadProducts({ preferredSelectedId: selectedId, cacheBust: true });
  }

  function renderQuickView(product) {
    if (!quickView) {
      return;
    }
    if (!product) {
      quickView.innerHTML = `<div class="empty-state">Không có sản phẩm phù hợp với bộ lọc hiện tại.</div>`;
      return;
    }

    quickView.innerHTML = `
      <div class="panel-header">
        <div>
          <h4>Xem nhanh</h4>
          <p>Dữ liệu sản phẩm thật lấy trực tiếp từ hệ thống quản trị.</p>
        </div>
        <div class="card-actions">
          <button class="btn btn-light" type="button" data-quick-delete="${product.id}">Xóa</button>
          <a class="btn btn-dark" href="${window.AdminSuite.route(`admin/product/edit/${product.id}`)}">Chỉnh sửa</a>
        </div>
      </div>
      <div class="media-card">
        <img src="${window.AdminSuite.asset(product.imageUrl || "images/admin/products/air-max-dn8.avif")}" alt="${product.name}">
        <div class="copy">
          <div class="eyebrow">${product.categoryName}</div>
          <h4>${product.name}</h4>
          <p>${product.description || "Chưa có mô tả."}</p>
        </div>
      </div>
      <div class="field-grid" style="margin-top: 16px;">
        <div class="field-span-6">
          <label>Giá gốc</label>
          <input class="input" type="text" value="${window.AdminSuite.currency(product.price)}" readonly>
        </div>
        <div class="field-span-6">
          <label>Giá bán</label>
          <input class="input" type="text" value="${window.AdminSuite.currency(product.salePrice || product.price)}" readonly>
        </div>
        <div class="field-span-6">
          <label>Tồn kho</label>
          <input class="input" type="text" value="${product.stock}" readonly>
        </div>
        <div class="field-span-6">
          <label>Trạng thái</label>
          <input class="input" type="text" value="${product.statusLabel}" readonly>
        </div>
      </div>
      <div style="margin-top: 16px;">
        <label class="eyebrow" style="margin: 0 0 8px;">Màu sắc</label>
        <div class="swatch-row">
          ${product.colorNames.map((color) => `<span class="swatch"><span class="swatch-dot"></span>${color}</span>`).join("")}
        </div>
      </div>
      <div style="margin-top: 16px;">
        <label class="eyebrow" style="margin: 0 0 8px;">Kích cỡ</label>
        <div class="chip-row">
          ${product.sizes.map((size) => `<span class="chip">${size}</span>`).join("")}
        </div>
      </div>
    `;

    quickView.querySelector("[data-quick-delete]")?.addEventListener("click", async () => {
      try {
        await removeProduct(product.id);
      } catch (error) {
        alert(error.message || "Không thể xóa sản phẩm.");
      }
    });
  }

  function visiblePages(totalPages) {
    if (totalPages <= 7) return Array.from({ length: totalPages }, (_, i) => i + 1);
    if (currentPage <= 4) return [1, 2, 3, 4, 5, "…", totalPages];
    if (currentPage >= totalPages - 3) return [1, "…", totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1, totalPages];
    return [1, "…", currentPage - 1, currentPage, currentPage + 1, "…", totalPages];
  }

  function renderPagination(total, totalPages) {
    if (!pagination) return;
    if (total <= PAGE_SIZE) {
      pagination.innerHTML = "";
      pagination.hidden = true;
      return;
    }
    pagination.hidden = false;
    const chevPrev = '<svg width="14" height="14" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M10.5 12 6 8l4.5-4"/></svg>';
    const chevNext = '<svg width="14" height="14" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5.5 4 10 8l-4.5 4"/></svg>';
    const pageBtns = visiblePages(totalPages).map((p) => {
      if (p === "…") return '<span class="pg-ellipsis" aria-hidden="true">…</span>';
      const active = p === currentPage;
      return `<button class="pg-num${active ? " is-active" : ""}" type="button" data-page="${p}"${active ? ' aria-current="page"' : ""}>${p}</button>`;
    }).join("");
    pagination.innerHTML = ''
      + `<button class="pg-nav" type="button" data-page="prev"${currentPage === 1 ? " disabled" : ""} aria-label="Previous page">${chevPrev}<span>Previous</span></button>`
      + `<span class="pg-pages">${pageBtns}</span>`
      + `<button class="pg-nav" type="button" data-page="next"${currentPage === totalPages ? " disabled" : ""} aria-label="Next page"><span>Next</span>${chevNext}</button>`;
  }

  function renderGrid() {
    const all = filteredProducts();
    const totalPages = pages(all.length);
    clampPage(totalPages);
    const start = (currentPage - 1) * PAGE_SIZE;
    const visible = all.slice(start, start + PAGE_SIZE);

    if (totalCount) {
      totalCount.textContent = `${all.length} sản phẩm`;
    }
    if (!grid) {
      return;
    }
    if (!all.length) {
      grid.innerHTML = `<div class="empty-state">Không tìm thấy sản phẩm phù hợp.</div>`;
      if (pagination) { pagination.innerHTML = ""; pagination.hidden = true; }
      renderQuickView(null);
      return;
    }
    if (all.length && !all.some((product) => product.id === selectedId)) {
      selectedId = visible[0]?.id || all[0].id;
    }

    grid.innerHTML = visible.map((product) => `
      <article class="product-card ${product.id === selectedId ? "is-selected" : ""}" data-id="${product.id}">
        <div class="product-thumb">
          <img src="${window.AdminSuite.asset(product.imageUrl || "images/admin/products/air-max-dn8.avif")}" alt="${product.name}">
          <span class="overlay-label">${product.categoryName}</span>
        </div>
        <div class="product-body">
          <div class="panel-header" style="margin-bottom: 10px;">
            <div>
              <h4 class="product-title">${product.name}</h4>
              <p class="product-copy">${product.description || "Chưa có mô tả."}</p>
            </div>
            <span class="badge ${window.AdminSuite.badgeClass(product.statusLabel)}">${product.statusLabel}</span>
          </div>
          <div class="product-meta">
            <span>${window.AdminSuite.currency(product.salePrice || product.price)}</span>
            <span>${product.stock} tồn kho</span>
          </div>
          <div class="card-actions" style="margin-top: 12px;">
            <button class="btn btn-light" type="button" data-open-id="${product.id}">Xem</button>
            <button class="btn btn-light" type="button" data-remove-id="${product.id}">Xóa</button>
            <a class="btn btn-dark" href="${window.AdminSuite.route(`admin/product/edit/${product.id}`)}">Sửa</a>
          </div>
        </div>
      </article>
    `).join("");

    grid.querySelectorAll("[data-id]").forEach((card) => {
      card.addEventListener("click", () => {
        selectedId = Number(card.dataset.id);
        renderGrid();
      });
    });

    grid.querySelectorAll("[data-open-id]").forEach((button) => {
      button.addEventListener("click", (event) => {
        event.stopPropagation();
        selectedId = Number(button.dataset.openId);
        renderGrid();
      });
    });

    grid.querySelectorAll("[data-remove-id]").forEach((button) => {
      button.addEventListener("click", async (event) => {
        event.stopPropagation();
        try {
          await removeProduct(Number(button.dataset.removeId));
        } catch (error) {
          alert(error.message || "Không thể xóa sản phẩm.");
        }
      });
    });

    renderPagination(all.length, totalPages);
    const selectedProduct = all.find((product) => product.id === selectedId) || visible[0] || null;
    renderQuickView(selectedProduct);
  }

  searchInput?.addEventListener("input", () => {
    currentPage = 1;
    renderGrid();
  });

  pagination?.addEventListener("click", (event) => {
    const btn = event.target.closest("[data-page]");
    if (!btn || btn.disabled) return;
    const v = btn.dataset.page;
    if (v === "prev" && currentPage > 1) currentPage--;
    else if (v === "next") currentPage++;
    else if (/^\d+$/.test(v)) { const n = Number(v); if (n === currentPage) return; currentPage = n; }
    else return;
    renderGrid();
  });

  loadProducts().catch((error) => {
    if (grid) {
      grid.innerHTML = `<div class="empty-state">${error.message}</div>`;
    }
    if (quickView) {
      quickView.innerHTML = `<div class="empty-state">Không thể tải xem nhanh sản phẩm.</div>`;
    }
  });
})();
