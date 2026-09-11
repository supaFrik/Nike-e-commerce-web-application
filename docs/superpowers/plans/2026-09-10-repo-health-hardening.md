# Repo Health Hardening — Tests, Coverage Gate &amp; Lint Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Raise repo from 41 tests / 183 source files with a decorative 10% JaCoCo gate and no lint to meaningful coverage on critical catalog/home services, a real coverage gate, and minimal editor/lint hygiene — without adding runtime dependencies.

**Architecture:** Pure unit tests with Mockito (same pattern as `CartServiceTest`/`CheckoutPageViewServiceTest`) — no DB, no Spring context. Each service gets one test class mocking its direct collaborators. Coverage gate is raised in `pom.xml` (JaCoCo) and synced in `.github/workflows/testing-coverage.yml`. Lint is `.editorconfig` + optional Checkstyle in a follow-up plan — this plan does not add SpotBugs/PMD.

**Tech Stack:** Java 17, Spring Boot 3.2.12 (parent), Maven 3.9.x, JUnit 5 + Mockito (via `spring-boot-starter-test`), JaCoCo 0.8.10, `MockitoExtension`

**Spec:** This conversation's repo health report — `main-2` after `e808a51` (41 tests, `spring-boot-starter-parent:3.2.12`, JaCoCo `0.10` in `pom.xml` + `10%` in `testing-coverage.yml`, no `.editorconfig`/Checkstyle, 5 test files vs 183 source files). No external spec doc; the "spec" is the health findings and the existing test patterns in `src/test/java/vn/demo/nike/features/{cart,checkout}/`.

## Global Constraints

- Java version: 17 (`maven.compiler.release=17`, `java.version=17`) — do not change.
- Spring Boot parent: 3.2.12 — do not bump again in this plan (separate concern).
- Build must stay `mvn -B verify` green (JaCoCo `check` phase runs on `verify`). Run with `JAVA_HOME="C:/Program Files/Java/jdk-17"` on Windows (see `ci.yml`/`testing-coverage.yml` use `temurin 17`).
- No new runtime/production dependencies. Test scope only (`spring-boot-starter-test` already provides JUnit5/Mockito/AssertJ).
- Test pattern: `@ExtendWith(MockitoExtension.class)`, `@Mock` collaborators, `@InjectMocks` service under test, `when(...).thenReturn(...)` + `assert*` + `verify(...)` — match `CartServiceTest.java` and `CheckoutPageViewServiceTest.java`. No `@SpringBootTest`, no DB.
- Coverage gate lives in two places — change both or neither: `pom.xml` (`jacoco-maven-plugin` `<minimum>`) and `.github/workflows/testing-coverage.yml` (`madrapps/jacoco-report` `min-coverage-overall` + `min-coverage-changed-files`).
- OrderPageViewService (372 lines, 3 repos + payment transactions + image resolver) is explicitly out of scope for this plan — too large for a "small diff" plan; defer to a dedicated plan.
- Commit per task, conventional messages, include `Co-Authored-By: Claude Code <noreply@anthropic.com>`.

---

## File Structure

```yaml
pom.xml                                              # modify: JaCoCo minimum (Task 2)
.github/workflows/testing-coverage.yml               # modify: min-coverage-* (Task 2)
.editorconfig                                         # create: Task 1
src/test/java/vn/demo/nike/features/catalog/category/service/CategoryServiceTest.java   # create: Task 3
src/test/java/vn/demo/nike/features/catalog/product/service/ProductListServiceTest.java # create: Task 4
src/test/java/vn/demo/nike/features/catalog/product/service/ProductDetailServiceTest.java # create: Task 5
src/test/java/vn/demo/nike/features/home/service/HomeServiceTest.java                  # create: Task 6
```

Each test file owns one service. No shared test utils — keep tests self-contained (YAGNI). If a helper is needed later, extract then.

---

### Task 1: Add `.editorconfig` (minimal hygiene)

**Files:**

