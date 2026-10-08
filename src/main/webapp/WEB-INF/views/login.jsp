<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login | Smart Canteen</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/login.css">
</head>
<body data-ctx="${ctx}">

<div class="auth-page">
    <%-- Left side: project information --%>
    <aside class="auth-info">
        <a class="logo" href="${ctx}/">
            <span class="logo-icon">&#9749;</span>
            <span class="logo-text">SMART CANTEEN<small>JAVA OOP PROJECT</small></span>
        </a>
        <div class="auth-info-body">
            <span class="java-label">JAVA OOP PROJECT</span>
            <h1>Smart Canteen</h1>
            <p>A Java OOP-based canteen ordering &amp; management system. Pre-order your food, choose a pickup time and skip the queue.</p>
            <ul class="auth-points">
                <li>Servlets + JSP + JDBC + MySQL</li>
                <li>Cart, demo payment &amp; order tracking</li>
                <li>Admin panel for menu and orders</li>
            </ul>
        </div>
        <pre class="auth-code" aria-hidden="true"><code><span class="kw">class</span> <span class="cls">User</span> {}
<span class="kw">class</span> <span class="cls">Student</span> <span class="kw">extends</span> <span class="cls">User</span> {}
<span class="kw">class</span> <span class="cls">Admin</span>   <span class="kw">extends</span> <span class="cls">User</span> {}</code></pre>
    </aside>

    <%-- Right side: login card --%>
    <main class="auth-form-side">
        <div class="auth-card card">
            <h2>Login</h2>
            <p class="muted">Sign in to continue.</p>

            <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

            <form method="post" action="${ctx}/login" autocomplete="on">
                <div class="form-group">
                    <label>Login as</label>
                    <div class="segmented" id="loginTypeSwitch">
                        <label class="segment">
                            <input type="radio" name="loginType" value="student"
                                   ${enteredLoginType == 'admin' ? '' : 'checked'}>
                            <span>Student / Faculty</span>
                        </label>
                        <label class="segment">
                            <input type="radio" name="loginType" value="admin"
                                   ${enteredLoginType == 'admin' ? 'checked' : ''}>
                            <span>Admin</span>
                        </label>
                    </div>
                </div>

                <div class="form-group">
                    <label for="collegeId" id="idLabel">${enteredLoginType == 'admin' ? 'Admin ID' : 'College ID'}</label>
                    <input type="text" id="collegeId" name="collegeId" class="form-control" required maxlength="30"
                           value="<c:out value='${enteredCollegeId}'/>" placeholder="e.g. STU001" autocomplete="username">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" required
                           maxlength="72" placeholder="Enter your password" autocomplete="current-password">
                </div>

                <button type="submit" class="btn btn-primary btn-block">LOGIN</button>
            </form>

            <div class="auth-links">
                <a href="${ctx}/">&larr; Back to Home</a>
                <a href="${ctx}/register">New student? Register</a>
            </div>
        </div>
    </main>
</div>

<script src="${ctx}/js/script.js"></script>
</body>
</html>
