(function () {
  const page = document.body.dataset.page;
  const adminMain = document.querySelector(".admin-main");
  const topbar = document.querySelector(".topbar");
  if (adminMain && !adminMain.querySelector(".promo-strip")) {
    const promo = document.createElement("div");
    promo.className = "promo-strip";
    promo.textContent = "Members: Free shipping on launch-week orders and same-day queue priority";
    adminMain.insertBefore(promo, adminMain.firstChild);
  }

  function syncTopbarState() {
    if (!topbar) {
      return;
    }
    document.body.classList.toggle("topbar-condensed", window.scrollY > 36);
  }

  syncTopbarState();
  window.addEventListener("scroll", syncTopbarState, { passive: true });

  document.querySelectorAll("[data-nav]").forEach((link) => {
    if (link.dataset.nav === page) {
      link.classList.add("active");
      link.setAttribute("aria-current", "page");
    }
  });

  const yearEl = document.getElementById("suiteYear");
  if (yearEl) {
    yearEl.textContent = new Date().getFullYear();
  }

  window.AdminSuite = {
    contextPath: window.APP_CTX || "",
    route(path) {
      const normalized = String(path || "").replace(/^\/+/, "");
      return `${window.APP_CTX || ""}/${normalized}`;
    },
    asset(path) {
      const normalized = String(path || "").replace(/^\/+/, "");
      return `${window.APP_CTX || ""}/${normalized}`;
    },
    currency(amount) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
        maximumFractionDigits: 0
      }).format(amount);
    },
    badgeClass(status) {
      const normalized = (status || "").toLowerCase();
      if (normalized.includes("active") || normalized.includes("shipped") || normalized.includes("delivered")) {
        return "success";
      }
      if (normalized.includes("review") || normalized.includes("pending") || normalized.includes("processing") || normalized.includes("low")) {
        return "warn";
      }
      if (normalized.includes("return") || normalized.includes("refund")) {
        return "danger";
      }
      return "neutral";
    }
  };
})();
