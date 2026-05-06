# Mock Interview Questions for Java Intern / Fresher

Tai lieu nay duoc soan dua tren thiet ke va codebase cua project `Nike Ecommerce Web Application`.

Muc tieu:
- kiem tra ung vien co thuc su tham gia lam project hay chi biet mo ta chung chung
- ket hop cau hoi nen tang Java/Spring va cau hoi tinh huong bam theo luong thuc te cua project
- phu hop cho muc intern / fresher, nhung van du de loc ra muc do hieu code that

Huong su dung:
- Tong so: `50` cau
- Nen hoi theo kieu dao sau vao nhung cau ung vien tra loi mo ho
- Co the chon ngau nhien 15-20 cau de phong van nhanh, hoac dung full bo cho mock interview

## Phan 1. Tong quan kien truc va cong nghe

### Q01.
Project nay dung nhung cong nghe chinh nao o backend, frontend va database?

### Q02.
Tai sao project nay dung `Spring Boot + JSP` thay vi tach rieng frontend framework nhu React?

### Q03.
Project dang to chuc source theo kieu nao: package-by-feature hay package-by-layer? Neu dua vi du trong project thi em se chi ra sao?

### Q04.
Hay ke ten cac module chinh dang co trong `features` va noi ngan gon chuc nang cua tung module.

### Q05.
Luong request tu browser vao server trong project nay di qua nhung lop nao truoc khi render JSP hoac tra JSON?

### Q06.
WAR application la gi? Trong project nay build artifact chinh la gi?

## Phan 2. Catalog, Product va Search

### Q07.
Entity product trong project nay khong chi co `Product` ma con tach them nhung entity nao? Tai sao phai tach nhu vay?

### Q08.
Phan biet giua `Product`, `ProductColor`, `ProductVariant`, `ProductImage` trong nghiep vu cua project nay.

### Q09.
Vi sao inventory trong project nay duoc quan ly theo `variant stock` thay vi luu mot `stockQuantity` tong o product?

### Q10.
Neu tren trang chi tiet san pham nguoi dung chon mau va size, backend can du lieu gi de xac dinh co them vao cart duoc hay khong?

### Q11.
`ProductListController`, `ProductListService`, `ProductDetailController`, `ProductDetailPageService` khac vai tro nhau nhu the nao?

### Q12.
Trong project nay, `salePrice` va `price` duoc dung nhu the nao de hien thi gia thuc te cho nguoi dung?

### Q13.
Neu mot san pham co nhieu anh theo tung mau, em nghi backend dang map anh theo product va theo color nhu the nao?

### Q14.
Trang search va product list trong project nay dang filter theo huong nao: full server-side hay mot phan client-side? Dieu nay anh huong gi den do tin cay cua bo loc?

### Q15.
Neu interviewer hoi: "Tai sao khong return truc tiep entity Product ra API?" thi em tra loi the nao dua tren project nay?

## Phan 3. Cart

### Q16.
Khi them vao gio hang, request cua cart toi backend can toi thieu nhung thong tin nao?

### Q17.
Tai sao `CartItem` nen tham chieu den `ProductVariant` hoac thong tin bien the thay vi chi luu `productId`?

### Q18.
Trong `CartService`, theo em can validate nhung loi nghiep vu nao truoc khi them hoac cap nhat so luong?

### Q19.
Neu user chua dang nhap ma bam add to cart, project nay co xu huong xu ly the nao?

### Q20.
`CartCountResponse`, `CartItemViewResponse`, `CartSummaryResponse` duoc tach rieng de lam gi?

### Q21.
Neu interviewer hoi: "Tai sao quantity update khong cho phep am hoac bang 0 trong mot so context?" em se giai thich sao?

### Q22.
Neu hai request cung luc tang so luong mot cart item, em thay nguy co gi va thuong xu ly theo huong nao o muc intern/fresher?

