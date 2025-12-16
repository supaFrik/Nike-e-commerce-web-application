<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- directive của JSTL -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html dir="ltr" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Category</title>
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
                    <h2 class="page-title text-truncate text-dark font-weight-medium mb-1">Edit Category</h2>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <sf:form method="post" action="${env}/admin/category/edit-save" modelAttribute="category" cssClass="form">
                                <sf:hidden path="id"/>
                                <div class="form-body">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group mb-4">
                                                <label for="name">Category Name <span class="text-danger">*</span></label>
                                                <sf:input path="name" id="name" cssClass="form-control" placeholder="Category name" required="required"/>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group mb-4">
                                                <label for="status">Status</label>
                                                <sf:select path="status" id="status" cssClass="form-control">
                                                    <sf:option value="Active" label="Active"/>
                                                    <sf:option value="Inactive" label="Inactive"/>
                                                </sf:select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-3">
                                            <div class="form-group mb-4">
                                                <label>Create Date</label>
                                                <div class="form-control-plaintext">
                                                    <fmt:formatDate value="${category.createDate}" pattern="dd-MM-yyyy"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group mb-4">
                                                <label>Last Update</label>
                                                <div class="form-control-plaintext">
                                                    <fmt:formatDate value="${category.updateDate}" pattern="dd-MM-yyyy"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group mb-4">
                                                <a href="${env}/admin/category/list" class="btn btn-secondary">Back to list</a>
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
        </div>
        <jsp:include page="/WEB-INF/views/administrator/layout/footer.jsp"/>
    </div>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"/>
</body>

</html>