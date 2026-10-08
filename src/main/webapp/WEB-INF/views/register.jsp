<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Register | Smart Canteen</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/login.css">
</head>
<body data-ctx="${ctx}">

<div class="auth-page">
    <aside class="auth-info">
        <a class="logo" href="${ctx}/">
            <span class="logo-icon">&#9749;</span>
            <span class="logo-text">SMART CANTEEN<small>JAVA OOP PROJECT</small></span>
        </a>
        <div class="auth-info-body">
            <span class="java-label">JAVA OOP PROJECT</span>
            <h1>Create an account</h1>
            <p>Students and faculty can register with their college ID. Admin accounts are created by the canteen staff.</p>
            <ul class="auth-points">
                <li>Your password is stored as a salted hash</li>
                <li>Use your real college ID</li>
            </ul>
        </div>
        <pre class="auth-code" aria-hidden="true"><code>Student s = <span class="kw">new</span> Student();
s.setCollegeId(<span class="str">"STU003"</span>);
userDAO.createUser(s);</code></pre>
    </aside>

    <main class="auth-form-side">
        <div class="auth-card card">
            <h2>Register</h2>
            <p class="muted">Student / Faculty account</p>

            <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

            <form method="post" action="${ctx}/register">
                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name" class="form-control" required maxlength="100"
                           value="<c:out value='${enteredName}'/>" autocomplete="name">
                </div>
                <div class="form-group">
                    <label for="collegeId">College ID</label>
                    <input type="text" id="collegeId" name="collegeId" class="form-control" required maxlength="20"
                           value="<c:out value='${enteredCollegeId}'/>" placeholder="e.g. STU003" autocomplete="username">
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" class="form-control" required maxlength="120"
                           value="<c:out value='${enteredEmail}'/>" autocomplete="email">
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" class="form-control" required
                               minlength="6" maxlength="72" autocomplete="new-password">
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">Confirm Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required
                               minlength="6" maxlength="72" autocomplete="new-password">
                    </div>
                </div>
                <button type="submit" class="btn btn-primary btn-block">CREATE ACCOUNT</button>
            </form>

            <div class="auth-links">
                <a href="${ctx}/">&larr; Back to Home</a>
                <a href="${ctx}/login">Already registered? Login</a>
            </div>
        </div>
    </main>
</div>

<script src="${ctx}/js/script.js"></script>
</body>
</html>
