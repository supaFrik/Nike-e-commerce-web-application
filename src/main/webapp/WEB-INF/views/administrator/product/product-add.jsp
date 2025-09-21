<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- directive của JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html dir="ltr" lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Tell the browser to be responsive to screen width -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<!-- Favicon icon -->
<link rel="icon" type="image/png" sizes="16x16"
	href="${env }/administrator/assets/images/favicon.png">
<title>${title }</title>
<!-- variables -->
<jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>

<!-- Custome css resource file -->
<jsp:include page="/WEB-INF/views/administrator/layout/css.jsp"></jsp:include>

</head>

<body>
	<!-- ============================================================== -->

	<!-- Main wrapper - style you can find in pages.scss -->
	<!-- ============================================================== -->
	<div id="main-wrapper" data-theme="light" data-layout="vertical"
		data-navbarbg="skin6" data-sidebartype="full"
		data-sidebar-position="fixed" data-header-position="fixed"
		data-boxed-layout="full">
		<!-- ============================================================== -->
		<!-- Topbar header - style you can find in pages.scss -->
		<!-- ============================================================== -->
		<jsp:include page="/WEB-INF/views/administrator/layout/header.jsp"></jsp:include>
		<!-- ============================================================== -->
		<!-- End Topbar header -->

		<!-- ============================================================== -->
		<!-- Left Sidebar - style you can find in sidebar.scss  -->
		<!-- ============================================================== -->
		<jsp:include page="/WEB-INF/views/administrator/layout/left-slide-bar.jsp"></jsp:include>
		<!-- ============================================================== -->
		<!-- End Left Sidebar - style you can find in sidebar.scss  -->
		<!-- ============================================================== -->
		<!-- ============================================================== -->
		<!-- Page wrapper  -->
		<!-- ============================================================== -->
		<div class="page-wrapper">
			<!-- ============================================================== -->
            <!-- Bread crumb and right sidebar toggle -->
            <!-- ============================================================== -->
            <div class="page-breadcrumb">
                <div class="row">
                    <div class="col-7 align-self-center">
                        <h2 class="page-title text-truncate text-dark font-weight-medium mb-1">Add New Product</h2>
                    </div>
                </div>
            </div>
            <!-- ============================================================== -->
            <!-- End Bread crumb and right sidebar toggle -->
            <!-- ============================================================== -->
			<!-- ============================================================== -->
			<!-- Container fluid  -->
			<!-- ============================================================== -->
			<div class="container-fluid">
				<!-- ============================================================== -->
				<!-- Start Page Content -->
				<!-- ============================================================== -->

                <div class="row">
                	<div class="col-12">
	                    <div class="card">
	                        <div class="card-body">
	                        	<sf:form class="form" action="${env }/admin/product/add-save" method="post"
	                        	modelAttribute="product" enctype="multipart/form-data">

	                        		 <div class="form-body">
	                        			<div class="row">
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="category">Select category</label>
			                                        <sf:select path="category.id" class="form-control" id="category">
			                                            <sf:option value="" disabled="true" selected="true">-- Select a Category --</sf:option>
			                                            <sf:options items="${categories }" itemValue="id" itemLabel="name"></sf:options>
			                                        </sf:select>
                                        		</div>
	                                    	</div>

	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="name">Product name</label>
			                                        <sf:input path="name" type="text" class="form-control" id="name" name="name" placeholder="product name"></sf:input>
                                        		</div>
	                                    	</div>
										</div>

										<div class="row">
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="price">Price</label>
			                                        <sf:input path="price" type="number" autocomplete="off" id="price" name="price" class="form-control" placeholder="price" step="0.01"></sf:input>
                                        		</div>
	                                    	</div>

	                                    	<!--
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="salePrice">Sale price</label>
			                                        <sf:input path="salePrice" type="number" autocomplete="off" id="salePrice" name="salePrice" class="form-control" placeholder="Sale price" step="0.01"></sf:input>
                                        		</div>
	                                    	</div>
	                                    	-->
										</div>

										<div class="row">
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="type">Product type</label>
			                                        <sf:input path="type" type="text" class="form-control" id="type" name="type" placeholder="Product type (e.g., shoes, clothing)"></sf:input>
                                        		</div>
	                                    	</div>

                                        <!-- Main Product Image -->
                                        <div class="col-md-6">
                                            <div class="col-md-12">
                                                <div class="form-group mb-4">
                                                    <label for="avatarFile">Main Product Image *</label>
                                                    <input id="avatarFile" name="avatarFile" type="file" class="form-control-file"
                                                           accept="image/*" required="true">
                                                    <small class="form-text text-muted">This will be the primary product image displayed in listings</small>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- COLOR VARIANT SECTION -->
                                        <div class="col-md-6">
                                            <h4 class="mb-4">
                                                <i class="fas fa-palette"></i> Product Color Variants
                                                <button type="button" class="btn btn-add btn-sm float-right" onclick="addColorRow()">
                                                    <i class="fas fa-plus"></i> Add Color
                                                </button>
                                            </h4>

                                            <div id="colorContainer">
                                                <!-- Color rows will be dynamically added here -->
                                            </div>
                                        </div>

                                        <!-- Additional Gallery Images -->
                                        <div class="col-md-6">
                                            <div class="col-md-12">
                                                <div class="form-group mb-4">
                                                    <label for="imageFiles">Additional Gallery Images</label>
                                                    <input id="imageFiles" name="imageFiles" type="file" class="form-control-file"
                                                           multiple="multiple" accept="image/*">
                                                    <small class="form-text text-muted">Select multiple images for the product gallery</small>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Product Settings -->
                                        <div class="col-md-6">
                                            <div class="col-md-12">
                                                <div class="form-group mb-4">
                                                    <label for="createBy">Created By</label>
                                                    <sf:select path="userCreateProduct.id" class="form-control" id="createBy">
                                                        <sf:option value="" disabled="true" selected="true">-- Select Creator --</sf:option>
                                                        <sf:options items="${customers}" itemValue="id" itemLabel="username"></sf:options>
                                                    </sf:select>
                                                </div>
                                            </div>

                                            <div class="col-md-12">
                                                <div class="form-group mb-4">
                                                    <label for="updateBy">Updated By</label>
                                                    <sf:select path="userUpdateProduct.id" class="form-control" id="updateBy">
                                                        <sf:option value="" disabled="true" selected="true">-- Select Updater --</sf:option>
                                                        <sf:options items="${customers}" itemValue="id" itemLabel="username"></sf:options>
                                                    </sf:select>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col">
                                            <div class="col-md-4">
                                                <div class="form-group mb-4">
                                                    <label for="seo">SEO Keywords</label>
                                                    <sf:input path="seo" type="text" class="form-control" id="seo"
                                                             placeholder="SEO keywords separated by commas"></sf:input>
                                                </div>
                                            </div>

                                            <div class="col-md-4">
                                                <div class="form-group mb-4">
                                                    <label for="status">Status</label>
                                                    <sf:select path="status" class="form-control" id="status">
                                                        <sf:option value="Bestseller">Bestseller</sf:option>
                                                        <sf:option value="Hot Take">Hot Take</sf:option>
                                                        <sf:option value="JUST_IN">Just In</sf:option>
                                                        <sf:option value="SALE">On Sale</sf:option>
                                                    </sf:select>
                                                </div>
                                            </div>
                                            <div class="form-group mb-4">
                                                    <div class="form-check mt-4">
                                                        <sf:checkbox path="favourites" class="form-check-input" id="favourites"></sf:checkbox>
                                                        <label class="form-check-label" for="favourites">
                                                            <strong>Featured Product</strong>
                                                        </label>
                                                    </div>
                                                </div>
                                        </div>
                                    </div>
                                    <!-- Form Actions -->
                                                                            <div class="row">
                                                                                <div class="col-md-12">
                                                                                    <div class="form-group mb-4">
                                                                                        <hr>
                                                                                        <a href="${env}/admin/product/list" class="btn btn-secondary">
                                                                                            <i class="fas fa-arrow-left"></i> Back to List
                                                                                        </a>
                                                                                        <button type="submit" class="btn btn-primary ml-2">
                                                                                            <i class="fas fa-save"></i> Save Product
                                                                                        </button>
                                                                                        <button type="button" class="btn btn-info ml-2" onclick="previewProduct()">
                                                                                            <i class="fas fa-eye"></i> Preview
                                                                                        </button>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                </sf:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <jsp:include page="/WEB-INF/views/administrator/layout/footer.jsp"></jsp:include>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"></jsp:include>

    <script>
        let colorIndex = 0;
        let variantIndex = 0;

        // Add new color row
        function addColorRow() {
            const container = document.getElementById('colorContainer');
            const colorRow = document.createElement('div');
            colorRow.className = 'color-row';
            colorRow.id = 'color-' + colorIndex;

            colorRow.innerHTML = `
                <div class="color-header">
                    <h6 class="mb-0 d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-tint"></i> Color Variant #${colorIndex + 1}</span>
                        <button type="button" class="btn btn-sm btn-danger" onclick="removeColorRow(${colorIndex})">
                            <i class="fas fa-trash"></i> Remove Color
                        </button>
                    </h6>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Color Name *</label>
                            <input type="text" name="colorNames" class="form-control"
                                   placeholder="e.g., Black, White, Red" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Color Base Image</label>
                            <input type="file" name="colorBaseImages" class="form-control-file"
                                   accept="image/*">
                            <small class="text-muted">Representative image for this color</small>
                        </div>
                    </div>
                </div>

                <div class="variants-section">
                    <h6 class="mb-2">
                        <i class="fas fa-cubes"></i> Size Variants for this Color
                        <button type="button" class="btn btn-add btn-xs ml-2" onclick="addVariantRow(${colorIndex})">
                            <i class="fas fa-plus"></i> Add Size
                        </button>
                    </h6>
                    <div id="variants-${colorIndex}">
                        <!-- Size variants will be added here -->
                    </div>
                </div>
            `;

            container.appendChild(colorRow);

            // Add initial size variant
            addVariantRow(colorIndex);
            colorIndex++;
        }

        // Add size variant row
        function addVariantRow(colorIdx) {
            const container = document.getElementById('variants-' + colorIdx);
            const variantRow = document.createElement('div');
            variantRow.className = 'variant-row';
            variantRow.id = 'variant-' + variantIndex;

            variantRow.innerHTML = `
                <div class="row align-items-center">
                    <div class="col-md-3">
                        <input type="text" name="variantSizes" class="form-control form-control-sm"
                               placeholder="Size (e.g., 8, M, L)" required>
                    </div>
                    <div class="col-md-3">
                        <input type="number" name="variantPrices" class="form-control form-control-sm"
                               placeholder="Price" step="0.01" required>
                    </div>
                    <div class="col-md-3">
                        <input type="number" name="variantStocks" class="form-control form-control-sm"
                               placeholder="Stock" min="0" required>
                    </div>
                    <div class="col-md-2">
                        <input type="hidden" name="variantColorIndex" value="${colorIdx}">
                        <button type="button" class="btn btn-remove btn-xs" onclick="removeVariantRow(${variantIndex})">
                            <i class="fas fa-minus"></i> Remove
                        </button>
                    </div>
                </div>
            `;

            container.appendChild(variantRow);
            variantIndex++;
        }

        // Remove color row
        function removeColorRow(index) {
            const colorRow = document.getElementById('color-' + index);
            if (colorRow) {
                colorRow.remove();
            }
        }

        // Remove variant row
        function removeVariantRow(index) {
            const variantRow = document.getElementById('variant-' + index);
            if (variantRow) {
                variantRow.remove();
            }
        }

        // Preview product (placeholder function)
        function previewProduct() {
            alert('Preview functionality will be implemented based on your requirements.');
        }

        // Form validation
        document.getElementById('productForm').addEventListener('submit', function(e) {
            const colorRows = document.querySelectorAll('.color-row');
            if (colorRows.length === 0) {
                if (!confirm('No color variants added. Continue with basic product only?')) {
                    e.preventDefault();
                    return false;
                }
            }

            // Additional validation can be added here
            return true;
        });

        // Initialize with one color row
        document.addEventListener('DOMContentLoaded', function() {
            addColorRow();
        });
    </script>
</body>

</html>