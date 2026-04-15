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

                    <!-- DTO không có ảnh, dùng ảnh mặc định cho CartItem -->
                    <img src="https://placehold.co/80x80?text=Dish" alt="No image" class="cart-item-img border opacity-50">

                    <div class="flex-grow-1">
                        <div class="d-flex justify-content-between align-items-start">
                            <!-- Đổi từ item.dish.name sang item.dishName vì DTO đã phẳng hóa dữ liệu -->
                            <h5 class="fw-bold text-dark mb-1">${item.dishName}</h5>

                            <!-- Form Xóa Món -->
                            <form action="${pageContext.request.contextPath}/cart/remove-item" method="post" class="m-0 p-0">
                                <input type="hidden" name="orderItemId" value="${item.orderItemId}">
                                <button type="submit" class="btn-trash" title="Xóa món này">
                                    <i class="bi bi-trash3"></i>
                                </button>
                            </form>
                        </div>

                        <div class="text-muted small mb-2 d-flex flex-wrap gap-1">
                            <!-- Đổi từ item.unitPriceAtPurchase sang item.unitPrice theo DTO -->
                            <span class="badge bg-light text-dark border"><i class="bi bi-tag"></i> <fmt:formatNumber value="${item.unitPrice}" type="number" pattern="###,###"/>đ /phần</span>
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
                            <!-- Đổi từ hàm item.calculateSubTotal() sang thuộc tính item.subTotal của DTO -->
                            <h5 class="mb-0 fw-bold text-danger" id="subtotal-${item.orderItemId}"><fmt:formatNumber value="${item.subTotal}" type="number" pattern="###,###"/>đ</h5>
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
                    <div class="d-flex justify-content-between mb-1" id="couponDiscountRow" style="display: none;">
                        <span class="text-muted">Giảm giá coupon</span>
                        <span class="fw-bold text-success">- <span id="couponDiscountAmount">0</span>đ</span>
                    </div>
                    <div id="couponAppliedLabel" class="small text-muted mb-3"></div>
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

                        <h6 class="fw-bold mb-2 text-secondary">Mã giảm giá</h6>
                        <div class="input-group mb-2">
                            <span class="input-group-text"><i class="bi bi-ticket-perforated"></i></span>
                            <input type="text" class="form-control" id="couponCodeInput" name="couponCode" placeholder="Nhập coupon (ví dụ: SAVE10)" value="${enteredCouponCode}">
                            <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#couponModal">
                                Chọn coupon phù hợp
                            </button>
                        </div>
                        <div class="small text-muted mb-4">Bạn có thể nhập mã trực tiếp hoặc bấm nút để hệ thống gợi ý mã hợp lệ theo giỏ hiện tại.</div>

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

