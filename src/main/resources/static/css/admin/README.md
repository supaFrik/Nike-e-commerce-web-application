# admin/ – Back Office Styling

Admin styles focus on product management UIs, data grids, editing panels, and form tooling. They isolate from customer styling through scoping and variable prefixes.

## Subfolders
| Folder | Purpose |
|--------|---------|
| `common/` | Shared admin variable aliases & foundational admin utilities |
| `add-product/` | Styles specific to the Add / Edit product creation flow |
| `product-list/` | Grid / list layout for product browsing & inline editing |

## Design Principles
- Functional clarity over marketing polish
- High density layout with consistent spacing rhythm (`--ap-gap`)
- Reduced animation to keep interactions snappy
- Explicit affordances (focus rings, hover states, selected row highlighting)

## Variable Strategy
`common/variables.css` defines the `--ap-*` names mapping to global tokens. This lets admin UI adjust independently (e.g., theming or contrast tweaks) without touching global root tokens.

## Recommended Import Order

```html

<link rel="stylesheet" href="/static/css/common/base.css">      <!-- global tokens -->
<link rel="stylesheet" href="/static/css/admin/common/variables.css"> <!-- admin aliases -->
<link rel="stylesheet" href="/static/css/admin/product-list/list-product.css"> <!-- page module -->
<link rel="stylesheet" href="/static/css/admin/add-product/add-product.css">   <!-- form module -->
```
Only load what a given admin page requires.

## Extending Admin Styles
1. Define new alias variables in `common/variables.css` (optional) if introducing a new semantic value.
2. Keep component styles BEM-like or use clear functional class names: `.ap-toolbar`, `.ap-card`, `.ap-product-row`.
3. Avoid deep descendant selectors; prefer single-class selectors for reusability.
4. Group responsive shifts near the bottom of the file with comments.

## Performance & Maintainability
- No heavy theming engine required; rely on cascading variables
- Consider future extraction into a CSS Module build for tree-shaking
- Keep file size modest (<15KB uncompressed per module ideally)

See each child folder README for component-level details.

Last updated: 2025-10-14

