(function () {
  const page = document.body.dataset.page;
  const tableBody = document.getElementById("categoryRows");
  const search = document.getElementById("categorySearch");
  const saveButton = document.getElementById("saveCategoryButton");
  const nameInput = document.getElementById("catName");
  const slugInput = document.getElementById("catSlug");
  const formMessage = document.getElementById("categoryFormMessage");
  let categories = [];

  function ctx() {
    return (window.APP_CTX || "").replace(/\/$/, "");
  }

  function csrfHeaders() {
    const csrf = window.APP_CSRF || {};
    if (!csrf.token || !csrf.headerName) {
      return {};
    }
    return { [csrf.headerName]: csrf.token };
  }

  function setFormMessage(message, isError) {
    if (!formMessage) {
      return;
    }

    formMessage.style.display = message ? "block" : "none";
    formMessage.textContent = message || "";
    formMessage.className = isError ? "error-message" : "success-message";
  }

  function slugify(value) {
    return String(value || "")
      .trim()
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, "-")
      .replace(/^-+|-+$/g, "");
  }

  async function loadCategories() {
    const response = await fetch(`${ctx()}/admin/api/page-data/categories`, {
      headers: { Accept: "application/json" }
    });
    if (!response.ok) {
      throw new Error("Không thể tải dữ liệu danh mục.");
    }
    categories = await response.json();
    renderRows("");
  }

  function renderRows(query) {
    if (!tableBody) {
      return;
    }

    const filtered = categories.filter((category) =>
      category.name.toLowerCase().includes((query || "").toLowerCase())
    );

    tableBody.innerHTML = filtered.length ? filtered.map((category) => `
      <tr>
        <td>${category.id}</td>
        <td>${category.name}</td>
        <td>${category.productCount}</td>
      </tr>
    `).join("") : `
      <tr>
        <td colspan="3">
          <div class="empty-state">Không tìm thấy danh mục phù hợp.</div>
        </td>
      </tr>
    `;
  }

  search?.addEventListener("input", () => renderRows(search.value));
  nameInput?.addEventListener("input", () => {
    if (slugInput) {
      slugInput.value = slugify(nameInput.value);
    }
    setFormMessage("", true);
  });

  saveButton?.addEventListener("click", async () => {
    const name = (nameInput?.value || "").trim();
    if (!name) {
      setFormMessage("Category name is required.", true);
      nameInput?.focus();
      return;
    }

    saveButton.disabled = true;
    setFormMessage("", true);

    try {
      const response = await fetch(`${ctx()}/admin/api/categories`, {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          ...csrfHeaders()
        },
        body: JSON.stringify({ name })
      });

      if (!response.ok) {
        const errorBody = await response.json().catch(() => null);
        throw new Error(errorBody?.message || "Failed to create category.");
      }

      const payload = await response.json();
      setFormMessage("Category created successfully.", false);
      window.setTimeout(() => {
        window.location.href = `${ctx()}${payload.redirectUrl || "/admin/category/list"}`;
      }, 300);
    } catch (error) {
      setFormMessage(error.message || "Failed to create category.", true);
    } finally {
      saveButton.disabled = false;
    }
  });

  if (page !== "category-list" || !tableBody) {
    return;
  }

  loadCategories().catch((error) => {
    if (tableBody) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="3">
            <div class="empty-state">${error.message}</div>
          </td>
        </tr>
      `;
    }
  });
})();