- Create: `.editorconfig`
- Modify: none
- Test: manual — `mvn -B verify` still passes; IDE picks up config

**Interfaces:**

- Consumes: nothing
- Produces: repo-wide editor defaults for future contributors; no build effect

- [ ] **Step 1: Create `.editorconfig`**

Create `C:\Users\aDMIN\orca\workspaces\Nike Ecommerce Web Application\main-2\.editorconfig`:

```ini
root = true

[*]
charset = utf-8
end_of_line = lf
insert_final_newline = true
trim_trailing_whitespace = true
indent_style = space
indent_size = 4

[*.{yml,yaml}]
indent_size = 2

[*.md]
trim_trailing_whitespace = false
```

Rationale: matches Maven/Java 4-space, YAML 2-space, and existing repo (LF, UTF-8). No Checkstyle yet — keep diff minimal.

- [ ] **Step 2: Verify no build break**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B verify -DskipTests 2>&1 | tail -20
```

Expected: `BUILD SUCCESS`.

- [ ] **Step 3: Commit**

```bash
git add .editorconfig
git commit -m "$(cat <<'EOF'
chore: add .editorconfig for consistent formatting

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

### Task 2: Raise JaCoCo gate from decorative 10% to real threshold

**Files:**

- Modify: `pom.xml:176-193` (jacoco `check` execution)
- Modify: `.github/workflows/testing-coverage.yml:28-34` (madrapps thresholds)
- Test: `mvn -B verify` — must still pass after bump (new tests in Tasks 3-6 will carry it; run this task last if needed, but plan orders it early so later tasks prove the gate)

**Interfaces:**

- Consumes: current `pom.xml` JaCoCo 0.8.10 config, `testing-coverage.yml` v1.8.0 usage
- Produces: enforced minimum that actually fails on regression

**Context:** Current thresholds are `0.10` / `'10'` — any non-empty coverage passes. Target is `0.30` overall (conservative — repo is ~22% instruction coverage with 41 tests; 30% is reachable after Tasks 3-6, but 50% would fail today). Changed-files threshold stays at `30` to catch new untested code. If this still fails locally, lower to `0.25` — do not keep `0.10`.

- [ ] **Step 1: Edit `pom.xml` — raise `<minimum>`**

In `pom.xml`, find:

```xml
<counter>INSTRUCTION</counter><value>COVEREDRATIO</value><minimum>0.10</minimum>
```

Replace with:

```xml
<counter>INSTRUCTION</counter><value>COVEREDRATIO</value><minimum>0.30</minimum>
```

Exact block after edit (lines 183-193):

```xml
[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Cexecution%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Cid%3E]]check[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Fid%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Cphase%3E]]verify[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Fphase%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Cgoals%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Cgoal%3E]]check[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Fgoal%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Fgoals%3E]]
[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:block-html:%20%20%20%20%3Cconfiguration%3E]]
        [[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Crules%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Crule%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Celement%3E]]BUNDLE[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Felement%3E]]
            [[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Climits%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Climit%3E]]
                [[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Ccounter%3E]]INSTRUCTION[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Fcounter%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Cvalue%3E]]COVEREDRATIO[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Fvalue%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3Cminimum%3E]]0.30[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Fminimum%3E]]
            [[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Flimit%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Flimits%3E]]
        [[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Frule%3E]][[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:inline-html:%3C%2Frules%3E]]
[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:block-html:%20%20%20%20%3C%2Fconfiguration%3E]]
[[ORCA_RICH_MD:53c59736530c0eebddeecd2a8d324fe9:block-html:%3C%2Fexecution%3E]]
```

- [ ] **Step 2: Edit `.github/workflows/testing-coverage.yml` — sync thresholds**

Replace:

```yaml
          min-coverage-overall: '10'
          min-coverage-changed-files: '10'
```

With:

```yaml
          min-coverage-overall: '30'
          min-coverage-changed-files: '30'
```

