(function () {
  const runtime = document.getElementById("appRuntime");
  if (!runtime) {
    return;
  }

  const ctx = runtime.dataset.appCtx || "";
  const csrfToken = runtime.dataset.csrfToken || "";
  const csrfHeader = runtime.dataset.csrfHeader || "";

  window.APP_CTX = ctx;
  window.APP_CSRF = {
    token: csrfToken,
    headerName: csrfHeader
  };
})();
