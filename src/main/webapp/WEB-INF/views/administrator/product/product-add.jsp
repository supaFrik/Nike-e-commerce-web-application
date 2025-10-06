<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Add Product</title>
    <jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp"/>
</head>
<body>
<!-- ============================================================== -->
	<!-- Main wrapper - style you can find in pages.scss -->
	<!-- ============================================================== -->
		<!-- Topbar header - style you can find in pages.scss -->
		<jsp:include page="/WEB-INF/views/administrator/layout/header.jsp"></jsp:include>
		<!-- End Topbar header -->

		<!-- Left Sidebar - style you can find in sidebar.scss  -->
		<jsp:include page="/WEB-INF/views/administrator/layout/left-slide-bar.jsp"></jsp:include>
		<!-- End Left Sidebar - style you can find in sidebar.scss  -->

<div class="container">
    <h2>Add New Product</h2>

    <sf:form modelAttribute="productDto" action="${env}/admin/product/add-save" method="post" enctype="multipart/form-data" id="productForm">
        <div class="form-row">
            <div class="form-group col-md-6">
                <label>Category</label>
                <sf:select path="categoryId" cssClass="form-control">
                    <sf:option value="" disabled="true" selected="true">-- Select a Category --</sf:option>
                    <sf:options items="${categories}" itemValue="id" itemLabel="name"/>
                </sf:select>
            </div>

            <div class="form-group col-md-6">
                <label>Product Name</label>
                <sf:input path="name" cssClass="form-control"/>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group col-md-4">
                <label>Price</label>
                <sf:input path="price" type="number" step="0.01" cssClass="form-control"/>
            </div>
            <div class="form-group col-md-4">
                <label>Type</label>
                <sf:input path="type" cssClass="form-control"/>
            </div>
            <div class="form-group col-md-4">
                <label>SEO</label>
                <sf:input path="seo" cssClass="form-control"/>
            </div>
        </div>

        <!-- COLORS container -->
        <div class="card mb-3">
            <div class="card-header d-flex justify-content-between align-items-center">
                <strong>Color Variants</strong>
                <button type="button" class="btn btn-sm btn-primary" onclick="addColorRow()">Add color</button>
            </div>
            <div class="card-body" id="colorContainer">
                <!-- JS will append .color-row blocks -->
            </div>
        </div>

        <!-- Product-level gallery (optional) -->
        <div class="form-group">
            <label>Product gallery (additional images)</label>
            <input type="file" name="imageFiles" id="imageFiles" multiple accept="image/*" class="form-control-file"/>
        </div>

        <!-- Form actions -->
        <div class="form-group">
            <a href="${env}/admin/product/list" class="btn btn-secondary">Back</a>
            <button type="submit" class="btn btn-success">Save product</button>
        </div>
    </sf:form>
</div>

<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"/>

