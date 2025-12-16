<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nike - Đăng nhập</title>
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/auth.jsp" />
</head>
<body>
    <div class="auth-container" role="main" aria-labelledby="auth-main-title">
        <div class="auth-form-section" role="region" aria-labelledby="auth-header-title">
            <div class="auth-header">
                <div class="auth-logo">
                    <a href="${env}/" aria-label="Go to Nike homepage">
                        <img src="${env}/images/e0891c394d4f7b7c09e783e29df07505.png" alt="Nike logo">
                    </a>
                </div>
                <h1 class="auth-title" id="authTitle" aria-live="polite">Chào mừng bạn quay trở lại!</h1>
                <p class="auth-subtitle" id="authSubtitle" aria-describedby="auth-main-title">Đăng nhập</p>
                <span id="auth-main-title" class="sr-only">Trang bảo mật của Nike để đăng nhập</span>
            </div>

            <!-- Global signup / login messages -->
            <c:if test="${not empty signupSuccess}">
                <div class="success-message" role="status" aria-live="polite">${signupSuccess}</div>
            </c:if>
            <c:if test="${not empty signupError}">
                <div class="error-message" role="alert" aria-live="assertive">${signupError}</div>
            </c:if>

            <!-- Sign In Form -->
            <c:set var="_csrf" value="${_csrf}" />
            <form class="auth-form form-toggle active" id="signInForm" method="post" action="${pageContext.request.contextPath}/login">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <c:if test="${not empty param.error}">
                    <div class="error-message" role="alert" aria-live="assertive">
                        Invalid username or password. Please try again.
                        <a href="${env}/forgot-password" class="forgot-link" aria-describedby="forgot-password-desc">Quên mật khẩu?</a>
                    </div>
                </c:if>
                <div class="form-group">
                    <label class="form-label" for="signInEmail">Địa chỉ email</label>

                    <input type="email" class="form-input" id="signInEmail" name="username" placeholder="Địa chỉ email" required autocomplete="email">
                    <div class="error-message" id="signInEmailError"></div>
                </div>
                <div class="form-group">
                    <label class="form-label" for="signInPassword">Mật khẩu</label>
                    <div class="password-container">
                        <input type="password" name="password" class="form-input" id="signInPassword" placeholder="Mật khẩu" required autocomplete="current-password">
                        <button type="button" class="password-toggle" onclick="togglePassword('signInPassword')" aria-label="Toggle password visibility" aria-pressed="false">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true" style="cursor: pointer; transition: transform 0.2s ease;">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                    <div class="error-message" id="signInPasswordError" role="alert" aria-live="polite"></div>
                </div>
                <div class="forgot-password">
                    <a href="#" onclick="showForgotPassword()" aria-describedby="forgot-password-desc">Quên mật khẩu?</a>
                    <span id="forgot-password-desc" class="sr-only">Đặt lại mật khẩu nếu bạn quên</span>
                </div>
                <button type="submit" class="btn-primary">Đăng nhập</button>
                <div class="auth-divider" role="separator" aria-label="Alternative sign in options">
                    <span class="auth-divider-text">hoặc đăng nhập bằng</span>
                </div>

                <div class="social-login" role="group" aria-labelledby="social-signin-title">
                    <h3 id="social-signin-title" class="sr-only">Social Sign In Options</h3>
                    <a href="#" class="social-btn" aria-label="Đăng nhập với Facebook" aria-describedby="facebook-signin-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="#1877F2" aria-hidden="true">
                            <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                    </a>
                    <span id="facebook-signin-desc" class="sr-only">Đăng nhập bằng tài khoản Facebook của bạn</span>

                    <a href="#" class="social-btn" aria-label="Đăng nhập với Google" aria-describedby="google-signin-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" aria-hidden="true">
                            <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                            <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                            <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                            <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                        </svg>
                    </a>
                    <span id="google-signin-desc" class="sr-only">Đăng nhập bằng tài khoản Google của bạn</span>

                    <a href="#" class="social-btn" aria-label="Đăng nhập với Apple" aria-describedby="apple-signin-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                            <path d="M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.81-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M13 3.5c.73-.83 1.94-1.46 2.94-1.5.13 1.17-.34 2.35-1.04 3.19-.69.85-1.83 1.51-2.95 1.42-.15-1.15.41-2.35 1.05-3.11z"/>
                        </svg>
                    </a>
                    <span id="apple-signin-desc" class="sr-only">Đăng nhập bằng Apple ID của bạn</span>
                </div>
            </form>

            <!-- Sign Up Form -->
            <form class="auth-form form-toggle" id="signUpForm" method="post" action="${env}/auth/signup">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <c:set var="usernameHasError" value="${not empty org.springframework.validation.BindingResult.signupForm.fieldErrors['username']}" />
                <c:set var="emailHasError" value="${not empty org.springframework.validation.BindingResult.signupForm.fieldErrors['email']}" />
                <c:set var="passwordHasError" value="${not empty org.springframework.validation.BindingResult.signupForm.fieldErrors['password']}" />
                <c:set var="confirmPasswordHasError" value="${not empty org.springframework.validation.BindingResult.signupForm.fieldErrors['confirmPassword'] or not empty org.springframework.validation.BindingResult.signupForm.globalErrors}" />
                <div class="form-group">
                    <label class="form-label" for="signUpName">Tên của bạn</label>
                    <input type="text" name="username" class="form-input" id="signUpName" placeholder="Tên của bạn"
                           value="${signupForm.username}" required autocomplete="name" aria-describedby="signUpNameError">
                    <div id="signUpNameError" class="error-message" role="alert" aria-live="assertive"
                         style="${usernameHasError ? '' : 'display:none;'} color:#d32f2f; margin-top:4px;">
                        <c:if test="${usernameHasError}">
                            ${org.springframework.validation.BindingResult.signupForm.fieldErrors['username'][0].defaultMessage}
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label" for="signUpEmail">Email</label>
                    <input type="email" name="email" class="form-input" id="signUpEmail" placeholder="Địa chỉ email"
                           value="${signupForm.email}" required autocomplete="email" aria-describedby="signUpEmailError">
                    <div id="signUpEmailError" class="error-message" role="alert" aria-live="assertive"
                         style="${emailHasError ? '' : 'display:none;'} color:#d32f2f; margin-top:4px;">
                        <c:if test="${emailHasError}">
                            ${org.springframework.validation.BindingResult.signupForm.fieldErrors['email'][0].defaultMessage}
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label" for="signUpPassword">Mật khẩu</label>
                    <div id="signUpPasswordError" class="error-message" role="alert" aria-live="assertive" style="display:none;"></div>
                    <div class="password-container">
                        <input type="password" name="password" class="form-input" id="signUpPassword" placeholder="Mật khẩu" required autocomplete="new-password" aria-describedby="signUpPasswordError">
                        <c:if test="${not empty org.springframework.validation.BindingResult.signupForm.fieldErrors['password']}">
                            <script>
                                document.addEventListener('DOMContentLoaded', function(){
                                    var pwdErr = document.getElementById('signUpPasswordError');
                                    if(pwdErr){
                                        pwdErr.style.display='block';
                                        pwdErr.textContent='${org.springframework.validation.BindingResult.signupForm.fieldErrors['password'][0].defaultMessage}';
                                    }
                                });
                            </script>
                        </c:if>
                        <button type="button" class="password-toggle" onclick="togglePassword('signUpPassword')" aria-label="Toggle password visibility" aria-pressed="false">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true" style="cursor: pointer; transition: transform 0.2s ease;">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label" for="signUpConfirmPassword">Xác nhận mật khẩu</label>
                    <div class="password-container">
                        <input type="password" name="confirmPassword" class="form-input" id="signUpConfirmPassword" placeholder="Xác nhận mật khẩu" required autocomplete="new-password" aria-describedby="signUpConfirmPasswordError">
                        <button type="button" class="password-toggle" onclick="togglePassword('signUpConfirmPassword')" aria-label="Toggle confirm password visibility" aria-pressed="false">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true" style="cursor: pointer; transition: transform 0.2s ease;">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                    <div id="signUpConfirmPasswordError" class="error-message" role="alert" aria-live="assertive" style="display:none; color:#d32f2f; margin-top:4px;"></div>
                </div>

                <button type="submit" class="btn-primary">Đăng ký</button>
                <div class="auth-divider" role="separator" aria-label="Alternative sign up options">
                    <span class="auth-divider-text">hoặc đăng ký bằng</span>
                </div>

                <div class="social-login" role="group" aria-labelledby="social-signup-title">
                    <h3 id="social-signup-title" class="sr-only">Social Sign Up Options</h3>
                    <a href="#" class="social-btn" onclick="socialLogin('facebook')" 
                       aria-label="Đăng ký với Facebook" aria-describedby="facebook-signup-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="#1877F2" aria-hidden="true">
                            <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/>
                        </svg>
                    </a>
                    <span id="facebook-signup-desc" class="sr-only">Tạo tài khoản bằng Facebook</span>

                    <a href="#" class="social-btn" onclick="socialLogin('google')" 
                       aria-label="Đăng ký với Google" aria-describedby="google-signup-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" aria-hidden="true">
                            <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                            <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                            <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
                            <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
                        </svg>
                    </a>
                    <span id="google-signup-desc" class="sr-only">Tạo tài khoản bằng Google</span>

                    <a href="#" class="social-btn" onclick="socialLogin('apple')" 
                       aria-label="Đăng ký với Apple" aria-describedby="apple-signup-desc">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                            <path d="M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.81-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M13 3.5c.73-.83 1.94-1.46 2.94-1.5.13 1.17-.34 2.35-1.04 3.19-.69.85-1.83 1.51-2.95 1.42-.15-1.15.41-2.35 1.05-3.11z"/>
                        </svg>
                    </a>
                    <span id="apple-signup-desc" class="sr-only">Tạo tài khoản bằng Apple ID</span>
                </div>
            </form>

            <c:if test="${usernameHasError or emailHasError or passwordHasError or confirmPasswordHasError}">
                <script>
                    document.addEventListener('DOMContentLoaded', function(){
                        if(typeof toggleToSignUp === 'function'){ toggleToSignUp(); }
                    });
                </script>
            </c:if>

            <div class="auth-footer" role="region" aria-labelledby="auth-footer-title">
                <p id="authFooterText">
                    Chưa có tài khoản? <a href="#" id="toggleAuthLink">Đăng ký</a>
                </p>
            </div>
        </div>
        <div class="auth-image-section" role="complementary" aria-labelledby="auth-image-title">
            <div class="auth-image-logo">
                <img src="${env}/images/e0891c394d4f7b7c09e783e29df07505.png" alt="Nike logo">
            </div>
        </div>
    </div>

    <!-- Toast container + styles -->
    <div id="serverLoginError" style="display:none">${fn:escapeXml(loginError)}</div>
    <div id="toast" aria-live="polite" aria-atomic="true"></div>

    <script>
      window.__AUTH_INIT = {
        serverLoginError: `${fn:escapeXml(loginError)}` || '',
        signupError: `${fn:escapeXml(signupError)}` || '',
        signupSuccess: `${fn:escapeXml(signupSuccess)}` || '',
        search: window.location ? window.location.search : ''
      };
    </script>
    <script src="${env}/js/customer/pages/auth.js"></script>
</body>
</html>
