<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/admin-header.jsp">
    <jsp:param name="title" value="Orders"/>
    <jsp:param name="active" value="orders"/>
</jsp:include>

<h1 class="admin-title">Orders</h1>
<p class="admin-subtitle">Change an order&rsquo;s status &mdash; the student sees it on the order page.</p>

<jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

<%-- Status filter (done in the browser by script.js) --%>
<div class="filter-buttons" id="orderFilters">
    <button type="button" class="filter-btn active" data-status="ALL">All</button>
    <c:forEach var="status" items="${statuses}">
        <button type="button" class="filter-btn" data-status="${status}">${status.label}</button>
    </c:forEach>
</div>

<c:choose>
    <c:when test="${empty orders}">
        <div class="card empty-state"><p>No orders yet.</p></div>
    </c:when>
    <c:otherwise>
        <div class="card table-card">
            <div class="table-wrap">
                <table class="data-table" id="ordersTable">
                    <thead>
                    <tr>
                        <th>Order ID</th><th>Student</th><th>Items</th><th>Amount</th>
                        <th>Pickup Time</th><th>Date</th><th>Status</th><th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr data-status="${order.status}">
                            <td><strong>${order.displayId}</strong></td>
                            <td><c:out value="${order.customerName}"/><br><small class="muted"><c:out value="${order.customerCollegeId}"/></small></td>
                            <td class="items-cell"><c:out value="${order.itemsSummary}"/></td>
                            <td>&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                                <br><small class="muted">${order.payment.methodLabel} &middot; ${order.payment.paymentStatus}</small></td>
                            <td>${order.pickupTimeText}<br><small class="muted">${order.pickupDateText}</small></td>
                            <td class="date-cell">${order.orderDateShortText}</td>
                            <td><span class="badge badge-${order.status.name().toLowerCase()}">${order.status.label}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.status.finished}">
                                        <small class="muted">Final</small>
                                    </c:when>
                                    <c:otherwise>
                                        <form method="post" action="${ctx}/admin/orders" class="status-form">
                                            <input type="hidden" name="orderId" value="${order.orderId}">
                                            <select name="status" class="form-control form-control-sm" aria-label="New status for ${order.displayId}">
                                                <c:forEach var="status" items="${statuses}">
                                                    <option value="${status}" ${order.status == status ? 'selected' : ''}>${status.label}</option>
                                                </c:forEach>
                                            </select>
                                            <button type="submit" class="btn btn-primary btn-sm">Update</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="card empty-state hidden" id="noOrders"><p>No orders with this status.</p></div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/includes/admin-footer.jsp"/>
