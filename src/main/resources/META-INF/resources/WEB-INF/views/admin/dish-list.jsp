<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Quản lý Món Ăn | Food Order</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
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
                            <a href="${pageContext.request.contextPath}/admin/dishes" class="nav-link active">
                                <i class="fa-solid fa-burger"></i> Món ăn
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/admin/coupons" class="nav-link">
                                <i class="fa-solid fa-ticket"></i> Coupon
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">
                                <i class="fa-solid fa-receipt"></i> Đơn hàng
                            </a>
                        </li>
                        <li
                            style="list-style: none; margin-top: 40px; padding-top: 20px; border-top: 1px solid #e5e7eb;">
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

                <!-- Main Content -->
                <div class="main-content">
                    <!-- Topbar -->
                    <div class="admin-topbar">
                        <div class="topbar-left">
                            <h3 class="topbar-title">Quản lý Món Ăn</h3>
                        </div>
                    </div>

                    <!-- Content Wrapper -->
                    <div class="content-wrapper">
                        <div class="page-header d-flex justify-content-between align-items-center">
                            <h4 class="page-title"></h4>
                            <div class="header-actions">
                                <a href="${pageContext.request.contextPath}/admin/undo"
                                    class="btn btn-sm btn-secondary me-2 ${canUndo ? '' : 'disabled'}"
                                    aria-disabled="${not canUndo}">
                                    <i class="fa-solid fa-rotate-left"></i> Undo
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/redo"
                                    class="btn btn-sm btn-secondary me-3 ${canRedo ? '' : 'disabled'}"
                                    aria-disabled="${not canRedo}">
                                    <i class="fa-solid fa-rotate-right"></i> Redo
                                </a>
                                <button class="btn btn-sm btn-success" data-bs-toggle="modal"
                                    data-bs-target="#addDishModal">
                                    <i class="fa-solid fa-plus me-2"></i> Thêm Món
                                </button>
                            </div>
                        </div>

                        <!-- Table Card -->
                        <div class="table-card">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th width="15%">Ảnh</th>
                                        <th width="25%">Tên Món</th>
                                        <th width="15%">Giá bán</th>
                                        <th width="35%">Mô tả</th>
                                        <th width="15%" class="text-center">Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${dishes}" var="dish">
                                        <tr>
                                            <td>
                                                <img src="${dish.imageUrl}" alt="${dish.name}" class="dish-thumbnail">
                                            </td>
                                            <td>
                                                <strong>${dish.name}</strong>
                                            </td>
                                            <td>
                                                <fmt:formatNumber value="${dish.price}" type="number"
                                                    groupingUsed="true" maxFractionDigits="0" /> đ
                                            </td>
                                            <td>
                                                <div class="text-truncate" style="max-width: 300px;"
                                                    title="${dish.description}">
                                                    ${dish.description}
                                                </div>
                                            </td>
                                            <td class="text-center">
                                                <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal"
                                                    data-bs-target="#editDishModal-${dish.dishId}" title="Sửa">
                                                    <i class="fa-solid fa-pen-to-square"></i>
                                                </button>
                                                <a href="${pageContext.request.contextPath}/admin/delete/${dish.dishId}"
                                                    class="btn btn-sm btn-outline-danger"
                                                    onclick="return confirm('Chắc chắn xóa?');" title="Xóa">
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

                <!-- Add Dish Modal -->
                <div class="modal fade" id="addDishModal" tabindex="-1">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form action="${pageContext.request.contextPath}/admin/add" method="POST">
                                <div class="modal-header">
                                    <h5 class="modal-title">Thêm Món Mới</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Tên Món</label>
                                        <input type="text" name="name" class="form-control" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Giá tiền (VNĐ)</label>
                                        <input type="number" name="price" class="form-control" inputmode="decimal"
                                            required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Link Hình Ảnh</label>
                                        <input type="text" name="imageUrl" class="form-control"
                                            value="https://via.placeholder.com/150">
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Mô tả</label>
                                        <textarea name="description" class="form-control" rows="3"></textarea>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary"
                                        data-bs-dismiss="modal">Đóng</button>
                                    <button type="submit" class="btn btn-success">Lưu</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Edit Dish Modals -->
                <c:forEach items="${dishes}" var="dish">
                    <div class="modal fade" id="editDishModal-${dish.dishId}" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="${pageContext.request.contextPath}/admin/update/${dish.dishId}"
                                    method="POST">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Sửa Thông Tin Món</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Tên Món</label>
                                            <input type="text" name="name" class="form-control" value="${dish.name}"
                                                required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Giá tiền (VNĐ)</label>
                                            <input type="number" name="price" class="form-control" value="${dish.price}"
                                                inputmode="decimal" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Link Hình Ảnh</label>
                                            <input type="text" name="imageUrl" class="form-control"
                                                value="${dish.imageUrl}">
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Mô tả</label>
                                            <textarea name="description" class="form-control"
                                                rows="3">${dish.description}</textarea>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">Đóng</button>
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