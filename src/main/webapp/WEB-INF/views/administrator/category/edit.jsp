<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="adminSidebarSubtitle" value="Category editing shell" />
<c:set var="adminSidebarFooterTitle" value="Do not blur the map" />
<c:set var="adminSidebarFooterCopy" value="Category edits affect storefront filters, operator understanding, and merchandising consistency." />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Category</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="category-list" data-category-mode="edit">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Catalog / Edit category</div>
                    <h1 class="page-title">Edit category</h1>
                    <p class="page-subtitle">Tune a live category without breaking the rest of the catalog language.</p>
                </div>
                <div class="action-row">
                    <a class="btn btn-light" href="${env}/admin/category/list">Back</a>
                    <button class="btn btn-dark" type="button">Update category</button>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <section class="split">
                <article class="panel">
                    <div class="panel-header">
                        <div>
                            <h3>Current category state</h3>
                            <p>Pre-filled edit surface for a live taxonomy node.</p>
                        </div>
                        <span class="badge success">Active</span>
                    </div>
                    <div class="field-grid">
                        <div class="field-span-6"><label for="catName">Category name</label><input class="input" id="catName" type="text" value="Running"></div>
                        <div class="field-span-6"><label for="catSlug">Slug</label><input class="input" id="catSlug" type="text" value="running"></div>
                        <div class="field-span-12"><label for="catDesc">Description</label><textarea class="textarea" id="catDesc">Curated shoes for long-distance training and daily performance use.</textarea></div>
                        <div class="field-span-6"><label for="catStatus">Status</label><select class="select" id="catStatus"><option selected>ACTIVE</option><option>INACTIVE</option><option>DRAFT</option></select></div>
                        <div class="field-span-6"><label for="catBadge">Badge tone</label><input class="input" id="catBadge" type="text" value="Black / neutral"></div>
                    </div>
                </article>
                <aside class="panel">
                    <div class="panel-header">
                        <div>
                            <h3>Impact</h3>
                            <p>What changes to this category are likely to influence.</p>
                        </div>
                    </div>
                    <div class="list-stack" id="categoryImpact"></div>
                </aside>
            </section>
            <div class="footer-space"></div>
        </div>
    </main>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/categories.js"></script>
</body>
</html>
