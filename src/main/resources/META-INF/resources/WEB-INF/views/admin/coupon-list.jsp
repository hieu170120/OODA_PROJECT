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
            <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">
                <i class="fa-solid fa-receipt"></i> Đơn hàng
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/admin/coupons" class="nav-link active">
                <i class="fa-solid fa-ticket"></i> Coupon
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
            <button class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#addCouponModal">
                <i class="fa-solid fa-plus me-2"></i> Thêm Coupon
            </button>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <div class="table-card">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>Mã</th>
                    <th>Loại</th>
                    <th>Giá trị</th>
                    <th>Đơn tối thiểu</th>
                    <th>Hiệu lực</th>
                    <th>Trạng thái</th>
                    <th>Lượt dùng</th>
                    <th class="text-center">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${coupons}" var="coupon">
                    <tr>
                        <td><strong>${coupon.couponCode}</strong></td>
                        <td>
                            <c:choose>
                                <c:when test="${coupon.percentage}">Phần trăm</c:when>
                                <c:otherwise>Số tiền cố định</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${coupon.percentage}">
                                    <fmt:formatNumber value="${coupon.discountValue}" maxFractionDigits="0"/>%
                                    <c:if test="${coupon.maxDiscount != null}">
                                        (tối đa <fmt:formatNumber value="${coupon.maxDiscount}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ)
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber value="${coupon.discountValue}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><fmt:formatNumber value="${coupon.minOrderValue}" type="number" groupingUsed="true" maxFractionDigits="0"/>đ</td>
                        <td>
                            <div>Từ: ${coupon.validFrom}</div>
                            <div>Đến: ${coupon.validUntil}</div>
                        </td>
                        <td>
                            <span class="badge ${coupon.active ? 'bg-success' : 'bg-secondary'}">
                                ${coupon.active ? 'Đang bật' : 'Đã tắt'}
                            </span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${coupon.usageLimit != null && coupon.usageLimit > 0}">
                                    ${coupon.usedCount} / ${coupon.usageLimit}
                                </c:when>
                                <c:otherwise>
                                    ${coupon.usedCount} / Không giới hạn
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editCouponModal-${coupon.couponId}" title="Sửa">
                                <i class="fa-solid fa-pen-to-square"></i>
                            </button>
                            <a href="${pageContext.request.contextPath}/admin/coupons/delete/${coupon.couponId}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Chắc chắn xóa coupon này?');" title="Xóa">
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
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin/coupons/add" method="POST">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm Coupon</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Mã coupon</label>
                        <input type="text" name="couponCode" class="form-control" required>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" name="percentage" class="form-check-input" id="addPercentage">
                        <label class="form-check-label" for="addPercentage">Giảm theo phần trăm</label>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Giá trị giảm</label>
                        <input type="number" step="0.01" min="0" name="discountValue" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Giảm tối đa (nếu %)</label>
                        <input type="number" step="0.01" min="0" name="maxDiscount" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Đơn tối thiểu</label>
                        <input type="number" step="0.01" min="0" name="minOrderValue" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Có hiệu lực từ</label>
                        <input type="datetime-local" name="validFrom" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Có hiệu lực đến</label>
                        <input type="datetime-local" name="validUntil" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Giới hạn lượt dùng (để trống là không giới hạn)</label>
                        <input type="number" min="0" name="usageLimit" class="form-control">
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" name="active" class="form-check-input" id="addActive" checked>
                        <label class="form-check-label" for="addActive">Kích hoạt coupon</label>
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
    <div class="modal fade" id="editCouponModal-${coupon.couponId}" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/admin/coupons/update/${coupon.couponId}" method="POST">
                    <div class="modal-header">
                        <h5 class="modal-title">Cập nhật Coupon</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Mã coupon</label>
                            <input type="text" name="couponCode" class="form-control" value="${coupon.couponCode}" required>
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" name="percentage" class="form-check-input" id="editPercentage-${coupon.couponId}" ${coupon.percentage ? 'checked' : ''}>
                            <label class="form-check-label" for="editPercentage-${coupon.couponId}">Giảm theo phần trăm</label>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Giá trị giảm</label>
                            <input type="number" step="0.01" min="0" name="discountValue" class="form-control" value="${coupon.discountValue}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Giảm tối đa (nếu %)</label>
                            <input type="number" step="0.01" min="0" name="maxDiscount" class="form-control" value="${coupon.maxDiscount}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Đơn tối thiểu</label>
                            <input type="number" step="0.01" min="0" name="minOrderValue" class="form-control" value="${coupon.minOrderValue}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Có hiệu lực từ</label>
                            <input type="datetime-local" name="validFrom" class="form-control" value="${coupon.validFrom}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Có hiệu lực đến</label>
                            <input type="datetime-local" name="validUntil" class="form-control" value="${coupon.validUntil}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Giới hạn lượt dùng</label>
                            <input type="number" min="0" name="usageLimit" class="form-control" value="${coupon.usageLimit}">
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" name="active" class="form-check-input" id="editActive-${coupon.couponId}" ${coupon.active ? 'checked' : ''}>
                            <label class="form-check-label" for="editActive-${coupon.couponId}">Kích hoạt coupon</label>
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
