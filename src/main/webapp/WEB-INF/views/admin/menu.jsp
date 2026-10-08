<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/admin-header.jsp">
    <jsp:param name="title" value="Food Menu"/>
    <jsp:param name="active" value="menu"/>
</jsp:include>

<div class="admin-title-row">
    <div>
        <h1 class="admin-title">Food Menu</h1>
        <p class="admin-subtitle">Add, edit, delete and switch availability of food items.</p>
    </div>
    <a href="${ctx}/admin/add-food" class="btn btn-primary">+ Add Food</a>
</div>

<jsp:include page="/WEB-INF/views/includes/messages.jsp"/>

<c:choose>
    <c:when test="${empty foodItems}">
        <div class="card empty-state">
            <p>There is no food on the menu yet.</p>
            <a href="${ctx}/admin/add-food" class="btn btn-primary">Add the first item</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card table-card">
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr><th>Food</th><th>Category</th><th>Price</th><th>Status</th><th>Actions</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="food" items="${foodItems}">
                        <tr>
                            <td>
                                <div class="food-cell">
                                    <span class="food-cell-icon">${food.icon}</span>
                                    <div>
                                        <strong><c:out value="${food.name}"/></strong>
                                        <small class="muted block"><c:out value="${food.description}"/></small>
                                    </div>
                                </div>
                            </td>
                            <td><span class="tag"><c:out value="${food.category}"/></span></td>
                            <td>&#8377;<fmt:formatNumber value="${food.price}" pattern="#,##0.00"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${food.available}"><span class="badge badge-ok">Available</span></c:when>
                                    <c:otherwise><span class="badge badge-off">Unavailable</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="action-group">
                                    <form method="post" action="${ctx}/admin/menu">
                                        <input type="hidden" name="action" value="availability">
                                        <input type="hidden" name="foodId" value="${food.foodId}">
                                        <input type="hidden" name="available" value="${food.available ? 'false' : 'true'}">
                                        <button type="submit" class="btn btn-outline btn-sm">
                                            ${food.available ? 'Mark Unavailable' : 'Mark Available'}
                                        </button>
                                    </form>
                                    <a href="${ctx}/admin/edit-food?id=${food.foodId}" class="btn btn-outline btn-sm">Edit</a>
                                    <form method="post" action="${ctx}/admin/menu"
                                          data-confirm="Delete &quot;<c:out value='${food.name}'/>&quot;? This cannot be undone.">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="foodId" value="${food.foodId}">
                                        <button type="submit" class="btn btn-danger-outline btn-sm">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/includes/admin-footer.jsp"/>
