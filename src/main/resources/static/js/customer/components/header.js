(function () {
  function syncHeaderOffset() {
    const header = document.getElementById("header");
    const headerTop = header ? header.querySelector(".header-top") : null;
    const navbar = header ? header.querySelector(".navbar") : null;
    const isMobileTopHidden = window.innerWidth <= 768;
    const headerTopHeight = headerTop && !isMobileTopHidden ? headerTop.offsetHeight : 0;
    const navHeight = navbar ? navbar.offsetHeight : 0;
    const visibleHeaderHeight = header ? header.offsetHeight : headerTopHeight + navHeight;

    document.documentElement.style.setProperty("--header-top-height", headerTopHeight + "px");
    document.documentElement.style.setProperty("--header-nav-height", navHeight + "px");
    document.documentElement.style.setProperty("--header-visible-offset", visibleHeaderHeight + "px");
  }

  function syncStickyState() {
    const header = document.getElementById("header");
    if (!header) {
      return;
    }

    header.classList.toggle("is-stuck", window.scrollY > 0);
  }

  function bindGlobalSearchFallback() {
    const form = document.getElementById("globalSearchForm");
    if (!form) {
      return;
    }

    const submitButton = form.querySelector(".search-icon-btn");
    const input = document.getElementById("searchInput");

    if (submitButton) {
      submitButton.addEventListener("click", function () {
        window.setTimeout(function () {
          if (!document.hidden) {
            form.requestSubmit ? form.requestSubmit() : form.submit();
          }
        }, 50);
      });
    }

    if (input) {
      input.addEventListener("keydown", function (event) {
        if (event.key !== "Enter") {
          return;
        }
        window.setTimeout(function () {
          if (!document.hidden) {
            form.requestSubmit ? form.requestSubmit() : form.submit();
          }
        }, 50);
      });
    }
  }

  function bindMegaDropdown() {
    var navbar = document.querySelector(".navbar");
    var megaDropdown = document.getElementById("megaDropdown");
    var navItems = document.querySelectorAll(".nav-item[data-menu]");
    var panels = document.querySelectorAll(".mega-dropdown-panel");

    if (!navbar || !megaDropdown || navItems.length === 0) {
      return;
    }

    // Disable on small screens
    function isDesktop() {
      return window.innerWidth > 1180;
    }

    var hideTimer = null;
    var activeMenu = null;

    function showMenu(menu) {
      if (!isDesktop()) return;
      clearTimeout(hideTimer);
      if (activeMenu === menu) return;

      activeMenu = menu;

      // Highlight nav item
      navItems.forEach(function (item) {
        item.classList.toggle("is-hovered", item.getAttribute("data-menu") === menu);
      });

      // Show corresponding panel
      panels.forEach(function (panel) {
        panel.classList.toggle("active", panel.getAttribute("data-menu") === menu);
      });

      megaDropdown.classList.add("active");
    }

    function scheduleHide() {
      clearTimeout(hideTimer);
      hideTimer = setTimeout(function () {
        activeMenu = null;
        navItems.forEach(function (item) {
          item.classList.remove("is-hovered");
        });
        panels.forEach(function (panel) {
          panel.classList.remove("active");
        });
        megaDropdown.classList.remove("active");
      }, 150);
    }

    function cancelHide() {
      clearTimeout(hideTimer);
    }

    // Hover on nav items
    navItems.forEach(function (item) {
      item.addEventListener("mouseenter", function () {
        var menu = item.getAttribute("data-menu");
        showMenu(menu);
      });
    });

    // Keep open while hovering mega dropdown itself
    megaDropdown.addEventListener("mouseenter", cancelHide);
    megaDropdown.addEventListener("mouseleave", scheduleHide);

    // Leave the whole navbar area -> close
    navbar.addEventListener("mouseleave", scheduleHide);
    navbar.addEventListener("mouseenter", function (e) {
      // If re-entering navbar but not on a nav-item, keep current dropdown if coming from dropdown
      // The mouseenter on nav-item will handle switching; this just prevents immediate close
    });

    // Also handle nav-center mouseleave more precisely: only hide if not entering megaDropdown
    var navCenter = document.querySelector(".nav-center");
    if (navCenter) {
      navCenter.addEventListener("mouseleave", function (e) {
        // Check if moving into megaDropdown
        var related = e.relatedTarget;
        if (related && (megaDropdown.contains(related) || navbar.contains(related))) {
          // Still inside interaction area - check if over a nav item
          return;
        }
        scheduleHide();
      });
    }
  }

  function bindPageShowReload() {
    window.addEventListener("pageshow", function (event) {
      const navigation = performance.getEntriesByType && performance.getEntriesByType("navigation");
      const backForward = navigation && navigation.length > 0 && navigation[0].type === "back_forward";
      if (event.persisted || backForward) {
        window.location.reload();
      }
    });
  }

  function init() {
    syncHeaderOffset();
    syncStickyState();
    bindGlobalSearchFallback();
    bindMegaDropdown();
    bindPageShowReload();
    window.addEventListener("load", syncHeaderOffset);
    window.addEventListener("resize", syncHeaderOffset, { passive: true });
    window.addEventListener("scroll", syncStickyState, { passive: true });

    if (typeof ResizeObserver === "function") {
      const header = document.getElementById("header");
      if (header) {
        const resizeObserver = new ResizeObserver(function () {
          syncHeaderOffset();
          syncStickyState();
        });
        resizeObserver.observe(header);
      }
    }
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
