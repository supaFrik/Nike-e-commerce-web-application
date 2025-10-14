# customer/checkout – Checkout Page Styles

## File
| File | Purpose |
|------|---------|
| `checkout.css` | Complete checkout flow styling: layout grid, shipping options, form fields, order summary, discount code, validation, responsive breakpoints |

## Layout Overview
- Two-column desktop grid: shipping (left) / summary (right) via `.checkout-content`
- Right column uses `position: sticky` for persistent order summary
- Breaks to single column under 1024px; summary moves above form on tablet/mobile

## Major Sections
| Selector | Responsibility |
|----------|----------------|
| `.delivery-options` | Shipping method radio group container with selectable cards |
| `.shipping-form` | Customer address & contact form fields (grid rows with `.form-row`) |
| `.order-summary` | Cart item list + totals + discount + pay CTA |
| `.discount-section` | Discount / promo code entry + apply button |
| `.order-totals` | Subtotal, shipping, discount, final total rows |
| `.pay-now-btn` | Primary checkout submit / payment trigger |
| `.security-info` | Trust / security messaging panel |

## Validation UX
- Invalid inputs get `.input-error` (red border + subtle background)
- Inline message uses `.field-error-msg`
- Unselected shipping group on submit adds `.delivery-options.input-error` with generated pseudo-content message

## Responsive Breakpoints
| Max Width | Adjustments |
|-----------|-------------|
| 1024px | Single column, summary reorders first |
| 768px | Reduced paddings, smaller title, single column form rows |
| 480px | Compact spacing, stacked phone input, tighter gaps |

## CSS Variables Used
Relies on global tokens: colors (`--color-*`), spacing (`--size-spacing-*`), font sizes (`--font-size-*`), transitions.

## Extend / Customize
- Add payment method icons block: `.payment-methods`
- Add progress indicator: `.progress-steps` (mentioned in media queries) – ensure consistent gap tokens
- Add gift message module: `.gift-message`

## Accessibility
- Shipping option entire card clickable (label wraps radio)
- Hidden radios remain accessible (native input retained)
- Error messages appear adjacent to field (associate via `aria-describedby` if adding attributes)

Last updated: 2025-10-14

