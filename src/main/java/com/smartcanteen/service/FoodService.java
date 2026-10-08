package com.smartcanteen.service;

import com.smartcanteen.dao.FoodItemDAO;
import com.smartcanteen.model.Cart;
import com.smartcanteen.model.CartItem;
import com.smartcanteen.model.FoodItem;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * Business logic for the menu: validation and the add / edit / delete rules.
 */
public class FoodService {

    private final FoodItemDAO foodItemDAO = new FoodItemDAO();

    public List<FoodItem> getFoodItems() throws ServiceException {
        try {
            return foodItemDAO.getAllFood();
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public List<FoodItem> getAvailableFoodItems() throws ServiceException {
        try {
            return foodItemDAO.getAvailableFood();
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    /** Throws a ServiceException if the food does not exist. */
    public FoodItem getFoodById(int foodId) throws ServiceException {
        try {
            FoodItem food = foodItemDAO.findById(foodId);
            if (food == null) {
                throw new ServiceException("Food item not found.");
            }
            return food;
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public void addFood(FoodItem food) throws ServiceException {
        validateFood(food);
        try {
            foodItemDAO.addFood(food);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException("A food item with this name already exists.");
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public void updateFood(FoodItem food) throws ServiceException {
        validateFood(food);
        try {
            if (!foodItemDAO.updateFood(food)) {
                throw new ServiceException("Food item not found.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException("A food item with this name already exists.");
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public void setAvailability(int foodId, boolean available) throws ServiceException {
        try {
            if (!foodItemDAO.setAvailability(foodId, available)) {
                throw new ServiceException("Food item not found.");
            }
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    /** Food that was already ordered cannot be deleted (it would break the order history). */
    public void deleteFood(int foodId) throws ServiceException {
        try {
            if (foodItemDAO.isFoodOrdered(foodId)) {
                throw new ServiceException("This item is part of past orders, so it cannot be deleted. "
                        + "Mark it as unavailable instead.");
            }
            if (!foodItemDAO.deleteFood(foodId)) {
                throw new ServiceException("Food item not found.");
            }
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public int countAllFood() throws ServiceException {
        try {
            return foodItemDAO.countAll();
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public int countAvailableFood() throws ServiceException {
        try {
            return foodItemDAO.countAvailable();
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    /**
     * Brings the cart up to date with the database: current price and availability,
     * and removes items the admin has deleted from the menu in the meantime.
     */
    public void refreshCart(Cart cart) throws ServiceException {
        try {
            for (CartItem cartItem : cart.getItems()) {
                FoodItem latest = foodItemDAO.findById(cartItem.getFoodId());
                if (latest == null) {
                    cart.removeItem(cartItem.getFoodId());
                } else {
                    cartItem.setFoodItem(latest);
                }
            }
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    private void validateFood(FoodItem food) throws ServiceException {
        String name = food.getName() == null ? "" : food.getName().trim();
        if (name.length() < 2 || name.length() > 100) {
            throw new ServiceException("Food name must be 2 to 100 characters long.");
        }
        if (food.getDescription() != null && food.getDescription().trim().length() > 255) {
            throw new ServiceException("Description can have at most 255 characters.");
        }
        if (!FoodItem.CATEGORIES.contains(food.getCategory())) {
            throw new ServiceException("Please choose a valid category.");
        }
        if (!(food.getPrice() > 0 && food.getPrice() <= 10000)) {   // written this way so NaN is rejected too
            throw new ServiceException("Please enter a valid price (greater than 0).");
        }
        String image = food.getImage() == null ? "" : food.getImage().trim();
        if (image.length() > 255) {
            throw new ServiceException("Image URL is too long.");
        }
        // Only normal web addresses or paths inside this project are accepted
        if (!image.isEmpty() && !image.startsWith("http://") && !image.startsWith("https://") && image.contains(":")) {
            throw new ServiceException("Image URL must start with http:// or https://");
        }

        // Clean the values before they are saved
        food.setName(name);
        food.setDescription(food.getDescription() == null ? "" : food.getDescription().trim());
        food.setImage(image);
        food.setPrice(Math.round(food.getPrice() * 100.0) / 100.0);
    }
}
