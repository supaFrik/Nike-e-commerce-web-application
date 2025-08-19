<%@ include file="/WEB-INF/views/common/variables.jsp" %>

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
                
                <div class="footer-section contact-section">
                    <h4>Contact Us</h4>
                    <form class="contact-form" id="contactForm">
                        <div class="form-group">
                            <input type="text" id="firstName" name="firstName" placeholder="First Name" required>
                        </div>
                        <div class="form-group">
                            <input type="email" id="email" name="email" placeholder="Email Address" required>
                        </div>
                        <div class="form-group">
                            <textarea id="message" name="message" placeholder="Your Message" rows="3" required></textarea>
                        </div>
                        <button type="submit" class="contact-submit-btn">Send Message</button>
                    </form>
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