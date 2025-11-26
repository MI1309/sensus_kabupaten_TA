package com.kabupaten.services;

import com.kabupaten.dao.UserDAO;
import com.kabupaten.model.User;
import java.util.List;

/**
 * Service layer untuk business logic User
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public List<User> searchUsers(String keyword) {
        return userDAO.searchUsers(keyword);
    }
    
    public User getUserById(int idUser) {
        return userDAO.getUserById(idUser);
    }
    
    public boolean addUser(String username, String password, String namaLengkap, 
                          String email, String role, String status) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNamaLengkap(namaLengkap);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);
        
        return userDAO.addUser(user);
    }
    
    public boolean updateUser(int idUser, String username, String namaLengkap, 
                             String email, String role, String status) {
        User user = userDAO.getUserById(idUser);
        if (user == null) {
            return false;
        }
        
        user.setUsername(username);
        user.setNamaLengkap(namaLengkap);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);
        
        return userDAO.updateUser(user);
    }
    
    public boolean resetPassword(int idUser, String newPassword) {
        return userDAO.resetPassword(idUser, newPassword);
    }
    
    public boolean deleteUser(int idUser) {
        return userDAO.deleteUser(idUser);
    }
    
    public int getTotalCount() {
        return userDAO.getTotalCount();
    }
    
    public int getCountByRole(String role) {
        return userDAO.getCountByRole(role);
    }
}