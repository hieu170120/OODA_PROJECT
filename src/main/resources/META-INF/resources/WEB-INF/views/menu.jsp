<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu Món Ăn - Food Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        body { background-color: #f8f9fa; padding-bottom: 50px; }
        .navbar-brand { font-weight: bold; color: #dc3545 !important; }

        .dish-card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            transition: transform 0.2s, box-shadow 0.2s;
            height: 100%;
            display: flex;
            flex-direction: column;
            overflow: hidden;
            background: #fff;
        }
        .dish-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        }
        .dish-img-wrapper {
            height: 180px;
            overflow: hidden;
            border-bottom: 1px solid #f1f1f1;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #fafafa;
        }
        .dish-img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .dish-content {
            padding: 15px;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }
        .dish-desc {
            font-size: 0.85rem;
            color: #6c757d;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            margin-bottom: 10px;
            flex-grow: 1;
        }
        .dish-price {
            font-weight: bold;
            color: #dc3545;
            font-size: 1.1rem;
        }

        /* Floating Cart Badge */
        .cart-badge {
            position: absolute;
            top: -5px;
            right: -8px;
            font-size: 0.7rem;
            padding: 4px 6px;
        }

        /* Topping Items */
        .topping-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #f8f9fa;
            border: 2px solid #eee;
            border-radius: 10px;
            padding: 10px 14px;
            margin-bottom: 8px;
            cursor: pointer;
            transition: all 0.2s ease;
            user-select: none;
        }
        .topping-item:hover {
            background: #eef6ff;
            border-color: #b8d4f0;
            transform: scale(1.015);
        }
        .topping-item.selected {
            background: #e7f1ff;
            border-color: #0d6efd;
            box-shadow: 0 2px 8px rgba(13,110,253,0.15);
        }
        .topping-icon {
            font-size: 1.2rem;
            margin-right: 8px;
            line-height: 1;
        }
        .topping-empty {
            text-align: center;
            color: #adb5bd;
            padding: 20px 15px;
            font-style: italic;
        }
        .topping-price-tag {
            background: #fff3cd;
            color: #856404;
            padding: 2px 8px;
            border-radius: 20px;
            font-size: 0.78rem;
            font-weight: 600;
            white-space: nowrap;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4 sticky-top">
    <div class="container">
        <a class="navbar-brand fs-4" href="${pageContext.request.contextPath}/menu">🍔 Food<span class="text-dark">Order</span></a>

        <div class="d-flex align-items-center">
            <span class="me-3 fw-bold text-secondary d-none d-md-inline">
                <i class="bi bi-person-circle"></i> Xin chào, ${user.fullName}
            </span>

            <a href="${pageContext.request.contextPath}/cart" class="btn btn-warning me-3 position-relative d-flex align-items-center shadow-sm">
                <i class="bi bi-cart3 fs-5 me-1"></i> <span class="fw-bold d-none d-md-inline">Giỏ Hàng</span>
                <span id="cartCountBadge" class="position-absolute translate-middle badge rounded-pill bg-danger cart-badge" <c:if test="${cartCount == 0}">style="display:none;"</c:if>>
                    ${cartCount}
                    <span class="visually-hidden">món trong giỏ</span>
                </span>
            </a>

            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-secondary btn-sm" title="Đăng xuất">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>
</nav>

<div class="container">
    <div class="d-flex justify-content-between align-items-end mb-4">
        <div>
            <h2 class="text-dark fw-bold mb-1">Khám phá Thực Đơn</h2>
            <p class="text-muted mb-0">Hôm nay bạn muốn ăn gì?</p>
        </div>
        <span class="badge bg-light text-dark border p-2 fs-6"><i class="bi bi-card-list text-danger"></i> ${menu.size()} món ăn</span>
    </div>

    <!-- Grid Món Ăn (Responsive: 1 cột mobile, 2 cột tablet, 4 cột desktop, 6 cột màn siêu rộng) -->
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 row-cols-xl-6 g-4">
        <c:forEach items="${menu}" var="dish">
            <div class="col">
                <div class="dish-card">
                    <div class="dish-img-wrapper">
                        <c:choose>
                            <c:when test="${not empty dish.imageUrl}">
                                <img src="${dish.imageUrl}" alt="${dish.name}" class="dish-img">
                            </c:when>
                            <c:otherwise>
                                <img src="https://placehold.co/400x300?text=No+Image" alt="No image" class="dish-img opacity-50">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="dish-content">
                        <h6 class="fw-bold mb-1 text-dark">${dish.name}</h6>
                        <div class="dish-desc">${dish.description}</div>
                        <div class="d-flex justify-content-between align-items-center mt-2">
                            <span class="dish-price"><fmt:formatNumber value="${dish.price}" type="number" pattern="###,###"/>đ</span>

                            <!-- Bấm nút này sẽ mở Modal kèm dữ liệu món ăn -->
                            <button type="button" class="btn btn-sm btn-primary rounded-circle shadow-sm"
                                    style="width: 35px; height: 35px;"
                                    onclick="openDishModal('${dish.name}', '${dish.price}', '${dish.imageUrl}')"
                                    title="Thêm vào giỏ">
                                <i class="bi bi-plus-lg fs-5"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty menu}">
            <div class="col-12 text-center py-5">
                <i class="bi bi-emoji-frown text-muted" style="font-size: 4rem;"></i>
                <h4 class="mt-3 text-muted">Hiện tại chưa có món ăn nào trong thực đơn.</h4>
            </div>
        </c:if>
    </div>
