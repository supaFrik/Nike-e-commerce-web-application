<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<c:set var="adminSidebarSubtitle" value="Product editing shell" />
<c:set var="adminSidebarFooterTitle" value="Edit with restraint" />
<c:set var="adminSidebarFooterCopy" value="Protect structure, pricing clarity, and visual consistency while adjusting the live assortment." />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa sản phẩm</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="product-edit" data-form-type="edit" data-product-id="${productId}">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Danh mục / Chỉnh sửa sản phẩm</div>
                    <h1 class="page-title" id="formTitle">Đang tải sản phẩm...</h1>
                    <p class="page-subtitle" id="formCopy"></p>
                </div>
                <div class="action-row">
                    <a class="btn btn-light" href="${env}/admin/product/list">Quay lại kho</a>
                    <button class="btn btn-light" data-save-mode="draft" type="button">Lưu bản nháp</button>
                    <button class="btn btn-dark" id="saveLabel" data-save-mode="publish" type="button">Cập nhật sản phẩm</button>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <section class="split">
                <article class="panel">
                    <div class="panel-header">
                        <div>
                            <h3>Thông tin sản phẩm hiện tại</h3>
                            <p>Form admin đang lấy trực tiếp dữ liệu giá, phân loại, hình ảnh và tồn kho biến thể từ hệ thống.</p>
                        </div>
                        <span class="badge neutral" id="editStatusBadge">Loading</span>
                    </div>
                    <div class="field-grid">
                        <div class="field-span-8">
                            <label for="productName">Tên sản phẩm</label>
                            <input class="input" id="productName" type="text" placeholder="Tên sản phẩm">
                        </div>
                        <div class="field-span-4">
                            <label for="productSku">SKU gốc</label>
                            <input class="input" id="productSku" type="text" placeholder="SKU gốc để sinh SKU biến thể">
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
                        <div class="field-span-4">
                            <label for="regularPrice">Giá gốc</label>
                            <input class="input" id="regularPrice" type="text" placeholder="1900000">
                        </div>
                        <div class="field-span-4">
                            <label for="salePrice">Giá bán</label>
                            <input class="input" id="salePrice" type="text" placeholder="1690000">
                        </div>
                    </div>
                </article>
                <aside class="panel">
                    <div class="media-card">
                        <img id="mainProductImage" src="${env}/images/admin/products/air-max-dn8-hero.avif" alt="Product preview">
                        <div class="copy">
                            <div class="eyebrow">Featured product</div>
                            <h4>Current hero selection</h4>
                            <p>The hero image reflects persisted image ordering and main-image selection for the active colorway.</p>
                        </div>
                    </div>
                </aside>
            </section>

            <section class="panel-grid">
                <article class="panel span-6">
                    <div class="panel-header">
                        <div>
                            <h3>Màu sắc</h3>
                            <p>Điều chỉnh màu sắc, kích cỡ và tồn kho thật ngay trên cùng một màn hình.</p>
                        </div>
                    </div>
                    <div class="toolbar">
                        <input class="input" id="colorInput" type="text" placeholder="Thêm màu sắc">
                        <button class="btn btn-light" id="addColorButton" type="button">Thêm màu</button>
                    </div>
                    <div class="swatch-row" id="colorList" style="margin-top:16px;"></div>

                    <div class="panel-header" style="margin-top:22px;">
                        <div>
                            <h4>Kích cỡ theo màu</h4>
                            <p>Quản lý rõ ràng kích cỡ theo từng màu sắc.</p>
                        </div>
                    </div>
                    <div class="toolbar">
                        <select class="select" id="sizeColor" style="flex:1 1 180px;"></select>
                        <input class="input" id="sizeInput" type="text" placeholder="42" style="flex:1 1 120px;">
                        <button class="btn btn-light" id="addSizeButton" type="button">Thêm size</button>
                    </div>
                    <div class="inventory-block" id="sizeMap" style="margin-top:16px;"></div>
                </article>
                <article class="panel span-6">
                    <div class="panel-header">
                        <div>
                            <h3>Hình ảnh và tồn kho</h3>
                            <p>Tải ảnh mới lên và xem tồn kho biến thể thật từ backend.</p>
                        </div>
                    </div>
                    <div class="field-grid" style="margin-bottom:16px;">
                        <div class="field-span-12">
                            <label for="uploadColor">Màu áp dụng ảnh</label>
                            <select class="select" id="uploadColor"></select>
                            <div class="mini-note">Ảnh cũ được đọc từ storage hiện tại. Ảnh mới sẽ được gộp vào payload khi lưu.</div>
                        </div>
                    </div>
                    <div class="upload-dropzone" id="uploadDropzone" tabindex="0" role="button" aria-controls="productImageInput">
                        <strong>Thay hoặc thêm hình ảnh</strong>
                        <span id="uploadDropzoneCopy">Hỗ trợ PNG, JPG, WEBP hoặc AVIF. Có thể chọn nhiều ảnh.</span>
                    </div>
                    <input id="productImageInput" type="file" accept="image/*" multiple class="hide">
                    <div class="gallery-grid" id="uploadPreview" style="margin-top:16px;"></div>
                    <div class="panel-header" style="margin-top:22px;">
                        <div>
                            <h4>Tồn kho biến thể</h4>
                            <p>Mỗi dòng bên dưới gắn với một biến thể thật và số lượng tồn kho đang lưu.</p>
                        </div>
                    </div>
                    <div class="inventory-block" id="variantStock"></div>
                </article>
            </section>
            <div class="footer-space"></div>
        </div>
    </main>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/product-form.js"></script>
</body>
</html>
