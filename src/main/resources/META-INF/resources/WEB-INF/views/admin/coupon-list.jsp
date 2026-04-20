<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Coupon | Food Order</title>
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
                    <i class="fa-solid fa-burger"></i> Món ăn
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/coupons" class="nav-link active">
                    <i class="fa-solid fa-ticket"></i> Coupon
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
                    <a href="${pageContext.request.contextPath}/admin/logout" class="btn-logout" title="Đăng xuất">
                        <i class="fa-solid fa-right-from-bracket"></i>
                    </a>
                </div>
            </li>
        </ul>
    </div>

    <div class="main-content">
        <div class="admin-topbar">
            <div class="topbar-left">
                <h3 class="topbar-title">Quản lý Coupon</h3>
            </div>
        </div>

        <div class="content-wrapper">
            <div class="page-header d-flex justify-content-between align-items-center">
                <h4 class="page-title"></h4>
                <div class="header-actions">
                    <button class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#addCouponModal">
                        <i class="fa-solid fa-plus me-2"></i> Thêm Coupon
                    </button>
                </div>
            </div>

            <div class="table-card">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th width="14%">Mã</th>
                            <th width="12%">Loại</th>
                            <th width="14%">Giảm giá</th>
                            <th width="14%">Tối thiểu</th>
                            <th width="16%">Hiệu lực từ</th>
                            <th width="16%">Hiệu lực đến</th>
                            <th width="8%" class="text-center">Trạng thái</th>
                            <th width="6%" class="text-center">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${coupons}" var="coupon">
                            <tr>
                                <td><strong>${coupon.couponCode}</strong></td>
                                <td>${coupon.strategyLabel}</td>
                                <td>${coupon.discountDisplay}</td>
                                <td>
                                    <fmt:formatNumber value="${coupon.minOrderValue}" type="number" groupingUsed="true" maxFractionDigits="0" /> đ
                                </td>
                                <td>${coupon.validFromText}</td>
                                <td>${coupon.validUntilText}</td>
                                <td class="text-center">
                                    <span class="badge ${coupon.active ? 'bg-success' : 'bg-secondary'}">
                                        ${coupon.active ? 'Đang dùng' : 'Hết hạn'}
                                    </span>
                                </td>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal"
                                        data-bs-target="#editCouponModal-${coupon.couponCode}" title="Sửa">
                                        <i class="fa-solid fa-pen-to-square"></i>
                                    </button>
                                    <a href="${pageContext.request.contextPath}/admin/coupons/delete/${coupon.couponCode}"
                                        class="btn btn-sm btn-outline-danger" onclick="return confirm('Chắc chắn xóa?');"
                                        title="Xóa">
                                        <i class="fa-solid fa-trash-can"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="modal fade" id="addCouponModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/admin/coupons/add" method="POST">
                    <div class="modal-header">
                        <h5 class="modal-title">Thêm Coupon Mới</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label fw-semibold">Mã coupon</label>
                                <input type="text" name="couponCode" class="form-control" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-semibold">Loại giảm giá</label>
                                <select name="strategyType" class="form-select" required>
                                    <option value="FIXED_AMOUNT">Giảm tiền cố định</option>
                                    <option value="PERCENTAGE">Giảm theo %</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-semibold">Giá trị giảm</label>
                                <input type="number" name="discountValue" class="form-control" step="0.01" min="0" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-semibold">Giảm tối đa</label>
                                <input type="number" name="maxDiscount" class="form-control" step="0.01" min="0">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-semibold">Đơn tối thiểu</label>
                                <input type="number" name="minOrderValue" class="form-control" step="0.01" min="0" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-semibold">Hiệu lực từ</label>
                                <input type="datetime-local" name="validFrom" class="form-control">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label fw-semibold">Hiệu lực đến</label>
                                <input type="datetime-local" name="validUntil" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-success">Lưu</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <c:forEach items="${coupons}" var="coupon">
        <div class="modal fade" id="editCouponModal-${coupon.couponCode}" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/admin/coupons/update/${coupon.couponCode}" method="POST">
                        <div class="modal-header">
                            <h5 class="modal-title">Sửa Coupon</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">Mã coupon</label>
                                    <input type="text" name="couponCode" class="form-control" value="${coupon.couponCode}" readonly>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">Loại giảm giá</label>
                                    <select name="strategyType" class="form-select" required>
                                        <option value="FIXED_AMOUNT" ${coupon.strategyType == 'FIXED_AMOUNT' ? 'selected' : ''}>Giảm tiền cố định</option>
                                        <option value="PERCENTAGE" ${coupon.strategyType == 'PERCENTAGE' ? 'selected' : ''}>Giảm theo %</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">Giá trị giảm</label>
                                    <input type="number" name="discountValue" class="form-control" value="${coupon.discountValue}" step="0.01" min="0" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">Giảm tối đa</label>
                                    <input type="number" name="maxDiscount" class="form-control" value="${coupon.maxDiscount}" step="0.01" min="0">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">Đơn tối thiểu</label>
                                    <input type="number" name="minOrderValue" class="form-control" value="${coupon.minOrderValue}" step="0.01" min="0" required>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">Hiệu lực từ</label>
                                    <input type="datetime-local" name="validFrom" class="form-control" value="${coupon.validFromText}">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label fw-semibold">Hiệu lực đến</label>
                                    <input type="datetime-local" name="validUntil" class="form-control" value="${coupon.validUntilText}">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="submit" class="btn btn-success">Cập nhật</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </c:forEach>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>