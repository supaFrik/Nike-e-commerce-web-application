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
