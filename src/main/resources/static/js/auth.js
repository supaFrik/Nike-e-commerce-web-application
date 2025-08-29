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
    }

    setupEventListeners() {
        if (this.signInForm) {
            this.signInForm.addEventListener('submit', (e) => {
                if (!this.validateSignIn()) {
                    e.preventDefault();
                }
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

    injectStyles() {
        // Optionally inject custom styles if needed
    }
}

// Initialize the authentication manager when DOM is ready
window.addEventListener('DOMContentLoaded', () => {
    new AuthenticationManager();
});
