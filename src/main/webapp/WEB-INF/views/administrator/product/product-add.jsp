<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Product - Nike</title>
    <link rel="icon" type="image/png" sizes="16x16" href="${env }/administrator/assets/images/favicon.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <%@ include file="/WEB-INF/views/administrator/layout/product-add-components.jsp" %>
    <!-- Expose context path to JS -->

</head>
<body>
    <div class="container">
        <!-- Action Buttons -->
        <div class="actions">
            <button type="button" class="btn btn-secondary" id="saveDraftBtn">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                </svg>
                Save Draft
            </button>
            <button id="submitProductBtn" form="productForm" type="submit" class="btn btn-primary">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M12.5 4.5L5.5 11.5L2 8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                Add Product
            </button>
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <!-- Form Section -->
            <div class="form-section">
                <h1 class="section-title" style="display:flex; align-items:center; gap:8px;">
                    <span class="section-title-icon" aria-hidden="true" style="display:inline-flex; color:var(--color-brand-red);">
                        <svg width="28" height="28" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M12 22 3 17V7l9 5 9-5v10l-9 5Z"/>
                            <path d="M12 12 3 7l9-5 9 5-9 5Z"/>
                        </svg>
                    </span>
                    <span>Add New Product</span>
                </h1>
                <p class="section-subtitle">Create your wish product</p>

                <form id="productForm" action="${env}/admin/product/add-save" method="post" enctype="multipart/form-data">
                    <!-- General Information -->
                    <div class="form-group">
                        <label class="form-label">Name Product</label>
                        <input id="productName" name="name" type="text" class="form-input" placeholder="Write something...">
                        <div id="productNameConflict" style="display:none; margin-top:6px; font-size:12px; line-height:1.4; border:1px solid #c93; background:#fff8e6; color:#8a5b00; padding:6px 8px; border-radius:4px;">
                            <!-- message báo lỗi -->
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Description Product</label>
                        <textarea id="productDescription" name="description" class="form-textarea" placeholder="Write something..."></textarea>
                    </div>

                    <!-- Size and Gender Selection -->
                    <div class="selection-group">
                        <div class="selection-item">
                            <h4>Size</h4>
                            <div class="size-options" id="sizeOptions">

                            </div>
                            <div id="stockSummary" style="margin-top:8px; font-size:12px; color:var(--color-text-secondary);"></div>
                            <div class="add-size-container">
                                <input type="text" class="add-size-input" id="newSizeInput" placeholder="31" maxlength="4">
                                <button type="button" id="addSizeBtn" class="add-size-btn">
                                    <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M6 2v8M2 6h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                                    </svg>
                                    Add size
                                </button>
                            </div>
                        </div>

                        <div class="selection-item">
                            <h4>Color</h4>
                            <div class="color-options" id="colorOptions">
                                <!-- Colors will be populated dynamically -->
                            </div>
                            <div class="add-color-container">
                                <input type="text" class="add-color-input" id="newColorInput" placeholder="Your color" maxlength="20">
                                <button type="button" id="addColorBtn" class="add-color-btn">
                                    <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M6 2v8M2 6h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                                    </svg>
                                    Add Color
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Gender Selection -->
                    <div class="selection-group">
                        <div class="selection-item">
                            <h4>Gender</h4>
                            <div class="gender-options">
                                <div class="radio-option">
                                    <input type="radio" name="gender" id="Men" checked>
                                    <label for="Men">Men</label>
                                </div>
                                <div class="radio-option">
                                    <input type="radio" name="gender" id="Women">
                                    <label for="Women">Women</label>
                                </div>
                                <div class="radio-option">
                                    <input type="radio" name="gender" id="Unisex">
                                    <label for="Unisex">Unisex</label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Pricing and Stock -->
                    <h3 style="font-size: var(--font-size-m); font-weight: var(--font-weight-bold); margin-bottom: var(--size-spacing-xl); color: var(--color-text-primary);">Pricing And Stock</h3>

                    <div class="pricing-grid">
                        <div class="form-group">
                            <label class="form-label">Base Pricing</label>
                            <div class="price-input-wrapper">
                                <input id="productPrice" name="price" type="text" inputmode="numeric" class="form-input" placeholder="1.000.000 ₫" autocomplete="off" />
                            </div>
                        </div>
                    </div>

                    <div class="discount-grid">
                        <div class="form-group">
                            <label class="form-label">Discount</label>
                            <input type="text" class="form-input" placeholder="10%">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Code</label>
                            <input type="text" class="form-input" placeholder="NIKEMEMBER">
                        </div>
                    </div>
                    <input type="hidden" id="categoryIdHidden" name="categoryId" />
                </form>
            </div>

            <!-- Image Section -->
            <div class="image-section">
                <h3 style="font-size: var(--font-size-m); font-weight: var(--font-weight-bold); margin-bottom: var(--size-spacing-xl); color: var(--color-brand-red);">Upload Img</h3>

                <div class="upload-area" onclick="document.getElementById('imageUpload').click()" id="uploadArea">
                    <div class="upload-placeholder" id="uploadPlaceholder">
                        <svg width="60" height="60" viewBox="0 0 60 60" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <rect width="60" height="60" rx="8" fill="var(--color-grey-100)"/>
                            <path d="M30 20v20M20 30h20" stroke="var(--color-red-500)" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                        <p class="upload-text">Click to upload product images</p>
                        <p style="font-size: var(--font-size-2xs); color: var(--color-text-secondary); margin-top: var(--size-spacing-xs);">Maximum 10 images</p>
                    </div>
                    <img src="" alt="Product" class="main-image" id="mainImage" style="display: none;">
                    <input type="file" id="imageUpload" style="display: none;" accept="image/*" multiple>
                </div>

                <div class="image-thumbnails" id="imageThumbnails">
                    <!-- Thumbnails will be generated dynamically -->
                </div>

                <div class="image-counter" id="imageCounter">
                    0 / 10 images uploaded
                </div>

                <div class="add-more-images" id="addMoreImages" style="display: none;">
                    <button type="button" class="add-images-btn" onclick="document.getElementById('imageUpload').click()">
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                        Add More Images
                    </button>
                </div>

                <!-- Category Section -->
                <div class="category-section">
                    <h4 style="font-size: var(--font-size-s); font-weight: var(--font-weight-bold); margin-bottom: var(--size-spacing-m); color: var(--color-text-primary);">Category</h4>
                    <div class="form-group">
                        <label class="form-label">Product Category</label>
                        <div class="custom-dropdown">
                            <div class="dropdown-selected" onclick="toggleDropdown()">
                                <span id="selected-category">Select Category</span>
                                <i class="fas fa-chevron-down dropdown-arrow"></i>
                            </div>
                            <div class="dropdown-options" id="dropdown-options">
                                <!-- Dynamically populated from model -->
                                <c:forEach var="cat" items="${categories}">
                                    <div class="dropdown-option" data-id="${cat.id}" onclick="selectCategory('${cat.name.replace("'","\\'")}')">${cat.name}</div>
                                </c:forEach>
                                <c:if test="${empty categories}">
                                    <div class="dropdown-option" style="opacity:.6;cursor:default;">No categories available</div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <a href="${env}/admin/category/add" class="add-category-btn" style="text-decoration:none; display:inline-block; text-align:center;">Add Category</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
