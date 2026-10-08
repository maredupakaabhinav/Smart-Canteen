<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/admin-header.jsp">
    <jsp:param name="title" value="Dashboard"/>
    <jsp:param name="active" value="dashboard"/>
</jsp:include>

<h1 class="admin-title">Dashboard</h1>
<p class="admin-subtitle">Welcome, <c:out value="${loggedInUser.name}"/>. Here is the canteen at a glance.</p>

<jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

<div class="grid grid-4 stat-grid">
    <a class="card stat-card" href="${ctx}/admin/menu">
        <span class="stat-icon">&#127860;</span>
        <span class="stat-value">${totalFood}</span>
        <span class="stat-label">Total Food Items</span>
    </a>
    <a class="card stat-card" href="${ctx}/admin/menu">
        <span class="stat-icon">&#9989;</span>
        <span class="stat-value">${availableFood}</span>
        <span class="stat-label">Available Items</span>
    </a>
    <a class="card stat-card stat-card-accent" href="${ctx}/admin/orders">
        <span class="stat-icon">&#9203;</span>
        <span class="stat-value">${pendingOrders}</span>
        <span class="stat-label">Pending Orders</span>
    </a>
    <a class="card stat-card" href="${ctx}/admin/orders">
        <span class="stat-icon">&#127881;</span>
        <span class="stat-value">${completedOrders}</span>
        <span class="stat-label">Completed Orders</span>
    </a>
</div>

<div class="section-head">
    <h2 class="section-title">Latest Orders</h2>
    <a href="${ctx}/admin/orders" class="link-more">Manage all orders &rarr;</a>
</div>

<c:choose>
    <c:when test="${empty latestOrders}">
        <div class="card empty-state"><p>No orders yet.</p></div>
    </c:when>
    <c:otherwise>
        <div class="card table-card">
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr><th>Order</th><th>Student</th><th>Items</th><th>Amount</th><th>Pickup</th><th>Status</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${latestOrders}">
                        <tr>
                            <td><strong>${order.displayId}</strong></td>
                            <td><c:out value="${order.customerName}"/><br><small class="muted"><c:out value="${order.customerCollegeId}"/></small></td>
                            <td><c:out value="${order.itemsSummary}"/></td>
                            <td>&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td>
                            <td>${order.pickupTimeText}<br><small class="muted">${order.pickupDateText}</small></td>
                            <td><span class="badge badge-${order.status.name().toLowerCase()}">${order.status.label}</span></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/includes/admin-footer.jsp"/>
