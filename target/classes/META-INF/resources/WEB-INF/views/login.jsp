<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập | Food Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<div class="shell">
    <header class="bar">
        <a class="brand" href="${pageContext.request.contextPath}/">Food<span>Order</span></a>
        <nav class="nav-actions">
            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/">Trang chủ</a>
        </nav>
    </header>
    <div class="card">
        <h2>Đăng nhập</h2>
        <p class="sub">Nhập tài khoản để tiếp tục</p>
        <c:if test="${not empty param.error}">
            <div class="alert">Sai tên đăng nhập hoặc mật khẩu.</div>
        </c:if>
        <c:if test="${not empty param.logout}">
            <div class="alert" style="background:#ecfdf5;color:#047857;border-color:#a7f3d0;">Bạn đã đăng xuất.</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập</label>
                <input id="username" name="username" type="text" autocomplete="username" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input id="password" name="password" type="password" autocomplete="current-password" required>
            </div>
            <button type="submit" class="btn btn-primary">Đăng nhập</button>
        </form>
        <p class="hint">Demo: <strong>admin</strong> / <strong>admin123</strong></p>
    </div>
</div>
</body>
</html>
