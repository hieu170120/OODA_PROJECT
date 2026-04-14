<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quan ly Don Hang | Food Order</title>
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
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
                    <i class="fa-solid fa-border-all"></i> Dashboard
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/dishes" class="nav-link">
                    <i class="fa-solid fa-burger"></i> Mon an
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link active">
                    <i class="fa-solid fa-receipt"></i> Don hang
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
                    <a href="${pageContext.request.contextPath}/admin/logout" class="btn-logout">Dang xuat</a>
                </div>
            </li>
        </ul>
    </div>

    <div class="main-content">
        <div class="admin-topbar">
            <div class="topbar-left">
                <h3 class="topbar-title">Quan ly Don Hang</h3>
            </div>
        </div>

        <div class="content-wrapper">
            <div class="page-header">
                <h4 class="page-title">Danh sach cac don hang</h4>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <div class="table-card">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th width="12%">Ma Don</th>
                            <th width="18%">Khach Hang</th>
                            <th width="25%">Dia Chi</th>
                            <th width="12%">Tong Tien</th>
                            <th width="12%">Thanh Toan</th>
                            <th width="15%">Trang Thai</th>
                            <th width="6%" class="text-center">Luu</th>
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
                                    <strong>
                                        <fmt:formatNumber value="${order.totalAmount}" type="number"
                                            groupingUsed="true" maxFractionDigits="0" /> d
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
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>