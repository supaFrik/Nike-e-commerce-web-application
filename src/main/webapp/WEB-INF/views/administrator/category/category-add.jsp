<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html dir="ltr" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" type="image/png" sizes="16x16" href="${root}/administrator/assets/images/favicon.png">
    <title>Thêm danh mục</title>

    <!-- variables -->
    <jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>

    <!-- Custome css resource file -->
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp"></jsp:include>
</head>

<body>
    <div id="main-wrapper" data-theme="light" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full" data-sidebar-position="fixed" data-header-position="fixed" data-boxed-layout="full">

        <jsp:include page="/WEB-INF/views/administrator/layout/header.jsp"></jsp:include>
        <jsp:include page="/WEB-INF/views/administrator/layout/left-slide-bar.jsp"></jsp:include>

        <div class="page-wrapper">
            <div class="page-breadcrumb">
                <div class="row">
                    <div class="col-7 align-self-center">
                        <h2 class="page-title text-truncate text-dark font-weight-medium mb-1">Thêm danh mục</h2>
                    </div>
                </div>
            </div>

            <div class="container-fluid">
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <div class="row">
                    <div class="col-md-8">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">
                                    <c:choose>
                                        <c:when test="${empty category.id}">Danh mục mới</c:when>
                                        <c:otherwise>Chỉnh sửa danh mục</c:otherwise>
                                    </c:choose>
                                </h4>
                                <form id="categoryForm" action="${env}/admin/category/save" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <!-- If editing, the controller may populate category.id; for add leave empty -->
                                    <input type="hidden" name="id" value="${category.id}" />

                                    <div class="form-group">
                                        <label for="name">Tên <span style="color:red">*</span></label>
                                        <input type="text" class="form-control" id="name" name="name" value="<c:out value='${category.name}' default=''/>" placeholder="Nhập tên danh mục" required />
                                    </div>

                                    <div class="form-group">
                                        <label for="description">Mô tả</label>
                                        <textarea class="form-control" id="description" name="description" rows="4" placeholder="Mô tả (không bắt buộc)"><c:out value='${param.description}' default=''/></textarea>
                                    </div>

                                    <div class="form-group">
                                        <label for="status">Trạng thái</label>
                                        <select class="form-control" id="status" name="status">
                                            <option value="active" <c:if test="${empty category.status || category.status == 'active' || category.status == 'Active'}">selected</c:if>>Hoạt động</option>
                                            <option value="inactive" <c:if test="${category.status == 'inactive' || category.status == 'Inactive'}">selected</c:if>>Không hoạt động</option>
                                        </select>
                                    </div>

                                    <div class="form-actions">
                                        <c:choose>
                                            <c:when test="${empty category.id}">
                                                <button type="submit" class="btn btn-primary">Tạo</button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="submit" class="btn btn-primary">Cập nhật</button>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${env}/admin/category/list" class="btn btn-secondary">Hủy</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <jsp:include page="/WEB-INF/views/administrator/layout/footer.jsp"></jsp:include>
        </div>

    </div>

    <!-- JS includes -->
    <jsp:include page="/WEB-INF/views/administrator/layout/js.jsp"></jsp:include>

    <script>
        // Simple client-side validation to ensure name is provided
        (function(){
            var form = document.getElementById('categoryForm');
            if(!form) return;
            form.addEventListener('submit', function(e){
                var name = document.getElementById('name').value.trim();
                if(!name){
                    e.preventDefault();
                    alert('Vui lòng nhập tên danh mục.');
                    document.getElementById('name').focus();
                    return false;
                }
                return true;
            });
        })();
    </script>
</body>

</html>
