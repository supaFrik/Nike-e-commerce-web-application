(function () {
  function qs(selector, root) {
    return (root || document).querySelector(selector);
  }

  function qsa(selector, root) {
    return Array.from((root || document).querySelectorAll(selector));
  }

  function parsePrice(value) {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : 0;
  }

  function formatCount(count) {
    return " " + count;
  }

  function matchesPriceRange(price, range) {
    switch (range) {
      case "under-2000000":
        return price < 2000000;
      case "2000000-4000000":
        return price >= 2000000 && price <= 4000000;
      case "above-4000000":
        return price > 4000000;
      default:
        return true;
    }
  }

  function initSortDropdown() {
    const toggle = qs("#sort-toggle");
    const options = qs("#sort-options");
    const arrow = qs("#sort-arrow");
    const wrapper = qs("#sortByBtn");
    const form = qs("#sort-form");
    const input = qs("#sort-input");

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

      function setExpanded(expanded) {
        group.setAttribute("aria-expanded", expanded ? "true" : "false");
        trigger.setAttribute("aria-expanded", expanded ? "true" : "false");
        panel.hidden = !expanded;
      }

      setExpanded(false);

      trigger.addEventListener("click", function () {
        const expanded = trigger.getAttribute("aria-expanded") === "true";
        setExpanded(!expanded);
      });

      trigger.addEventListener("keydown", function (event) {
        if (event.key === "Enter" || event.key === " ") {
          event.preventDefault();
          const expanded = trigger.getAttribute("aria-expanded") === "true";
          setExpanded(!expanded);
        }
      });
    });
  }

  function initSidebarToggle() {
    const toggle = qs(".hide-filters-btn");
    const label = qs(".hide-filters-label", toggle);
    const listing = qs(".product-listing");

    if (!toggle || !label || !listing) {
      return;
    }

    toggle.addEventListener("click", function () {
      const collapsed = listing.classList.toggle("filters-collapsed");
      label.textContent = collapsed ? "Show Filters" : "Hide Filters";
    });
  }

  function initClientFilters() {
    const cards = qsa("[data-product-card='true']");
    const emptyState = qs("#client-filter-empty");
    const titleCount = qs("#page-title-count");
    const priceInputs = qsa("input[name='priceRange']");
    const saleOnlyInput = qs("#saleOnlyFilter");

    if (!cards.length || !titleCount || !priceInputs.length) {
      return;
    }

    function getActivePriceRange() {
      const selected = priceInputs.find(function (input) {
        return input.checked;
      });
      return selected ? selected.value : "all";
    }

    function applyFilters() {
      const activePriceRange = getActivePriceRange();
      const saleOnly = saleOnlyInput ? saleOnlyInput.checked : false;
      let visibleCount = 0;

      cards.forEach(function (card) {
        const price = parsePrice(card.dataset.price);
        const hasSale = String(card.dataset.hasSale).toLowerCase() === "true";
        const visible = matchesPriceRange(price, activePriceRange) && (!saleOnly || hasSale);

        card.hidden = !visible;
        if (visible) {
          visibleCount += 1;
        }
      });

      titleCount.textContent = formatCount(visibleCount);
      if (emptyState) {
        emptyState.hidden = visibleCount > 0;
      }
    }

    priceInputs.forEach(function (input) {
      input.addEventListener("change", applyFilters);
    });

    if (saleOnlyInput) {
      saleOnlyInput.addEventListener("change", applyFilters);
    }

    applyFilters();
  }

  function init() {
    initSortDropdown();
    initFilterSections();
    initSidebarToggle();
    initClientFilters();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
