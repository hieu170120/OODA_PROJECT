<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt Hàng - Cửa Hàng Food Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; }
        .card { border-radius: 15px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); margin-bottom: 20px;}
        .dish-card { border: 1px solid #e9ecef; border-radius: 10px; padding: 15px; margin-bottom: 15px; transition: transform 0.2s;}
        .dish-card:hover { transform: translateY(-3px); box-shadow: 0 4px 10px rgba(0,0,0,0.1); border-color: #0d6efd;}
        .dish-img { width: 80px; height: 80px; object-fit: cover; border-radius: 8px; }
        .topping-list { padding-left: 20px; font-style: italic; color: #6c757d;}
        .pay-option label.card { cursor: pointer; transition: border-color .2s, box-shadow .2s; border: 2px solid #e9ecef; }
        .pay-option input:checked + label.card { border-color: #0d6efd; box-shadow: 0 0 0 0.2rem rgba(13,110,253,.25); }
        .navbar-brand { font-weight: bold; color: #dc3545 !important; }
        .cart-item { background-color: #fff; border-radius: 10px; padding: 12px; margin-bottom: 10px; border-left: 4px solid #0d6efd;}
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">🍔 Food<span class="text-dark">Order</span></a>
        <div class="d-flex align-items-center">
            <span class="me-3 fw-bold text-secondary"><i class="bi bi-person-circle"></i> Xin chào, ${user.fullName}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Đăng xuất</a>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <!-- Cột Menu Món Ăn -->
        <div class="col-md-7">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2 class="text-danger mb-0"><i class="bi bi-menu-button-wide"></i> Menu Hôm Nay</h2>
                <span class="badge bg-danger rounded-pill fs-6">${menu.size()} món</span>
            </div>

            <form action="${pageContext.request.contextPath}/order/add-item" method="post">
                <div class="card p-4 border-0">
                    <h5 class="mb-3 text-secondary">1. Chọn món chính (Từ Database)</h5>

                    <div style="max-height: 400px; overflow-y: auto; padding-right: 10px;">
                        <c:forEach items="${menu}" var="dish" varStatus="loop">
                            <label class="dish-card d-flex w-100" style="cursor: pointer;">
                                <div class="me-3 d-flex align-items-center">
                                    <input class="form-check-input fs-5" type="radio" name="baseDish" value="${dish.name}_${dish.price}" required ${loop.index == 0 ? 'checked' : ''}>
                                </div>
                                <div class="d-flex w-100">
                                    <!-- HIỂN THỊ ẢNH -->
                                    <c:choose>
                                        <c:when test="${not empty dish.imageUrl}">
                                            <img src="${dish.imageUrl}" alt="${dish.name}" class="dish-img me-3">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="https://placehold.co/80x80?text=Food" alt="No image" class="dish-img me-3">
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="flex-grow-1">
                                        <h6 class="mb-1 fw-bold text-dark">${dish.name}</h6>
                                        <p class="text-muted small mb-1" style="display: -webkit-box; -webkit-line-clamp: 2; line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">${dish.description}</p>
                                        <span class="text-danger fw-bold"><fmt:formatNumber value="${dish.price}" type="number" pattern="###,###"/> đ</span>
                                    </div>
                                </div>
                            </label>
                        </c:forEach>
                    </div>

                    <h5 class="mt-4 mb-3 text-secondary">2. Chọn thêm Topping (Decorator)</h5>
                    <!-- Container: JS sẽ render topping phù hợp khi chọn món chính -->
                    <div id="orderToppingContainer" class="row g-2">
                        <!-- Toppings sẽ được render động -->
                    </div>

                    <div class="row mt-4 align-items-end">
                        <div class="col-md-4">
                            <label class="form-label fw-bold text-secondary">Số lượng:</label>
                            <input type="number" name="quantity" class="form-control" value="1" min="1">
                        </div>
                        <div class="col-md-8">
                            <button type="submit" class="btn btn-warning w-100 text-dark fw-bold" style="height: 38px;">
                                <i class="bi bi-cart-plus"></i> Thêm vào Giỏ Hàng
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <!-- Cột Giỏ Hàng & Thanh Toán -->
        <div class="col-md-5">
            <h2 class="mb-3 text-primary"><i class="bi bi-bag-check"></i> Giỏ Hàng</h2>

            <c:if test="${not empty checkoutError}">
                <div class="alert alert-danger shadow-sm"><i class="bi bi-exclamation-triangle-fill"></i> ${checkoutError}</div>
            </c:if>

            <div class="card p-3 bg-light border-0 shadow-sm mb-4">
                <div style="max-height: 300px; overflow-y: auto;" class="pe-2">
                    <c:forEach items="${cartItems}" var="item">
                        <div class="cart-item position-relative shadow-sm">
                            <div class="d-flex justify-content-between align-items-start">
                                <div class="pe-3">
                                    <h6 class="mb-1 fw-bold text-primary">${item.dish.name}</h6>
                                    <div class="text-muted small mb-1">
                                        Đơn giá: <fmt:formatNumber value="${item.unitPriceAtPurchase}" type="number" pattern="###,###"/>đ
                                        x ${item.quantity}
                                    </div>
                                    <h6 class="mb-0 text-danger fw-bold"><fmt:formatNumber value="${item.calculateSubTotal()}" type="number" pattern="###,###"/> đ</h6>
                                </div>

                                <!-- Nút Xóa Món (Form ẩn) -->
                                <form action="${pageContext.request.contextPath}/order/remove-item" method="post" class="m-0 p-0">
                                    <input type="hidden" name="orderItemId" value="${item.orderItemId}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger border-0" title="Xóa món này">
                                        <i class="bi bi-trash3"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>

                    <c:if test="${empty cartItems}">
                        <div class="text-center p-4 text-muted">
                            <i class="bi bi-cart-x fs-1 opacity-50 d-block mb-2"></i>
                            Giỏ hàng trống. Hãy chọn món bên trái!
                        </div>
                    </c:if>
                </div>

                <c:if test="${not empty cartItems}">
                    <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top border-secondary">
                        <span class="fs-5 text-secondary">Tổng cộng:</span>
                        <span class="fs-4 fw-bold text-danger"><fmt:formatNumber value="${cartTotal}" type="number" pattern="###,###"/> VNĐ</span>
                    </div>
                </c:if>
            </div>

            <!-- Form Thanh Toán (Builder & Strategy) -->
            <form action="${pageContext.request.contextPath}/order/checkout" method="post">
                <div class="card p-4 border-0 shadow-sm">
                    <h5 class="mb-3 text-secondary"><i class="bi bi-truck"></i> Giao Hàng & Thanh Toán</h5>

                    <div class="mb-3">
                        <label class="form-label text-muted small mb-1">Tên người nhận</label>
                        <input type="text" class="form-control" name="customerName" value="${user.fullName}" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-muted small mb-1">Địa chỉ nhận hàng</label>
                        <textarea class="form-control" name="address" rows="2" required placeholder="Nhập địa chỉ nhà, tòa nhà, đường..."></textarea>
                    </div>

                    <label class="form-label text-muted small mb-2 mt-2">Phương thức thanh toán</label>
                    <div class="row g-2 mb-3">
                        <div class="col-6 pay-option">
                            <input class="d-none" type="radio" name="paymentMethod" id="payCod" value="COD" checked>
                            <label class="card p-2 mb-0 h-100 text-center" for="payCod">
                                <i class="bi bi-cash-coin fs-3 text-success mb-1"></i>
                                <div class="fw-bold small">Tiền mặt (COD)</div>
                            </label>
                        </div>
                        <div class="col-6 pay-option">
                            <input class="d-none" type="radio" name="paymentMethod" id="payBank" value="BANKING">
                            <label class="card p-2 mb-0 h-100 text-center" for="payBank">
                                <i class="bi bi-bank fs-3 text-primary mb-1"></i>
                                <div class="fw-bold small">Chuyển khoản</div>
                            </label>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 btn-lg shadow-sm" ${empty cartItems ? 'disabled' : ''}>
                        <i class="bi bi-check2-circle"></i> Đặt Hàng Ngay
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // ================================================
    // DỮ LIỆU TOPPING TĨNH - PHÙ HỢP THEO TỪNG MÓN
    // ================================================
    const TOPPING_DATA = {
        'Hamburger Bò Phô Mai': [
            { name: 'Thêm phô mai',       price: 8000,  icon: '🧀' },
            { name: 'Thêm patty bò',      price: 15000, icon: '🥩' },
            { name: 'Bacon',               price: 12000, icon: '🥓' },
            { name: 'Trứng ốp la',        price: 8000,  icon: '🍳' },
            { name: 'Xà lách thêm',       price: 3000,  icon: '🥬' },
        ],
        'Kebab Thịt Gà': [
            { name: 'Thêm thịt gà',       price: 15000, icon: '🍗' },
            { name: 'Phô mai',            price: 8000,  icon: '🧀' },
            { name: 'Sốt tỏi thêm',      price: 5000,  icon: '🧄' },
            { name: 'Rau sống thêm',      price: 3000,  icon: '🥗' },
            { name: 'Ớt cay',             price: 3000,  icon: '🌶️' },
        ],
        'Gà Rán Giòn (3 miếng)': [
            { name: 'Thêm 2 miếng gà',    price: 25000, icon: '🍗' },
            { name: 'Sốt cay Hàn Quốc',   price: 5000,  icon: '🌶️' },
            { name: 'Khoai tây nghiền',   price: 10000, icon: '🥔' },
            { name: 'Coleslaw',            price: 8000,  icon: '🥗' },
        ],
        'Pizza Hải Sản': [
            { name: 'Phô mai thêm',       price: 15000, icon: '🧀' },
            { name: 'Xúc xích',           price: 12000, icon: '🌭' },
            { name: 'Thêm hải sản',       price: 20000, icon: '🦐' },
            { name: 'Viền phô mai',       price: 18000, icon: '🫓' },
        ],
        'Hot Dog Xúc Xích': [
            { name: 'Phô mai',            price: 8000,  icon: '🧀' },
            { name: 'Thêm xúc xích',      price: 12000, icon: '🌭' },
            { name: 'Hành phi',            price: 5000,  icon: '🧅' },
            { name: 'Jalapeño',            price: 5000,  icon: '🌶️' },
        ],
        'Khoai Tây Chiên (L)': [
            { name: 'Sốt phô mai',        price: 8000,  icon: '🧀' },
            { name: 'Sốt cà chua thêm',   price: 3000,  icon: '🍅' },
            { name: 'Muối rắc tiêu',      price: 2000,  icon: '🧂' },
            { name: 'Sốt BBQ',            price: 5000,  icon: '🥫' },
        ],
        'Trà Sữa Trân Châu': [
            { name: 'Trân châu đen',      price: 5000,  icon: '⚫' },
            { name: 'Trân châu trắng',    price: 5000,  icon: '⚪' },
            { name: 'Thạch dừa',          price: 7000,  icon: '🥥' },
            { name: 'Pudding trứng',      price: 8000,  icon: '🍮' },
            { name: 'Size Lớn (L)',       price: 10000, icon: '⬆️' },
        ],
        'Coca Cola (L)': [
            { name: 'Thêm đá',            price: 0,     icon: '🧊' },
            { name: 'Chanh tươi',         price: 5000,  icon: '🍋' },
        ],
        '_default': [
            { name: 'Phô mai',            price: 10000, icon: '🧀' },
            { name: 'Trứng ốp la',        price: 8000,  icon: '🍳' },
            { name: 'Xúc xích',           price: 12000, icon: '🌭' },
            { name: 'Size Lớn (L)',       price: 15000, icon: '⬆️' },
        ]
    };

    function formatVND(num) {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    // Render topping vào container dạng grid 2 cột
    function renderOrderToppings(dishName) {
        const container = document.getElementById('orderToppingContainer');
        const toppings = TOPPING_DATA[dishName] || TOPPING_DATA['_default'];

        if (!toppings || toppings.length === 0) {
            container.innerHTML = '<div class="col-12 text-center text-muted py-2"><em>Không có topping cho món này</em></div>';
            return;
        }

        let html = '';
        toppings.forEach(function(t, idx) {
            html += '<div class="col-md-6">'
                + '<div class="form-check p-2 border rounded" style="cursor:pointer; transition: all 0.2s;">'
                + '<input class="form-check-input ms-1" type="checkbox" name="toppings"'
                + ' value="' + t.name + '_' + t.price + '" id="order-top-' + idx + '">'
                + '<label class="form-check-label ms-2 w-100" for="order-top-' + idx + '" style="cursor:pointer;">'
                + '<span style="font-size:1.1rem">' + t.icon + '</span> ' + t.name
                + '<span class="float-end text-muted fw-bold">+' + formatVND(t.price) + 'đ</span>'
                + '</label>'
                + '</div>'
                + '</div>';
        });

        container.innerHTML = html;
    }

    // Lắng nghe thay đổi radio button chọn món chính → render topping
    document.querySelectorAll('input[name="baseDish"]').forEach(radio => {
        radio.addEventListener('change', function() {
            // Giá trị radio: "TênMón_Giá" => lấy tên món
            const dishName = this.value.split('_')[0];
            renderOrderToppings(dishName);
        });
    });

    // Render topping cho món đầu tiên (đã checked sẵn)
    const checkedDish = document.querySelector('input[name="baseDish"]:checked');
    if (checkedDish) {
        renderOrderToppings(checkedDish.value.split('_')[0]);
    }
</script>
</body>
</html>