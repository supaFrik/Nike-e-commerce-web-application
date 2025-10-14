# customer/cart – Cart (Bag) Page Styles

## File
| File | Purpose |
|------|---------|
| `cart.css` | Main cart page (bag) styling: line items, pricing summary, quantity controls, empty state, responsive adjustments |

## Key Styled Elements
- Item list container and item rows
- Product thumbnail, meta (name, color, size), price alignment
- Quantity stepper buttons `.quantity-btn`, remove action `.remove-btn`
- Discount / subtotal / total summary box
- Empty cart state (paired with JS `update-button.js` / removal logic)
- Checkout CTA enabling/disabling transitions

## State Classes (Expected / JS Collaboration)
| Class | Meaning |
|-------|---------|
| `.updating` | Row in pending background update (PUT / DELETE) |
| `.empty-cart` | Container for empty bag UI |
| `.disabled` | (Optional) style for disabled checkout button |

## Accessibility Considerations
- Ensure button roles for quantity controls have `aria-label` (e.g. "Increase quantity")
- Maintain `aria-live` region for dynamic subtotal (future enhancement)
- Preserve focus after asynchronous row update / removal

## Responsive Guidelines
- Narrow viewports: stack thumbnail above details or compress spacing
- Provide consistent tap target size for quantity buttons (44px min hit area)

## Extending
- Add promo code module → create `.cart-promo` block
- Add shipping estimator → create `.cart-shipping-estimator`

Last updated: 2025-10-14

