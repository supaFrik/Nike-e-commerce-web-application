<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- directive của JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
    <link rel="icon" type="image/png" sizes="16x16" href="${root}/administrator/assets/images/favicon.png">
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
    <div id="main-wrapper" data-theme="light" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full" data-sidebar-position="fixed" data-header-position="fixed" data-boxed-layout="full">
        
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
                        <h2 class="page-title text-truncate text-dark font-weight-medium mb-1">List Category</h2>
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
                <!-- Flash messages -->
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                <!-- ============================================================== -->
                <!-- Start Page Content -->
                <!-- ============================================================== -->
                <!-- basic table -->
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body">
                                <div class="row">
                                    <!-- Column -->
                                    <div class="col-md-6 col-lg-3 col-xlg-3">
                                        <div class="card card-hover">
                                            <div class="p-2 bg-primary text-center">
                                                <h1 class="font-light text-white">2,064</h1>
                                                <h6 class="text-white">Total Tickets</h6>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Column -->
                                    <div class="col-md-6 col-lg-3 col-xlg-3">
                                        <div class="card card-hover">
                                            <div class="p-2 bg-cyan text-center">
                                                <h1 class="font-light text-white">1,738</h1>
                                                <h6 class="text-white">Responded</h6>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Column -->
                                    <div class="col-md-6 col-lg-3 col-xlg-3">
                                        <div class="card card-hover">
                                            <div class="p-2 bg-success text-center">
                                                <h1 class="font-light text-white">1100</h1>
                                                <h6 class="text-white">Resolve</h6>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Column -->
                                    <div class="col-md-6 col-lg-3 col-xlg-3">
                                        <div class="card card-hover">
                                            <div class="p-2 bg-danger text-center">
                                                <h1 class="font-light text-white">964</h1>
                                                <h6 class="text-white">Pending</h6>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Column -->
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-striped table-bordered no-wrap">
                                        <thead>
                                            <tr>
                                            	<th scope="col">No.</th>
                                                <th scope="col">Id</th>
                                                <th scope="col">Name</th>
                                                <th scope="col">Create by</th>
                                                <th scope="col">Update by</th>
                                                <th scope="col">Create date</th>
                                                <th scope="col">Update date</th>
                                                <th scope="col">Status</th>
                                                <th scope="col">Description</th>
                                                <th scope="col">Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="category" items="${categories}" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td>${category.id}</td>
                                                <td>${category.name}</td>
                                                <td><fmt:formatDate value="${category.createDate}" pattern="dd-MM-yyyy"/></td>
                                                <td><fmt:formatDate value="${category.updateDate}" pattern="dd-MM-yyyy"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${empty category.status || fn:toLowerCase(category.status) eq 'active'}">
                                                            <span class="badge badge-success">Active</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-secondary">Inactive</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="${env}/admin/category/edit/${category.id}" class="btn btn-sm btn-primary">Edit</a>
                                                    <c:choose>
                                                        <c:when test="${empty category.status || fn:toLowerCase(category.status) eq 'active'}">
                                                            <a href="${env}/admin/category/delete/${category.id}" class="btn btn-sm btn-danger" onclick="return confirm('Inactivate this category?');">Delete</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${env}/admin/category/activate/${category.id}" class="btn btn-sm btn-success">Activate</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty categories}">
                                            <tr><td colspan="7" class="text-center">No categories found.</td></tr>
                                        </c:if>
                                        </tbody>
                                        <tfoot>
                                        <tr>
                                            <th>No.</th>
                                            <th>Id</th>
                                            <th>Name</th>
                                            <th>Create date</th>
                                            <th>Update date</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                        </tfoot>
                                    </table>
                                </div>
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
    <!-- ============================================================== -->
    <!-- End Wrapper -->
    <!-- ============================================================== -->
    <!-- End Wrapper -->
    <!-- ============================================================== -->
    
	<!-- Slider js: All Jquery-->
    <jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"></jsp:include>
</body>

</html>