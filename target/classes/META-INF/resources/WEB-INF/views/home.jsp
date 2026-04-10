<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ | Food Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<div class="shell">
    <header class="bar">
        <a class="brand" href="${pageContext.request.contextPath}/home">Food<span>Order</span></a>
        <nav class="nav-actions">
            <span class="user-tag"><sec:authentication property="name" /></span>
            <form action="${pageContext.request.contextPath}/logout" method="post" style="display:inline;">
                <button type="submit" class="btn btn-ghost">Đăng xuất</button>
            </form>
        </nav>
    </header>
    <div class="panel">
        <h2>Xin chào</h2>
        <p>Bạn đã đăng nhập. Đây là khu vực làm việc cơ bản; có thể mở rộng thêm menu, giỏ hàng và đơn hàng sau.</p>
    </div>
</div>
</body>
</html>
