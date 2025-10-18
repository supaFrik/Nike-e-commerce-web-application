<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Footer -->
<footer class="footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h4>Find A Store</h4>
                    <ul>
                        <li><a href="#">Store Locator</a></li>
                        <li><a href="#">Nike App</a></li>
                        <li><a href="#">Nike Run Club</a></li>
                        <li><a href="#">Nike Training Club</a></li>
                    </ul>
                </div>
                
                <div class="footer-section">
                    <h4>Get Help</h4>
                    <ul>
                        <li><a href="#">Order Status</a></li>
                        <li><a href="#">Delivery</a></li>
                        <li><a href="#">Returns</a></li>
                        <li><a href="#">Size Guide</a></li>
                    </ul>
                </div>
                
                <div class="footer-section">
                    <h4>About Nike</h4>
                    <ul>
                        <li><a href="#">News</a></li>
                        <li><a href="#">Careers</a></li>
                        <li><a href="#">Investors</a></li>
                        <li><a href="#">Sustainability</a></li>
                    </ul>
                </div>
                
                <!-- Contact Form -->
                
                <div class="footer-section contact-section" id="footer-contact">
                    <h4>Contact Us</h4>
                    <form:form modelAttribute="contactForm" action="${env}/contact" method="POST" class="contact-form" id="contactForm">
                        <c:if test="${not empty _csrf}">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        </c:if>
                        <div class="form-group">
                            <form:input path="name" type="text" id="name" name="name" placeholder="Your Name (optional)" />
                            <form:errors path="name" cssClass="error-message" />
                        </div>
                        <div class="form-group">
                            <form:input path="email" type="email" id="email" name="email" placeholder="Email (optional, for reply)" />
                            <form:errors path="email" cssClass="error-message" />
                        </div>
                        <div class="form-group">
                            <form:textarea path="message" id="message" name="message" placeholder="Your Message" rows="3" required="required"></form:textarea>
                            <form:errors path="message" cssClass="error-message" />
                        </div>
                        <button type="submit" class="contact-submit-btn">Send Message</button>
                        <!-- Capture BindingResult (if flashed) safely using bracket notation -->
                        <c:set var="contactErrors" value="${requestScope['org.springframework.validation.BindingResult.contactForm']}" />
                        <c:if test="${not empty contactErrors and contactErrors.errorCount > 0}">
                            <p class="error-message" style="color:#c00;font-weight:600;">Please fix the errors above.</p>
                        </c:if>
                        <c:if test="${not empty successMessage}">
                            <p style="display:none;" class="success-message">${successMessage}</p>
                        </c:if>
                    </form:form>
                    <!-- Toast container -->
                    <div id="contactToast" class="contact-toast" style="display:none;position:fixed;bottom:1.5rem;right:1.5rem;background:#111;color:#fff;padding:1rem 1.25rem;border-radius:8px;box-shadow:0 4px 16px rgba(0,0,0,.3);font-size:.95rem;z-index:1000;opacity:0;transform:translateY(20px);transition:opacity .3s ease,transform .3s ease;">
                        <span id="contactToastMsg"></span>
                        <button type="button" id="contactToastClose" style="background:none;border:none;color:#fff;margin-left:1rem;font-size:1rem;cursor:pointer;">×</button>
                    </div>
                    <script>
                        (function(){
                            var msgEl = document.querySelector('#footer-contact .success-message');
                            if(msgEl){
                                var toast = document.getElementById('contactToast');
                                var txt = document.getElementById('contactToastMsg');
                                txt.textContent = msgEl.textContent.trim();
                                toast.style.display='flex';
                                requestAnimationFrame(function(){toast.style.opacity='1';toast.style.transform='translateY(0)';});
                                var hide=function(){toast.style.opacity='0';toast.style.transform='translateY(20px)';setTimeout(function(){toast.style.display='none';},300);};
                                document.getElementById('contactToastClose').addEventListener('click', hide);
                                setTimeout(hide, 4000);
                            }
                        })();
                    </script>
                </div>
                
                <!-- Social  -->
                
                <div class="footer-section">
                    <div class="social-links">
                        <a href="#" class="social-link"><i class="fab fa-twitter"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-facebook"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-youtube"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-instagram"></i></a>
                    </div>
                </div>
            </div>
            
            <div class="footer-bottom">
                <div class="footer-bottom-left">
                    <p>&copy; 2024 Nike, Inc. All Rights Reserved</p>
                    <div class="footer-links">
                        <a href="#">Privacy Policy</a>
                        <a href="#">Terms of Use</a>
                        <a href="#">CA Supply Chains Act</a>
                    </div>
                </div>
            </div>
        </div>
    </footer>