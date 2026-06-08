# OAuth2 + JWT Hybrid Authentication Design

## Context

The Nike E-commerce application is a Spring Boot 3.2 JSP web application with Spring Security form login and session-based authentication. Admin pages, checkout, payment return flows, profile pages, cart pages, and JSP rendering currently rely on browser sessions.

The next authentication direction is hybrid:

- Keep session authentication for browser/JSP workflows.
- Add OAuth2 login for social web login, starting with Google.
- Add JWT authentication for API and mobile clients.
- Keep OAuth provider identity separate from Nike application authorization.

## Goals

- Support Google login for the JSP web application without breaking existing email/password login.
- Preserve session authentication for admin, checkout, payment, and JSP pages.
- Add a clean JWT boundary for `/api/v1/**` clients.
- Allow future mobile clients to authenticate through OAuth2/OIDC and receive Nike-issued JWTs.
- Keep application roles controlled by the Nike database, not by Google or another provider.

## Non-Goals

- Do not migrate the JSP application to JWT.
- Do not use Google access tokens as Nike application access tokens.
- Do not implement multiple OAuth providers in the first milestone.
- Do not introduce refresh-token rotation until basic JWT access-token authentication is stable.
- Do not store cart contents inside JWT claims.

## Recommended Architecture

Use two authentication boundaries:

```text
Browser/JSP/Admin/Checkout/Payment -> Session
Mobile/API clients                 -> Nike JWT
OAuth2/OIDC providers              -> External identity proof only
```

The web security chain remains session-based and supports both form login and OAuth2 login.

The API security chain is stateless and accepts only Nike-issued JWTs for protected `/api/v1/**` endpoints.

## Authentication Flows

### Existing Web Login

1. User submits email/password through the JSP login form.
2. Spring Security authenticates with the existing `UserDetailsService`.
3. Spring Security creates a server-side session.
4. JSP pages, admin pages, checkout, and payment flows continue to use the session.

### Google Login For Web

1. User clicks "Continue with Google" on the login page.
2. Spring Security OAuth2 redirects the user to Google.
3. Google returns the user to the application OAuth2 callback.
4. Backend reads verified OAuth2/OIDC user attributes.
5. Backend finds or creates a local `User`.
6. The local user is assigned application roles from the Nike database.
7. Spring Security creates a normal web session.

New Google users are created with role `CUSTOMER`. Google must never assign `ADMIN`.

### API Email/Password Login

1. API client posts credentials to `/api/v1/auth/login`.
2. Backend authenticates with the existing `AuthenticationManager`.
3. Backend returns a Nike access token.
4. Client sends the token as `Authorization: Bearer <token>` on protected API requests.

### OAuth2/OIDC For Mobile

1. Mobile client authenticates with Google using an OIDC-capable mobile flow.
2. Mobile sends the Google ID token to the backend.
3. Backend validates the ID token and extracts the verified email/provider subject.
4. Backend finds or creates the local Nike user.
5. Backend returns a Nike-issued JWT.

The mobile client must use the Nike JWT for Nike APIs. Google tokens are only input to identity verification.

## Security Chain Design

Use separate Spring Security filter chains:

```text
@Order(1) /api/v1/** -> stateless JWT authentication
@Order(2) web        -> formLogin + oauth2Login + session
```

API chain:

- Match `/api/v1/**`.
- Disable CSRF because API clients use bearer tokens, not browser form sessions.
- Set `SessionCreationPolicy.STATELESS`.
- Permit public API endpoints such as product browsing and auth endpoints.
- Require authentication for protected profile, cart, checkout API, order API, and admin API endpoints.
- Add `JwtAuthenticationFilter` before username/password authentication.

Web chain:

- Keep form login.
- Add OAuth2 login.
- Keep CSRF protection.
- Keep session logout and `JSESSIONID` invalidation.
- Continue protecting `/admin/**` with `ROLE_ADMIN`.

## Components

### OAuth2UserProvisioningService

Responsible for converting provider identity into a local Nike user.

Responsibilities:

- Normalize provider email.
- Check whether the local user already exists.
- Create a new user for a new verified email.
- Link provider identity to an existing user when the email matches.
- Reject disabled or locked local accounts.
- Assign default role `CUSTOMER` for new OAuth users.

### OAuth2LoginSuccessHandler

Responsible for post-login routing for browser OAuth2 login.

Responsibilities:

