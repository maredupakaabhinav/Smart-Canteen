<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Top of every public / student page: <head>, navbar. Parameters: title, active, css --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<fmt:setLocale value="en_IN" scope="request"/>
<c:set var="cartCount" value="${empty sessionScope.cart ? 0 : sessionScope.cart.totalQuantity}" scope="request"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${param.title}"/> | Smart Canteen</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <c:if test="${not empty param.css}">
        <link rel="stylesheet" href="${ctx}/css/<c:out value='${param.css}'/>">
    </c:if>
</head>
<body data-ctx="${ctx}">

<header class="site-header">
    <div class="container nav-inner">
        <a class="logo" href="${ctx}/">
            <span class="logo-icon">&#9749;</span>
            <span class="logo-text">SMART CANTEEN<small>JAVA OOP PROJECT</small></span>
        </a>

        <button type="button" class="nav-toggle" id="navToggle" aria-label="Open menu" aria-expanded="false">&#9776;</button>

        <nav class="main-nav" id="mainNav">
            <c:choose>
                <c:when test="${loggedInUser.role == 'STUDENT'}">
                    <a href="${ctx}/dashboard" class="${param.active == 'home' ? 'active' : ''}">Home</a>
                    <a href="${ctx}/menu" class="${param.active == 'menu' ? 'active' : ''}">Menu</a>
                    <a href="${ctx}/cart" class="${param.active == 'cart' ? 'active' : ''}">My Cart
                        <span class="cart-badge ${cartCount == 0 ? 'hidden' : ''}" id="cartBadge">${cartCount}</span></a>
                    <a href="${ctx}/orders" class="${param.active == 'orders' ? 'active' : ''}">My Orders</a>
                    <a href="${ctx}/profile" class="${param.active == 'profile' ? 'active' : ''}">Profile</a>
                    <a href="${ctx}/logout" class="nav-logout">Logout</a>
                </c:when>
                <c:when test="${loggedInUser.role == 'ADMIN'}">
                    <a href="${ctx}/" class="${param.active == 'home' ? 'active' : ''}">Home</a>
                    <a href="${ctx}/about" class="${param.active == 'about' ? 'active' : ''}">About</a>
                    <a href="${ctx}/admin/dashboard">Admin Panel</a>
                    <a href="${ctx}/logout" class="nav-logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${ctx}/" class="${param.active == 'home' ? 'active' : ''}">Home</a>
                    <a href="${ctx}/menu" class="${param.active == 'menu' ? 'active' : ''}">Menu</a>
                    <a href="${ctx}/about" class="${param.active == 'about' ? 'active' : ''}">About</a>
                    <a href="${ctx}/login" class="nav-login">Login</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
