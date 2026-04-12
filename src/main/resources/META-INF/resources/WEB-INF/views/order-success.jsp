<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt Hàng Thành Công 🎉</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .card { border-radius: 15px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); margin-bottom: 20px;}
    </style>
</head>
<body>

<div class="container mt-5 w-50 mx-auto">
    <h2 class="mb-4 text-success text-center">🎉 Đặt Hàng Thành Công! 🎉</h2>
    <div class="card p-5 border-success">
        <h4 class="text-primary border-bottom pb-2">📦 THÔNG TIN ĐƠN HÀNG (Builder Result)</h4>

        <p><strong>Mã Đơn Hàng (ID):</strong> <span class="text-danger fw-bold">${order.orderId}</span></p>
        <p><strong>Khách Hàng:</strong> <span>${order.customer.fullName}</span></p>
        <p><strong>Trạng Thái:</strong> <span class="badge bg-warning text-dark">${order.status}</span></p>
        <p><strong>Giờ Đặt:</strong> <span>${order.orderTime}</span></p>
        <p><strong>Dự Kiến Nhận:</strong> <span class="text-success fw-bold">${order.estimatedPickupTime}</span></p>
        <p><strong>Địa chỉ giao:</strong> <span>${order.shippingAddress}</span></p>

        <h5 class="mt-4 border-bottom pb-2 text-info">🍔 Chi Tiết Món (Decorator Result)</h5>
        <ul class="list-group list-group-flush mb-3">
            <c:forEach items="${order.orderItems}" var="item">
                <li class="list-group-item d-flex justify-content-between lh-sm">
                    <div>
                        <h6 class="my-0 text-primary">${item.dish.name}</h6>
                        <small class="text-muted">Số lượng: ${item.quantity}</small>
                    </div>
                    <span class="text-muted fw-bold">${item.calculateSubTotal()}đ</span>
                </li>
            </c:forEach>
        </ul>

        <h5 class="mt-4 border-bottom pb-2 text-danger">💰 Thanh Toán:</h5>
        <div class="d-flex justify-content-between">
            <span>Tổng Tiền Món:</span> <strong>${order.subTotal}đ</strong>
        </div>
        <div class="d-flex justify-content-between">
            <span>Phí Giao Hàng:</span> <strong>${order.shippingFee}đ</strong>
        </div>
        <div class="d-flex justify-content-between fs-4 mt-2 border-top pt-2">
            <span class="text-danger fw-bold">TỔNG CỘNG:</span>
            <strong class="text-danger">${order.calculateTotal()}đ</strong>
        </div>

        <c:if test="${order.payment != null}">
            <div class="card bg-light mt-4 border-0">
                <div class="card-body">
                    <h6 class="card-title text-primary mb-3">🔐 Strategy — Kết quả thanh toán</h6>
                    <p class="mb-1"><strong>Mã giao dịch:</strong> <code>${order.payment.paymentId}</code></p>
                    <p class="mb-1"><strong>Phương thức:</strong>
                        <c:choose>
                            <c:when test="${order.payment.paymentMethod eq 'COD'}">Thanh toán khi nhận (COD)</c:when>
                            <c:when test="${order.payment.paymentMethod eq 'BANKING'}">Chuyển khoản ngân hàng</c:when>
                            <c:otherwise>${order.payment.paymentMethod}</c:otherwise>
                        </c:choose>
                    </p>
                    <p class="mb-1"><strong>Số tiền thanh toán:</strong> ${order.payment.amount}đ</p>
                    <p class="mb-0"><strong>Trạng thái:</strong>
                        <c:choose>
                            <c:when test="${order.payment.paymentStatus.name() == 'COMPLETED'}">
                                <span class="badge bg-success">Hoàn tất</span>
                            </c:when>
                            <c:when test="${order.payment.paymentStatus.name() == 'FAILED'}">
                                <span class="badge bg-danger">Thất bại</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">${order.payment.paymentStatus}</span>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${order.payment.paidAt != null}">
                            <span class="text-muted small ms-2">— ${order.payment.paidAt}</span>
                        </c:if>
                    </p>
                </div>
            </div>
        </c:if>

        <a href="${pageContext.request.contextPath}/order" class="btn btn-outline-secondary w-100 mt-4">⬅️ Đặt Món Khác</a>
    </div>
</div>

</body>
</html>
