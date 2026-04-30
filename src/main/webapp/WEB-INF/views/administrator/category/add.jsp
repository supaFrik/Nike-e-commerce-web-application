<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="adminSidebarSubtitle" value="Category creation shell" />
<c:set var="adminSidebarFooterTitle" value="Keep labels clean" />
<c:set var="adminSidebarFooterCopy" value="A category should group product intent, not hide uncertainty." />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Category</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="category-list">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <header class="topbar">
            <div class="topbar-inner">
                <div>
                    <div class="eyebrow">Catalog / Add category</div>
                    <h1 class="page-title">Add category</h1>
                    <p class="page-subtitle">Define a category with one clear name, one usable slug, and one sentence of context.</p>
                </div>
                <div class="action-row">
                    <a class="btn btn-light" href="${env}/admin/category/list">Cancel</a>
                    <button class="btn btn-dark" type="button">Save category</button>
                </div>
            </div>
        </header>
        <div class="content-wrap">
            <section class="split">
                <article class="panel">
                    <div class="panel-header">
                        <div>
                            <h3>Category details</h3>
                            <p>Keep the structure lean enough that operators can understand it immediately.</p>
                        </div>
                    </div>
                    <div class="field-grid">
                        <div class="field-span-6"><label for="catName">Category name</label><input class="input" id="catName" type="text" placeholder="Running"></div>
                        <div class="field-span-6"><label for="catSlug">Slug</label><input class="input" id="catSlug" type="text" placeholder="running"></div>
                        <div class="field-span-12"><label for="catDesc">Description</label><textarea class="textarea" id="catDesc" placeholder="Keep it short and purposeful."></textarea></div>
                        <div class="field-span-6"><label for="catStatus">Status</label><select class="select" id="catStatus"><option>ACTIVE</option><option>INACTIVE</option><option>DRAFT</option></select></div>
                        <div class="field-span-6"><label for="catBadge">Badge tone</label><input class="input" id="catBadge" type="text" placeholder="Black / neutral / red"></div>
                    </div>
                </article>
                <aside class="panel">
                    <div class="panel-header">
                        <div>
                            <h3>Category rules</h3>
                            <p>Small reminders that keep the catalog sharp.</p>
                        </div>
                    </div>
                    <div class="rule-list">
                        <div class="list-item"><h4>Use precise labels</h4><p>Names should map to clear product intent instead of vague merchandising language.</p></div>
                        <div class="list-item"><h4>Avoid overlap</h4><p>If two categories compete for the same products, the taxonomy is already losing clarity.</p></div>
                        <div class="list-item"><h4>Keep visibility explicit</h4><p>Status should immediately tell operators whether the category is live, draft, or hidden.</p></div>
                    </div>
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
