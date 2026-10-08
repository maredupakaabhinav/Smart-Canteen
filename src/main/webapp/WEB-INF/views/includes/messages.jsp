<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Shows one-time messages: flash messages from the session and the "error" of the current request.
     All text is printed with c:out so it can never run as HTML / JavaScript. --%>
<c:if test="${not empty sessionScope.flashSuccess}">
    <div class="alert alert-success" role="status"><c:out value="${sessionScope.flashSuccess}"/></div>
    <c:remove var="flashSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.flashError}">
    <div class="alert alert-error" role="alert"><c:out value="${sessionScope.flashError}"/></div>
    <c:remove var="flashError" scope="session"/>
</c:if>
<c:if test="${not empty requestScope.error}">
    <div class="alert alert-error" role="alert"><c:out value="${requestScope.error}"/></div>
</c:if>
