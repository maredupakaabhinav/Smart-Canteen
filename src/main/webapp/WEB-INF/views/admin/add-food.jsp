<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/admin-header.jsp">
    <jsp:param name="title" value="Add Food"/>
    <jsp:param name="active" value="add"/>
</jsp:include>

<h1 class="admin-title">Add Food</h1>
<p class="admin-subtitle">The new item appears on the student menu immediately.</p>

<jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

<jsp:include page="/WEB-INF/views/includes/food-form.jsp">
    <jsp:param name="mode" value="add"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/includes/admin-footer.jsp"/>
