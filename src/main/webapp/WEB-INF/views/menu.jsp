<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Menu"/>
    <jsp:param name="active" value="menu"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="page-head">
        <h1 class="page-title">Food Menu</h1>
        <p class="page-subtitle">
            <c:choose>
                <c:when test="${loggedInUser.role == 'STUDENT'}">Pick what you like and add it to your cart.</c:when>
                <c:otherwise>Have a look at today&rsquo;s food. <a href="${ctx}/login">Login</a> to place an order.</c:otherwise>
            </c:choose>
        </p>
    </div>

    <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

    <%-- Search + category filter (done in the browser by script.js) --%>
    <div class="menu-toolbar">
        <input type="search" id="foodSearch" class="form-control search-box"
               placeholder="Search food..." aria-label="Search food by name or category">
        <div class="filter-buttons" id="categoryFilters">
            <button type="button" class="filter-btn active" data-category="All">All</button>
            <c:forEach var="category" items="${categories}">
                <button type="button" class="filter-btn" data-category="${category}">${category}</button>
            </c:forEach>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty foodItems}">
            <div class="card empty-state"><p>The menu is empty right now. Please check again later.</p></div>
        </c:when>
        <c:otherwise>
            <div class="food-grid" id="foodGrid">
                <c:forEach var="food" items="${foodItems}">
                    <%@ include file="includes/food-card.jspf" %>
                </c:forEach>
            </div>
            <div class="card empty-state hidden" id="noResults"><p>No food found. Try another search.</p></div>
        </c:otherwise>
    </c:choose>
</main>

<div class="toast hidden" id="toast" role="status"></div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