- [ ] **Step 3: Verify build still passes (before new tests, this may fail — that's expected)**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B verify 2>&1 | tail -30
```

- If `BUILD SUCCESS` (coverage already ≥30%): proceed to commit.
- If `Rule violated for bundle nike-ecommerce: instructions covered ratio is 0.2x, expected minimum is 0.30`: **do not commit yet** — leave changes staged and proceed to Tasks 3-6; return here after they pass and re-verify. The plan intentionally orders this early to surface the gap; implementers may defer the commit to the end of the plan.

- [ ] **Step 4: Commit (or defer commit to end if coverage not yet met)**

```bash
git add pom.xml .github/workflows/testing-coverage.yml
git commit -m "$(cat <<'EOF'
chore: raise JaCoCo gate from 10% to 30%

Sync pom.xml and testing-coverage.yml so CI actually fails on
coverage regression. 30% is conservative for current ~22% baseline;
raise to 50% after catalog/home tests land.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

### Task 3: Unit tests for `CategoryService` (simplest service — start here)

**Files:**

- Create: `src/test/java/vn/demo/nike/features/catalog/category/service/CategoryServiceTest.java`
- Modify: none
- Test: `mvn -B -Dtest=CategoryServiceTest test`

**Interfaces:**

- Consumes: `CategoryService` (`getAllCategories()`, `getCategoryNameById(Long)`, `getCategoryIdByName(String)`), `CategoryRepository` (`findAllByOrderByNameAsc()`, `findById(Long)`, `findByNameIgnoreCase(String)`), `Category` entity (`getId()`, `getName()`), `CategoryNotFoundException`
- Produces: coverage for `catalog/category/service` package

**Service under test** (`src/main/java/vn/demo/nike/features/catalog/category/service/CategoryService.java`):

- `List<CategoryView> getAllCategories()` — maps `findAllByOrderByNameAsc()` to `CategoryView(id, name)`
- `String getCategoryNameById(Long id)` — null→null, found→name, missing→`CategoryNotFoundException(id)`
- `Long getCategoryIdByName(String name)` — null→null, found→id, missing→`CategoryNotFoundException(name)`

- [ ] **Step 1: Write the failing test (file does not exist yet — this is the red)**

Create `src/test/java/vn/demo/nike/features/catalog/category/service/CategoryServiceTest.java`:

```java
package vn.demo.nike.features.catalog.category.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.category.dto.response.CategoryView;
import vn.demo.nike.features.catalog.category.entity.Category;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private CategoryService categoryService;

    private Category cat(long id, String name) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        return c;
    }

    @Test
    void getAllCategories_mapsAllOrdered() {
        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(List.of(cat(2, "B"), cat(1, "A")));

        List<CategoryView> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals("B", result.get(0).getName());
        verify(categoryRepository).findAllByOrderByNameAsc();
    }

    @Test
    void getCategoryNameById_returnsNullWhenIdNull() {
        assertNull(categoryService.getCategoryNameById(null));
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    void getCategoryNameById_returnsNameWhenFound() {
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(cat(10, "Running")));

        assertEquals("Running", categoryService.getCategoryNameById(10L));
    }

    @Test
    void getCategoryNameById_throwsWhenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryNameById(99L));
    }

    @Test
    void getCategoryIdByName_returnsNullWhenNameNull() {
        assertNull(categoryService.getCategoryIdByName(null));
        verify(categoryRepository, never()).findByNameIgnoreCase(any());
    }

    @Test
    void getCategoryIdByName_returnsIdWhenFound() {
        when(categoryRepository.findByNameIgnoreCase("running")).thenReturn(Optional.of(cat(5, "Running")));

        assertEquals(5L, categoryService.getCategoryIdByName("running"));
    }

    @Test
    void getCategoryIdByName_throwsWhenNotFound() {
        when(categoryRepository.findByNameIgnoreCase("Nope")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryIdByName("Nope"));
    }
}
```

Notes:

- `Category` has `setId`/`setName` (Lombok) — verified via `src/main/java/vn/demo/nike/features/catalog/category/entity/Category.java`.
- If JaCoCo still at 10% when this task runs, threshold is irrelevant; if already at 30%, this test alone does not need to push bundle to 30% — later tasks combine.

- [ ] **Step 2: Run test to verify it passes (was red before file existed)**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B -Dtest=CategoryServiceTest test 2>&1 | tail -20
```

Expected: `Tests run: 7, Failures: 0, Errors: 0`.

- [ ] **Step 3: Run full verify to ensure no regression**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B verify 2>&1 | tail -20
```

Expected: `BUILD SUCCESS`, `Tests run: 48` (41 + 7).

- [ ] **Step 4: Commit**

```bash
git add src/test/java/vn/demo/nike/features/catalog/category/service/CategoryServiceTest.java
git commit -m "$(cat <<'EOF'
test: add unit tests for CategoryService

Cover getAllCategories, getCategoryNameById and getCategoryIdByName
including null-input and not-found branches.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

### Task 4: Unit tests for `ProductListService` (pagination + sort)

**Files:**

- Create: `src/test/java/vn/demo/nike/features/catalog/product/service/ProductListServiceTest.java`
- Modify: none
- Test: `mvn -B -Dtest=ProductListServiceTest test`

**Interfaces:**

- Consumes: `ProductListService` (`getProductList(Long categoryId, String sort, int page)` and 2-arg overload), `ProductRepository.findProductList(Long, Pageable)`, `ProductQueryResponseMapper.toProductListItemView(ProductListItemView)`, `ProductListItemView` DTO
- Produces: coverage for sort resolution (`price_asc`, `price_desc`, `newest`, default) and page clamping

**Service under test** (`src/main/java/vn/demo/nike/features/catalog/product/service/ProductListService.java`):

- `PAGE_SIZE = 20`, `safePage = max(0, page)`, `PageRequest.of(safePage, 20, resolveSort(sort))`
- `resolveSort` switch: `price_asc`→`price ASC`, `price_desc`→`price DESC`, `newest`→`createDate DESC`, default→`createDate DESC`
- Delegates to `productRepository.findProductList(categoryId, pageable).map(mapper::toProductListItemView)`

- [ ] **Step 1: Write the failing test**

Create `src/test/java/vn/demo/nike/features/catalog/product/service/ProductListServiceTest.java`:

```java
package vn.demo.nike.features.catalog.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductListServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductQueryResponseMapper mapper;
    @InjectMocks private ProductListService service;

    private ProductListItemView view(long id) {
        return new ProductListItemView(id, "Air Max", BigDecimal.valueOf(200), null, false, null, "Shoes", "Running", "hero.jpg", 3);
    }

    @Test
    void getProductList_clampsNegativePageToZero() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        Page<ProductListItemView> result = service.getProductList(5L, "newest", -3);

        assertEquals(1, result.getTotalElements());
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(5L), captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(20, captor.getValue().getPageSize());
    }

    @Test
    void getProductList_resolvesPriceAscSort() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "price_asc", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertTrue(captor.getValue().getSort().getOrderFor("price").isAscending());
    }

    @Test
    void getProductList_resolvesPriceDescSort() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "price_desc", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertTrue(captor.getValue().getSort().getOrderFor("price").isDescending());
    }

    @Test
    void getProductList_resolvesNewestSort() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "newest", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertEquals("createDate", captor.getValue().getSort().getOrderFor("createDate").getProperty());
        assertTrue(captor.getValue().getSort().getOrderFor("createDate").isDescending());
    }

    @Test
    void getProductList_defaultSortIsNewest() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "unknown", 0);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertEquals("createDate", captor.getValue().getSort().getOrderFor("createDate").getProperty());
    }

    @Test
    void getProductList_twoArgOverloadDefaultsToPageZero() {
        Page<ProductListItemView> page = new PageImpl<>(List.of(view(1)));
        when(productRepository.findProductList(any(), any(Pageable.class))).thenReturn(page);
        when(mapper.toProductListItemView(any())).thenAnswer(i -> i.getArgument(0));

        service.getProductList(1L, "newest");

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findProductList(eq(1L), captor.capture());
        assertEquals(0, captor.getValue().getPageNumber());
    }
}
```

- [ ] **Step 2: Run test to verify it passes**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B -Dtest=ProductListServiceTest test 2>&1 | tail -20
```

