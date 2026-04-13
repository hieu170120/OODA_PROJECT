<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Đăng ký | Food Order</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
            <style>
                .register-form {
                    max-width: 500px;
                    margin: 0 auto;
                }

                .form-row {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 15px;
                }

                .form-row.full {
                    grid-template-columns: 1fr;
                }

                .form-group label {
                    display: block;
                    margin-bottom: 8px;
                    font-weight: 500;
                    color: #1f2937;
                    font-size: 14px;
                }

                .form-group input,
                .form-group textarea {
                    width: 100%;
                    padding: 10px 12px;
                    border: 1px solid #e5e7eb;
                    border-radius: 6px;
                    font-size: 14px;
                    font-family: inherit;
                    transition: border-color 0.3s;
                }

                .form-group input:focus,
                .form-group textarea:focus {
                    outline: none;
                    border-color: #5b21b6;
                    box-shadow: 0 0 0 3px rgba(91, 33, 182, 0.1);
                }

                .form-group textarea {
                    resize: vertical;
                    min-height: 80px;
                }

                .alert {
                    padding: 12px 16px;
                    border-radius: 6px;
                    margin-bottom: 20px;
                    font-size: 14px;
                }

                .alert.error {
                    background-color: #fef2f2;
                    color: #b91c1c;
                    border: 1px solid #fecaca;
                }

                .alert.success {
                    background-color: #ecfdf5;
                    color: #047857;
                    border: 1px solid #a7f3d0;
                }

                .login-link {
                    text-align: center;
                    margin-top: 20px;
                    font-size: 14px;
                    color: #6b7280;
                }

                .login-link a {
                    color: #5b21b6;
                    text-decoration: none;
                    font-weight: 600;
                }

                .login-link a:hover {
                    text-decoration: underline;
                }
            </style>
        </head>

        <body>
            <div class="shell">
                <header class="bar">
                    <a class="brand" href="${pageContext.request.contextPath}/">Food<span>Order</span></a>
                    <nav class="nav-actions">
                        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/">Trang chủ</a>
                        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    </nav>
                </header>

                <div class="card register-form">
                    <h2>Đăng ký tài khoản</h2>
                    <p class="sub">Tạo tài khoản mới để bắt đầu đặt hàng</p>

                    <c:if test="${not empty registerError}">
                        <div class="alert error">
                            ${registerError}
                        </div>
                    </c:if>

                    <c:if test="${not empty param.error}">
                        <div class="alert error">
                            ${param.error}
                        </div>
                    </c:if>

                    <c:if test="${not empty param.success}">
                        <div class="alert success">
                            Đăng ký thành công! Vui lòng <a href="${pageContext.request.contextPath}/login">đăng
                                nhập</a> để tiếp tục.
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="firstName">Tên</label>
                                <input id="firstName" name="firstName" type="text" required placeholder="Ví dụ: Văn"
                                    autofocus value="${param.firstName}">
                            </div>
                            <div class="form-group">
                                <label for="lastName">Họ</label>
                                <input id="lastName" name="lastName" type="text" required placeholder="Ví dụ: Nguyễn"
                                    value="${param.lastName}">
                            </div>
                        </div>

                        <div class="form-group form-row full">
                            <label for="email">Email</label>
                            <input id="email" name="email" type="email" required placeholder="example@gmail.com"
                                value="${param.email}">
                        </div>

                        <div class="form-group form-row full">
                            <label for="phone">Số điện thoại</label>
                            <input id="phone" name="phone" type="tel" required placeholder="0123456789"
                                value="${param.phone}">
                        </div>

                        <div class="form-group form-row full">
                            <label for="password">Mật khẩu</label>
                            <input id="password" name="password" type="password" required placeholder="Ít nhất 6 ký tự"
                                autocomplete="new-password">
                        </div>

                        <div class="form-group form-row full">
                            <label for="confirmPassword">Xác nhận mật khẩu</label>
                            <input id="confirmPassword" name="confirmPassword" type="password" required
                                placeholder="Nhập lại mật khẩu" autocomplete="new-password">
                        </div>

                        <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 20px;">Đăng
                            ký</button>
                    </form>

                    <div class="login-link">
                        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập tại đây</a>
                    </div>
                </div>
            </div>

            <script>
                // Validate password match
                document.querySelector('form').addEventListener('submit', function (e) {
                    const password = document.getElementById('password').value;
                    const confirmPassword = document.getElementById('confirmPassword').value;

                    if (password !== confirmPassword) {
                        e.preventDefault();
                        alert('Mật khẩu không khớp. Vui lòng kiểm tra lại.');
                        <script>
    // Validate password match
                            document.querySelector('form').addEventListener('submit', function(e) {
        const password = document.getElementById('password').value;
                            const confirmPassword = document.getElementById('confirmPassword').value;

                            if (password !== confirmPassword) {
                                e.preventDefault();
                            alert('Mật khẩu không khớp. Vui lòng kiểm tra lại.');
                            document.getElementById('confirmPassword').focus();
                            return;
        }

                            if (password.length < 6) {
                                e.preventDefault();
                            alert('Mật khẩu phải có ít nhất 6 ký tự.');
                            document.getElementById('password').focus();
                            return;
        }
    });
            </script>
        </body>