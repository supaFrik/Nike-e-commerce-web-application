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
  <link rel="shortcut icon" type="image/png" href="${env}/administrator/assets/images/logos/favicon.png" />
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
    rel="stylesheet" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@2.44.0/tabler-icons.min.css" />
  <!-- Core Css -->
  <link rel="stylesheet" href="${env}/css/admin/common/theme.css" />
  <link rel="stylesheet" href="${env}/css/common/base.css" />
  <link rel="stylesheet" href="${env}/css/admin/product-list/add-product.css" />
  <title>Thêm sản phẩm - Bảng điều khiển</title>
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
            <div class="flex justify-between items-center mb-6 bg-white p-5 rounded-sm">
              <div>
                <h1 class="text-2xl font-semibold text-gray-800">Thêm sản phẩm mới</h1>
                <p class="text-sm text-gray-500 mt-1">Tạo sản phẩm mới cho cửa hàng của bạn</p>
              </div>
              <div class="flex gap-3">
                <a href="${env}/admin/product/list" class="btn-outline-primary px-5 py-2">
                  Hủy
                </a>
                <button id="deleteProductBtn" class="btn px-5 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 pointer" <c:if test="${empty productDto.id}">disabled style="opacity:.5;cursor:not-allowed;"</c:if>>
                  Xóa sản phẩm
                </button>
                <button id="saveProductBtn" class="btn px-5 py-2 bg-black text-white rounded-lg hover:bg-gray-800 pointer">
                  Lưu sản phẩm
                </button>
              </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <!-- Left Column - Main Form -->
              <div class="lg:col-span-2 space-y-6">
                <!-- Basic Information -->
                <div class="card">
                  <div class="card-body mb-4">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">Thông tin cơ bản</h2>

                    <div class="space-y-4">
                      <div class="mb-2">
                        <label for="productName" class="block text-sm font-medium text-gray-700 mb-2">
                          Tên sản phẩm <span class="text-red-500" style="color: red">*</span>
                        </label>
                        <input
                          type="text"
                          id="productName"
                          name="productName"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="Nhập tên sản phẩm"
                          required
                        />
                      </div>

                      <div>
                        <label for="productDescription" class="block text-sm font-medium text-gray-700 mb-2">
                          Mô tả <span class="text-red-500" style="color: red">*</span>
                        </label>
                        <textarea
                          id="productDescription"
                          name="productDescription"
                          rows="5"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500 resize-vertical"
                          placeholder="Nhập mô tả sản phẩm"
                          required
                        ></textarea>
                      </div>

                      <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div>
                          <label for="productSKU" class="block text-sm font-medium text-gray-700 mb-2">
                            Mã SKU <span class="text-red-500" style="color: red">*</span>
                          </label>
                          <input
                            type="text"
                            id="productSKU"
                            name="productSKU"
                            class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                            placeholder="VD: PROD-001"
                            required
                          />
                        </div>

                        <div>
                          <label for="productBarcode" class="block text-sm font-medium text-gray-700 mb-2">
                            Mã vạch
                          </label>
                          <input
                            type="text"
                            id="productBarcode"
                            name="productBarcode"
                            class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                            placeholder="VD: 123456789012"
                          />
                        </div>
                      </div>

                      <!-- Color Variants -->
                      <div class="mt-4">
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          Phiên bản màu <span class="text-red-500" style="color: red">*</span>
                        </label>
                        <div class="flex gap-2 mb-3">
                          <input
                            type="text"
                            id="colorInput"
                            class="flex-1 px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                            placeholder="Nhập tên màu (VD: Đỏ, Xanh, Đen)"
                          />
                          <button
                            type="button"
                            id="addColorBtn"
                            class="px-4 py-2.5 bg-black text-white rounded-lg text-sm hover:bg-gray-800 transition-colors"
                          >
                            Thêm màu
                          </button>
                        </div>
                        <div id="colorsList" class="flex flex-wrap gap-2">
                          <!-- Colors will be added here -->
                        </div>
                      </div>

                      <!-- Size Variants -->
                      <div class="mt-4">
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          Phiên bản kích thước (Không bắt buộc)
                        </label>

                        <div id="noColorsForSize" class="text-center py-4 text-gray-500 text-sm">
                          <p>Vui lòng thêm ít nhất một màu</p>
                        </div>

                        <div id="sizesInputSection" class="hidden">
                          <div class="flex gap-2 mb-3">
                            <select
                              id="colorForSize"
                              class="px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                            >
                              <option value="">Chọn màu</option>
                            </select>
                            <input
                              type="number"
                              id="sizeInput"
                              class="flex-1 px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                              placeholder="Nhập số kích thước (VD: 38, 39, 40, 41, 42)"
                              min="1"
                              step="1"
                              disabled
                            />
                            <button
                              type="button"
                              id="addSizeBtn"
                              class="px-4 py-2.5 bg-black text-white rounded-lg text-sm hover:bg-gray-800 transition-colors"
                              disabled
                            >
                              Thêm kích thước
                            </button>
                          </div>
                          <div id="sizesByColorContainer" class="space-y-3">
                            <!-- Sizes by color will be added here -->
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Pricing -->
                <div class="card">
                  <div class="card-body mb-4">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">Giá</h2>

                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      <div>
                        <label for="regularPrice" class="block text-sm font-medium text-gray-700 mb-2">
                          Giá thường <span class="text-red-500" style="color: red">*</span>
                        </label>
                        <div class="relative">
                          <span class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500">$</span>
                          <input
                            type="number"
                            id="regularPrice"
                            name="regularPrice"
                            class="w-full pl-8 pr-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                            placeholder="0.00"
                            step="0.01"
                            min="0"
                            required
                          />
                        </div>
                      </div>

                      <div>
                        <label for="salePrice" class="block text-sm font-medium text-gray-700 mb-2">
                          Giá khuyến mãi
                        </label>
                        <div class="relative">
                          <span class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500">$</span>
                          <input
                            type="number"
                            id="salePrice"
                            name="salePrice"
                            class="w-full pl-8 pr-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                            placeholder="0.00"
                            step="0.01"
                            min="0"
                          />
                        </div>
                      </div>

                      <div>
                        <label for="costPrice" class="block text-sm font-medium text-gray-700 mb-2">
                          Giá vốn
                        </label>
                        <div class="relative">
                          <span class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500">$</span>
                          <input
                            type="number"
                            id="costPrice"
                            name="costPrice"
                            class="w-full pl-8 pr-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                            placeholder="0.00"
                            step="0.01"
                            min="0"
                          />
                        </div>
                      </div>

                      <div>
                        <label for="taxRate" class="block text-sm font-medium text-gray-700 mb-2">
                          Thuế suất (%)
                        </label>
                        <input
                          type="number"
                          id="taxRate"
                          name="taxRate"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="0"
                          step="0.1"
                          min="0"
                          max="100"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Inventory -->
                <div class="card">
                  <div class="card-body mb-4">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">Tồn kho</h2>

                    <div id="noVariantsMessage" class="text-center py-8 text-gray-500">
                      <i class="ti ti-info-circle text-4xl mb-2"></i>
                      <p class="text-sm">Vui lòng thêm phiên bản màu và kích thước trước để quản lý tồn kho</p>
                    </div>

                    <div id="inventorySection" class="hidden">
                      <p class="text-sm text-gray-600 mb-4">Thiết lập số lượng tồn cho từng tổ hợp phiên bản</p>
                      <div id="variantStockContainer" class="space-y-4">
                        <!-- Variant stock inputs will be added here -->
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Shipping -->
                <!-- <div class="card">
                  <div class="card-body mb-4">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">Shipping</h2>

                    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                      <div>
                        <label for="weight" class="block text-sm font-medium text-gray-700 mb-2">
                          Weight (kg)
                        </label>
                        <input
                          type="number"
                          id="weight"
                          name="weight"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="0.00"
                          step="0.01"
                          min="0"
                        />
                      </div>

                      <div>
                        <label for="length" class="block text-sm font-medium text-gray-700 mb-2">
                          Length (cm)
                        </label>
                        <input
                          type="number"
                          id="length"
                          name="length"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="0"
                          min="0"
                        />
                      </div>

                      <div>
                        <label for="width" class="block text-sm font-medium text-gray-700 mb-2">
                          Width (cm)
                        </label>
                        <input
                          type="number"
                          id="width"
                          name="width"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="0"
                          min="0"
                        />
                      </div>

                      <div>
                        <label for="height" class="block text-sm font-medium text-gray-700 mb-2">
                          Height (cm)
                        </label>
                        <input
                          type="number"
                          id="height"
                          name="height"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="0"
                          min="0"
                        />
                      </div>
                    </div>
                  </div>
                </div> -->
              </div>

              <!-- Right Column - Additional Info -->
              <div class="lg:col-span-1 space-y-6">
                <!-- Product Images -->
                <div class="card">
                  <div class="card-body mb-4">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">Hình ảnh sản phẩm</h2>

                    <div id="noColorsMessage" class="text-center py-8 text-gray-500">
                      <i class="ti ti-info-circle text-4xl mb-2"></i>
                      <p class="text-sm">Vui lòng thêm ít nhất một phiên bản màu</p>
                    </div>

                    <div id="imageUploadSection" class="hidden">
                      <!-- Color Selection for Upload -->
                      <div class="mb-4">
                        <label for="selectColorForUpload" class="block text-sm font-medium text-gray-700 mb-2">
                          Chọn màu để tải ảnh <span class="text-red-500">*</span>
                        </label>
                        <select
                          id="selectColorForUpload"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                        >
                          <option value="">Chọn màu</option>
                        </select>
                      </div>

                      <!-- Upload Area -->
                      <div class="image-upload-area" id="imageUploadArea">
                        <div class="image-preview" id="imageUploadPreview">
                          <i class="ti ti-photo text-5xl text-gray-400"></i>
                          <p class="text-sm text-gray-500 mt-2">Nhấn để tải ảnh lên</p>
                          <p class="text-xs text-gray-400 mt-1">PNG, JPG tối đa 5MB mỗi ảnh</p>
                        </div>
                        <input
                          type="file"
                          id="productImages"
                          name="productImages"
                          class="hidden"
                          accept="image/*"
                          multiple
                          disabled
                        />
                      </div>

                      <!-- Uploaded Images by Color -->
                      <div class="mt-4">
                        <p class="text-xs text-gray-600 mb-2">Nhấp vào ảnh để đặt ảnh đại diện cho màu đó</p>
                        <div id="imagesByColorContainer">
                          <!-- Images grouped by color will be shown here -->
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Category -->
                <div class="card">
                  <div class="card-body mb-4">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">Danh mục</h2>

                    <div class="space-y-4">
                      <div class="mb-2">
                        <label for="category" class="block text-sm font-medium text-gray-700 mb-2">
                          Danh mục sản phẩm <span class="text-red-500" style="color: red">*</span>
                        </label>
                        <select
                          id="category"
                          name="category"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          required
                        >
                          <option value="">Chọn danh mục</option>
                          <option value="electronics">Điện tử</option>
                          <option value="clothing">Quần áo</option>
                          <option value="accessories">Phụ kiện</option>
                          <option value="home">Nhà cửa & Vườn</option>
                          <option value="sports">Thể thao & Ngoài trời</option>
                          <option value="books">Sách</option>
                          <option value="toys">Đồ chơi & Trò chơi</option>
                          <option value="other">Khác</option>
                        </select>
                      </div>

                      <div class="mb-2">
                        <label for="brand" class="block text-sm font-medium text-gray-700 mb-2">
                          Thương hiệu
                        </label>
                        <input
                          type="text"
                          id="brand"
                          name="brand"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="Nhập tên thương hiệu"
                        />
                      </div>

                      <div class="mb-2">
                        <label for="tags" class="block text-sm font-medium text-gray-700 mb-2">
                          Thẻ
                        </label>
                        <input
                          type="text"
                          id="tags"
                          name="tags"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          placeholder="VD: mới, nổi bật, giảm giá"
                        />
                        <p class="text-xs text-gray-500 mt-1">Ngăn cách thẻ bằng dấu phẩy</p>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Status -->
                <div class="card">
                  <div class="card-body mb-4">
                    <h2 class="text-lg font-semibold text-gray-800 mb-4">Trạng thái</h2>

                    <div class="space-y-4">
                      <div class="mb-2">
                        <label for="status" class="block text-sm font-medium text-gray-700 mb-2">
                          Trạng thái sản phẩm <span class="text-red-500" style="color: red">*</span>
                        </label>
                        <select
                          id="status"
                          name="status"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                          required
                        >
                          <option value="active">Đang bán</option>
                          <option value="draft">Bản nháp</option>
                          <option value="archived">Lưu trữ</option>
                        </select>
                      </div>

                      <div class="mb-2">
                        <label for="visibility" class="block text-sm font-medium text-gray-700 mb-2">
                          Hiển thị
                        </label>
                        <select
                          id="visibility"
                          name="visibility"
                          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg text-sm focus:outline-none focus:border-blue-500"
                        >
                          <option value="public">Công khai</option>
                          <option value="private">Riêng tư</option>
                          <option value="hidden">Ẩn</option>
                        </select>
                      </div>

                      <div>
                        <label class="flex items-center gap-2 cursor-pointer">
                          <input
                            type="checkbox"
                            id="featured"
                            name="featured"
                            class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                          />
                          <span class="text-sm text-gray-700">Đánh dấu là sản phẩm nổi bật</span>
                        </label>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </main>

  <script src="${env}/administrator/assets/libs/jquery/dist/jquery.min.js"></script>
  <script src="${env}/administrator/assets/libs/simplebar/dist/simplebar.min.js"></script>
  <script src="${env}/administrator/assets/libs/iconify-icon/dist/iconify-icon.min.js"></script>
  <script src="${env}/administrator/assets/libs/@preline/dropdown/index.js"></script>
  <script src="${env}/administrator/assets/libs/@preline/overlay/index.js"></script>
  <script src="${env}/administrator/assets/js/sidebarmenu.js"></script>
  <script src="${env}/js/admin/pages/product-add.js"></script>
  <script>
    (function(){
      document.addEventListener('DOMContentLoaded', function(){
        const delBtn = document.getElementById('deleteProductBtn');
        const id = '<c:out value="${productDto.id}"/>';
        if (delBtn && id) {
          delBtn.addEventListener('click', function(){
            if (!id) return;
            if (!confirm('Xóa sản phẩm này? Hành động không thể hoàn tác.')) return;
            fetch('${env}/admin/api/products/' + id, { method: 'DELETE' })
              .then(r => {
                if (!r.ok) return r.json().then(e => Promise.reject(e));
                return r.json();
              })
              .then(() => { window.location.href='${env}/admin/product/list'; })
              .catch(err => alert('Xóa thất bại: ' + (err.error || err.message || 'Lỗi không xác định')));
          });
        }
      });
    })();
  </script>
</body>
</html>
