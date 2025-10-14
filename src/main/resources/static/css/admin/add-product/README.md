# admin/add-product – Product Creation & Editing Styles

## File
| File | Purpose |
|------|---------|
| `add-product.css` | Layout + form styling for the admin Add Product workflow (multi-section form, gallery previews, variant controls) |

## Key UI Areas Styled
- Container layout (flex / grid structure of info vs media panels)
- Form field groups (labels, inputs, validation states)
- Variant management (color & size matrix / dynamic list styling)
- Image upload area (drop zone, thumbnails, default highlight)
- Action toolbar (submit, reset, secondary actions)
- Toast / inline feedback alignment (paired with JS toast module if used)

## Integration with JavaScript Modules
Pairs conceptually with scripts found under `js/add-product/` (state, images, colors, sizes, dropdown, toast, form-handler). CSS assumes dynamic classes:
- `.is-invalid`, `.is-dragover`, `.image-default`, `.variant-active`, `.toast-*`
Add states rather than inline styles from JS where possible.

## Recommended Markup Structure (Abstracted)
```html
<section class="ap-add">
  <div class="ap-add__main">
    <form class="ap-add__form" id="addProductForm"> ... </form>
  </div>
  <aside class="ap-add__media"> ... image manager ... </aside>
</section>
```
(Adjust to match existing class names if already implemented.)

## Accessibility Notes
- Ensure all inputs have `<label for>` or `aria-label`
- Focus outline preserved; avoid `outline: none` unless replaced by accessible ring
- Drag & drop zone announces state via `aria-live` (future enhancement)

## Extending
Add new feature sections as distinct blocks: `.ap-add__shipping`, `.ap-add__seo` etc. Keep each visually separated via spacing scale variables.

Last updated: 2025-10-14

