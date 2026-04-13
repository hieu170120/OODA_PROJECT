<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Đăng nhập Quản lý | Food Order</title>
            <style>
                * {
                    margin: 0;
                    padding: 0;
                    box-sizing: border-box;
                }

                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
                    background-color: #f5f5f5;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    min-height: 100vh;
                    padding: 20px;
                }

                .login-container {
                    background: white;
                    width: 100%;
                    max-width: 400px;
                    padding: 40px;
                    border-radius: 8px;
                    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                }

                .login-header {
                    margin-bottom: 30px;
                    text-align: center;
                }

                .login-header h1 {
                    font-size: 24px;
                    color: #333;
                    margin-bottom: 8px;
                    font-weight: 600;
                }

                .login-header p {
                    font-size: 14px;
                    color: #666;
                }

                .form-group {
                    margin-bottom: 20px;
                }

                .form-group label {
                    display: block;
                    margin-bottom: 8px;
                    font-size: 14px;
                    color: #333;
                    font-weight: 500;
                }

                .form-group input {
                    width: 100%;
                    padding: 10px 12px;
                    border: 1px solid #ddd;
                    border-radius: 4px;
                    font-size: 14px;
                    transition: border-color 0.3s;
                }

                .form-group input:focus {
                    outline: none;
                    border-color: #4CAF50;
                    box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.1);
                }

                .alert {
                    padding: 12px 14px;
                    border-radius: 4px;
                    margin-bottom: 20px;
                    font-size: 14px;
                    display: none;
                }

                .alert.show {
                    display: block;
                }

                .alert-error {
                    background-color: #ffebee;
                    color: #c62828;
                    border: 1px solid #ef5350;
                }

                .alert-success {
                    background-color: #e8f5e9;
                    color: #2e7d32;
                    border: 1px solid #81c784;
                }

                .btn-login {
                    width: 100%;
                    padding: 11px;
                    background-color: #4CAF50;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    font-size: 15px;
                    font-weight: 600;
                    cursor: pointer;
                    transition: background-color 0.3s;
                    margin-top: 10px;
                }

                .btn-login:hover {
                    background-color: #45a049;
                }

                .btn-login:active {
                    background-color: #3d8b40;
                }

                .login-footer {
                    margin-top: 20px;
                    padding-top: 20px;
                    border-top: 1px solid #eee;
                    text-align: center;
                    font-size: 13px;
                }

                .login-footer a {
                    color: #4CAF50;
                    text-decoration: none;
                    margin: 0 5px;
                }

                .login-footer a:hover {
                    text-decoration: underline;
                }
            </style>
        </head>

        <body>
            <div class="login-container">
                <div class="login-header">
                    <h1>Đăng nhập Quản lý</h1>
                    <p>Quản lý hệ thống Food Order</p>
                </div>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-error show">
                        Email hoặc mật khẩu không chính xác
                    </div>
                </c:if>

                <c:if test="${not empty param.logout}">
                    <div class="alert alert-success show">
                        Bạn đã đăng xuất thành công
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin/login" method="post">
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input id="email" name="email" type="email" autocomplete="email" required autofocus
                            placeholder="Nhập email của bạn">
                    </div>

                    <div class="form-group">
                        <label for="password">Mật khẩu</label>
                        <input id="password" name="password" type="password" autocomplete="current-password" required
                            placeholder="Nhập mật khẩu">
                    </div>

                    <button type="submit" class="btn-login">Đăng Nhập</button>
                </form>

                <div class="login-footer">
                    <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                    |
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập Khách hàng</a>
                </div>
            </div>
        </body>

        </html>