# js/product-list – Server-Integrated Admin Product List Script

## File
| File | Purpose |
|------|---------|
| `product-list.js` | Fetches and renders paginated product data from server endpoints with sorting, search, category filtering, and inline editing support. |

## High-Level Flow
1. Initialize state (paging, sort, category, search)
2. Cache DOM references (grid, pagination, templates, edit panel)
3. Inject dynamic categories from `window.__CATEGORIES__`
4. Build query string & `fetch` data from `/admin/api/products`
5. Render product cards from HTML `<template>` with fallback image handling
6. Provide editor panel for updating / deleting products (PUT / DELETE)
7. Re-fetch or locally adjust list on mutations

## Key Selectors / Elements
| Selector | Role |
|----------|------|
| `#productGrid` | Container for product cards |
| `#productGridEmpty` | Empty state / error messaging node |
| `#paginationList` | Pagination pill buttons |
| `#paginationSummary` | Summary text (count + page info) |
| `#search` | Live search input (debounced) |
| `#sortToggle` / `#sortMenu` | Sort dropdown toggler + menu container |
| `.tab` inside `#categoryTabs` | Category filter controls |
| `#productCardTemplate` | `<template>` element for cloning card DOM |
| `.edit-panel` | Side drawer edit panel |
| `#editForm` | Edit form (name, description, price, stock, category) |
| `#editCategorySelect` | Category select inside edit form |
| `#addProductBtn` | Button to navigate to add product page |

## State Object
```js
state = {
  items: [],
  page: 1,
  pageSize: 20,
  totalItems: 0,
  totalPages: 1,
  sort: 'name',
  category: 'all',
  search: ''
}
```

## Networking
Endpoint: `GET /admin/api/products?page=&size=&sort=&keyword=&categoryId=` (conditional params).
CSRF handling for PUT/DELETE operations uses meta tags: `meta[_csrf]` + `meta[_csrf_header]` (if present).

## Editing Workflow
- Card click opens panel (`openEditor()`)
- Form submit serializes changed fields → `PUT /admin/api/products/{id}`
- Delete (discard) button asks confirmation → `DELETE /admin/api/products/{id}` → re-renders list

## Accessibility Features
- Product cards are focusable (`tabIndex=0`)
- Pagination supports `aria-current="page"` on active pill
- Sort menu updates `aria-expanded` & option `aria-checked`

## Image Handling
If `p.imageUrl` is missing or fails to load, code adds `.no-image` class or removes `<img>` to trigger a CSS fallback (placeholder styling recommended in CSS).

## Helper Utilities
- `debounce(fn, ms)` for search input
- `formatCurrency(v)` using Intl API
- Category resolution helper `categoryByKey`

## Extending
- Add bulk selection: extend state with `selectedIds: Set`
- Add status filtering: filter before render using `p.productStatus`
- Add infinite scroll: replace pagination pills with sentinel observer

## Error Handling
- Network or parsing errors trigger console logging + empty state message
- AbortController cancels prior fetch when search / filter rapidly changes

## Performance Tips
- DocumentFragment batch append reduces layout thrashing
- Keep card DOM lightweight (defer heavy metrics until hover or edit)

Last updated: 2025-10-14

