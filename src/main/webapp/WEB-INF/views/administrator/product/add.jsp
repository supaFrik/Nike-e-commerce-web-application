<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="adminSidebarSubtitle" value="Product creation shell" />
<c:set var="adminSidebarFooterTitle" value="Creation rules" />
<c:set var="adminSidebarFooterCopy" value="Keep naming crisp, colorways intentional, and inventory buckets obvious before launch approval." />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm sản phẩm</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="product-add" data-form-type="add">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Danh mục / Thêm sản phẩm</div>
                    <h1 class="page-title" id="formTitle">Thêm sản phẩm</h1>
                    <p class="page-subtitle" id="formCopy"></p>
                </div>
                <div class="action-row add-product-header-actions">
                    <a class="btn btn-light" href="${env}/admin/product/list">Hủy</a>
                    <button class="btn btn-light" data-save-mode="draft" type="button">Lưu bản nháp</button>
                    <button class="btn btn-dark" id="saveLabel" data-save-mode="publish" type="button">Lưu sản phẩm</button>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <nav class="mobile-jump-nav" aria-label="Add product sections">
                <a class="chip is-active" href="#detailsSection">Thông tin</a>
                <a class="chip" href="#variantsSection">Biến thể</a>
                <a class="chip" href="#stockSection">Tồn kho</a>
            </nav>

            <section class="panel" id="detailsSection">
                <div class="panel-header">
                    <div>
                        <h3>Thông tin cơ bản</h3>
                        <p>Nhập dữ liệu thật cho sản phẩm trước khi khai báo màu, size và tồn kho.</p>
                    </div>
                    <span class="pill pill-red" id="readinessPill">0 / 4 sẵn sàng</span>
                </div>
                <div class="mini-note" style="margin-bottom:16px;">Đi theo thứ tự này: tên và mô tả, giá, danh mục, màu, size, rồi mới nhập tồn kho theo từng biến thể.</div>
                <div class="field-grid">
                    <div class="field-span-8">
                        <label for="productName">Tên sản phẩm</label>
                        <input class="input" id="productName" type="text" placeholder="Nike Air Max Dn8">
                    </div>
                    <div class="field-span-4">
                        <label for="productSku">SKU gốc</label>
                        <input class="input" id="productSku" type="text" placeholder="DN8-001">
                    </div>
                    <div class="field-span-12">
                        <label for="productDescription">Mô tả</label>
                        <textarea class="textarea" id="productDescription" placeholder="Mô tả ngắn gọn, rõ ràng cho sản phẩm."></textarea>
                    </div>
                    <div class="field-span-4">
                        <label for="productType">Loại</label>
                        <select class="select" id="productType">
                            <option value="UNISEX">UNISEX</option>
                            <option value="MEN">MEN</option>
                            <option value="WOMEN">WOMEN</option>
                        </select>
                    </div>
                    <div class="field-span-4">
                        <label for="productCategory">Danh mục</label>
                        <select class="select" id="productCategory"></select>
                    </div>
                    <div class="field-span-4">
                        <label for="productStatus">Trạng thái</label>
                        <select class="select" id="productStatus">
                            <option value="ACTIVE">Đang bán</option>
                            <option value="DRAFT">Nháp</option>
                            <option value="FEW_LEFT">Sắp hết hàng</option>
                            <option value="OUT_OF_STOCK">Hết hàng</option>
                            <option value="DISCONTINUED">Ngừng kinh doanh</option>
                        </select>
                    </div>
                    <div class="field-span-6">
                        <label for="regularPrice">Giá gốc</label>
                        <input class="input" id="regularPrice" type="text" placeholder="1900000">
                    </div>
                    <div class="field-span-6">
                        <label for="salePrice">Giá bán</label>
                        <input class="input" id="salePrice" type="text" placeholder="1690000">
                    </div>
                </div>
            </section>

            <section class="panel-grid add-product-panels">
                <article class="panel span-5" id="variantsSection">
                    <div class="panel-header">
                        <div>
                            <h3>Màu sắc và size</h3>
                            <p>Thêm màu trước, sau đó gắn size để tạo ra biến thể tồn kho thật.</p>
                        </div>
                    </div>
                    <div class="toolbar mobile-stack-toolbar">
                        <input class="input" id="colorInput" type="text" placeholder="Thêm màu sắc">
                        <button class="btn btn-light" id="addColorButton" type="button">Thêm màu</button>
                    </div>
                    <div class="swatch-row" id="colorList" style="margin-top:16px;"></div>

                    <div class="panel-header" style="margin-top:22px;">
                        <div>
                            <h4>Kích cỡ theo màu</h4>
                            <p>Gán size cho màu đang chọn trước khi nhập tồn kho.</p>
                        </div>
                    </div>
                    <div class="toolbar mobile-stack-toolbar">
                        <select class="select" id="sizeColor" style="flex:1 1 180px;"></select>
                        <input class="input" id="sizeInput" type="text" placeholder="42" style="flex:1 1 120px;">
                        <button class="btn btn-light" id="addSizeButton" type="button">Thêm size</button>
                    </div>
                    <div class="inventory-block" id="sizeMap" style="margin-top:16px;"></div>
                </article>

                <article class="panel span-7" id="stockSection">
                    <div class="panel-header">
                        <div>
                            <h3>Hình ảnh và tồn kho</h3>
                            <p>Upload ảnh theo từng màu và nhập tồn kho thật cho từng cặp màu/size.</p>
                        </div>
                    </div>
                    <div class="field-grid" style="margin-bottom:16px;">
                        <div class="field-span-12">
                            <label for="uploadColor">Màu áp dụng ảnh</label>
                            <select class="select" id="uploadColor"></select>
                            <div class="mini-note">Ảnh mới chỉ được lưu thật khi sản phẩm được lưu thành công.</div>
                        </div>
                    </div>
                    <div class="upload-dropzone" id="uploadDropzone" tabindex="0" role="button" aria-controls="productImageInput">
                        <strong>Kéo thả hoặc bấm để tải ảnh lên</strong>
                        <span id="uploadDropzoneCopy">Hỗ trợ PNG, JPG, WEBP hoặc AVIF. Có thể chọn nhiều ảnh.</span>
                    </div>
                    <input id="productImageInput" type="file" accept="image/*" multiple class="hide">
                    <div class="gallery-grid" id="uploadPreview" style="margin-top:16px;"></div>

                    <div class="panel-header" style="margin-top:22px;">
                        <div>
                            <h4>Tồn kho biến thể</h4>
                            <p>Nhập tồn kho thật cho từng biến thể. Mỗi dòng là một màu và một size.</p>
                        </div>
                    </div>
                    <div class="inventory-block" id="variantStock"></div>
                </article>
            </section>
            <div class="footer-space"></div>
        </div>
    </main>
</div>
<div class="mobile-action-bar">
    <div class="mobile-action-copy">
        <span>Trạng thái form</span>
        <strong id="mobileReadinessText">0 / 4 sẵn sàng</strong>
    </div>
    <div class="mobile-action-buttons mobile-action-buttons-triple">
        <a class="btn btn-light" href="${env}/admin/product/list">Hủy</a>
        <button class="btn btn-light" data-save-mode="draft" type="button">Lưu bản nháp</button>
        <button class="btn btn-dark" data-save-mode="publish" type="button">Lưu sản phẩm</button>
    </div>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/product-form.js"></script>
</body>
</html>
