// ============================================
// 1. MODEL: User.java
// ============================================
package com.kabupaten.model;

import java.sql.Timestamp;

/**
 * Model class untuk tabel User
 */

public class User {
    private int idUser;
    private String username;
    private String password;
    private String namaLengkap;
    private String email;
    private String role; // admin, operator, guest
    private String status; // aktif, nonaktif
    private String fotoProfile;
    private Timestamp lastLogin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public User() {}
    
    public User(String username, String password, String namaLengkap, String email, String role) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.role = role;
        this.status = "aktif"; // default
    }
    
    // Getters and Setters
    public int getIdUser() {
        return idUser;
    }
    
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNamaLengkap() {
        return namaLengkap;
    }
    
    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFotoProfile() {
        return fotoProfile;
    }
    
    public void setFotoProfile(String fotoProfile) {
        this.fotoProfile = fotoProfile;
    }
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}