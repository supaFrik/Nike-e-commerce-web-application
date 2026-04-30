(function () {
  const tableBody = document.getElementById("orderRows");
  const tabs = document.querySelectorAll("[data-status]");
  const searchInput = document.getElementById("orderSearch");
  const kpis = document.getElementById("orderKpis");
  const selectedOrderCard = document.getElementById("selectedOrderCard");
  const selectedOrderBadge = document.getElementById("selectedOrderBadge");
  const priorityList = document.getElementById("priorityList");
  const orderCountPill = document.getElementById("orderCountPill");
  let orders = [];
  let activeStatus = "Tất cả";
  let selectedOrderId = null;

  function ctx() {
    return (window.APP_CTX || "").replace(/\/$/, "");
  }

  async function loadOrders() {
    const response = await fetch(`${ctx()}/admin/api/page-data/orders`, {
      headers: { Accept: "application/json" }
    });
    if (!response.ok) {
      throw new Error("Không thể tải dữ liệu đơn hàng.");
    }
    orders = await response.json();
    selectedOrderId = orders[0]?.id || null;
    renderKpis();
    renderRows();
  }

  function filteredOrders() {
    const query = (searchInput?.value || "").trim().toLowerCase();
    return orders.filter((order) => {
      const matchesStatus = activeStatus === "Tất cả" || order.statusLabel === activeStatus;
      const haystack = `${order.id} ${order.customerName} ${order.recipientName} ${order.statusLabel} ${order.destinationLabel}`.toLowerCase();
      return matchesStatus && haystack.includes(query);
    });
  }

  function renderSelectedOrder(order) {
    if (!selectedOrderCard) {
      return;
    }
    if (!order) {
      selectedOrderCard.innerHTML = `<div class="empty-state">Không có đơn hàng phù hợp với bộ lọc hiện tại.</div>`;
      return;
    }

    if (selectedOrderBadge) {
      selectedOrderBadge.textContent = order.statusLabel;
      selectedOrderBadge.className = `badge ${window.AdminSuite.badgeClass(order.statusLabel)}`;
    }

    selectedOrderCard.innerHTML = `
      <div class="detail-stack">
        <div class="detail-row">
          <div>
            <span>Mã đơn</span>
            <strong>#${order.id}</strong>
          </div>
          <div>
            <span>Tổng tiền</span>
            <strong>${window.AdminSuite.currency(order.total)}</strong>
          </div>
        </div>
        <div class="detail-row">
          <div>
            <span>Tài khoản</span>
            <strong>${order.customerName}</strong>
          </div>
          <div>
            <span>Người nhận</span>
            <strong>${order.recipientName || "Chưa có"}</strong>
          </div>
        </div>
        <div class="detail-row">
          <div>
            <span>Số điện thoại</span>
            <strong>${order.phone || "Chưa có"}</strong>
          </div>
          <div>
            <span>Điểm đến</span>
            <strong>${order.destinationLabel || "Chưa có"}</strong>
          </div>
        </div>
        <div class="detail-row">
          <div>
            <span>Phương thức giao</span>
            <strong>${order.shippingMethodLabel}</strong>
          </div>
          <div>
            <span>Thanh toán</span>
            <strong>${order.paymentMethodLabel}</strong>
          </div>
        </div>
      </div>
    `;
  }

  function renderPriorityList(visible) {
    if (!priorityList) {
      return;
    }
    const priorities = visible
      .filter((order) => ["Chờ thanh toán", "Đang xử lý"].includes(order.statusLabel))
      .slice(0, 3);

    priorityList.innerHTML = priorities.length
      ? priorities.map((order) => `
        <div class="priority-item">
          <strong>#${order.id} · ${order.recipientName || order.customerName}</strong>
          <span>${order.statusLabel} / ${order.destinationLabel || "Chưa có điểm đến"}.</span>
        </div>
      `).join("")
      : `<div class="priority-item"><strong>Không có đơn ưu tiên trong bộ lọc hiện tại</strong><span>Danh sách hiện tại không có đơn cần xử lý gấp.</span></div>`;
  }

  function renderRows() {
    const visible = filteredOrders();
    if (orderCountPill) {
      orderCountPill.textContent = `${visible.length} đơn`;
    }

    if (!tableBody) {
      return;
    }

    if (!visible.length) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="8"><div class="empty-state">Không tìm thấy đơn hàng phù hợp.</div></td>
        </tr>
      `;
      renderSelectedOrder(null);
      return;
    }

    if (!visible.some((order) => order.id === selectedOrderId)) {
      selectedOrderId = visible[0].id;
    }

    tableBody.innerHTML = visible.map((order) => `
      <tr class="${order.id === selectedOrderId ? "is-selected" : ""}" data-order-id="${order.id}">
        <td>#${order.id}</td>
        <td>${order.createdAtLabel}</td>
        <td>${order.customerName}</td>
        <td><span class="badge ${window.AdminSuite.badgeClass(order.statusLabel)}">${order.statusLabel}</span></td>
        <td>${window.AdminSuite.currency(order.total)}</td>
        <td>${order.shippingMethodLabel}</td>
        <td>${order.itemCount}</td>
        <td>${order.destinationLabel || "Chưa có"}</td>
      </tr>
    `).join("");

    tableBody.querySelectorAll("[data-order-id]").forEach((row) => {
      row.addEventListener("click", () => {
        selectedOrderId = Number(row.dataset.orderId);
        renderRows();
      });
    });

    renderPriorityList(visible);
    renderSelectedOrder(visible.find((order) => order.id === selectedOrderId));
  }

  function renderKpis() {
    if (!kpis) {
      return;
    }
    const processing = orders.filter((item) => item.statusLabel === "Đang xử lý").length;
    const waiting = orders.filter((item) => item.statusLabel === "Chờ thanh toán").length;
    const delivered = orders.filter((item) => item.statusLabel === "Đã giao").length;

    kpis.innerHTML = [
      ["Tổng đơn", String(orders.length), "Số đơn đang có trong hệ thống."],
      ["Chờ thanh toán", String(waiting), "Các đơn chưa hoàn tất thanh toán."],
      ["Đang xử lý", String(processing), "Các đơn đang đi qua luồng vận hành."],
      ["Đã giao", String(delivered), "Các đơn đã giao thành công."]
    ].map(([label, value, copy]) => `
      <article class="stat">
        <div class="stat-label">${label}</div>
        <div class="stat-value">${value}</div>
        <div class="stat-help">${copy}</div>
      </article>
    `).join("");
  }

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      activeStatus = tab.dataset.status;
      tabs.forEach((item) => item.classList.toggle("is-active", item === tab));
      renderRows();
    });
  });

  searchInput?.addEventListener("input", renderRows);

  loadOrders().catch((error) => {
    if (tableBody) {
      tableBody.innerHTML = `<tr><td colspan="8"><div class="empty-state">${error.message}</div></td></tr>`;
    }
  });
})();
