(function () {
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

  function bindHeaderActions() {
    document.querySelectorAll("[data-mobile-menu-toggle]").forEach(function (button) {
      button.addEventListener("click", function () {
        if (typeof window.toggleMobileMenu === "function") {
          window.toggleMobileMenu();
        }
      });
    });
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
    bindGlobalSearchFallback();
    bindHeaderActions();
    bindPageShowReload();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
