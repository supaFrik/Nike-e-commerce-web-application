<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

		<!-- Topbar header - style you can find in pages.scss -->
		<jsp:include page="/WEB-INF/views/administrator/layout/header.jsp"></jsp:include>
		<!-- End Topbar header -->

		<!-- Left Sidebar - style you can find in sidebar.scss  -->
		<jsp:include page="/WEB-INF/views/administrator/layout/left-slide-bar.jsp"></jsp:include>
		<!-- End Left Sidebar - style you can find in sidebar.scss  -->

		<!-- Page wrapper  -->
		<!-- ============================================================== -->
		<div class="page-wrapper">
			<!-- ============================================================== -->
			<!-- Bread crumb and right sidebar toggle -->
			<!-- ============================================================== -->
			<div class="page-breadcrumb">
				<div class="row">
					<div class="col-7 align-self-center">
						<h2
							class="page-title text-truncate text-dark font-weight-medium mb-1">List
							Product</h2>
					</div>
				</div>
			</div>
			<!-- ============================================================== -->
			<!-- End Bread crumb and right sidebar toggle -->
			<!-- ============================================================== -->
			<!-- Container fluid  -->
			<!-- ============================================================== -->
			<div class="container-fluid">
				<!-- ============================================================== -->
				<!-- Start Page Content -->
				<!-- ============================================================== -->
				<!-- basic table -->
				<div class="row">
					<div class="col-12">
						<div class="card">

							<div class="card-body">
								<form action="${env }/admin/product/view" method="get">
									<div class="table-responsive">
										<div class="row">
											<div class="col-md-2">
												<div class="form-group mb-4">
													<a href="${env }/admin/product/add" role="button"
														class="btn btn-primary">Add new product</a>
												</div>
											</div>
											<div class="col-md-4">
												<div class="form-group mb-4">
													<h3>Total products: &nbsp ${searchModel.totalItems }</h3>
												</div>
											</div>

											<div class="col-md-3">
												<div class="form-group mb-4">
													<%-- label>Current page</label--%> 
													<input id="currentPage" type="hidden"
														name="currentPage" class="form-control"
														value="${searchModel.currentPage }">
												</div>
											</div>
											
											<div class="col-md-3">
												<div class="form-group mb-4">
													<%--label>Total items</label--%> 
													<input id="totalItems" type="hidden"
														name="totalItems" class="form-control"
														value="${searchModel.totalItems }">
												</div>
											</div>

										</div>
										<!-- Tìm kiếm -->
										<div class="row">
											<div class="col-md-2">
												<div class="form-group mb-4">
													<!-- 
													<label for="status">&nbsp;&nbsp;&nbsp;&nbsp;</label>
													<input type="checkbox" class="form-check-input" id="status" name="status" checked="checked" />
			                                        <label for="status">Active</label>
			                                         -->
													<select class="form-control" id="status" name="status">
														<option value="2">All</option>
														<option value="1">Active</option>
														<option value="0">Inactive</option>
													</select>
												</div>
											</div>

											<div class="col-md-2">
												<select class="form-control" id="categoryId"
													name="categoryId" style="margin-right: 10px;">
													<option value="0">All category</option>
													<c:forEach items="${categories }" var="category">
														<option value="${category.id }">${category.name }</option>
													</c:forEach>
												</select>
											</div>

											<div class="col-md-2">
												<input class="form-control" type="date" id="beginDate"
													name="beginDate" />
											</div>
											<div class="col-md-2">
												<input class="form-control" type="date" id="endDate"
													name="endDate" />
											</div>

											<div class="col-md-2">
												<input type="text" class="form-control" id="keyword"
													name="keyword" placeholder="Search keyword" />
											</div>

											<div class="col-md-1">
												<button type="submit" id="btnSearch" name="btnSearch"
													class="btn btn-primary">Search</button>
											</div>
											
											<div class="col-md-1">
												<button type="reset" id="btnClear" name="btnClear"
													class="btn btn-primary">Clear</button>
											</div>
										</div>
										<!-- Hết tìm kiếm -->
										<table id="zero_config"
											class="table table-striped table-bordered no-wrap">
											<thead>
												<tr align="center">
													<th scope="col">No.</th>
													<th scope="col">Id</th>
													<th scope="col">Category</th>
													<th scope="col">Name</th>
													<th scope="col">Price</th>
													<th scope="col">Sale price</th>
													<th scope="col">Avatar</th>
													<th scope="col">Description</th>
													<th scope="col">Details</th>
													<th scope="col">Create by</th>
													<th scope="col">Update by</th>
													<th scope="col">Create date</th>
													<th scope="col">Update date</th>
													<th scope="col">Status</th>
													<th scope="col">Is favourite</th>
													<th scope="col">Seo</th>
													<th scope="col">Actions</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="product" items="${products }"
													varStatus="loop">
													<tr>
														<th scope="row">${loop.index + 1 }</th>
														<td>${product.id }</td>
														<td>
															<c:choose>
																<c:when test="${product.category != null}">
																	${product.category.name}
																</c:when>
																<c:otherwise>
																	No Category
																</c:otherwise>
															</c:choose>
														</td>
														<td>${product.name }</td>
														<td align="right"><fmt:formatNumber
																value="${product.price }" minFractionDigits="0"></fmt:formatNumber>
														</td>
														<td align="right">
															<c:choose>
																<c:when test="${product.salePrice != null}">
																	<fmt:formatNumber value="${product.salePrice }" minFractionDigits="0"></fmt:formatNumber>
																</c:when>
																<c:otherwise>
																	N/A
																</c:otherwise>
															</c:choose>
														</td>

														<td>
															<c:choose>
																<c:when test="${product.avatar != null && !empty product.avatar}">
																	<img width="40px" height="40px"
																		src="${env }/UploadFiles/${product.avatar }"
																		class="light-logo">
																</c:when>
																<c:otherwise>
																	No Image
																</c:otherwise>
															</c:choose>
														</td>

														<td>
															<c:choose>
																<c:when test="${product.type != null}">
																	${product.type}'s Shoes
																</c:when>
																<c:otherwise>
																	Product
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${product.description != null}">
																	${product.description}
																</c:when>
																<c:otherwise>
																	No Description
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${product.userCreateProduct != null}">
																	${product.userCreateProduct.username}
																</c:when>
																<c:otherwise>
																	Unknown
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${product.userUpdateProduct != null}">
																	${product.userUpdateProduct.username}
																</c:when>
																<c:otherwise>
																	Unknown
																</c:otherwise>
															</c:choose>
														</td>

														<td>
															<c:choose>
																<c:when test="${product.createDate != null}">
																	<fmt:formatDate value="${product.createDate }" pattern="dd-MM-yyyy" />
																</c:when>
																<c:otherwise>
																	N/A
																</c:otherwise>
															</c:choose>
														</td>
														<td>
															<c:choose>
																<c:when test="${product.updateDate != null}">
																	<fmt:formatDate value="${product.updateDate }" pattern="dd-MM-yyyy" />
																</c:when>
																<c:otherwise>
																	Not Updated
																</c:otherwise>
															</c:choose>
														</td>

														<td><span id="_product_status_${product.id }">
																<c:choose>
																	<c:when test="${product.status == 'Active' || product.status == '1'}">
																		<span>Active</span>
																	</c:when>
																	<c:otherwise>
																		<span>Inactive</span>
																	</c:otherwise>
																</c:choose>
														</span></td>
														<td><span id="_product_isFavourites_${product.id }">
																<c:choose>
																	<c:when test="${product.favourites == true}">
																		<span>Yes</span>
																	</c:when>
																	<c:otherwise>
																		<span>No</span>
																	</c:otherwise>
																</c:choose>
														</span></td>
														<td>
															<c:choose>
																<c:when test="${product.seo != null}">
																	${product.seo}
																</c:when>
																<c:otherwise>
																	N/A
																</c:otherwise>
															</c:choose>
														</td>
														<td><a
															href="${env }/admin/product/edit/${product.id }"
															role="button" class="btn btn-primary">Edit</a> <a
															href="${env }/admin/product/delete/${product.id }"
															role="button" class="btn btn-secondary">Delete</a></td>
													</tr>
												</c:forEach>
											</tbody>
											<tfoot>
												<tr align="center">
													<th scope="col">No.</th>
													<th scope="col">Id</th>
													<th scope="col">Category</th>
													<th scope="col">Name</th>
													<th scope="col">Price</th>
													<th scope="col">Sale price</th>
													<th scope="col">Avatar</th>
													<th scope="col">Description</th>
													<th scope="col">Details</th>
													<th scope="col">Create by</th>
													<th scope="col">Update by</th>
													<th scope="col">Create date</th>
													<th scope="col">Update date</th>
													<th scope="col">Status</th>
													<th scope="col">Is hot</th>
													<th scope="col">Seo</th>
													<th scope="col">Actions</th>
												</tr>
											</tfoot>
										</table>

										<div class="row">
											<div class="col-md-6">
												<div class="form-group mb-4">
													<a href="${env }/admin/product/add" role="button"
														class="btn btn-primary">Add new product</a>
												</div>
											</div>
											<%-- Phan trang --%>
											<div class="col-md-6">
												<div class="pagination float-right">
													<div id="paging"></div>
												</div>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
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

	<!-- End Wrapper -->
	<!-- ============================================================== -->

	<!-- Slider js: All Jquery-->
	<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"></jsp:include>

	<!-- pagination -->
	<script type="text/javascript">
		$( document ).ready(function() {
			//Dat gia tri cua status ung voi dieu kien search truoc do
			$("#status").val(${searchModel.status});
			//Dat gia tri cua category ung voi dieu kien search truoc do
			$("#categoryId").val(${searchModel.categoryId});
			//Dat gia tri cua keyword ung voi dieu kien search truoc do
			$("#keyword").val('${searchModel.keyword}');	
			$("#beginDate").val('${searchModel.beginDate}');
			$("#endDate").val('${searchModel.endDate}');
			
			$("#paging").pagination({
				currentPage: ${searchModel.currentPage }, //Trang hien tai
				items: ${searchModel.totalItems }, //Tong so san pham (total products)
				itemsOnPage: ${searchModel.itemOnPage },
				cssStyle: 'light-theme',
				onPageClick: function(pageNumber, event) {
					$('#currentPage').val(pageNumber);
					$('#btnSearch').trigger('click');
				},
			});
		});
	</script>

</body>

</html>