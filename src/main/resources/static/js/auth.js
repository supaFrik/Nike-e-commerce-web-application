/**
 * auth-fixed.js
 * Simplified: sign-in validation only checks email format + non-empty password.
 * sign-up uses strong password rule.
 */

class AuthenticationManager {
    constructor() {
        this.validationRules = {
            email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            strongPassword: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/
        };
        this.init();
    }

    init() {
        this.setupFormElements();
        this.setupEventListeners();
    }

    setupFormElements() {
        this.signInForm = document.getElementById('signInForm');
        this.signInEmail = document.getElementById('signInEmail');
        this.signInPassword = document.getElementById('signInPassword');
        this.signInEmailError = document.getElementById('signInEmailError');
        this.signInPasswordError = document.getElementById('signInPasswordError');

        this.signUpForm = document.getElementById('signUpForm');
        this.signUpEmail = document.querySelector('#signUpEmail');
        this.signUpPassword = document.querySelector('#signUpPassword');
        this.signUpName = document.querySelector('#signUpName');

        this.toggleAuthLink = document.getElementById('toggleAuthLink');
        this.authFooterText = document.getElementById('authFooterText');
    }

    setupEventListeners() {
        // SIGN IN: only basic checks; do NOT block legitimate login attempts with strong password rule
        if (this.signInForm) {
            this.signInForm.addEventListener('submit', (e) => {
                if (!this.validateSignIn()) {
                    // prevent submission only when basic fields are clearly invalid (empty or malformed email)
                    e.preventDefault();
                }
                // otherwise allow the form to submit normally to Spring Security
            });
        }

        // SIGN UP: use strong validation; prevent submit if invalid
        if (this.signUpForm) {
            this.signUpForm.addEventListener('submit', (e) => {
                if (!this.validateSignUp()) {
                    e.preventDefault();
                }
            });
        }

        // Toggle between forms (footer link and below sign up form)
        const toggleAuthLink = document.getElementById('toggleAuthLink');
        const switchToSignIn = document.getElementById('switchToSignIn');
        if (toggleAuthLink) {
            toggleAuthLink.addEventListener('click', (ev) => {
                ev.preventDefault();
                this.showSignUp();
            });
        }
        if (switchToSignIn) {
            switchToSignIn.addEventListener('click', (ev) => {
                ev.preventDefault();
                this.showSignIn();
            });
        }
    }

    validateSignIn() {
        let valid = true;

        // Email must be present & valid
        const email = this.signInEmail && this.signInEmail.value ? this.signInEmail.value.trim() : '';
        if (!email || !this.validationRules.email.test(email)) {
            if (this.signInEmailError) this.signInEmailError.textContent = 'Please enter a valid email address.';
            if (this.signInEmail) this.signInEmail.setAttribute('aria-invalid', 'true');
            valid = false;
        } else {
            if (this.signInEmailError) this.signInEmailError.textContent = '';
            if (this.signInEmail) this.signInEmail.setAttribute('aria-invalid', 'false');
        }

        // Password must not be empty (no strong rule here)
        const pass = this.signInPassword && this.signInPassword.value ? this.signInPassword.value : '';
        if (!pass) {
            if (this.signInPasswordError) this.signInPasswordError.textContent = 'Please enter your password.';
            if (this.signInPassword) this.signInPassword.setAttribute('aria-invalid', 'true');
            valid = false;
        } else {
            if (this.signInPasswordError) this.signInPasswordError.textContent = '';
            if (this.signInPassword) this.signInPassword.setAttribute('aria-invalid', 'false');
        }

        return valid;
    }

    validateSignUp() {
        let valid = true;
        // Email
        if (!this.signUpEmail || !this.validationRules.email.test(this.signUpEmail.value || '')) {
            // show error next to sign-up email (if you had an element)
            valid = false;
        }
        // Name
        if (!this.signUpName || !this.signUpName.value.trim()) {
            valid = false;
        }
        // Strong password
        if (!this.signUpPassword || !this.validationRules.strongPassword.test(this.signUpPassword.value || '')) {
            // show error message for sign up password
            valid = false;
        }
        return valid;
    }

    showSignUp() {
        if (!this.signInForm || !this.signUpForm) return;
        this.signInForm.classList.remove('active');
        this.signInForm.style.display = 'none';
        this.signUpForm.classList.add('active');
        this.signUpForm.style.display = '';
        // Update footer text
        if (this.authFooterText) {
            this.authFooterText.innerHTML = 'Already have an account? <a href="#" id="switchToSignIn">Sign In</a>';
            // Re-attach event listener for new link
            const switchToSignIn = document.getElementById('switchToSignIn');
            if (switchToSignIn) {
                switchToSignIn.addEventListener('click', (ev) => {
                    ev.preventDefault();
                    this.showSignIn();
                });
            }
        }
    }

    showSignIn() {
        if (!this.signInForm || !this.signUpForm) return;
        this.signUpForm.classList.remove('active');
        this.signUpForm.style.display = 'none';
        this.signInForm.classList.add('active');
        this.signInForm.style.display = '';
        // Update footer text
        if (this.authFooterText) {
            this.authFooterText.innerHTML = 'Dont have an account? <a href="#" id="toggleAuthLink">Sign Up</a>';
            // Re-attach event listener for new link
            const toggleAuthLink = document.getElementById('toggleAuthLink');
            if (toggleAuthLink) {
                toggleAuthLink.addEventListener('click', (ev) => {
                    ev.preventDefault();
                    this.showSignUp();
                });
            }
        }
    }

    toggleForms() {
        // simply toggle 'active' class and display style
        if (!this.signInForm || !this.signUpForm) return;
        const signInActive = this.signInForm.classList.contains('active');
        if (signInActive) {
            this.signInForm.classList.remove('active');
            this.signInForm.style.display = 'none';
            this.signUpForm.classList.add('active');
            this.signUpForm.style.display = '';
        } else {
            this.signUpForm.classList.remove('active');
            this.signUpForm.style.display = 'none';
            this.signInForm.classList.add('active');
            this.signInForm.style.display = '';
        }
    }
}

// Toggle password visibility
window.togglePassword = function(inputId) {
    const input = document.getElementById(inputId);
    if (!input) return;
    const btn = input.parentElement.querySelector('.password-toggle');
    if (input.type === 'password') {
        input.type = 'text';
        if (btn) btn.setAttribute('aria-pressed', 'true');
    } else {
        input.type = 'password';
        if (btn) btn.setAttribute('aria-pressed', 'false');
    }
};

window.addEventListener('DOMContentLoaded', () => {
    new AuthenticationManager();
});
