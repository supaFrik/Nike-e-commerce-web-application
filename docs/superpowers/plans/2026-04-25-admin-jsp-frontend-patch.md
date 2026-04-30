# Admin JSP Frontend Patch Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate the LikeKey admin frontend into the Nike project as JSP skeleton views and organized admin static assets, without implementing backend data binding.

**Architecture:** Build a shared administrator JSP shell under `WEB-INF/views/administrator/layout`, move the LikeKey CSS/JS/image assets into the project’s existing admin static roots, and convert each HTML page into a JSP skeleton that references `${env}` assets. Keep optional Java changes limited to plain page routing in `AdminPageController` so the new views are reachable without changing business logic.

**Tech Stack:** Spring Boot WAR, JSP/JSTL, static CSS, vanilla JavaScript

---

## File Structure

**Create or replace view files**
- `src/main/webapp/WEB-INF/views/administrator/dashboard.jsp`
- `src/main/webapp/WEB-INF/views/administrator/product/list.jsp`
- `src/main/webapp/WEB-INF/views/administrator/product/add.jsp`
- `src/main/webapp/WEB-INF/views/administrator/product/edit.jsp`
- `src/main/webapp/WEB-INF/views/administrator/category/list.jsp`
- `src/main/webapp/WEB-INF/views/administrator/category/add.jsp`
- `src/main/webapp/WEB-INF/views/administrator/category/edit.jsp`
- `src/main/webapp/WEB-INF/views/administrator/order/list.jsp`
- `src/main/webapp/WEB-INF/views/administrator/layout/css.jsp`
- `src/main/webapp/WEB-INF/views/administrator/layout/js.jsp`
- `src/main/webapp/WEB-INF/views/administrator/layout/sidebar.jsp`

**Create or replace admin assets**
- `src/main/resources/static/css/admin/admin.css`
- `src/main/resources/static/js/admin/shell.js`
- `src/main/resources/static/js/admin/data/admin-data.js`
- `src/main/resources/static/js/admin/pages/dashboard.js`
- `src/main/resources/static/js/admin/pages/product-list.js`
- `src/main/resources/static/js/admin/pages/product-form.js`
- `src/main/resources/static/js/admin/pages/categories.js`
- `src/main/resources/static/js/admin/pages/orders.js`
- `src/main/resources/static/images/admin/**`

**Modify only if needed for plain page routing**
- `src/main/java/vn/demo/nike/features/admin/api/AdminPageController.java`

### Task 1: Audit current admin view and asset collisions

**Files:**
- Inspect: `src/main/webapp/WEB-INF/views/administrator/**`
- Inspect: `src/main/resources/static/css/admin/**`
- Inspect: `src/main/resources/static/js/admin/**`
- Inspect: `src/main/resources/static/images/**`

- [ ] **Step 1: Inspect current admin files that may be overwritten**

```powershell
Get-ChildItem 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator' -Recurse -File | Select-Object FullName
Get-ChildItem 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\css\admin' -Recurse -File | Select-Object FullName
Get-ChildItem 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\js\admin' -Recurse -File | Select-Object FullName
```

Expected: a concrete inventory of the existing administrator JSP and static admin files.

- [ ] **Step 2: Compare against the LikeKey source files that will be migrated**

```powershell
Get-ChildItem 'C:\Users\aDMIN\Documents\Admin Dashboard LikeKey' -Recurse -File | Select-Object FullName
```

Expected: a source inventory showing the exact HTML, CSS, JS, and image files to migrate.

- [ ] **Step 3: Confirm the replacement strategy before editing**

```text
Rule: replace only files tied to the new JSP/admin shell, do not revert unrelated user changes, and do not couple the new JSPs to legacy admin JS that expects different DOM structure.
```

- [ ] **Step 4: Commit the audit checkpoint**

```bash
git status --short
```

Expected: understand the dirty state before patching. No commit is required if unrelated work exists.

### Task 2: Build shared administrator layout includes

**Files:**
- Create: `src/main/webapp/WEB-INF/views/administrator/layout/css.jsp`
- Create: `src/main/webapp/WEB-INF/views/administrator/layout/js.jsp`
- Create: `src/main/webapp/WEB-INF/views/administrator/layout/sidebar.jsp`
- Reference: `src/main/webapp/WEB-INF/views/common/variables.jsp`

- [ ] **Step 1: Write the shared CSS include JSP**

```jsp
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<link rel="icon" href="${env}/images/admin/nike-favicon.ico">
<link rel="stylesheet" href="${env}/css/admin/admin.css">
```

- [ ] **Step 2: Write the shared JS include JSP**

```jsp
<script src="${env}/js/admin/data/admin-data.js"></script>
<script src="${env}/js/admin/shell.js"></script>
```

- [ ] **Step 3: Write the shared sidebar shell JSP**

