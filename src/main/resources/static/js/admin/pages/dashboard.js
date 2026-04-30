(function () {
  const heroStats = document.getElementById("heroStats");
  const actionList = document.getElementById("priorityActions");
  const summary = document.getElementById("dashboardSummary");

  function ctx() {
    return (window.APP_CTX || "").replace(/\/$/, "");
  }

  async function loadDashboard() {
    const response = await fetch(`${ctx()}/admin/api/page-data/dashboard`, {
      headers: { Accept: "application/json" }
    });
    if (!response.ok) {
      throw new Error("Không thể tải số liệu dashboard.");
    }
    return response.json();
  }

  function renderActions() {
    if (!actionList) {
      return;
    }
    actionList.innerHTML = [
      ["Quản lý kho sản phẩm", "Kiểm tra tồn kho, giá bán và trạng thái sản phẩm.", "admin/product/list"],
      ["Xử lý đơn hàng", "Theo dõi đơn mới, đang xử lý và đã giao.", "admin/order/list"],
      ["Quản lý danh mục", "Kiểm tra danh mục đang dùng và số sản phẩm liên kết.", "admin/category/list"]
    ].map(([title, copy, href]) => `
      <div class="list-item">
        <div class="panel-header">
          <div>
            <h4>${title}</h4>
            <p>${copy}</p>
          </div>
          <a class="btn btn-light" href="${window.AdminSuite.route(href)}">Mở</a>
        </div>
      </div>
    `).join("");
  }

  function renderDashboard(data) {
    const cards = [
      ["Sản phẩm", data.productCount],
      ["Danh mục", data.categoryCount],
      ["Đơn hàng", data.orderCount],
      ["Sản phẩm sắp hết", data.lowStockProductCount]
    ];

    if (heroStats) {
      heroStats.innerHTML = cards.map(([label, value]) => `
        <div class="kpi">
          <span>${label}</span>
          <strong>${value}</strong>
        </div>
      `).join("");
    }

    if (summary) {
      summary.innerHTML = [
        ["Tổng doanh thu", window.AdminSuite.currency(data.totalRevenue || 0)],
        ["Tổng sản phẩm", data.productCount],
        ["Tổng danh mục", data.categoryCount],
        ["Tổng đơn hàng", data.orderCount]
      ].map(([label, value]) => `
        <div class="kpi">
          <span>${label}</span>
          <strong>${value}</strong>
        </div>
      `).join("");
    }
  }

  renderActions();
  loadDashboard()
    .then(renderDashboard)
    .catch((error) => {
      if (summary) {
        summary.innerHTML = `<div class="empty-state">${error.message}</div>`;
      }
    });
})();
