package com.kabupaten.util;

import com.kabupaten.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class FasilitasSchemaUpdate {
    public static void main(String[] args) {
        String sql = "ALTER TABLE fasilitas ADD COLUMN foto_url VARCHAR(255) AFTER keterangan";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Updating fasilitas table schema...");
            stmt.executeUpdate(sql);
            System.out.println("Successfully added foto_url column to fasilitas table.");
            
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate column name")) {
                System.out.println("Column foto_url already exists.");
            } else {
                System.err.println("Error updating schema: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