```jsp
<aside class="admin-sidebar">
    <a class="brand-lockup" href="${env}/admin">
        <img src="${env}/images/admin/nike-icon-webpage.png" alt="Nike admin logo">
        <div>
            <div class="brand-eyebrow">Likey x Nike</div>
            <div class="brand-title">Admin Suite</div>
            <div class="brand-subtitle">JSP frontend skeleton</div>
        </div>
    </a>
    <div class="nav-section">
        <div class="nav-label">Main</div>
        <a class="nav-link" data-nav="dashboard" href="${env}/admin"><span><strong>Dashboard</strong><small>Command center and launch overview</small></span><span>01</span></a>
        <a class="nav-link" data-nav="product-list" href="${env}/admin/product/list"><span><strong>Product inventory</strong><small>Browse and inspect catalog stock</small></span><span>02</span></a>
        <a class="nav-link" data-nav="product-add" href="${env}/admin/product/add"><span><strong>Add product</strong><small>Create launch-ready product shells</small></span><span>03</span></a>
        <a class="nav-link" data-nav="category-list" href="${env}/admin/category/list"><span><strong>Categories</strong><small>Control catalog taxonomy</small></span><span>04</span></a>
        <a class="nav-link" data-nav="orders" href="${env}/admin/order/list"><span><strong>Order management</strong><small>Track fulfillment queues</small></span><span>05</span></a>
    </div>
</aside>
```

- [ ] **Step 4: Run a search to confirm new include paths are consistent**

```powershell
rg -n 'administrator/layout/(css|js|sidebar)\.jsp|/js/admin/|/css/admin/|/images/admin/' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator'
```

Expected: every administrator JSP references the shared include files and `${env}` asset paths.

- [ ] **Step 5: Commit the layout checkpoint**

```bash
git add src/main/webapp/WEB-INF/views/administrator/layout
git commit -m "feat: add admin jsp layout includes"
```

### Task 3: Migrate the LikeKey static admin assets into the target admin roots

**Files:**
- Create or replace: `src/main/resources/static/css/admin/admin.css`
- Create or replace: `src/main/resources/static/js/admin/shell.js`
- Create or replace: `src/main/resources/static/js/admin/data/admin-data.js`
- Create or replace: `src/main/resources/static/js/admin/pages/dashboard.js`
- Create or replace: `src/main/resources/static/js/admin/pages/product-list.js`
- Create or replace: `src/main/resources/static/js/admin/pages/product-form.js`
- Create or replace: `src/main/resources/static/js/admin/pages/categories.js`
- Create or replace: `src/main/resources/static/js/admin/pages/orders.js`
- Create: `src/main/resources/static/images/admin/**`

- [ ] **Step 1: Write the failing asset path check**

```powershell
rg -n 'assets/(css|js|images)|\.\./\.\./assets|\.\./assets|index\.html|list\.html|add\.html|edit\.html' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\js\admin' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\css\admin' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator'
```

Expected: FAIL before migration because the source markup and scripts still use HTML-relative paths.

- [ ] **Step 2: Copy and adapt the LikeKey CSS and JS assets into the target structure**

```text
Move the source suite into the target roots, then rewrite references so images use `${env}/images/admin/...` from JSP and JS uses route paths like `/admin/product/list` instead of `list.html`.
```

- [ ] **Step 3: Copy the required images under `static/images/admin`**

```text
Include the favicon, logo/icon assets, feature image, and product images used by the migrated views and mock scripts.
```

- [ ] **Step 4: Re-run the asset path check to verify cleanup**

```powershell
rg -n 'assets/(css|js|images)|\.\./\.\./assets|\.\./assets|index\.html|list\.html|add\.html|edit\.html' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\js\admin' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\css\admin' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator'
```

Expected: PASS with no matches.

- [ ] **Step 5: Commit the asset migration checkpoint**

```bash
git add src/main/resources/static/css/admin src/main/resources/static/js/admin src/main/resources/static/images/admin
git commit -m "feat: migrate admin frontend static assets"
```

### Task 4: Convert each LikeKey admin page into a JSP skeleton view

**Files:**
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/dashboard.jsp`
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/product/list.jsp`
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/product/add.jsp`
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/product/edit.jsp`
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/category/list.jsp`
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/category/add.jsp`
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/category/edit.jsp`
- Create or replace: `src/main/webapp/WEB-INF/views/administrator/order/list.jsp`

- [ ] **Step 1: Write the failing JSP include/path check**

```powershell
rg -n '<%@ include file="/WEB-INF/views/common/variables.jsp" %>|<jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />|<jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />|<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator'
```

Expected: FAIL before the new JSP pages are written.

- [ ] **Step 2: Convert the dashboard page to JSP**

```jsp
<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />
</head>
<body data-page="dashboard">
<div class="admin-shell">
    <jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />
    <main class="admin-main">
        <!-- dashboard content -->
    </main>
</div>
<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />
<script src="${env}/js/admin/pages/dashboard.js"></script>
</body>
</html>
```

- [ ] **Step 3: Convert the product, category, and order pages to JSP with the same shell**

```text
For each page:
- include `common/variables.jsp`
- include `administrator/layout/css.jsp`
- wrap content in `.admin-shell`
- include `administrator/layout/sidebar.jsp`
- end with `administrator/layout/js.jsp`
- load only the page-specific script needed for that page
```

- [ ] **Step 4: Keep skeleton placeholders explicit instead of fake model bindings**

```jsp
<c:choose>
    <c:when test="${false}">
        <!-- future server-rendered rows go here -->
    </c:when>
    <c:otherwise>
        <tr>
            <td colspan="7">Data binding will be connected in a later backend pass.</td>
        </tr>
    </c:otherwise>
