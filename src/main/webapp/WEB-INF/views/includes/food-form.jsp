<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- The form shared by add-food.jsp and edit-food.jsp.  Parameter: mode = "add" or "edit".
     The request attributes "food" and "categories" are filled in by the servlet. --%>
<form method="post" action="${ctx}/admin/${param.mode == 'edit' ? 'edit-food' : 'add-food'}" class="card form-card">
    <c:if test="${param.mode == 'edit'}">
        <input type="hidden" name="foodId" value="${food.foodId}">
    </c:if>

    <div class="form-group">
        <label for="name">Food Name</label>
        <input type="text" id="name" name="name" class="form-control" maxlength="100" required
               value="<c:out value='${food.name}'/>" placeholder="e.g. Veg Sandwich">
    </div>

    <div class="form-group">
        <label for="description">Description</label>
        <textarea id="description" name="description" class="form-control" rows="3" maxlength="255"
                  placeholder="Short description shown on the menu card"><c:out value="${food.description}"/></textarea>
    </div>

    <div class="form-row">
        <div class="form-group">
            <label for="category">Category</label>
            <select id="category" name="category" class="form-control" required>
                <c:forEach var="category" items="${categories}">
                    <option value="${category}" ${food.category == category ? 'selected' : ''}>${category}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="price">Price (&#8377;)</label>
            <input type="number" id="price" name="price" class="form-control" min="1" max="10000" step="0.01" required
                   value="${food.price > 0 ? food.price : ''}" placeholder="e.g. 50">
        </div>
    </div>

    <div class="form-group">
        <label for="image">Image URL <span class="muted">(optional)</span></label>
        <input type="text" id="image" name="image" class="form-control" maxlength="255"
               value="<c:out value='${food.image}'/>" placeholder="https://... (leave empty to show an emoji)">
    </div>

    <div class="form-group">
        <label for="available">Availability</label>
        <select id="available" name="available" class="form-control">
            <option value="true" ${food.available ? 'selected' : ''}>Available</option>
            <option value="false" ${food.available ? '' : 'selected'}>Unavailable</option>
        </select>
    </div>

    <div class="form-actions">
        <button type="submit" class="btn btn-primary">${param.mode == 'edit' ? 'Save Changes' : 'Add Food'}</button>
        <a href="${ctx}/admin/menu" class="btn btn-outline">Cancel</a>
    </div>
</form>
