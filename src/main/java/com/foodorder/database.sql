-- Cấu hình của bạn đang dùng spring.jpa.hibernate.ddl-auto=update nên Hibernate sẽ tự tạo bảng.
-- Bạn có thể chạy trực tiếp các lệnh INSERT dưới đây vào MySQL Workbench / phpMyAdmin
-- để thêm dữ liệu mẫu.

USE food_order_db;

-- ==========================================
-- BẢNG: users (Dữ liệu người dùng / nhân viên / khách hàng)
-- Các cột: user_id, full_name, email, password, phone, created_at
-- ==========================================
INSERT IGNORE INTO users (user_id, full_name, email, password, phone, created_at) VALUES 
('usr-001', 'Quản trị viên', 'admin@foodorder.com', '123456', '0901234567', NOW()),
('usr-002', 'Quản lý cửa hàng', 'manager@foodorder.com', '123456', '0912345678', NOW()),
('usr-003', 'Nguyễn Văn A', 'nva@gmail.com', '123456', '0987654321', NOW()),
('usr-004', 'Trần Thị B', 'ttb@gmail.com', '123456', '0934567890', NOW());

-- ==========================================
-- BẢNG: managers (Nhân viên quản lý)
-- Kế thừa từ users, manager_id chính là user_id của bản ghi bên bảng users
-- Các cột: manager_id
-- ==========================================
INSERT IGNORE INTO managers (manager_id) VALUES 
('usr-001'),
('usr-002');

-- ==========================================
-- BẢNG: dishes (Món ăn)
-- Các cột: dish_id, name, price, image_url, description
-- ==========================================
INSERT IGNORE INTO dishes (dish_id, name, price, image_url, description) VALUES 
('dish-001', 'Phở Bò Đặc Biệt', 65000.0, 'https://cdn.tgdd.vn/Files/2022/01/25/1412805/cach-nau-pho-b…nh-chuan-vi-thom-ngon-nhu-ngoai-hang-202201250230038502.jpg', 'Phở bò truyền thống với bò viên, nạm và gầu'),
('dish-002', 'Bún Chả Hà Nội', 55000.0, 'https://cdn.tgdd.vn/Files/2021/08/17/1375685/tu-lam-bun-cha…on-gian-dam-da-huong-vi-truyen-thong-202201041049339794.jpg', 'Thịt lợn nướng chả xiên ăn kèm bún và nước mắm chua ngọt'),
('dish-003', 'Cơm Tấm Sườn Bì Chả', 50000.0, 'https://cdn.tgdd.vn/Files/2019/07/26/1182061/cach-lam-com-tam-suon-bi-cha-chuan-vi-sai-gon-202201051515152865.jpg', 'Cơm tấm kèm sườn nướng mỡ hành thơm nức mũi'),
('dish-004', 'Pizza Chả Cá', 150000.0, 'https://cdn.tgdd.vn/Files/2020/09/24/1293375/cach-lam-pizza…-beo-ngay-thom-ngon-don-gian-tai-nha-202201121021160352.jpg', 'Pizza nướng củi nhân hải sản và phô mai kéo sợi'),
('dish-005', 'Trà Sữa Trân Châu', 35000.0, 'https://cdn.tgdd.vn/Files/2021/08/12/1374526/cach-lam-tra-s…ran-chau-duong-den-sieu-ngon-tai-nha-202108121111559868.jpg', 'Trà sữa truyền thống, ngậy vị sữa và dẻo dai trân châu');

-- ==========================================
-- BẢNG: order_records (Lịch sử đơn hàng)
-- Các cột: order_id, customer_name, shipping_address, sub_total, shipping_fee, total_amount, order_status, payment_id, payment_method, payment_status, paid_at, order_time, estimated_pickup_time
-- ==========================================
INSERT IGNORE INTO order_records (order_id, customer_name, shipping_address, sub_total, shipping_fee, total_amount, order_status, payment_id, payment_method, payment_status, paid_at, order_time, estimated_pickup_time) VALUES 
('ORD-1001', 'Nguyễn Văn A', '123 Đường Điện Biên Phủ, Quận Bình Thạnh, TP.HCM', 130000.0, 15000.0, 145000.0, 'RECEIVED', NULL, 'COD', 'PENDING', NULL, NOW(), DATE_ADD(NOW(), INTERVAL 45 MINUTE)),
('ORD-1002', 'Trần Thị B', '456 Lê Lợi, Quận 1, TP.HCM', 150000.0, 0.0, 150000.0, 'FINISHED', 'PAY-MOMO-X1', 'BANKING', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 30 MINUTE)),
('ORD-1003', 'Khách Vãng Lai', '789 Nguyễn Văn Cừ, Quận 5, TP.HCM', 105000.0, 20000.0, 125000.0, 'PREPARING', 'PAY-ZALO-Y2', 'BANKING', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 5 MINUTE), DATE_SUB(NOW(), INTERVAL 5 MINUTE), DATE_ADD(NOW(), INTERVAL 40 MINUTE));

-- ==========================================
-- BẢNG: coupons (Mã giảm giá)
-- Các cột: coupon_id, coupon_code, discount_value, min_order_value,
--          is_percentage, max_discount, valid_from, valid_until,
--          is_active, usage_limit, used_count
-- ==========================================
INSERT IGNORE INTO coupons
(coupon_id, coupon_code, discount_value, min_order_value, is_percentage, max_discount, valid_from, valid_until, is_active, usage_limit, used_count)
VALUES
('cp-001', 'SAVE10', 10.0, 100000.0, true, 30000.0, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), true, 100, 0),
('cp-002', 'LESS25K', 25000.0, 120000.0, false, NULL, NOW(), DATE_ADD(NOW(), INTERVAL 45 DAY), true, 200, 0),
('cp-003', 'FREESHIP15', 15000.0, 80000.0, false, NULL, NOW(), DATE_ADD(NOW(), INTERVAL 20 DAY), true, NULL, 0),
('cp-004', 'BIGSALE20', 20.0, 200000.0, true, 50000.0, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), true, 50, 0),
('cp-005', 'EXPIRED5', 5.0, 50000.0, true, 10000.0, DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY), false, 100, 100);