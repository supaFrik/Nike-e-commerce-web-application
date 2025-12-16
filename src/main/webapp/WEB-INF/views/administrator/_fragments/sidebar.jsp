<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<aside id="application-sidebar-brand"
  class="hs-overlay hs-overlay-open:translate-x-0 -translate-x-full transform hidden xl:block xl:translate-x-0 xl:end-auto xl:bottom-0 fixed top-0 with-vertical h-screen z-[999] flex-shrink-0 border-r-[1px] w-[270px] border-gray-400/20 bg-white left-sidebar transition-all duration-300">
  <style>
    #application-sidebar-brand .simplebar-scrollbar::before {
      background: rgba(0,0,0,0.25);
      border-radius: 9999px;
    }
    #application-sidebar-brand .simplebar-track.simplebar-vertical {
      width: 6px; /* slim track */
    }
    #application-sidebar-brand .simplebar-track.simplebar-horizontal { height: 6px; }
    #application-sidebar-brand .simplebar-track { background: transparent; }
    #application-sidebar-brand:hover .simplebar-scrollbar::before {
      background: rgba(0,0,0,0.35);
    }
  </style>
  <div class="p-5">
    <a href="${env}/admin" class="text-nowrap">
      <img src="${env}/images/logo-admin.png" alt="Nike Admin" />
    </a>
  </div>
  <div class="scroll-sidebar" data-simplebar data-simplebar-auto-hide="true" style="max-height: calc(100vh - 220px); overflow-y: auto;">
    <div class="px-6 mt-8">
      <nav class="w-full flex flex-col sidebar-nav" role="navigation" aria-label="Sidebar">
        <ul id="sidebarnav" class="text-dark text-sm" style="padding: 0;">
          <li class="text-xs font-bold pb-4"><i class="ti ti-dots nav-small-cap-icon text-lg hidden text-center"></i><span>MENU CHÍNH</span></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin"><i class="ti ti-layout-dashboard text-xl"></i><span>Bảng điều khiển</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/orders"><i class="ti ti-shopping-cart text-xl"></i><span>Quản lý đơn hàng</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/customers"><i class="ti ti-users text-xl"></i><span>Khách hàng</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/coupons"><i class="ti ti-ticket text-xl"></i><span>Mã giảm giá</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/categories"><i class="ti ti-category text-xl"></i><span>Danh mục</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/transactions"><i class="ti ti-credit-card text-xl"></i><span>Giao dịch</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/brands"><i class="ti ti-brand-apple text-xl"></i><span>Thương hiệu</span></a></li>
          <li class="text-xs font-bold mb-4 mt-8"><i class="ti ti-dots nav-small-cap-icon text-lg hidden text-center"></i><span>SẢN PHẨM</span></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/product/add"><i class="ti ti-plus text-xl"></i><span>Thêm sản phẩm</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/product/media"><i class="ti ti-photo text-xl"></i><span>Thư viện sản phẩm</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/product/list"><i class="ti ti-list text-xl"></i><span>Danh sách sản phẩm</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/product/reviews"><i class="ti ti-star text-xl"></i><span>Đánh giá sản phẩm</span></a></li>
          <li class="text-xs font-bold mb-4 mt-8"><i class="ti ti-dots nav-small-cap-icon text-lg hidden text-center"></i><span>QUẢN TRỊ</span></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/roles"><i class="ti ti-user-shield text-xl"></i><span>Vai trò quản trị</span></a></li>
          <li class="sidebar-item"><a class="sidebar-link gap-3 py-3 px-3 rounded-md w-full flex items-center hover:text-primary hover:bg-primary/15" href="${env}/admin/authority"><i class="ti ti-lock text-xl"></i><span>Phân quyền</span></a></li>
        </ul>
      </nav>
    </div>
  </div>
  <div class="p-6">
    <div class="card bg-gradient-to-r from-blue-500 to-blue-600 shadow-md">
      <div class="card-body text-white">
        <div class="flex items-center justify-between mb-3">
          <h5 class="text-base font-semibold">E-commerce Admin</h5>
          <button class="ti ti-x text-white text-xl"></button>
        </div>
        <p class="text-xs text-white/90 mb-4">Quản lý cửa hàng hiệu quả</p>
        <button class="w-full bg-white text-blue-600 py-2 px-4 rounded-md font-semibold text-sm hover:bg-blue-50 transition-colors">Tìm hiểu thêm</button>
      </div>
    </div>
  </div>
</aside>
