<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${not empty productDto.id}">Chỉnh sửa sản phẩm - <c:out value='${productDto.name}' default=''/></c:when><c:otherwise>Thêm sản phẩm mới - Nike</c:otherwise></c:choose></title>
    <link rel="icon" type="image/png" sizes="16x16" href="${env }/images/icons/nike-icon-webpage.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <%@ include file="/WEB-INF/views/administrator/layout/product-add-components.jsp" %>
</head>
<body>
    <div class="container">
        <!-- Action Buttons -->
        <div class="actions">
                <c:if test="${empty productDto.id}">
                    <button type="button" class="btn btn-secondary" id="saveDraftBtn">
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                        </svg>
                        Lưu nháp
                    </button>
                </c:if>
                <button id="submitProductBtn" form="productForm" type="submit" class="btn btn-primary">
                    <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12.5 4.5L5.5 11.5L2 8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <c:choose><c:when test="${not empty productDto.id}">Cập nhật sản phẩm</c:when><c:otherwise>Thêm sản phẩm</c:otherwise></c:choose>
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
                    <span>
                        <c:choose><c:when test="${not empty productDto.id}">Chỉnh sửa sản phẩm</c:when><c:otherwise>Thêm sản phẩm mới</c:otherwise></c:choose>
                    </span>
                </h1>
                <p class="section-subtitle">
                    <c:choose><c:when test="${not empty productDto.id}">Sửa thông tin sản phẩm</c:when><c:otherwise>Tạo sản phẩm mới</c:otherwise></c:choose>
                </p>

                <form id="productForm" action="<c:choose><c:when test='${not empty productDto.id}'>${env}/admin/product/edit-save</c:when><c:otherwise>${env}/admin/product/add-save</c:otherwise></c:choose>" method="post" enctype="multipart/form-data">
                    <c:if test="${not empty productDto.id}">
                        <input type="hidden" name="id" value="${productDto.id}" />
                    </c:if>
                    <!-- General Information -->
                    <div class="form-group">
                        <label class="form-label" for="productName">Tên sản phẩm</label>
                        <input id="productName" name="name" type="text" class="form-input" placeholder="Nhập tên..." autocomplete="off" value="<c:out value='${productDto.name}' default=''/>">
                        <div id="productNameConflict" style="display:none; margin-top:6px; font-size:12px; line-height:1.4; border:1px solid #c93; background:#fff8e6; color:#8a5b00; padding:6px 8px; border-radius:4px;"></div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="productDescription">Mô tả sản phẩm</label>
                        <textarea id="productDescription" name="description" class="form-textarea" placeholder="Nhập mô tả..."><c:out value='${productDto.description}' default=''/></textarea>
                    </div>

                    <!-- Size and Gender Selection -->
                    <div class="selection-group">
                        <div class="selection-item">
                            <h4>Kích thước</h4>
                            <div class="size-options" id="sizeOptions"></div>
                            <div id="stockSummary" style="margin-top:8px; font-size:12px; color:var(--color-text-secondary);"></div>
                            <div class="add-size-container">
                                <input type="text" class="add-size-input" id="newSizeInput" placeholder="31" maxlength="4" autocomplete="off">
                                <button type="button" id="addSizeBtn" class="add-size-btn">
                                    <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M6 2v8M2 6h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                                    </svg>
                                    Thêm kích thước
                                </button>
                            </div>
                        </div>

                        <div class="selection-item">
                            <h4>Màu sắc</h4>
                            <div class="color-options" id="colorOptions"></div>
                            <div class="add-color-container">
                                <input type="text" class="add-color-input" id="newColorInput" placeholder="Tên màu" maxlength="20" autocomplete="off">
                                <button type="button" id="addColorBtn" class="add-color-btn">
                                    <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M6 2v8M2 6h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                                    </svg>
                                    Thêm màu
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Gender Selection -->
                    <div class="selection-group">
                        <div class="selection-item">
                            <h4>Giới tính</h4>
                            <div class="gender-options">
                                <div class="radio-option">
                                    <input type="radio" name="gender" id="Men" value="MEN" <c:if test="${productDto.type == 'MEN'}">checked</c:if>>
                                    <label for="Men">Nam</label>
                                </div>
                                <div class="radio-option">
                                    <input type="radio" name="gender" id="Women" value="WOMEN" <c:if test="${productDto.type == 'WOMEN'}">checked</c:if>>
                                    <label for="Women">Nữ</label>
                                </div>
                                <div class="radio-option">
                                    <input type="radio" name="gender" id="Unisex" value="UNISEX" <c:if test="${empty productDto.type || productDto.type == 'UNISEX'}">checked</c:if>>
                                    <label for="Unisex">Unisex</label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Pricing and Stock -->
                    <h3 style="font-size: var(--font-size-m); font-weight: var(--font-weight-bold); margin-bottom: var(--size-spacing-xl); color: var(--color-text-primary);">Giá và tồn kho</h3>

                    <div class="pricing-grid">
                        <div class="form-group">
                            <label class="form-label" for="productPrice">Giá gốc</label>
                            <div class="price-input-wrapper">
                                <input id="productPrice" name="price" type="text" inputmode="numeric" class="form-input" placeholder="1.000.000 ₫" autocomplete="off" value="<c:out value='${productDto.price}'/>" />
                            </div>
                        </div>
                    </div>

                    <div class="discount-grid">
                        <div class="form-group">
                            <label class="form-label">Giảm giá</label>
                            <input type="text" class="form-input" placeholder="10%" autocomplete="off">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Mã</label>
                            <input type="text" class="form-input" placeholder="NIKEMEMBER" autocomplete="off">
                        </div>
                    </div>
                    <input type="hidden" id="categoryIdHidden" name="categoryId" value="<c:out value='${productDto.categoryId}'/>" />
                </form>
            </div>

            <!-- Image Section -->
            <div class="image-section">
                <h3 style="font-size: var(--font-size-m); font-weight: var(--font-weight-bold); margin-bottom: var(--size-spacing-xl); color: var(--color-brand-red);">Tải ảnh</h3>

                <div class="upload-area" id="uploadArea">
                    <div class="upload-placeholder" id="uploadPlaceholder">
                        <svg width="60" height="60" viewBox="0 0 60 60" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <rect width="60" height="60" rx="8" fill="var(--color-grey-100)"/>
                            <path d="M30 20v20M20 30h20" stroke="var(--color-red-500)" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                        <p class="upload-text">Nhấp để tải ảnh sản phẩm</p>
                        <p style="font-size: var(--font-size-2xs); color: var(--color-text-secondary); margin-top: var(--size-spacing-xs);">Tối đa 10 ảnh</p>
                    </div>
                    <img src="" alt="Product" class="main-image" id="mainImage" style="display: none;">
                    <input type="file" id="imageUpload" style="display: none;" accept="image/*" multiple>
                </div>

                <div class="image-thumbnails" id="imageThumbnails"></div>

                <div class="image-counter" id="imageCounter">0 / 10 ảnh đã tải lên</div>

                <div class="add-more-images" id="addMoreImages" style="display: none;">
                    <button type="button" class="add-images-btn" id="addMoreImagesBtn">
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                        Thêm ảnh
                    </button>
                </div>

                <!-- Category Section -->
                <div class="category-section">
                    <h4 style="font-size: var(--font-size-s); font-weight: var(--font-weight-bold); margin-bottom: var(--size-spacing-m); color: var(--color-text-primary);">Danh mục</h4>
                    <div class="form-group">
                        <label class="form-label">Danh mục sản phẩm</label>
                        <div class="custom-dropdown" id="categoryDropdown">
                            <div class="dropdown-selected" id="dropdownToggle" role="button" tabindex="0" aria-haspopup="listbox" aria-expanded="false">
                                <span id="selected-category"><c:choose><c:when test="${not empty productDto.categoryId}"><c:forEach var="c" items="${categories}"><c:if test="${c.id == productDto.categoryId}"><c:out value='${c.name}' default=''/></c:if></c:forEach></c:when><c:otherwise>Chọn danh mục</c:otherwise></c:choose></span>
                                <i class="fas fa-chevron-down dropdown-arrow" aria-hidden="true"></i>
                            </div>
                            <div class="dropdown-options" id="dropdown-options" role="listbox">
                                <c:forEach var="cat" items="${categories}">
                                    <div class="dropdown-option" role="option" data-id="${cat.id}" data-name="<c:out value='${cat.name}' default=''/>"><c:out value='${cat.name}' default=''/></div>
                                </c:forEach>
                                <c:if test="${empty categories}">
                                    <div class="dropdown-option" style="opacity:.6;cursor:default;">Không có danh mục</div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <a href="${env}/admin/category/add" class="add-category-btn" style="text-decoration:none; display:inline-block; text-align:center;">Thêm danh mục</a>
                </div>
            </div>
        </div>
    </div>

    <c:if test="${not empty productDto.id}">
        <!-- Embed existing product structured data for JS initialization -->
        <script>
        (function(){
            var ctx = (window.APP_CTX || '${env}').replace(/\/$/, '');
            window.EDIT_PRODUCT = {
                id: ${productDto.id},
                name: "<c:out value='${productDto.name}' default=''/>",
                description: "<c:out value='${productDto.description}' default=''/>",
                price: ${productDto.price != null ? productDto.price : 0},
                type: "${productDto.type != null ? productDto.type : 'UNISEX'}",
                categoryId: ${productDto.categoryId != null ? productDto.categoryId : 'null'},
                colors: []
            };
        })();
        </script>
        <c:forEach var="pc" items="${product.colors}">
            <script>
            (function(){
                if(!window.EDIT_PRODUCT){ return; }
                var color = { name: "<c:out value='${pc.colorName}' default=''/>", sizes: [], sizeStocks: [], images: [], defaultImageIndex: 0 };
                var imgIdx = 0; var defaultIdx = 0;
                // Collect variants for this color
                <c:forEach var="v" items="${product.variants}">
                    <c:if test="${v.color != null && pc.id == v.color.id}">
                        color.sizes.push("<c:out value='${v.sizeLabel}' default=''/>");
                        color.sizeStocks.push(${v.stock != null ? v.stock : 0});
                    </c:if>
                </c:forEach>
                // Collect images for this color
                <c:forEach var="img" items="${product.images}">
                    <c:if test="${img.color != null && pc.id == img.color.id}">
                        color.images.push("<c:out value='${img.path}' default='' />");
                        <c:if test="${product.mainImage != null && product.mainImage.id == img.id}">defaultIdx = imgIdx;</c:if>
                        imgIdx++;
                    </c:if>
                </c:forEach>
                color.defaultImageIndex = defaultIdx;
                window.EDIT_PRODUCT.colors.push(color);
            })();
            </script>
        </c:forEach>
    </c:if>

<!-- Removed inline script: logic migrated to external dropdown.js & images.js -->
</body>
</html>
