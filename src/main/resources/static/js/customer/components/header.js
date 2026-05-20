(function () {
  function syncHeaderOffset() {
    const header = document.getElementById("header");
    const headerTop = header ? header.querySelector(".header-top") : null;
    const navbar = header ? header.querySelector(".navbar") : null;
    const isMobileTopHidden = window.innerWidth <= 768;
    const headerTopHeight = headerTop && !isMobileTopHidden ? headerTop.offsetHeight : 0;
    const navHeight = navbar ? navbar.offsetHeight : 0;

    document.documentElement.style.setProperty("--header-top-height", headerTopHeight + "px");
    document.documentElement.style.setProperty("--header-nav-height", navHeight + "px");
    document.documentElement.style.setProperty("--header-visible-offset", (headerTopHeight + navHeight) + "px");
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
    bindGlobalSearchFallback();
    bindPageShowReload();
    window.addEventListener("resize", syncHeaderOffset);

    if (typeof ResizeObserver === "function") {
      const header = document.getElementById("header");
      if (header) {
        const resizeObserver = new ResizeObserver(syncHeaderOffset);
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
