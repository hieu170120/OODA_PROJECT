<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ Hàng & Thanh Toán - Food Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; padding-bottom: 50px; }
        .navbar-brand { font-weight: bold; color: #dc3545 !important; }

        .cart-item {
            background-color: #fff;
            border-radius: 12px;
            padding: 15px;
            margin-bottom: 15px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            display: flex;
            align-items: center;
        }
        .cart-item-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
            margin-right: 15px;
        }
        .pay-option label.card {
            cursor: pointer;
            transition: border-color .2s, box-shadow .2s;
            border: 2px solid #e9ecef;
            height: 100%;
        }
        .pay-option input:checked + label.card {
            border-color: #0d6efd;
            box-shadow: 0 0 0 0.2rem rgba(13,110,253,.25);
            background-color: #f8fbff;
        }
        .btn-trash {
            color: #dc3545;
            background: rgba(220,53,69,0.1);
            border-radius: 50%;
            width: 35px; height: 35px;
            display: flex; align-items: center; justify-content: center;
            border: none; transition: 0.2s;
        }
        .btn-trash:hover { background: #dc3545; color: white; }

        .qty-control {
            display: flex;
            align-items: center;
            gap: 0;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            overflow: hidden;
            background: #fff;
        }
        .qty-btn {
            width: 34px; height: 34px;
            border: none;
            background: #f8f9fa;
            font-size: 1rem;
            font-weight: bold;
            color: #495057;
            display: flex; align-items: center; justify-content: center;
            cursor: pointer;
            transition: background 0.15s, color 0.15s;
        }
        .qty-btn:hover { background: #0d6efd; color: #fff; }
        .qty-btn:active { transform: scale(0.92); }
        .qty-btn.decrease:hover { background: #dc3545; color: #fff; }
        .qty-value {
            min-width: 36px;
            text-align: center;
            font-weight: bold;
            font-size: 0.95rem;
            color: #212529;
            border-left: 1px solid #dee2e6;
            border-right: 1px solid #dee2e6;
            padding: 4px 0;
            background: #fff;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand fs-4" href="${pageContext.request.contextPath}/menu">🍔 Food<span class="text-dark">Order</span></a>

        <div class="d-flex align-items-center">
            <a href="${pageContext.request.contextPath}/menu" class="btn btn-outline-secondary btn-sm me-3 rounded-pill">
                <i class="bi bi-arrow-left"></i> Tiếp tục chọn món
            </a>
            <span class="fw-bold text-secondary d-none d-md-inline">
                <i class="bi bi-person-circle"></i> ${user.fullName}
            </span>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <!-- Cột Danh sách Giỏ hàng -->
        <div class="col-lg-7 mb-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3 class="fw-bold text-dark mb-0"><i class="bi bi-bag-check text-primary"></i> Giỏ Hàng Của Bạn</h3>
                <span id="cartCountBadge" class="badge bg-primary rounded-pill fs-6">${cartCount} món</span>
            </div>

            <c:if test="${not empty checkoutError}">
                <div class="alert alert-danger shadow-sm border-0 border-start border-4 border-danger"><i class="bi bi-exclamation-triangle-fill text-danger me-2"></i> ${checkoutError}</div>
            </c:if>

            <c:if test="${empty cartItems}">
                <div class="text-center py-5 bg-white rounded shadow-sm border">
                    <img src="https://cdn-icons-png.flaticon.com/512/11329/11329060.png" alt="Empty Cart" style="width: 120px; opacity: 0.5" class="mb-3">
                    <h4 class="text-muted fw-bold">Giỏ hàng đang trống!</h4>
                    <p class="text-muted">Bạn chưa chọn món nào. Hãy dạo quanh thực đơn nhé!</p>
                    <a href="${pageContext.request.contextPath}/menu" class="btn btn-primary px-4 py-2 mt-2 fw-bold">Khám phá Thực Đơn</a>
                </div>
            </c:if>

            <!-- Lặp qua các món trong giỏ -->
            <c:forEach items="${cartItems}" var="item">
                <div class="cart-item position-relative" id="cart-item-${item.orderItemId}">
                    <c:choose>
                        <c:when test="${not empty item.dish.imageUrl}">
                            <img src="${item.dish.imageUrl}" alt="${item.dish.name}" class="cart-item-img border">
                        </c:when>
                        <c:otherwise>
                            <img src="https://placehold.co/80x80?text=No+Img" alt="No image" class="cart-item-img border opacity-50">
                        </c:otherwise>
                    </c:choose>

                    <div class="flex-grow-1">
                        <div class="d-flex justify-content-between align-items-start">
                            <h5 class="fw-bold text-dark mb-1">${item.dish.name}</h5>

                            <!-- Form Xóa Món -->
                            <form action="${pageContext.request.contextPath}/cart/remove-item" method="post" class="m-0 p-0">
                                <input type="hidden" name="orderItemId" value="${item.orderItemId}">
                                <button type="submit" class="btn-trash" title="Xóa món này">
                                    <i class="bi bi-trash3"></i>
                                </button>
                            </form>
                        </div>

                        <div class="text-muted small mb-2 d-flex flex-wrap gap-1">
                            <span class="badge bg-light text-dark border"><i class="bi bi-tag"></i> <fmt:formatNumber value="${item.unitPriceAtPurchase}" type="number" pattern="###,###"/>đ /phần</span>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mt-2">
                            <!-- Nút tăng/giảm số lượng -->
                            <div class="qty-control">
                                <button type="button" class="qty-btn decrease" onclick="updateQuantity('${item.orderItemId}', 'decrease')" title="Giảm">
                                    <i class="bi bi-dash"></i>
                                </button>
                                <span class="qty-value" id="qty-${item.orderItemId}">${item.quantity}</span>
                                <button type="button" class="qty-btn increase" onclick="updateQuantity('${item.orderItemId}', 'increase')" title="Tăng">
                                    <i class="bi bi-plus"></i>
                                </button>
                            </div>
                            <h5 class="mb-0 fw-bold text-danger" id="subtotal-${item.orderItemId}"><fmt:formatNumber value="${item.calculateSubTotal()}" type="number" pattern="###,###"/>đ</h5>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Cột Thanh Toán (Builder & Strategy) -->
        <div class="col-lg-5">
            <div class="card border-0 shadow-sm sticky-top" style="top: 80px; z-index: 10;">
                <div class="card-header bg-white border-bottom py-3">
                    <h4 class="mb-0 fw-bold text-dark"><i class="bi bi-receipt"></i> Thanh Toán</h4>
                </div>
                <div class="card-body p-4">

                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Tổng tiền món</span>
                        <span class="fw-bold"><span id="cartTotalAmount"><fmt:formatNumber value="${cartTotal}" type="number" pattern="###,###"/></span>đ</span>
                    </div>
                    <div class="d-flex justify-content-between mb-3 border-bottom pb-3">
                        <span class="text-muted">Phí giao hàng dự kiến</span>
                        <span class="fw-bold text-success">+ 15,000đ</span>
                    </div>
                    <div class="d-flex justify-content-between mb-4">
                        <span class="fs-5 fw-bold text-dark">Tổng Cần Thanh Toán</span>
                        <span class="fs-4 fw-bold text-danger"><span id="cartGrandTotal"><fmt:formatNumber value="${cartTotal + 15000}" type="number" pattern="###,###"/></span>đ</span>
                    </div>

                    <!-- Form Đặt hàng gửi tới Controller -->
                    <form action="${pageContext.request.contextPath}/cart/checkout" method="post">
                        <h6 class="fw-bold mb-3 text-secondary">Thông tin nhận hàng</h6>
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="custName" name="customerName" value="${user.fullName}" required placeholder="Tên">
                            <label for="custName">Tên người nhận</label>
                        </div>
                        <div class="form-floating mb-4">
                            <textarea class="form-control" id="custAddress" name="address" style="height: 80px" required placeholder="Địa chỉ"></textarea>
                            <label for="custAddress">Địa chỉ giao hàng chi tiết</label>
                        </div>

                        <h6 class="fw-bold mb-3 text-secondary">Phương thức thanh toán</h6>
                        <div class="row g-2 mb-4">
                            <div class="col-6 pay-option">
                                <input class="d-none" type="radio" name="paymentMethod" id="payCod" value="COD" checked>
                                <label class="card p-3 mb-0 text-center shadow-none" for="payCod">
                                    <i class="bi bi-cash-stack fs-2 text-success mb-2"></i>
                                    <div class="fw-bold text-dark">Tiền mặt</div>
                                    <small class="text-muted" style="font-size: 0.7rem">Khi nhận hàng</small>
                                </label>
                            </div>
                            <div class="col-6 pay-option">
                                <input class="d-none" type="radio" name="paymentMethod" id="payBank" value="BANKING">
                                <label class="card p-3 mb-0 text-center shadow-none" for="payBank">
                                    <i class="bi bi-bank2 fs-2 text-primary mb-2"></i>
                                    <div class="fw-bold text-dark">Chuyển khoản</div>
                                    <small class="text-muted" style="font-size: 0.7rem">Mở app NH</small>
                                </label>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-danger w-100 btn-lg fw-bold shadow" ${empty cartItems ? 'disabled' : ''}>
                            <i class="bi bi-check2-circle"></i> XÁC NHẬN ĐẶT HÀNG
                        </button>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function formatVND(num) {
        return Math.round(num).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    function updateQuantity(orderItemId, action) {
        const formData = new FormData();
        formData.append('orderItemId', orderItemId);
        formData.append('action', action);

        fetch('${pageContext.request.contextPath}/cart/update-quantity', {
            method: 'POST',
            body: formData
        })
        .then(res => res.json())
        .then(data => {
            if (data.error) {
                alert(data.error);
                return;
            }

            if (data.removed) {
                // Món đã bị xóa (quantity = 0) -> animate rồi remove DOM
                const el = document.getElementById('cart-item-' + orderItemId);
                if (el) {
                    el.style.transition = 'opacity 0.3s, transform 0.3s';
                    el.style.opacity = '0';
                    el.style.transform = 'translateX(-30px)';
                    setTimeout(() => el.remove(), 300);
                }
            } else {
                // Cập nhật số lượng và subtotal
                document.getElementById('qty-' + orderItemId).textContent = data.newQuantity;
                document.getElementById('subtotal-' + orderItemId).textContent = formatVND(data.subTotal) + 'đ';
            }

            // Cập nhật tổng tiền và badge
            document.getElementById('cartTotalAmount').textContent = formatVND(data.cartTotal);
            document.getElementById('cartGrandTotal').textContent = formatVND(data.cartTotal + 15000);
            document.getElementById('cartCountBadge').textContent = data.cartCount + ' món';

            // Nếu giỏ trống -> reload trang để hiện UI "giỏ hàng trống"
            if (data.cartCount === 0) {
                location.reload();
            }
        })
        .catch(err => {
            console.error('Error:', err);
            alert('Lỗi kết nối!');
        });
    }
</script>
</body>
</html>