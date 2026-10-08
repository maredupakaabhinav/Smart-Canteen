<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/admin-header.jsp">
    <jsp:param name="title" value="Edit Food"/>
    <jsp:param name="active" value="menu"/>
</jsp:include>

<h1 class="admin-title">Edit Food</h1>
<p class="admin-subtitle">Change the details or the availability of this item.</p>

<jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

<jsp:include page="/WEB-INF/views/includes/food-form.jsp">
    <jsp:param name="mode" value="edit"/>
</jsp:include>

<jsp:include page="/WEB-INF/views/includes/admin-footer.jsp"/>