</div>

<!-- Modal Chọn Topping (Decorator) & Add to Cart -->
<div class="modal fade" id="dishModal" tabindex="-1" aria-labelledby="dishModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header border-bottom-0 bg-light">
                <h5 class="modal-title fw-bold" id="dishModalLabel">Thêm vào giỏ</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body pt-0">
                <div class="d-flex align-items-center mb-4 pb-3 border-bottom">
                    <img id="modalDishImg" src="" alt="Img" style="width: 70px; height: 70px; object-fit: cover; border-radius: 8px;" class="me-3 border">
                    <div>
                        <h5 id="modalDishName" class="mb-1 text-primary fw-bold">Tên Món</h5>
                        <h6 id="modalDishPriceStr" class="mb-0 text-danger fw-bold">0đ</h6>
                    </div>
                </div>

                <form id="addToCartForm">
                    <!-- Dữ liệu ẩn chứa base dish -->
                    <input type="hidden" id="modalBaseDish" name="baseDish" value="">
                    <input type="hidden" id="modalImageUrl" name="imageUrl" value="">

                    <h6 class="fw-bold mb-3 text-secondary"><i class="bi bi-stars"></i> Thêm Topping (Tùy chọn)</h6>

                    <!-- Container: JS sẽ render topping phù hợp với từng món -->
                    <div id="toppingListContainer" class="mb-4">
                        <div class="topping-empty">Chọn món để xem topping phù hợp</div>
                    </div>

                    <div class="d-flex align-items-center justify-content-between">
                        <div class="d-flex align-items-center">
                            <span class="me-2 fw-bold text-secondary">Số lượng:</span>
                            <div class="input-group" style="width: 110px;">
                                <button class="btn btn-outline-secondary px-2 py-1" type="button" onclick="updateQty(-1)">-</button>
                                <input type="number" class="form-control text-center px-1 py-1" id="modalQty" name="quantity" value="1" min="1" readonly>
                                <button class="btn btn-outline-secondary px-2 py-1" type="button" onclick="updateQty(1)">+</button>
                            </div>
                        </div>

                        <h5 class="mb-0 fw-bold text-danger">
                            Tổng: <span id="modalTotalCalc">0</span>đ
                        </h5>
                    </div>
                </form>
            </div>
            <div class="modal-footer border-top-0 pt-0">
                <button type="button" class="btn btn-primary w-100 fw-bold py-2 shadow-sm" onclick="submitAddToCart()">
                    <i class="bi bi-cart-plus"></i> Thêm Vào Giỏ
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Toast Thông báo thành công góc phải -->
<div class="toast-container position-fixed bottom-0 end-0 p-3">
    <div id="successToast" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body fw-bold">
                <i class="bi bi-check-circle me-1"></i> Đã thêm món vào giỏ hàng!
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // ================================================
    // DỮ LIỆU TOPPING TĨNH - PHÙ HỢP THEO TỪNG MÓN
    // (Decorator Pattern: mỗi Topping bọc thêm lên BaseDish)
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

    // ================================================
    // BIẾN GLOBAL & DOM REFERENCES
    // ================================================
    let currentBasePrice = 0;
    const modalImg = document.getElementById('modalDishImg');
    const modalName = document.getElementById('modalDishName');
    const modalPriceStr = document.getElementById('modalDishPriceStr');
    const modalBaseDishInput = document.getElementById('modalBaseDish');
    const modalQty = document.getElementById('modalQty');
    const modalTotalCalc = document.getElementById('modalTotalCalc');
    const toppingContainer = document.getElementById('toppingListContainer');
    const dishModal = new bootstrap.Modal(document.getElementById('dishModal'));
    const successToast = new bootstrap.Toast(document.getElementById('successToast'));

    function formatVND(num) {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    // ================================================
    // RENDER TOPPING ĐỘNG THEO MÓN ĂN
    // ================================================
    function renderToppings(dishName) {
        const toppings = TOPPING_DATA[dishName] || TOPPING_DATA['_default'];

        if (!toppings || toppings.length === 0) {
            toppingContainer.innerHTML = '<div class="topping-empty"><i class="bi bi-emoji-neutral"></i> Món này không có topping</div>';
            return;
        }

        let html = '';
        toppings.forEach(function(t, idx) {
            html += '<label class="topping-item" id="topping-label-' + idx + '">'
                + '<div class="d-flex align-items-center">'
                + '<input class="form-check-input me-2 topping-checkbox" type="checkbox"'
                + ' name="toppings" value="' + t.name + '_' + t.price + '" data-price="' + t.price + '"'
                + ' onchange="toggleToppingStyle(this, ' + idx + '); calculateModalTotal();">'
                + '<span class="topping-icon">' + t.icon + '</span>'
                + '<span class="fw-medium">' + t.name + '</span>'
                + '</div>'
                + '<span class="topping-price-tag">+' + formatVND(t.price) + 'đ</span>'
                + '</label>';
        });

        toppingContainer.innerHTML = html;
    }

    // Toggle highlight khi chọn/bỏ topping
    function toggleToppingStyle(checkbox, idx) {
        const label = document.getElementById('topping-label-' + idx);
        if (label) {
            label.classList.toggle('selected', checkbox.checked);
        }
    }

    // ================================================
    // TÍNH TỔNG GIÁ TRONG MODAL
    // ================================================
    function calculateModalTotal() {
        let total = parseInt(currentBasePrice) || 0;
        document.querySelectorAll('.topping-checkbox').forEach(cb => {
            if (cb.checked) total += parseInt(cb.dataset.price);
        });
        total = total * parseInt(modalQty.value);
        modalTotalCalc.textContent = formatVND(total);
    }

    // ================================================
    // MỞ MODAL CHO MÓN ĂN
    // ================================================
    function openDishModal(name, price, imgUrl) {
        currentBasePrice = price;
        modalName.textContent = name;
        modalPriceStr.textContent = formatVND(price) + 'đ';
        modalImg.src = imgUrl ? imgUrl : 'https://placehold.co/80x80?text=Food';

        // Chuẩn bị value gửi POST (VD: "Gà rán_35000")
        modalBaseDishInput.value = name + "_" + price;
        document.getElementById('modalImageUrl').value = imgUrl ? imgUrl : '';

        // Reset form
        modalQty.value = 1;

        // Render topping PHÙ HỢP cho món đang chọn
        renderToppings(name);

        calculateModalTotal();
        dishModal.show();
    }

    function updateQty(change) {
        let current = parseInt(modalQty.value);
        current += change;
        if (current < 1) current = 1;
        modalQty.value = current;
        calculateModalTotal();
    }

    // ================================================
    // AJAX: THÊM MÓN VÀO GIỎ
    // ================================================
    function submitAddToCart() {
        const form = document.getElementById('addToCartForm');
        const formData = new FormData(form);

        fetch('${pageContext.request.contextPath}/api/cart/add', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Đóng modal
                dishModal.hide();
                // Hiện thông báo góc màn hình
                successToast.show();

                // Cập nhật con số Badge Giỏ hàng
                const badge = document.getElementById('cartCountBadge');
                badge.textContent = data.cartCount;
                badge.style.display = 'inline-block';

                // Hiệu ứng nảy badge
                badge.style.transform = 'scale(1.5)';
                setTimeout(() => badge.style.transform = 'scale(1)', 300);
            } else {
                alert('Có lỗi xảy ra: ' + (data.error || 'Vui lòng thử lại'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Lỗi kết nối mạng!');
        });
    }
</script>
</body>
</html>