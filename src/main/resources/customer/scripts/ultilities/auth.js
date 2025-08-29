/**
 * Authentication Page JavaScript - Optimized
 * Handles sign in/sign up forms, validation, and authentication flow
 */

class AuthenticationManager {
    constructor() {
        this.isSignInMode = true;
        this.validationRules = {
            email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            password: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/,
            phone: /^\d{10,}$/
        };

        this.init();
    }

    init() {
        this.setupFormElements();
        this.setupEventListeners();
        this.updateFormDisplay();
        this.injectStyles();
    }

    setupFormElements() {
        this.signInForm = document.getElementById('signInForm');
        this.signUpForm = document.getElementById('signUpForm');
        this.authTitle = document.getElementById('authTitle');
        this.authSubtitle = document.getElementById('authSubtitle');
        this.authFooterText = document.getElementById('authFooterText');
        this.successMessage = document.getElementById('successMessage');
    }

    setupEventListeners() {
        // Form submit listeners
        if (this.signInForm) {
            this.signInForm.addEventListener('submit', (e) => this.handleSignIn(e));
        }

        if (this.signUpForm) {
            this.signUpForm.addEventListener('submit', (e) => this.handleSignUp(e));
        }

        // Input validation listeners
        this.addInputValidation();

        // Password visibility toggles
        this.setupPasswordToggles();
    }

    addInputValidation() {
        const inputs = document.querySelectorAll('input[type="email"], input[type="password"], input[type="tel"]');

        inputs.forEach(input => {
            input.addEventListener('blur', (e) => this.validateField(e.target));
            input.addEventListener('input', (e) => this.clearFieldError(e.target));
        });
    }

    setupPasswordToggles() {
        const toggleButtons = document.querySelectorAll('.password-toggle');

        toggleButtons.forEach(button => {
            button.addEventListener('click', (e) => this.togglePasswordVisibility(e));
        });
    }

