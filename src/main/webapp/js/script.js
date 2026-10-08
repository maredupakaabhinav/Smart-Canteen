/* ==========================================================
   SMART CANTEEN - basic JavaScript
   JavaScript only does small UI jobs (filtering, quantity buttons, messages).
   Everything important (cart, prices, orders, status) lives on the Java server.
   ========================================================== */
(function () {
    'use strict';

    var contextPath = document.body.dataset.ctx || '';

    // ---------- small helpers ----------

    function formatMoney(value) {
        return new Intl.NumberFormat('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(Number(value));
    }

    var toastTimer = null;
    function showToast(message, isError) {
        var toast = document.getElementById('toast');
        if (!toast) { return; }
        toast.textContent = message;
        toast.classList.toggle('toast-error', !!isError);
        toast.classList.remove('hidden');
        clearTimeout(toastTimer);
        toastTimer = setTimeout(function () { toast.classList.add('hidden'); }, 2800);
    }

    function updateCartBadge(count) {
        var badge = document.getElementById('cartBadge');
        if (!badge) { return; }
        badge.textContent = count;
        badge.classList.toggle('hidden', Number(count) === 0);
    }

    // Sends a request to the CartServlet and returns the JSON answer (or null if we were sent to the login page).
    function postToCart(params) {
        return fetch(contextPath + '/cart', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams(params)
        }).then(function (response) {
            if (response.redirected) {          // session expired -> the server sent us to /login
                window.location.href = response.url;
                return null;
            }
            return response.json();
        });
    }

    var NETWORK_ERROR = 'Something went wrong. Please try again.';

    // ---------- mobile menu button ----------
    var navToggle = document.getElementById('navToggle');
    var mainNav = document.getElementById('mainNav');
    if (navToggle && mainNav) {
        navToggle.addEventListener('click', function () {
            var open = mainNav.classList.toggle('open');
            navToggle.setAttribute('aria-expanded', open ? 'true' : 'false');
        });
    }

    // ---------- "Are you sure?" for forms with data-confirm ----------
    document.addEventListener('submit', function (event) {
        var message = event.target.dataset ? event.target.dataset.confirm : null;
        if (message && !window.confirm(message)) {
            event.preventDefault();
        }
    });

    // ---------- food image that fails to load -> show the emoji instead ----------
    function showIconInsteadOfImage(image) {
        var icon = document.createElement('span');
        icon.className = 'food-icon';
        icon.textContent = image.dataset.icon;
        image.replaceWith(icon);
    }
    document.addEventListener('error', function (event) {
        var image = event.target;
        if (image.tagName === 'IMG' && image.dataset.icon) {
            showIconInsteadOfImage(image);
        }
    }, true);
    // An image may already have failed before this script ran, so check those too
    document.querySelectorAll('img[data-icon]').forEach(function (image) {
        if (image.complete && image.naturalWidth === 0) {
            showIconInsteadOfImage(image);
        }
    });

    // ---------- login page: label changes with the selected login type ----------
    var loginSwitch = document.getElementById('loginTypeSwitch');
    if (loginSwitch) {
        var idLabel = document.getElementById('idLabel');
        var idInput = document.getElementById('collegeId');
        loginSwitch.addEventListener('change', function (event) {
            var isAdmin = event.target.value === 'admin';
            idLabel.textContent = isAdmin ? 'Admin ID' : 'College ID';
            idInput.placeholder = isAdmin ? 'e.g. ADMIN001' : 'e.g. STU001';
        });
    }

    // ---------- menu page: search + category filter ----------
    var foodGrid = document.getElementById('foodGrid');
    if (foodGrid) {
        var foodCards = Array.prototype.slice.call(foodGrid.querySelectorAll('.food-card'));
        var searchBox = document.getElementById('foodSearch');
        var categoryButtons = document.querySelectorAll('#categoryFilters .filter-btn');
        var noResults = document.getElementById('noResults');
        var activeCategory = 'All';

        var applyFoodFilter = function () {
            var query = searchBox.value.trim().toLowerCase();
            var visibleCount = 0;
            foodCards.forEach(function (card) {
                var name = card.dataset.name.toLowerCase();
                var category = card.dataset.category;
                var categoryMatches = activeCategory === 'All' || category === activeCategory;
                var textMatches = query === '' || name.indexOf(query) !== -1 || category.toLowerCase().indexOf(query) !== -1;
                var show = categoryMatches && textMatches;
                card.classList.toggle('hidden', !show);
                if (show) { visibleCount++; }
            });
            noResults.classList.toggle('hidden', visibleCount > 0);
        };

        searchBox.addEventListener('input', applyFoodFilter);
        categoryButtons.forEach(function (button) {
            button.addEventListener('click', function () {
                activeCategory = button.dataset.category;
                categoryButtons.forEach(function (other) { other.classList.toggle('active', other === button); });
                applyFoodFilter();
            });
        });
    }

    // ---------- "Add to Cart" buttons (menu and dashboard) ----------
    document.querySelectorAll('.btn-add').forEach(function (button) {
        button.addEventListener('click', function () {
            button.disabled = true;
            postToCart({ action: 'add', foodId: button.dataset.foodId, quantity: 1 })
                .then(function (result) {
                    if (!result) { return; }
                    showToast(result.message, !result.success);
                    if (result.success) { updateCartBadge(result.cartCount); }
                })
                .catch(function () { showToast(NETWORK_ERROR, true); })
                .then(function () { button.disabled = false; });
        });
    });

    // ---------- cart page: + / - / type a quantity / remove ----------
    var cartContent = document.getElementById('cartContent');
    if (cartContent) {
        var updateTotals = function (result) {
            document.querySelectorAll('.js-total').forEach(function (element) {
                element.textContent = formatMoney(result.total);
            });
            updateCartBadge(result.cartCount);
        };

        var changeQuantity = function (row, newQuantity) {
            var input = row.querySelector('.qty-input');
            if (!Number.isInteger(newQuantity) || newQuantity < 1 || newQuantity > 10) {
                showToast('Please enter a valid quantity (1 to 10).', true);
                input.value = input.dataset.previous;
                return;
            }
            postToCart({ action: 'update', foodId: row.dataset.foodId, quantity: newQuantity })
                .then(function (result) {
                    if (!result) { return; }
                    if (result.success) {
                        input.value = result.itemQuantity;
                        input.dataset.previous = result.itemQuantity;
                        row.querySelector('.js-subtotal').textContent = formatMoney(result.itemSubtotal);
                        updateTotals(result);
                    } else {
                        showToast(result.message, true);
                        input.value = input.dataset.previous;
                    }
                })
                .catch(function () { showToast(NETWORK_ERROR, true); input.value = input.dataset.previous; });
        };

        var removeItem = function (row) {
            postToCart({ action: 'remove', foodId: row.dataset.foodId })
                .then(function (result) {
                    if (!result) { return; }
                    if (result.success) {
                        row.remove();
                        updateTotals(result);
                        if (cartContent.querySelectorAll('.cart-row[data-food-id]').length === 0) {
                            cartContent.classList.add('hidden');
                            document.getElementById('emptyCart').classList.remove('hidden');
                        }
                    } else {
                        showToast(result.message, true);
                    }
                })
                .catch(function () { showToast(NETWORK_ERROR, true); });
        };

        cartContent.querySelectorAll('.cart-row[data-food-id]').forEach(function (row) {
            var input = row.querySelector('.qty-input');
            input.dataset.previous = input.value;

            row.querySelectorAll('.qty-btn').forEach(function (button) {
                button.addEventListener('click', function () {
                    var newQuantity = parseInt(input.value, 10) + parseInt(button.dataset.delta, 10);
                    if (newQuantity < 1) { return; }   // use "Remove" to delete an item
                    changeQuantity(row, newQuantity);
                });
            });
            input.addEventListener('change', function () { changeQuantity(row, parseInt(input.value, 10)); });
            row.querySelector('.btn-remove').addEventListener('click', function () { removeItem(row); });
        });
    }

    // ---------- checkout page: disable pickup slots that are already over ----------
    var checkoutForm = document.getElementById('checkoutForm');
    if (checkoutForm) {
        var dateSelect = document.getElementById('pickupDate');
        var slotInputs = checkoutForm.querySelectorAll('input[name="pickupSlot"]');
        var slotHint = document.getElementById('slotHint');
        var payButton = document.getElementById('payButton');
        var todayValue = checkoutForm.dataset.today;
        var cutoffMinutes = parseInt(checkoutForm.dataset.cutoff, 10);   // from the server clock

        var refreshSlots = function () {
            var isToday = dateSelect.value === todayValue;
            var enabledCount = 0;
            slotInputs.forEach(function (slot) {
                var tooEarly = isToday && parseInt(slot.dataset.minutes, 10) < cutoffMinutes;
                slot.disabled = tooEarly;
                if (tooEarly && slot.checked) { slot.checked = false; }
                if (!tooEarly) { enabledCount++; }
            });
            slotHint.textContent = enabledCount === 0
                ? 'No pickup slots are left today. Please choose tomorrow.'
                : 'Slots that are already over or too close are disabled.';
        };
        dateSelect.addEventListener('change', refreshSlots);
        refreshSlots();

        // Stop double clicks on "Pay & Place Order"
        checkoutForm.addEventListener('submit', function () {
            payButton.disabled = true;
            payButton.textContent = 'Processing demo payment...';
        });
        window.addEventListener('pageshow', function () {   // when the user comes back with the Back button
            payButton.disabled = false;
            payButton.textContent = 'Pay & Place Order';
        });
    }

    // ---------- admin orders page: filter rows by status ----------
    var orderFilters = document.getElementById('orderFilters');
    var ordersTable = document.getElementById('ordersTable');
    if (orderFilters && ordersTable) {
        var orderRows = ordersTable.querySelectorAll('tbody tr');
        var noOrders = document.getElementById('noOrders');
        var filterButtons = orderFilters.querySelectorAll('.filter-btn');

        filterButtons.forEach(function (button) {
            button.addEventListener('click', function () {
                var status = button.dataset.status;
                var visibleCount = 0;
                filterButtons.forEach(function (other) { other.classList.toggle('active', other === button); });
                orderRows.forEach(function (row) {
                    var show = status === 'ALL' || row.dataset.status === status;
                    row.classList.toggle('hidden', !show);
                    if (show) { visibleCount++; }
                });
                noOrders.classList.toggle('hidden', visibleCount > 0);
            });
        });
    }
})();
