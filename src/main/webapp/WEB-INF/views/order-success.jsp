<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Order Placed"/>
    <jsp:param name="active" value="orders"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="card success-card">
        <div class="success-check">&#10003;</div>

        <c:choose>
            <c:when test="${order.payment.paid}">
                <h1>Payment Successful</h1>
                <p class="muted">This was a demo payment &mdash; no real money was charged.</p>
            </c:when>
            <c:otherwise>
                <h1>Order Placed</h1>
                <p class="muted">Please pay <strong>&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong> at the canteen counter when you collect your food.</p>
            </c:otherwise>
        </c:choose>

        <dl class="detail-list">
            <div><dt>Transaction ID</dt><dd class="mono"><c:out value="${order.payment.transactionId}"/></dd></div>
            <div><dt>Order ID</dt><dd class="mono">${order.displayId}</dd></div>
            <div><dt>Amount</dt><dd>&#8377;<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></dd></div>
            <div><dt>Payment Method</dt><dd>${order.payment.methodLabel}</dd></div>
            <div><dt>Pickup</dt><dd>${order.pickupTimeText}, ${order.pickupDateText}</dd></div>
        </dl>

        <p class="success-message">Your order has been placed successfully!</p>

        <div class="success-actions">
            <a href="${ctx}/order-details?id=${order.orderId}" class="btn btn-primary">Track My Order</a>
            <a href="${ctx}/menu" class="btn btn-outline">Back to Menu</a>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
