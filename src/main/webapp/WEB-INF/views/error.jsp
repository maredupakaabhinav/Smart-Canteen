<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Error"/>
    <jsp:param name="active" value=""/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<%-- A friendly error page. The technical details are written to the Tomcat console, never shown here. --%>
<main class="container page">
    <div class="card empty-state error-card">
        <div class="error-code">${pageContext.errorData.statusCode}</div>
        <c:choose>
            <c:when test="${pageContext.errorData.statusCode == 404}">
                <p class="empty-title">Page not found.</p>
                <p class="muted">The page you are looking for does not exist.</p>
            </c:when>
            <c:when test="${pageContext.errorData.statusCode == 405}">
                <p class="empty-title">That action is not allowed.</p>
                <p class="muted">Please go back and use the buttons on the page.</p>
            </c:when>
            <c:otherwise>
                <p class="empty-title">Something went wrong. Please try again.</p>
            </c:otherwise>
        </c:choose>
        <a href="${ctx}/" class="btn btn-primary">Back to Home</a>
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
