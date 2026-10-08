<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Checkout"/>
    <jsp:param name="active" value="cart"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="page-head">
        <h1 class="page-title">Checkout</h1>
        <p class="page-subtitle">Choose your pickup time and payment method.</p>
    </div>

    <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

    <form method="post" action="${ctx}/checkout" id="checkoutForm" class="checkout-layout"
          data-today="${todayValue}" data-cutoff="${cutoffMinutes}">

        <div class="checkout-main">
            <%-- Pickup --%>
            <div class="card checkout-section">
                <h2 class="card-title">1. Pickup time</h2>
                <div class="form-group">
                    <label for="pickupDate">Pickup Date</label>
                    <select id="pickupDate" name="pickupDate" class="form-control">
                        <option value="${todayValue}" ${selectedDate == todayValue ? 'selected' : ''}>${todayLabel}</option>
                        <option value="${tomorrowValue}" ${selectedDate == tomorrowValue ? 'selected' : ''}>${tomorrowLabel}</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Pickup Time</label>
                    <div class="slot-grid" id="slotGrid">
                        <c:forEach var="slot" items="${slots}">
                            <label class="slot">
                                <input type="radio" name="pickupSlot" value="${slot.value}"
                                       data-minutes="${slot.minutesOfDay}" ${param.pickupSlot == slot.value ? 'checked' : ''} required>
                                <span>${slot.label}</span>
                            </label>
                        </c:forEach>
                    </div>
                    <p class="small-text muted" id="slotHint">Slots that are already over or too close are disabled.</p>
                </div>
            </div>

            <%-- Payment --%>
            <div class="card checkout-section">
                <h2 class="card-title">2. Payment method</h2>
                <div class="pay-options">
                    <label class="pay-option">
                        <input type="radio" name="paymentMethod" value="UPI" ${param.paymentMethod == 'UPI' ? 'checked' : ''} required>
                        <span><strong>UPI</strong><small>Demo online payment</small></span>
                    </label>
                    <label class="pay-option">
                        <input type="radio" name="paymentMethod" value="CARD" ${param.paymentMethod == 'CARD' ? 'checked' : ''}>
                        <span><strong>Card</strong><small>Demo online payment</small></span>
                    </label>
                    <label class="pay-option">
                        <input type="radio" name="paymentMethod" value="CASH" ${param.paymentMethod == 'CASH' ? 'checked' : ''}>
                        <span><strong>Cash at Counter</strong><small>Pay when you collect the food</small></span>
                    </label>
                </div>
                <p class="demo-note">&#9432; This is a <strong>DEMO / simulated payment</strong>. No real money is charged and no card or UPI details are asked.</p>
            </div>
        </div>

        <%-- Order summary --%>
        <aside class="card summary-card">
            <h2 class="card-title">Order Summary</h2>
            <ul class="summary-items">
                <c:forEach var="item" items="${cart.items}">
                    <li>
                        <span><c:out value="${item.foodItem.name}"/> <small class="muted">x ${item.quantity}</small></span>
                        <span>&#8377;<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
                    </li>
                </c:forEach>
            </ul>
            <div class="summary-line summary-total"><span>Total</span><span>&#8377;<fmt:formatNumber value="${orderTotal}" pattern="#,##0.00"/></span></div>
            <button type="submit" class="btn btn-accent btn-block" id="payButton">Pay &amp; Place Order</button>
            <a href="${ctx}/cart" class="btn btn-outline btn-block">Back to Cart</a>
        </aside>
    </form>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
