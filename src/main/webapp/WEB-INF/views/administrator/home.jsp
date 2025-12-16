<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/views/common/variables.jsp" %>

<!DOCTYPE html>
<html dir="ltr" lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- Favicon icon-->
    <link rel="shortcut icon" type="image/png" href="https://via.placeholder.com/32x32?text=Logo" />
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
        rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@2.44.0/tabler-icons.min.css" />
    <!-- Core Css -->
    <link rel="stylesheet" href="${env}/css/admin/common/theme.css" />
    <!-- Swiper CSS for alternative carousel -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.css" />
    <title>Nike Admin - Bảng điều khiển</title>
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

                <!-- Main Content -->
                <main class="h-full overflow-y-auto max-w-full pt-4">
                    <div class="container full-container py-5 flex flex-col gap-6">
                        <div class="grid grid-cols-1 lg:grid-cols-3 lg:gap-x-6 gap-x-0 lg:gap-y-0 gap-y-6">
                            <div class="col-span-2">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="sm:flex block justify-between mb-5">
                                            <h4 class="text-dark text-lg font-semibold sm:mb-0 mb-2">
                                                Tổng quan bán hàng
                                            </h4>
                                            <select name="period" id="period"
                                                class="border-gray-400/20 text-gray-500 rounded-md text-sm border-[1px] focus:ring-0 sm:w-auto w-full">
                                                <option value="mar">Tháng 3, 2025</option>
                                                <option value="apr">Tháng 4, 2025</option>
                                                <option value="may">Tháng 5, 2025</option>
                                                <option value="jun">Tháng 6, 2025</option>
                                            </select>
                                        </div>
                                        <div id="chart"></div>
                                    </div>
                                </div>
                            </div>

                            <div class="flex flex-col gap-6">
                                <div class="card">
                                    <div class="card-body">
                                        <h4 class="text-dark text-lg font-semibold mb-5">
                                            Phân tích theo năm
                                        </h4>
                                        <div class="flex gap-6 items-center justify-between">
                                            <div class="flex flex-col gap-4">
                                                <h3 class="text-[21px] font-semibold text-dark">
                                                    $36,358
                                                </h3>
                                                <div class="flex items-center gap-1">
                                                    <span
                                                        class="flex items-center justify-center w-5 h-5 rounded-full bg-success/20">
                                                        <i class="ti ti-arrow-up-left text-success"></i>
                                                    </span>
                                                    <p class="text-dark text-sm font-normal">+9%</p>
                                                    <p class="text-gray-500 text-sm font-normal text-nowrap">
                                                        năm trước
                                                    </p>
                                                </div>
                                                <div class="flex gap-3">
                                                    <div class="flex gap-2 items-center">
                                                        <span class="w-2 h-2 rounded-full bg-primary"></span>
                                                        <p class="text-gray-500 font-normal text-xs">
                                                            2024
                                                        </p>
                                                    </div>
                                                    <div class="flex gap-2 items-center">
                                                        <span class="w-2 h-2 rounded-full bg-gray-500/20"></span>
                                                        <p class="text-gray-500 font-normal text-xs">
                                                            2025
                                                        </p>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="flex items-center">
                                                <div id="breakup"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="card">
                                    <div class="card-body">
                                        <div class="flex gap-6 items-center justify-between">
                                            <div class="flex flex-col gap-5">
                                                <h4 class="text-dark text-lg font-semibold">
                                                    Thu nhập hàng tháng
                                                </h4>
                                                <div class="flex flex-col gap-[18px]">
                                                    <h3 class="text-[21px] font-semibold text-dark">
                                                        $6,820
                                                    </h3>
                                                    <div class="flex items-center gap-1">
                                                        <span
                                                            class="flex items-center justify-center w-5 h-5 rounded-full bg-error/20">
                                                            <i class="ti ti-arrow-down-right text-error"></i>
                                                        </span>
                                                        <p class="text-dark text-sm font-normal">+9%</p>
                                                        <p class="text-gray-500 text-sm font-normal">
                                                            năm trước
                                                        </p>
                                                    </div>
                                                </div>
                                            </div>

                                            <div
                                                class="w-11 h-11 flex justify-center items-center rounded-full bg-info text-white self-start">
                                                <i class="ti ti-currency-dollar text-xl"></i>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="earning"></div>
                                </div>
                            </div>
                        </div>
                        <div class="grid grid-cols-1 lg:grid-cols-3 lg:gap-x-6 gap-x-0 lg:gap-y-0 gap-y-6">
                            <div class="card">
                                <div class="card-body">
                                    <h4 class="text-dark text-lg font-semibold mb-6">
                                        Giao dịch gần đây
                                    </h4>
                                    <ul class="timeline-widget relative">
                                        <li class="timeline-item flex relative overflow-hidden min-h-[70px]">
                                            <div
                                                class="timeline-time text-dark text-sm min-w-[90px] py-[6px] pr-4 text-end">
                                                9:30 am
                                            </div>
                                            <div class="timeline-badge-wrap flex flex-col items-center">
                                                <div
                                                    class="timeline-badge w-3 h-3 rounded-full shrink-0 bg-transparent border-2 border-primary my-[10px]">
                                                </div>
                                                <div class="timeline-badge-border block h-full w-[1px] bg-gray-100">
                                                </div>
                                            </div>
                                            <div class="timeline-desc py-[6px] px-4">
                                                <p class="text-dark text-sm font-normal">
                                                    Thanh toán nhận từ John Doe là $385.90
                                                </p>
                                            </div>
                                        </li>
                                        <li class="timeline-item flex relative overflow-hidden min-h-[70px]">
                                            <div
                                                class="timeline-time text-dark min-w-[90px] py-[6px] text-sm pr-4 text-end">
                                                10:00 am
                                            </div>
                                            <div class="timeline-badge-wrap flex flex-col items-center">
                                                <div
                                                    class="timeline-badge w-3 h-3 rounded-full shrink-0 bg-transparent border-2 border-secondary my-[10px]">
                                                </div>
                                                <div class="timeline-badge-border block h-full w-[1px] bg-gray-100">
                                                </div>
                                            </div>
                                            <div class="timeline-desc py-[6px] px-4 text-sm">
                                                <p class="text-dark font-semibold">
                                                    Ghi nhận đơn hàng mới
                                                </p>
                                                <a href="javascript:void('')" class="text-primary">#ML-3467</a>
                                            </div>
                                        </li>

                                        <li class="timeline-item flex relative overflow-hidden min-h-[70px]">
                                            <div
                                                class="timeline-time text-dark min-w-[90px] text-sm py-[6px] pr-4 text-end">
                                                12:00 am
                                            </div>
                                            <div class="timeline-badge-wrap flex flex-col items-center">
                                                <div
                                                    class="timeline-badge w-3 h-3 rounded-full shrink-0 bg-transparent border-2 border-success my-[10px]">
                                                </div>
                                                <div class="timeline-badge-border block h-full w-[1px] bg-gray-100">
                                                </div>
                                            </div>
                                            <div class="timeline-desc py-[6px] px-4">
                                                <p class="text-dark text-sm font-normal">
                                                    Thanh toán là $64.95 cho Michael
                                                </p>
                                            </div>
                                        </li>

                                        <li class="timeline-item flex relative overflow-hidden min-h-[70px]">
                                            <div
                                                class="timeline-time text-dark min-w-[90px] text-sm py-[6px] pr-4 text-end">
                                                9:30 am
                                            </div>
                                            <div class="timeline-badge-wrap flex flex-col items-center">
                                                <div
                                                    class="timeline-badge w-3 h-3 rounded-full shrink-0 bg-transparent border-2 border-warning my-[10px]">
                                                </div>
                                                <div class="timeline-badge-border block h-full w-[1px] bg-gray-100">
                                                </div>
                                            </div>
                                            <div class="timeline-desc py-[6px] px-4 text-sm">
                                                <p class="text-dark font-semibold">
                                                    Ghi nhận đơn hàng mới
                                                </p>
                                                <a href="javascript:void('')" class="text-primary">#ML-3467</a>
                                            </div>
                                        </li>

                                        <li class="timeline-item flex relative overflow-hidden min-h-[70px]">
                                            <div
                                                class="timeline-time text-dark text-sm min-w-[90px] py-[6px] pr-4 text-end">
                                                9:30 am
                                            </div>
                                            <div class="timeline-badge-wrap flex flex-col items-center">
                                                <div
                                                    class="timeline-badge w-3 h-3 rounded-full shrink-0 bg-transparent border-2 border-error my-[10px]">
                                                </div>
                                                <div class="timeline-badge-border block h-full w-[1px] bg-gray-100">
                                                </div>
                                            </div>
                                            <div class="timeline-desc py-[6px] px-4">
                                                <p class="text-dark text-sm font-semibold">
                                                    Ghi nhận sản phẩm mới
                                                </p>
                                            </div>
                                        </li>
                                        <li class="timeline-item flex relative overflow-hidden">
                                            <div
                                                class="timeline-time text-dark text-sm min-w-[90px] py-[6px] pr-4 text-end">
                                                12:00 am
                                            </div>
                                            <div class="timeline-badge-wrap flex flex-col items-center">
                                                <div
                                                    class="timeline-badge w-3 h-3 rounded-full shrink-0 bg-transparent border-2 border-success my-[10px]">
                                                </div>
                                                <div class="timeline-badge-border block h-full w-[1px] bg-gray-100">
                                                </div>
                                            </div>
                                            <div class="timeline-desc py-[6px] px-4">
                                                <p class="text-dark text-sm font-normal">
                                                    Thanh toán hoàn tất
                                                </p>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div class="col-span-2">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="sm:flex block justify-between mb-5">
                                            <div>
                                                <h4 class="text-dark text-lg font-semibold sm:mb-0 mb-2">
                                                    Nhân viên xuất sắc
                                                </h4>
                                                <p class="text-gray-500 text-sm">Nhân viên xuất sắc</p>
                                            </div>
                                            <div>
                                                <select name="period2" id="period2"
                                                    class="border-gray-400/20 text-gray-500 rounded-md text-sm border-[1px] focus:ring-0 sm:w-auto w-full">
                                                    <option value="mar">Tháng 3, 2025</option>
                                                    <option value="apr">Tháng 4, 2025</option>
                                                    <option value="may">Tháng 5, 2025</option>
                                                    <option value="jun">Tháng 6, 2025</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="relative overflow-x-auto">
                                            <!-- table -->
                                            <table class="text-left w-full whitespace-nowrap text-sm my-2.5">
                                                <thead class="text-gray-700">
                                                    <tr class="font-semibold text-dark">
                                                        <th scope="col" class="p-4">Người được giao</th>
                                                        <th scope="col" class="p-4">Dự án</th>
                                                        <th scope="col" class="p-4">Độ ưu tiên</th>
                                                        <th scope="col" class="p-4">Ngân sách</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr class="border-b border-gray-400/10">
                                                        <td class="p-4">
                                                            <div class="flex gap-3 items-center">
                                                                <img class="object-cover w-9 h-9 rounded-full"
                                                                    src="${env}/" alt aria-hidden="true" />
                                                                <div>
                                                                    <h3 class="font-semibold text-dark">
                                                                        Sunil Joshi
                                                                    </h3>
                                                                    <span class="font-normal text-gray-500 text-xs">Web
                                                                        Designer</span>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td class="p-4">
                                                            <span class="font-normal text-gray-500">Elite
                                                                Admin</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span
                                                                class="inline-flex items-center py-[3px] px-[10px] rounded-md font-medium bg-primary text-white">Thấp</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span class="font-semibold text-base text-dark">$3.9</span>
                                                        </td>
                                                    </tr>
                                                    <tr class="border-b border-gray-400/10">
                                                        <td class="p-4">
                                                            <div class="flex gap-3 items-center">
                                                                <img class="object-cover w-9 h-9 rounded-full"
                                                                    src="${env}/static/images/profile/user-2.png" alt
                                                                    aria-hidden="true" />
                                                                <div>
                                                                    <h3 class="font-semibold text-dark">
                                                                        Andrew McDownland
                                                                    </h3>
                                                                    <span
                                                                        class="font-normal text-gray-500 text-xs">Project
                                                                        Manager</span>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td class="p-4">
                                                            <span class="font-normal text-gray-500">Real
                                                                Homes WP Theme</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span
                                                                class="inline-flex items-center py-[3px] px-[10px] rounded-md font-medium text-white bg-info">Trung
                                                                bình</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span
                                                                class="font-semibold text-base text-dark">$24.5k</span>
                                                        </td>
                                                    </tr>
                                                    <tr class="border-b border-gray-400/10">
                                                        <td class="p-4">
                                                            <div class="flex gap-3 items-center">
                                                                <img class="object-cover w-9 h-9 rounded-full"
                                                                    src="${env}/static/images/profile/user-3.png" alt
                                                                    aria-hidden="true" />
                                                                <div>
                                                                    <h3 class="font-semibold text-dark">
                                                                        Christopher Jamil
                                                                    </h3>
                                                                    <span
                                                                        class="font-normal text-xs text-gray-500">Project
                                                                        Manager</span>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td class="p-4">
                                                            <span class="font-normal text-gray-500">MedicalPro
                                                                WP Theme</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span
                                                                class="inline-flex items-center py-[3px] px-[10px] rounded-md font-medium text-white bg-error">Cao</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span
                                                                class="font-semibold text-base text-dark">$12.8k</span>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="p-4">
                                                            <div class="flex gap-3 items-center">
                                                                <img class="object-cover w-9 h-9 rounded-full"
                                                                    src="${env}/static/images/profile/user-4.png" alt
                                                                    aria-hidden="true" />
                                                                <div>
                                                                    <h3 class="font-semibold text-dark">
                                                                        Nirav Joshi
                                                                    </h3>
                                                                    <span
                                                                        class="font-normal text-xs text-gray-500">Frontend
                                                                        Engineer</span>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td class="p-4">
                                                            <span class="font-normal text-sm text-gray-500">Hosting
                                                                Press HTML</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span
                                                                class="inline-flex items-center py-[3px] px-[10px] rounded-md font-medium text-white bg-success">Quan
                                                                trọng</span>
                                                        </td>
                                                        <td class="p-4">
                                                            <span class="font-semibold text-base text-dark">$2.4k</span>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Best-selling Products Carousel Section -->
                        <div class="grid grid-cols-1 xl:grid-cols-4 lg:grid-cols-2 gap-6">
                            <div class="card overflow-hidden">
                                <div class="relative">
                                    <a href="javascript:void(0)">
                                        <img src="./assets/images/products/product-1.jpg" alt="product_img"
                                            class="w-full" />
                                    </a>
                                    <a href="javascript:void(0)"
                                        class="bg-primary w-8 h-8 flex justify-center items-center text-white rounded-full absolute bottom-0 right-0 mr-4 -mb-3">
                                        <i class="ti ti-basket text-base"></i>
                                    </a>
                                </div>
                                <div class="card-body">
                                    <h6 class="text-base font-semibold text-dark mb-1">
                                        Boat Headphone
                                    </h6>
                                    <div class="flex justify-between">
                                        <div class="flex gap-2 items-center">
                                            <h6 class="text-base text-dark font-semibold">$50</h6>
                                            <span class="text-gray-500 text-sm">
                                                <del>$65</del>
                                            </span>
                                        </div>
                                        <ul class="list-none flex gap-1">
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="card overflow-hidden">
                                <div class="relative">
                                    <a href="javascript:void(0)">
                                        <img src="./assets/images/products/product-2.jpg" alt="product_img"
                                            class="w-full" />
                                    </a>
                                    <a href="javascript:void(0)"
                                        class="bg-primary w-8 h-8 flex justify-center items-center text-white rounded-full absolute bottom-0 right-0 mr-4 -mb-3">
                                        <i class="ti ti-basket text-base"></i>
                                    </a>
                                </div>
                                <div class="card-body">
                                    <h6 class="text-base font-semibold text-dark mb-1">
                                        MacBook Air Pro
                                    </h6>
                                    <div class="flex justify-between">
                                        <div class="flex gap-2 items-center">
                                            <h6 class="text-base text-dark font-semibold">$650</h6>
                                            <span class="text-gray-500 text-sm">
                                                <del>$900</del>
                                            </span>
                                        </div>
                                        <ul class="list-none flex gap-1">
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="card overflow-hidden">
                                <div class="relative">
                                    <a href="javascript:void(0)">
                                        <img src="./assets/images/products/product-3.jpg" alt="product_img"
                                            class="w-full" />
                                    </a>
                                    <a href="javascript:void(0)"
                                        class="bg-primary w-8 h-8 flex justify-center items-center text-white rounded-full absolute bottom-0 right-0 mr-4 -mb-3">
                                        <i class="ti ti-basket text-base"></i>
                                    </a>
                                </div>
                                <div class="card-body">
                                    <h6 class="text-base font-semibold text-dark mb-1">
                                        Red Valvet Dress
                                    </h6>
                                    <div class="flex justify-between">
                                        <div class="flex gap-2 items-center">
                                            <h6 class="text-base text-dark font-semibold">$150</h6>
                                            <span class="text-gray-500 text-sm">
                                                <del>$200</del>
                                            </span>
                                        </div>
                                        <ul class="list-none flex gap-1">
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="card overflow-hidden">
                                <div class="relative">
                                    <a href="javascript:void(0)">
                                        <img src="./assets/images/products/product-4.jpg" alt="product_img"
                                            class="w-full" />
                                    </a>
                                    <a href="javascript:void(0)"
                                        class="bg-primary w-8 h-8 flex justify-center items-center text-white rounded-full absolute bottom-0 right-0 mr-4 -mb-3">
                                        <i class="ti ti-basket text-base"></i>
                                    </a>
                                </div>
                                <div class="card-body">
                                    <h6 class="text-base font-semibold text-dark mb-1">
                                        Cute Soft Teddybear
                                    </h6>
                                    <div class="flex justify-between">
                                        <div class="flex gap-2 items-center">
                                            <h6 class="text-base text-dark font-semibold">$285</h6>
                                            <span class="text-gray-500 text-sm">
                                                <del>$345</del>
                                            </span>
                                        </div>
                                        <ul class="list-none flex gap-1">
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                            <li>
                                                <a href="javascript:void(0)">
                                                    <i class="ti ti-star text-yellow-500 text-sm"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
            </div>
    </main>
    <!-- Main Content End -->
    </div>
    </div>
    </main>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/simplebar@5.3.8/dist/simplebar.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/iconify-icon@1.0.7/dist/iconify-icon.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/preline@1.9.0/dist/preline.js"></script>
    <script src="${env}/js/admin/sidebarmenu.js"></script>
    <script src="${env}/js/admin/pages/dashboard.js"></script>

    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
    <script src="${env}/js/admin/dashboard.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var swiper = new Swiper('.centered-slide-carousel', {
                centeredSlides: true,
                loop: true,
                spaceBetween: 24,
                slideToClickedSlide: true,
                pagination: {
                    el: '.centered-slide-carousel .swiper-pagination',
                    clickable: true,
                },
                breakpoints: {
                    1920: {
                        slidesPerView: 4,
                        spaceBetween: 24
                    },
                    1024: {
                        slidesPerView: 2,
                        spaceBetween: 16
                    },
                    640: {
                        slidesPerView: 1,
                        spaceBetween: 0
                    }
                }
            });
        });
    </script>

    <!-- Swiper minimal styles override -->
    <style>
        .swiper-wrapper {
            width: 100%;
            height: max-content !important;
            padding-bottom: 24px !important;
            -webkit-transition-timing-function: linear !important;
            transition-timing-function: linear !important;
            position: relative;
        }

        .swiper-pagination-bullet {
            background: #4F46E5;
        }

        .swiper-pagination-bullet-active {
            background: #4F46E5 !important;
        }
    </style>
</body>

</html>
