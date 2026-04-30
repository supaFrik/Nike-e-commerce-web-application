# 2026-04-25 Admin JSP Frontend Patch Design

**Topic:** Patch series admin from `Admin Dashboard LikeKey` into `Nike Ecommerce Web Application` as JSP frontend skeletons.

## Goal

Migrate the LikeKey admin frontend into the main Nike project as JSP views and static assets only.

This patch must:
- target the main project at `C:\Users\aDMIN\Documents\Nike Ecommerce Web Application`
- stay strictly in frontend scope
- convert HTML pages into JSP skeleton views under `WEB-INF/views/administrator`
- keep CSS, JS, and images organized under the existing `static/css/admin`, `static/js/admin`, and `static/images/admin` roots
- avoid creating a dedicated `likey` namespace folder
- accept that pages will not render real backend data yet

## Scope

Included:
- JSP view structure for admin dashboard, products, categories, and orders
- shared JSP layout includes for common CSS, JS, and sidebar shell
- static asset migration from the LikeKey source project into the main project
- path conversion from relative HTML asset URLs to `${env}`-based JSP asset references
- skeleton placeholders for future JSTL or model binding

Excluded:
- Java backend logic
- service, repository, domain, DTO, or API changes
- real data binding and controller population
- authentication, authorization, or business workflow changes

## Source Pages

Source frontend pages to migrate:
- `index.html`
- `admin/product/list.html`
- `admin/product/add.html`
- `admin/product/edit.html`
- `admin/category/list.html`
- `admin/category/add.html`
- `admin/category/edit.html`
- `admin/orders/list.html`

## Target View Structure

Views will be created or replaced under:
- `src/main/webapp/WEB-INF/views/administrator/dashboard.jsp`
- `src/main/webapp/WEB-INF/views/administrator/product/list.jsp`
- `src/main/webapp/WEB-INF/views/administrator/product/add.jsp`
- `src/main/webapp/WEB-INF/views/administrator/product/edit.jsp`
- `src/main/webapp/WEB-INF/views/administrator/category/list.jsp`
- `src/main/webapp/WEB-INF/views/administrator/category/add.jsp`
- `src/main/webapp/WEB-INF/views/administrator/category/edit.jsp`
- `src/main/webapp/WEB-INF/views/administrator/order/list.jsp`

Shared includes will be added under:
- `src/main/webapp/WEB-INF/views/administrator/layout/css.jsp`
- `src/main/webapp/WEB-INF/views/administrator/layout/js.jsp`
- `src/main/webapp/WEB-INF/views/administrator/layout/sidebar.jsp`

## Static Asset Structure

Assets will stay under the project’s existing admin roots without a `likey` folder.

Planned structure:
- `src/main/resources/static/css/admin/admin.css`
- `src/main/resources/static/js/admin/shell.js`
- `src/main/resources/static/js/admin/data/admin-data.js`
- `src/main/resources/static/js/admin/pages/dashboard.js`
- `src/main/resources/static/js/admin/pages/product-list.js`
- `src/main/resources/static/js/admin/pages/product-form.js`
- `src/main/resources/static/js/admin/pages/categories.js`
- `src/main/resources/static/js/admin/pages/orders.js`
- `src/main/resources/static/images/admin/...`

If any existing admin asset file would be overwritten, the patch should first verify whether the file is unused or whether preserving compatibility requires updating references instead of replacing unrelated code.

## JSP Rendering Strategy

Because backend changes are out of scope, the migrated pages will be JSP skeletons.

Rules:
- use `<%@ include file="/WEB-INF/views/common/variables.jsp" %>`
- use `${env}` for static asset URLs
- keep page markup production-shaped rather than raw HTML dumps
- allow static placeholder cards, tables, and empty states where real model data is not yet available
- leave clear IDs and sections intact so future JS or JSTL binding can be added safely
- do not introduce fake server-side variables that imply backend support already exists

## Layout Design

A shared administrator shell will be extracted so pages do not duplicate the entire sidebar asset setup.

Common layout responsibilities:
- `css.jsp`: admin stylesheet includes and favicon references
- `sidebar.jsp`: shared navigation and shell footer
- `js.jsp`: shared JS asset includes

Per-page responsibilities:
- page title and topbar copy
- page body content specific to dashboard, products, categories, or orders
- page-specific script include when needed

## Routing Assumption

The project already contains `AdminPageController`, but it is currently empty.

This frontend patch will not modify controller logic unless a minimal view-return mapping is strictly necessary to make the JSP pages reachable without changing business behavior. If such mapping is required, it must stay limited to plain page routing only.

## Risk Notes

Key risks:
- existing admin static assets in the main project may partially overlap in names or purpose
- existing admin JSP files may be missing or structured differently from user views
- page JS from the LikeKey source uses mock data and relative paths, so references must be rewritten carefully
- some current admin JS in the main project expects specific DOM IDs; mismatched markup could break those scripts if reused unintentionally

Mitigation:
- keep migrated assets self-consistent
- use only the migrated page scripts for the new JSPs
- avoid coupling new JSPs to unrelated existing admin JS
- verify every JSP asset include path after patching

## Success Criteria

The patch is successful when:
- the target project contains administrator JSP pages matching the LikeKey frontend structure
- the admin frontend is organized cleanly inside the existing admin static folders
- no backend/business logic is changed beyond optional minimal page routing
- JSP pages compile as view templates and reference assets through `${env}` paths
- the result is ready for later model binding without redoing the frontend structure
