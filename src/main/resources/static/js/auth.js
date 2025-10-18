// ==========================
// auth.js – Toggle & Validation (cleaned & fixed duplicate checks)
// ==========================

document.addEventListener("DOMContentLoaded", () => {
    const ctx = (window.APP_CTX || '').replace(/\/$/, '');

    // ---------- 1. Toggle Sign In / Sign Up ----------
    const signInForm = document.getElementById('signInForm');
    const signUpForm = document.getElementById('signUpForm');
    const toggleAuthLink = document.getElementById('toggleAuthLink');
    const titleEl = document.getElementById('authTitle');
    const subtitleEl = document.getElementById('authSubtitle');

    function updateForms(showSignUp) {
        if (!signInForm || !signUpForm) return;
        if (showSignUp) {
            signInForm.classList.remove('active');
            signUpForm.classList.add('active');
            if (toggleAuthLink) toggleAuthLink.textContent = 'Sign In';
            if (titleEl) titleEl.textContent = 'Create Account';
            if (subtitleEl) subtitleEl.textContent = 'Sign Up to Nike';
        } else {
            signUpForm.classList.remove('active');
            signInForm.classList.add('active');
            if (toggleAuthLink) toggleAuthLink.textContent = 'Sign Up';
            if (titleEl) titleEl.textContent = 'Welcome back';
            if (subtitleEl) subtitleEl.textContent = 'Sign In to Nike';
        }
    }

    if (toggleAuthLink && signInForm && signUpForm) {
        toggleAuthLink.addEventListener('click', (e) => {
            e.preventDefault();
            const showSignUp = signInForm.classList.contains('active');
            updateForms(showSignUp);
        });
    }

    // Exposed for server-triggered toggle (e.g., signupError)
    window.toggleToSignUp = function () { updateForms(true); };

    if (!signUpForm) return; // nothing else needed if signup form not present

    // ---------- 2. Elements for signup (MATCH JSP IDs) ----------
    const usernameInput = document.getElementById('signUpName');
    const emailInput = document.getElementById('signUpEmail');
    const passwordInput = document.getElementById('signUpPassword');
    const confirmPasswordInput = document.getElementById('signUpConfirmPassword');

    const usernameError = document.getElementById('signUpNameError');
    const emailError = document.getElementById('signUpEmailError');
    const passwordError = document.getElementById('signUpPasswordError');
    const confirmPasswordError = document.getElementById('signUpConfirmPasswordError');

    const submitBtn = signUpForm.querySelector('button[type="submit"]');

    // ---------- 3. Helpers ----------
    const showError = (el, msg) => { if (el) { el.style.display = msg ? 'block' : (el.id.includes('Password') ? el.style.display : 'none'); el.textContent = msg || ''; } };
    const clearErrors = () => {
        [usernameError, emailError, passwordError, confirmPasswordError]
            .forEach(e => e && showError(e, ''));
    };

    const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$/;

    // ---------- 4. Duplicate check (single endpoint) ----------
    async function fetchDuplicateStatus({ username, email }) {
        const params = new URLSearchParams();
        if (username) params.append('username', username);
        if (email) params.append('email', email.toLowerCase());
        if ([...params.keys()].length === 0) return { usernameExists: false, emailExists: false };
        try {
            const res = await fetch(`${ctx}/api/auth/check-duplicate?${params.toString()}`, { headers: { 'Accept': 'application/json' } });
            if (!res.ok) throw new Error('Duplicate check failed');
            const data = await res.json();
            return {
                usernameExists: !!data.usernameExists,
                emailExists: !!data.emailExists
            };
        } catch (e) {
            return { usernameExists: false, emailExists: false };
        }
    }

    async function validateDuplicates() {
        const username = (usernameInput?.value || '').trim();
        const email = (emailInput?.value || '').trim();
        if (!username && !email) return true;
        if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return false;
        const { usernameExists, emailExists } = await fetchDuplicateStatus({ username, email });

        if (usernameExists) showError(usernameError, 'Username already exists');
        if (emailExists) showError(emailError, 'Email already exists');
        return !(usernameExists || emailExists);
    }

    usernameInput?.addEventListener('blur', async () => {
        const v = usernameInput.value.trim();
        if (v.length < 3) return;
        const { usernameExists } = await fetchDuplicateStatus({ username: v });
        showError(usernameError, usernameExists ? 'Username already exists' : '');
    });

    emailInput?.addEventListener('blur', async () => {
        const v = emailInput.value.trim();
        if (!v || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)) {
            signUpForm.dataset.emailDuplicate = 'false';
            updateSubmitDisabled();
            return;
        }
        const { emailExists } = await fetchDuplicateStatus({ email: v });
        if (emailExists) {
            showError(emailError, 'Email already exists');
            signUpForm.dataset.emailDuplicate = 'true';
        } else {
            showError(emailError, '');
            signUpForm.dataset.emailDuplicate = 'false';
        }
        updateSubmitDisabled();
    });

    [usernameInput, emailInput, passwordInput, confirmPasswordInput].forEach(input => {
        input?.addEventListener('input', () => {
            const errElIdMap = {
                'signUpName': usernameError,
                'signUpEmail': emailError,
                'signUpPassword': passwordError,
                'signUpConfirmPassword': confirmPasswordError
            };
            showError(errElIdMap[input.id], '');
        });
    });

    // ---------- 6. Form submit validation ----------
    function updateSubmitDisabled() {
        if (!submitBtn) return;
        const emailDup = signUpForm.dataset.emailDuplicate === 'true';
        submitBtn.disabled = emailDup;
        if (emailDup) {
            submitBtn.title = 'Email already exists';
        } else {
            submitBtn.removeAttribute('title');
        }
    }

    signUpForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        clearErrors();
        let valid = true;

        const username = (usernameInput?.value || '').trim();
        const email = (emailInput?.value || '').trim();
        const password = passwordInput?.value || '';
        const confirmPassword = confirmPasswordInput?.value || '';

        if (!username) { showError(usernameError, 'Username is required'); valid = false; }
        if (!email) { showError(emailError, 'Email is required'); valid = false; }
        else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { showError(emailError, 'Invalid email format'); valid = false; }

        if (!password) { showError(passwordError, 'Password is required'); valid = false; }
        else if (!strongPasswordRegex.test(password)) { showError(passwordError, 'Must be 8+ chars incl. upper, lower & number'); valid = false; }

        if (confirmPasswordInput && password !== confirmPassword) {
            showError(confirmPasswordError, 'Passwords do not match'); valid = false;
        }

        if (signUpForm.dataset.emailDuplicate === 'true') {
            showError(emailError, 'Email already exists');
            valid = false;
        }

        if (valid) {
            submitBtn && (submitBtn.disabled = true);
            const dupOk = await validateDuplicates();
            if (!dupOk) {
                valid = false;
                signUpForm.dataset.emailDuplicate = emailError?.textContent ? 'true' : 'false';
                updateSubmitDisabled();
            }
        }

        if (valid) {
            signUpForm.removeAttribute('data-email-duplicate');
            submitBtn && (submitBtn.disabled = false);
            signUpForm.submit();
        } else {
            submitBtn && (submitBtn.disabled = false);
        }
    });

    // ------------- 7. Password visibility toggle -------------
    window.togglePassword = function (inputId) {
        const input = document.getElementById(inputId);
        if (!input) return;

        const btn = Array.from(document.querySelectorAll('button.password-toggle'))
            .find(b => (b.getAttribute('onclick') || '').includes(`'${inputId}'`));

        const isPassword = input.type === 'password';
        input.type = isPassword ? 'text' : 'password';
        if (btn) {
            btn.setAttribute('aria-pressed', String(isPassword));
        }
    };

    // ------------------ Toast utility ------------------
    function showToast(message, options = {}) {
        try {
            if (!document.getElementById('toast-style')) {
                const style = document.createElement('style');
                style.id = 'toast-style';
                style.textContent = `
                    .toast {
                      position: fixed;
                      bottom: 1.25rem;
                      right: 1.25rem;
                      background: #111;
                      color: #fff;
                      padding: 1rem 1.25rem;
                      border-radius: 8px;
                      box-shadow: 0 6px 28px rgba(0,0,0,.35);
                      font-size: .95rem;
                      z-index: 1000;
                      opacity: 0;
                      visibility: hidden;
                      transform: translateY(20px) scale(.995);
                      transition: opacity .38s cubic-bezier(.2,.9,.2,1), transform .38s cubic-bezier(.2,.9,.2,1), visibility 0s linear .38s;
                      pointer-events: none;
                      display: flex;
                      align-items: center;
                      gap: 8px;
                    }
                    .toast.active {
                      opacity: 1;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      visibility: visible;
                      transform: translateY(0) scale(1);
                      transition: opacity .38s cubic-bezier(.2,.9,.2,1), transform .38s cubic-bezier(.2,.9,.2,1), visibility 0s linear 0s;
                      pointer-events: auto;
                    }
                    .toast i:first-child { color: #fff; font-size: 18px; margin-right: 8px; }
                    .toast .toast-text { margin: 0; padding: 0 6px; text-transform: none; color: #fff; line-height:1.2 }
                    .toast .toast-close { background: transparent; border: none; color: #ccc; cursor: pointer; transition: color .2s; margin-left: 6px; font-size: 18px; }
                    .toast .toast-close:hover, .toast .toast-close:focus { color: #fff; outline: none; }
                `;
                document.head.appendChild(style);
            }

            const cfg = Object.assign({ type: 'error', duration: 5000 }, options);
            const container = document.getElementById('toast') || (function(){ const d = document.createElement('div'); d.id = 'toast'; document.body.appendChild(d); return d; })();

            const toast = document.createElement('div');
            toast.className = 'toast';
            toast.setAttribute('role', 'status');
            toast.setAttribute('aria-live', 'polite');

            let borderColor = '#000';
            if (cfg.type === 'error') borderColor = '#d32f2f';
            if (cfg.type === 'success') borderColor = '#2e7d32';
            if (cfg.type === 'info') borderColor = '#1976d2';
            toast.style.borderLeft = `3px solid ${borderColor}`;

            const icon = document.createElement('i');
            icon.setAttribute('aria-hidden', 'true');
            icon.innerHTML = cfg.type === 'success' ? '&#10003;' : (cfg.type === 'info' ? '&#8505;' : '&#9888;');
            icon.style.marginRight = '12px';

            const text = document.createElement('div');
            text.className = 'toast-text';
            text.innerHTML = escapeHtml(message || '');

            const closeBtn = document.createElement('button');
            closeBtn.className = 'toast-close';
            closeBtn.setAttribute('aria-label', 'Dismiss notification');
            closeBtn.innerHTML = '&times;';
            closeBtn.style.background = 'transparent';
            closeBtn.style.border = 'none';
            closeBtn.style.padding = '0';
            closeBtn.style.cursor = 'pointer';

            toast.appendChild(icon);
            toast.appendChild(text);
            toast.appendChild(closeBtn);

            container.appendChild(toast);
            requestAnimationFrame(() => { toast.classList.add('active'); });

            let dismissed = false;
            function removeToast() {
                if (dismissed) return; dismissed = true;
                toast.classList.remove('active');
                setTimeout(() => { try { container.removeChild(toast); } catch (e) {} }, 500);
            }

            closeBtn.addEventListener('click', removeToast);

            if (cfg.duration && cfg.duration > 0) {
                setTimeout(removeToast, cfg.duration);
            }

            return { remove: removeToast };
        } catch (e) {
            try { console.warn('showToast error', e); } catch (er) {}
        }
    }

    // Expose for manual testing/debugging from console
    try { window.showToast = showToast; } catch (e) { /* ignore in strict contexts */ }

    // Escape helper used by showToast
    function escapeHtml(unsafe) {
        return String(unsafe)
          .replace(/&/g, '&amp;')
          .replace(/</g, '&lt;')
          .replace(/>/g, '&gt;')
          .replace(/"/g, '&quot;')
          .replace(/'/g, '&#039;');
    }

    try {
        const init = window.__AUTH_INIT || {};
        const urlParams = new URLSearchParams(init.search || window.location.search || '');
        let shown = false;

        if (init.serverLoginError && String(init.serverLoginError).trim()) {
            showToast(String(init.serverLoginError).trim(), { type: 'error' });
            shown = true;
        }

        if (!shown && (urlParams.has('error') || urlParams.get('error') === 'true')) {
            showToast('Invalid email or password', { type: 'error' });
            shown = true;
        }

        if (init.signupError && String(init.signupError).trim()) {
            if (typeof toggleToSignUp === 'function') toggleToSignUp();
            showToast(String(init.signupError).trim(), { type: 'error' });
        }

        if (init.signupSuccess && String(init.signupSuccess).trim()) {
            showToast(String(init.signupSuccess).trim(), { type: 'success' });
        }
    } catch (e) { }

});
