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
    const confirmPasswordInput = document.getElementById('confirmPassword'); // only if present

    const usernameError = document.getElementById('signUpNameError');
    const emailError = document.getElementById('signUpEmailError');
    const passwordError = document.getElementById('signUpPasswordError');
    const confirmPasswordError = document.getElementById('confirmPasswordError');

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
        // Basic format guard to avoid noisy calls
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
                'confirmPassword': confirmPasswordError
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
});
