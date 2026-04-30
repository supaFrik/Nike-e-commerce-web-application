(function () {
  function toggleCart(forceOpen) {
    const cartSidebar = document.getElementById("cartSidebar");
    if (!cartSidebar) {
      return;
    }

    const shouldOpen = typeof forceOpen === "boolean"
      ? forceOpen
      : !(cartSidebar.classList.contains("active") || cartSidebar.classList.contains("open"));

    cartSidebar.classList.toggle("active", shouldOpen);
    cartSidebar.classList.toggle("open", shouldOpen);
    document.body.classList.toggle("cart-open", shouldOpen);
  }

  function toggleMobileFilters(forceOpen) {
    const sidebar = document.querySelector(".sidebar");
    if (!sidebar) {
      return;
    }

    const shouldOpen = typeof forceOpen === "boolean"
      ? forceOpen
      : !sidebar.classList.contains("mobile-open");

    sidebar.classList.toggle("mobile-open", shouldOpen);
    document.body.classList.toggle("filters-open", shouldOpen);
  }

  document.addEventListener("click", function (event) {
    const cartToggle = event.target.closest("[data-cart-toggle]");
    if (cartToggle) {
      const force = cartToggle.dataset.cartToggle === "open" ? true : cartToggle.dataset.cartToggle === "close" ? false : undefined;
      toggleCart(force);
      return;
    }

    const filterToggle = event.target.closest("[data-mobile-filters-toggle]");
    if (filterToggle) {
      const force = filterToggle.dataset.mobileFiltersToggle === "open" ? true : filterToggle.dataset.mobileFiltersToggle === "close" ? false : undefined;
      toggleMobileFilters(force);
    }
  });
})();
