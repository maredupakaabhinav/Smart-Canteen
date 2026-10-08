<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Profile"/>
    <jsp:param name="active" value="profile"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="page-head">
        <h1 class="page-title">My Profile</h1>
    </div>

    <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

    <div class="card profile-card">
        <div class="profile-avatar"><c:out value="${loggedInUser.name.substring(0, 1)}"/></div>
        <dl class="detail-list">
            <div><dt>Name</dt><dd><c:out value="${loggedInUser.name}"/></dd></div>
            <div><dt>College ID</dt><dd class="mono"><c:out value="${loggedInUser.collegeId}"/></dd></div>
            <div><dt>Email</dt><dd><c:out value="${loggedInUser.email}"/></dd></div>
            <div><dt>Account Type</dt><dd>${loggedInUser.roleTitle}</dd></div>
            <div><dt>Orders Placed</dt><dd>${orderCount}</dd></div>
        </dl>
        <div class="profile-actions">
            <a href="${ctx}/orders" class="btn btn-primary">My Orders</a>
            <a href="${ctx}/logout" class="btn btn-outline">Logout</a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
