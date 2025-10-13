<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- JSTL directives -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Admin Products</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <link rel="stylesheet" href="${env}/css/common/base.css" />
  <link rel="stylesheet" href="${env}/css/admin/product-list/list-product.css" />
</head>
<body>
  <header class="app-header" role="banner">
    <div class="logo" aria-label="Dashboard Home">Nike</div>
    <nav class="main-nav" aria-label="Main navigation">
      <a href="#" class="active">Products</a>
      <a href="#">Orders</a>
      <a href="#">Customers</a>
      <a href="#">Analytics</a>
      <a href="#">Settings</a>
    </nav>
    <div class="user-chip" aria-label="Current user">
      <span class="avatar" aria-hidden="true">AD</span>
      <span class="name">Admin</span>
    </div>
  </header>
  <main class="layout" role="main">
    <section class="product-panel" aria-labelledby="products-heading">
      <h1 id="products-heading" class="visually-hidden">Products</h1>
      <div class="panel-header">
        <div class="toolbar-row">
          <div class="search-wrapper pill">
            <svg class="search-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="11" cy="11" r="8" /><path d="m21 21-4.35-4.35" /></svg>
            <input id="search" type="search" placeholder="Search product…" aria-label="Search products" />
          </div>
          <button id="addProductBtn" class="btn outline add-btn" type="button">Add New Product <span class="plus" aria-hidden="true">+</span></button>
        </div>
        <div class="categories-bar">
          <!-- Added id=categoryTabs so JS can attach -->
          <div id="categoryTabs" class="tabs categories" role="tablist" aria-label="Product categories">
            <button role="tab" aria-selected="true" class="tab active" data-filter="all">All products</button>
          </div>
          <div class="sort-wrapper" data-sort>
            <label for="sort" class="visually-hidden">Sort by</label>
            <select id="sort" aria-hidden="true" tabindex="-1" style="position:absolute;left:-9999px;">
              <option value="name" selected>Name</option>
              <option value="price">Price</option>
              <option value="stock">Stock</option>
            </select>
            <button id="sortToggle" class="sort-btn" type="button" aria-haspopup="menu" aria-expanded="false" aria-controls="sortMenu">
              <span class="sort-label">Sort by</span>
              <svg class="sort-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="M3 6h13" />
                <path d="M3 12h9" />
                <path d="M3 18h5" />
                <path d="m19 6-4 4 4 4M15 10h8" />
              </svg>
            </button>
            <div id="sortMenu" class="sort-menu" role="menu" aria-label="Sort products" hidden>
              <button class="sort-option active" role="menuitemradio" aria-checked="true" data-value="name">Name</button>
              <button class="sort-option" role="menuitemradio" aria-checked="false" data-value="price">Price</button>
              <button class="sort-option" role="menuitemradio" aria-checked="false" data-value="stock">Stock</button>
            </div>
          </div>
        </div>
      </div>
      <div id="productGrid" class="product-grid" aria-live="polite" aria-busy="false"></div>
      <!-- Added empty state container expected by JS -->
      <div id="productGridEmpty" style="display:none; font-size:14px; opacity:.7; padding:24px;">No products found.</div>
      <nav class="pagination" aria-label="Product pages">
        <div class="pagination-summary" id="paginationSummary" aria-live="polite"></div>
        <ul id="paginationList" class="pagination-list" role="list"></ul>
      </nav>
    </section>
    <aside class="edit-panel" aria-labelledby="edit-heading">
      <div class="edit-panel-inner">
        <div class="edit-panel-header">
          <div class="title-line">
            <h2 id="edit-heading">Edit Products</h2>
            <a href="#" class="see-full" aria-label="Open full view of product editor">See full view →</a>
          </div>
          <button id="closeEdit" class="icon-btn" aria-label="Close edit panel">&times;</button>
        </div>
        <div class="edit-tabs" role="tablist" aria-label="Edit product sections">
          <button class="edit-tab active" role="tab" aria-selected="true" data-section="desc">Descriptions</button>
          <button class="edit-tab" role="tab" aria-selected="false" data-section="inventory">Inventory</button>
          <button class="edit-tab" role="tab" aria-selected="false" data-section="pricing">Pricing</button>
        </div>
        <form id="editForm" novalidate>
          <section class="edit-section" data-section="desc" role="tabpanel" aria-labelledby="tab-desc">
            <div class="image-box" aria-label="Product image preview">
              <img id="editImage" src="" alt="Selected product image" />
            </div>
            <h3 class="section-heading">Description</h3>
            <label class="field boxed">
              <span>Product Name</span>
              <input name="name" required maxlength="80" />
            </label>
            <label class="field boxed">
              <span>Description</span>
              <textarea name="description" rows="4" maxlength="400"></textarea>
            </label>
            <h3 class="section-heading">Category</h3>
            <div class="field-cols">
              <label class="field boxed">
                <span>Product Category</span>
                <select id="editCategorySelect" name="category"></select>
              </label>
              <label class="field boxed">
                <span>Price (₫)</span>
                <input name="price" type="number" step="0.01" min="0" required />
              </label>
              <label class="field boxed">
                <span>Stock</span>
                <input name="stock" type="number" min="0" required />
              </label>
            </div>
          </section>
          <section class="edit-section" data-section="inventory" role="tabpanel" hidden>
            <p class="placeholder">Inventory controls will go here.</p>
          </section>
          <section class="edit-section" data-section="pricing" role="tabpanel" hidden>
            <p class="placeholder">Pricing tiers / discounts configuration.</p>
          </section>
          <div class="panel-actions sticky-actions">
            <button type="button" id="discardBtn" class="btn ghost">Discard</button>
            <button type="submit" class="btn primary">Update Product</button>
          </div>
        </form>
      </div>
    </aside>
  </main>
  <template id="productCardTemplate">
    <article class="product-card" tabindex="0" role="button" aria-pressed="false" data-status="">
      <div class="thumb" data-ref="thumb">
        <img data-ref="image" alt="" />
      </div>
      <h3 class="title" data-ref="title"></h3>
      <div class="meta">
        <span data-ref="price" class="price"></span>
        <span data-ref="stock" class="stock"></span>
      </div>
      <span class="status-dot" data-ref="statusDot" aria-hidden="true"></span>
    </article>
  </template>

  <script>
    window.APP_CTX='${env}';
    window.__CATEGORIES__ = [
      <c:forEach var="cat" items="${categories}" varStatus="s">{
        id:${cat.id},
        name:"${fn:escapeXml(cat.name)}",
        key:"${fn:replace(fn:toLowerCase(fn:trim(cat.name)), ' ', '-')}"
      }<c:if test="${!s.last}">,</c:if></c:forEach>
    ];
  </script>
  <script src="${env}/js/product-list/product-list.js" defer></script>
</body>
</html>