</c:choose>
```

- [ ] **Step 5: Re-run the JSP include/path check**

```powershell
rg -n '<%@ include file="/WEB-INF/views/common/variables.jsp" %>|<jsp:include page="/WEB-INF/views/administrator/layout/css.jsp" />|<jsp:include page="/WEB-INF/views/administrator/layout/sidebar.jsp" />|<jsp:include page="/WEB-INF/views/administrator/layout/js.jsp" />' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator'
```

Expected: PASS with matches in every migrated administrator JSP.

- [ ] **Step 6: Commit the view migration checkpoint**

```bash
git add src/main/webapp/WEB-INF/views/administrator
git commit -m "feat: migrate admin pages to jsp skeletons"
```

### Task 5: Add minimal page routing only if required to reach the JSP pages

**Files:**
- Modify: `src/main/java/vn/demo/nike/features/admin/api/AdminPageController.java`

- [ ] **Step 1: Write the failing routing check**

```powershell
Get-Content 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\java\vn\demo\nike\features\admin\api\AdminPageController.java'
```

Expected: the controller is empty and does not currently map the new administrator JSP pages.

- [ ] **Step 2: Add plain page-return mappings without business logic**

```java
@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String dashboard() {
        return "administrator/dashboard";
    }

    @GetMapping("/admin/product/list")
    public String productList() {
        return "administrator/product/list";
    }

    @GetMapping("/admin/product/add")
    public String productAdd() {
        return "administrator/product/add";
    }

    @GetMapping("/admin/product/edit")
    public String productEdit() {
        return "administrator/product/edit";
    }

    @GetMapping("/admin/category/list")
    public String categoryList() {
        return "administrator/category/list";
    }

    @GetMapping("/admin/category/add")
    public String categoryAdd() {
        return "administrator/category/add";
    }

    @GetMapping("/admin/category/edit")
    public String categoryEdit() {
        return "administrator/category/edit";
    }

    @GetMapping("/admin/order/list")
    public String orderList() {
        return "administrator/order/list";
    }
}
```

- [ ] **Step 3: Compile-check the controller mappings**

```bash
mvn -q -DskipTests compile
```

Expected: PASS with the new page mappings compiling cleanly.

- [ ] **Step 4: Commit the routing checkpoint**

```bash
git add src/main/java/vn/demo/nike/features/admin/api/AdminPageController.java
git commit -m "feat: add admin page routes"
```

### Task 6: Verify the migrated JSP frontend end to end

**Files:**
- Verify: `src/main/webapp/WEB-INF/views/administrator/**`
- Verify: `src/main/resources/static/css/admin/**`
- Verify: `src/main/resources/static/js/admin/**`
- Verify: `src/main/resources/static/images/admin/**`

- [ ] **Step 1: Run a full reference scan for legacy HTML links and source-relative assets**

```powershell
rg -n 'index\.html|list\.html|add\.html|edit\.html|assets/images|assets/js|assets/css' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\js\admin' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\resources\static\css\admin'
```

Expected: PASS with no matches.

- [ ] **Step 2: Run a JSP/static path sanity scan for the migrated administrator pages**

```powershell
rg -n '\$\{env\}/(css/admin|js/admin|images/admin)|administrator/layout/(css|js|sidebar)\.jsp' 'C:\Users\aDMIN\Documents\Nike Ecommerce Web Application\src\main\webapp\WEB-INF\views\administrator'
```

Expected: PASS with correct `${env}` references and layout includes.

- [ ] **Step 3: Run the project compile check**

```bash
mvn clean compile
```

Expected: PASS.

- [ ] **Step 4: Inspect final git diff**

```bash
git status --short
git diff -- src/main/webapp/WEB-INF/views/administrator src/main/resources/static/css/admin src/main/resources/static/js/admin src/main/resources/static/images/admin src/main/java/vn/demo/nike/features/admin/api/AdminPageController.java
```

Expected: only the intended admin JSP/frontend files and optional page routing changes appear.

- [ ] **Step 5: Commit the final patch**

```bash
git add src/main/webapp/WEB-INF/views/administrator src/main/resources/static/css/admin src/main/resources/static/js/admin src/main/resources/static/images/admin src/main/java/vn/demo/nike/features/admin/api/AdminPageController.java
git commit -m "feat: patch admin frontend jsp skeleton"
```

## Self-Review

- Spec coverage: this plan covers shared layout extraction, asset migration, JSP conversion, optional minimal routing, and verification.
- Placeholder scan: no `TBD` or `TODO` implementation holes remain; each task names exact files and commands.
- Type consistency: all route returns use the `administrator/...` view prefix, and all JSPs use `${env}` paths through the shared layout includes.
