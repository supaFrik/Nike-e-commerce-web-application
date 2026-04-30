<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ | Thành viên Nike</title>

    <!-- SEO Meta Tags -->
    <meta name="description" content="Hồ sơ thành viên Nike - Quản lý sở thích, xem yêu thích và khám phá quyền lợi dành cho thành viên">
    <meta name="keywords" content="Nike, hồ sơ, thành viên, yêu thích, ưu đãi, thể thao">

    <!-- Open Graph Meta Tags -->
    <meta property="og:title" content="Hồ sơ thành viên Nike">
    <meta property="og:description" content="Quản lý hồ sơ thành viên Nike và khám phá nội dung được cá nhân hóa">
    <meta property="og:type" content="website">

    <!-- CSS includes -->
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/profile.jsp" />

    <!-- JavaScript includes -->
    <script src="${env}/js/customer/components/carousel.js" defer></script>
</head>

<body>
        <!-- Header Navigation -->
        <header role="banner" aria-label="Site header">
            <jsp:include page="/WEB-INF/views/customer/layout/header.jsp" />
        </header>

        <!-- Main Content Area -->
        <main class="container" role="main" aria-label="Nội dung chính - Hồ sơ">
            <section class="profile-header" aria-label="Thông tin hồ sơ">
                <div class="profile-avatar" aria-hidden="true">
                    <svg width="80" height="80" viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <circle cx="40" cy="32" r="18" fill="#ddd" />
                        <ellipse cx="40" cy="62" rx="22" ry="12" fill="#eee" />
                    </svg>
                </div>
                <div class="profile-info">
                    <div class="profile-name"><c:out value="${profile.name}" /></div>
                    <div class="profile-member">Thành viên Nike từ <c:out value="${profile.memberSince}" /></div>
                </div>
            </section>
            <nav class="profile-nav" role="navigation" aria-label="Điều hướng hồ sơ">
                <a href="#" class="active" aria-current="page">Hồ sơ</a>
                <a href="#">Hộp thư</a>
                <a href="#">Đơn hàng</a>
                <a href="#">Yêu thích</a>
                <a href="#">Cài đặt</a>
            </nav>
            <section class="interests-section">
                        <div class="interests-header">
                            <div class="interests-title">Sở thích</div>
                            <div class="interests-edit">Chỉnh sửa</div>
                        </div>
                            <ul class="interests-tabs tab">
                                <li class="interests-tab active"><a href="#tab-all">Tất cả</a></li>
                                <li class="interests-tab"><a href="#tab-sports">Thể thao</a></li>
                                <li class="interests-tab"><a href="#tab-products">Sản phẩm</a></li>
                                <li class="interests-tab"><a href="#tab-teams">Đội</a></li>
                                <li class="interests-tab"><a href="#tab-athletes">Vận động viên</a></li>
                                <li class="interests-tab"><a href="#tab-cities">Thành phố</a></li>
                            </ul>
                        <div class="interests-desc">
                            Thêm sở thích của bạn để mua sắm bộ sưu tập dựa trên những gì bạn quan tâm.
                        </div>
                        <div class="tab-content">
                            <ul class="interests-contents contents">
                                <li class="active" id="tab-all">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Thêm sở thích</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-sports">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Thêm thể thao</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-products">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Thêm sản phẩm</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-teams">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Thêm đội</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-athletes">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Thêm vận động viên</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-cities">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Thêm thành phố</span>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </section>
            <!-- Favourites Section -->
            <section class="favourites-section" aria-label="Danh sách yêu thích">
                <div class="favourites-carousel-container">
                    <h2 class="section-title">Tìm sản phẩm yêu thích tiếp theo</h2>
                    <div class="favourites-carousel" role="region" aria-label="Danh sách yêu thích" aria-live="polite">
                        <button class="carousel-btn prev" id="favouritesPrevBtn" aria-label="Sản phẩm trước" tabindex="0">
                            <i class="fas fa-chevron-left" aria-hidden="true"></i>
                        </button>
                        <div class="favourites-wrapper">
                            <div class="favourites-track" id="favouritesTrack" role="list">
                                <c:forEach var="fav" items="${products.favourites}">
                                    <div class="favourite-card" data-product-id="${product.id}" role="listitem" tabindex="0" aria-label="${product.name}, ${product.type}, ${product.price}">
                                        <img src="${env}/images/products/${product.images[0].url}" alt="${product.name}" loading="lazy" aria-describedby="favourites-${status.index + 1}-desc">
                                        <div class="card-info">
                                            <div class="card-title"><c:out value="${product.name}" /></div>
                                            <div class="card-subtitle"><c:out value="${product.type}'s Shoes" /></div>
                                            <p class="card-price" aria-label="Price: <fmt:formatNumber value='${product.price}' type='number' maxFractionDigits='0'/> Vietnamese Dong">
                                                <fmt:formatNumber value="${product.price}" type="number" maxFractionDigits="0"/>₫
                                            </p>
                                        </div>
                                </c:forEach>
                            </div>
                        </div>
                        <button class="carousel-btn next" id="favouritesNextBtn" aria-label="Sản phẩm tiếp theo" tabindex="0">
                            <i class="fas fa-chevron-right" aria-hidden="true"></i>
                        </button>
                    </div>
                </div>
            </section>
            <!-- Member Benefits Section -->
            <section class="benefits-section" aria-label="Quyền lợi thành viên">
                <div class="benefits-carousel-container">
                    <h2 class="section-title">Quyền lợi thành viên</h2>
                    <div class="benefits-carousel" role="region" aria-label="Quyền lợi thành viên" aria-live="polite">
                        <button class="carousel-btn prev" id="benefitsPrevBtn" aria-label="Quyền lợi trước" tabindex="0">
                            <i class="fas fa-chevron-left" aria-hidden="true"></i>
                        </button>
                        <div class="benefits-wrapper">
                            <div class="benefits-track" id="benefitsTrack" role="list">
                                <c:forEach var="benefit" items="${benefits}">
                                    <div class="benefit-card" role="listitem" tabindex="0" aria-label="${benefit.title}">
                                        <img src="${benefit.img}" alt="${benefit.title}" loading="lazy">
                                        <div class="card-title"><c:out value="${benefit.title}" /></div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <button class="carousel-btn next" id="benefitsNextBtn" aria-label="Quyền lợi tiếp theo" tabindex="0">
                            <i class="fas fa-chevron-right" aria-hidden="true"></i>
                        </button>
                    </div>
                    <hr class="benefits-divider">
                </div>
            </section>
            <!-- Nike Apps Section -->
            <section class="apps-section" aria-label="Ứng dụng Nike">
                <h2 class="section-title">Ứng dụng Nike</h2>
                <div class="apps-cards">
                    <c:forEach var="app" items="${apps}">
                        <div class="app-card" role="region" aria-label="${app.title}">
                            <div class="card-info">
                                <div class="card-title">
                                    <img src="${app.logo}" alt="${app.title} logo" class="app-logo"> <c:out value="${app.title}" />
                                </div>
                                <div class="card-desc"><c:out value="${app.desc}" /></div>
                                <a href="${app.downloadUrl}" class="download-btn" role="button" aria-label="Tải xuống ${app.title}">Tải xuống</a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
        </main>
        <script src="${env}/js/customer/pages/profile.js"></script>
        <!-- Footer -->
        <footer role="contentinfo" aria-label="Site footer">
            <jsp:include page="/WEB-INF/views/customer/layout/footer.jsp" />
        </footer>
    </body>
</html>
