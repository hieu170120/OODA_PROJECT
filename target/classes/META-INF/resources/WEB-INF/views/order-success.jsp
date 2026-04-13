<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt Hàng Thành Công 🎉 - Food Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; }
        .card { border-radius: 15px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); margin-bottom: 20px;}
        .navbar-brand { font-weight: bold; color: #dc3545 !important; }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">🍔 Food<span class="text-dark">Order</span></a>
    </div>
</nav>

<div class="container mt-4 mb-5" style="max-width: 700px;">
    <div class="text-center mb-4">
        <i class="bi bi-check-circle-fill text-success" style="font-size: 4rem;"></i>
        <h2 class="mt-3 text-success fw-bold">Đặt Hàng Thành Công!</h2>
        <p class="text-muted">Cảm ơn bạn đã đặt món. Dưới đây là thông tin chi tiết đơn hàng của bạn.</p>
    </div>

    <div class="card border-0 shadow">
        <div class="card-header bg-white border-bottom py-3 d-flex justify-content-between align-items-center">
            <h5 class="mb-0 text-primary"><i class="bi bi-receipt"></i> Mã đơn: <span class="fw-bold text-dark">#${order.orderId.substring(0,8)}</span></h5>
            <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">${order.status}</span>
        </div>

        <div class="card-body p-4">
            <!-- Thông tin giao hàng -->
            <div class="row mb-4">
                <div class="col-sm-6 mb-3 mb-sm-0">
                    <h6 class="text-muted mb-2"><i class="bi bi-person"></i> Người nhận:</h6>
                    <p class="fw-bold mb-0">${order.customerName}</p>
                </div>
                <div class="col-sm-6">
                    <h6 class="text-muted mb-2"><i class="bi bi-clock"></i> Dự kiến giao lúc:</h6>
                    <p class="fw-bold mb-0 text-success">${order.estimatedPickupTime}</p>
                </div>
                <div class="col-12 mt-3">
                    <h6 class="text-muted mb-2"><i class="bi bi-geo-alt"></i> Địa chỉ giao hàng:</h6>
                    <p class="fw-bold mb-0">${order.shippingAddress}</p>
                </div>
            </div>

            <!-- Chi tiết món ăn -->
            <h6 class="border-bottom pb-2 text-danger fw-bold"><i class="bi bi-basket"></i> Món ăn đã chọn</h6>
            <div class="bg-light rounded p-3 mb-4">
                <c:forEach items="${order.orderItems}" var="item">
                    <div class="d-flex justify-content-between align-items-center border-bottom pb-2 mb-2 last-no-border">
                        <div>
                            <div class="fw-bold">${item.dishName}</div>
                            <small class="text-muted">Số lượng: ${item.quantity}</small>
                        </div>
                        <div class="fw-bold text-dark"><fmt:formatNumber value="${item.subTotal}" type="number" pattern="###,###"/>đ</div>
                    </div>
                </c:forEach>
            </div>

            <!-- Tổng tiền -->
            <div class="row">
                <div class="col-8 text-end text-muted">Tổng tiền món:</div>
                <div class="col-4 text-end fw-bold"><fmt:formatNumber value="${order.subTotal}" type="number" pattern="###,###"/>đ</div>

                <div class="col-8 text-end text-muted mt-1">Phí giao hàng:</div>
                <div class="col-4 text-end fw-bold mt-1"><fmt:formatNumber value="${order.shippingFee}" type="number" pattern="###,###"/>đ</div>

                <div class="col-8 text-end fs-5 mt-2 fw-bold text-danger">TỔNG CỘNG:</div>
                <div class="col-4 text-end fs-5 mt-2 fw-bold text-danger"><fmt:formatNumber value="${order.totalAmount}" type="number" pattern="###,###"/>đ</div>
            </div>

            <!-- Thông tin thanh toán (Strategy Pattern) -->
            <c:if test="${order.paymentMethod != null}">
                <div class="alert alert-secondary mt-4 mb-0 border-0" role="alert">
                    <h6 class="alert-heading text-primary mb-3"><i class="bi bi-credit-card"></i> Thông tin thanh toán</h6>
                    <div class="row">
                        <div class="col-sm-6 mb-2">
                            <span class="text-muted small d-block">Phương thức:</span>
                            <strong>
                                <c:choose>
                                    <c:when test="${order.paymentMethod.name() == 'COD'}">Thanh toán khi nhận (COD)</c:when>
                                    <c:when test="${order.paymentMethod.name() == 'BANKING'}">Chuyển khoản (Banking)</c:when>
                                    <c:otherwise>${order.paymentMethod}</c:otherwise>
                                </c:choose>
                            </strong>
                        </div>
                        <div class="col-sm-6 mb-2">
                            <span class="text-muted small d-block">Trạng thái:</span>
                            <c:choose>
                                <c:when test="${order.paymentStatus.name() == 'COMPLETED'}">
                                    <span class="badge bg-success">Đã thanh toán</span>
                                </c:when>
                                <c:when test="${order.paymentStatus.name() == 'PENDING'}">
                                    <span class="badge bg-warning text-dark">Chờ thanh toán</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger">${order.paymentStatus}</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="col-12 mt-1">
                            <span class="text-muted small">Mã GD (Payment ID):</span> <code class="bg-white px-2 py-1 rounded">${order.paymentId}</code>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <div class="card-footer bg-white border-top-0 p-4 text-center">
            <a href="${pageContext.request.contextPath}/order" class="btn btn-outline-primary px-4 py-2 me-2">
                <i class="bi bi-arrow-left"></i> Tiếp tục đặt món
            </a>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary px-4 py-2">
                Về trang chủ <i class="bi bi-house"></i>
            </a>
        </div>
    </div>
</div>

<style>
    .last-no-border:last-child { border-bottom: none !important; margin-bottom: 0 !important; padding-bottom: 0 !important; }
</style>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
