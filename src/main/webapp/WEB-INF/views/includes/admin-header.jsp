<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Top of every admin page: top bar + sidebar. Parameters: title, active --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<fmt:setLocale value="en_IN" scope="request"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${param.title}"/> | Admin Panel | Smart Canteen</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
    <link rel="stylesheet" href="${ctx}/css/admin.css">
</head>
<body class="admin-body" data-ctx="${ctx}">

<header class="admin-topbar">
    <a class="logo" href="${ctx}/admin/dashboard">
        <span class="logo-icon">&#9749;</span>
        <span class="logo-text">SMART CANTEEN<small>ADMIN PANEL</small></span>
    </a>
    <div class="admin-user">
        <span class="admin-user-name"><c:out value="${loggedInUser.name}"/> <small>(<c:out value="${loggedInUser.collegeId}"/>)</small></span>
        <a href="${ctx}/logout" class="btn btn-outline-light btn-sm">Logout</a>
    </div>
</header>

<div class="admin-layout">
    <aside class="sidebar">
        <nav class="sidebar-nav">
            <a href="${ctx}/admin/dashboard" class="${param.active == 'dashboard' ? 'active' : ''}">&#9638; Dashboard</a>
            <a href="${ctx}/admin/menu" class="${param.active == 'menu' ? 'active' : ''}">&#127860; Food Menu</a>
            <a href="${ctx}/admin/add-food" class="${param.active == 'add' ? 'active' : ''}">&#10133; Add Food</a>
            <a href="${ctx}/admin/orders" class="${param.active == 'orders' ? 'active' : ''}">&#128203; Orders</a>
            <a href="${ctx}/logout" class="sidebar-logout">&#10140; Logout</a>
        </nav>
    </aside>

    <main class="admin-main">
