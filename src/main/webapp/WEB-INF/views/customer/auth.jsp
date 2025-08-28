<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nike - Sign In</title>
    
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/auth.jsp" />
    
    <link rel="icon" href="${env}/customer/img/e0891c394d4f7b7c09e783e29df07505.png">
</head>
<body>
    <div class="auth-container" role="main" aria-labelledby="auth-main-title">
        <!-- Left side - Form section -->
        <div class="auth-form-section" role="region" aria-labelledby="auth-header-title">
            <div class="auth-header">
                <div class="auth-logo">
                    <a href="${env}/" aria-label="Go to Nike homepage">
                        <img src="${env}/customer/img/e0891c394d4f7b7c09e783e29df07505.png" alt="Nike logo">
                    </a>
                </div>
                <h1 class="auth-title" id="authTitle" aria-live="polite">Welcome back</h1>
                <p class="auth-subtitle" id="authSubtitle" aria-describedby="auth-main-title">Sign In to Nike</p>
                <span id="auth-main-title" class="sr-only">Nike authentication page for signing in or creating an account</span>
            </div>

            <!-- Success message -->
            <div class="success-message" id="successMessage" role="status" aria-live="polite" aria-atomic="true"></div>

            <!-- Sign In Form -->
            <form class="auth-form form-toggle active" id="signInForm" role="form" aria-labelledby="signin-form-title" aria-describedby="signin-form-desc" method ="post" action="${env}/login">
                <h2 id="signin-form-title" class="sr-only">Sign In Form</h2>
                <span id="signin-form-desc" class="sr-only">Enter your email and password to sign in to your Nike account</span>
                
                <div class="form-group">
                    <label class="form-label" for="signInEmail">Email address</label>
                    <input type="email" name = "username" class="form-input" id="signInEmail" placeholder="Email address"
                           required aria-required="true" aria-describedby="signInEmailError"
                           aria-invalid="false" autocomplete="email">
                    <div class="error-message" id="signInEmailError" role="alert" aria-live="polite"></div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="signInPassword">Password</label>
                    <div class="password-container">
                        <input type="password" name = "password" class="form-input" id="signInPassword" placeholder="Password"
                               required aria-required="true" aria-describedby="signInPasswordError password-toggle-desc"
                               aria-invalid="false" autocomplete="current-password">
                        <button type="button" class="password-toggle" onclick="togglePassword('signInPassword')"
                                aria-label="Toggle password visibility" aria-describedby="password-toggle-desc"
                                aria-pressed="false">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                        <span id="password-toggle-desc" class="sr-only">Click to show or hide password</span>
                    </div>
                    <div class="error-message" id="signInPasswordError" role="alert" aria-live="polite"></div>
                </div>

                <div class="forgot-password">
                    <a href="#" onclick="showForgotPassword()" aria-describedby="forgot-password-desc">Forgot password?</a>
                    <span id="forgot-password-desc" class="sr-only">Reset your password if you've forgotten it</span>
                </div>

                <button type="submit" class="btn-primary" aria-describedby="signin-btn-desc">Sign In</button>
                <span id="signin-btn-desc" class="sr-only">Sign in to your Nike account</span>

                <div class="auth-divider" role="separator" aria-label="Alternative sign in options">
                    <span class="auth-divider-text">or sign in with</span>
                </div>

                <div class="social-login" role="group" aria-labelledby="social-signin-title">
                    <h3 id="social-signin-title" class="sr-only">Social Sign In Options</h3>
                    <a href="#" class="social-btn" onclick="socialLogin('facebook')" 
                       aria-label="Sign in with Facebook" aria-describedby="facebook-signin-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="#1877F2" aria-hidden="true">
                            <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                        </svg>
                    </a>
                    <span id="facebook-signin-desc" class="sr-only">Sign in using your Facebook account</span>
                    
                    <a href="#" class="social-btn" onclick="socialLogin('google')" 
                       aria-label="Sign in with Google" aria-describedby="google-signin-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" aria-hidden="true">
                            <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                            <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                            <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                            <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                        </svg>
                    </a>
                    <span id="google-signin-desc" class="sr-only">Sign in using your Google account</span>
                    
                    <a href="#" class="social-btn" onclick="socialLogin('apple')" 
                       aria-label="Sign in with Apple" aria-describedby="apple-signin-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                            <path d="M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.81-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M13 3.5c.73-.83 1.94-1.46 2.94-1.5.13 1.17-.34 2.35-1.04 3.19-.69.85-1.83 1.51-2.95 1.42-.15-1.15.41-2.35 1.05-3.11z"/>
                        </svg>
                    </a>
                    <span id="apple-signin-desc" class="sr-only">Sign in using your Apple ID</span>
                </div>
            </form>

            <!-- Sign Up Form -->
            <form class="auth-form form-toggle" id="signUpForm" role="form" aria-labelledby="signup-form-title" aria-describedby="signup-form-desc" method = "post" action="${env}/signup">
                <h2 id="signup-form-title" class="sr-only">Sign Up Form</h2>
                <span id="signup-form-desc" class="sr-only">Create a new Nike account by providing your name, email, and password</span>
                
                <div class="form-group">
                    <label class="form-label" for="signUpName">Your name</label>
                    <input type="text" name = "name" class="form-input" id="signUpName" placeholder="Your name"
                           required aria-required="true" aria-describedby="signUpNameError"
                           aria-invalid="false" autocomplete="name">
                    <div class="error-message" id="signUpNameError" role="alert" aria-live="polite"></div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="signUpEmail">Email address</label>
                    <input type="email" name = "email" class="form-input" id="signUpEmail" placeholder="Email address"
                           required aria-required="true" aria-describedby="signUpEmailError"
                           aria-invalid="false" autocomplete="email">
                    <div class="error-message" id="signUpEmailError" role="alert" aria-live="polite"></div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="signUpPassword">Password</label>
                    <div class="password-container">
                        <input type="password" name = "password" class="form-input" id="signUpPassword" placeholder="Password"
                               required aria-required="true" aria-describedby="signUpPasswordError password-toggle-signup-desc"
                               aria-invalid="false" autocomplete="new-password">
                        <button type="button" class="password-toggle" onclick="togglePassword('signUpPassword')"
                                aria-label="Toggle password visibility" aria-describedby="password-toggle-signup-desc"
                                aria-pressed="false">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                        <span id="password-toggle-signup-desc" class="sr-only">Click to show or hide password</span>
                    </div>
                    <div class="error-message" id="signUpPasswordError" role="alert" aria-live="polite"></div>
                </div>

                <button type="submit" class="btn-primary" aria-describedby="signup-btn-desc">Sign Up</button>
                <span id="signup-btn-desc" class="sr-only">Create your Nike account</span>

                <div class="auth-divider" role="separator" aria-label="Alternative sign up options">
                    <span class="auth-divider-text">or sign up with</span>
                </div>

                <div class="social-login" role="group" aria-labelledby="social-signup-title">
                    <h3 id="social-signup-title" class="sr-only">Social Sign Up Options</h3>
                    <a href="#" class="social-btn" onclick="socialLogin('facebook')" 
                       aria-label="Sign up with Facebook" aria-describedby="facebook-signup-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="#1877F2" aria-hidden="true">
                            <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                        </svg>
                    </a>
                    <span id="facebook-signup-desc" class="sr-only">Create account using your Facebook account</span>
                    
                    <a href="#" class="social-btn" onclick="socialLogin('google')" 
                       aria-label="Sign up with Google" aria-describedby="google-signup-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" aria-hidden="true">
                            <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                            <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                            <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                            <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                        </svg>
                    </a>
                    <span id="google-signup-desc" class="sr-only">Create account using your Google account</span>
                    
                    <a href="#" class="social-btn" onclick="socialLogin('apple')" 
                       aria-label="Sign up with Apple" aria-describedby="apple-signup-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                            <path d="M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.81-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M13 3.5c.73-.83 1.94-1.46 2.94-1.5.13 1.17-.34 2.35-1.04 3.19-.69.85-1.83 1.51-2.95 1.42-.15-1.15.41-2.35 1.05-3.11z"/>
                        </svg>
                    </a>
                    <span id="apple-signup-desc" class="sr-only">Create account using your Apple ID</span>
                </div>
            </form>

            <div class="auth-footer" role="region" aria-labelledby="auth-footer-title">
                <h3 id="auth-footer-title" class="sr-only">Account Navigation</h3>
                <p id="authFooterText">
                    Have an account? <a href="#" onclick="toggleForm()" aria-describedby="toggle-form-desc">Sign In</a>
                    <span id="toggle-form-desc" class="sr-only">Switch between sign in and sign up forms</span>
                </p>
            </div>
        </div>

        <!-- Right side - Image section -->
        <div class="auth-image-section" role="complementary" aria-labelledby="auth-image-title">
            <h2 id="auth-image-title" class="sr-only">Nike Brand Section</h2>
            <div class="auth-image-logo">
                <img src="${env}/customer/img/e0891c394d4f7b7c09e783e29df07505.png" alt="Nike logo" 
                     aria-describedby="auth-image-desc">
                <span id="auth-image-desc" class="sr-only">Nike swoosh logo displayed on the authentication page</span>
            </div>
        </div>
    </div>

    <script src="${env}/customer/scripts/auth.js"></script>
</body>
</html>