# admin/common – Admin Variable Aliases & Shared Foundations

## Files
| File | Purpose |
|------|---------|
| `variables.css` | Defines `--ap-*` scoped variables referencing global tokens for admin UI theming |
| `base.css` | (If present) Additional base resets or admin-specific structural rules |
| `components.css` | (If present) Reusable admin components (cards, toolbars, badges) |

Only `variables.css` detected in current snapshot.

## Variable Highlights (variables.css)
- Color aliasing: `--ap-border`, `--ap-bg`, `--ap-text` map to global token fallbacks
- Radii scale: `--ap-radius-s`, `--ap-radius-m`
- Spacing / gap: `--ap-gap`
- Transition baseline: `--ap-transition`
- Root font-size anchored at 16px for predictable rem calculations

## Usage
Import before any admin component stylesheet:
```css
@import url('../common/variables.css');
```
(or via `<link>` tag order in HTML templates.)

## Adding New Admin Variables
1. Reference a global token instead of hardcoding values unless value is admin-unique.
2. Keep naming descriptive but concise: `--ap-accent`, `--ap-row-hover`, etc.
3. Document new additions here.

## Theming Strategy
A future dark mode or high-contrast admin theme can override only `variables.css` inside a `.theme-dark` or `.hc-mode` root scope:
```css
.theme-dark { --ap-bg: #1F1F21; --ap-text: #FFFFFF; }
```

Last updated: 2025-10-14

