# STATUS DOT CONVENTION (Optional Reference)

If implementing visual status dots in `list-product.css`, consider mapping:

| data-status | Color Token | Meaning |
|-------------|-------------|---------|
| ACTIVE | --color-success | Visible & purchasable |
| INACTIVE | --color-grey-400 | Hidden / disabled |
| FEW_LEFT | --color-warning | Low inventory threshold |
| OUT_OF_STOCK | --color-critical | No stock available |
| ARCHIVED | --color-grey-300 | Soft-deleted / archived |
| UNKNOWN | --color-grey-200 | Fallback state |

Example CSS snippet:
```css
.status-dot[data-status] { width:10px; height:10px; border-radius:50%; display:inline-block; }
.status-dot[data-status="ACTIVE"] { background: var(--color-success); }
.status-dot[data-status="INACTIVE"] { background: var(--color-grey-400); }
.status-dot[data-status="FEW_LEFT"] { background: var(--color-warning); }
.status-dot[data-status="OUT_OF_STOCK"] { background: var(--color-critical); }
.status-dot[data-status="ARCHIVED"] { background: var(--color-grey-300); }
.status-dot[data-status="UNKNOWN"] { background: var(--color-grey-200); }
```

Update mapping as backend enum evolves.
# Global CSS Architecture

This directory contains all stylesheet assets for the Nike E‑Commerce Web Application. The architecture is modular, layered, and domain-scoped to keep styles predictable, scalable, and performance friendly.

## Layering Overview (Import / Cascade Order)
1. common/ (design tokens + global resets)
2. admin/ (back‑office UI scopes)
3. customer/ (public storefront scopes)
4. Page / component overrides (loaded as needed)

| Layer | Purpose | Core Files |
|-------|---------|------------|
| Design Tokens | Color system, spacing, typography, radii, elevations, transitions | `common/base.css` |
| Admin Base | Admin-only variable aliases & structural utilities | `admin/common/variables.css` |
| Customer Modules | Feature / page specific styling | e.g. `customer/product-list/*.css` |
| Responsive Overrides | Consolidated breakpoint logic per feature set | `*-responsive.css` files |

## Naming Conventions
- Files use lowercase kebab-case.
- Page group master file (if present) ends with the group name (e.g. `product-list.css`).
- Shared responsive overrides suffixed with `-responsive`.
- Scope-specific variable prefixes (e.g. `--ap-` for admin product list widgets).

## CSS Variable Strategy
Global variables are declared in `common/base.css` under `:root`. Feature or scope level variables extend these (never redefine globals unless intentional). Admin-specific variable aliases exist under `admin/common/variables.css`.

## Browser & A11y Targets
- Modern evergreen browsers (Chrome, Edge, Safari, Firefox)
- Progressive enhancement for layout (Grid + Flex fallbacks where needed)
- Minimum touch target sizing: 44px (actionable UI)
- Accessible focus (custom ring via `--form-shadow-focus` envelope) and ARIA states

## Folder Map
```
css/
├── README.md                # This file
├── common/                  # Global tokens & structural shared styling
├── admin/                   # Back‑office (product management, dashboards)
└── customer/                # Public storefront experiences
```

See each subfolder README for deep dives.

## Adding New Styles
1. Confirm if a token already exists (add new only when semantically justified)
2. Choose correct domain (admin vs customer vs truly shared → common)
3. Keep selectors shallow; prefer utility / component classes over nested cascades
4. Co-locate responsive adjustments in the page group `*-responsive.css` file
5. Run visual regression / lighthouse (performance + CLS) if large UI change

## Performance Guidelines
- Avoid large unused bundles: import only page-needed bundles in templates
- Prefer modern CSS features (no heavy JS polyfills)
- Strive for: CLS < 0.1, render-blocking CSS minimal & critical path inline (future enhancement)

## Future Enhancements
- Introduce a build step (PostCSS) for pruning and autoprefixing
- Create a tokens JSON source-of-truth → generated CSS vars
- Dark mode token inversion layer
- Component documentation site (Storybook or similar)

---
Last updated: 2025-10-14

