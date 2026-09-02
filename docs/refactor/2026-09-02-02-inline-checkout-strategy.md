# 02 — Inline CheckoutPaymentHandler Strategy

- **Ngày:** 2026-09-02
- **Phạm vi:** `src/main/java/vn/demo/nike/features/checkout/service/`
- **Loại:** Xóa abstraction 1-impl (YAGNI)

## Vấn đề

`CheckoutPaymentHandler` là interface `supports(PaymentMethod) + handle(Order, snapshots)` với duy nhất 1 implementation `CodCheckoutPaymentHandler` (COD). VNPay không qua handler mà qua `VNPayPaymentService` riêng. `CheckoutService:43` inject `List<CheckoutPaymentHandler>` rồi stream/filter/findFirst/orElse fallback `buildPendingExternalPaymentResponse()` — ceremony cho 1 nhánh.

`PaymentMethod` chỉ có `COD` + `VNPAY`. Đến khi có gateway thứ 3 mới cần strategy.

## Thay đổi

Xóa 2 file:

- `CheckoutPaymentHandler.java` (interface 10 dòng)
- `CodCheckoutPaymentHandler.java` (`@Component`, 42 dòng)

`CheckoutService.java`:

```diff
- private final List<CheckoutPaymentHandler> checkoutPaymentHandlers;

  // ponytail: single if-branch, extract strategy when 3rd gateway lands
  private CheckoutInitiationResponse handleCheckoutCompletion(Order order, List<CheckoutItemSnapshot> snapshots) {
-     return checkoutPaymentHandlers.stream()
-             .filter(h -> h.supports(order.getPaymentMethod()))
-             .findFirst()
-             .map(h -> h.handle(order, snapshots))
-             .orElseGet(() -> buildPendingExternalPaymentResponse(order, snapshots));
+     if (order.getPaymentMethod() == PaymentMethod.COD) {
+         int itemCount = snapshots.stream()
+                 .map(CheckoutItemSnapshot::getQuantity).filter(q -> q != null)
+                 .mapToInt(Integer::intValue).sum();
+         return new CheckoutInitiationResponse(order.getId(), itemCount, order.getSubtotal(),
+                 order.getShippingCost(), order.getDiscount(), order.getTotal(),
+                 order.getOrderStatus(), order.getPaymentMethod(), false, null, null, snapshots);
+     }
+     return buildPendingExternalPaymentResponse(order, snapshots);
  }
```

Net: **-2 file**, `CheckoutService` +~15 / -7 dòng. Logic COD giữ nguyên (copy từ handler cũ), `buildPendingExternalPaymentResponse` giữ cho VNPay/extern.

## Vérification

- `mvn compile -q` → pass.
- `grep -r "CheckoutPaymentHandler" src` → 0 hit.
- `PaymentMethod` enum chỉ `COD`/`VNPAY` — if/else đủ.

## Khi nào tách lại

Khi gateway thứ 3 (Momo/ZaloPay/PayPal) xuất hiện, tách thành `CheckoutPaymentHandler` interface + `List` inject như cũ hoặc registry `Map<PaymentMethod, handler>`. Dán comment `ponytail` làm mốc.

## Tham chiếu

- `features/checkout/service/CheckoutPaymentHandler.java:10`
- `features/checkout/service/CodCheckoutPaymentHandler.java`
- `features/checkout/service/CheckoutService.java:43,93-99,101-122`
