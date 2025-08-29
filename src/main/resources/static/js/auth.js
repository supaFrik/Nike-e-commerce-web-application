/**
 * Authentication Page JavaScript
 * Handles sign in/sign up forms, validation, and authentication flow
 */

class AuthenticationManager {
    constructor() {
        this.validationRules = {
            email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/
        };
        this.init();
    }

    init() {
        this.setupFormElements();
        this.setupEventListeners();
        this.injectStyles();
    }

    setupFormElements() {
        this.signInForm = document.getElementById('signInForm');
        this.signInEmail = document.getElementById('signInEmail');
        this.signInPassword = document.getElementById('signInPassword');
        this.signInEmailError = document.getElementById('signInEmailError');
        this.signInPasswordError = document.getElementById('signInPasswordError');
        // Add references for sign up form and toggle link
        this.signUpForm = document.getElementById('signUpForm');
        this.toggleAuthLink = document.getElementById('toggleAuthLink');
        this.authFooterText = document.getElementById('authFooterText');
        this.toggleFormDesc = document.getElementById('toggle-form-desc');
    }

    setupEventListeners() {
        if (this.signInForm) {
            this.signInForm.addEventListener('submit', (e) => {
                if (!this.validateSignIn()) {
                    e.preventDefault();
                }
            });
        }
        // Add event listener for toggle link
        if (this.toggleAuthLink) {
            this.toggleAuthLink.addEventListener('click', (e) => {
                e.preventDefault();
                this.toggleForms();
            });
        }
    }

    validateSignIn() {
        let valid = true;
        // Email validation
        if (!this.validationRules.email.test(this.signInEmail.value)) {
            this.signInEmailError.textContent = 'Please enter a valid email address.';
            this.signInEmail.setAttribute('aria-invalid', 'true');
            valid = false;
        } else {
            this.signInEmailError.textContent = '';
            this.signInEmail.setAttribute('aria-invalid', 'false');
        }
        // Password validation
        if (!this.validationRules.password.test(this.signInPassword.value)) {
            this.signInPasswordError.textContent = 'Password must be at least 8 characters, include uppercase, lowercase, and a number.';
            this.signInPassword.setAttribute('aria-invalid', 'true');
            valid = false;
        } else {
            this.signInPasswordError.textContent = '';
            this.signInPassword.setAttribute('aria-invalid', 'false');
        }
        return valid;
    }

    toggleForms() {
        const isSignInActive = this.signInForm.classList.contains('active');
        if (isSignInActive) {
            // Hide sign in, show sign up
            this.signInForm.classList.remove('active');
            this.signInForm.style.display = 'none';
            this.signUpForm.classList.add('active');
            this.signUpForm.style.display = '';
            // Update link text and description
            this.authFooterText.innerHTML = `Already have an account? <a href="#" id="toggleAuthLink" aria-describedby="toggle-form-desc">Sign In</a><span id="toggle-form-desc" class="sr-only">Switch between sign in and sign up forms</span>`;
        } else {
            // Hide sign up, show sign in
            this.signUpForm.classList.remove('active');
            this.signUpForm.style.display = 'none';
            this.signInForm.classList.add('active');
            this.signInForm.style.display = '';
            // Update link text and description
            this.authFooterText.innerHTML = `Dont have an account? <a href="#" id="toggleAuthLink" aria-describedby="toggle-form-desc">Sign Up</a><span id="toggle-form-desc" class="sr-only">Switch between sign in and sign up forms</span>`;
        }
        // Re-attach event listener to new link
        this.toggleAuthLink = document.getElementById('toggleAuthLink');
        if (this.toggleAuthLink) {
            this.toggleAuthLink.addEventListener('click', (e) => {
                e.preventDefault();
                this.toggleForms();
            });
        }
    }

    injectStyles() {
        // Optionally inject custom styles if needed
    }
}

// Toggle password visibility for password fields
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

// Initialize the authentication manager when DOM is ready
window.addEventListener('DOMContentLoaded', () => {
    new AuthenticationManager();
});
