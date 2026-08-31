# VNPay sandbox — test cards & how to use them

This document shows how to use the VNPay sandbox test cards (the example cards included in the project's README) to exercise the checkout/payment flow in development.

Checklist
- Ensure local app is running and using the `dev`/sandbox VNPay config
- Create an order through the storefront and choose VNPay as payment method
- On the VNPay sandbox page use one of the test card numbers below
- Use the OTP / password values provided in this repo when prompted

Prerequisites

- Set VNPay environment variables for sandbox in your shell or `.env` (see `README.md`). Typical keys:
  - `VNPAY_TMN_CODE` — your sandbox terminal code
  - `VNPAY_HASH_SECRET` — your sandbox secret used to sign requests
  - `VNPAY_PAY_URL` — sandbox payment URL (e.g. `https://sandbox.vnpayment.vn/paymentv2/vpcpay.html`)
  - `VNPAY_RETURN_URL` and `VNPAY_IPN_URL` — callbacks used by the app

How to run a manual sandbox payment

1. Start the application locally and the MySQL container if needed.

```powershell
mvn spring-boot:run
```

2. Browse the storefront and place an order as a normal customer (add items to cart → checkout).

3. On the payment step select VNPay (the app will redirect you to the VNPay sandbox URL).

4. On the VNPay sandbox payment page enter the card details from the table below, then use the OTP / password values shown at the end of the table when prompted.

5. The sandbox will simulate the result (success, insufficient balance, locked card, expired, etc.) depending on the card number you use.

Table of test cards (also present in the project README)

| Type     | Bank | Card number           | Name           | Valid through | CVV | Status                                           |
|----------|------|-----------------------|----------------|---------------|-----|--------------------------------------------------|
| ATM Card | NCB  | 9704198526191432198   | NGUYEN VAN A   | 07/15         | 123 | Successful                                       |
| ATM Card | NCB  | 9704195798459170488   | NGUYEN VAN A   | 07/15         | 123 | Card does not have enough balance                |
| ATM Card | NCB  | 9704192181368742      | NGUYEN VAN A   | 07/15         | 123 | Card not activated                               |
| ATM Card | NCB  | 9704193370791314      | NGUYEN VAN A   | 07/15         | 123 | Card is locked                                   |
| ATM Card | NCB  | 9704194841945513      | NGUYEN VAN A   | 07/15         | 123 | Card expired                                     |

- OTP: `123456`
- Password: `1234`

Notes and troubleshooting

- Security: these numbers are test-only and belong in development/sandbox. Never use real card numbers or production secrets in this repository.
- If you do not see the VNPay payment option during checkout:
  - Verify `VNPAY_*` environment variables are set and the app was restarted after setting them.
  - Check the server logs for VNPay redirect request generation errors.
- If the sandbox page rejects the card or shows unexpected behavior, try a different card from the table above — each card simulates a different response.
- The VNPay sandbox may require the return/notify URLs to be reachable (some sandbox setups accept localhost; others require a reachable callback). If you run into IPN/notify failures, you may need to run the app on a public tunnel (e.g., `ngrok`) or inspect the redirect result only.

Developer tips

- The application constructs a VNPay request URL and signs it using `VNPAY_HASH_SECRET`. Inspect logs in `VNPayPaymentController` (or the feature package) to see the exact parameters sent to VNPay.
- When testing automated flows (integration tests), you may stub the VNPay endpoints or run a lightweight integration harness that verifies the controller signatures rather than actually calling the sandbox.

If you want, I can also add a small example request snippet showing the typical VNPay form parameters the app sends (e.g. `vnp_Amount`, `vnp_ReturnUrl`, `vnp_TmnCode`, `vnp_SecureHash`) — tell me if you want that included.

