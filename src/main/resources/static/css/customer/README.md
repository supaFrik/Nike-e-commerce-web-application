# customer/ – Public Storefront Styles

This directory groups page & feature styles for the customer-facing storefront. Each subfolder isolates a functional or page domain to keep bundles lean and maintainable.

## Subfolders
| Folder | Purpose | Has README |
|--------|---------|-----------|
| `auth/` | Sign In / Sign Up experiences (layout, forms, animations) | Yes |
| `landing-page/` | Marketing homepage hero, featured, category carousels | Yes |
| `product-detail/` | Product detail page gallery, info, reviews, recommendations | Yes |
| `product-list/` | Browsable product catalogue grid, filters, banners, stories | Yes |
| `profile/` | Logged-in member profile hub (benefits, favourites, apps) | Yes |
| `cart/` | Cart (bag) page layout and item list styling | No (added below) |
| `checkout/` | Checkout shipping, summary, payment CTA, validation states | No (added below) |
| `order/` | Order detail / purchase confirmation view | No (added below) |

## Shared Styling Conventions
- Uses global tokens from `common/base.css`
- Spacing scale: `--size-spacing-*`
- Typography scale: `--font-size-*` with medium weight emphasis tokens
- Focus indicators preserved for accessibility

## Page Loading Strategy
Include only the subfolder CSS required by the rendered JSP/HTML to minimize unused CSS. Example for checkout page:
```html
<link rel="stylesheet" href="/static/css/common/base.css">
<link rel="stylesheet" href="/static/css/customer/checkout/checkout.css">
```

## Folder Documentation (New Additions)
### cart/
See `cart/README.md` – describes `cart.css` (item rows, summary panel, empty-state styling, responsive adjustments).

### checkout/
See `checkout/README.md` – documents `checkout.css` (grid layout, shipping options, validation states, order summary, responsive breakpoints).

### order/
See `order/README.md` – documents `order-detail.css` (confirmation layout, status badges, timeline, address/payment recap).

## Extending Customer Styles
1. Create a new subfolder for major new flows (e.g. `wishlist/`)
2. Add a single entrypoint CSS file named after the flow (e.g. `wishlist.css`)
3. Co-locate any responsive overrides inside the same file unless very large → split `*-responsive.css`
4. Update this README table with description & README status

## Performance & A11y Targets
- Aim for under 70KB uncompressed CSS per critical page (before gzip)
- Use `aspect-ratio`, `object-fit` for stable media rendering (avoid layout shifts)
- All interactive elements styled to show clear focus & hover states

Last updated: 2025-10-14