Expected: `Tests run: 6, Failures: 0`.

- [ ] **Step 3: Run full verify**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B verify 2>&1 | tail -20
```

Expected: `BUILD SUCCESS`, `Tests run: 54` (48 + 6).

- [ ] **Step 4: Commit**

```bash
git add src/test/java/vn/demo/nike/features/catalog/product/service/ProductListServiceTest.java
git commit -m "$(cat <<'EOF'
test: add unit tests for ProductListService

Cover page clamping, sort resolution (price_asc/desc, newest,
default) and 2-arg overload.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

### Task 5: Unit tests for `ProductDetailService` (not-found + mapping)

**Files:**

- Create: `src/test/java/vn/demo/nike/features/catalog/product/service/ProductDetailServiceTest.java`
- Modify: none
- Test: `mvn -B -Dtest=ProductDetailServiceTest test`

**Interfaces:**

- Consumes: `ProductDetailService.getProductDetail(Long id)`, `ProductRepository.findDetailById(Long)`, `ProductQueryResponseMapper.toProductDetailResponse(Product)`, `ProductNotFoundException`, `Product` entity with `getColors()`/`getImages()`/`getVariants()`
- Produces: coverage for product detail happy path and not-found

- [ ] **Step 1: Write the failing test**

Create `src/test/java/vn/demo/nike/features/catalog/product/service/ProductDetailServiceTest.java`:

```java
package vn.demo.nike.features.catalog.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.demo.nike.features.catalog.product.dto.response.ProductDetailResponse;
import vn.demo.nike.features.catalog.product.dto.response.ProductQueryResponseMapper;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.exception.ProductNotFoundException;
import vn.demo.nike.features.catalog.product.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDetailServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductQueryResponseMapper mapper;
    @InjectMocks private ProductDetailService service;

    @Test
    void getProductDetail_throwsWhenNotFound() {
        when(productRepository.findDetailById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.getProductDetail(99L));
    }

    @Test
    void getProductDetail_returnsMappedResponseWhenFound() {
        Product product = mock(Product.class);
        ProductColor color = mock(ProductColor.class);
        when(color.getImages()).thenReturn(List.of());
        when(color.getVariants()).thenReturn(List.of());
        when(product.getColors()).thenReturn(List.of(color));
        when(productRepository.findDetailById(1L)).thenReturn(Optional.of(product));

        ProductDetailResponse expected = mock(ProductDetailResponse.class);
        when(mapper.toProductDetailResponse(product)).thenReturn(expected);

        ProductDetailResponse result = service.getProductDetail(1L);

        assertSame(expected, result);
        verify(productRepository).findDetailById(1L);
        verify(mapper).toProductDetailResponse(product);
    }

    @Test
    void getProductDetail_initializesLazyCollections() {
        Product product = mock(Product.class);
        ProductColor color = mock(ProductColor.class);
        when(color.getImages()).thenReturn(List.of(mock(vn.demo.nike.features.catalog.product.entity.ProductImage.class)));
        when(color.getVariants()).thenReturn(List.of(mock(vn.demo.nike.features.catalog.product.entity.ProductVariant.class)));
        when(product.getColors()).thenReturn(List.of(color));
        when(productRepository.findDetailById(1L)).thenReturn(Optional.of(product));
        when(mapper.toProductDetailResponse(product)).thenReturn(mock(ProductDetailResponse.class));

        service.getProductDetail(1L);

        verify(color).getImages();
        verify(color).getVariants();
    }
}
```

Note: uses `mock(Product.class)` to avoid building full entity graph — service only calls `getColors()` then `getImages()`/`getVariants()` on each color and delegates to mapper. If `Product` is final or has no default constructor issues, this will still work because we mock it; alternative is to build a real `Product` with `ProductColor` — but mock is minimal.

