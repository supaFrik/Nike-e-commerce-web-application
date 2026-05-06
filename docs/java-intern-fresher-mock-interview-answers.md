# Mock Interview Answers for Java Intern / Fresher

Tai lieu nay la dap an tom tat cho bo cau hoi mock interview dua tren project `Nike Ecommerce Web Application`.

Nguyen tac:
- dap an ngan gon, gom y chinh
- khong co gang viet thanh "van mau"
- khi phong van that, nen dao tiep bang cau hoi vi sao, neu thay doi requirement thi sao, trade-off la gi

## Dap an tong hop

### A01.
Backend dung `Java 17`, `Spring Boot 3.2`, `Spring MVC`, `Spring Data JPA`, `Spring Security`.
Frontend dung `JSP + JSTL + CSS + Vanilla JS`.
Database dung `MySQL 8`.

### A02.
Vi day la project SSR theo huong Java web truyen thong, JSP render tren server don gian hon cho intern/fresher, deployment cung de dong bo trong mot WAR app. No phu hop muc tieu hoc Spring MVC, JPA, Security va flow full-stack co ban.

### A03.
Project theo `package-by-feature`.
Vi du co `features/admin`, `features/catalog`, `features/checkout`, `features/payment`, `features/identity`, `features/order`.
Moi feature tu gom controller, service, dto, repository, domain lien quan.

### A04.
- `admin`: dashboard, product, category, order list ben quan tri
- `catalog`: category, product, search, cart storefront
- `checkout`: xu ly dat hang
- `home`: landing page
- `identity`: auth, user, current user
- `order`: trang don hang va thong tin order
- `payment`: VNPAY va giao dich thanh toan

### A05.
Thuong la: browser -> controller -> service -> repository -> database.
Neu la page SSR thi controller dat data vao model roi render JSP.
Neu la API thi controller tra JSON/DTO.

### A06.
WAR la dang package de deploy len servlet container hoac app server.
Artifact chinh cua project la `target/nike-starter.war`.

### A07.
Tach them `ProductColor`, `ProductVariant`, `ProductImage`.
Ly do la san pham co nhieu mau, moi mau co nhieu size/stock, va co nhieu anh theo tung mau. Tach nhu vay dung hon nghiep vu va de mo rong.

### A08.
- `Product`: thong tin goc cua san pham
- `ProductColor`: mau sac cua san pham
- `ProductVariant`: bien the mua duoc, thuong gan voi `color + size + stock + sku`
- `ProductImage`: anh cua product hoac cua tung color

### A09.
Vi ton kho that su nam o muc bien the, khong nam o muc product chung.
Nguoi dung mua size 41 mau do, khong phai mua "Nike Air Max" chung chung.

### A10.
Can biet variant nao duoc chon, stock hien tai, variant co active khong, quantity hop le khong, va user co dang nhap khong neu gio hang gan voi tai khoan.

### A11.
Controller nhan request va dieu huong.
Service xu ly nghiep vu va tong hop data.
`ProductList*` phuc vu trang danh sach, `ProductDetail*` phuc vu trang chi tiet.

### A12.
`price` la gia goc, `salePrice` la gia giam neu co.
Gia hien thi thuc te thuong uu tien `salePrice`, neu khong co moi dung `price`.

### A13.
`ProductImage` co the gan truc tiep voi `Product` hoac gan voi `ProductColor`.
Nhung anh theo color giup trang detail doi gallery dung theo mau ma user dang chon.

### A14.
Project dang co phan filter server-side va co phan client-side.
Dieu nay nghia la mot so nhan filter tren UI co the chua thuc su duoc database/query ho tro day du.

### A15.
Khong return entity truc tiep vi de lo cau truc noi bo, de bi loop relation, kho kiem soat field tra ve, va kem an toan.
Project nay uu tien DTO/request/response de tach boundary.

### A16.
Toi thieu can variant id hoac thong tin xac dinh duy nhat variant, va quantity.
Trong mot so thiet ke co them product id de doi chieu, nhung variant moi la du lieu chinh.

### A17.
Vi cart phai biet chinh xac mau, size, stock va gia lien quan den bien the.
Chi luu `productId` se mo ho va khong du de checkout dung.

### A18.
Can validate:
- user da dang nhap chua
- variant ton tai khong
- variant co active khong
- so luong hop le khong
- ton kho co du khong

### A19.
Project co exception `UnauthenticatedUserException`, nen xu huong hien tai la cart gan voi user dang nhap va backend chan neu chua auth.

### A20.
De moi API tra ve dung muc data can thiet.
`CartCountResponse` cho badge so luong, `CartItemViewResponse` cho dong item, `CartSummaryResponse` cho tong hop tien/so luong.

### A21.
Vi quantity am la sai nghiep vu, con bang 0 thuong nen duoc coi la xoa item thay vi update binh thuong.
No giup giu rule ro rang va tranh state loi.

### A22.
Nguy co race condition va vuot ton kho.
Huong xu ly co the la transaction, re-check stock khi update, va khong tin state cu tu frontend.

### A23.
`CheckoutPageController` render trang checkout va nap data hien thi.
`CheckoutController` nhan request dat hang, validate va tao order.

