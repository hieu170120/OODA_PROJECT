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
-- BẢNG: dishes (Món ăn - Fast Food)
-- Các cột: dish_id, name, price, image_url, description
-- ==========================================
INSERT IGNORE INTO dishes (dish_id, name, price, image_url, description) VALUES 
('dish-001', 'Hamburger Bò Phô Mai', 55000.0, 'https://cdn.tgdd.vn/Files/2022/01/25/1412805/cach-lam-hamburger-bo-pho-mai-tai-nha-ngon-nhu-ngoai-hang-202201251030038502.jpg', 'Hamburger bò Úc kẹp phô mai cheddar, rau xà lách tươi và sốt đặc biệt'),
('dish-002', 'Kebab Thịt Gà', 45000.0, 'https://cdn.tgdd.vn/2021/11/CookDish/cach-lam-banh-mi-kebab-don-gian-ngon-kho-cuong-avt-1200x676.jpg', 'Kebab cuộn thịt gà nướng, rau sống, hành tây và sốt tỏi'),
('dish-003', 'Gà Rán Giòn (3 miếng)', 65000.0, 'https://cdn.tgdd.vn/Files/2020/03/30/1245765/cach-lam-ga-ran-kfc-gion-rum-thom-ngon-nhu-ngoai-hang-202003300834474754.jpg', 'Gà rán giòn tẩm ướp gia vị bí truyền, giòn rụm bên ngoài, mềm mọng bên trong'),
('dish-004', 'Pizza Hải Sản', 120000.0, 'https://cdn.tgdd.vn/Files/2020/09/24/1293375/cach-lam-pizza-hai-san-beo-ngay-thom-ngon-don-gian-tai-nha-202201121021160352.jpg', 'Pizza đế giòn với tôm, mực, phô mai mozzarella kéo sợi'),
('dish-005', 'Hot Dog Xúc Xích', 35000.0, 'https://cdn.tgdd.vn/2021/09/CookProduct/1200-1200x676-15.jpg', 'Hot dog xúc xích Đức nướng, kẹp bánh mì mềm với sốt mù tạt và ketchup'),
('dish-006', 'Khoai Tây Chiên (L)', 30000.0, 'https://cdn.tgdd.vn/Files/2021/07/22/1369637/cach-lam-khoai-tay-chien-gion-ngon-bang-noi-chien-khong-dau-202201070844298498.jpg', 'Khoai tây chiên giòn vàng ươm, rắc muối biển tự nhiên'),
('dish-007', 'Trà Sữa Trân Châu', 35000.0, 'https://cdn.tgdd.vn/Files/2021/08/12/1374526/cach-lam-tra-sua-tran-chau-duong-den-sieu-ngon-tai-nha-202108121111559868.jpg', 'Trà sữa truyền thống, ngậy vị sữa và dẻo dai trân châu đường đen'),
('dish-008', 'Coca Cola (L)', 20000.0, 'https://cdn.tgdd.vn/Products/Images/2443/84856/bhx/coca-cola-lon-330ml-202110291559103498.jpg', 'Coca Cola lon mát lạnh sảng khoái');

-- ==========================================
-- BẢNG: order_records (Lịch sử đơn hàng)
-- Các cột: order_id, customer_name, shipping_address, sub_total, shipping_fee, total_amount, order_status, payment_id, payment_method, payment_status, paid_at, order_time, estimated_pickup_time
-- ==========================================
INSERT IGNORE INTO order_records (order_id, customer_name, shipping_address, sub_total, shipping_fee, total_amount, order_status, payment_id, payment_method, payment_status, paid_at, order_time, estimated_pickup_time) VALUES 
('ORD-1001', 'Nguyễn Văn A', '123 Đường Điện Biên Phủ, Quận Bình Thạnh, TP.HCM', 130000.0, 15000.0, 145000.0, 'RECEIVED', NULL, 'COD', 'PENDING', NULL, NOW(), DATE_ADD(NOW(), INTERVAL 45 MINUTE)),
('ORD-1002', 'Trần Thị B', '456 Lê Lợi, Quận 1, TP.HCM', 150000.0, 0.0, 150000.0, 'FINISHED', 'PAY-MOMO-X1', 'BANKING', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_ADD(NOW(), INTERVAL 30 MINUTE)),
('ORD-1003', 'Khách Vãng Lai', '789 Nguyễn Văn Cừ, Quận 5, TP.HCM', 105000.0, 20000.0, 125000.0, 'PREPARING', 'PAY-ZALO-Y2', 'BANKING', 'COMPLETED', DATE_SUB(NOW(), INTERVAL 5 MINUTE), DATE_SUB(NOW(), INTERVAL 5 MINUTE), DATE_ADD(NOW(), INTERVAL 40 MINUTE));