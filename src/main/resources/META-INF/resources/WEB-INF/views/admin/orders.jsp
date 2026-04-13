<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Quản lý Đơn Hàng</title>

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
            box-shadow: 4px 0 10px rgba(0,0,0,0.03);
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

        .nav-item .nav-link:hover, .nav-item .nav-link.active {
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
            box-shadow: 0 4px 6px rgba(0,0,0,0.02);
        }

        .table > thead {
            color: #6b7280;
            font-weight: 600;
            font-size: 14px;
        }

        .table > tbody > tr > td {
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
    </style>
</head>
<body>

<div class="sidebar">
    <div class="sidebar-brand">
        <i class="fa-solid fa-infinity"></i> FoodOrder
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/dishes" class="nav-link"><i class="fa-solid fa-burger"></i> Món ăn</a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link active"><i class="fa-solid fa-receipt"></i> Đơn hàng</a>
        </li>
    </ul>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 style="font-weight: 700; color: #111827;">Danh sách Đơn hàng</h2>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="table-card">
        <table class="table table-hover border-bottom">
            <thead>
            <tr>
                <th>Mã đơn</th>
                <th>Khách hàng</th>
                <th>Địa chỉ</th>
                <th>Tổng tiền</th>
                <th>Thanh toán</th>
                <th>Trạng thái đơn</th>
                <th>Cập nhật</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${orders}" var="order">
                <tr>
                    <td><code>${order.orderId}</code></td>
                    <td>${order.customerName}</td>
                    <td>${order.shippingAddress}</td>
                    <td>
                        <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${order.paymentStatus != null}">
                                ${order.paymentStatus}
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/admin/orders/${order.orderId}/status" method="post" class="d-flex gap-2">
                            <select name="status" class="form-select form-select-sm" style="min-width: 180px;">
                                <c:forEach items="${selectableByStatus[order.status]}" var="statusOpt">
                                    <option value="${statusOpt}" ${statusOpt == order.status ? 'selected' : ''}>${statusOpt}</option>
                                </c:forEach>
                            </select>
                    </td>
                    <td>
                            <button type="submit" class="btn btn-sm btn-purple">Lưu</button>
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