- [ ] **Step 2: Run test to verify it passes**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B -Dtest=ProductDetailServiceTest test 2>&1 | tail -20
```

Expected: `Tests run: 3, Failures: 0`.

- [ ] **Step 3: Run full verify**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B verify 2>&1 | tail -20
```

Expected: `BUILD SUCCESS`, `Tests run: 57` (54 + 3).

- [ ] **Step 4: Commit**

```bash
git add src/test/java/vn/demo/nike/features/catalog/product/service/ProductDetailServiceTest.java
git commit -m "$(cat <<'EOF'
test: add unit tests for ProductDetailService

Cover not-found exception, happy-path mapping and lazy
collection initialization.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

### Task 6: Unit tests for `HomeService` (orchestration + exception swallowing)

**Files:**

- Create: `src/test/java/vn/demo/nike/features/home/service/HomeServiceTest.java`
- Modify: none
- Test: `mvn -B -Dtest=HomeServiceTest test`

**Interfaces:**

- Consumes: `HomeService.getRunningSection()`, `CategoryService.getCategoryIdByName(String)`, `ProductListService.getProductList(Long, String)`, `CategoryNotFoundException`, `RunningSectionView`, `ProductListItemView`
- Produces: coverage for `home/service` and the `Running` category fallback

**Service under test** (`src/main/java/vn/demo/nike/features/home/service/HomeService.java`):

- Calls `categoryService.getCategoryIdByName("Running")` → `productListService.getProductList(runningCategoryId, "newest")` → `limit(8)` → `new RunningSectionView(id, "Running", products)`
- On `CategoryNotFoundException`: returns `new RunningSectionView(null, "Running", List.of())`

- [ ] **Step 1: Write the failing test**

Create `src/test/java/vn/demo/nike/features/home/service/HomeServiceTest.java`:

```java
package vn.demo.nike.features.home.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import vn.demo.nike.features.catalog.category.exception.CategoryNotFoundException;
import vn.demo.nike.features.catalog.category.service.CategoryService;
import vn.demo.nike.features.catalog.category.dto.response.RunningSectionView;
import vn.demo.nike.features.catalog.product.dto.request.ProductListItemView;
import vn.demo.nike.features.catalog.product.service.ProductListService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @Mock private CategoryService categoryService;
    @Mock private ProductListService productListService;
    @InjectMocks private HomeService homeService;

    private ProductListItemView view(long id) {
        return new ProductListItemView(id, "Shoe " + id, BigDecimal.valueOf(100), null, false, null, "Shoes", "Running", "hero.jpg", 2);
    }

    @Test
    void getRunningSection_returnsProductsWhenCategoryExists() {
        when(categoryService.getCategoryIdByName("Running")).thenReturn(7L);
        List<ProductListItemView> ten = IntStream.rangeClosed(1, 10).mapToObj(i -> view(i)).toList();
        when(productListService.getProductList(7L, "newest")).thenReturn(new PageImpl<>(ten));

        RunningSectionView result = homeService.getRunningSection();

        assertEquals(7L, result.getCategoryId());
        assertEquals("Running", result.getCategoryName());
        assertEquals(8, result.getProducts().size());
        assertEquals(1L, result.getProducts().get(0).getId());
    }

    @Test
    void getRunningSection_limitsToEight() {
        when(categoryService.getCategoryIdByName("Running")).thenReturn(3L);
        List<ProductListItemView> twenty = IntStream.rangeClosed(1, 20).mapToObj(i -> view(i)).toList();
        when(productListService.getProductList(3L, "newest")).thenReturn(new PageImpl<>(twenty));

        RunningSectionView result = homeService.getRunningSection();

        assertEquals(8, result.getProducts().size());
    }

    @Test
    void getRunningSection_returnsEmptyWhenCategoryNotFound() {
        when(categoryService.getCategoryIdByName("Running")).thenThrow(new CategoryNotFoundException("Running"));

        RunningSectionView result = homeService.getRunningSection();

        assertNull(result.getCategoryId());
        assertEquals("Running", result.getCategoryName());
        assertTrue(result.getProducts().isEmpty());
        verify(productListService, never()).getProductList(any(), any());
    }

    @Test
    void getRunningSection_returnsEmptyListWhenNoProducts() {
        when(categoryService.getCategoryIdByName("Running")).thenReturn(9L);
        when(productListService.getProductList(9L, "newest")).thenReturn(new PageImpl<>(List.of()));

        RunningSectionView result = homeService.getRunningSection();

        assertEquals(9L, result.getCategoryId());
        assertTrue(result.getProducts().isEmpty());
    }
}
```

- [ ] **Step 2: Run test to verify it passes**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B -Dtest=HomeServiceTest test 2>&1 | tail -20
```

