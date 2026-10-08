<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Order Details"/>
    <jsp:param name="active" value="orders"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<%-- progress: 1 = placed ... 4 = completed, 0 = cancelled (the status comes from the database) --%>
<c:set var="progress" value="${order.status.progress}"/>

<main class="container page">
    <div class="page-head page-head-row">
        <div>
            <h1 class="page-title">Order #${order.displayId}</h1>
            <p class="page-subtitle">Placed on ${order.orderDateText}</p>
        </div>
        <span class="badge badge-lg badge-${order.status.name().toLowerCase()}">${order.status.label}</span>
    </div>

    <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

    <%-- Status tracker --%>
    <div class="card tracker-card">
        <h2 class="card-title">Order Status</h2>
        <c:choose>
            <c:when test="${order.status.name() == 'CANCELLED'}">
                <div class="alert alert-error">This order was cancelled.
                    <c:if test="${order.payment.paymentStatus == 'REFUNDED'}"> The demo payment has been refunded.</c:if>
                </div>
            </c:when>
            <c:otherwise>
                <ol class="tracker">
                    <li class="${progress >= 1 ? 'done' : ''}"><span class="tracker-dot">${progress >= 1 ? '&#10003;' : ''}</span><span class="tracker-label">Order Placed</span></li>
                    <li class="${order.payment.paid ? 'done' : ''}"><span class="tracker-dot">${order.payment.paid ? '&#10003;' : ''}</span>
                        <span class="tracker-label">${order.payment.paid ? 'Payment Confirmed' : 'Pay at Counter'}</span></li>
                    <li class="${progress >= 2 ? 'done' : ''} ${progress == 2 ? 'current' : ''}"><span class="tracker-dot">${progress >= 2 ? '&#10003;' : ''}</span><span class="tracker-label">Preparing</span></li>
                    <li class="${progress >= 3 ? 'done' : ''} ${progress == 3 ? 'current' : ''}"><span class="tracker-dot">${progress >= 3 ? '&#10003;' : ''}</span><span class="tracker-label">Ready for Pickup</span></li>
                    <li class="${progress >= 4 ? 'done' : ''}"><span class="tracker-dot">${progress >= 4 ? '&#10003;' : ''}</span><span class="tracker-label">Completed</span></li>
                </ol>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="grid grid-2">
        <%-- Items --%>
        <div class="card">
            <h2 class="card-title">Items</h2>
            <ul class="summary-items">
                <c:forEach var="item" items="${order.items}">
                    <li>
                        <span><c:out value="${item.foodName}"/> <small class="muted">x ${item.quantity} @ &#8377;<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></small></span>
                        <span>&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                    </li>
                </c:forEach>
            </ul>
            <div class="summary-line summary-total"><span>Total</span><span>&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span></div>
        </div>

        <%-- Pickup + payment --%>
        <div class="card">
            <h2 class="card-title">Pickup &amp; Payment</h2>
            <dl class="detail-list">
                <div><dt>Pickup Time</dt><dd>${order.pickupTimeText}</dd></div>
                <div><dt>Pickup Date</dt><dd>${order.pickupDateText}</dd></div>
                <div><dt>Payment Method</dt><dd>${order.payment.methodLabel}</dd></div>
                <div><dt>Payment Status</dt><dd><span class="badge badge-pay-${order.payment.paymentStatus.toLowerCase()}">${order.payment.paymentStatus}</span></dd></div>
                <div><dt>Transaction ID</dt><dd class="mono"><c:out value="${order.payment.transactionId}"/></dd></div>
            </dl>
        </div>
    </div>

    <%-- Cancellation (the rule is checked again by the server when you press the button) --%>
    <div class="card cancel-card">
        <h2 class="card-title">Cancel Order</h2>
        <c:choose>
            <c:when test="${canCancel}">
                <p>You can cancel this order within ${cancellationMinutes} minutes of placing it.
                    About <strong>${cancelMinutesLeft} min</strong> left.</p>
                <form method="post" action="${ctx}/cancel-order" data-confirm="Cancel order ${order.displayId}? This cannot be undone.">
                    <input type="hidden" name="orderId" value="${order.orderId}">
                    <button type="submit" class="btn btn-danger">Cancel Order</button>
                </form>
            </c:when>
            <c:otherwise>
                <button type="button" class="btn btn-danger" disabled>Cancel Order</button>
                <p class="text-danger cancel-reason"><c:out value="${cancelBlockedReason}"/></p>
            </c:otherwise>
        </c:choose>
    </div>

    <p><a href="${ctx}/orders" class="btn btn-outline">&larr; Back to My Orders</a></p>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
