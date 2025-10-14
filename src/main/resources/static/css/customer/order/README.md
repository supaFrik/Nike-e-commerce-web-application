# customer/order – Order Detail & Confirmation Styles

## File
| File | Purpose |
|------|---------|
| `order-detail.css` | Displays confirmation / order detail: header status, item recap, totals, addresses, timeline/progress components |

## Typical Components
(Adjust names to actual classes used in `order-detail.css` once reviewed.)
| Component | Suggested Class Pattern |
|-----------|------------------------|
| Status banner | `.order-status`, `.order-status--success` |
| Order metadata bar | `.order-meta` (order #, date, payment method) |
| Item list | `.order-items`, `.order-item` |
| Address blocks | `.order-address`, `.order-address--shipping`, `.order-address--billing` |
| Progress / timeline | `.order-timeline`, `.order-step` |
| Totals summary | `.order-totals` |
| Help / support block | `.order-help` |

## Accessibility
- Use `<dl>` for key/value metadata where possible
- Status messages should be in an `aria-live="polite"` region if updated asynchronously
- Timeline steps include `aria-current="step"` for current milestone (future enhancement)

## Extending
Add printable invoice styles with `@media print` inside the same file or a dedicated `order-print.css` if it grows large.

## Future Enhancements
- Add reorder CTA styling
- Introduce visual icons for each fulfillment step
- Add skeleton states while fetching order details asynchronously

Last updated: 2025-10-14

