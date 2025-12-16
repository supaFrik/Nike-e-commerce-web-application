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
  <link rel="stylesheet" href="${env}/css/admin/order/order-management.css" />
  <title>Quản lý đơn hàng - Bảng điều khiển</title>
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

        <!-- Page Content -->
        <div class="container mx-auto px-6 py-6" style = "padding-top: 24px">
          <!-- Page Title & Actions -->
          <div class="card mb-6">
            <div class="card-body">
              <div class="flex items-center justify-between gap-4">
                <div class="flex flex-col justify-start gap-2">
                  <h1 class="text-2xl md:text-3xl font-semibold text-gray-900">Đơn hàng</h1>
                  <button class="flex items-center gap-2 px-3 py-2 text-sm text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
                    type="button">
                    <i class="ti ti-calendar text-base"></i>
                    <span>
                      <c:choose>
                        <c:when test="${not empty dateRange}">${dateRange}</c:when>
                        <c:otherwise>Chọn khoảng thời gian</c:otherwise>
                      </c:choose>
                    </span>
                    <i class="ti ti-chevron-down text-base"></i>
                  </button>
                </div>

                <div class="flex items-center gap-3">
                  <button class="flex items-center gap-2 px-4 py-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                    type="button">
                    <i class="ti ti-download text-base"></i>
                    Xuất
                  </button>
                  <button class="flex items-center gap-2 px-4 py-2.5 text-sm text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                    type="button">
                    Thao tác
                    <i class="ti ti-chevron-down text-base"></i>
                  </button>
                  <button class="flex items-center gap-2 px-4 py-2.5 text-sm text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors font-medium"
                    type="button" onclick="window.location.href='${env}/admin/orders/create'">
                    Tạo đơn hàng
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Statistics Cards -->
          <div class="revenue-card gap-6 mb-6">
            <!-- Total Orders Card -->
            <div class="stat-card p-6">
              <div class="flex items-start justify-between mb-4 p-5">
                <div>
                  <p class="text-sm text-gray-600 mb-1">Tổng số đơn hàng</p>
                  <h3 class="text-3xl font-bold text-gray-900 mb-2">21 <span class="text-lg font-normal text-gray-400">-</span>
                  </h3>
                  <div class="flex items-center gap-1 text-sm">
                    <i class="ti ti-trending-up text-success text-base"></i>
                    <span class="text-trend-up font-semibold">25.2%</span>
                    <span class="text-gray-500">tuần trước</span>
                  </div>
                </div>
              </div>
              <div class="mt-4">
                <svg width="100%" height="60" viewBox="0 0 280 60" fill="none" xmlns="http://www.w3.org/2000/svg"
                  preserveAspectRatio="none">
                  <path d="M0 45 L40 35 L80 40 L120 25 L160 30 L200 20 L240 15 L280 10" stroke="#FCD34D" stroke-width="2"
                    fill="none" stroke-linecap="round" stroke-linejoin="round" />
                  <path d="M0 45 L40 35 L80 40 L120 25 L160 30 L200 20 L240 15 L280 10 L280 60 L0 60 Z"
                    fill="url(#gradient-yellow)" opacity="0.1" />
                  <defs>
                    <linearGradient id="gradient-yellow" x1="0" y1="0" x2="0" y2="60">
                      <stop offset="0%" stop-color="#FCD34D" stop-opacity="0.3" />
                      <stop offset="100%" stop-color="#FCD34D" stop-opacity="0" />
                    </linearGradient>
                  </defs>
                </svg>
              </div>
            </div>

            <!-- Order Items Over Time Card -->
            <div class="stat-card p-2">
              <div class="flex items-start justify-between mb-4 p-5">
                <div>
                  <p class="text-sm text-gray-600 mb-1">Sản phẩm theo thời gian</p>
                  <h3 class="text-3xl font-bold text-gray-900 mb-2">15 <span class="text-lg font-normal text-gray-400">-</span>
                  </h3>
                  <div class="flex items-center gap-1 text-sm">
                    <i class="ti ti-trending-up text-success text-base"></i>
                    <span class="text-trend-up font-semibold">18.2%</span>
                    <span class="text-gray-500">tuần trước</span>
                  </div>
                </div>
              </div>
              <div class="mt-4">
                <svg width="100%" height="60" viewBox="0 0 280 60" fill="none" xmlns="http://www.w3.org/2000/svg"
                  preserveAspectRatio="none">
                  <path d="M0 40 L40 38 L80 35 L120 30 L160 32 L200 28 L240 22 L280 18" stroke="#6EE7B7" stroke-width="2"
                    fill="none" stroke-linecap="round" stroke-linejoin="round" />
                  <path d="M0 40 L40 38 L80 35 L120 30 L160 32 L200 28 L240 22 L280 18 L280 60 L0 60 Z"
                    fill="url(#gradient-green)" opacity="0.1" />
                  <defs>
                    <linearGradient id="gradient-green" x1="0" y1="0" x2="0" y2="60">
                      <stop offset="0%" stop-color="#6EE7B7" stop-opacity="0.3" />
                      <stop offset="100%" stop-color="#6EE7B7" stop-opacity="0" />
                    </linearGradient>
                  </defs>
                </svg>
              </div>
            </div>

            <!-- Returns Orders Card -->
            <div class="stat-card p-6">
              <div class="flex items-start justify-between mb-4 p-5">
                <div>
                  <p class="text-sm text-gray-600 mb-1">Đơn hàng trả lại</p>
                  <h3 class="text-3xl font-bold text-gray-900 mb-2">0 <span class="text-lg font-normal text-gray-400">-</span>
                  </h3>
                  <div class="flex items-center gap-1 text-sm">
                    <i class="ti ti-trending-down text-critical text-base"></i>
                    <span class="text-trend-down font-semibold">-1.2%</span>
                    <span class="text-gray-500">tuần trước</span>
                  </div>
                </div>
              </div>
              <div class="mt-4">
                <svg width="100%" height="60" viewBox="0 0 280 60" fill="none" xmlns="http://www.w3.org/2000/svg"
                  preserveAspectRatio="none">
                  <path d="M0 20 L40 25 L80 22 L120 28 L160 30 L200 32 L240 35 L280 38" stroke="#FCA5A5" stroke-width="2"
                    fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-dasharray="4 4" />
                  <path d="M0 20 L40 25 L80 22 L120 28 L160 30 L200 32 L240 35 L280 38 L280 60 L0 60 Z"
                    fill="url(#gradient-red)" opacity="0.05" />
                  <defs>
                    <linearGradient id="gradient-red" x1="0" y1="0" x2="0" y2="60">
                      <stop offset="0%" stop-color="#FCA5A5" stop-opacity="0.2" />
                      <stop offset="100%" stop-color="#FCA5A5" stop-opacity="0" />
                    </linearGradient>
                  </defs>
                </svg>
              </div>
            </div>

            <!-- Fulfilled Orders Card -->
            <div class="stat-card p-6">
              <div class="flex items-start justify-between mb-4 p-5">
                <div>
                  <p class="text-sm text-gray-600 mb-1">Đơn hàng đã hoàn thành</p>
                  <h3 class="text-3xl font-bold text-gray-900 mb-2">12 <span class="text-lg font-normal text-gray-400">-</span>
                  </h3>
                  <div class="flex items-center gap-1 text-sm">
                    <i class="ti ti-trending-up text-success text-base"></i>
                    <span class="text-trend-up font-semibold">12.2%</span>
                    <span class="text-gray-500">tuần trước</span>
                  </div>
                </div>
              </div>
              <div class="mt-4">
                <svg width="100%" height="60" viewBox="0 0 280 60" fill="none" xmlns="http://www.w3.org/2000/svg"
                  preserveAspectRatio="none">
                  <path d="M0 42 L40 38 L80 40 L120 32 L160 28 L200 30 L240 24 L280 20" stroke="#6EE7B7" stroke-width="2"
                    fill="none" stroke-linecap="round" stroke-linejoin="round" />
                  <path d="M0 42 L40 38 L80 40 L120 32 L160 28 L200 30 L240 24 L280 20 L280 60 L0 60 Z"
                    fill="url(#gradient-teal)" opacity="0.1" />
                  <defs>
                    <linearGradient id="gradient-teal" x1="0" y1="0" x2="0" y2="60">
                      <stop offset="0%" stop-color="#6EE7B7" stop-opacity="0.3" />
                      <stop offset="100%" stop-color="#6EE7B7" stop-opacity="0" />
                    </linearGradient>
                  </defs>
                </svg>
              </div>
            </div>
          </div>

          <!-- Orders Table Card -->
          <div class="card">
            <div class="card-body">
              <!-- Filter Tabs -->
              <div class="mb-6">
                <div class="flex items-center gap-3 border-b border-gray-200">
                  <button class="tab-button active px-4 py-3 text-sm font-medium transition-all relative"
                    data-status="all">
                    Tất cả
                  </button>
                  <button class="tab-button px-4 py-3 text-sm font-medium transition-all relative"
                    data-status="unfulfilled">
                    Chưa giao
                  </button>
                  <button class="tab-button px-4 py-3 text-sm font-medium transition-all relative"
                    data-status="unpaid">
                    Chưa thanh toán
                  </button>
                  <button class="tab-button px-4 py-3 text-sm font-medium transition-all relative"
                    data-status="open">
                    Mở
                  </button>
                  <button class="tab-button px-4 py-3 text-sm font-medium transition-all relative"
                    data-status="closed">
                    Đã đóng
                  </button>
                  <button class="tab-button add-tab px-3 py-3 text-sm font-medium transition-all relative ml-auto"
                    type="button">
                    <i class="ti ti-circle-plus text-lg"></i>
                    Thêm
                  </button>
                </div>
              </div>

              <!-- Search and Actions Bar -->
              <div class="flex items-center justify-between mb-4 gap-3">
                <div class="flex-1 relative max-w-md">
                  <form method="get" action="${env}/admin/orders" class="w-full">
                    <input name="q" value="${param.q}" type="text" placeholder="Tìm kiếm đơn hàng..."
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      aria-label="Tìm kiếm đơn hàng">
                  </form>
                </div>
                <div class="flex items-center gap-2">
                  <button class="p-2 hover:bg-gray-100 rounded-lg transition-colors" title="Bộ lọc" type="button">
                    <i class="ti ti-adjustments text-xl text-gray-600"></i>
                  </button>
                  <button class="p-2 hover:bg-gray-100 rounded-lg transition-colors" title="Sắp xếp" type="button">
                    <i class="ti ti-arrows-sort text-xl text-gray-600"></i>
                  </button>
                  <button class="p-2 hover:bg-gray-100 rounded-lg transition-colors" title="Khác" type="button">
                    <i class="ti ti-dots-vertical text-xl text-gray-600"></i>
                  </button>
                </div>
              </div>

              <!-- Table -->
              <div class="overflow-x-auto">
                <table class="w-full">
                  <thead>
                    <tr class="border-b border-gray-200" style="background-color: rgb(247, 247, 247);">
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn tất cả">
                      </th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">
                        Mã đơn
                      </th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">
                        Ngày
                        <i class="ti ti-chevron-down inline text-base" aria-hidden="true"></i>
                      </th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">Khách hàng</th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">Thanh toán</th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">Tổng</th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">Giao hàng</th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">Sản phẩm</th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">Trạng thái</th>
                      <th class="text-left py-3 px-4 text-sm font-semibold text-gray-700" scope="col">Thao tác</th>
                    </tr>
                  </thead>
                  <tbody>
                    <!-- Order Row 1 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1002</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">11 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Wade Warren</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Pending</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$20.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">2 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Unfulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 2 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1004</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">13 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Esther Howard</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Success</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$22.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">3 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Fulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 3 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1007</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">15 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Jenny Wilson</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Pending</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$25.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">1 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Unfulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 4 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1010</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">17 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Cody Fisher</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Success</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$30.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">4 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Fulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 5 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1013</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">19 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Arlene McCoy</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Pending</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$28.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">2 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Unfulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 6 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1016</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">21 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Ronald Richards</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Success</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$35.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">5 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Fulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 7 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1019</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">23 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Savannah Nguyen</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Pending</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$40.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">3 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Unfulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 8 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1022</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">25 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Marvin McKinney</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Success</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$45.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">6 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--completed">Fulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <!-- Order Row 9 -->
                    <tr class="border-b border-gray-100 hover:bg-gray-50">
                      <td class="py-4 px-4">
                        <input type="checkbox" class="w-4 h-4 rounded border-gray-300" aria-label="Chọn đơn hàng">
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">#1025</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">27 Feb, 2024</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-900">Esther Howard</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Pending</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm font-medium text-gray-900">$50.00</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">N/A</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="text-sm text-gray-600">2 Items</span>
                      </td>
                      <td class="py-4 px-4">
                        <span class="badge badge--delivery">Unfulfilled</span>
                      </td>
                      <td class="py-4 px-4">
                        <div class="flex items-center gap-2">
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Sao chép">
                            <i class="ti ti-copy text-gray-600"></i>
                          </button>
                          <button class="p-1.5 hover:bg-gray-100 rounded" type="button" title="Xóa">
                            <i class="ti ti-trash text-gray-600"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Pagination (optional if provided by backend) -->
              <c:if test="${not empty page}">
                <div class="flex flex-col sm:flex-row justify-between items-center gap-3 py-4 border-t border-gray-100">
                  <div class="text-sm text-gray-600">
                    Hiển thị
                    <span id="pageStart">${page.number * page.size + 1}</span>–
                    <span id="pageEnd">${page.number * page.size + fn:length(orders)}</span>
                    trong tổng <span id="totalItems">${page.totalElements}</span> kết quả
                  </div>
                  <div class="flex gap-1 items-center" aria-label="Phân trang đơn hàng">
                    <a href="${env}/admin/orders?page=${page.number - 1}&size=${page.size}"
                      class="w-9 h-9 flex items-center justify-center bg-white rounded-md text-sm text-gray-700 hover:bg-gray-50 ${page.first ? 'pointer-events-none opacity-40' : ''}">
                      <i class="ti ti-chevron-left"></i>
                    </a>
                    <!-- Page numbers could be rendered here -->
                    <a href="${env}/admin/orders?page=${page.number + 1}&size=${page.size}"
                      class="w-9 h-9 flex items-center justify-center bg-white rounded-md text-sm text-gray-700 hover:bg-gray-50 ${page.last ? 'pointer-events-none opacity-40' : ''}">
                      <i class="ti ti-chevron-right"></i>
                    </a>
                  </div>
                </div>
              </c:if>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>

  <script src="${env}/libs/jquery/jquery.min.js"></script>
  <script src="${env}/libs/simplebar/simplebar.min.js"></script>
  <script src="${env}/libs/iconify/iconify-icon.min.js"></script>
  <script src="${env}/libs/preline/dropdown.min.js"></script>
  <script src="${env}/libs/preline/overlay.min.js"></script>
  <script src="${env}/js/admin/common/sidebarmenu.js"></script>
  <script src="${env}/js/admin/pages/order-management.js"></script>

  <!-- Tab functionality: keep minimal here, main logic is in order-management.js -->
  <script>
    document.addEventListener('DOMContentLoaded', function () {
      const tabButtons = document.querySelectorAll('.tab-button:not(.add-tab)');
      tabButtons.forEach(button => {
        button.addEventListener('click', function () {
          tabButtons.forEach(btn => btn.classList.remove('active'));
          this.classList.add('active');
          const status = this.getAttribute('data-status');
          const url = new URL(window.location.href);
          url.searchParams.set('status', status);
          window.location.href = url.toString();
        });
      });
      const addTab = document.querySelector('.tab-button.add-tab');
      if (addTab) {
        addTab.addEventListener('click', function () {
          // Placeholder: open custom filter modal (implemented in order-management.js)
          console.log('Open custom filter creator');
        });
      }
      // Delete order (AJAX handled in order-management.js); fallback simple confirm
      document.querySelectorAll('.js-delete-order').forEach(btn => {
        btn.addEventListener('click', function () {
          const id = this.getAttribute('data-id');
          if (confirm('Bạn có chắc muốn xóa đơn hàng #' + id + ' ?')) {
            // Fallback navigation; prefer AJAX in order-management.js
            window.location.href = `${env}/admin/orders/delete?id=${id}`;
          }
        });
      });
    });
  </script>
</body>

</html>
