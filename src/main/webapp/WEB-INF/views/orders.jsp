<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="My Orders"/>
    <jsp:param name="active" value="orders"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="page-head">
        <h1 class="page-title">My Orders</h1>
        <p class="page-subtitle">Open an order to track its status.</p>
    </div>

    <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="card empty-state">
                <p class="empty-title">You have not placed any order yet.</p>
                <a href="${ctx}/menu" class="btn btn-primary">Browse Menu</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card table-card">
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                        <tr><th>Order</th><th>Placed on</th><th>Items</th><th>Total</th><th>Pickup</th><th>Status</th><th></th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td><strong>${order.displayId}</strong></td>
                                <td>${order.orderDateText}</td>
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
