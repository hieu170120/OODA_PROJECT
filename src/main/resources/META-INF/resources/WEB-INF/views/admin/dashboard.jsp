<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard | Food Order</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
        </head>

        <body>
            <div class="sidebar">
                <div class="sidebar-brand">
                    <i class="fa-solid fa-infinity"></i> FoodOrder
                </div>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link active">
                            <i class="fa-solid fa-border-all"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/admin/dishes" class="nav-link">
                            <i class="fa-solid fa-burger"></i> Món ăn
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">
                            <i class="fa-solid fa-receipt"></i> Đơn hàng
                        </a>
                    </li>
                    <li style="list-style: none; margin-top: 40px; padding-top: 20px; border-top: 1px solid #e5e7eb;">
                        <div class="sidebar-admin-info">
                            <span class="admin-name">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.LOGGED_IN_ADMIN}">
                                        ${sessionScope.LOGGED_IN_ADMIN.fullName}
                                    </c:when>
                                    <c:otherwise>
                                        Manager
                                    </c:otherwise>
                                </c:choose>
                            </span>
                            <a href="${pageContext.request.contextPath}/admin/logout" class="btn-logout"
                                title="Đăng xuất">
                                <i class="fa-solid fa-right-from-bracket"></i>
                            </a>
                        </div>
                    </li>
                </ul>
            </div>

            <div class="main-content">
                <div class="admin-topbar">
                    <div class="topbar-left">
                        <h3 class="topbar-title">Dashboard</h3>
                    </div>
                </div>

                <div class="content-wrapper">
                    <div class="page-header">
                        <!-- <h4 class="page-title">Tổng quan hệ thống</h4> -->
                    </div>

                    <div class="row" style="margin-bottom: 30px;">
                        <div class="col-md-3">
                            <div
                                style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.08);">
                                <div style="display: flex; align-items: center; justify-content: space-between;">
                                    <div>
                                        <div
                                            style="font-size: 12px; color: #6b7280; text-transform: uppercase; letter-spacing: 0.5px;">
                                            Tổng Món Ăn</div>
                                        <div
                                            style="font-size: 28px; font-weight: 700; color: #1f2937; margin-top: 8px;">
                                            ${totalDishes}</div>
                                    </div>
                                    <div style="font-size: 32px; color: #6d28d9; opacity: 0.2;">
                                        <i class="fa-solid fa-burger"></i>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div
                                style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.08);">
                                <div style="display: flex; align-items: center; justify-content: space-between;">
                                    <div>
                                        <div
                                            style="font-size: 12px; color: #6b7280; text-transform: uppercase; letter-spacing: 0.5px;">
                                            Tổng Đơn Hàng</div>
                                        <div
                                            style="font-size: 28px; font-weight: 700; color: #1f2937; margin-top: 8px;">
                                            ${totalOrders}</div>
                                    </div>
                                    <div style="font-size: 32px; color: #f59e0b; opacity: 0.2;">
                                        <i class="fa-solid fa-receipt"></i>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div
                                style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.08);">
                                <div style="display: flex; align-items: center; justify-content: space-between;">
                                    <div>
                                        <div
                                            style="font-size: 12px; color: #6b7280; text-transform: uppercase; letter-spacing: 0.5px;">
                                            Đơn Hôm Nay</div>
                                        <div
                                            style="font-size: 28px; font-weight: 700; color: #1f2937; margin-top: 8px;">
                                            ${ordersToday}</div>
                                    </div>
                                    <div style="font-size: 32px; color: #10b981; opacity: 0.2;">
                                        <i class="fa-solid fa-chart-line"></i>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div
                                style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.08);">
                                <div style="display: flex; align-items: center; justify-content: space-between;">
                                    <div>
                                        <div
                                            style="font-size: 12px; color: #6b7280; text-transform: uppercase; letter-spacing: 0.5px;">
                                            Doanh Thu Hôm Nay</div>
                                        <div
                                            style="font-size: 28px; font-weight: 700; color: #1f2937; margin-top: 8px;">
                                            ${revenueToday}</div>
                                    </div>
                                    <div style="font-size: 32px; color: #ef4444; opacity: 0.2;">
                                        <i class="fa-solid fa-coins"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="table-card">
                        <h5 style="margin-bottom: 20px; font-weight: 600;">Nhanh chóng truy cập</h5>
                        <div
                            style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px;">
                            <a href="${pageContext.request.contextPath}/admin/dishes"
                                style="text-decoration: none; color: inherit;">
                                <div style="padding: 20px; border: 1px solid #e5e7eb; border-radius: 8px; text-align: center; transition: all 0.3s; cursor: pointer;"
                                    onmouseover="this.style.boxShadow='0 4px 12px rgba(0,0,0,0.1)'; this.style.borderColor='#000000';"
                                    onmouseout="this.style.boxShadow='none'; this.style.borderColor='#e5e7eb';">
                                    <div style="font-size: 28px; margin-bottom: 10px; color: #6d28d9;">
                                        <i class="fa-solid fa-burger"></i>
                                    </div>
                                    <div style="font-weight: 600; color: #1f2937;">Quản lý Món Ăn</div>
                                </div>
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/orders"
                                style="text-decoration: none; color: inherit;">
                                <div style="padding: 20px; border: 1px solid #e5e7eb; border-radius: 8px; text-align: center; transition: all 0.3s; cursor: pointer;"
                                    onmouseover="this.style.boxShadow='0 4px 12px rgba(0,0,0,0.1)'; this.style.borderColor='#000000';"
                                    onmouseout="this.style.boxShadow='none'; this.style.borderColor='#e5e7eb';">
                                    <div style="font-size: 28px; margin-bottom: 10px; color: #f59e0b;">
                                        <i class="fa-solid fa-receipt"></i>
                                    </div>
                                    <div style="font-weight: 600; color: #1f2937;">Quản lý Đơn Hàng</div>
                                </div>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>