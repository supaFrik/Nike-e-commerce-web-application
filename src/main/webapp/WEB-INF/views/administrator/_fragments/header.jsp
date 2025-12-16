<%@ include file="/WEB-INF/views/common/variables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Header Fragment -->
<header class="full-container w-full text-sm py-3 px-5 sticky top-0 z-30" style="background: white;">
  <nav class="w-full flex items-center justify-between" aria-label="Toàn trang">
    <ul class="icon-nav flex items-center gap-4">
      <li class="relative xl:hidden">
        <a class="text-xl icon-hover cursor-pointer text-heading" id="headerCollapse"
           data-hs-overlay="#application-sidebar-brand" aria-controls="application-sidebar-brand"
           aria-label="Chuyển đổi điều hướng" href="javascript:void(0)">
          <i class="ti ti-menu-2 relative z-1"></i>
        </a>
      </li>
      <li class="relative">
        <a class="text-xl icon-hover cursor-pointer text-heading" href="javascript:void(0)">
          <i class="ti ti-search relative z-1"></i>
        </a>
      </li>
      <li class="relative">
        <div class="hs-dropdown relative inline-flex [--placement:bottom-left] sm:[--trigger:hover]">
          <a class="hs-dropdown-toggle inline-flex items-center gap-2 text-heading cursor-pointer" href="javascript:void(0)">
            <span class="text-sm font-medium">Ứng dụng</span>
            <i class="ti ti-chevron-down text-sm"></i>
          </a>
          <div class="hs-dropdown-menu transition-[opacity,margin] rounded-md duration hs-dropdown-open:opacity-100 opacity-0 mt-2 min-w-max w-[200px] hidden bg-white shadow-md">
            <ul class="list-none flex flex-col">
              <li><a href="#" class="py-2 px-4 block hover:bg-primary/10"><p class="text-sm text-dark">Ứng dụng 1</p></a></li>
              <li><a href="#" class="py-2 px-4 block hover:bg-primary/10"><p class="text-sm text-dark">Ứng dụng 2</p></a></li>
            </ul>
          </div>
        </div>
      </li>
      <li class="relative"><a class="text-sm font-medium text-heading hover:text-primary cursor-pointer" href="${env}/admin/chat">Trò chuyện</a></li>
      <li class="relative"><a class="text-sm font-medium text-heading hover:text-primary cursor-pointer" href="${env}/admin/calendar">Lịch</a></li>
      <li class="relative"><a class="text-sm font-medium text-heading hover:text-primary cursor-pointer" href="${env}/admin/email">Email</a></li>
    </ul>
    <div class="flex items-center gap-4">
      <a class="text-xl icon-hover cursor-pointer text-heading" href="javascript:void(0)"><i class="ti ti-moon relative z-1"></i></a>
      <div class="hs-dropdown relative inline-flex [--placement:bottom-right] sm:[--trigger:hover]">
        <a class="hs-dropdown-toggle inline-flex icon-hover text-heading cursor-pointer" href="javascript:void(0)">
          <img src="${env}/images/placeholder.svg" alt="Ngôn ngữ" class="w-5 h-5 rounded-full" />
        </a>
        <div class="hs-dropdown-menu transition-[opacity,margin] rounded-md duration hs-dropdown-open:opacity-100 opacity-0 mt-2 min-w-max w-[150px] hidden bg-white shadow-md">
          <ul class="list-none flex flex-col">
            <li>
              <a href="#" class="py-2 px-4 block hover:bg-primary/10 flex items-center gap-2">
                <img src="${env}/images/placeholder.svg" alt="Tiếng Anh" class="w-4 h-4" />
                <span class="text-sm text-dark">Tiếng Anh</span>
              </a>
            </li>
            <li>
              <a href="#" class="py-2 px-4 block hover:bg-primary/10 flex items-center gap-2">
                <img src="${env}/images/placeholder.svg" alt="Tiếng Tây Ban Nha" class="w-4 h-4" />
                <span class="text-sm text-dark">Tiếng Tây Ban Nha</span>
              </a>
            </li>
          </ul>
        </div>
      </div>
      <a class="relative text-xl icon-hover cursor-pointer text-heading" href="javascript:void(0)">
        <i class="ti ti-shopping-cart relative z-1"></i>
        <div class="absolute inline-flex items-center justify-center text-white text-[10px] font-semibold bg-red-500 w-4 h-4 rounded-full -top-[6px] -right-[8px]">2</div>
      </a>
      <div class="hs-dropdown relative inline-flex [--placement:bottom-right] sm:[--trigger:hover]">
        <a class="relative hs-dropdown-toggle inline-flex icon-hover text-dark cursor-pointer" href="javascript:void(0)">
          <i class="ti ti-bell-ringing text-xl relative z-[1]"></i>
          <div class="absolute inline-flex items-center justify-center text-white text-[10px] font-semibold bg-blue-500 w-4 h-4 rounded-full -top-[6px] -right-[8px]">4</div>
        </a>
        <div class="hs-dropdown-menu transition-[opacity,margin] rounded-md duration hs-dropdown-open:opacity-100 opacity-0 mt-2 min-w-max w-[300px] hidden bg-white shadow-md" aria-labelledby="hs-dropdown-custom-icon-trigger">
          <div>
            <h3 class="text-dark font-semibold text-base px-6 py-3">Thông báo</h3>
            <ul class="list-none flex flex-col">
              <li><a href="#" class="py-3 px-6 block hover:bg-primary/15"><p class="text-sm text-dark font-semibold">Roman đã tham gia đội!</p><p class="text-xs text-gray-500 font-medium">Chúc mừng anh ấy</p></a></li>
              <li><a href="#" class="py-3 px-6 block hover:bg-primary/15"><p class="text-sm text-dark font-semibold">Bạn có tin nhắn mới</p><p class="text-xs text-gray-500 font-medium">Salma đã gửi tin nhắn cho bạn</p></a></li>
              <li><a href="#" class="py-3 px-6 block hover:bg-primary/15"><p class="text-sm text-dark font-semibold">Thanh toán mới đã nhận</p><p class="text-xs text-gray-500 font-medium">Kiểm tra doanh thu của bạn</p></a></li>
              <li><a href="#" class="py-3 px-6 block hover:bg-primary/15"><p class="text-sm text-dark font-semibold">Jolly đã hoàn thành nhiệm vụ</p><p class="text-xs text-gray-500 font-medium">Giao nhiệm vụ mới cho cô ấy</p></a></li>
            </ul>
          </div>
        </div>
      </div>
      <div class="m-1 hs-dropdown [--trigger:hover] relative inline-flex">
        <img class="object-cover w-9 h-9 rounded-full cursor-pointer" src="${env}/images/placeholder.svg" alt="Hồ sơ" aria-hidden="true">
        <div class="hs-dropdown-menu py-3 transition-[opacity,margin] duration hs-dropdown-open:opacity-100 opacity-0 hidden bg-white shadow-md rounded-lg mt-2 after:h-4 after:absolute after:-bottom-4 after:start-0 after:w-full before:h-4 before:absolute before:-top-4 before:start-0 before:w-full z-20 w-[200px]" role="menu">
          <div class="space-y-1">
            <a href="javascript:void(0)" class="flex gap-2 items-center px-4 py-2.5 hover:bg-primary/10"><i class="ti ti-user text-gray-500 text-xl"></i><p class="text-sm text-dark">Hồ sơ của tôi</p></a>
            <a href="javascript:void(0)" class="flex gap-2 items-center px-4 py-2.5 hover:bg-primary/10"><i class="ti ti-mail text-gray-500 text-xl"></i><p class="text-sm text-dark">Tài khoản của tôi</p></a>
            <a href="javascript:void(0)" class="flex gap-2 items-center px-4 py-2.5 hover:bg-primary/10"><i class="ti ti-list-check text-gray-500 text-xl"></i><p class="text-sm text-dark">Nhiệm vụ của tôi</p></a>
            <div class="px-4 mt-[7px] grid">
              <a href="${env}/auth" class="btn-outline-primary w-full hover:bg-blue-700/80 hover:text-white">Đăng xuất</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</header>
