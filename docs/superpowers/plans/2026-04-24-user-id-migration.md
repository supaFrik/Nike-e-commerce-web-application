# User Id Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Migrate checkout-related user foreign keys from `customer_id` to `user_id` so the database matches the `User -> users` model.

**Architecture:** Keep `User` as the aggregate root mapped to `users`. Update JPA join columns to `user_id`, then add a guarded SQL migration that renames legacy columns, recreates foreign keys to `users(id)`, and normalizes the cart unique constraint.

**Tech Stack:** Spring Boot, Spring Data JPA, Hibernate, MySQL

---

### Task 1: Update JPA mappings

**Files:**
- Modify: `src/main/java/vn/demo/nike/features/identity/user/domain/Address.java`
- Modify: `src/main/java/vn/demo/nike/features/identity/user/domain/Contact.java`
- Modify: `src/main/java/vn/demo/nike/features/checkout/cart/domain/CartItem.java`
- Modify: `src/main/java/vn/demo/nike/features/checkout/order/domain/Order.java`

- [ ] Change each `@JoinColumn(name = "customer_id")` to `@JoinColumn(name = "user_id")`.
- [ ] Rename the cart unique constraint from `uk_customer_variant` to `uk_user_variant`.
- [ ] Change the cart unique constraint columns from `customer_id, variant_id` to `user_id, variant_id`.

### Task 2: Add guarded SQL migration

**Files:**
- Create: `src/main/resources/db/migration/V20260424_01__migrate_customer_id_to_user_id.sql`

- [ ] Create helper stored procedures that:
  - drop a foreign key by looking it up from `information_schema.KEY_COLUMN_USAGE`
  - drop an index if it exists
- [ ] For `addresses`, `cart_items`, `orders`, and `contact_messages`:
  - rename `customer_id` to `user_id` if needed
  - recreate FK from `user_id` to `users(id)`
- [ ] Recreate cart unique index as `uk_user_variant (user_id, variant_id)`.
- [ ] Drop helper procedures at the end of the script.

### Task 3: Verify code compiles

**Files:**
- Test: project compile only

- [ ] Run: `mvn -q -DskipTests compile`
- [ ] Confirm there are no compilation errors from the mapping changes.
