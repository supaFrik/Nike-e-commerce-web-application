(function () {
  const baseEnv = window.APP_CTX || "";
  const toggleBtn = document.getElementById("sortToggleBtn");
  const menu = document.getElementById("sortMenu");
  const hiddenInput = document.getElementById("sortHiddenInput");
  const searchInput = document.getElementById("liveSearchInput");
  const productGrid = document.getElementById("productGrid");
  const resultsCount = document.getElementById("resultsCount");
  const paginationContainer = document.getElementById("paginationContainer");
  const statusEl = document.getElementById("liveSearchStatus");

  let currentPage = parseInt(new URLSearchParams(window.location.search).get("page") || "1", 10);
  let currentSort = hiddenInput ? hiddenInput.value : new URLSearchParams(window.location.search).get("sort") || "";
  let currentCategory = new URLSearchParams(window.location.search).get("category") || "";
  let lastQuery = searchInput ? searchInput.value.trim() : "";
  let abortCtrl = null;
  let debounceTimer = null;

  function formatPrice(value) {
    if (value == null) return "";
    try {
      return Number(value).toLocaleString("vi-VN");
    } catch (error) {
      return value;
    }
  }

  function setStatus(message) {
    if (statusEl) {
      statusEl.textContent = message;
    }
  }

  function clearStatus() {
    setStatus("");
  }

  function escapeHtml(value) {
    return (value || "").replace(/[&<>"]|'/g, function (char) {
      return {
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
      }[char];
    });
  }

  function resolveImageUrl(imageUrl) {
    if (!imageUrl) {
      return baseEnv + "/images/products/default-product.avif";
    }
    if (imageUrl.indexOf("http://") === 0 || imageUrl.indexOf("https://") === 0) {
      return imageUrl;
    }
    if (imageUrl.indexOf("uploads/products/") === 0) {
      return baseEnv + "/" + imageUrl;
    }
    if (imageUrl.indexOf("/") === 0) {
      return baseEnv + imageUrl;
    }
    return baseEnv + "/uploads/products/" + imageUrl;
  }

  function buildProductCard(product) {
    const hasSale = !!product.hasSale && product.salePrice != null && product.price != null;
    const imageUrl = resolveImageUrl(product.imageUrl);
    const priceHtml = hasSale
      ? '<span class="sale-price">' + formatPrice(product.salePrice) + '₫</span>' +
        ' <span class="old-price">' + formatPrice(product.price) + '₫</span>'
      : formatPrice(product.price) + "₫";

    let html = "";
    html += '<a class="product-card" data-id="' + product.id + '" href="' + baseEnv + "/product-detail?id=" + product.id + '">';
    html += '<div class="product-image-wrapper">';
    html += '<img src="' + imageUrl + '" alt="' + escapeHtml(product.name || "Product") + '" class="product-image" />';
    html += "</div>";
    html += '<div class="product-info">';
    if (hasSale) {
      html += '<p class="product-badge">Giảm giá</p>';
    }
    html += '<h3 class="product-name">' + escapeHtml(product.name || "") + "</h3>";
    html += '<p class="product-category">' + escapeHtml(product.categoryName || "") + "</p>";
    html += '<p class="product-price">' + priceHtml + "</p>";
    html += "</div>";
    html += "</a>";
    return html;
  }

  function buildPagination(totalPages, page) {
    if (!paginationContainer) return;
    if (totalPages <= 1) {
      paginationContainer.innerHTML = "";
      paginationContainer.style.display = "none";
      return;
    }

    paginationContainer.style.display = "flex";
    let html = "";
    for (let i = 1; i <= totalPages; i += 1) {
      const active = i === page;
      html += '<a href="#" data-page="' + i + '" class="page-link' + (active ? " active" : "") + '">' + i + "</a>";
    }
    paginationContainer.innerHTML = html;

    const links = paginationContainer.querySelectorAll("a.page-link");
    for (let i = 0; i < links.length; i += 1) {
      links[i].addEventListener("click", function (event) {
        event.preventDefault();
        const pageValue = parseInt(this.getAttribute("data-page"), 10);
        if (pageValue !== currentPage) {
          currentPage = pageValue;
          performSearch(false);
        }
      });
    }
  }

  function updateUrl(query) {
    const params = new URLSearchParams(window.location.search);
    if (query) params.set("q", query); else params.delete("q");
    if (currentSort) params.set("sort", currentSort); else params.delete("sort");
    if (currentCategory) params.set("category", currentCategory); else params.delete("category");
    params.set("page", String(currentPage));
    window.history.replaceState(null, "", window.location.pathname + "?" + params.toString());
  }

  function performSearch(isTyping) {
    if (!searchInput) return;

    const query = searchInput.value.trim();
    if (query === lastQuery && !isTyping) {
      return;
    }
    lastQuery = query;

    if (abortCtrl) {
      abortCtrl.abort();
    }
    abortCtrl = new AbortController();
    const controller = abortCtrl;

    setStatus("Searching...");
    if (productGrid) {
      productGrid.classList.add("loading");
    }

    const params = new URLSearchParams();
    if (query) params.append("q", query);
    if (currentCategory) params.append("category", currentCategory);
    if (currentSort) params.append("sort", currentSort);
    params.append("page", String(currentPage));
    params.append("pageSize", "24");

    fetch(baseEnv + "/api/products/search?" + params.toString(), { signal: controller.signal })
      .then(function (response) {
        if (!response.ok) {
          throw new Error("HTTP " + response.status);
        }
        return response.json();
      })
      .then(function (data) {
        if (controller.signal.aborted) return;

        clearStatus();
        const items = data.items || [];
        if (resultsCount) {
          resultsCount.textContent = data.total === 0
            ? "Không có kết quả"
            : "Hiển thị " + data.total + " kết quả";
        }

        if (productGrid) {
          productGrid.innerHTML = items.length === 0
            ? '<div class="no-products">Không tìm thấy sản phẩm phù hợp với tìm kiếm của bạn.</div>'
            : items.map(buildProductCard).join("");
          productGrid.classList.remove("loading");
        }

        buildPagination(data.totalPages || 1, data.page || 1);
        updateUrl(query);
      })
      .catch(function (error) {
        if (error.name === "AbortError") return;
        setStatus("Error searching");
        if (productGrid) {
          productGrid.classList.remove("loading");
        }
      });
  }

  function debouncedSearch() {
    if (debounceTimer) {
      clearTimeout(debounceTimer);
    }
    debounceTimer = setTimeout(function () {
      currentPage = 1;
      performSearch(false);
    }, 350);
    performSearch(true);
  }

  if (searchInput) {
    searchInput.addEventListener("input", debouncedSearch);
    searchInput.addEventListener("keydown", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        currentPage = 1;
        performSearch(false);
      }
    });
  }

  function applySort(value) {
    currentSort = value;
    currentPage = 1;
    if (hiddenInput) {
      hiddenInput.value = value;
    }
    performSearch(false);
  }

  const closeBtn = document.getElementById("closeSearchBtn");
  if (closeBtn) {
    closeBtn.addEventListener("click", function () {
      const currentUrl = window.location.href.split("#")[0];
      const referrer = document.referrer;
      if (referrer && referrer.indexOf(currentUrl) === -1) {
        window.history.back();
      } else {
        window.location.href = baseEnv + "/";
      }
    });
  }

  if (toggleBtn && menu) {
    function closeMenu() {
      menu.classList.remove("show");
      toggleBtn.setAttribute("aria-expanded", "false");
      toggleBtn.classList.remove("active");
    }

    function openMenu() {
      menu.classList.add("show");
      toggleBtn.setAttribute("aria-expanded", "true");
      toggleBtn.classList.add("active");
    }

    toggleBtn.addEventListener("click", function (event) {
      event.stopPropagation();
      if (menu.classList.contains("show")) {
        closeMenu();
      } else {
        openMenu();
      }
    });

    document.addEventListener("click", function (event) {
      if (!menu.contains(event.target) && event.target !== toggleBtn) {
        closeMenu();
      }
    });

    document.addEventListener("keydown", function (event) {
      if (event.key === "Escape") {
        closeMenu();
      }
    });

    const sortOptions = menu.querySelectorAll(".sort-option");
    for (let i = 0; i < sortOptions.length; i += 1) {
      sortOptions[i].addEventListener("click", function () {
        applySort(this.getAttribute("data-sort"));
        closeMenu();
      });
    }
  }
})();
