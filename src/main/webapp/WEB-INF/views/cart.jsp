<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="My Cart"/>
    <jsp:param name="active" value="cart"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="page-head">
        <h1 class="page-title">My Cart</h1>
    </div>

    <jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

    <%-- Shown by script.js when the last item is removed --%>
    <div class="card empty-state ${empty cart.items ? '' : 'hidden'}" id="emptyCart">
        <p class="empty-title">Your cart is empty.</p>
        <p class="muted">Add some food from the menu to get started.</p>
        <a href="${ctx}/menu" class="btn btn-primary">Browse Menu</a>
    </div>

    <c:if test="${not empty cart.items}">
        <div class="cart-layout" id="cartContent">
            <div class="card cart-card">
                <div class="cart-row cart-head">
                    <span>Food Item</span><span>Price</span><span>Quantity</span><span>Subtotal</span><span></span>
                </div>

                <c:forEach var="item" items="${cart.items}">
                    <div class="cart-row" data-food-id="${item.foodId}">
                        <div class="cart-item-name">
                            <span class="cart-item-icon">${item.foodItem.icon}</span>
                            <div>
                                <strong><c:out value="${item.foodItem.name}"/></strong>
                                <small class="muted"><c:out value="${item.foodItem.category}"/></small>
                                <c:if test="${not item.foodItem.available}">
                                    <span class="availability off">Currently Unavailable &mdash; remove it to continue</span>
                                </c:if>
                            </div>
                        </div>
                        <span class="cart-price" data-label="Price">&#8377;<fmt:formatNumber value="${item.foodItem.price}" pattern="#,##0.00"/></span>
                        <div class="cart-qty" data-label="Quantity">
                            <div class="qty-control">
                                <button type="button" class="qty-btn" data-delta="-1" aria-label="Decrease quantity">&minus;</button>
                                <input type="number" class="qty-input" value="${item.quantity}" min="1" max="10" aria-label="Quantity">
                                <button type="button" class="qty-btn" data-delta="1" aria-label="Increase quantity">+</button>
                            </div>
                        </div>
                        <span class="cart-subtotal" data-label="Subtotal">&#8377;<span class="js-subtotal"><fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span></span>
                        <button type="button" class="btn btn-sm btn-danger-outline btn-remove">Remove</button>
                    </div>
                </c:forEach>
            </div>

            <aside class="card summary-card">
                <h2 class="card-title">Order Summary</h2>
                <div class="summary-line"><span>Subtotal</span><span>&#8377;<span class="js-total"><fmt:formatNumber value="${cart.totalAmount}" pattern="#,##0.00"/></span></span></div>
                <div class="summary-line summary-total"><span>Total</span><span>&#8377;<span class="js-total"><fmt:formatNumber value="${cart.totalAmount}" pattern="#,##0.00"/></span></span></div>
                <p class="muted small-text">The final amount is calculated again by the server when you place the order.</p>
                <c:choose>
                    <c:when test="${hasUnavailableItem}">
                        <button type="button" class="btn btn-primary btn-block" disabled>Proceed to Checkout</button>
                        <p class="small-text text-danger">Remove the unavailable item to continue.</p>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/checkout" class="btn btn-primary btn-block">Proceed to Checkout</a>
                    </c:otherwise>
                </c:choose>
                <a href="${ctx}/menu" class="btn btn-outline btn-block">Add more items</a>
            </aside>
        </div>
    </c:if>
</main>

<div class="toast hidden" id="toast" role="status"></div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