## Phan 4. Checkout, Order va Payment

### Q23.
Phan biet vai tro cua `CheckoutPageController` va `CheckoutController`.

### Q24.
`CheckoutPageViewService` va `CheckoutService` khac nhau o diem nao?

### Q25.
Tai sao `PlaceCheckoutRequest` khong nen nhan toan bo thong tin gia, tong tien tu frontend roi tin tuong su dung luon?

### Q26.
Khi place order, backend can snapshot nhung thong tin nao vao `Order` va `OrderItem` de tranh bi anh huong boi viec catalog thay doi sau nay?

### Q27.
Tai sao trong tai lieu domain co nhac `OrderItem` can luu `product_name`, `unit_price`, `size`, `color` thay vi chi FK den product?

### Q28.
`CheckoutPaymentHandler` duoc tao ra de giai quyet bai toan gi? Tai sao hien tai da co `CodCheckoutPaymentHandler`?

### Q29.
Neu sau nay them thanh toan bang VNPAY vao ngay trong checkout flow, em se noi cach gan no vao abstraction hien tai nhu the nao?

### Q30.
Trong VNPAY flow, tai sao can tach `VNPaySignatureService` rieng khoi `VNPayPaymentService`?

### Q31.
Phan biet `return URL` va `IPN` trong thanh toan online.

### Q32.
Tai sao `PaymentTransaction` can duoc persist xuong database ma khong chi xu ly tren session hoac memory?

### Q33.
Neu payment gateway goi IPN 2 lan cho cung mot giao dich, backend nen de phong dieu gi?

## Phan 5. Identity va Security

### Q34.
Project nay dang xac thuc nguoi dung theo cach nao? Em biet dieu do qua nhung class nao?

### Q35.
`User` implement `UserDetails` va dong thoi co `CustomUserPrincipal`. Tai sao trong du an that co khi van ton tai ca hai huong nay?

### Q36.
`SecurityCurrentUserProvider` giai quyet van de gi? Tai sao khong doc thang `SecurityContextHolder` o moi service?

### Q37.
Neu interviewer hoi "role-based access control trong project nay ap dung o dau?" thi em se neu nhung khu vuc nao?

### Q38.
Tai sao frontend/admin runtime can `CSRF-aware bootstrap` hoac can token CSRF khi goi API?

### Q39.
Khi dang ky tai khoan moi, service signup thuong phai validate nhung gi ngoai cac annotation co san?

## Phan 6. Admin va luong Product Add/Edit

### Q40.
Hay mo ta luong admin them moi product tu JSP den API va xuong database trong project nay.

### Q41.
Tai sao form add/edit product su dung `multipart/form-data` thay vi JSON thuan?

### Q42.
Trong request create/update product cua admin, `productData`, `files`, `fileClientKeys` dong vai tro gi?

### Q43.
`existingImageId` duoc them vao contract update de giai quyet bai toan nao?

### Q44.
Tai sao luong edit product trong project nay chon chien luoc `full-replace color block`? Uu va nhuoc diem cua cach nay la gi?

### Q45.
Tai sao service phai validate `existingImageId` co thuoc dung product dang sua hay khong?

### Q46.
Vì sao danh sach file cu can xoa tren disk lai nen duoc xu ly sau khi transaction database commit thanh cong?

### Q47.
`ProductImageStorageService` la abstraction cho muc dich gi? Tai sao khong goi truc tiep thao tac file trong `AdminProductService`?

## Phan 7. JPA, du lieu, exception va test

### Q48.
Trong phan order, tai sao repository co the dung `@EntityGraph` thay vi de lazy load mac dinh?

### Q49.
Hay neu 3 exception nghiep vu thuc te trong project nay va noi khi nao tung exception co the xay ra.

### Q50.
Neu em duoc yeu cau viet them test de chung minh minh thuc su hieu project nay, em se uu tien test 3 khu vuc nao nhat va vi sao?
