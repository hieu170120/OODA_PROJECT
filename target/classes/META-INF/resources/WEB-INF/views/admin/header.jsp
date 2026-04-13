<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <div class="admin-topbar">
            <div class="topbar-left">
                <h3 class="topbar-title">${pageTitle}</h3>
            </div>
            <div class="topbar-right">
                <div class="admin-info">
                    <span class="admin-name">
                        <c:choose>
                            <c:when test="${not empty sessionScope.LOGGED_IN_ADMIN}">
                                ${sessionScope.LOGGED_IN_ADMIN.fullName}
                            </c:when>
                            <c:otherwise>
                                Manager
                            </c:otherwise>
                        </c:choose>
                    </span>
                    <a href="${pageContext.request.contextPath}/admin/logout" class="btn-logout">Đăng xuất</a>
                </div>
            </div>
        </div>