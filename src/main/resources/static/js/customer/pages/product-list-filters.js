(function () {
  function qs(selector, root) {
    return (root || document).querySelector(selector);
  }

  function qsa(selector, root) {
    return Array.from((root || document).querySelectorAll(selector));
  }

  function syncStickyMetrics() {
    const productHeader = qs(".product-header");
    const stickyHeight = productHeader ? productHeader.offsetHeight : 0;
    document.documentElement.style.setProperty("--product-header-height", stickyHeight + "px");
  }

  function initSortDropdown() {
    const toggle = qs("#sort-toggle");
    const options = qs("#sort-options");
    const arrow = qs("#sort-arrow");
    const wrapper = qs("#sortByBtn");
    const form = qs("#sort-form");
    const input = qs("#sort-input");
    const catalogSortInput = qs("#catalog-sort-input");

    if (!toggle || !options || !wrapper || !form || !input) {
      return;
    }

    function setOpen(open) {
      options.classList.toggle("show", open);
      wrapper.classList.toggle("expanded", open);
      toggle.setAttribute("aria-expanded", open ? "true" : "false");
      if (arrow) {
        arrow.style.transform = open ? "rotate(180deg)" : "rotate(0deg)";
      }
    }

    toggle.addEventListener("click", function (event) {
      event.stopPropagation();
      setOpen(!options.classList.contains("show"));
    });

    options.addEventListener("click", function (event) {
      const option = event.target.closest(".sort-option");
      if (!option) {
        return;
      }

      options.querySelectorAll(".sort-option").forEach(function (item) {
        item.classList.remove("active");
        item.setAttribute("aria-selected", "false");
      });

      option.classList.add("active");
      option.setAttribute("aria-selected", "true");
      input.value = option.dataset.value || "newest";
      if (catalogSortInput) {
        catalogSortInput.value = input.value;
      }
      setOpen(false);
      form.submit();
    });

    document.addEventListener("click", function (event) {
      if (!wrapper.contains(event.target)) {
        setOpen(false);
      }
    });
  }

  function initFilterSections() {
    qsa(".filters-group__close").forEach(function (group) {
      const trigger = qs(".trigger-content", group);
      const panel = qs("[data-filter-panel]", group);

      if (!trigger || !panel) {
        return;
      }

      function animatePanel(open) {
        const durationMs = 220;

        if (open) {
          panel.hidden = false;
          panel.style.overflow = "hidden";
          panel.style.height = "0px";
          panel.style.opacity = "0";
          panel.offsetHeight;

          panel.style.transition = "height " + durationMs + "ms cubic-bezier(0.2, 0, 0, 1), opacity " + durationMs + "ms ease";
          panel.style.height = panel.scrollHeight + "px";
          panel.style.opacity = "1";

          window.setTimeout(function () {
            panel.style.transition = "";
            panel.style.height = "auto";
            panel.style.opacity = "";
            panel.style.overflow = "";
          }, durationMs);
          return;
        }

        panel.style.overflow = "hidden";
        panel.style.height = panel.scrollHeight + "px";
        panel.style.opacity = "1";
        panel.offsetHeight;

        panel.style.transition = "height " + durationMs + "ms cubic-bezier(0.4, 0, 0.2, 1), opacity " + durationMs + "ms ease";
        panel.style.height = "0px";
        panel.style.opacity = "0";

        window.setTimeout(function () {
          panel.hidden = true;
          panel.style.transition = "";
          panel.style.height = "";
          panel.style.opacity = "";
          panel.style.overflow = "";
        }, durationMs);
      }

      function setExpanded(expanded, instant) {
        group.setAttribute("aria-expanded", expanded ? "true" : "false");
        trigger.setAttribute("aria-expanded", expanded ? "true" : "false");

        if (instant) {
          panel.hidden = !expanded;
          panel.style.height = expanded ? "auto" : "0px";
          panel.style.opacity = expanded ? "1" : "0";
          panel.style.transition = "";
          return;
        }

        animatePanel(expanded);
      }

      setExpanded(false, true);

      trigger.addEventListener("click", function () {
        const expanded = trigger.getAttribute("aria-expanded") === "true";
        setExpanded(!expanded, false);
      });

      trigger.addEventListener("keydown", function (event) {
        if (event.key === "Enter" || event.key === " ") {
          event.preventDefault();
          const expanded = trigger.getAttribute("aria-expanded") === "true";
          setExpanded(!expanded, false);
        }
      });
    });
  }

  function initSidebarToggle() {
    const toggle = qs(".hide-filters-btn");
    const label = qs(".hide-filters-label", toggle);
    const listing = qs(".product-listing");
    const sidebar = qs(".sidebar", listing);

    if (!toggle || !label || !listing || !sidebar) {
      return;
    }

    toggle.addEventListener("click", function () {
      const durationMs = 320;
      const isCollapsed = listing.classList.contains("filters-collapsed");

      if (isCollapsed) {
        listing.classList.remove("filters-collapsed");
        listing.classList.remove("filters-collapsing");
        listing.classList.add("filters-expanding");
        label.textContent = "Ẩn bộ lọc";

        window.requestAnimationFrame(function () {
          listing.classList.remove("filters-expanding");
        });
        return;
      }

      listing.classList.remove("filters-expanding");
      listing.classList.add("filters-collapsing");
      label.textContent = "Hiện bộ lọc";

      window.setTimeout(function () {
        listing.classList.remove("filters-collapsing");
        listing.classList.add("filters-collapsed");
      }, durationMs);
    });
  }

  function initFilterCounts() {
    qsa(".filters-group__close").forEach(function (group) {
      const counter = qs(".filter-group_count", group);
      const inputs = qsa("input[type='checkbox']:checked, input[type='radio']:checked", group);

      if (!counter) {
        return;
      }

      counter.textContent = inputs.length > 0 ? "(" + inputs.length + ")" : "";
    });
  }

  function initFilterForm() {
    const form = qs("#catalog-filter-form");

    if (!form) {
      return;
    }

    form.addEventListener("change", function (event) {
      const target = event.target;
      if (!target || !target.name) {
        return;
      }

      initFilterCounts();
      form.submit();
    });

    initFilterCounts();
  }

  function initCategoryLinks() {
    const links = qsa("[data-category-link='true']");

    if (!links.length) {
      return;
    }

    const currentParams = new URLSearchParams(window.location.search);

    links.forEach(function (link) {
      const url = new URL(link.href, window.location.origin);
      const params = new URLSearchParams(currentParams.toString());
      const categoryId = link.dataset.categoryId;

      if (categoryId) {
        params.set("categoryId", categoryId);
      } else {
        params.delete("categoryId");
      }

      if (!params.get("sort")) {
        params.set("sort", "newest");
      }

      link.href = url.pathname + (params.toString() ? "?" + params.toString() : "");
    });
  }

  function initSportsToggle() {
    const button = qs("[data-sports-toggle]");
    const extraOptions = qsa(".filter-option--more");

    if (!button || !extraOptions.length) {
      return;
    }

    function setExpanded(expanded) {
      extraOptions.forEach(function (option) {
        option.hidden = !expanded;
      });

      button.setAttribute("data-sports-toggle", expanded ? "true" : "false");
      button.textContent = expanded ? "-Less" : "+More";
    }

    const hasSelectedExtraOption = extraOptions.some(function (option) {
      const input = option.querySelector("input");
      return input && input.checked;
    });

    setExpanded(hasSelectedExtraOption);

    button.addEventListener("click", function () {
      const expanded = button.getAttribute("data-sports-toggle") === "true";
      setExpanded(!expanded);
    });
  }

  function initMobileFilters() {
    const sidebar = qs(".sidebar");
    const openButton = qs("[data-mobile-filters-toggle='open']");
    const closeButton = qs("[data-mobile-filters-toggle='close']");

    if (!sidebar || !openButton || !closeButton) {
      return;
    }

    function setOpen(open) {
      sidebar.classList.toggle("mobile-open", open);
      document.body.classList.toggle("mobile-filters-open", open);
    }

    openButton.addEventListener("click", function () {
      setOpen(true);
    });

    closeButton.addEventListener("click", function () {
      setOpen(false);
    });
  }

  function init() {
    syncStickyMetrics();
    initSortDropdown();
    initFilterSections();
    initSidebarToggle();
    initFilterForm();
    initCategoryLinks();
    initSportsToggle();
    initMobileFilters();
    window.addEventListener("resize", syncStickyMetrics);
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
