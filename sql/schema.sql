-- Database Schema for Sistem Pendataan Kabupaten
-- Created based on DAO analysis

-- 1. Table Users (for Login)
CREATE TABLE IF NOT EXISTS users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- SHA-256 Hash
    nama_lengkap VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Bupati', 'Guest') NOT NULL DEFAULT 'Guest',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Initial Admin User (password: admin123)
-- SHA-256('admin123') = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
INSERT INTO users (username, password, nama_lengkap, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Administrator Sistem', 'Admin'),
('bupati', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Bupati', 'Bupati');

-- 2. Table Provinsi (Referenced by Kabupaten)
CREATE TABLE IF NOT EXISTS provinsi (
    id_provinsi INT AUTO_INCREMENT PRIMARY KEY,
    nama_provinsi VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed Provinsi
INSERT INTO provinsi (nama_provinsi) VALUES ('Jawa Timur');

-- 3. Table Kabupaten
CREATE TABLE IF NOT EXISTS kabupaten (
    id_kabupaten INT AUTO_INCREMENT PRIMARY KEY,
    id_provinsi INT,
    kode_kabupaten VARCHAR(20),
    nama_kabupaten VARCHAR(100) NOT NULL,
    ibukota VARCHAR(100),
    jumlah_penduduk INT DEFAULT 0,
    jumlah_kecamatan INT DEFAULT 0,
    jumlah_desa INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_provinsi) REFERENCES provinsi(id_provinsi) ON DELETE SET NULL
);

-- 4. Table Kecamatan
CREATE TABLE IF NOT EXISTS kecamatan (
    id_kecamatan INT AUTO_INCREMENT PRIMARY KEY,
    nama_kecamatan VARCHAR(100) NOT NULL,
    alamat_kantor TEXT,
    nama_kepala VARCHAR(100),
    alamat_rumah_kepala TEXT,
    no_hp VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 5. Table Desa/Kelurahan
CREATE TABLE IF NOT EXISTS desa_kelurahan (
    id_desa INT AUTO_INCREMENT PRIMARY KEY,
    id_kecamatan INT,
    nama_desa VARCHAR(100) NOT NULL,
    jenis ENUM('DESA', 'KELURAHAN') DEFAULT 'DESA',
    alamat_kantor TEXT,
    nama_kepala VARCHAR(100),
    alamat_rumah_kepala TEXT,
    no_hp VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_kecamatan) REFERENCES kecamatan(id_kecamatan) ON DELETE SET NULL
);

-- 6. Table RTRW
CREATE TABLE IF NOT EXISTS rtrw (
    id_rtrw INT AUTO_INCREMENT PRIMARY KEY,
    nama_desa VARCHAR(100), -- Linked by name based on DAO, ideally should be ID but following code
    rt VARCHAR(10),
    rw VARCHAR(10),
    nama_ketua VARCHAR(100),
    kontak VARCHAR(20),
    alamat TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 7. Table Warga (Inferred existence from CrudWargaPanel)
CREATE TABLE IF NOT EXISTS warga (
    id_warga INT AUTO_INCREMENT PRIMARY KEY,
    nik VARCHAR(20) UNIQUE,
    nama_lengkap VARCHAR(100),
    jenis_kelamin ENUM('L', 'P'),
    tempat_lahir VARCHAR(50),
    tanggal_lahir DATE,
    alamat TEXT,
    id_desa INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_desa) REFERENCES desa_kelurahan(id_desa) ON DELETE SET NULL
);
