<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Food Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<div class="shell">
    <header class="bar">
        <a class="brand" href="${pageContext.request.contextPath}/">Food<span>Order</span></a>
        <nav class="nav-actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </nav>
    </header>
    <section class="hero">
        <h1>Đặt món nhanh, giao tận nơi</h1>
        <p>Giao diện cơ bản cho hệ thống đặt đồ ăn. Đăng nhập để vào khu vực dành cho người dùng.</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Bắt đầu</a>
    </section>
</div>
</body>
</html>
