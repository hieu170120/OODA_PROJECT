<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt Hàng - Demo Decorator &amp; Builder</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .card { border-radius: 15px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); margin-bottom: 20px;}
        .topping-list { padding-left: 20px; font-style: italic; color: #6c757d;}
        .pay-option label.card { cursor: pointer; transition: border-color .2s, box-shadow .2s; border: 2px solid #e9ecef; }
        .pay-option input:checked + label.card { border-color: #0d6efd; box-shadow: 0 0 0 0.2rem rgba(13,110,253,.25); }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-7">
            <h2 class="mb-4 text-danger">🍗 Menu Gà Rán &amp; Burger</h2>

            <form action="${pageContext.request.contextPath}/order/add-item" method="post">
                <div class="card p-4">
                    <h4>1. Chọn món chính (Base Dish)</h4>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="baseDish" id="dish1" value="Gà rán giòn_35000" checked>
                        <label class="form-check-label fw-bold" for="dish1">Gà rán giòn - 35,000 VNĐ</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="baseDish" id="dish2" value="Burger Bò_45000">
                        <label class="form-check-label fw-bold" for="dish2">Burger Bò - 45,000 VNĐ</label>
                    </div>

                    <h4 class="mt-4">2. Chọn Topping (Decorator Pattern)</h4>
                    <p class="text-muted small">Bọc món ăn của bạn với nhiều loại Topping khác nhau.</p>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="toppings" value="Phô mai_10000" id="top1">
                        <label class="form-check-label" for="top1">+ Phô mai (10,000 VNĐ)</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="toppings" value="Trứng ốp la_8000" id="top2">
                        <label class="form-check-label" for="top2">+ Trứng ốp la (8,000 VNĐ)</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="toppings" value="Tương ớt cay_2000" id="top3">
                        <label class="form-check-label" for="top3">+ Tương ớt cay (2,000 VNĐ)</label>
                    </div>

                    <div class="mt-3">
                        <label class="form-label fw-bold">Số lượng:</label>
                        <input type="number" name="quantity" class="form-control w-25" value="1" min="1">
                    </div>

                    <button type="submit" class="btn btn-warning mt-4 text-dark fw-bold">Thêm vào Đơn hàng 🛒</button>
                </div>
            </form>
        </div>

        <div class="col-md-5">
            <h2 class="mb-4 text-primary">📦 Đơn Hàng Của Bạn</h2>
            <div class="card p-4">
                <h5 class="border-bottom pb-2">Chi tiết món ăn:</h5>
                <c:if test="${not empty checkoutError}">
                    <div class="alert alert-danger mt-2 mb-3">${checkoutError}</div>
                </c:if>

                <ul class="list-group mb-3">
                    <c:forEach items="${cartItems}" var="item">
                        <li class="list-group-item d-flex justify-content-between lh-sm">
                            <div>
                                <h6 class="my-0 text-primary">${item.dish.name}</h6>
                                <small class="text-muted">Số lượng: ${item.quantity}</small>
                            </div>
                            <span class="text-muted fw-bold">${item.calculateSubTotal()}đ</span>
                        </li>
                    </c:forEach>
                    <c:if test="${empty cartItems}">
                        <li class="list-group-item bg-light text-center text-muted">
                            Chưa có món nào.
                        </li>
                    </c:if>
                </ul>

                <form action="${pageContext.request.contextPath}/order/checkout" method="post">
                    <h5 class="border-bottom pb-2 mt-4">Thông tin Giao hàng (Builder Pattern):</h5>
                    <div class="mb-3">
                        <label class="form-label">Tên Khách Hàng</label>
                        <input type="text" class="form-control" name="customerName" required placeholder="Nguyễn Văn A">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Địa chỉ giao hàng</label>
                        <textarea class="form-control" name="address" required placeholder="123 Nguyễn Huệ..."></textarea>
                    </div>

                    <h5 class="border-bottom pb-2 mt-4">Thanh toán <span class="badge bg-secondary">Strategy Pattern</span></h5>
                    <p class="text-muted small">Chọn phương thức — ứng dụng sẽ dùng đúng <code>PaymentStrategy</code> tương ứng (COD / Banking).</p>

                    <div class="row g-2 mb-3">
                        <div class="col-12 pay-option">
                            <input class="d-none" type="radio" name="paymentMethod" id="payCod" value="COD" checked>
                            <label class="card p-3 mb-0" for="payCod">
                                <div class="fw-bold text-primary">💵 Thanh toán khi nhận hàng (COD)</div>
                                <small class="text-muted">Strategy: <code>CodPaymentStrategy</code> — xác nhận đơn, thu tiền khi giao.</small>
                            </label>
                        </div>
                        <div class="col-12 pay-option">
                            <input class="d-none" type="radio" name="paymentMethod" id="payBank" value="BANKING">
                            <label class="card p-3 mb-0" for="payBank">
                                <div class="fw-bold text-success">🏦 Chuyển khoản ngân hàng</div>
                                <small class="text-muted">Strategy: <code>BankingPaymentStrategy</code> — giả lập đã chuyển khoản thành công.</small>
                            </label>
                        </div>
                    </div>
                    <div class="alert alert-light border small mb-3" id="bankHint" style="display:none;">
                        <strong>Gợi ý demo Banking:</strong> Sau khi đặt hàng, hệ thống gán mã giao dịch và xử lý qua strategy chuyển khoản (demo).
                    </div>

                    <div class="alert alert-info p-2 mt-3">
                        <strong>Lưu ý Builder:</strong> OrderDirector lắp Khách + Món + Địa chỉ + phí ship; <strong>Strategy</strong> xử lý thanh toán theo lựa chọn của bạn.
                    </div>

                    <c:choose>
                        <c:when test="${empty cartItems}">
                            <button type="submit" class="btn btn-primary w-100 btn-lg mt-2" disabled>Xác nhận Đặt Hàng 🚀</button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" class="btn btn-primary w-100 btn-lg mt-2">Xác nhận Đặt Hàng 🚀</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
(function () {
    var bankHint = document.getElementById('bankHint');
    var payCod = document.getElementById('payCod');
    var payBank = document.getElementById('payBank');
    function sync() {
        bankHint.style.display = payBank.checked ? 'block' : 'none';
    }
    payCod.addEventListener('change', sync);
    payBank.addEventListener('change', sync);
})();
</script>
</body>
</html>
