package com.revature;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    public User login(String userName, String password);
    public User Register();
    public List<User> getUsers(User employee) throws SQLException;
    public void StatusUpdate(User employee, int customerID);
}
