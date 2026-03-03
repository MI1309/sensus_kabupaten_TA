USE db_kabupaten;

-- ==========================================
-- DUMMY DATA UNTUK SISTEM PENDATAAN KABUPATEN
-- ==========================================

-- 1. Dummy Data untuk Kecamatan
INSERT INTO kecamatan (nama_kecamatan, alamat_kantor, nama_kepala, alamat_rumah_kepala, no_hp) VALUES
('Sidoarjo', 'Jl. Jenggolo No. 1, Sidoarjo', 'Budi Santoso', 'Jl. Gajah Mada No. 10', '081234567890'),
('Buduran', 'Jl. Raya Buduran No. 2', 'Ahmad Yani', 'Jl. Siwalan No. 5', '081298765432'),
('Candi', 'Jl. Raya Candi No. 3', 'Siti Aminah', 'Jl. Gelam No. 12', '081345678901');

-- 2. Dummy Data untuk Desa/Kelurahan
INSERT INTO desa_kelurahan (id_kecamatan, nama_desa, jenis, alamat_kantor, nama_kepala, no_hp) VALUES
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Sidoarjo' LIMIT 1), 'Magersari', 'KELURAHAN', 'Jl. Magersari No. 1', 'Joko Supriyanto', '082111222333'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Sidoarjo' LIMIT 1), 'Sidokumpul', 'KELURAHAN', 'Jl. Sidokumpul No. 2', 'Bagus Haryono', '082122333444'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Sidoarjo' LIMIT 1), 'Lemahputro', 'KELURAHAN', 'Jl. Lemahputro No. 3', 'Dwi Yanto', '082133444555'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Buduran' LIMIT 1), 'Siwalanpanji', 'DESA', 'Jl. Siwalanpanji No. 1', 'Agus Salim', '083111222333'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Buduran' LIMIT 1), 'Banjarkemantren', 'DESA', 'Jl. Banjarkemantren No. 1', 'Rudi Hermawan', '083122333444'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Candi' LIMIT 1), 'Gelam', 'DESA', 'Jl. Gelam No. 1', 'Hendro', '084111222333'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Candi' LIMIT 1), 'Bligo', 'DESA', 'Jl. Bligo No. 1', 'Rini Sulistyowati', '084122333444');

-- 3. Dummy Data untuk RT/RW
INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) VALUES
('Magersari', '001', '001', 'Sutrisno', '085111111111', 'Jl. Magersari RT 001 RW 001'),
('Magersari', '002', '001', 'Bambang', '085222222222', 'Jl. Magersari RT 002 RW 001'),
('Magersari', '001', '002', 'Hartono', '085333333333', 'Jl. Magersari RT 001 RW 002'),
('Sidokumpul', '001', '001', 'Wahyu', '085444444444', 'Jl. Sidokumpul RT 001 RW 001'),
('Siwalanpanji', '001', '001', 'Anton', '085555555555', 'Jl. Siwalanpanji RT 001 RW 001'),
('Gelam', '001', '001', 'Sugeng', '085666666666', 'Jl. Gelam RT 001 RW 001');

-- 4. Dummy Data untuk Warga
INSERT INTO warga (nik, nama_lengkap, jenis_kelamin, tempat_lahir, tanggal_lahir, alamat, id_desa) VALUES
('3515010101900001', 'Andi Susanto', 'L', 'Sidoarjo', '1990-01-01', 'Jl. Magersari Gg 1 No 5 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Magersari' LIMIT 1)),
('3515010101900002', 'Siti Rahmawati', 'P', 'Surabaya', '1992-05-15', 'Jl. Magersari Gg 1 No 6 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Magersari' LIMIT 1)),
('3515010101900003', 'Hendra Kusuma', 'L', 'Sidoarjo', '1985-08-20', 'Jl. Magersari Gg 2 No 10 RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Magersari' LIMIT 1)),
('3515010101900004', 'Dina Mariana', 'P', 'Malang', '1988-12-10', 'Jl. Magersari Gg 3 No 15 RT 001 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Magersari' LIMIT 1)),
('3515010101900005', 'Fajar Ardiansyah', 'L', 'Sidoarjo', '1995-03-25', 'Jl. Sidokumpul Blok A No 1 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Sidokumpul' LIMIT 1)),
('3515010101900006', 'Rina Agustina', 'P', 'Sidoarjo', '1998-08-17', 'Jl. Sidokumpul Blok B No 5 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Sidokumpul' LIMIT 1)),
('3515010101900007', 'Eko Prasetyo', 'L', 'Sidoarjo', '1980-11-11', 'Desa Siwalanpanji RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Siwalanpanji' LIMIT 1)),
('3515010101900008', 'Maya Sari', 'P', 'Gresik', '1982-02-28', 'Desa Siwalanpanji RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Siwalanpanji' LIMIT 1)),
('3515010101900009', 'Teguh Wibowo', 'L', 'Sidoarjo', '1975-06-05', 'Desa Gelam RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Gelam' LIMIT 1)),
('3515010101900010', 'Endang Lestari', 'P', 'Madiun', '1978-09-30', 'Desa Gelam RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Gelam' LIMIT 1)),
('3515010101900011', 'Arif Rahman', 'L', 'Sidoarjo', '2000-04-12', 'Jl. Magersari Gg 1 No 5 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Magersari' LIMIT 1)),
('3515010101900012', 'Nisa Nabila', 'P', 'Sidoarjo', '2002-07-20', 'Jl. Magersari Gg 1 No 5 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Magersari' LIMIT 1));

-- Selesai insert data dummy
SELECT 'Berhasil menambahkan data dummy ke database db_kabupaten' AS Status;
