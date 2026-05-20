// Mobile Navigation Utility
// Handles mobile menu sidebar functionality

const MOBILE_NAV_BREAKPOINT = 1180;

document.addEventListener("DOMContentLoaded", function () {
  initializeMobileNav();
});

function initializeMobileNav() {
  const mobileMenuBtn = document.querySelector(".mobile-menu-btn");
  const mobileMenuOverlay = document.getElementById("mobileMenuOverlay");
  const mobileMenuContent = document.querySelector(".mobile-menu-content");
  const toggleButtons = document.querySelectorAll("[data-mobile-menu-toggle]");
  const mobileSearchBtn = document.querySelector(".mobile-search-btn");
  const globalSearchForm = document.getElementById("globalSearchForm");
  const mobileMenuLinks = mobileMenuOverlay.querySelectorAll("a[href]");
  const body = document.body;

  if (!mobileMenuBtn || !mobileMenuOverlay || !mobileMenuContent) {
    return;
  }

  function handleEscapeKey(event) {
    if (event.key === "Escape") {
      closeMobileMenu();
    }
  }

  function handleClickOutside(event) {
    if (!mobileMenuContent.contains(event.target) && !mobileMenuBtn.contains(event.target)) {
      closeMobileMenu();
    }
  }

  function preventBodyScroll() {
    const scrollY = window.scrollY;
    body.style.position = "fixed";
    body.style.top = `-${scrollY}px`;
    body.style.width = "100%";
  }

  function restoreBodyScroll() {
    const scrollY = body.style.top;
    body.style.position = "";
    body.style.top = "";
    body.style.width = "";
    window.scrollTo(0, parseInt(scrollY || "0", 10) * -1);
  }

  function openMobileMenu() {
    if (window.innerWidth > MOBILE_NAV_BREAKPOINT) {
      return;
    }

    mobileMenuOverlay.classList.add("active");
    mobileMenuBtn.classList.add("active");
    body.classList.add("menu-open");
    toggleButtons.forEach(function (button) {
      button.setAttribute("aria-expanded", "true");
    });
    preventBodyScroll();

    document.addEventListener("keydown", handleEscapeKey);
    setTimeout(function () {
      document.addEventListener("click", handleClickOutside);
    }, 100);
  }

  function closeMobileMenu() {
    const wasOpen = mobileMenuOverlay.classList.contains("active");

    mobileMenuOverlay.classList.remove("active");
    mobileMenuBtn.classList.remove("active");
    body.classList.remove("menu-open");
    toggleButtons.forEach(function (button) {
      button.setAttribute("aria-expanded", "false");
    });

    document.removeEventListener("keydown", handleEscapeKey);
    document.removeEventListener("click", handleClickOutside);

    if (wasOpen) {
      restoreBodyScroll();
    }
  }

  window.toggleMobileMenu = function () {
    const isOpen = mobileMenuOverlay.classList.contains("active");
    if (isOpen) {
      closeMobileMenu();
    } else {
      openMobileMenu();
    }
  };

  toggleButtons.forEach(function (button) {
    button.addEventListener("click", function (event) {
      event.preventDefault();
      event.stopPropagation();
      window.toggleMobileMenu();
    });
  });

  if (mobileSearchBtn && globalSearchForm) {
    mobileSearchBtn.addEventListener("click", function (event) {
      event.preventDefault();
      closeMobileMenu();

      const searchInput = globalSearchForm.querySelector('input[name="q"]');
      if (searchInput) {
        searchInput.focus();
        searchInput.scrollIntoView({ block: "nearest", inline: "nearest" });
      } else {
        globalSearchForm.submit();
      }
    });
  }

  mobileMenuLinks.forEach(function (link) {
    link.addEventListener("click", function (event) {
      const href = (link.getAttribute("href") || "").trim();

      if (!href || href === "#") {
        event.preventDefault();
        return;
      }

      window.setTimeout(function () {
        closeMobileMenu();
      }, 0);
    });
  });

  let resizeTimeout;
  window.addEventListener("resize", function () {
    clearTimeout(resizeTimeout);
    resizeTimeout = setTimeout(function () {
      if (window.innerWidth > MOBILE_NAV_BREAKPOINT) {
        closeMobileMenu();
      }
    }, 150);
  });

  document.addEventListener("openMobileMenu", openMobileMenu);
  document.addEventListener("closeMobileMenu", closeMobileMenu);
}

// Export functions for external use
window.openMobileMenu = function () {
  document.dispatchEvent(new CustomEvent("openMobileMenu"));
};

window.closeMobileMenu = function () {
  document.dispatchEvent(new CustomEvent("closeMobileMenu"));
};
