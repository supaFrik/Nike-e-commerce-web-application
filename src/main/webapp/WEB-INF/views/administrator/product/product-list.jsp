<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <!-- Required meta tags -->
  <meta charset="UTF-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />

  <!-- Favicon icon-->
  <link rel="shortcut icon" type="image/png" href="${env}/images/favicon.png" />
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
    rel="stylesheet" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@2.44.0/tabler-icons.min.css" />
  <!-- Core Css -->
  <link rel="stylesheet" href="${env}/css/admin/common/theme.css" />
  <link rel="stylesheet" href="${env}/css/common/base.css" />
  <link rel="stylesheet" href="${env}/css/admin/product-list/list-product.css" />
  <title>Danh sách sản phẩm - Bảng điều khiển</title>
</head>

<body class="bg-info/5">
  <main>
    <!--start the project-->
    <div id="main-wrapper" class="flex">
          <aside id="application-sidebar-brand"
            class="hs-overlay hs-overlay-open:translate-x-0 -translate-x-full transform hidden xl:block xl:translate-x-0 xl:end-auto xl:bottom-0 fixed top-0 with-vertical h-screen z-[999] flex-shrink-0 border-r-[1px] w-[270px] border-gray-400/20 bg-white left-sidebar transition-all duration-300">
            <jsp:include page="/WEB-INF/views/administrator/_fragments/sidebar.jsp" />
          </aside>
      <div class="w-full page-wrapper">
        <!-- ========== HEADER ========== -->
        <jsp:include page="/WEB-INF/views/administrator/_fragments/header.jsp" />
        <!-- ========== END HEADER ========== -->

        <!--  Header End -->

        <!-- Main Content -->
        <main class="h-full overflow-y-auto max-w-full pt-4">
          <div class="container full-container py-5">
            <div class="card">
              <div class="card-body">
                <!-- Products Header -->
                <div class="flex justify-between items-center mb-6 gap-4 flex-wrap">
                  <div class="flex flex-col gap-1">
                    <h1 class="text-2xl font-semibold text-gray-900">Sản phẩm</h1>
                    <p class="text-sm text-gray-500">Quản lý danh sách sản phẩm, tồn kho và giá bán.</p>
                  </div>
                  <div class="flex gap-3 items-center w-full md:w-auto justify-between md:justify-end">
                    <div class="relative max-w-xs w-full md:w-[300px] search-wrapper">
                      <span class="absolute inset-y-0 left-3 flex items-center pointer-events-none text-gray-400">
                        <i class="ti ti-search text-base"></i>
                      </span>
                      <input
                        id="searchInput"
                        type="search"
                        placeholder="Tìm kiếm theo tên, danh mục..."
                        class="w-full py-2.5 pl-9 pr-3 border border-gray-200 rounded-lg text-sm outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                        aria-label="Tìm kiếm sản phẩm"
                      />
                    </div>
                    <button
                      id="addProductBtn"
                      type="button"
                      onclick="window.location.href='${env}/admin/product/add'"
                      class="btn-add-product">
                      <span>Thêm sản phẩm</span>
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="12" y1="5" x2="12" y2="19"></line>
                        <line x1="5" y1="12" x2="19" y2="12"></line>
                      </svg>
                    </button>
                  </div>
                </div>

                <!-- Products Layout -->
                <div class="flex flex-col lg:flex-row gap-6">
                  <!-- Products Main -->
                  <div class="flex-1 min-w-0">
                    <!-- Hidden input to store selected product ID -->
                    <input type="hidden" id="selectedProductId" value="" />
                    <!-- Filters Bar -->
                    <div class="flex flex-wrap items-center justify-between mb-5 gap-3">
                      <div class="flex gap-2 flex-wrap flex-1" id="categoryTabs">
                        <button
                          class="category-tab active capitalize"
                          data-category="all">
                          Tất cả sản phẩm
                        </button>
                        <c:forEach var="category" items="${categories}">
                          <button
                            class="category-tab capitalize cursor-pointer"
                            data-category="${category.id}">
                            ${category.name}
                          </button>
                        </c:forEach>
                      </div>
                      <div class="relative">
                        <button
                          class="flex items-center gap-2 py-2 px-4 border border-gray-200 bg-white rounded-md text-sm cursor-pointer text-gray-700 hover:bg-gray-50 hover:border-gray-300 transition"
                          id="sortBtn"
                          type="button"
                          aria-haspopup="true"
                          aria-expanded="false">
                          <span class="hidden sm:inline">Sắp xếp theo</span>
                          <span class="inline sm:hidden">Sắp xếp</span>
                          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <line x1="3" y1="6" x2="21" y2="6"></line>
                            <line x1="3" y1="12" x2="18" y2="12"></line>
                            <line x1="3" y1="18" x2="15" y2="18"></line>
                          </svg>
                        </button>
                        <!-- Sort dropdown is handled by JS (admin-product-list.js) -->
                      </div>
                    </div>

                    <!-- Product Grid -->
                    <div class="min-h-[120px] mb-8" id="productGridWrapper">
                      <!-- Loading / empty states injected by JS will sit here -->
                      <div id="productGrid" class="product-grid"></div>
                      <div id="productEmptyState" class="hidden flex flex-col items-center justify-center py-10 text-sm text-gray-500 gap-3">
                        <span class="inline-flex items-center gap-2 rounded-full border border-dashed border-gray-300 bg-gray-50 px-4 py-2 text-xs font-medium text-gray-600">
                          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="16" height="16" aria-hidden="true" class="text-gray-500">
                            <path fill="currentColor" d="M4 5a2 2 0 0 1 2-2h4.382a2 2 0 0 1 1.414.586l1.828 1.828A2 2 0 0 0 15.464 6H18a2 2 0 0 1 2 2v1h-2V8h-2.536a2 2 0 0 1-1.414-.586L12.222 5.05A2 2 0 0 0 10.808 4.5H6V19h5v2H6a2 2 0 0 1-2-2V5Zm11 7a1 1 0 0 1 1.707-.707L18 10.586l1.293-1.293A1 1 0 0 1 20.707 10L19.414 11.293 20.707 12.586A1 1 0 0 1 19.293 14L18 12.707 16.707 14A1 1 0 0 1 15.293 12.586L16.586 11.293 15.293 10A1 1 0 0 1 16 9h-.001Z"/>
                          </svg>
                          <span>Không có sản phẩm nào được hiển thị</span>
                        </span>
                        <p class="text-xs text-gray-400 text-center max-w-xs">
                          Thử thay đổi bộ lọc, danh mục hoặc từ khóa tìm kiếm để tìm sản phẩm khác.
                        </p>
                      </div>
                      <div id="productLoadingState" class="hidden flex justify-center py-8">
                        <div class="flex items-center gap-2 text-gray-500 text-sm">
                          <span class="w-4 h-4 border-2 border-gray-300 border-t-gray-500 rounded-full animate-spin"></span>
                          <span>Đang tải sản phẩm...</span>
                        </div>
                      </div>
                    </div>

                    <!-- Pagination -->
                    <div class="flex flex-col sm:flex-row justify-between items-center gap-3 py-4 border-t border-gray-100">
                      <div class="text-sm text-gray-600">
                        Hiển thị <span id="pageStart">1</span>–<span id="pageEnd">10</span> trong tổng
                        <span id="totalItems">20</span> kết quả
                      </div>
                      <div class="flex gap-1 items-center" id="paginationControls" aria-label="Phân trang sản phẩm">
                        <button
                          class="w-9 h-9 flex items-center justify-center bg-white rounded-md cursor-pointer text-sm text-gray-700 hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed"
                          id="prevBtn"
                          type="button"
                          aria-label="Trang trước">
                          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="15 18 9 12 15 6"></polyline>
                          </svg>
                        </button>
                        <!-- Numeric buttons are rendered/updated by JS; the static ones are just initial placeholders -->
                        <button class="w-9 h-9 flex items-center justify-center bg-gray-900 text-white rounded-full text-xs font-medium">1</button>
                        <button class="w-9 h-9 flex items-center justify-center border border-gray-200 bg-white rounded-full text-xs text-gray-700 hover:bg-gray-50">2</button>
                        <button class="w-9 h-9 flex items-center justify-center border border-gray-200 bg-white rounded-full text-xs text-gray-700 hover:bg-gray-50">3</button>
                        <span class="px-1 text-xs text-gray-400">...</span>
                        <button class="w-9 h-9 flex items-center justify-center border border-gray-200 bg-white rounded-full text-xs text-gray-700 hover:bg-gray-50">10</button>
                        <button
                          class="w-9 h-9 flex items-center justify-center bg-white rounded-md cursor-pointer text-sm text-gray-700 hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed"
                          id="nextBtn"
                          type="button"
                          aria-label="Trang tiếp theo">
                          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="9 18 15 12 9 6"></polyline>
                          </svg>
                        </button>
                      </div>
                    </div>
                  </div>

                  <!-- Quick View Panel -->
                  <aside
                    class="quick-view-panel w-full max-w-md lg:w-[260px] bg-white border border-gray-200 rounded-lg max-h-[80vh] lg:sticky lg:top-4 shadow-sm overflow-y-auto"
                    id="quickViewPanel"
                    aria-label="Chi tiết nhanh sản phẩm">
                    <div class="px-3 py-2 border-b border-gray-200 flex justify-between items-center gap-3">
                      <div>
                        <h2 class="text-sm font-semibold text-gray-900">Chỉnh sửa nhanh</h2>
                        <p class="text-[11px] text-gray-500">Cập nhật thông tin chính của sản phẩm.</p>
                      </div>
                      <a
                        href="#"
                        id="fullViewLink"
                        class="inline-flex items-center gap-1 text-blue-600 text-[11px] font-medium no-underline hover:text-blue-700">
                        <span>Xem chi tiết</span>
                        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                          <line x1="5" y1="12" x2="19" y2="12"></line>
                          <polyline points="12 5 19 12 12 19"></polyline>
                        </svg>
                      </a>
                    </div>

                    <div class="flex gap-3 mb-2 border-b border-gray-200 px-3 overflow-x-auto">
                      <button
                        class="quick-view-tab py-1.5 border-0 bg-transparent cursor-pointer text-[11px] sm:text-xs text-gray-900 border-b-2 border-gray-900 font-medium hover:text-gray-900 whitespace-nowrap"
                        data-tab="descriptions">
                        Mô tả
                      </button>
                      <button
                        class="quick-view-tab py-1.5 border-0 bg-transparent cursor-pointer text-[11px] sm:text-xs text-gray-600 border-b-2 border-transparent font-medium hover:text-gray-900 whitespace-nowrap"
                        data-tab="inventory">
                        Tồn kho
                      </button>
                      <button
                        class="quick-view-tab py-1.5 border-0 bg-transparent cursor-pointer text-[11px] sm:text-xs text-gray-600 border-b-2 border-transparent font-medium hover:text-gray-900 whitespace-nowrap"
                        data-tab="pricing">
                        Giá
                      </button>
                    </div>

                    <div class="px-3 pb-3 pt-1">
                      <!-- Descriptions Tab -->
                      <div class="block" id="descriptionsTab">
                        <div class="product-image-preview mb-3 rounded-md overflow-hidden bg-gray-50 flex items-center justify-center min-h-[110px]">
                          <img id="quickViewImage" src="" alt="Sản phẩm" class="max-h-24 object-contain" />
                        </div>

                        <div class="mb-2.5">
                          <label class="block text-[11px] font-medium text-gray-700 mb-1" for="productName">Tên sản phẩm</label>
                          <input
                            type="text"
                            class="w-full py-2 px-2.5 border border-gray-200 rounded-md text-xs outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                            id="productName"
                            placeholder="Nhập tên sản phẩm" />
                        </div>

                        <div class="mb-2.5">
                          <label class="block text-[11px] font-medium text-gray-700 mb-1" for="productDescription">Mô tả</label>
                          <textarea
                            class="form-textarea w-full py-2 px-2.5 border border-gray-200 rounded-md text-xs outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition min-h-[70px]"
                            id="productDescription"
                            placeholder="Nhập mô tả sản phẩm"></textarea>
                        </div>

                        <div class="mb-2">
                          <label class="block text-[11px] font-medium text-gray-700 mb-1" for="productCategory">Danh mục sản phẩm</label>
                          <select
                            class="w-full py-2 px-2.5 border border-gray-200 rounded-md text-xs outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                            id="productCategory">
                            <option value="running">Chạy bộ</option>
                            <option value="basketball">Bóng rổ</option>
                            <option value="football">Bóng bầu dục</option>
                            <option value="soccer">Bóng đá</option>
                          </select>
                        </div>
                      </div>

                      <!-- Inventory Tab -->
                      <div class="hidden" id="inventoryTab">
                        <div class="flex flex-col gap-2.5">
                          <div class="p-2.5 bg-gray-50 rounded-md flex items-center justify-between">
                            <div>
                              <div class="text-[11px] text-gray-500 mb-0.5">Tồn kho hiện tại</div>
                              <div class="text-base font-semibold text-gray-900" id="currentStock">0</div>
                            </div>
                            <span class="inline-flex items-center justify-center w-6 h-6 rounded-full bg-white text-gray-700 border border-gray-200 text-[10px] font-medium">Kho</span>
                          </div>
                          <div class="p-2.5 bg-gray-50 rounded-md flex items-center justify-between">
                            <div>
                              <div class="text-[11px] text-gray-500 mb-0.5">Tổng đã bán</div>
                              <div class="text-base font-semibold text-gray-900" id="totalSold">0</div>
                            </div>
                            <span class="inline-flex items-center justify-center w-6 h-6 rounded-full bg-white text-gray-700 border border-gray-200 text-[10px] font-medium">Bán</span>
                          </div>
                          <div class="mb-2">
                            <label class="block text-[11px] font-medium text-gray-700 mb-1" for="updateStock">Cập nhật tồn kho</label>
                            <input
                              type="number"
                              class="w-full py-2 px-2.5 border border-gray-200 rounded-md text-xs outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                              id="updateStock"
                              placeholder="Nhập số lượng tồn kho mới" />
                          </div>
                        </div>
                      </div>

                      <!-- Pricing Tab -->
                      <div class="hidden" id="pricingTab">
                        <div class="flex flex-col gap-2.5">
                          <div class="mb-2">
                            <label class="block text-[11px] font-medium text-gray-700 mb-1" for="productPrice">Giá</label>
                            <input
                              type="text"
                              class="w-full py-2 px-2.5 border border-gray-200 rounded-md text-xs outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                              id="productPrice"
                              placeholder="₫ 0" />
                          </div>
                          <div class="grid grid-cols-2 gap-2.5">
                            <div class="mb-2">
                              <label class="block text-[11px] font-medium text-gray-700 mb-1" for="productDiscount">Giảm giá (%)</label>
                              <input
                                type="number"
                                class="w-full py-2 px-2.5 border border-gray-200 rounded-md text-xs outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                                id="productDiscount"
                                placeholder="0" />
                            </div>
                            <div class="mb-2">
                              <label class="block text-[11px] font-medium text-gray-700 mb-1" for="finalPrice">Giá cuối</label>
                              <input
                                type="text"
                                class="w-full py-2 px-2.5 border border-gray-200 rounded-md text-xs outline-none bg-gray-50 text-gray-700"
                                id="finalPrice"
                                placeholder="₫ 0"
                                readonly />
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="flex gap-2 px-3 py-2.5 border-t border-gray-100 bg-gray-50/60 rounded-b-lg">
                      <button
                        class="flex-1 py-1.5 px-2.5 rounded-md text-xs font-medium cursor-pointer border border-gray-200 bg-white text-gray-700 hover:bg-gray-100 active:scale-[0.98] transition"
                        id="discardBtn"
                        type="button">
                        Hủy
                      </button>
                      <button
                        class="flex-1 py-1.5 px-2.5 rounded-md text-xs font-medium cursor-pointer border-0 bg-red-600 text-white hover:bg-red-700 active:scale-[0.98] transition"
                        id="deleteBtn"
                        type="button"
                        style="background: var(--color-critical);">
                        Xóa
                      </button>
                      <button
                        class="flex-1 py-1.5 px-2.5 rounded-md text-xs font-medium cursor-pointer border-0 bg-gray-900 text-white hover:bg-gray-800 active:scale-[0.98] transition"
                        id="updateBtn"
                        type="button"
                        style="background: var(--color-bg-success);">
                        Cập nhật
                      </button>
                    </div>
                  </aside>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </main>

  <!-- Product Card Template -->
  <template id="productCardTemplate">
    <div class="product-card bg-white border border-gray-200 rounded-xl overflow-hidden cursor-pointer relative transition-[border-color] duration-200 hover:border-gray-300">
      <div class="thumb">
        <img data-ref="image" alt="" />
        <span class="status-indicator pulse" data-ref="statusDot"></span>
      </div>
      <div class="p-4">
        <h3 class="text-sm font-semibold text-gray-800 mb-2 overflow-hidden text-ellipsis whitespace-nowrap" data-ref="title"></h3>
        <div class="text-base font-semibold text-gray-800 mb-3" data-ref="price"></div>
        <div class="flex justify-between text-xs text-gray-600 mb-2">
          <div class="flex flex-col gap-0.5">
            <span class="text-gray-400">Tồn kho</span>
            <span class="font-semibold text-gray-700" data-ref="stock"></span>
          </div>
          <div class="flex flex-col gap-0.5">
            <span class="text-gray-400">Đã bán</span>
            <span class="font-semibold text-gray-700" data-ref="sold"></span>
          </div>
        </div>
        <div class="flex gap-1 mt-2" data-ref="colors">
          <!-- Color dots will be added here -->
        </div>
      </div>
    </div>
  </template>

  <script>
    // Context path for resources
    window.APP_CTX = '${env}';

    window.__CATEGORIES__ = [
      <c:forEach var="category" items="${categories}" varStatus="status">
        { id: ${category.id}, name: "${fn:escapeXml(category.name)}" }<c:if test="${!status.last}">,</c:if>
      </c:forEach>
    ];

    window.__PRODUCTS__ = [
      <c:forEach var="product" items="${products}" varStatus="status">
      {
        id: ${product.id},
        name: "${fn:escapeXml(product.name)}",
        description: "${fn:escapeXml(product.description)}",
        price: ${product.price},
        salePrice: ${product.salePrice != null ? product.salePrice : 0},
        stock: ${product.totalStock},
        sold: 0,
        category: ${product.category.id},
        categoryName: "${fn:escapeXml(product.category.name)}",
        colors: [
          <c:forEach var="color" items="${product.colors}" varStatus="colorStatus">
            "${color.hexCode}"<c:if test="${!colorStatus.last}">,</c:if>
          </c:forEach>
        ],
        image: <c:choose>
          <c:when test="${product.mainImage != null}">
            "${product.mainImage.path}"
          </c:when>
          <c:when test="${not empty product.images}">
            "${product.images[0].path}"
          </c:when>
          <c:otherwise>
            "${env}/images/placeholder.svg"
          </c:otherwise>
        </c:choose>,
        active: ${product.productStatus.name() == 'ACTIVE'}
      }<c:if test="${!status.last}">,</c:if>
      </c:forEach>
    ];
  </script>

  <script src="${env}/administrator/assets/libs/jquery/dist/jquery.min.js"></script>
  <script src="${env}/administrator/assets/libs/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${env}/administrator/assets/libs/perfect-scrollbar/dist/perfect-scrollbar.jquery.min.js"></script>
  <script src="${env}/administrator/dist/js/sidebarmenu.js"></script>
  <script src="${env}/js/admin/pages/admin-product-list.js"></script>
</body>

</html>
