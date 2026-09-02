# 04 — Prune Administrator Assets

- **Ngày:** 2026-09-02
- **Phạm vi:** `src/main/resources/administrator/`
- **Loại:** Xóa asset chết (YAGNI — grep 0 hit trong JSP/JS/CSS)

## Vấn đề

`administrator/` 30 MB, trong đó:

- `assets/extra-libs/` 2,6 MB — 8 lib vendored: `c3`, `datatables.net`, `datatables.net-bs4`, `jvector`, `knob`, `prism`, `sparkline`, `taskboard` (kèm `taskboard/example1/*.php` — file PHP thừa trong WAR Java). Grep `c3|d3|datatables|jvector|knob|sparkline|taskboard|prism` + `extra-libs` toàn `src/main/webapp/**/*.jsp` + `src/main/resources/static/**/*.js` → 0 hit. `administrator/layout/css.jsp` + `js.jsp` chỉ nạp `admin.css` + `runtime.js`/`shell.js`, không nạp lib nào. `docs/docs.html` liệt kê lib nhưng là trang doc tĩnh, không phải runtime admin.
- `dist/css/icons/font-awesome/less/` (124 KB) + `scss/` (129 KB) — source Less/SCSS không dùng ở runtime, chỉ cần `css/` (compiled) + `webfonts/`. Xóa cả 2.
- `dist/css/icons/themify-icons/ie7/` — shim IE7, không còn target browser.

## Vérification trước xóa

```
grep -r "extra-libs" src/main/webapp → 0
grep -r "c3|datatables|jvector|knob|sparkline|taskboard" src/main/resources/static --include="*.js" → 0
ls administrator/assets/extra-libs → 8 folder
du -sh font-awesome/less 124K, scss 129K, themify-icons/ie7
```

## Thay đổi

```diff
- src/main/resources/administrator/assets/extra-libs/          (8 folder, 2,6 MB, gồm taskboard/*.php)
- src/main/resources/administrator/dist/css/icons/font-awesome/less/
- src/main/resources/administrator/dist/css/icons/font-awesome/scss/
- src/main/resources/administrator/dist/css/icons/themify-icons/ie7/
```

4 lệnh `rm -rf`. Không đụng `dist/css/icons/font-awesome/css/` + `webfonts/` + `themify-icons.css/less` + `assets/libs|js|images` (đang dùng). `administrator/` 30 MB → 27 MB.

Giữ `assets/libs/` — cần kiểm tra riêng (Perfect Scrollbar, Bootstrap) có dùng không trước khi xóa.

## Vérification sau xóa

- `JAVA_HOME="C:/Program Files/Java/jdk-17" mvn compile -q` → `exit: 0`.
- `ls assets/` → `images js libs css webfonts fonts themify-icons.css themify-icons.less` (không còn `extra-libs`).
- `ls font-awesome/` → `css webfonts` (không còn `less/scss`).

## Rủi ro & khôi phục

- Nếu sau này cần `datatables`/`c3` cho admin, khôi phục từ git hoặc CDN thay vì vendor trong WAR. `docs/docs.html` có link `https://datatables.net/` làm tham chiếu.
- `less`/`scss` khôi phục từ `font-awesome` upstream nếu cần build lại CSS (hiện dùng `css/` compiled sẵn).

## Tham chiếu

- `src/main/webapp/WEB-INF/views/administrator/layout/css.jsp`
- `src/main/webapp/WEB-INF/views/administrator/layout/js.jsp`
- `src/main/webapp/WEB-INF/views/administrator/dashboard.jsp`
- `src/main/resources/administrator/docs/docs.html:162,302,411` (liệt kê lib trong doc, không phải runtime)
