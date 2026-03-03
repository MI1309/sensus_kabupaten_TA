package com.kabupaten.main;

import com.kabupaten.database.DatabaseConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class DataSeeder {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            System.out.println("Menghubungkan ke database dan membaca sql/dummy_data_jombang.sql...");
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader("sql/dummy_data_jombang.sql"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Abaikan komentar
                    if (line.trim().startsWith("--"))
                        continue;
                    sb.append(line).append("\n");
                }
            }

            String[] queries = sb.toString().split(";");
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    try {
                        stmt.execute(query.trim());
                    } catch (Exception ex) {
                        System.err.println("Gagal mengeksekusi: " + query.trim());
                        System.err.println("Alasan: " + ex.getMessage());
                    }
                }
            }

            System.out.println("Data dummy berhasil diimport!");

        } catch (Exception e) {
            System.err.println("Gagal terhubung ke database!");
            e.printStackTrace();
        }
    }
}
