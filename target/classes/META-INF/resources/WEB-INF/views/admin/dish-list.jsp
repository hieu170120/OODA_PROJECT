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
                    <a href="${pageContext.request.contextPath}/admin/logout" class="btn-logout">Đăng xuất</a>
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
                <h4 class="page-title">Danh sách các món ăn</h4>
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
                    <button class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#addDishModal">
                        <i class="fa-solid fa-plus me-2"></i> Thêm Món
                    </button>
                </div>
            </div>

            <!-- Table Card -->
            <div class="table-card">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th width="5%">Ảnh</th>
                            <th width="25%">Tên Món</th>
                            <th width="15%">Giá bán</th>
                            <th width="40%">Mô tả</th>
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
                                    <fmt:formatNumber value="${dish.price}" type="number" groupingUsed="true"
                                        maxFractionDigits="0" /> đ
                                </td>
                                <td>
                                    <div class="text-truncate" style="max-width: 300px;" title="${dish.description}">
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
                            <input type="number" name="price" class="form-control" inputmode="decimal" required>
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
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
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
                    <form action="${pageContext.request.contextPath}/admin/update/${dish.dishId}" method="POST">
                        <div class="modal-header">
                            <h5 class="modal-title">Sửa Thông Tin Món</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Tên Món</label>
                                <input type="text" name="name" class="form-control" value="${dish.name}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Giá tiền (VNĐ)</label>
                                <input type="number" name="price" class="form-control" value="${dish.price}"
                                    inputmode="decimal" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Link Hình Ảnh</label>
                                <input type="text" name="imageUrl" class="form-control" value="${dish.imageUrl}">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Mô tả</label>
                                <textarea name="description" class="form-control" rows="3">${dish.description}</textarea>
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
                        font-size: 15px;
                        color: #374151;
                    }

                    .dish-thumbnail {
                        width: 48px;
                        height: 48px;
                        object-fit: cover;
                        border-radius: 8px;
                        border: 1px solid #e5e7eb;
                    }

                    .btn-purple {
                        background-color: #6d28d9;
                        color: white;
                        font-weight: 500;
                        border-radius: 8px;
                    }

                    .btn-purple:hover {
                        background-color: #5b21b6;
                        color: white;
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
                </style>
            </head>

            <body>

                <div class="sidebar">
                    <div class="sidebar-brand">
                        <i class="fa-solid fa-infinity"></i> FoodOrder
                    </div>
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link"><i
                                    class="fa-solid fa-border-all"></i> Dashboard</a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/admin/dishes" class="nav-link active"><i
                                    class="fa-solid fa-burger"></i> Món ăn</a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link"><i
                                    class="fa-solid fa-receipt"></i> Đơn hàng</a>
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
                                <a href="${pageContext.request.contextPath}/admin/logout" class="btn-logout">Đăng
                                    xuất</a>
                            </div>
                        </li>
                    </ul>
                </div>

                <div class="main-content">

                    <div class="admin-topbar">
                        <div class="topbar-left">
                            <h3 class="topbar-title">Danh sách Món ăn</h3>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between align-items-center mb-4" style="margin-top: 30px;">
                        <h4 style="font-weight: 600; color: #374151; margin: 0;">Quản lý các món ăn</h4>

                        <div>
                            <a href="${pageContext.request.contextPath}/admin/undo"
                                class="btn btn-outline-secondary me-2 ${canUndo ? '' : 'disabled'}"
                                aria-disabled="${not canUndo}">
                                <i class="fa-solid fa-rotate-left"></i> Undo
                            </a>

                            <a href="${pageContext.request.contextPath}/admin/redo"
                                class="btn btn-outline-secondary me-3 ${canRedo ? '' : 'disabled'}"
                                aria-disabled="${not canRedo}">
                                <i class="fa-solid fa-rotate-right"></i> Redo
                            </a>

                            <button class="btn btn-purple px-4 py-2" data-bs-toggle="modal"
                                data-bs-target="#addDishModal">
                                <i class="fa-solid fa-plus me-2"></i> Thêm Món
                            </button>
                        </div>
                    </div>

                    <div class="table-card">
                        <table class="table table-hover border-bottom">
                            <thead>
                                <tr>
                                    <th width="30%">Tên món</th>
                                    <th width="20%">Giá bán</th>
                                    <th width="35%">Mô tả</th>
                                    <th width="15%" class="text-center">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${dishes}" var="dish">
                                    <tr>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <img src="${dish.imageUrl}" alt="Img" class="dish-thumbnail me-3">
                                                <span style="font-weight: 600;">${dish.name}</span>
                                            </div>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${dish.price}" type="number" groupingUsed="true"
                                                maxFractionDigits="0" /> đ
                                        </td>
                                        <td>
                                            <div class="text-muted"
                                                style="max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                <c:out value="${dish.description}" />
                                            </div>
                                        </td>
                                        <td class="text-center">
                                            <button class="btn btn-sm btn-light text-primary me-1"
                                                data-bs-toggle="modal" data-bs-target="#editDishModal-${dish.dishId}"
                                                title="Sửa">
                                                <i class="fa-solid fa-pen-to-square"></i>
                                            </button>

                                            <a href="${pageContext.request.contextPath}/admin/delete/${dish.dishId}"
                                                class="btn btn-sm btn-light text-danger" title="Xóa">
                                                <i class="fa-solid fa-trash-can"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="modal fade" id="addDishModal" tabindex="-1">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form action="${pageContext.request.contextPath}/admin/add" method="POST">
                                <div class="modal-header">
                                    <h5 class="modal-title fw-bold text-purple">Thêm Món Mới</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Tên món</label>
                                        <input type="text" name="name" class="form-control" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold">Giá tiền (VNĐ)</label>
                                        <input type="number" name="price" class="form-control" required>
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
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                    <button type="submit" class="btn btn-purple">Lưu Món Mới</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <c:forEach items="${dishes}" var="dish">
                    <div class="modal fade" id="editDishModal-${dish.dishId}" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="${pageContext.request.contextPath}/admin/update/${dish.dishId}"
                                    method="POST">
                                    <div class="modal-header">
                                        <h5 class="modal-title fw-bold text-primary">Cập Nhật Món Ăn</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Tên món</label>
                                            <input type="text" name="name" class="form-control" value="${dish.name}"
                                                required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Giá tiền (VNĐ)</label>
                                            <input type="number" name="price" class="form-control" value="${dish.price}"
                                                required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Link Hình Ảnh</label>
                                            <input type="text" name="imageUrl" class="form-control"
                                                value="${dish.imageUrl}">
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label fw-semibold">Mô tả</label>
                                            <textarea name="description" class="form-control"
                                                rows="3"><c:out value="${dish.description}"/></textarea>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">Hủy</button>
                                        <button type="submit" class="btn btn-primary">Lưu Thay Đổi</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>

            </html>