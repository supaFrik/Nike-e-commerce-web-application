# admin/product-list – Product Grid & Editor Styles

## File
| File | Purpose |
|------|---------|
| `list-product.css` | Visual design for admin product browsing grid, pagination pills, selection states, inline edit panel, sort/filter UI |

## Core Styled Components
- Product grid cards / rows (image placeholder, title, meta stats, status dot)
- Pagination (page pills, ellipsis strategy supported by JS counterpart or simplified variant)
- Sort dropdown / menu (ARIA states: `aria-expanded`, `aria-checked`)
- Category filter tabs
- Side edit panel (flyout) with tabbed content sections
- Form controls inside edit panel
- Status indicators (e.g. `.status-dot[data-status="ACTIVE"]` styling suggestion)

## Interactivity Expectations
JavaScript (`js/product.js` or server-driven `js/product-list/product-list.js`) toggles classes:
- `.selected` (active card)
- `.open` (edit panel visibility)
- `.active` (current tab / sort option)
- `.anim-enter` / `.anim-in` (entry animation pair)

Provide transitions that are performant (opacity/transform only).

## Suggested BEM / Class Patterns
While exact class names may differ, keep separation between structure and state:
- Block: `.ap-pl-grid`, `.ap-pl-card`, `.ap-pl-pagination`
- Element: `.ap-pl-card__thumb`, `.ap-pl-card__meta`
- Modifier / state: `.ap-pl-card--selected`, `.is-loading`

## Accessibility
- Use focus outlines for keyboard navigation across cards
- Maintain `aria-current="page"` for active pagination pill (JS already sets this)
- Provide hidden text or `title` for status dot color meanings

## Performance Tips
- Avoid large box-shadows for dozens of cards (expensive paint)
- Use `contain: content;` on cards if heavy internal changes occur (optional)
- Defer non-critical animations until after initial paint

Last updated: 2025-10-14