    togglePasswordVisibility(e) {
        const button = e.target.closest('.password-toggle');
        const input = button.previousElementSibling;
        const icon = button.querySelector('i');

        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
            button.setAttribute('aria-label', 'Hide password');
        } else {
            input.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
            button.setAttribute('aria-label', 'Show password');
        }
    }

    toggleForm() {
        this.isSignInMode = !this.isSignInMode;
        this.updateFormDisplay();
        this.clearErrors();
        this.clearForms();
    }

    updateFormDisplay() {
        if (this.isSignInMode) {
            this.showSignInForm();
        } else {
            this.showSignUpForm();
        }
    }

    showSignInForm() {
        // Toggle form visibility
        if (this.signInForm) this.signInForm.classList.add('active');
        if (this.signUpForm) this.signUpForm.classList.remove('active');

        // Update text content
        if (this.authTitle) this.authTitle.textContent = 'Welcome back';
        if (this.authSubtitle) this.authSubtitle.textContent = 'Sign In to Nike';
        if (this.authFooterText) {
            this.authFooterText.innerHTML = 'Don\'t have an account? <a href="#" onclick="authManager.toggleForm()">Sign Up</a>';
        }

        // Update document title
        document.title = 'Nike - Sign In';
    }

    showSignUpForm() {
        // Toggle form visibility
        if (this.signInForm) this.signInForm.classList.remove('active');
        if (this.signUpForm) this.signUpForm.classList.add('active');

        // Update text content
        if (this.authTitle) this.authTitle.textContent = 'Join Nike';
        if (this.authSubtitle) this.authSubtitle.textContent = 'Create your Nike account';
        if (this.authFooterText) {
            this.authFooterText.innerHTML = 'Already have an account? <a href="#" onclick="authManager.toggleForm()">Sign In</a>';
        }

        // Update document title
        document.title = 'Nike - Sign Up';
    }

    handleSignIn(e) {
        e.preventDefault();

        const formData = new FormData(this.signInForm);
        const email = formData.get('email');
        const password = formData.get('password');

        // Validate form
        if (!this.validateSignInForm(email, password)) {
            return;
        }

        // Show loading state
        this.setFormLoading(true);

        // Simulate authentication process
        setTimeout(() => {
            this.processSignIn(email, password);
        }, 1500);
    }

    handleSignUp(e) {
        e.preventDefault();

        const formData = new FormData(this.signUpForm);
        const userData = {
            firstName: formData.get('firstName'),
            lastName: formData.get('lastName'),
            email: formData.get('email'),
            password: formData.get('password'),
            confirmPassword: formData.get('confirmPassword'),
            phone: formData.get('phone'),
            dateOfBirth: formData.get('dateOfBirth'),
            agreeToTerms: formData.get('agreeToTerms')
        };

        // Validate form
        if (!this.validateSignUpForm(userData)) {
            return;
        }

        // Show loading state
        this.setFormLoading(true);

        // Simulate registration process
        setTimeout(() => {
            this.processSignUp(userData);
        }, 2000);
    }

    validateSignInForm(email, password) {
        let isValid = true;

        // Clear previous errors
        this.clearErrors();

        if (!email) {
            this.showFieldError('email', 'Email is required');
            isValid = false;
        } else if (!this.validationRules.email.test(email)) {
            this.showFieldError('email', 'Please enter a valid email address');
            isValid = false;
        }

        if (!password) {
            this.showFieldError('password', 'Password is required');
            isValid = false;
        }

        return isValid;
    }

    validateSignUpForm(userData) {
        let isValid = true;

        // Clear previous errors
        this.clearErrors();

        // First name validation
        if (!userData.firstName?.trim()) {
            this.showFieldError('firstName', 'First name is required');
            isValid = false;
        }

        // Last name validation
        if (!userData.lastName?.trim()) {
            this.showFieldError('lastName', 'Last name is required');
            isValid = false;
        }

        // Email validation
        if (!userData.email) {
            this.showFieldError('email', 'Email is required');
            isValid = false;
        } else if (!this.validationRules.email.test(userData.email)) {
            this.showFieldError('email', 'Please enter a valid email address');
            isValid = false;
        }

        // Password validation
        if (!userData.password) {
            this.showFieldError('password', 'Password is required');
            isValid = false;
        } else if (!this.validationRules.password.test(userData.password)) {
            this.showFieldError('password', 'Password must be at least 8 characters with uppercase, lowercase, and number');
            isValid = false;
        }

        // Confirm password validation
        if (userData.password !== userData.confirmPassword) {
            this.showFieldError('confirmPassword', 'Passwords do not match');
            isValid = false;
        }

        // Terms agreement validation
        if (!userData.agreeToTerms) {
            this.showGeneralError('You must agree to the Terms and Conditions');
            isValid = false;
        }

        return isValid;
    }

    validateField(field) {
        const value = field.value.trim();
        const fieldName = field.name;

        this.clearFieldError(field);

        if (field.hasAttribute('required') && !value) {
            this.showFieldError(fieldName, `${this.getFieldLabel(fieldName)} is required`);
            return false;
        }

        if (value) {
            switch (field.type) {
                case 'email':
                    if (!this.validationRules.email.test(value)) {
                        this.showFieldError(fieldName, 'Please enter a valid email address');
                        return false;
                    }
                    break;
                case 'password':
                    if (!this.validationRules.password.test(value)) {
                        this.showFieldError(fieldName, 'Password must be at least 8 characters with uppercase, lowercase, and number');
                        return false;
                    }
                    break;
                case 'tel':
                    if (!this.validationRules.phone.test(value.replace(/\D/g, ''))) {
                        this.showFieldError(fieldName, 'Please enter a valid phone number');
                        return false;
                    }
                    break;
            }
        }

        return true;
    }

    getFieldLabel(fieldName) {
        const labels = {
            firstName: 'First name',
            lastName: 'Last name',
            email: 'Email',
            password: 'Password',
            confirmPassword: 'Confirm password',
            phone: 'Phone number'
        };
        return labels[fieldName] || fieldName;
    }

    showFieldError(fieldName, message) {
        const field = document.querySelector(`[name="${fieldName}"]`);
        if (!field) return;

        field.classList.add('error');

        // Remove existing error message
        const existingError = field.parentNode.querySelector('.error-message');
        if (existingError) existingError.remove();

        // Add new error message
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.textContent = message;
        field.parentNode.appendChild(errorDiv);
    }

    clearFieldError(field) {
        field.classList.remove('error');
        const errorMessage = field.parentNode.querySelector('.error-message');
        if (errorMessage) errorMessage.remove();
    }

    showGeneralError(message) {
        const errorContainer = document.createElement('div');
        errorContainer.className = 'general-error';
        errorContainer.innerHTML = `
            <i class="fas fa-exclamation-triangle"></i>
            <span>${message}</span>
        `;

        // Insert at the top of the active form
        const activeForm = this.isSignInMode ? this.signInForm : this.signUpForm;
        activeForm.insertBefore(errorContainer, activeForm.firstChild);

        // Auto-remove after 5 seconds
        setTimeout(() => {
            if (errorContainer.parentElement) {
                errorContainer.remove();
            }
        }, 5000);
    }

    clearErrors() {
        // Clear field errors
        const errorFields = document.querySelectorAll('.error');
        errorFields.forEach(field => field.classList.remove('error'));

        const errorMessages = document.querySelectorAll('.error-message');
        errorMessages.forEach(message => message.remove());

        // Clear general errors
        const generalErrors = document.querySelectorAll('.general-error');
        generalErrors.forEach(error => error.remove());
    }

    clearForms() {
        if (this.signInForm) this.signInForm.reset();
        if (this.signUpForm) this.signUpForm.reset();
    }

    setFormLoading(isLoading) {
        const submitButtons = document.querySelectorAll('button[type="submit"]');
        const forms = document.querySelectorAll('form');

        submitButtons.forEach(button => {
            if (isLoading) {
                button.disabled = true;
                button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';
            } else {
                button.disabled = false;
                button.innerHTML = this.isSignInMode ? 'Sign In' : 'Create Account';
            }
        });

        forms.forEach(form => {
            form.style.pointerEvents = isLoading ? 'none' : 'auto';
            form.style.opacity = isLoading ? '0.7' : '1';
        });
    }

    processSignIn(email, password) {
        // Simulate authentication
        if (email === 'demo@nike.com' && password === 'Demo123!') {
            this.showSuccessMessage('Welcome back! Redirecting to your account...');
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 2000);
        } else {
            this.setFormLoading(false);
            this.showGeneralError('Invalid email or password. Try demo@nike.com / Demo123!');
        }
    }

    processSignUp(userData) {
        // Simulate registration
        this.showSuccessMessage('Account created successfully! Redirecting...');

        // Save user data to localStorage for demo purposes
        localStorage.setItem('nikeUser', JSON.stringify({
            firstName: userData.firstName,
            lastName: userData.lastName,
            email: userData.email,
            registeredAt: new Date().toISOString()
        }));

        setTimeout(() => {
            window.location.href = 'index.html';
        }, 2000);
    }

    showSuccessMessage(message) {
        if (this.successMessage) {
            this.successMessage.innerHTML = `
                <div class="success-content">
                    <i class="fas fa-check-circle"></i>
                    <span>${message}</span>
                </div>
            `;
            this.successMessage.classList.add('show');
        }
    }

    injectStyles() {
        const style = document.createElement('style');
        style.textContent = `
            .error-message {
                color: #ee0005;
                font-size: 12px;
                margin-top: 4px;
                display: flex;
                align-items: center;
                gap: 4px;
            }

            .error-message::before {
                content: "⚠";
                font-size: 10px;
            }

            .form-group input.error,
            .form-group select.error {
                border-color: #ee0005 !important;
                box-shadow: 0 0 0 3px rgba(238, 0, 5, 0.1);
            }

            .general-error {
                background: #fee2e2;
                border: 1px solid #ee0005;
                color: #7f1d1d;
                padding: 12px 16px;
                border-radius: 8px;
                margin-bottom: 16px;
                display: flex;
                align-items: center;
                gap: 8px;
                animation: slideDown 0.3s ease-out;
            }

            .success-content {
                display: flex;
                align-items: center;
                gap: 12px;
                color: #10B981;
                font-weight: 500;
            }

            .success-content i {
                font-size: 20px;
            }

            .password-toggle {
                position: absolute;
                right: 12px;
                top: 50%;
                transform: translateY(-50%);
                background: none;
                border: none;
                color: #666;
                cursor: pointer;
                padding: 4px;
            }

            .password-toggle:hover {
                color: #111;
            }

            @keyframes slideDown {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
        `;
        document.head.appendChild(style);
    }
}

// Initialize authentication manager
let authManager;
document.addEventListener('DOMContentLoaded', () => {
    authManager = new AuthenticationManager();
});

// Global functions for backward compatibility
function toggleForm() {
    if (authManager) authManager.toggleForm();
}

function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    if (!input) return;
    if (input.type === "password") {
        input.type = "text";
    } else {
        input.type = "password";
    }
}