- Ensure a local user exists through `OAuth2UserProvisioningService`.
- Build the authenticated principal.
- Redirect the user to the intended web page or home page.

### JwtService

Responsible for Nike JWT creation and validation.

Token claims should stay small:

- `sub`: local user id
- `email`: local user email
- `roles`: local application roles
- `iat`: issued at
- `exp`: expiration time

Do not put password hashes, addresses, cart data, payment data, or mutable profile data in JWT claims.

### JwtAuthenticationFilter

Responsible for authenticating protected API requests.

Responsibilities:

- Read `Authorization: Bearer <token>`.
- Validate signature and expiration.
- Load or reconstruct authorities.
- Set `SecurityContextHolder`.
- Return `401` for missing/invalid tokens on protected endpoints.

### OAuthProviderAccount

Add a local persistence model in the first OAuth2 milestone so account linking does not rely only on email.

Suggested fields:

- `id`
- `user_id`
- `provider`
- `provider_subject`
- `email_at_link_time`
- `created_at`

This avoids relying only on email forever and allows future providers.

## Data And Account Linking Rules

- If OAuth email matches an existing local user, link the provider to that user.
- If no user exists and provider email is verified, create a new `CUSTOMER` user.
- If provider email is missing or unverified, reject login.
- If local user is locked or disabled, reject login.
- If a provider subject is already linked to another user, reject login.
- Admin access must be granted only through local database role changes.

## Cart, Checkout, And Payment Rules

Cart contents remain server-side in the database or session-backed guest identity. JWT only identifies the user.

Browser cart:

- Continue using session/current shopper behavior.
- Merge guest cart into user cart after successful login when needed.

API cart:

- Use JWT to identify the user.
- Store cart items in the database.
- Do not encode cart state in JWT.

Checkout and payment:

- Prefer session for JSP checkout and payment return flows.
- Future API checkout can use JWT, but payment provider return/IPN endpoints need explicit validation independent of JWT.

## Error Handling

Web errors:

- OAuth login denied or failed redirects back to `/login?oauthError=true`.
- Locked or disabled account redirects back to `/login?accountLocked=true`.
- Missing provider email redirects back to login with a generic message.

API errors:

- Missing token: `401 Unauthorized`.
- Invalid or expired token: `401 Unauthorized`.
- Authenticated but insufficient role: `403 Forbidden`.
- Disabled or locked local account: `403 Forbidden`.

Avoid exposing whether an email is registered during login failures.

## Token Policy

Initial JWT policy:

- Access token lifetime: 15-30 minutes.
- Signing key comes from environment configuration.
- Use HTTPS in production.
- Do not store access tokens in browser localStorage for JSP flows.
- Mobile clients should store tokens in platform secure storage.

Refresh tokens are deferred until the API JWT layer is stable.

## Testing Strategy

Unit tests:

- `JwtService` creates valid tokens.
- `JwtService` rejects expired tokens.
- `JwtService` rejects tampered tokens.
- `OAuth2UserProvisioningService` creates a new customer.
- `OAuth2UserProvisioningService` links an existing email.
- `OAuth2UserProvisioningService` rejects unverified email.

Security tests:

- Protected API without token returns `401`.
- Protected API with valid customer token returns `200`.
- Admin API with customer token returns `403`.
- Web form login still creates a session.
- OAuth2 web success creates or links a local user.
- Logout invalidates web session.

Regression tests:

- Checkout JSP flow still works with session.
- Payment return/IPN endpoints remain reachable according to current payment rules.
- Public product APIs remain accessible without authentication if intended.

## Implementation Milestones

1. Add OAuth2 client dependency and Google configuration placeholders.
2. Add Google OAuth2 login to the web security chain.
3. Add OAuth2 user provisioning and account-linking rules.
4. Add JWT service and JWT API security chain.
5. Add `/api/v1/auth/login` for email/password JWT login.
6. Add mobile OAuth2/OIDC token exchange endpoint.
7. Add refresh tokens only after access-token flows and tests are stable.

## Fixed Initial Decisions

- Google redirect URIs are environment-specific configuration, not hardcoded code constants.
- Introduce `OAuthProviderAccount` in the OAuth2 milestone so account linking is explicit from the first provider.
- Use `/api/v1/auth/oauth2/google` as the initial mobile OAuth2/OIDC exchange endpoint.
- Keep admin JSP pages session-based. Add admin JWT APIs only when there is a concrete non-JSP admin client.
