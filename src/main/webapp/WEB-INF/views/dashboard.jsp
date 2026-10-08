<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Dashboard"/>
    <jsp:param name="active" value="home"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="page-head">
        <h1 class="page-title">Welcome, <c:out value="${loggedInUser.name}"/></h1>
        <p class="page-subtitle"><c:out value="${loggedInUser.collegeId}"/> &middot; What would you like to eat today?</p>
    </div>

    <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

    <%-- Summary cards --%>
    <div class="grid grid-4 stat-grid">
        <a class="card stat-card" href="${ctx}/menu">
            <span class="stat-icon">&#127860;</span>
            <span class="stat-value">${availableCount}</span>
            <span class="stat-label">Available Food</span>
        </a>
        <a class="card stat-card" href="${ctx}/cart">
            <span class="stat-icon">&#128722;</span>
            <span class="stat-value">${cartCount}</span>
            <span class="stat-label">My Cart</span>
        </a>
        <a class="card stat-card" href="${ctx}/orders">
            <span class="stat-icon">&#128203;</span>
            <span class="stat-value">${orderCount}</span>
            <span class="stat-label">My Orders</span>
        </a>
        <c:choose>
            <c:when test="${not empty upcomingPickup}">
                <a class="card stat-card stat-card-accent" href="${ctx}/order-details?id=${upcomingPickup.orderId}">
                    <span class="stat-icon">&#9200;</span>
                    <span class="stat-value stat-value-text">${upcomingPickup.pickupTimeText}</span>
                    <span class="stat-label">Upcoming Pickup &middot; ${upcomingPickup.pickupDateText}</span>
                </a>
            </c:when>
            <c:otherwise>
                <div class="card stat-card">
                    <span class="stat-icon">&#9200;</span>
                    <span class="stat-value stat-value-text">None</span>
                    <span class="stat-label">Upcoming Pickup</span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Today's menu --%>
    <div class="section-head">
        <h2 class="section-title">Today&rsquo;s Menu</h2>
        <a href="${ctx}/menu" class="link-more">View full menu &rarr;</a>
    </div>
    <c:choose>
        <c:when test="${empty todaysMenu}">
            <div class="card empty-state"><p>No food is available right now. Please check again later.</p></div>
        </c:when>
        <c:otherwise>
            <div class="food-grid">
                <c:forEach var="food" items="${todaysMenu}">
                    <%@ include file="includes/food-card.jspf" %>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <%-- Recent orders --%>
    <div class="section-head">
        <h2 class="section-title">Recent Orders</h2>
        <a href="${ctx}/orders" class="link-more">All orders &rarr;</a>
    </div>
    <c:choose>
        <c:when test="${empty recentOrders}">
            <div class="card empty-state">
                <p>You have not placed any order yet.</p>
                <a href="${ctx}/menu" class="btn btn-primary">Order your first meal</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card table-card">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                        <tr><th>Order</th><th>Items</th><th>Total</th><th>Pickup</th><th>Status</th><th></th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="order" items="${recentOrders}">
                            <tr>
                                <td><strong>${order.displayId}</strong></td>
                                <td><c:out value="${order.itemsSummary}"/></td>
                                <td>&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td>
                                <td>${order.pickupTimeText}<br><small class="muted">${order.pickupDateText}</small></td>
                                <td><span class="badge badge-${order.status.name().toLowerCase()}">${order.status.label}</span></td>
                                <td><a class="btn btn-outline btn-sm" href="${ctx}/order-details?id=${order.orderId}">View</a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
