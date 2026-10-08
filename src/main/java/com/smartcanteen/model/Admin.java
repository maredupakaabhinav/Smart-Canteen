package com.smartcanteen.model;

/**
 * INHERITANCE: Admin (canteen staff) extends User.
 */
public class Admin extends User {

    public Admin() {
        setRole(ROLE_ADMIN);
    }

    public Admin(int id, String name, String collegeId, String password, String email) {
        super(id, name, collegeId, password, email, ROLE_ADMIN);
    }

    // METHOD OVERRIDING: the Admin version of the abstract methods
    @Override
    public String getDashboardPath() {
        return "/admin/dashboard";
    }

    @Override
    public String getRoleTitle() {
        return "Admin";
    }
}
