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
  const verificationCodeInput = document.getElementById("signUpVerificationCode");
  const resendCodeButton = document.getElementById("signUpResendCode");
  const verificationCountdown = document.getElementById("signUpVerificationCountdown");
  const verificationBanner = document.getElementById("signUpVerificationBanner");
  const passwordInput = document.getElementById("signUpPassword");
  const confirmPasswordInput = document.getElementById("signUpConfirmPassword");

  const usernameError = document.getElementById("signUpNameError");
  const emailError = document.getElementById("signUpEmailError");
  const verificationCodeError = document.getElementById("signUpVerificationCodeError");
  const passwordError = document.getElementById("signUpPasswordError");
  const confirmPasswordError = document.getElementById("signUpConfirmPasswordError");
  const passwordRequirementItems = document.querySelectorAll("[data-password-rule]");
  const submitBtn = signUpForm?.querySelector('button[type="submit"]');

  const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/;
  const verificationCodeRegex = /^\d{8}$/;
  const resendCooldownSeconds = 30;
  let resendTimer = null;
  let resendRemainingSeconds = 0;

  function showError(el, msg) {
    if (!el) {
      return;
    }
    el.style.display = msg ? "block" : "none";
    el.textContent = msg || "";
  }

  function setInputError(input, hasError) {
    input?.classList.toggle("error", Boolean(hasError));
  }

  function showVerificationError(message, options = {}) {
    showError(verificationCodeError, message);
    setInputError(verificationCodeInput, Boolean(message));
    if (verificationBanner) {
      verificationBanner.style.display = options.banner ? "flex" : "none";
    }
  }

  function clearErrors() {
    [usernameError, emailError, verificationCodeError, passwordError, confirmPasswordError].forEach((el) => showError(el, ""));
    [usernameInput, emailInput, verificationCodeInput, passwordInput, confirmPasswordInput].forEach((input) => {
      setInputError(input, false);
    });
    if (verificationBanner) {
      verificationBanner.style.display = "none";
    }
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
      setInputError(usernameInput, true);
    }
    if (emailExists) {
      showError(emailError, "Email already exists");
      setInputError(emailInput, true);
    }
    signUpForm.dataset.usernameDuplicate = usernameExists ? "true" : "false";
    signUpForm.dataset.emailDuplicate = emailExists ? "true" : "false";
    return !(usernameExists || emailExists);
  }

  function getCsrfHeaders() {
    const runtime = document.getElementById("appRuntime");
    const token = runtime?.dataset.csrfToken;
    const headerName = runtime?.dataset.csrfHeader;
    if (!token || !headerName) {
      return {};
    }
    return { [headerName]: token };
  }

  function updateCountdown() {
    if (!resendCodeButton || !verificationCountdown) {
      return;
    }

    if (resendRemainingSeconds <= 0) {
      resendCodeButton.disabled = false;
      resendCodeButton.removeAttribute("aria-disabled");
      verificationCountdown.textContent = "";
      window.clearInterval(resendTimer);
      resendTimer = null;
      return;
    }

    resendCodeButton.disabled = true;
    resendCodeButton.setAttribute("aria-disabled", "true");
    verificationCountdown.textContent = `Resend code in ${resendRemainingSeconds}s`;
    resendRemainingSeconds -= 1;
  }

  function startResendCooldown() {
    resendRemainingSeconds = resendCooldownSeconds;
    updateCountdown();
    window.clearInterval(resendTimer);
    resendTimer = window.setInterval(updateCountdown, 1000);
  }

  function updatePasswordRequirements() {
    const value = passwordInput?.value || "";
    const checks = {
      length: value.length >= 8,
      uppercase: /[A-Z]/.test(value),
      lowercase: /[a-z]/.test(value),
      number: /\d/.test(value)
    };

    passwordRequirementItems.forEach((item) => {
      const passed = Boolean(checks[item.dataset.passwordRule]);
      item.classList.toggle("is-met", passed);
    });
  }

  async function requestSignupVerificationCode() {
    const email = (emailInput?.value || "").trim();
    showVerificationError("");

    if (!email) {
      showError(emailError, "Email is required before requesting a code");
      setInputError(emailInput, true);
      return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      showError(emailError, "Invalid email format");
      setInputError(emailInput, true);
      return;
    }

    resendCodeButton && (resendCodeButton.disabled = true);
    try {
      const response = await fetch(`${ctx}/api/auth/signup-verification-code`, {
        method: "POST",
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
          ...getCsrfHeaders()
        },
        body: JSON.stringify({ email: email.toLowerCase() })
      });
      if (!response.ok) {
        throw new Error("Verification email endpoint is not ready");
      }
      signUpForm.dataset.verificationRequested = "true";
      signUpForm.dataset.verificationEmail = email.toLowerCase();
      showToast("Verification code sent. Check your email.", { type: "success" });
      startResendCooldown();
    } catch (error) {
      resendCodeButton && (resendCodeButton.disabled = false);
      showToast("Verification email service is not ready yet", { type: "info" });
    }
  }

  usernameInput?.addEventListener("blur", async () => {
    const value = usernameInput.value.trim();
    if (value.length < 3) {
      signUpForm.dataset.usernameDuplicate = "false";
      updateSubmitDisabled();
      return;
    }
    const { usernameExists } = await fetchDuplicateStatus({ username: value });
    showError(usernameError, usernameExists ? "Username already exists" : "");
    setInputError(usernameInput, usernameExists);
    signUpForm.dataset.usernameDuplicate = usernameExists ? "true" : "false";
    updateSubmitDisabled();
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
    setInputError(emailInput, emailExists);
    signUpForm.dataset.emailDuplicate = emailExists ? "true" : "false";
    updateSubmitDisabled();
  });

  [usernameInput, emailInput, verificationCodeInput, passwordInput, confirmPasswordInput].forEach((input) => {
    input?.addEventListener("input", () => {
      const errorMap = {
        signUpName: usernameError,
        signUpEmail: emailError,
        signUpVerificationCode: verificationCodeError,
        signUpPassword: passwordError,
        signUpConfirmPassword: confirmPasswordError
      };
      showError(errorMap[input.id], "");
      setInputError(input, false);
      if (input.id === "signUpName") {
        signUpForm.dataset.usernameDuplicate = "false";
        updateSubmitDisabled();
      }
      if (input.id === "signUpEmail") {
        signUpForm.dataset.emailDuplicate = "false";
        updateSubmitDisabled();
      }
      if (input.id === "signUpVerificationCode" && verificationBanner) {
        verificationBanner.style.display = "none";
      }
    });
  });

  verificationCodeInput?.addEventListener("input", () => {
    verificationCodeInput.value = verificationCodeInput.value.replace(/\D/g, "").slice(0, 8);
  });

  passwordInput?.addEventListener("input", updatePasswordRequirements);
  resendCodeButton?.addEventListener("click", requestSignupVerificationCode);

  function updateSubmitDisabled() {
    if (!submitBtn) {
      return;
    }
    const usernameDuplicate = signUpForm.dataset.usernameDuplicate === "true";
    const emailDuplicate = signUpForm.dataset.emailDuplicate === "true";
    submitBtn.disabled = usernameDuplicate || emailDuplicate;
    if (usernameDuplicate) {
      submitBtn.title = "Username already exists";
      return;
    }
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
    const verificationCode = (verificationCodeInput?.value || "").trim();
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
      setInputError(emailInput, true);
      valid = false;
    }

    if (!verificationCode) {
      showVerificationError("Required", { banner: true });
      valid = false;
    } else if (!verificationCodeRegex.test(verificationCode)) {
      showVerificationError("Code must be 8 digits long", { banner: true });
      valid = false;
    }

    if (valid && signUpForm.dataset.verificationRequested !== "true") {
      showVerificationError("Request a verification code before signing up", { banner: true });
      valid = false;
    }

    if (valid && signUpForm.dataset.verificationEmail !== email.toLowerCase()) {
      showVerificationError("Request a new code for this email", { banner: true });
      valid = false;
    }

    if (!password) {
      showError(passwordError, "Password is required");
      setInputError(passwordInput, true);
      valid = false;
    } else if (!strongPasswordRegex.test(password)) {
      showError(passwordError, "Must be 8+ chars incl. upper, lower & number");
      setInputError(passwordInput, true);
      valid = false;
    }

    if (confirmPasswordInput && password !== confirmPassword) {
      showError(confirmPasswordError, "Passwords do not match");
      setInputError(confirmPasswordInput, true);
      valid = false;
    }

    if (signUpForm.dataset.emailDuplicate === "true") {
      showError(emailError, "Email already exists");
      setInputError(emailInput, true);
      valid = false;
    }

    if (signUpForm.dataset.usernameDuplicate === "true") {
      showError(usernameError, "Username already exists");
      setInputError(usernameInput, true);
      valid = false;
    }

    if (valid) {
      submitBtn && (submitBtn.disabled = true);
      const duplicateOk = await validateDuplicates();
      if (!duplicateOk) {
        valid = false;
        signUpForm.dataset.usernameDuplicate = usernameError?.textContent ? "true" : "false";
        signUpForm.dataset.emailDuplicate = emailError?.textContent ? "true" : "false";
        updateSubmitDisabled();
      }
    }

    if (valid) {
      signUpForm.removeAttribute("data-username-duplicate");
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
      const provider = button.dataset.socialProvider;
      if (provider === "google") {
        window.location.href = `${ctx}/oauth2/authorization/google`;
        return;
      }
      showToast(`Dang nhap voi ${provider} se duoc trien khai sau.`, { type: "info" });
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
  updatePasswordRequirements();
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
