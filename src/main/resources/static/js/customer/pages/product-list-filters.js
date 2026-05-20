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
    const filterChips = qsa(".mobile-filter-chip[data-mobile-filter-target]");
    const categoryTabs = qsa(".mobile-category-tab");
    const hub = qs("[data-mobile-filter-hub]");
    const hubBackdrop = qs("[data-mobile-hub-backdrop]");
    const hubContent = qs("[data-mobile-hub-content]");
    const hubApplyButton = qs("[data-mobile-hub-apply]");
    const hubCloseButton = qs("[data-mobile-hub-close]");
    const sheet = qs("[data-mobile-filter-sheet]");
    const backdrop = qs("[data-mobile-sheet-backdrop]");
    const sheetTitle = qs("[data-mobile-sheet-title]");
    const sheetContent = qs("[data-mobile-sheet-content]");
    const applyButton = qs("[data-mobile-sheet-apply]");
    const closeSheetButton = qs("[data-mobile-sheet-close]");
    const sortForm = qs("#sort-form");
    const sortInput = qs("#sort-input");
    const catalogSortInput = qs("#catalog-sort-input");
    const catalogForm = qs("#catalog-filter-form");

    if (!sidebar || !openButton || !hub || !hubBackdrop || !hubContent || !hubApplyButton || !hubCloseButton || !sheet || !backdrop || !sheetTitle || !sheetContent || !applyButton || !closeSheetButton || !catalogForm) {
      return;
    }

    let activeSheetKey = null;
    let pendingState = null;
    let hubState = null;
    const sheetFooter = applyButton.parentElement;

    const hubGroups = ["gender", "sale", "size", "colour", "height", "width", "sports"];

    function isMobileViewport() {
      return window.matchMedia("(max-width: 768px)").matches;
    }

    function cloneSelectedValues(inputs) {
      return inputs.filter(function (input) {
        return input.checked;
      }).map(function (input) {
        return input.value;
      });
    }

    function getGroup(groupKey) {
      return qs("[data-mobile-filter-key='" + groupKey + "']");
    }

    function getGroupInputs(groupKey) {
      const group = getGroup(groupKey);
      return group ? qsa("input[name]", group) : [];
    }

    function arraysEqual(left, right) {
      if (left.length !== right.length) {
        return false;
      }

      return left.every(function (value, index) {
        return value === right[index];
      });
    }

    function getFilterTitle(groupKey) {
      const titles = {
        all: "Filters",
        category: "Choose Category",
        sort: "Sort by",
        gender: "Filter by Gender",
        sale: "Filter by Sale",
        size: "Filter by Size",
        colour: "Filter by Colour",
        sports: "Filter by Sports"
      };

      return titles[groupKey] || "Filters";
    }

    function getCurrentState(groupKey) {
      if (groupKey === "category") {
        const activeTab = qs(".mobile-category-tab.active");
        return {
          value: activeTab ? (activeTab.dataset.categoryId || "") : ""
        };
      }

      if (groupKey === "sort") {
        return {
          value: sortInput ? sortInput.value || "newest" : "newest"
        };
      }

      const inputs = getGroupInputs(groupKey);
      return {
        values: cloneSelectedValues(inputs)
      };
    }

    function getCurrentHubState() {
      const filterValues = {};

      hubGroups.forEach(function (groupKey) {
        filterValues[groupKey] = cloneSelectedValues(getGroupInputs(groupKey));
      });

      return {
        sort: sortInput ? sortInput.value || "newest" : "newest",
        filters: filterValues
      };
    }

    function hasPendingChanges() {
      if (!activeSheetKey || !pendingState) {
        return false;
      }

      const currentState = getCurrentState(activeSheetKey);

      if (activeSheetKey === "category" || activeSheetKey === "sort") {
        return pendingState.value !== currentState.value;
      }

      return !arraysEqual([].concat(pendingState.values || []).sort(), [].concat(currentState.values || []).sort());
    }

    function updateApplyState() {
      applyButton.disabled = !hasPendingChanges();
    }

    function arraysMatch(left, right) {
      return arraysEqual([].concat(left || []).sort(), [].concat(right || []).sort());
    }

    function hasHubChanges() {
      if (!hubState) {
        return false;
      }

      const currentState = getCurrentHubState();

      if (hubState.sort !== currentState.sort) {
        return true;
      }

      return hubGroups.some(function (groupKey) {
        return !arraysMatch(hubState.filters[groupKey], currentState.filters[groupKey]);
      });
    }

    function updateHubApplyState() {
      hubApplyButton.disabled = !hasHubChanges();
    }

    function closeSheet() {
      activeSheetKey = null;
      pendingState = null;
      sheet.hidden = true;
      backdrop.hidden = true;
      sheet.setAttribute("aria-hidden", "true");
      document.body.classList.remove("mobile-filters-open");
      sheetContent.innerHTML = "";
      updateApplyState();
    }

    function closeHub() {
      hubState = null;
      hub.hidden = true;
      hubBackdrop.hidden = true;
      hub.setAttribute("aria-hidden", "true");
      document.body.classList.remove("mobile-filters-open");
      hubContent.innerHTML = "";
      updateHubApplyState();
    }

    function openSheet(groupKey, nextState) {
      closeHub();
      activeSheetKey = groupKey;
      pendingState = nextState || getCurrentState(groupKey);
      renderSheet(groupKey);
      sheet.hidden = false;
      backdrop.hidden = false;
      sheet.setAttribute("aria-hidden", "false");
      document.body.classList.add("mobile-filters-open");
      updateApplyState();
    }

    function openHub() {
      closeSheet();
      hubState = getCurrentHubState();
      renderHub();
      hub.hidden = false;
      hubBackdrop.hidden = false;
      hub.setAttribute("aria-hidden", "false");
      document.body.classList.add("mobile-filters-open");
      updateHubApplyState();
    }

    function renderCategorySheet() {
      const selectedValue = pendingState.value || "";
      const items = categoryTabs.map(function (tab) {
        const categoryId = tab.dataset.categoryId || "";
        const checked = categoryId === selectedValue ? " checked" : "";
        return [
          "<label class='mobile-filter-sheet__option'>",
          "<input type='radio' name='mobile-sheet-category' value='" + categoryId + "'" + checked + ">",
          "<span>" + tab.textContent.trim() + "</span>",
          "</label>"
        ].join("");
      });

      sheetContent.innerHTML = "<div class='mobile-filter-sheet__options'>" + items.join("") + "</div>";

      qsa("input[name='mobile-sheet-category']", sheetContent).forEach(function (input) {
        input.addEventListener("change", function () {
          pendingState.value = input.value;
          updateApplyState();
        });
      });
    }

    function renderSortSheet() {
      const options = qsa(".sort-option", sortForm).map(function (option) {
        return {
          value: option.dataset.value || "newest",
          label: option.textContent.trim()
        };
      });

      const selectedValue = pendingState.value || "newest";
      sheetContent.innerHTML = "<div class='mobile-filter-sheet__options'>" + options.map(function (option) {
        const checked = option.value === selectedValue ? " checked" : "";
        return [
          "<label class='mobile-filter-sheet__option'>",
          "<input type='radio' name='mobile-sheet-sort' value='" + option.value + "'" + checked + ">",
          "<span>" + option.label + "</span>",
          "</label>"
        ].join("");
      }).join("") + "</div>";

      qsa("input[name='mobile-sheet-sort']", sheetContent).forEach(function (input) {
        input.addEventListener("change", function () {
          pendingState.value = input.value;
          updateApplyState();
        });
      });
    }

    function renderFilterGroupSheet(groupKey) {
      const inputs = getGroupInputs(groupKey);
      const selectedValues = [].concat(pendingState.values || []);

      sheetContent.innerHTML = "<div class='mobile-filter-sheet__options'>" + inputs.map(function (sourceInput, index) {
        const optionType = sourceInput.type === "radio" ? "radio" : "checkbox";
        const optionName = "mobile-sheet-" + groupKey + (optionType === "radio" ? "" : "-" + index);
        const checked = selectedValues.indexOf(sourceInput.value) !== -1 ? " checked" : "";
        const sourceLabel = sourceInput.closest("label");
        const labelText = sourceLabel ? sourceLabel.textContent.trim() : sourceInput.value;
        return [
          "<label class='mobile-filter-sheet__option'>",
          "<input type='" + optionType + "' data-sheet-value='" + sourceInput.value + "' name='" + optionName + "'" + checked + ">",
          "<span>" + labelText + "</span>",
          "</label>"
        ].join("");
      }).join("") + "</div>";

      qsa("input[data-sheet-value]", sheetContent).forEach(function (input) {
        input.addEventListener("change", function () {
          pendingState.values = qsa("input[data-sheet-value]", sheetContent).filter(function (item) {
            return item.checked;
          }).map(function (item) {
            return item.getAttribute("data-sheet-value");
          });
          updateApplyState();
        });
      });
    }

    function renderSheet(groupKey) {
      sheetTitle.textContent = getFilterTitle(groupKey);
      sheetFooter.hidden = false;

      if (groupKey === "category") {
        renderCategorySheet();
        return;
      }

      if (groupKey === "sort") {
        renderSortSheet();
        return;
      }

      renderFilterGroupSheet(groupKey);
    }

    function renderHubSortSection() {
      const options = qsa(".sort-option", sortForm).map(function (option) {
        return {
          value: option.dataset.value || "newest",
          label: option.textContent.trim()
        };
      });

      return [
        "<section class='mobile-filter-hub__section'>",
        "<h3 class='mobile-filter-hub__section-title'>Sort By</h3>",
        "<div class='mobile-filter-hub__options'>",
        options.map(function (option) {
          const checked = option.value === hubState.sort ? " checked" : "";
          return [
            "<label class='mobile-filter-hub__option'>",
            "<input type='radio' name='mobile-hub-sort' value='" + option.value + "'" + checked + ">",
            "<span>" + option.label + "</span>",
            "</label>"
          ].join("");
        }).join(""),
        "</div>",
        "</section>"
      ].join("");
    }

    function renderHubFilterSection(groupKey) {
      const group = getGroup(groupKey);
      const labelNode = group ? qs(".trigger-content__label", group) : null;
      const title = labelNode ? labelNode.childNodes[0].textContent.trim() : getFilterTitle(groupKey).replace("Filter by ", "");
      const inputs = getGroupInputs(groupKey);
      const selectedValues = [].concat((hubState.filters[groupKey] || []));

      return [
        "<section class='mobile-filter-hub__section' data-mobile-hub-group='" + groupKey + "'>",
        "<h3 class='mobile-filter-hub__section-title'>" + title + "</h3>",
        "<div class='mobile-filter-hub__options'>",
        inputs.map(function (sourceInput, index) {
          const optionType = sourceInput.type === "radio" ? "radio" : "checkbox";
          const optionName = "mobile-hub-" + groupKey + (optionType === "radio" ? "" : "-" + index);
          const checked = selectedValues.indexOf(sourceInput.value) !== -1 ? " checked" : "";
          const sourceLabel = sourceInput.closest("label");
          const labelText = sourceLabel ? sourceLabel.textContent.trim() : sourceInput.value;
          return [
            "<label class='mobile-filter-hub__option'>",
            "<input type='" + optionType + "' data-hub-group='" + groupKey + "' data-hub-value='" + sourceInput.value + "' name='" + optionName + "'" + checked + ">",
            "<span>" + labelText + "</span>",
            "</label>"
          ].join("");
        }).join(""),
        "</div>",
        "</section>"
      ].join("");
    }

    function bindHubEvents() {
      qsa("input[name='mobile-hub-sort']", hubContent).forEach(function (input) {
        input.addEventListener("change", function () {
          hubState.sort = input.value;
          updateHubApplyState();
        });
      });

      hubGroups.forEach(function (groupKey) {
        qsa("[data-hub-group='" + groupKey + "']", hubContent).forEach(function (input) {
          input.addEventListener("change", function () {
            hubState.filters[groupKey] = qsa("[data-hub-group='" + groupKey + "']", hubContent).filter(function (item) {
              return item.checked;
            }).map(function (item) {
              return item.getAttribute("data-hub-value");
            });
            updateHubApplyState();
          });
        });
      });
    }

    function renderHub() {
      hubContent.innerHTML = [
        renderHubSortSection(),
        hubGroups.map(function (groupKey) {
          return renderHubFilterSection(groupKey);
        }).join("")
      ].join("");

      bindHubEvents();
    }

    function syncCategorySelection(categoryId) {
      const currentParams = new URLSearchParams(window.location.search);

      if (categoryId) {
        currentParams.set("categoryId", categoryId);
      } else {
        currentParams.delete("categoryId");
      }

      if (!currentParams.get("sort")) {
        currentParams.set("sort", "newest");
      }

      window.location.search = currentParams.toString();
    }

    function syncSortSelection(sortValue) {
      if (sortInput) {
        sortInput.value = sortValue;
      }

      if (catalogSortInput) {
        catalogSortInput.value = sortValue;
      }

      sortForm.submit();
    }

    function syncFilterSelection(groupKey) {
      const selectedValues = [].concat(pendingState.values || []);

      getGroupInputs(groupKey).forEach(function (input) {
        input.checked = selectedValues.indexOf(input.value) !== -1;
      });

      initFilterCounts();
      catalogForm.submit();
    }

    function applyHub() {
      if (!hubState) {
        return;
      }

      if (sortInput) {
        sortInput.value = hubState.sort;
      }

      if (catalogSortInput) {
        catalogSortInput.value = hubState.sort;
      }

      hubGroups.forEach(function (groupKey) {
        const selectedValues = [].concat(hubState.filters[groupKey] || []);
        getGroupInputs(groupKey).forEach(function (input) {
          input.checked = selectedValues.indexOf(input.value) !== -1;
        });
      });

      initFilterCounts();
      catalogForm.submit();
    }

    function applySheet() {
      if (!activeSheetKey || !pendingState) {
        return;
      }

      if (activeSheetKey === "category") {
        syncCategorySelection(pendingState.value || "");
        return;
      }

      if (activeSheetKey === "sort") {
        syncSortSelection(pendingState.value || "newest");
        return;
      }

      syncFilterSelection(activeSheetKey);
    }

    openButton.addEventListener("click", function (event) {
      if (!isMobileViewport()) {
        return;
      }

      event.preventDefault();
      openHub();
    });

    hubCloseButton.addEventListener("click", function () {
      closeHub();
    });

    hubBackdrop.addEventListener("click", closeHub);

    hubApplyButton.addEventListener("click", function () {
      applyHub();
    });

    closeSheetButton.addEventListener("click", function () {
      closeSheet();
    });

    backdrop.addEventListener("click", closeSheet);

    applyButton.addEventListener("click", function () {
      applySheet();
    });

    filterChips.forEach(function (chip) {
      chip.addEventListener("click", function (event) {
        if (!isMobileViewport()) {
          return;
        }

        event.preventDefault();
        const target = chip.getAttribute("data-mobile-filter-target");
        openSheet(target || "gender");
      });
    });

    categoryTabs.forEach(function (tab) {
      tab.addEventListener("click", function (event) {
        if (!isMobileViewport()) {
          return;
        }

        event.preventDefault();
        openSheet("category", {
          value: tab.dataset.categoryId || ""
        });
      });
    });

    document.addEventListener("keydown", function (event) {
      if (event.key !== "Escape") {
        return;
      }

      if (!sheet.hidden) {
        closeSheet();
      }

      if (!hub.hidden) {
        closeHub();
      }
    });

    window.addEventListener("resize", function () {
      if (!isMobileViewport()) {
        if (!sheet.hidden) {
          closeSheet();
        }

        if (!hub.hidden) {
          closeHub();
        }
      }
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
