<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Đơn Hàng | Food Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f3f4f6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .sidebar {
            width: 260px;
            height: 100vh;
            background: #ffffff;
            position: fixed;
            top: 0;
            left: 0;
            padding-top: 20px;
            box-shadow: 4px 0 10px rgba(0, 0, 0, 0.03);
        }

        .sidebar-brand {
            color: #5b21b6;
            font-weight: 800;
            font-size: 24px;
            padding-left: 20px;
            margin-bottom: 30px;
        }

        .nav-item .nav-link {
            color: #6b7280;
            font-weight: 500;
            padding: 12px 20px;
            margin: 4px 15px;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .nav-item .nav-link i {
            width: 25px;
            font-size: 18px;
        }

        .nav-item .nav-link:hover,
        .nav-item .nav-link.active {
            background-color: #f5f3ff;
            color: #6d28d9;
        }

        .main-content {
            margin-left: 260px;
            padding: 30px 40px;
        }

        .table-card {
            background: #ffffff;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.02);
        }

        .table>thead {
            color: #6b7280;
            font-weight: 600;
            font-size: 14px;
        }

        .table>tbody>tr>td {
            vertical-align: middle;
            font-size: 14px;
            color: #374151;
        }

        .btn-purple {
            background-color: #6d28d9;
            color: white;
            font-weight: 500;
            border-radius: 8px;
            border: none;
        }

        .btn-purple:hover {
            background-color: #5b21b6;
            color: white;
        }

        .topbar-title {
            margin: 0;
            font-size: 20px;
            color: #1f2937;
            font-weight: 600;
        }

        .sidebar-admin-info {
            padding: 15px 20px;
            margin: auto 15px 20px 15px;
            background-color: #f5f3ff;
            border-radius: 8px;
            border-left: 4px solid #6d28d9;
        }

        .admin-name {
            display: block;
            font-size: 14px;
            color: #374151;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .btn-logout {
            display: inline-block;
            font-size: 13px;
            color: #ef4444;
            text-decoration: none;
            border: 1px solid #fca5a5;
            padding: 6px 10px;
            border-radius: 4px;
            transition: all 0.3s;
        }

        .btn-logout:hover {
            background-color: #fee2e2;
            color: #dc2626;
        }

        .admin-topbar {
            margin-left: 260px;
            background: white;
            padding: 20px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #e5e7eb;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1000;
        }
        .main-content-wrapper {
             margin-top: 80px;
             margin-left: 260px;
             padding: 30px 40px;
        }
    </style>
</head>

<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-brand">
            <i class="fa-solid fa-infinity"></i> FoodOrder
        </div>
        <ul class="nav flex-column">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
                    <i class="fa-solid fa-border-all"></i> Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/dishes" class="nav-link">
                    <i class="fa-solid fa-burger"></i> Món ăn
                </a>
            </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/admin/coupons" class="nav-link">
                        <i class="fa-solid fa-ticket"></i> Coupon
                    </a>
                </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link active">
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
                    <a href="${pageContext.request.contextPath}/admin/logout" class="btn-logout" title="Đăng xuất">
                        Đăng xuất <i class="fa-solid fa-right-from-bracket"></i>
                    </a>
                </div>
            </li>
        </ul>
    </div>

    <!-- Topbar -->
    <div class="admin-topbar">
        <div class="topbar-left">
            <h3 class="topbar-title">Quản lý Đơn Hàng</h3>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-content-wrapper">
        <div class="page-header d-flex justify-content-between align-items-center mb-4">
             <h4 style="font-weight: 600; color: #374151; margin: 0;">Danh sách các đơn hàng</h4>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- Table Card -->
        <div class="table-card">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th width="12%">Mã Đơn</th>
                        <th width="15%">Khách Hàng</th>
                        <th width="18%">Địa Chỉ</th>
                        <th width="14%">Thời gian</th>
                        <th width="12%">Tổng Tiền</th>
                        <th width="10%">Thanh Toán</th>
                        <th width="13%">Trạng Thái</th>
                        <th width="6%" class="text-center">Lưu</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orders}" var="order">
                            <tr>
                                <td>
                                    <code style="background-color: #f3f4f6; padding: 4px 8px; border-radius: 4px;">
                                        ${order.orderId}
                                    </code>
                                </td>
                                <td>${order.customerName}</td>
                                <td class="text-truncate" title="${order.shippingAddress}">
                                    ${order.shippingAddress}
                                </td>
                                <td>
                                    <c:if test="${not empty order.orderTime}">
                                        <small><fmt:parseDate value="${order.orderTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedTime" type="both" /><fmt:formatDate value="${parsedTime}" pattern="dd/MM/yyyy HH:mm" /></small>
                                    </c:if>
                                    <c:if test="${empty order.orderTime}">
                                        <small class="text-muted">N/A</small>
                                    </c:if>
                                </td>
                                <td>
                                    <strong>
                                        <fmt:formatNumber value="${order.totalAmount}" type="number"
                                            groupingUsed="true" maxFractionDigits="0" />d
                                    </strong>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.paymentStatus == 'COMPLETED'}">
                                            <span class="badge bg-success">Da thanh toan</span>
                                        </c:when>
                                        <c:when test="${order.paymentStatus == 'PENDING'}">
                                            <span class="badge bg-warning">Cho thanh toan</span>
                                        </c:when>
                                        <c:when test="${order.paymentStatus == 'FAILED'}">
                                            <span class="badge bg-danger">That bai</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">N/A</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <form
                                        action="${pageContext.request.contextPath}/admin/orders/${order.orderId}/status"
                                        method="post" style="display: inline;">
                                        <select name="status" class="form-select form-select-sm">
                                            <c:forEach items="${selectableByStatus[order.status]}" var="statusOpt">
                                                <option value="${statusOpt}" ${statusOpt == order.status ? 'selected' : ''}>
                                                    ${statusOpt}
                                                </option>
                                            </c:forEach>
                                        </select>
                                </td>
                                <td class="text-center">
                                    <button type="submit" class="btn btn-sm btn-success">
                                        <i class="fa-solid fa-check"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
