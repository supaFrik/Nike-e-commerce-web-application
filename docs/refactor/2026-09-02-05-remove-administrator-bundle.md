# 05 — Remove Entire `administrator/` Resource Bundle

- **Ngày:** 2026-09-02
- **Phạm vi:** `src/main/resources/administrator/` (toàn bộ 27 MB)
- **Loại:** Xóa bundle template chết (YAGNI)

## Vấn đề

`src/main/resources/administrator/` 27 MB còn lại sau lần 04 gồm:

- `assets/` 18 MB — `assets/libs/` 7,1 MB (bootstrap 3,4M, moment 1,5M, fullcalendar 784K, raphael 489K, chart.js 368K, jquery 172K, …), `assets/images/` 11 MB (alert, background, gallery, landingpage, product, users, widgets 6M), `assets/js/product-add.js` 16K
- `dist/` 7,4 MB — `dist/css/` 7,2 MB (style.css 236K, style.min.css 244K, `fonts/Gilmer,Tofino` 3,9M, `icons/font-awesome css+webfonts` + `themify simple-line`), `dist/js/` 217K (feather 67K, jquery.simplePagination, sidebarmenu, app-style-switcher)
- `docs/` 880K + `scss/` 547K

Toàn bộ là bundle của một admin template mua/borrow (Xtreme/AdminPro-like), không phải code dự án.

## Vérification trước xóa

```
grep -r "administrator/dist|administrator/assets|administrator/scss|administrator/docs|administrator/css|administrator/js" src/ → 0
cat src/main/webapp/WEB-INF/views/administrator/layout/css.jsp → chỉ <link href="${env}/css/admin/admin.css">
cat src/main/webapp/WEB-INF/views/administrator/layout/js.jsp → chỉ <script src="${env}/js/common/runtime.js"> + shell.js
grep -r "Gilmer|Tofino|font-awesome|themify|simple-line|feather|sidebarmenu|simplePagination" src/main/resources/static --include="*.css" → 0 (user dùng CDN https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0 + Nunito Sans Google Fonts)
grep administator/*.jsp → chỉ return "administrator/dashboard" view name, không phải resource path
du -sh administrator/* → assets 18M, dist 7,4M, docs 880K, scss 547K = 27M
```

Tất cả JSP admin (`dashboard`, `product/list|add|edit`, `category/*`, `order/list`) đều qua `layout/css.jsp` + `js.jsp` — không chạm `administrator/` ở bất kỳ đâu.

## Thay đổi

```diff
- src/main/resources/administrator/   (27 MB, 4 folder, ~hundreds files)
```

1 lệnh `rm -rf src/main/resources/administrator`. Không tạo file thay thế. `src/main/resources/` còn `db/`, `static/` (34M bao gồm admin theme thật), `application*.properties`.

## Vérification sau xóa

- `JAVA_HOME="C:/Program Files/Java/jdk-17" mvn compile -q` → `exit: 0`.
- `ls src/main/resources/` → `application*.properties, db, static` (không còn `administrator`).
- WAR giảm ~27 MB. Không ảnh hưởng runtime — classpath không còn scan resource thừa.

## Rủi ro & khôi phục

- Nếu sau này cần lại template (ví dụ revert admin UI), khôi phục từ git history (`git log -- src/main/resources/administrator`). Nhưng admin hiện tại đã có `static/css/admin/` + `static/js/admin/` riêng, không phụ thuộc bundle này.
- `Gilmer/Tofino` fonts không dùng — nếu thiết kế đổi font, thêm vào `static/fonts/` thay vì vendor cả icon pack.

## Tham chiếu

- `src/main/webapp/WEB-INF/views/administrator/layout/css.jsp`
- `src/main/webapp/WEB-INF/views/administrator/layout/js.jsp`
- `src/main/webapp/WEB-INF/views/user/layout/css.jsp` (CDN FA, không vendor)
- `src/main/resources/static/css/admin/` — theme thật đang dùng
