# JavaScript Front-End Architecture

This folder contains all JavaScript powering customer UI interactions and admin product tooling. Scripts are intentionally modular; each file owns a focused responsibility.

## Folder Structure
```
js/
├── add-product/         # Modular admin product creation system (has its own README)
├── product-list/        # Server-driven admin product list (pagination, edit panel)
├── add-to-cart.js       # Customer PDP add-to-cart workflow
├── auth.js              # Sign in / sign up form toggling & client validation
├── carousel.js          # Carousels (legacy + emerging CarouselManager abstraction)
├── cart.js              # (Placeholder) future cart interactions (currently empty)
├── checkout-shipping.js # Checkout shipping method selection + total recalculation
├── checkout-validation.js # Checkout form client validation + inline errors
├── color-options.js     # PDP dynamic size loading when color changes (server endpoint dependent)
├── mobile-nav.js        # Mobile menu / overlay / search interactions
├── product.js           # Admin product list (static sample data version)
├── sort-btn.js          # Customer product list sort dropdown (static variant)
├── thumbnail-scope.js   # PDP thumbnail gallery accessibility + interaction
├── update-button.js     # Cart quantity & removal updates with optimistic UI
├── url-utils.js         # PDP thumbnail reconstruction for selected color
```

## Global Conventions
- All scripts wrapped in IIFE or DOMContentLoaded handlers to avoid polluting global scope unless exporting intentionally.
- Some modules expose globals (e.g., `window.toggleMobileMenu`, `window.ThumbnailGallery`) for template integration.
- Use feature detection & graceful early returns (`if(!el) return`) to prevent console noise on pages that don’t include certain markup.

## Module Summaries
### add-to-cart.js
Handles PDP: size & color selection validation, toast notifications, secure POST to `/api/cart/add/{productId}` with CSRF header injection and robust response content-type handling.

### auth.js
Toggles between sign-in / sign-up forms; performs strong password & duplicate username/email checks via `/api/auth/check-duplicate`. Provides accessible error messaging & prevents submission when invalid.

### carousel.js
Initializes three kinds of carousels (sport, shop icons, running) + a `CarouselManager` class scaffolding for a generalized approach. Adds button state management, keyboard & touch support.

### cart.js
(Empty placeholder) Reserved for future cart overlay or unified event bus logic. Current cart update / remove logic lives in `update-button.js`.

### checkout-shipping.js
Radio group enhancement for shipping methods: updates shipping cost, total recalculation (`subtotal + shipping - discount`), synchronizes hidden input, manages selected card styling.

### checkout-validation.js
Client-side validation for checkout form: required fields, email format, phone pattern (9–12 digits), shipping method selection, terms agreement. Adds inline error nodes and scrolls first invalid field into view.

### color-options.js
On color button click, fetches available sizes for that color via `/api/products/{id}/sizes?color={color}` and repopulates size buttons. (Relies on server templating for `product.id` variable – currently contains a template literal placeholder.)

### mobile-nav.js
Controls mobile navigation sidebar open/close, body scroll locking, outside click + Escape handling, and mobile search stub. Exposes global toggles & dispatches custom events.

### product.js
Admin product list (static client-only prototype): sample data generation, filtering, sorting (name, price, stock), pagination with ellipsis logic, selection & edit side panel, accessible keyboard interactions.

### product-list/product-list.js
Server-integrated admin product list: fetches products with pagination, sorting, search, and category filtering. Provides edit panel with PUT/DELETE operations using CSRF headers (if available). Dynamically builds category tabs from `window.__CATEGORIES__`.

### sort-btn.js
Customer-facing product list sort dropdown: expand/collapse animation, hidden form submission with selected sort value, filter toggle (hiding sidebar). Contains deprecated commented legacy approach.

### thumbnail-scope.js
Accessible product image thumbnail gallery: keyboard arrow navigation, Enter/Space activation, updates main image, maintains `aria-selected` & `.active` state.

### update-button.js
Handles cart quantity changes (PUT) and item removal (DELETE) with optimistic UI feedback, concurrency guarding using a Set, summary totals recalculation, empty cart state injection.

### url-utils.js
Rebuilds PDP thumbnail list when switching product color. Constructs image paths using product + color folder names and a base image prefix. Hides missing images gracefully and ensures active thumbnail always visible.

## Dependency Graph (Simplified)
```
update-button.js  ──> server /api/cart endpoints
add-to-cart.js     ──> /api/cart/add + CSRF meta
checkout-*         ──> form markup IDs & shipping radio dataset.cost
auth.js            ──> /api/auth/check-duplicate
product-list.js    ──> /admin/api/products (GET/PUT/DELETE)
color-options.js   ──> /api/products/{id}/sizes
```
Independent modules do not import each other; they coordinate through DOM + occasionally global functions.

## Patterns & Best Practices
- Defensive querying: Always null-check DOM lookups
- Graceful degradation for missing API responses (add logging only in dev)
- Use `fetch` with structured error handling including content-type validation
- Keep per-module state local; only expose minimal surface to `window`

## Adding a New Module
1. Name it according to feature: `wishlist.js`, `address-book.js`
2. Wrap in IIFE or DOMContentLoaded guard
3. Keep global exports optional & namespaced (e.g. `window.Wishlist = { ... }`)
4. Document it here (Module Summaries section)
5. Wire into JSP/HTML only on pages that require it

## Future Enhancements
- Transition to ES Modules (native import syntax) with a build step (Vite / Rollup) for tree-shaking
- TypeScript migration for safer refactors
- Central event bus (custom events or tiny pub/sub) to remove ad-hoc globals
- Error boundary overlay for admin editing actions
- Add unit tests (Vitest / Jest) for logic-heavy modules (pagination, validation)

Last updated: 2025-10-14

