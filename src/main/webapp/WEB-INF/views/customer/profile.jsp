<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile | Nike Member</title>

    <!-- SEO Meta Tags -->
    <meta name="description" content="Nike Member Profile - Manage your interests, view favorites, and explore member benefits">
    <meta name="keywords" content="Nike, profile, member, favorites, benefits, sports">

    <!-- Open Graph Meta Tags -->
    <meta property="og:title" content="Nike Member Profile">
    <meta property="og:description" content="Manage your Nike member profile and explore personalized content">
    <meta property="og:type" content="website">

    <!-- CSS includes -->
    <jsp:include page="/WEB-INF/views/customer/layout/css.jsp" />
    <jsp:include page="/WEB-INF/views/customer/imported/profile.jsp" />

    <!-- JavaScript includes -->
    <script src="${env}/js/carousel.js" defer></script>
</head>

<body>
        <!-- Header Navigation -->
        <header role="banner" aria-label="Site header">
            <jsp:include page="/WEB-INF/views/customer/layout/header.jsp" />
        </header>

        <!-- Main Content Area -->
        <main class="container" role="main" aria-label="Profile main content">
            <section class="profile-header" aria-label="Profile information">
                <div class="profile-avatar" aria-hidden="true">
                    <svg width="80" height="80" viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <circle cx="40" cy="32" r="18" fill="#ddd" />
                        <ellipse cx="40" cy="62" rx="22" ry="12" fill="#eee" />
                    </svg>
                </div>
                <div class="profile-info">
                    <div class="profile-name"><c:out value="${profile.name}" /></div>
                    <div class="profile-member">Nike Member Since <c:out value="${profile.memberSince}" /></div>
                </div>
            </section>
            <nav class="profile-nav" role="navigation" aria-label="Profile navigation">
                <a href="#" class="active" aria-current="page">Profile</a>
                <a href="#">Inbox</a>
                <a href="#">Orders</a>
                <a href="#">Favourites</a>
                <a href="#">Settings</a>
            </nav>
            <section class="interests-section">
                        <div class="interests-header">
                            <div class="interests-title">Interests</div>
                            <div class="interests-edit">Edit</div>
                        </div>
                            <ul class="interests-tabs tab">
                                <li class="interests-tab active"><a href="#tab-all">All</a></li>
                                <li class="interests-tab"><a href="#tab-sports">Sports</a></li>
                                <li class="interests-tab"><a href="#tab-products">Products</a></li>
                                <li class="interests-tab"><a href="#tab-teams">Teams</a></li>
                                <li class="interests-tab"><a href="#tab-athletes">Athletes</a></li>
                                <li class="interests-tab"><a href="#tab-cities">Cities</a></li>
                            </ul>
                        <div class="interests-desc">
                            Add your interests to shop a collection of products that are based on what you're into.
                        </div>
                        <div class="tab-content">
                            <ul class="interests-contents contents">
                                <li class="active" id="tab-all">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Add Interests</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-sports">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Add Sports</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-products">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Add Products</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-teams">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Add Teams</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-athletes">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Add Athletes</span>
                                        </div>
                                    </div>
                                </li>
                                <li id="tab-cities">
                                    <div class="interests-cards">
                                        <div class="interests-card">
                                            <span class="add-icon">&#43;</span>
                                            <span class="add-label">Add Cities</span>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </section>
            <!-- Favourites Section -->
            <section class="favourites-section" aria-label="Favourites carousel">
                <div class="favourites-carousel-container">
                    <h2 class="section-title">Find Your Next Favourite</h2>
                    <div class="favourites-carousel" role="region" aria-label="Favourites carousel" aria-live="polite">
                        <button class="carousel-btn prev" id="favouritesPrevBtn" aria-label="Previous favourites" tabindex="0">
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
                                </c:forEach>
                            </div>
                        </div>
                        <button class="carousel-btn next" id="favouritesNextBtn" aria-label="Next favourites" tabindex="0">
                            <i class="fas fa-chevron-right" aria-hidden="true"></i>
                        </button>
                    </div>
                </div>
            </section>
            <!-- Member Benefits Section -->
            <section class="benefits-section" aria-label="Member benefits carousel">
                <div class="benefits-carousel-container">
                    <h2 class="section-title">Member Benefits</h2>
                    <div class="benefits-carousel" role="region" aria-label="Member benefits carousel" aria-live="polite">
                        <button class="carousel-btn prev" id="benefitsPrevBtn" aria-label="Previous benefits" tabindex="0">
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
                        <button class="carousel-btn next" id="benefitsNextBtn" aria-label="Next benefits" tabindex="0">
                            <i class="fas fa-chevron-right" aria-hidden="true"></i>
                        </button>
                    </div>
                    <hr class="benefits-divider">
                </div>
            </section>
            <!-- Nike Apps Section -->
            <section class="apps-section" aria-label="Nike apps">
                <h2 class="section-title">Nike Apps</h2>
                <div class="apps-cards">
                    <c:forEach var="app" items="${apps}">
                        <div class="app-card" role="region" aria-label="${app.title}">
                            <div class="card-info">
                                <div class="card-title">
                                    <img src="${app.logo}" alt="${app.title} logo" class="app-logo"> <c:out value="${app.title}" />
                                </div>
                                <div class="card-desc"><c:out value="${app.desc}" /></div>
                                <a href="${app.downloadUrl}" class="download-btn" role="button" aria-label="Download ${app.title}">Download</a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
        </main>
        <script>
        function tabs(menuName, contentName) {
            $(menuName).find('li').each(function() {
                $(this).click(function() {
                    $(menuName).find('li').removeClass('active');
                    $(contentName).children().removeClass('active');
                    var clicked = $(this).find('a').attr('href');
                    $(this).addClass('active');
                    $(contentName).find(clicked).addClass('active');
                });
            });
        }
        $(document).ready(function() {
            tabs('.tab', '.contents');
        });
        </script>
        <!-- Footer -->
        <footer role="contentinfo" aria-label="Site footer">
            <jsp:include page="/WEB-INF/views/customer/layout/footer.jsp" />
        </footer>
    </body>
</html>