<script>
    // trạng thái global
    let colorIndex = 0;
    let globalVariantIndex = 0;

    // thêm 1 hàng color (prefill không bắt buộc)
    function addColorRow(prefill) {
        const container = document.getElementById('colorContainer');

        const row = document.createElement('div');
        row.className = 'color-row border p-3 mb-3';
        row.dataset.colorIndex = colorIndex;

        row.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-2">
                <strong>Color #<span class="color-number">${colorIndex+1}</span></strong>
                <div>
                    <button type="button" class="btn btn-sm btn-danger" onclick="removeColorRow(this)">Remove color</button>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Color name</label>
                    <input type="text" data-name="colorName" class="form-control" />
                </div>
                <div class="form-group col-md-6">
                    <label>Base image (representative)</label>
                    <input type="file" data-name="images" accept="image/*" multiple class="form-control-file" />
                    <input type="hidden" data-name="existingPreviewFilename" />
                    <div class="form-check mt-2">
                        <input type="checkbox" data-name="defaultPreview" class="form-check-input" />
                        <label class="form-check-label">Use this color image as default preview</label>
                    </div>
                </div>
            </div>

            <div class="variants-block">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <strong>Size Variants</strong>
                    <button type="button" class="btn btn-sm btn-primary" onclick="addVariantRow(this)">Add size</button>
                </div>
                <div class="variants-container"></div>
            </div>
        `;

        container.appendChild(row);

        // thêm một variant mặc định
        addVariantRow(row);
        reindexAll();
        colorIndex++;
    }

    function removeColorRow(btn) {
        const row = btn.closest('.color-row');
        if (row) {
            row.remove();
            reindexAll();
        }
    }

    // thêm một variant hàng vào colorRow (hoặc button trong colorRow)
    function addVariantRow(colorRowOrBtn) {
        let colorRow;
        if (colorRowOrBtn instanceof HTMLElement && colorRowOrBtn.classList.contains('color-row')) {
            colorRow = colorRowOrBtn;
        } else {
            colorRow = colorRowOrBtn.closest('.color-row');
        }
        if (!colorRow) return;

        const variantsContainer = colorRow.querySelector('.variants-container');
        const vRow = document.createElement('div');
        vRow.className = 'variant-row d-flex align-items-center mb-2';

        vRow.innerHTML = `
            <div class="col-3 pr-1"><input type="text" data-name="size" class="form-control form-control-sm" placeholder="Size"></div>
            <div class="col-3 pr-1"><input type="number" data-name="price" class="form-control form-control-sm" placeholder="Price" step="0.01"></div>
            <div class="col-3 pr-1"><input type="number" data-name="stock" class="form-control form-control-sm" placeholder="Stock" min="0"></div>
            <div class="col-2 pr-1"><input type="hidden" data-name="colorIndex" value=""><button type="button" class="btn btn-sm btn-danger" onclick="removeVariantRow(this)">Remove</button></div>
        `;
        variantsContainer.appendChild(vRow);
        reindexAll();
    }

    function removeVariantRow(btn) {
        const vRow = btn.closest('.variant-row');
        if (vRow) {
            vRow.remove();
            reindexAll();
        }
    }

    /**
     * Reindex toàn bộ form trước khi submit
     * - Đặt name cho từng input theo dạng Spring mong muốn:
     *   colors[0].colorName, colors[0].images (file[]), variants[0].size, variants[0].colorIndex, ...
     */
    function reindexAll() {
        const colorRows = document.querySelectorAll('.color-row');
        colorRows.forEach((row, i) => {
            row.dataset.colorIndex = i;
            const numberSpan = row.querySelector('.color-number');
            if (numberSpan) numberSpan.textContent = (i + 1);

            // color fields
            const colorNameInput = row.querySelector('[data-name="colorName"]');
            if (colorNameInput) colorNameInput.name = `colors[${i}].colorName`;

            const fileInput = row.querySelector('[data-name="images"]');
            if (fileInput) {
                // name cho MultipartFile[] (multiple) -> Spring bind được thành MultipartFile[]
                fileInput.name = `colors[${i}].images`;
            }

            const existingPreviewHidden = row.querySelector('[data-name="existingPreviewFilename"]');
            if (existingPreviewHidden) existingPreviewHidden.name = `colors[${i}].existingPreviewFilename`;

            const defaultPreviewCheckbox = row.querySelector('[data-name="defaultPreview"]');
            if (defaultPreviewCheckbox) {
                // checkbox nếu checked sẽ gửi giá trị "true", nếu unchecked sẽ không gửi
                defaultPreviewCheckbox.name = `colors[${i}].defaultPreview`;
                defaultPreviewCheckbox.value = "true";
            }
        });

        // variants indexing global (flat list)
        let globalVarIndex = 0;
        colorRows.forEach((row, colorIdx) => {
            const variantRows = row.querySelectorAll('.variant-row');
            variantRows.forEach((vRow) => {
                const size = vRow.querySelector('[data-name="size"]');
                const price = vRow.querySelector('[data-name="price"]');
                const stock = vRow.querySelector('[data-name="stock"]');
                const colorIndexHidden = vRow.querySelector('[data-name="colorIndex"]');

                if (size) size.name = `variants[${globalVarIndex}].size`;
                if (price) price.name = `variants[${globalVarIndex}].price`;
                if (stock) stock.name = `variants[${globalVarIndex}].stock`;
                if (colorIndexHidden) {
                    colorIndexHidden.name = `variants[${globalVarIndex}].colorIndex`;
                    colorIndexHidden.value = colorIdx;
                }
                globalVarIndex++;
            });
        });
    }

    /**
     * Loại bỏ các input có 'index rỗng' trước khi submit
     * (ví dụ tên kiểu 'colors[]' hoặc 'variants[].size' với [] rỗng)
     * Cách làm: nếu detect một cặp [] rỗng trong tên -> remove attribute name để browser không gửi param đó.
     */
    function sanitizeBeforeSubmit() {
        document.querySelectorAll('[name]').forEach(function(el){
            const nm = el.getAttribute('name');
            if(!nm) return;
            // tìm mọi cặp [ ... ] rồi kiểm tra nếu có cặp rỗng -> xóa name
            const allBrackets = Array.from(nm.matchAll(/\[([^\]]*)\]/g));
            for (const m of allBrackets) {
                if (m[1] === '') { // index rỗng
                    el.removeAttribute('name');
                    break;
                }
            }
        });
    }

    // Listener submit: reindex + sanitize trước khi browser gửi request
    document.addEventListener('DOMContentLoaded', function() {
        // đảm bảo có 1 color row mặc định
        if (document.querySelectorAll('.color-row').length === 0) {
            addColorRow();
        }

        const form = document.getElementById('productForm');
        if (form) {
            form.addEventListener('submit', function(e) {
                // cập nhật name fields
                reindexAll();

                // remove các tên có index rỗng (ngăn lỗi bind)
                sanitizeBeforeSubmit();

                // (tuỳ chọn) bạn có thể validate ở đây, ví dụ require ít nhất 1 color name
                // const hasColorName = Array.from(document.querySelectorAll('[name^="colors"]')).some(i => i.name.endsWith('.colorName') && i.value.trim());
                // if (!hasColorName) { e.preventDefault(); alert('Bạn phải nhập ít nhất 1 màu'); return false; }

                // để form submit tiếp tục
            });
        }
    });
</script>


</body>
</html>
