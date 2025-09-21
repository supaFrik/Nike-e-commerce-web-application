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
                        <h2 class="page-title text-truncate text-dark font-weight-medium mb-1">Edit Product</h2>
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
	                        	<sf:form class="form" action="${env }/admin/product/edit-save" method="post"
	                        	modelAttribute="product" enctype="multipart/form-data">

	                        		 <div class="form-body">

										<sf:hidden path="id" />
										<!-- id > 0 -> Edit -->
	                        		 	
	                        			<div class="row">
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="category">Select category</label>
			                                        <sf:select path="category.id" class="form-control" id="category">
			                                            <sf:option value="" disabled="true">-- Select a Category --</sf:option>
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
										
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="salePrice">Sale price</label>
			                                        <sf:input path="salePrice" type="number" autocomplete="off" id="salePrice" name="salePrice" class="form-control" placeholder="Sale price" step="0.01"></sf:input>
                                        		</div>
	                                    	</div>
										</div>

										<div class="row">
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="type">Product type</label>
			                                        <sf:input path="type" type="text" class="form-control" id="type" name="type" placeholder="Product type (e.g., shoes, clothing)"></sf:input>
                                        		</div>
	                                    	</div>

	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="seo">SEO</label>
			                                        <sf:input path="seo" type="text" class="form-control" id="seo" name="seo" placeholder="SEO keywords"></sf:input>
                                        		</div>
	                                    	</div>
										</div>
										
										<div class="row">
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="create">Create by</label>
			                                        <sf:select path="userCreateProduct.id" class="form-control" id="createBy">
			                                            <sf:option value="" disabled="true">-- Select Creator --</sf:option>
			                                            <sf:options items="${customers }" itemValue="id" itemLabel="username"></sf:options>
			                                        </sf:select>
                                        		</div>
	                                    	</div>
									
											<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="update">Update by</label>
			                                        <sf:select path="userUpdateProduct.id" class="form-control" id="updateBy">
			                                            <sf:option value="" disabled="true">-- Select Updater --</sf:option>
                                                        <sf:options items="${customers }" itemValue="id" itemLabel="username"></sf:options>
			                                        </sf:select>
                                        		</div>
	                                    	</div>
										</div>
										
										<div class="row">
	                        		 		<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="createdate">Create date</label>
			                                        <sf:input path="createDate" class="form-control" type="date"
			                                        			id="createDate" name="createDate"></sf:input>
                                        		</div>
	                                    	</div>
									
											<div class="col-md-6">
												<div class="form-group mb-4">
			                                        <label for="updatedate">Update date</label>
			                                        <sf:input path="updateDate" class="form-control" type="date"
			                                        			id="updateDate" name="updateDate"></sf:input>
                                        		</div>
	                                    	</div>
										</div>
										
										<div class="row">
	                        		 		<div class="col-md-12">
												<div class="form-group mb-4">
			                                        <label for="description">Description</label>
			                                        <sf:textarea path="description" id="description" name="description"
																class="form-control" rows="4" placeholder="Product description..."></sf:textarea>
                                        		</div>
	                                    	</div>
										</div>
										
										<div class="row">
	                        		 		<div class="col-md-3">
												<div class="form-group mb-4">
													<div class="form-check">
														<sf:checkbox path="favourites" class="form-check-input" id="favourites" name="favourites"></sf:checkbox>
				                                        <label class="form-check-label" for="favourites">Featured Product</label>
													</div>
                                        		</div>
	                                    	</div>

	                                    	<div class="col-md-9">
												<div class="form-group mb-4">
													<label for="status">Status</label>
													<sf:select path="status" class="form-control" id="status">
														<sf:option value="ACTIVE">Active</sf:option>
														<sf:option value="INACTIVE">Inactive</sf:option>
													</sf:select>
                                        		</div>
	                                    	</div>
										</div>
										
										<div class="row">
	                        		 		<div class="col-md-12">
												<div class="form-group mb-4">
			                                        <label for="avatarFile">Choose product Avatar</label>
                                    				<input id="avatarFile" name="avatarFile" type="file" class="form-control-file" accept="image/*">
                                        		</div>
	                                    	</div>
										</div>
										
										<div class="row">
	                        		 		<div class="col-md-12">
												<div class="form-group mb-4">
			                                        <label for="imageFiles">Choose product Images</label>
                                    				<input id="imageFiles" name="imageFiles" type="file"
                                    				class="form-control-file" multiple="multiple" accept="image/*">
                                        		</div>
	                                    	</div>
										</div>
										
										<div class="row">
	                        		 		<div class="col-md-12">
												<div class="form-group mb-4">
			                                        <a href="${env }/admin/product/list" class="btn btn-secondary active" role="button" aria-pressed="true">
			                                        	Back to list
			                                        </a>
                                    				<button type="submit" class="btn btn-primary">Save Changes</button>
                                        		</div>
	                                    	</div>
										</div>
										
	                        		</div>
	                        	</sf:form>
	                        </div>
	                    </div>    
                  </div> 
				
				</div>

				<!-- End PAge Content -->
				<!-- ============================================================== -->
			</div>
			<!-- ============================================================== -->
			<!-- End Container fluid  -->
			<!-- ============================================================== -->
			<!-- ============================================================== -->
			<!-- footer -->
			<!-- ============================================================== -->
			<jsp:include page="/WEB-INF/views/administrator/layout/footer.jsp"></jsp:include>
			<!-- ============================================================== -->
			<!-- End footer -->
			<!-- ============================================================== -->
		</div>
		<!-- ============================================================== -->
		<!-- End Page wrapper  -->
		<!-- ============================================================== -->
	</div>
	<!-- ============================================================== -->
	<!-- End Wrapper -->
	<!-- ============================================================== -->
	<!-- End Wrapper -->
	<div class="chat-windows "></div>
	<!-- ============================================================== -->
	<!-- Slider js: All Jquery-->
	<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"></jsp:include>
</body>

</html>