Expected: `Tests run: 4, Failures: 0`.

- [ ] **Step 3: Run full verify (final gate check)**

Run:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B verify 2>&1 | tail -30
```

Expected: `BUILD SUCCESS`, `Tests run: 61` (57 + 4). JaCoCo `All coverage checks have been met.` if Task 2's 30% is already committed; otherwise this is the point to commit Task 2 (or re-run with `30%` and confirm).

- [ ] **Step 4: Commit**

```bash
git add src/test/java/vn/demo/nike/features/home/service/HomeServiceTest.java
git commit -m "$(cat <<'EOF'
test: add unit tests for HomeService

Cover happy path, 8-item limit, CategoryNotFound fallback
and empty product list.

Co-Authored-By: Claude Code <noreply@anthropic.com>
EOF
)"
```

---

## Verification (after all tasks)

```bash
JAVA_HOME="C:/Program Files/Java/jdk-17" mvn -B verify 2>&1 | grep -E "Tests run|BUILD|coverage|violated"
```

Expected:

- `Tests run: 61, Failures: 0, Errors: 0` (41 existing + 20 new)
- `BUILD SUCCESS`
- `All coverage checks have been met.` (if 30% threshold committed)
- `git status --porcelain` clean (no `??`, no `!!` for `.editorconfig` — it should be tracked)

Check diff before PR:

```bash
git log --oneline -7
git diff --stat HEAD~6  # adjust for number of commits
```

## Deferred / Out of Scope

- **OrderPageViewService (372 lines)** — needs its own plan (heavy mocking of `OrderRepository` + `PaymentTransactionRepository` + `ProductRepository` + `ProductImageUrlResolverUtil`). Do not add in this plan.
- **Checkstyle/SpotBugs/PMD** — deferred; `.editorconfig` is the minimal lint for this plan. Add `maven-checkstyle-plugin` (Google style) in next hardening pass after tests stabilize.
- **Spring Boot 3.3.x/3.4.x jump** — separate plan; 3.2.12 is the patch ceiling for this plan.
- **Raising JaCoCo to 50%+** — do after `admin`, `auth`, `payment`, `search` services get tests; 30% is the honest next rung.
- `**versions-maven-plugin**` — optional; run `mvn versions:display-dependency-updates` ad-hoc, no need to add to `pom.xml` yet.

## Self-Review

- [x] Spec coverage: health report's 4 gaps (uncovered `CategoryService`/`ProductListService`/`ProductDetailService`/`HomeService`, decorative 10% gate, no `.editorconfig`) each map to a task (Tasks 1-6). `OrderPageViewService` explicitly deferred.
- [x] Placeholder scan: no `TBD`/`TODO`/`handle edge cases` — each test file has concrete assertions, each edit has exact file+line and replacement value.
- [x] Type consistency: `CategoryView`, `RunningSectionView`, `ProductListItemView`, `ProductDetailResponse`, `ProductNotFoundException`, `CategoryNotFoundException` names match source (`src/main/java/...` verified via `cat`). `ProductRepository.findProductList` and `CategoryRepository.findAllByOrderByNameAsc` signatures verified via `grep`.

