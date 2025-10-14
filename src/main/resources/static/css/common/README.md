# common/ – Global Design System Layer

This folder centralizes global tokens and primitive structural rules shared across both customer and admin experiences.

## Files
| File | Purpose |
|------|---------|
| `base.css` | Master variable map (colors, spacing, typography, shadows, breakpoints, motion, form primitives) + core font-face declarations + typography utilities |
| `header.css` | (If present) Global header layout / navigation baseline (check existence before use) |
| `footer.css` | (If present) Global footer layout / typography alignment |
| `main.css` | Global resets / base element normalizations and site wide layout wrappers |

(Only `base.css` was detected in the current scan; add others as needed.)

## Highlights: `base.css`
- Full semantic color scale per hue (50–900) for red, orange, yellow, green, blue, teal, purple, pink, grey
- Dual theme-friendly naming: `--color-text-*`, `--color-bg-*`, `--color-border-*`
- Component sizing scale: spacing (`--size-spacing-*`), radii, icon sizes, border widths
- Typography scale: `--font-size-xxs` → `--font-size-xxxxxl`, weight tokens, hero font separation
- Motion: standardized timing + easing (`--transition-*`)
- Breakpoints: mobile-first tokens (`--breakpoint-xs` ... `--breakpoint-xl`)
- Focus styling: `--form-shadow-focus` ring system
- Utility classes: `.font-heading`, `.font-helvetica`, `.body-alt-scope` scope swap

## Usage
Import `base.css` first in every rendered layout or bundle:
```html
<link rel="stylesheet" href="/static/css/common/base.css">
```
Then layer feature or page-specific styles below.

## Token Extension Guidelines
1. Do not overwrite base tokens inside component CSS; derive new aliases if needed.
2. Use prefixing for scope-specific additions (e.g. `--pl-` for product list, `--ap-` for admin product list) to prevent collisions.
3. When adding a new semantic purpose (e.g. info, warning subtle background) create both normal and inverse tokens.

## Accessibility
- Contrast: Base palette curated to support AA text contrast against white / dark backgrounds.
- Motion: Provide reduced motion variant (future enhancement) by checking `@media (prefers-reduced-motion)`.

## Future Improvements
- Convert tokens to a design token pipeline (JSON → multiple output formats)
- Add dark mode palette parity (suffix `-dark` or dynamic root class swap)
- Provide CSS logical property utilities for bidirectional layout support

Last updated: 2025-10-14

