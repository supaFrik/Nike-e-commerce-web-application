(function () {
  const tableBody = document.getElementById("categoryRows");
  const search = document.getElementById("categorySearch");
  let categories = [];

  function ctx() {
    return (window.APP_CTX || "").replace(/\/$/, "");
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

  if (!tableBody) {
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
