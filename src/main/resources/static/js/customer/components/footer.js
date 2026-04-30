(function () {
  function initFooterToast() {
    const footerContact = document.getElementById("footer-contact");
    if (!footerContact) {
      return;
    }

    const messageEl = footerContact.querySelector(".success-message");
    if (!messageEl) {
      return;
    }

    const toast = document.getElementById("contactToast");
    const text = document.getElementById("contactToastMsg");
    const close = document.getElementById("contactToastClose");

    if (!toast || !text || !close) {
      return;
    }

    text.textContent = messageEl.textContent.trim();
    toast.style.display = "flex";

    window.requestAnimationFrame(function () {
      toast.style.opacity = "1";
      toast.style.transform = "translateY(0)";
    });

    function hideToast() {
      toast.style.opacity = "0";
      toast.style.transform = "translateY(20px)";
      window.setTimeout(function () {
        toast.style.display = "none";
      }, 300);
    }

    close.addEventListener("click", hideToast);
    window.setTimeout(hideToast, 4000);
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initFooterToast);
  } else {
    initFooterToast();
  }
})();
