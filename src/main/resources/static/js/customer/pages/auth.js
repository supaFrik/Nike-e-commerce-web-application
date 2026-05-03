document.addEventListener("DOMContentLoaded", () => {
  const ctx = (window.APP_CTX || "").replace(/\/$/, "");
  const authInit = document.getElementById("authInit");

  const signInForm = document.getElementById("signInForm");
  const signUpForm = document.getElementById("signUpForm");
  const toggleAuthLink = document.getElementById("toggleAuthLink");
  const titleEl = document.getElementById("authTitle");
  const subtitleEl = document.getElementById("authSubtitle");

  function updateForms(showSignUp) {
    if (!signInForm || !signUpForm) {
      return;
    }

    if (showSignUp) {
      signInForm.classList.remove("active");
      signUpForm.classList.add("active");
      if (toggleAuthLink) toggleAuthLink.textContent = "Đăng nhập";
      if (titleEl) titleEl.textContent = "Tạo tài khoản";
      if (subtitleEl) subtitleEl.textContent = "Đăng ký Nike";
      return;
    }

    signUpForm.classList.remove("active");
    signInForm.classList.add("active");
    if (toggleAuthLink) toggleAuthLink.textContent = "Đăng ký";
    if (titleEl) titleEl.textContent = "Chào mừng bạn quay trở lại!";
    if (subtitleEl) subtitleEl.textContent = "Đăng nhập";
  }

  if (toggleAuthLink && signInForm && signUpForm) {
    toggleAuthLink.addEventListener("click", (event) => {
      event.preventDefault();
      updateForms(signInForm.classList.contains("active"));
    });
  }

  const usernameInput = document.getElementById("signUpName");
  const emailInput = document.getElementById("signUpEmail");
  const passwordInput = document.getElementById("signUpPassword");
  const confirmPasswordInput = document.getElementById("signUpConfirmPassword");

  const usernameError = document.getElementById("signUpNameError");
  const emailError = document.getElementById("signUpEmailError");
  const passwordError = document.getElementById("signUpPasswordError");
  const confirmPasswordError = document.getElementById("signUpConfirmPasswordError");
  const submitBtn = signUpForm?.querySelector('button[type="submit"]');

  const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/;

  function showError(el, msg) {
    if (!el) {
      return;
    }
    el.style.display = msg ? "block" : "none";
    el.textContent = msg || "";
  }

  function clearErrors() {
    [usernameError, emailError, passwordError, confirmPasswordError].forEach((el) => showError(el, ""));
  }

  async function fetchDuplicateStatus({ username, email }) {
    const params = new URLSearchParams();
    if (username) {
      params.append("username", username);
    }
    if (email) {
      params.append("email", email.toLowerCase());
    }
    if ([...params.keys()].length === 0) {
      return { usernameExists: false, emailExists: false };
    }

    try {
      const response = await fetch(`${ctx}/api/auth/check-duplicate?${params.toString()}`, {
        headers: { Accept: "application/json" }
      });
      if (!response.ok) {
        throw new Error("Duplicate check failed");
      }
      const data = await response.json();
      return {
        usernameExists: Boolean(data.usernameExists),
        emailExists: Boolean(data.emailExists)
      };
    } catch (error) {
      return { usernameExists: false, emailExists: false };
    }
  }

  async function validateDuplicates() {
    const username = (usernameInput?.value || "").trim();
    const email = (emailInput?.value || "").trim();
    if (!username && !email) {
      return true;
    }
    if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      return false;
    }

    const { usernameExists, emailExists } = await fetchDuplicateStatus({ username, email });
    if (usernameExists) {
      showError(usernameError, "Username already exists");
    }
    if (emailExists) {
      showError(emailError, "Email already exists");
    }
    return !(usernameExists || emailExists);
  }

  usernameInput?.addEventListener("blur", async () => {
    const value = usernameInput.value.trim();
    if (value.length < 3) {
      return;
    }
    const { usernameExists } = await fetchDuplicateStatus({ username: value });
    showError(usernameError, usernameExists ? "Username already exists" : "");
  });

  emailInput?.addEventListener("blur", async () => {
    const value = emailInput.value.trim();
    if (!value || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
      signUpForm.dataset.emailDuplicate = "false";
      updateSubmitDisabled();
      return;
    }

    const { emailExists } = await fetchDuplicateStatus({ email: value });
    showError(emailError, emailExists ? "Email already exists" : "");
    signUpForm.dataset.emailDuplicate = emailExists ? "true" : "false";
    updateSubmitDisabled();
  });

  [usernameInput, emailInput, passwordInput, confirmPasswordInput].forEach((input) => {
    input?.addEventListener("input", () => {
      const errorMap = {
        signUpName: usernameError,
        signUpEmail: emailError,
        signUpPassword: passwordError,
        signUpConfirmPassword: confirmPasswordError
      };
      showError(errorMap[input.id], "");
    });
  });

  function updateSubmitDisabled() {
    if (!submitBtn) {
      return;
    }
    const emailDuplicate = signUpForm.dataset.emailDuplicate === "true";
    submitBtn.disabled = emailDuplicate;
    if (emailDuplicate) {
      submitBtn.title = "Email already exists";
      return;
    }
    submitBtn.removeAttribute("title");
  }

  signUpForm?.addEventListener("submit", async (event) => {
    event.preventDefault();
    clearErrors();

    let valid = true;
    const username = (usernameInput?.value || "").trim();
    const email = (emailInput?.value || "").trim();
    const password = passwordInput?.value || "";
    const confirmPassword = confirmPasswordInput?.value || "";

    if (!username) {
      showError(usernameError, "Username is required");
      valid = false;
    }
    if (!email) {
      showError(emailError, "Email is required");
      valid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      showError(emailError, "Invalid email format");
      valid = false;
    }

    if (!password) {
      showError(passwordError, "Password is required");
      valid = false;
    } else if (!strongPasswordRegex.test(password)) {
      showError(passwordError, "Must be 8+ chars incl. upper, lower & number");
      valid = false;
    }

    if (confirmPasswordInput && password !== confirmPassword) {
      showError(confirmPasswordError, "Passwords do not match");
      valid = false;
    }

    if (signUpForm.dataset.emailDuplicate === "true") {
      showError(emailError, "Email already exists");
      valid = false;
    }

    if (valid) {
      submitBtn && (submitBtn.disabled = true);
      const duplicateOk = await validateDuplicates();
      if (!duplicateOk) {
        valid = false;
        signUpForm.dataset.emailDuplicate = emailError?.textContent ? "true" : "false";
        updateSubmitDisabled();
      }
    }

    if (valid) {
      signUpForm.removeAttribute("data-email-duplicate");
      submitBtn && (submitBtn.disabled = false);
      signUpForm.submit();
      return;
    }

    submitBtn && (submitBtn.disabled = false);
  });

  document.querySelectorAll(".password-toggle[data-target-input]").forEach((button) => {
    button.addEventListener("click", () => {
      const input = document.getElementById(button.dataset.targetInput);
      if (!input) {
        return;
      }
      const show = input.type === "password";
      input.type = show ? "text" : "password";
      button.setAttribute("aria-pressed", String(show));
    });
  });

  document.querySelectorAll("[data-social-provider]").forEach((button) => {
    button.addEventListener("click", (event) => {
      event.preventDefault();
      showToast(`Đăng nhập với ${button.dataset.socialProvider} sẽ được triển khai sau.`, { type: "info" });
    });
  });

  document.querySelectorAll("[data-forgot-password]").forEach((button) => {
    button.addEventListener("click", (event) => {
      event.preventDefault();
      window.location.href = `${ctx}/forgot-password`;
    });
  });

  function showToast(message, options = {}) {
    const container = document.getElementById("toast") || document.body.appendChild(document.createElement("div"));
    const toast = document.createElement("div");
    toast.className = "toast active";
    toast.setAttribute("role", "status");
    toast.setAttribute("aria-live", "polite");
    const type = options.type === "success" ? "success" : options.type === "info" ? "info" : "error";
    toast.dataset.toastType = type;
    toast.style.borderLeft = `3px solid ${type === "success" ? "#2e7d32" : type === "info" ? "#1976d2" : "#d32f2f"}`;
    toast.innerHTML = `<span class="toast-icon" aria-hidden="true">${type === "success" ? "✓" : type === "info" ? "i" : "!"}</span><div class="toast-text"></div><button class="toast-close" type="button" aria-label="Dismiss notification">&times;</button>`;
    toast.querySelector(".toast-text").textContent = message;
    container.appendChild(toast);

    function removeToast() {
      toast.classList.remove("active");
      window.setTimeout(() => toast.remove(), 400);
    }

    toast.querySelector(".toast-close")?.addEventListener("click", removeToast);
    window.setTimeout(removeToast, options.duration || 5000);
  }

  window.showToast = showToast;

  const init = authInit?.dataset || {};
  if (init.signupPasswordError) {
    showError(passwordError, init.signupPasswordError);
  }
  if (init.openSignUp === "true") {
    updateForms(true);
  }
  if (init.serverLoginError) {
    showToast(init.serverLoginError, { type: "error" });
  } else {
    const params = new URLSearchParams(window.location.search || "");
    if (params.get("error") === "true" || params.has("error")) {
      showToast("Invalid email or password", { type: "error" });
    }
  }
  if (init.signupError) {
    updateForms(true);
    showToast(init.signupError, { type: "error" });
  }
  if (init.signupSuccess) {
    showToast(init.signupSuccess, { type: "success" });
  }
});