### A24.
`CheckoutPageViewService` phuc vu read-model cho UI.
`CheckoutService` phuc vu write/business flow nhu place order, tru stock, tao order, xu ly payment.

### A25.
Vi frontend khong dang tin cay.
Gia, shipping, tong tien phai duoc backend tinh lai tu database va business rule hien tai.

### A26.
Can snapshot ten san pham, don gia, mau, size, so luong, tong tien, dia chi giao, payment method, shipping method, trang thai.
Muc dich la de order van dung du sau khi catalog thay doi.

### A27.
Vi product sau nay co the doi ten, doi gia, doi mau, thậm chi bi xoa.
Order la ban ghi lich su, nen phai luu snapshot thay vi phu thuoc hoan toan vao du lieu song.

### A28.
Nó tao diem mo rong cho nhieu kieu thanh toan.
`CodCheckoutPaymentHandler` la implementation hien co cho thanh toan khi nhan hang.

### A29.
Tao them implementation nhu `VnPayCheckoutPaymentHandler`, map payment method den handler tuong ung, va de `CheckoutService` goi qua interface thay vi hard-code.

### A30.
Tach signature service de phan rieng logic ky/xac thuc chu ky.
Nhu vay `VNPayPaymentService` tap trung vao luong nghiep vu, con logic ma hoa/verify co the test va tai su dung de hon.

### A31.
`return URL` la luong browser cua nguoi dung quay ve sau khi thanh toan.
`IPN` la callback server-to-server tu cong thanh toan de xac nhan ket qua giao dich mot cach tin cay hon.

### A32.
Can persist de co lich su giao dich, doi soat, retry, xu ly IPN, chong mat du lieu khi restart, va debug su co thanh toan.

### A33.
Can de phong xu ly lap.
Backend nen co idempotency theo ma giao dich hoac check trang thai cu truoc khi cap nhat lai.

### A34.
Dang xac thuc bang `Spring Security` theo email/password.
Co the thay qua `User`, `AuthController`, `CustomUserPrincipal`, `SecurityCurrentUserProvider`.

### A35.
Do project co the dang trong giai doan chuyen doi hoac thu nghiem hai cach wrap principal.
Y chinh la co mot doi tuong dai dien user dang nhap cho Spring Security dung.

### A36.
No dong goi cach lay current user, giam viec service phu thuoc truc tiep vao security framework.
De test de hon va giu service sach hon.

### A37.
Ap dung ro nhat o khu `admin` va khu user thong thuong.
Admin can role cao hon, con customer chi duoc thao tac gio hang, checkout, order cua chinh minh.

### A38.
Vi API thay doi du lieu nhu create/update/delete can chong CSRF.
Frontend can gui token hop le de request duoc server chap nhan.

### A39.
Thuong phai validate:
- email da ton tai chua
- password va confirm password co khop khong
- dinh dang input co hop le khong
- business rule nhu role mac dinh, field bat buoc

### A40.
Admin mo JSP add/edit -> JS `product-form.js` quan ly state -> goi API lay options/detail -> submit multipart request vao `AdminProductController` -> `AdminProductService` validate/xu ly image/variant/color -> repository save xuong DB.

### A41.
Vi form vua co metadata JSON, vua co file anh upload.
`multipart/form-data` cho phep gui dong thoi ca hai loai du lieu.

### A42.
- `productData`: JSON mo ta product, color, image metadata, variant
- `files`: cac file anh moi
- `fileClientKeys`: key de map file upload vao image metadata dung tren client

### A43.
De giu lai anh cu khi edit.
Neu khong co truong nay backend khong biet image nao la image da ton tai can retain.

### A44.
Vi no don gian hoa logic dong bo state phia server: client gui state moi, server thay moi toan bo block color.
Uu diem: de reason, de xoa phan tu khong con ton tai.
Nhuoc diem: co the ghi de nhieu hon can thiet va can validate/xu ly xoa file can than.

### A45.
De tranh bug hoac loi bao mat kieu lay nham image cua product khac gan vao product dang sua.
Nó cung giup dam bao tinh nhat quan du lieu.

### A46.
Neu xoa file truoc ma transaction DB rollback thi se mat file nhung du lieu van co the chua doi thanh cong.
Lam sau commit giup uu tien nhat quan business state.

### A47.
De tach storage concern khoi business service.
Sau nay co the doi tu local disk sang cloud storage ma khong pha `AdminProductService`.

### A48.
De tranh N+1 query va nap truoc relation can dung cho trang order.
No giup view service doc du lieu day du hon ma khong bi lazy loading tung phan.

### A49.
Vi du:
- `InsufficientStockException`: them cart/checkout vuot ton kho
- `VariantNotFoundException`: variant khong ton tai
- `AddressNotFoundException`: checkout voi dia chi khong hop le
- `UnauthenticatedCheckoutException`: chua dang nhap ma checkout
- `InvalidUploadedImageException`: file anh upload khong hop le

### A50.
Nen uu tien:
- test `AdminProductService`: phuc tap nhat, nhieu validate, image, variant, full-replace
- test `CheckoutService`: lien quan dat hang, snapshot, stock, payment flow
- test `CartService`: nghiep vu co ban nhung rat de phat sinh loi ton kho, quantity, auth

Neu muon them, co the test `VNPayPaymentController/Service` va `OrderPageViewService`.
