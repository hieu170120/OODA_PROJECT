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

                    <c:if test="${not empty loginError}">
                        <div class="alert"
                            style="background:#fef2f2;color:#b91c1c;border: 1px solid #fecaca; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
                            ${loginError}
                        </div>
                    </c:if>

                    <c:if test="${not empty param.error}">
                        <div class="alert"
                            style="background:#fef2f2;color:#b91c1c;border: 1px solid #fecaca; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
                            Sai tên đăng nhập hoặc mật khẩu.
                        </div>
                    </c:if>

                    <c:if test="${not empty param.logout}">
                        <div class="alert"
                            style="background:#ecfdf5;color:#047857;border: 1px solid #a7f3d0; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
                            Bạn đã đăng xuất.
                        </div>
                    </c:if>

                    <c:if test="${not empty param.success}">
                        <div class="alert"
                            style="background:#ecfdf5;color:#047857;border: 1px solid #a7f3d0; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
                            Đăng ký thành công! Vui lòng đăng nhập bằng tài khoản của bạn.
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input id="email" name="email" type="email" autocomplete="email" required autofocus
                                placeholder="Ví dụ: khachhang@gmail.com">
                        </div>
                        <div class="form-group">
                            <label for="password">Mật khẩu</label>
                            <input id="password" name="password" type="password" autocomplete="current-password"
                                required>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 10px;">Đăng
                            nhập</button>
                    </form>
                    <div class="hint" style="margin-top: 20px; font-size: 0.9em; color: #6b7280; text-align: center;">
                        <p>Tài khoản Demo (Người dùng):<br>
                            Email: <strong>khachhang@gmail.com</strong> <br> Mật khẩu: <strong>123456</strong></p>
                    </div>

                    <div style="margin-top: 20px; text-align: center; font-size: 14px; color: #6b7280;">
                        Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register"
                            style="color: #5b21b6; text-decoration: none; font-weight: 600;">Đăng ký tại đây</a>
                    </div>
                </div>
            </div>
        </body>

        </html>