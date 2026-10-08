package com.smartcanteen.model;

/**
 * INHERITANCE: Student extends User. Faculty members also use this class.
 */
public class Student extends User {

    public Student() {
        setRole(ROLE_STUDENT);
    }

    public Student(int id, String name, String collegeId, String password, String email) {
        super(id, name, collegeId, password, email, ROLE_STUDENT);
    }

    // METHOD OVERRIDING: the Student version of the abstract methods
    @Override
    public String getDashboardPath() {
        return "/dashboard";
    }

    @Override
    public String getRoleTitle() {
        return "Student";
    }
}
