<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <title>Thêm sản phẩm</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp"/>
    <style>
        .color-row { border: 1px dashed #ddd; padding: 12px; margin-bottom: 12px; border-radius:6px; }
        .variant-row { margin-bottom:8px; }
        .small-btn { padding: 4px 8px; font-size: 12px; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/administrator/layout/header.jsp"/>

<div class="container mt-4">
    <h3>Thêm sản phẩm mới</h3>

    <sf:form modelAttribute="productDto" method="post" action="${env}/admin/product/add-save"
             id="productForm" enctype="multipart/form-data" cssClass="mt-3">

        <!-- cơ bản -->
        <div class="form-row mb-3">
            <div class="form-group col-md-6">
                <label>Danh mục</label>
                <sf:select path="categoryId" cssClass="form-control">
                    <sf:option value="" disabled="true" selected="true">-- Chọn danh mục --</sf:option>
                    <sf:options items="${categories}" itemValue="id" itemLabel="name"/>
                </sf:select>
            </div>
            <div class="form-group col-md-6">
                <label>Tên sản phẩm</label>
                <sf:input path="name" cssClass="form-control"/>
            </div>
        </div>

        <div class="form-row mb-3">
            <div class="form-group col-md-3">
                <label>Giá</label>
                <sf:input path="price" type="number" step="0.01" cssClass="form-control"/>
            </div>
            <div class="form-group col-md-3">
                <label>Loại</label>
                <sf:input path="type" cssClass="form-control"/>
            </div>
            <div class="form-group col-md-6">
                <label>SEO</label>
                <sf:input path="seo" cssClass="form-control"/>
            </div>
        </div>

        <!-- container màu -->
        <div class="card mb-3">
            <div class="card-header d-flex justify-content-between align-items-center">
                <strong>Color Variants (Mỗi màu có ảnh đại diện riêng)</strong>
                <div>
                    <button type="button" class="btn btn-primary btn-sm small-btn" onclick="addColorRow()">Thêm màu</button>
                </div>
            </div>
            <div class="card-body" id="colorContainer">
                <!-- JS sẽ thêm các color-row ở đây -->
            </div>
        </div>

        <!-- gallery product-level (tùy chọn) -->
        <div class="form-group mb-3">
            <label>Ảnh gallery (cấp product)</label>
            <input type="file" name="imageFiles" id="imageFiles" multiple accept="image/*" class="form-control-file"/>
            <small class="form-text text-muted">Ảnh gallery chung cho product (không thuộc màu cụ thể)</small>
        </div>

        <div class="form-group mb-4">
            <a href="${env}/admin/product/list" class="btn btn-secondary">Quay lại</a>
            <button type="submit" class="btn btn-success">Lưu sản phẩm</button>
        </div>
    </sf:form>
</div>

<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"/>

<script>
    // State
    let colorIndex = 0;
    let globalVariantIndex = 0;

    // Tạo 1 color row mặc định
    function addColorRow(prefill) {
        // prefill optional: {colorName: '', existingPreview: '', variants: [...]}
        const container = document.getElementById('colorContainer');
        const row = document.createElement('div');
        row.className = 'color-row';
        row.dataset.colorIndex = colorIndex;

        row.innerHTML = `
            <div class="d-flex justify-content-between align-items-center mb-2">
                <strong>Màu #<span class="color-number">${colorIndex+1}</span></strong>
                <div>
                    <button type="button" class="btn btn-danger btn-sm small-btn" onclick="removeColorRow(this)">Xóa</button>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group col-md-5">
                    <label>Tên màu</label>
                    <input type="text" data-name="colorName" class="form-control" placeholder="Ví dụ: Black" />
                </div>

                <div class="form-group col-md-7">
                    <label>Ảnh đại diện cho màu (base image)</label>
                    <!-- name sẽ được set dynamic bởi reindexAll() -->
                    <input type="file" accept="image/*" data-name="images" class="form-control-file" />
                    <small class="form-text text-muted">Chọn 1 ảnh (hoặc nhiều) để lưu làm ảnh cho màu này. Nếu muốn 1 ảnh làm ảnh đại diện tick Default</small>

                    <div class="form-check mt-2">
                        <input type="checkbox" data-name="defaultPreview" class="form-check-input" />
                        <label class="form-check-label">Dùng ảnh đầu tiên làm preview cho danh sách/hero</label>
                    </div>

                    <!-- hidden: dùng khi edit: giữ tên file preview đã có -->
                    <input type="hidden" data-name="existingPreviewFilename" />
                </div>
            </div>

            <div class="variants-block mt-2">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <strong>Size variants</strong>
                    <button type="button" class="btn btn-sm btn-primary small-btn" onclick="addVariantRow(this)">Thêm size</button>
                </div>
                <div class="variants-container"></div>
            </div>
        `;
        container.appendChild(row);

        // thêm 1 variant mặc định
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

    function addVariantRow(colorRowOrBtn) {
        // colorRowOrBtn có thể là element colorRow hoặc button
        let colorRow;
        if (colorRowOrBtn instanceof HTMLElement && colorRowOrBtn.classList.contains('color-row')) {
            colorRow = colorRowOrBtn;
        } else {
            colorRow = colorRowOrBtn.closest('.color-row');
        }
        if (!colorRow) return;

        const container = colorRow.querySelector('.variants-container');
        const vRow = document.createElement('div');
        vRow.className = 'variant-row d-flex align-items-center';

        vRow.innerHTML = `
            <div class="col-3 pr-1"><input type="text" data-name="size" class="form-control form-control-sm" placeholder="Size"></div>
            <div class="col-3 pr-1"><input type="number" data-name="price" class="form-control form-control-sm" placeholder="Price" step="0.01"></div>
            <div class="col-3 pr-1"><input type="number" data-name="stock" class="form-control form-control-sm" placeholder="Stock" min="0"></div>
            <div class="col-3 pr-1"><input type="hidden" data-name="colorIndex" value=""><button type="button" class="btn btn-sm btn-danger" onclick="removeVariantRow(this)">Xóa</button></div>
        `;
        container.appendChild(vRow);
        reindexAll();
    }

    function removeVariantRow(btn) {
        const v = btn.closest('.variant-row');
        if (v) {
            v.remove();
            reindexAll();
        }
    }

    /**
     * Reindex tất cả input names theo cấu trúc Spring:
     *  - colors[i].colorName
     *  - colors[i].images  (file input name)
     *  - colors[i].defaultPreview
     *  - colors[i].existingPreviewFilename
     *  - variants[j].size, variants[j].price, variants[j].stock, variants[j].colorIndex (flat list)
     */
    function reindexAll() {
        const colorRows = document.querySelectorAll('.color-row');

        // colors
        colorRows.forEach((row, i) => {
            row.dataset.colorIndex = i;
            const numberSpan = row.querySelector('.color-number');
            if (numberSpan) numberSpan.textContent = i + 1;

            const colorName = row.querySelector('[data-name="colorName"]');
            if (colorName) colorName.name = `colors[${i}].colorName`;

            const fileInput = row.querySelector('[data-name="images"]');
            if (fileInput) {
                // dùng name dạng colors[i].images để service lấy files từ request.getFiles("colors[i].images")
                fileInput.name = `colors[${i}].images`;
            }

            const existingPreview = row.querySelector('[data-name="existingPreviewFilename"]');
            if (existingPreview) existingPreview.name = `colors[${i}].existingPreviewFilename`;

            const defaultPreview = row.querySelector('[data-name="defaultPreview"]');
            if (defaultPreview) {
                defaultPreview.name = `colors[${i}].defaultPreview`;
                defaultPreview.value = "true";
            }
        });

        // variants: index toàn cục (flat)
        let globalVarIndex = 0;
        colorRows.forEach((row, colorIdx) => {
            const variants = row.querySelectorAll('.variant-row');
            variants.forEach(vRow => {
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
     * Loại bỏ các input có index rỗng (ví dụ 'colors[]' hoặc 'variants[].size') để tránh Spring cố parse index rỗng.
     * Nếu tên chứa bất kỳ cặp [] rỗng nào -> remove attribute name.
     */
    function sanitizeBeforeSubmit() {
        document.querySelectorAll('[name]').forEach(el => {
            const nm = el.getAttribute('name');
            if (!nm) return;
            // nếu tồn tại [ ] rỗng -> xóa tên
            const matches = nm.match(/\[\s*\]/);
            if (matches) {
                el.removeAttribute('name');
            }
            // khác: kiểm tra pattern [<whitespace>]
            if (/\[\s*\]/.test(nm)) {
                el.removeAttribute('name');
            }
            // bảo đảm không có [ ] với chuỗi rỗng giữa
            const allBrackets = nm.matchAll(/\[([^\]]*)\]/g);
            for (const m of allBrackets) {
                if (m[1] === '') { el.removeAttribute('name'); break; }
            }
        });
    }

    // Gán event submit: reindex + sanitize
    document.addEventListener('DOMContentLoaded', function(){
        // nếu không có color-row nào, tạo 1 cái để tránh binding rỗng
        if (document.querySelectorAll('.color-row').length === 0) addColorRow();

        const form = document.getElementById('productForm');
        if (form) {
            form.addEventListener('submit', function(e){
                reindexAll();
                sanitizeBeforeSubmit();
                // có thể thêm validate ở đây: bắt buộc categoryId, name, ít nhất 1 colorName, v.v.
                // nếu validate fail -> e.preventDefault();
            });
        }
    });
</script>

</body>
</html>