<div class="modal fade" id="couponModal" tabindex="-1" aria-labelledby="couponModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-light">
                <h5 class="modal-title fw-bold" id="couponModalLabel"><i class="bi bi-ticket-perforated text-primary"></i> Chọn Coupon Phù Hợp</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <c:if test="${empty couponOptions}">
                    <div class="alert alert-secondary mb-0">Hiện tại chưa có coupon khả dụng.</div>
                </c:if>

                <c:forEach items="${couponOptions}" var="opt">
                    <div class="border rounded-3 p-3 mb-3">
                        <div class="d-flex justify-content-between align-items-start mb-2">
                            <div>
                                <div class="fw-bold fs-6">${opt.code}</div>
                                <div class="text-muted small">${opt.description}</div>
                            </div>
                            <c:choose>
                                <c:when test="${opt.eligible}">
                                    <span class="badge bg-success">Phù hợp</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">Chưa đủ điều kiện</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="small mb-2 ${opt.eligible ? 'text-success' : 'text-muted'}">${opt.reason}</div>

                        <div class="d-flex justify-content-between align-items-center">
                            <div class="fw-semibold text-danger">
                                Ước tính giảm: <fmt:formatNumber value="${opt.estimatedDiscount}" type="number" pattern="###,###"/>đ
                            </div>
                            <button type="button"
                                    class="btn btn-sm ${opt.eligible ? 'btn-primary' : 'btn-outline-secondary'}"
                                    onclick="applyCouponFromModal('${opt.code}', ${opt.eligible}, ${opt.estimatedDiscount})"
                                    ${opt.eligible ? '' : 'disabled'}>
                                ${opt.eligible ? 'Dùng mã này' : 'Không thể áp dụng'}
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const SHIPPING_FEE = 15000;
    let currentCartTotal = ${cartTotal};
    const couponOptionsMap = {};
    <c:forEach items="${couponOptions}" var="opt">
    couponOptionsMap['${opt.code}'] = { eligible: ${opt.eligible}, discount: ${opt.estimatedDiscount} };
    </c:forEach>

    function formatVND(num) {
        return Math.round(num).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    function normalizeCouponCode(code) {
        return (code || '').trim().toUpperCase();
    }

    function getAppliedCouponData() {
        const couponInput = document.getElementById('couponCodeInput');
        const normalizedCode = normalizeCouponCode(couponInput ? couponInput.value : '');

        if (!normalizedCode) {
            return { code: '', discount: 0, eligible: false, knownCode: false };
        }

        const option = couponOptionsMap[normalizedCode];
        if (!option) {
            return { code: normalizedCode, discount: 0, eligible: false, knownCode: false };
        }

        if (!option.eligible || option.discount <= 0) {
            return { code: normalizedCode, discount: 0, eligible: false, knownCode: true };
        }

        return { code: normalizedCode, discount: option.discount, eligible: true, knownCode: true };
    }

    function refreshPaymentSummary(cartTotal) {
        const couponRow = document.getElementById('couponDiscountRow');
        const couponAmountEl = document.getElementById('couponDiscountAmount');
        const couponLabelEl = document.getElementById('couponAppliedLabel');
        const cartTotalEl = document.getElementById('cartTotalAmount');
        const grandTotalEl = document.getElementById('cartGrandTotal');

        const applied = getAppliedCouponData();
        const discount = Math.min(applied.discount, cartTotal);
        const grandTotal = Math.max(cartTotal + SHIPPING_FEE - discount, 0);

        cartTotalEl.textContent = formatVND(cartTotal);
        grandTotalEl.textContent = formatVND(grandTotal);

        if (discount > 0) {
            couponRow.style.display = 'flex';
            couponAmountEl.textContent = formatVND(discount);
            couponLabelEl.textContent = 'Đang áp dụng mã: ' + applied.code;
            couponLabelEl.className = 'small text-success mb-3';
            return;
        }

        couponRow.style.display = 'none';
        couponAmountEl.textContent = '0';

        if (applied.code && applied.knownCode) {
            couponLabelEl.textContent = 'Mã ' + applied.code + ' chưa đủ điều kiện với giỏ hiện tại.';
            couponLabelEl.className = 'small text-muted mb-3';
            return;
        }

        if (applied.code && !applied.knownCode) {
            couponLabelEl.textContent = 'Mã ' + applied.code + ' không tồn tại trong danh sách hiện tại.';
            couponLabelEl.className = 'small text-muted mb-3';
            return;
        }

        couponLabelEl.textContent = '';
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
            currentCartTotal = data.cartTotal;
            refreshPaymentSummary(currentCartTotal);
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

    function applyCouponFromModal(code, eligible, estimatedDiscount) {
        if (!eligible) {
            return;
        }

        const couponInput = document.getElementById('couponCodeInput');
        if (couponInput) {
            couponInput.value = code;
        }

        if (!couponOptionsMap[code]) {
            couponOptionsMap[code] = { eligible: true, discount: estimatedDiscount || 0 };
        }

        refreshPaymentSummary(currentCartTotal);

        const modalElement = document.getElementById('couponModal');
        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        if (modalInstance) {
            modalInstance.hide();
        }
    }

    const couponCodeInput = document.getElementById('couponCodeInput');
    if (couponCodeInput) {
        couponCodeInput.addEventListener('input', function () {
            refreshPaymentSummary(currentCartTotal);
        });
    }

    refreshPaymentSummary(currentCartTotal);
</script>
</body>
</html>