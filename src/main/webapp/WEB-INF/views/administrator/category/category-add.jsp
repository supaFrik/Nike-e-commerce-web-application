<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html dir="ltr" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Add Category</title>
    <jsp:include page="/WEB-INF/views/common/variables.jsp"/>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp"/>
</head>
<body>
<div id="main-wrapper" data-theme="light" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full" data-sidebar-position="fixed" data-header-position="fixed" data-boxed-layout="full">
    <jsp:include page="/WEB-INF/views/administrator/layout/header.jsp"/>
    <jsp:include page="/WEB-INF/views/administrator/layout/left-slide-bar.jsp"/>
    <div class="page-wrapper">
        <div class="page-breadcrumb">
            <div class="row">
                <div class="col-7 align-self-center">
                    <h2 class="page-title text-truncate text-dark font-weight-medium mb-1">Add Category</h2>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <sf:form method="post" action="${env}/admin/category/add-save" modelAttribute="category" cssClass="form">
                                <div class="form-body">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group mb-4">
                                                <label for="name">Category Name <span class="text-danger">*</span></label>
                                                <sf:input path="name" id="name" cssClass="form-control" placeholder="Category name" required="required"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group mb-4">
                                                <a href="${env}/admin/category/list" class="btn btn-secondary">Back to list</a>
                                                <button type="submit" class="btn btn-primary">Create Category</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </sf:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/WEB-INF/views/administrator/layout/footer.jsp"/>
    </div>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"/>
</body>
</html>

