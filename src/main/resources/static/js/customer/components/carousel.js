(function () {
  function qs(selector, root) {
    return (root || document).querySelector(selector);
  }

  function qsa(selector, root) {
    return Array.from((root || document).querySelectorAll(selector));
  }

  function getAppContext() {
    return (window.APP_CTX || "").replace(/\/$/, "");
  }

  function toRoute(path) {
    const base = getAppContext();
    if (!path) {
      return base + "/products/list";
    }
    if (/^https?:\/\//i.test(path)) {
      return path;
    }
    return base + (path.startsWith("/") ? path : "/" + path);
  }

  function initCarousel(config) {
    const track = qs("#" + config.trackId);
    const prevBtn = qs("#" + config.prevBtnId);
    const nextBtn = qs("#" + config.nextBtnId);
    const wrapper = track ? track.parentElement : null;

    if (!track || !prevBtn || !nextBtn || !wrapper) {
      return;
    }

    const slides = qsa(config.slideSelector, track);
    if (!slides.length) {
      return;
    }

    function getScrollDistance() {
      const firstSlide = slides[0];
      const gap = parseFloat(window.getComputedStyle(track).gap || "0");
      return firstSlide.getBoundingClientRect().width + gap;
    }

    function updateButtonStates() {
      const scrollLeft = wrapper.scrollLeft;
      const maxScroll = Math.max(wrapper.scrollWidth - wrapper.clientWidth, 0);
      const atStart = scrollLeft <= 1;
      const atEnd = scrollLeft >= maxScroll - 1;

      prevBtn.style.opacity = atStart ? "0.5" : "1";
      prevBtn.style.pointerEvents = atStart ? "none" : "auto";
      prevBtn.setAttribute("aria-disabled", atStart ? "true" : "false");

      nextBtn.style.opacity = atEnd ? "0.5" : "1";
      nextBtn.style.pointerEvents = atEnd ? "none" : "auto";
      nextBtn.setAttribute("aria-disabled", atEnd ? "true" : "false");
    }

    function scrollByStep(direction) {
      wrapper.scrollBy({
        left: getScrollDistance() * direction,
        behavior: "smooth"
      });
    }

    prevBtn.addEventListener("click", function () {
      scrollByStep(-1);
    });

    nextBtn.addEventListener("click", function () {
      scrollByStep(1);
    });

    wrapper.addEventListener("scroll", updateButtonStates);
    window.addEventListener("resize", updateButtonStates);

    slides.forEach(function (slide) {
      if (!slide.hasAttribute("tabindex")) {
        slide.setAttribute("tabindex", "0");
      }

      function activate() {
        if (typeof config.onActivate === "function") {
          config.onActivate(slide);
        }
      }

      slide.addEventListener("click", activate);
      slide.addEventListener("keydown", function (event) {
        if (event.key === "Enter" || event.key === " ") {
          event.preventDefault();
          activate();
        }
      });
    });

    const section = qs(config.sectionSelector);
    if (section) {
      section.addEventListener("keydown", function (event) {
        if (event.key === "ArrowLeft") {
          event.preventDefault();
          scrollByStep(-1);
        } else if (event.key === "ArrowRight") {
          event.preventDefault();
          scrollByStep(1);
        } else if (event.key === "Home") {
          event.preventDefault();
          wrapper.scrollTo({ left: 0, behavior: "smooth" });
        } else if (event.key === "End") {
          event.preventDefault();
          wrapper.scrollTo({ left: wrapper.scrollWidth, behavior: "smooth" });
        }
      });
    }

    let dragStartX = 0;
    let dragStartScrollLeft = 0;
    let dragging = false;

    wrapper.addEventListener("touchstart", function (event) {
      dragStartX = event.touches[0].clientX;
      dragStartScrollLeft = wrapper.scrollLeft;
      dragging = true;
    }, { passive: true });

    wrapper.addEventListener("touchmove", function (event) {
      if (!dragging) {
        return;
      }

      const deltaX = dragStartX - event.touches[0].clientX;
      wrapper.scrollLeft = dragStartScrollLeft + deltaX;
    }, { passive: true });

    wrapper.addEventListener("touchend", function () {
      dragging = false;
    }, { passive: true });

    updateButtonStates();
  }

  function bindNavigationFromData(slide, fallbackPath) {
    const href = slide.dataset.href || fallbackPath;
    window.location.href = toRoute(href);
  }

  function init() {
    initCarousel({
      trackId: "sportSliderTrack",
      prevBtnId: "sportPrev",
      nextBtnId: "sportNext",
      slideSelector: ".sport-slide",
      sectionSelector: ".shop-by-sport-section",
      onActivate: function (slide) {
        bindNavigationFromData(slide, "/products/list");
      }
    });

    initCarousel({
      trackId: "shopIconsTrack",
      prevBtnId: "shopIconsPrevBtn",
      nextBtnId: "shopIconsNextBtn",
      slideSelector: ".shop-icon-card",
      sectionSelector: ".shop-icons-section",
      onActivate: function (slide) {
        bindNavigationFromData(slide, "/products/list");
      }
    });

    initCarousel({
      trackId: "shopRunningTrack",
      prevBtnId: "shopRunningPrevBtn",
      nextBtnId: "shopRunningNextBtn",
      slideSelector: ".running-card",
      sectionSelector: ".shop-running-section",
      onActivate: function (slide) {
        const detailLink = qs("a[href]", slide);
        if (detailLink) {
          window.location.href = detailLink.getAttribute("href");
        }
      }
    });

    initCarousel({
      trackId: "storySliderTrack",
      prevBtnId: "storyPrev",
      nextBtnId: "storyNext",
      slideSelector: ".story-slide",
      sectionSelector: ".related-stories-section"
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
