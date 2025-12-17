<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${product.name}" /> - Nike</title>

    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/product-detail.jsp" />
</head>
<body>
    <!-- Mobile Menu Overlay -->
    <main class="product-detail">
        <div class="container">
            <div class="product-layout">
                <div class="product-images">
                    <div class="image-preview">
                        <c:choose>
                            <c:when test="${not empty colors}">
                                <div class="thumbnail-nav">
                                    <c:forEach begin="1" end="10" var="i">
                                        <div class="thumbnail-item${i == 1 ? ' active' : ''}" data-index="${i - 1}" style="display: none;">
                                            <img src="${env}/images/products/${product.name}/${colors[0].folderPath}/${colors[0].baseImage}-${i}.avif"
                                                 alt="Ảnh sản phẩm ${i}"
                                                 onload="this.parentElement.style.display = 'block';"
                                                 onerror="this.parentElement.style.display = 'none';">
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="main-image-container">
                                    <div class="main-image">
                                        <img id="currentImage" src="${env}/images/products/${product.name}/${colors[0].folderPath}/${colors[0].baseImage}-1.avif" alt="<c:out value='${product.name}'/>">
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="main-image-container">
                                    <div class="main-image">
                                        <img id="currentImage" src="${env}/images/products/default-product.avif" alt="<c:out value='${product.name}'/>">
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="product-info">
                    <div class="product-header">
                        <h1 class="product-title">JA 3 'Light Show' EP</h1>
                        <p class="product-subtitle">Giày bóng rổ</p>
                        <div class="product-price">₫149</div>
                        <div class="color-options-shoes" style="margin: 24px 0 0 0; display: flex; gap: 16px;">
                            <div class="color-option selected">
                                <img src="assets/img/product-detail/NIKE GATO MAIN.avif" alt="White/Silver" class="color-option-img">
                            </div>
                            <div class="color-option">
                                <img src="assets/img/product-detail/NIKE GATO SIDE !.avif" alt="Black" class="color-option-img">
                            </div>
                            <div class="color-option">
                                <img src="assets/img/product-detail/NIKE GATO SIDE 2.avif" alt="Red" class="color-option-img">
                            </div>
                            <div class="color-option">
                                <img src="assets/img/product-detail/NIKE GATO UP.avif" alt="Pink" class="color-option-img">
                            </div>
                            <div class="color-option">
                                <img src="assets/img/product-detail/NIKE GATO DOWN.avif" alt="Blue" class="color-option-img">
                            </div>
                        </div>
                    </div>

                    <div class="size-selection">
                        <div class="size-header">
                            <h4>Chọn kích cỡ</h4>
                            <a href="#" class="size-guide">
                                <i class="fas fa-ruler"></i>
                                Hướng dẫn kích cỡ
                            </a>
                        </div>
                        <div class="size-options">
                            <div class="size-option unavailable" data-size="6" onclick="selectSize(this)">US 6</div>
                            <div class="size-option unavailable" data-size="6.5" onclick="selectSize(this)">US 6.5</div>
                            <div class="size-option unavailable" data-size="7" onclick="selectSize(this)">US 7</div>
                            <div class="size-option unavailable" data-size="7.5" onclick="selectSize(this)">US 7.5</div>
                            <div class="size-option" data-size="8" onclick="selectSize(this)">US M 8 / W 9.5</div>
                            <div class="size-option unavailable" data-size="8.5" onclick="selectSize(this)">US 8.5</div>
                            <div class="size-option unavailable" data-size="9" onclick="selectSize(this)">US 9</div>
                            <div class="size-option unavailable" data-size="9.5" onclick="selectSize(this)">US 9.5</div>
                            <div class="size-option unavailable" data-size="10" onclick="selectSize(this)">US 10</div>
                            <div class="size-option unavailable" data-size="10.5" onclick="selectSize(this)">US 10.5</div>
                            <div class="size-option unavailable" data-size="11" onclick="selectSize(this)">US 11</div>
                            <div class="size-option unavailable" data-size="11.5" onclick="selectSize(this)">US 11.5</div>
                            <div class="size-option unavailable" data-size="12" onclick="selectSize(this)">US 12</div>
                            <div class="size-option unavailable" data-size="12.5" onclick="selectSize(this)">US 12.5</div>
                            <div class="size-option unavailable" data-size="13" onclick="selectSize(this)">US 13</div>
                        </div>
                    </div>

                    <div class="product-actions">
                        <div class="primary-actions">
                            <button class="btn btn-primary btn-half add-to-cart" onclick="addToCart()">
                                Thêm vào giỏ
                            </button>
                            <button class="btn btn-secondary btn-half buy-now-btn" onclick="buyNow()">
                                Mua ngay
                            </button>
                        </div>
                        <button class="btn btn-outline btn-full wishlist-btn" onclick="addToWishlist()">
                            <i class="far fa-heart"></i>
                            Yêu thích
                        </button>
                    </div>

                    <div class="exclusion-notice">
                        <p>Sản phẩm này không tham gia các chương trình khuyến mãi và giảm giá trên trang.</p>
                    </div>

                    <div class="product-description">
                        <p>Mỗi lần Ja bước vào sân là một màn trình diễn. Để giúp anh ấy nâng tầm trò chơi, chúng tôi đã hợp tác để tạo nên phiên bản giày JA mang tính biểu tượng nhất từ trước đến nay. Nhẹ, thấp và siêu phản hồi, đôi giày được thiết kế xoay quanh thời gian bật nhảy và quãng di chuyển của anh ấy với lớp đệm Zoom X toàn phần. "Zoom phản hồi rất tốt," anh nói, "tôi cảm nhận rõ ràng điều đó!" Đây thực sự là tin xấu cho các hậu vệ. Với đế cao su siêu bền, phiên bản này mang lại độ bám lý tưởng cho sân ngoài trời.</p>

                        <ul class="product-features">
                            <li><strong>Màu hiển thị:</strong> White/Dusty Cactus/Hyper Pink/Star Blue</li>
                            <li><strong>Mã sản phẩm:</strong> HF2714-100</li>
                            <li><strong>Xuất xứ:</strong> China</li>
                        </ul>

                        <div class="product-details-link">
                            <a href="#" class="details-link">Xem chi tiết sản phẩm</a>
                        </div>
                    </div>
                </div>

                <div class="product-sections">
                    <div class="expandable-section">
                        <button class="section-header" onclick="toggleSection('delivery')">
                            <span>Giao hàng và đổi trả miễn phí</span>
                            <i class="fas fa-chevron-down"></i>
                        </button>
                        <div class="section-content" id="delivery">
                            <p>Đơn hàng từ S$75 trở lên được giao hàng tiêu chuẩn miễn phí.</p>

                            <ul class="delivery-details">
                                <li>Giao hàng tiêu chuẩn: 1–3 ngày làm việc</li>
                                <li>Giao nhanh: 0–2 ngày làm việc</li>
                            </ul>

                            <p>Đơn hàng được xử lý và giao từ Thứ Hai đến Thứ Sáu (trừ ngày lễ).</p>

                            <p>Thành viên Nike được <a href="#" class="link-underline">miễn phí trả hàng</a>. <a href="#" class="link-underline">Có áp dụng điều kiện</a>.</p>
                        </div>
                    </div>

                    <div class="expandable-section">
                        <button class="section-header" onclick="toggleSection('reviews')">
                            <span>Đánh giá (1)</span>
                            <div class="rating-stars-inline">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                            </div>
                            <i class="fas fa-chevron-down"></i>
                        </button>
                        <div class="section-content" id="reviews">
                            <div class="reviews-header">
                                <div class="overall-rating">
                                    <div class="rating-stars">
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                    </div>
                                    <span class="rating-text">5 sao</span>
                                </div>
                                <button class="write-review-btn">Viết đánh giá</button>
                            </div>

                            <div class="review-item">
                                <div class="review-header">
                                    <h4 class="review-title">thank you so much nike</h4>
                                </div>
                                <div class="review-rating">
                                    <div class="rating-stars">
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                        <i class="fas fa-star"></i>
                                    </div>
                                    <span class="review-author">losbañoslagunad309176653 - 05 Aug 2025</span>
                                </div>
                                <p class="review-text">nike shoes is just amazing and it has different design.i love nike shoes and other products</p>
                            </div>

                            <button class="more-reviews-btn">Xem thêm đánh giá</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- How Others Are Wearing It Section -->
        <div class="social-section">
            <div class="container">
                <h2>Mọi người đang phối đồ như thế nào</h2>
                <p>Tải ảnh của bạn hoặc nhắc @nike trên Instagram để có cơ hội được xuất hiện.</p>
                <button class="btn btn-outline upload-btn">
                    Tải ảnh của bạn
                </button>
                <div class="social-carousel">
                    <button type="button" class="carousel-btn prev" id="socialPrevBtn" aria-label="Trước">
                        <i class="fas fa-chevron-left"></i>
                    </button>
                    <div class="social-viewport" id="socialViewport">
                        <div class="social-track" id="socialTrack">
                        <figure id="wear-1" class="social-slide">
                            <img src="./assets/img/best-trail-running-shoes-by-nike.jpg" alt="Member style 1">
                            <figcaption class="social-caption">@lanilove_</figcaption>
                        </figure>
                        <figure id="wear-2" class="social-slide">
                            <img src="./assets/img/member/member-2.jpg" alt="Member style 2">
                            <figcaption class="social-caption">@fayeknightly_</figcaption>
                        </figure>
                        <figure id="wear-3" class="social-slide">
                            <img src="./assets/img/member/member-3.jpg" alt="Member style 3">
                            <figcaption class="social-caption">@_alexandramarasigan</figcaption>
                        </figure>
                        <figure id="wear-4" class="social-slide">
                            <img src="./assets/img/member/member-4.jpg" alt="Member style 4">
                            <figcaption class="social-caption">@streetfit</figcaption>
                        </figure>
                        <figure id="wear-5" class="social-slide">
                            <img src="./assets/img/member/member-5.jpg" alt="Member style 5">
                            <figcaption class="social-caption">@runclub</figcaption>
                        </figure>
                        </div>
                    </div>
                    <button type="button" class="carousel-btn next" id="socialNextBtn" aria-label="Sau">
                        <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
            </div>
        </div>

        <!-- You Might Also Like Section -->
        <section class="recommendations-section">
            <div class="container">
                <h2 class="section-title">Có thể bạn sẽ thích</h2>
                <div class="recommendations-carousel">
                    <button class="carousel-btn prev" id="recommendationsPrevBtn" aria-label="Gợi ý trước">
                        <i class="fas fa-chevron-left"></i>
                    </button>

                    <div class="recommendations-wrapper">
                        <div class="recommendations-track" id="recommendationsTrack">
                            <div class="recommendation-card">
                                <div class="product-image-slider">
                                    <div class="product-image-track" id="productSlider0">
                                        <img src="./assets/img/products/AIR+FORCE+1+'07.avif" alt="Nike Air Force 1 '07" class="product-slide active">
                                        <img src="./assets/img/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Force 1 '07 Side" class="product-slide">
                                        <img src="./assets/img/products/AIR+MAX+90+G.avif" alt="Nike Air Force 1 '07 Detail" class="product-slide">
                                    </div>
                                    <div class="slider-dots">
                                        <span class="dot active" onclick="currentSlide(1, 0)"></span>
                                        <span class="dot" onclick="currentSlide(2, 0)"></span>
                                        <span class="dot" onclick="currentSlide(3, 0)"></span>
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title">Nike Air Force 1 '07</h3>
                                    <p class="product-type">Giày nam</p>
                                    <p class="product-price">₫90</p>
                                </div>
                            </div>
                            <div class="recommendation-card">
                                <div class="product-image-slider">
                                    <div class="product-image-track" id="productSlider1">
                                        <img src="./assets/img/products/AIR+JORDAN+1+LOW.avif" alt="Air Jordan 1 Low" class="product-slide active">
                                        <img src="./assets/img/products/AIR+MAX+DN8.avif" alt="Air Jordan 1 Low Side" class="product-slide">
                                        <img src="./assets/img/products/BLAZER+MID+'77+VNTG.avif" alt="Air Jordan 1 Low Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title">Air Jordan 1 Low</h3>
                                    <p class="product-type">Giày nam</p>
                                    <p class="product-price">₫110</p>
                                </div>
                            </div>
                            <div class="recommendation-card">
                                <div class="product-image-slider">
                                    <div class="product-image-track" id="productSlider2">
                                        <img src="./assets/img/products/AIR+MAX+90+G.avif" alt="Nike Air Max 90" class="product-slide active">
                                        <img src="./assets/img/products/AIR+MAX+DN8.avif" alt="Nike Air Max 90 Side" class="product-slide">
                                        <img src="./assets/img/products/AIR+FORCE+1+'07.avif" alt="Nike Air Max 90 Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title">Nike Air Max 90</h3>
                                    <p class="product-type">Giày nam</p>
                                    <p class="product-price">₫120</p>
                                </div>
                            </div>
                            <div class="recommendation-card">
                                <div class="product-image-slider">
                                    <div class="product-image-track" id="productSlider3">
                                        <img src="./assets/img/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Blazer Mid '77 Vintage" class="product-slide active">
                                        <img src="./assets/img/products/AIR+JORDAN+1+LOW.avif" alt="Nike Blazer Mid '77 Side" class="product-slide">
                                        <img src="./assets/img/products/AIR+MAX+DN8.avif" alt="Nike Blazer Mid '77 Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title">Nike Blazer Mid '77 Vintage</h3>
                                    <p class="product-type">Giày nam</p>
                                    <p class="product-price">₫100</p>
                                </div>
                            </div>
                            <div class="recommendation-card">
                                <div class="product-image-slider">
                                    <div class="product-image-track" id="productSlider4">
                                        <img src="./assets/img/products/AIR+MAX+DN8.avif" alt="Nike Air Max DN" class="product-slide active">
                                        <img src="./assets/img/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Max DN Side" class="product-slide">
                                        <img src="./assets/img/products/BLAZER+MID+'77+VNTG.avif" alt="Nike Air Max DN Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title">Nike Air Max DN</h3>
                                    <p class="product-type">Giày nam</p>
                                    <p class="product-price">₫150</p>
                                </div>
                            </div>
                            <div class="recommendation-card">
                                <div class="product-image-slider">
                                    <div class="product-image-track" id="productSlider5">
                                        <img src="./assets/img/products/AIR+FORCE+1+'07 (1).avif" alt="Nike Air Force 1 '07 LV8" class="product-slide active">
                                        <img src="./assets/img/products/AIR+JORDAN+1+LOW.avif" alt="Nike Air Force 1 LV8 Side" class="product-slide">
                                        <img src="./assets/img/products/AIR+MAX+90+G.avif" alt="Nike Air Force 1 LV8 Detail" class="product-slide">
                                    </div>
                                </div>
                                <div class="product-info">
                                    <h3 class="product-title">Nike Air Force 1 '07 LV8</h3>
                                    <p class="product-type">Giày nam</p>
                                    <p class="product-price">₫110</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <button class="carousel-btn next" id="recommendationsNextBtn" aria-label="Gợi ý sau">
                        <i class="fas fa-chevron-right"></i>
                    </button>
                </div>
            </div>
        </section>
    </main>

    <!-- JS Files -->
    <script src="scripts/pages/main.js"></script>
    <script src="scripts/components/carousel.js"></script>

    <!-- Thumbnail Navigation JS -->
    <script src="scripts/components/thumbnailScope.js"></script>
</body>
</html>

