<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Home"/>
    <jsp:param name="active" value="home"/>
    <jsp:param name="css" value="menu.css"/>
</jsp:include>

<%-- ========== HERO ========== --%>
<section class="hero">
    <div class="container hero-inner">
        <div class="hero-text">
            <span class="java-label">&#9749; JAVA OOP PROJECT</span>
            <h1>Smart Canteen</h1>
            <p class="hero-subtitle">Java OOP-Based Canteen Ordering &amp; Management System</p>
            <p class="hero-tagline">&ldquo;Order smart. Skip the queue.&rdquo;</p>
            <p class="hero-description">Pre-order your food, choose your pickup time, and skip the queue.</p>

            <div class="hero-actions">
                <c:choose>
                    <c:when test="${not empty loggedInUser}">
                        <a href="${ctx}${loggedInUser.dashboardPath}" class="btn btn-accent">Go to Dashboard</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/login" class="btn btn-accent">Login</a>
                    </c:otherwise>
                </c:choose>
                <a href="${ctx}/menu" class="btn btn-outline-light">View Menu</a>
            </div>
        </div>

        <%-- Decorative only: a code card and the "Class -> Object -> Order" idea --%>
        <div class="hero-visual" aria-hidden="true">
            <div class="code-card">
                <div class="code-card-bar"><span></span><span></span><span></span><em>SmartCanteen.java</em></div>
<pre><code><span class="kw">public class</span> <span class="cls">SmartCanteen</span> {

    <span class="kw">public void</span> <span class="fn">placeOrder</span>() {
        System.out.println(<span class="str">"Order Placed!"</span>);
    }

}</code></pre>
            </div>

            <div class="class-flow">
                <div class="flow-title">JAVA OOP &nbsp;&middot;&nbsp; Classes &rarr; Objects &rarr; Orders</div>
                <div class="flow-boxes">
                    <div class="flow-box">CLASS</div>
                    <div class="flow-arrow">&rarr;</div>
                    <div class="flow-box">OBJECT</div>
                    <div class="flow-arrow">&rarr;</div>
                    <div class="flow-box flow-box-accent">ORDER</div>
                </div>
            </div>
        </div>
    </div>
</section>

<%-- ========== HOW IT WORKS ========== --%>
<section class="section">
    <div class="container">
        <h2 class="section-title">How it works</h2>
        <div class="grid grid-4">
            <div class="card step-card">
                <div class="step-number">1</div>
                <h3>Login</h3>
                <p>Sign in with your college ID and password.</p>
            </div>
            <div class="card step-card">
                <div class="step-number">2</div>
                <h3>Pick your food</h3>
                <p>Browse today&rsquo;s menu and add items to your cart.</p>
            </div>
            <div class="card step-card">
                <div class="step-number">3</div>
                <h3>Choose pickup time</h3>
                <p>Select a time slot and pay (demo payment).</p>
            </div>
            <div class="card step-card">
                <div class="step-number">4</div>
                <h3>Collect &amp; enjoy</h3>
                <p>Track your order and pick it up when it is ready.</p>
            </div>
        </div>
    </div>
</section>

<%-- ========== BEHIND THE SCENES ========== --%>
<section class="section section-alt">
    <div class="container">
        <h2 class="section-title">Behind the scenes</h2>
        <p class="section-intro">Every click travels through the same layers &mdash; this is the Java OOP architecture of the project.</p>
        <div class="arch-strip">
            <div class="arch-box"><strong>Browser</strong><small>HTML / CSS / JS</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>Servlet</strong><small>controller</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>Service</strong><small>business logic</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>DAO</strong><small>SQL queries</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box"><strong>JDBC</strong><small>driver</small></div>
            <div class="arch-arrow">&rarr;</div>
            <div class="arch-box arch-box-accent"><strong>MySQL</strong><small>database</small></div>
        </div>
        <p class="center-text"><a href="${ctx}/about" class="btn btn-primary">About the project</a></p>
    </div>
</section>

<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>
