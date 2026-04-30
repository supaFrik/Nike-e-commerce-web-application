<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aside class="admin-sidebar">
    <a class="brand-lockup" href="${env}/admin">
        <img src="${env}/images/admin/nike-icon-webpage.png" alt="Nike admin logo">
        <div>
            <div class="brand-title">Admin Nike</div>
        </div>
    </a>
    <div class="nav-section">
        <div class="nav-label">Main</div>
        <a class="nav-link" data-nav="dashboard" href="${env}/admin">
            <span><strong>Dashboard</strong><small>Command center and launch overview</small></span><span>01</span>
        </a>
        <a class="nav-link" data-nav="product-list" href="${env}/admin/product/list">
            <span><strong>Product inventory</strong><small>Browse and inspect catalog stock</small></span><span>02</span>
        </a>
        <a class="nav-link" data-nav="product-add" href="${env}/admin/product/add">
            <span><strong>Add product</strong><small>Create launch-ready product shells</small></span><span>03</span>
        </a>
        <a class="nav-link" data-nav="category-list" href="${env}/admin/category/list">
            <span><strong>Categories</strong><small>Control catalog taxonomy</small></span><span>04</span>
        </a>
        <a class="nav-link" data-nav="orders" href="${env}/admin/order/list">
            <span><strong>Order management</strong><small>Track fulfillment queues</small></span><span>05</span>
        </a>
    </div>
    <div class="nav-section">
        <div class="nav-label">Switch</div>
        <a class="nav-link" data-nav="storefront" href="${env}/">
            <span><strong>User storefront</strong><small>Return to the customer-facing Nike experience</small></span><span>UI</span>
        </a>
    </div>
</aside>
