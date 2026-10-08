<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="About"/>
    <jsp:param name="active" value="about"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<main class="container page">
    <div class="page-head">
        <span class="java-label java-label-dark">&#9749; JAVA OOP PROJECT</span>
        <h1 class="page-title">SMART CANTEEN</h1>
        <p class="page-subtitle">Java OOP-Based Canteen Ordering &amp; Management System</p>
    </div>

    <div class="card about-card">
        <p>
            Smart Canteen is a college project built to show how the main ideas of Java OOP work together in a real
            web application. Students and faculty pre-order food, choose a pickup time and track the order;
            the canteen staff manage the menu and the orders. All the data is stored in a MySQL database.
        </p>
    </div>

    <div class="grid grid-3">
        <div class="card about-card">
            <h2 class="card-title">Project Features</h2>
            <ul class="check-list">
                <li>User Authentication</li>
                <li>Food Menu</li>
                <li>Cart Management</li>
                <li>Online Demo Payment</li>
                <li>Pickup Scheduling</li>
                <li>Order Tracking</li>
                <li>Order Cancellation</li>
                <li>Admin Menu Management</li>
                <li>Admin Order Management</li>
            </ul>
        </div>

        <div class="card about-card">
            <h2 class="card-title">Technology</h2>
            <div class="chips">
                <span class="chip">Java</span>
                <span class="chip">Java OOP</span>
                <span class="chip">Servlets</span>
                <span class="chip">JSP + JSTL</span>
                <span class="chip">JDBC</span>
                <span class="chip">MySQL</span>
                <span class="chip">Apache Tomcat</span>
                <span class="chip">Maven</span>
                <span class="chip">HTML</span>
                <span class="chip">CSS</span>
                <span class="chip">JavaScript</span>
            </div>
        </div>

        <div class="card about-card">
            <h2 class="card-title">OOP Concepts</h2>
            <dl class="concept-list">
                <dt>Encapsulation</dt>
                <dd>Private fields + getters/setters in every model class.</dd>
                <dt>Inheritance</dt>
                <dd><code>User</code> &rarr; <code>Student</code>, <code>Admin</code></dd>
                <dt>Polymorphism</dt>
                <dd>Each user type gives its own dashboard; each payment service handles payment its own way.</dd>
                <dt>Abstraction</dt>
                <dd>Abstract class <code>User</code>.</dd>
                <dt>Interfaces</dt>
                <dd><code>PaymentService</code> &rarr; <code>DemoPaymentService</code>, <code>CashPaymentService</code></dd>
            </dl>
        </div>
    </div>

    <div class="card about-card">
        <h2 class="card-title">How a request travels</h2>
        <div class="arch-strip">
            <div class="arch-box"><strong>HTML / CSS</strong><small>browser</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>Servlet</strong><small>controller</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>Service</strong><small>business logic</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>DAO</strong><small>SQL</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>JDBC</strong><small>driver</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box arch-box-accent"><strong>MySQL</strong><small>database</small></div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
