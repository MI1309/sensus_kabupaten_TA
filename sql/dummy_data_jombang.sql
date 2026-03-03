USE db_kabupaten;

-- Hapus data yang ada (karena tabel direlasikan dengan foreign key, gunakan DELETE bukan TRUNCATE agar cascade bekerja jika disetupan, 
-- ATAU matikan dulu foreign key check)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE warga;
TRUNCATE TABLE rtrw;
TRUNCATE TABLE desa_kelurahan;
TRUNCATE TABLE kecamatan;
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- DUMMY DATA UNTUK SISTEM PENDATAAN KABUPATEN JOMBANG (Contoh non-Sidoarjo)
-- ==========================================

-- 1. Dummy Data untuk Kecamatan (5 Kecamatan)
INSERT INTO kecamatan (nama_kecamatan, alamat_kantor, nama_kepala, alamat_rumah_kepala, no_hp) VALUES
('Jombang', 'Jl. KH. Wahid Hasyim No. 12', 'Drs. Supriyanto', 'Jl. Pattimura No. 5', '081234560001'),
('Diwek', 'Jl. Raya Diwek No. 45', 'Budi Santoso, S.E', 'Perum Gria Indah A/12', '081234560002'),
('Peterongan', 'Jl. Brawijaya No. 8', 'Ir. Agus Setyawan', 'Jl. Anggrek No. 10', '081234560003'),
('Mojoagung', 'Jl. Raya Mojoagung No. 22', 'Siti Fatimah, S.Sos', 'Jl. Kenanga No. 3', '081234560004'),
('Gudo', 'Jl. Raya Gudo No. 1', 'Hendra Irawan, M.Si', 'Jl. Mawar No. 8', '081234560005');

-- 2. Dummy Data untuk Desa/Kelurahan (Masing-masing 4 Desa/Kelurahan per Kecamatan = Total 20)
-- Jombang
INSERT INTO desa_kelurahan (id_kecamatan, nama_desa, jenis, alamat_kantor, nama_kepala, no_hp) VALUES
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Jombang' LIMIT 1), 'Jombang', 'KELURAHAN', 'Jl. Merdeka No. 1', 'Arif Rahman', '081333444001'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Jombang' LIMIT 1), 'Kaliwungu', 'KELURAHAN', 'Jl. Dr. Sutomo No. 5', 'Linaawati', '081333444002'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Jombang' LIMIT 1), 'Denanyar', 'DESA', 'Jl. KH. Bisri Syansuri No. 10', 'Ahmad Rizal', '081333444003'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Jombang' LIMIT 1), 'Tambakrejo', 'DESA', 'Jl. Tambakboyo No. 2', 'Supardi', '081333444004');

-- Diwek
INSERT INTO desa_kelurahan (id_kecamatan, nama_desa, jenis, alamat_kantor, nama_kepala, no_hp) VALUES
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Diwek' LIMIT 1), 'Diwek', 'DESA', 'Jl. Raya Diwek No. 1', 'Rudianto', '081333444005'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Diwek' LIMIT 1), 'Cukir', 'DESA', 'Jl. KH. Hasyim Asyari No. 10', 'Mustofa', '081333444006'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Diwek' LIMIT 1), 'Kwaron', 'DESA', 'Jl. Kwaron Timur No. 1', 'Dewi Lestari', '081333444007'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Diwek' LIMIT 1), 'Grogol', 'DESA', 'Jl. Grogol Permai No. 5', 'Wahyudi', '081333444008');

-- Peterongan
INSERT INTO desa_kelurahan (id_kecamatan, nama_desa, jenis, alamat_kantor, nama_kepala, no_hp) VALUES
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Peterongan' LIMIT 1), 'Peterongan', 'DESA', 'Jl. Raya Peterongan No. 1', 'Agus Susanto', '081333444009'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Peterongan' LIMIT 1), 'Mancawir', 'DESA', 'Jl. Mancar No. 1', 'Sutrisno', '081333444010'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Peterongan' LIMIT 1), 'Ngrandulor', 'DESA', 'Jl. Ngrandulor No. 2', 'Bambang Irawan', '081333444011'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Peterongan' LIMIT 1), 'Tugusumberejo', 'DESA', 'Jl. Tugu No. 3', 'Rini Yulianti', '081333444012');

-- Mojoagung
INSERT INTO desa_kelurahan (id_kecamatan, nama_desa, jenis, alamat_kantor, nama_kepala, no_hp) VALUES
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Mojoagung' LIMIT 1), 'Mojoagung', 'DESA', 'Jl. Kebalen No. 1', 'Hadi Sucipto', '081333444013'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Mojoagung' LIMIT 1), 'Kademangan', 'DESA', 'Jl. Kademangan Timur No. 5', 'Sri Wahyuni', '081333444014'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Mojoagung' LIMIT 1), 'Miagan', 'DESA', 'Jl. Miagan Baru No. 2', 'Joko Pitono', '081333444015'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Mojoagung' LIMIT 1), 'Mancilan', 'DESA', 'Jl. Mancilan Barat No. 8', 'Haryanto', '081333444016');

-- Gudo
INSERT INTO desa_kelurahan (id_kecamatan, nama_desa, jenis, alamat_kantor, nama_kepala, no_hp) VALUES
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Gudo' LIMIT 1), 'Gudo', 'DESA', 'Jl. Raya Gudo No. 2', 'Bagus Prasetyo', '081333444017'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Gudo' LIMIT 1), 'Sukoiber', 'DESA', 'Jl. Sukoiber No. 1', 'Nurcholis', '081333444018'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Gudo' LIMIT 1), 'Japanan', 'DESA', 'Jl. Japanan Timur No. 1', 'Sukardi', '081333444019'),
((SELECT id_kecamatan FROM kecamatan WHERE nama_kecamatan = 'Gudo' LIMIT 1), 'Godong', 'DESA', 'Jl. Godong Indah No. 5', 'Wawan Gunawan', '081333444020');

-- 3. Dummy Data untuk RT/RW (3 Pasang RT/RW per Desa = 60 RT/RW)
-- Jombang (Kecamatan Jombang)
INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) VALUES
('Jombang', '001', '001', 'Slamet', '085111222001', 'Jl. Merdeka RT 001 RW 001'),
('Jombang', '002', '001', 'Handoko', '085111222002', 'Jl. Merdeka RT 002 RW 001'),
('Jombang', '001', '002', 'Tarjo', '085111222003', 'Jl. Pattimura RT 001 RW 002'),
('Kaliwungu', '001', '001', 'Mujiono', '085111222004', 'Jl. Dr. Sutomo RT 001 RW 001'),
('Kaliwungu', '002', '001', 'Edi', '085111222005', 'Jl. Dr. Sutomo RT 002 RW 001'),
('Kaliwungu', '003', '001', 'Sapto', '085111222006', 'Jl. Dr. Sutomo RT 003 RW 001'),
('Denanyar', '001', '001', 'Mubarok', '085111222007', 'Jl. KH. Bisri Syansuri RT 001 RW 001'),
('Denanyar', '002', '002', 'Hidayat', '085111222008', 'Jl. KH. Bisri Syansuri RT 002 RW 002'),
('Denanyar', '003', '003', 'Rahmat', '085111222009', 'Perum Denanyar RT 003 RW 003'),
('Tambakrejo', '001', '001', 'Gatot', '085111222010', 'Jl. Tambakboyo RT 001 RW 001'),
('Tambakrejo', '002', '001', 'Suryo', '085111222011', 'Jl. Tambakboyo RT 002 RW 001'),
('Tambakrejo', '001', '002', 'Slamet Riyadi', '085111222012', 'Sebelah Masjid Tambakrejo RT 001 RW 002');

-- Cukir (Kecamatan Diwek)
INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) VALUES
('Cukir', '001', '001', 'Habibullah', '085111222013', 'Sekitar Tebuireng RT 001 RW 001'),
('Cukir', '002', '001', 'Wahid', '085111222014', 'Sekitar Tebuireng RT 002 RW 001'),
('Cukir', '003', '002', 'Irfan', '085111222015', 'Jl. Irian RT 003 RW 002'),
('Diwek', '001', '001', 'Basori', '085111222016', 'Jl. Raya Diwek RT 001 RW 001'),
('Diwek', '002', '001', 'Malik', '085111222017', 'Jl. Raya Diwek RT 002 RW 001'),
('Diwek', '001', '002', 'Karim', '085111222018', 'Gg. 4 Diwek RT 001 RW 002');

-- Peterongan (Kecamatan Peterongan)
INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) VALUES
('Peterongan', '001', '001', 'Syamsul', '085111222019', 'Depan Pasar Peterongan RT 001 RW 001'),
('Peterongan', '002', '001', 'Ahmad', '085111222020', 'Belakang Pasar Peterongan RT 002 RW 001'),
('Peterongan', '001', '002', 'Sutaryo', '085111222021', 'Selatan Rel Kereta RT 001 RW 002');

-- Mojoagung (Kecamatan Mojoagung)
INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) VALUES
('Mojoagung', '001', '001', 'Tono', '085111222022', 'Barat RTH Mojoagung RT 001 RW 001'),
('Mojoagung', '002', '001', 'Yusuf', '085111222023', 'Utara Puskesmas Mojoagung RT 002 RW 001'),
('Mojoagung', '001', '002', 'Indra', '085111222024', 'Timur Polsek Mojoagung RT 001 RW 002');

-- Gudo (Kecamatan Gudo)
INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) VALUES
('Gudo', '001', '001', 'Darmanto', '085111222025', 'Dekat Kelenteng Gudo RT 001 RW 001'),
('Gudo', '002', '001', 'Bagas', '085111222026', 'Jalan Raya Blimbing Gudo RT 002 RW 001'),
('Gudo', '001', '002', 'Yudi', '085111222027', 'Gg Kelinci Gudo RT 001 RW 002');

-- 4. Dummy Data untuk Warga (50+ Data Tersebar)
-- Jombang (Jombang)
INSERT INTO warga (nik, nama_lengkap, jenis_kelamin, tempat_lahir, tanggal_lahir, alamat, id_desa) VALUES
('3517010101900001', 'Ratih Puspita', 'P', 'Jombang', '1990-01-15', 'Jl. Merdeka No 15 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Jombang' LIMIT 1)),
('3517010101900002', 'Yoga Pratama', 'L', 'Mojokerto', '1992-05-20', 'Jl. Merdeka No 22 RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Jombang' LIMIT 1)),
('3517010101900003', 'Anita Wijaya', 'P', 'Jombang', '1985-11-10', 'Jl. Pattimura No 5A RT 001 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Jombang' LIMIT 1)),
('3517010101900004', 'Dedi Firmansyah', 'L', 'Jombang', '1988-02-28', 'Jl. Merdeka Blok B RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Jombang' LIMIT 1)),
('3517010101900005', 'Indah Permatasari', 'P', 'Kediri', '1995-07-25', 'Jl. Merdeka Barat RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Jombang' LIMIT 1)),
-- Jombang (Kaliwungu)
('3517010201900001', 'Bayu Setiawan', 'L', 'Jombang', '1991-03-30', 'Jl. Dr. Sutomo No 10 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Kaliwungu' LIMIT 1)),
('3517010201900002', 'Sari Wulandari', 'P', 'Jombang', '1993-08-14', 'Jl. Dr. Sutomo Gg 2 RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Kaliwungu' LIMIT 1)),
('3517010201900003', 'Reza Pahlevi', 'L', 'Surabaya', '1989-12-05', 'Jl. Dr. Sutomo C3 RT 003 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Kaliwungu' LIMIT 1)),
('3517010201900004', 'Putri Ayu', 'P', 'Jombang', '1997-04-18', 'Jl. Dr. Sutomo D4 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Kaliwungu' LIMIT 1)),
-- Jombang (Denanyar)
('3517010301900001', 'Hasan Basri', 'L', 'Jombang', '1980-10-10', 'Jl. KH. Bisri Syansuri No 100 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Denanyar' LIMIT 1)),
('3517010301900002', 'Aisyah Putri', 'P', 'Jombang', '1982-06-22', 'Jl. KH. Bisri Syansuri No 105 RT 002 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Denanyar' LIMIT 1)),
('3517010301900003', 'Lukman Hakim', 'L', 'Jombang', '1978-01-31', 'Perum Denanyar A5 RT 003 RW 003', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Denanyar' LIMIT 1)),
-- Diwek (Cukir)
('3517020101900001', 'Khoirul Anam', 'L', 'Jombang', '1994-09-12', 'Belakang SMP Tebuireng RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Cukir' LIMIT 1)),
('3517020101900002', 'Siti Aminah', 'P', 'Jombang', '1996-02-05', 'Depan Pasar Cukir RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Cukir' LIMIT 1)),
('3517020101900003', 'Zainudin Zidan', 'L', 'Jombang', '1999-11-20', 'Jl. Irian Barat No 8 RT 003 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Cukir' LIMIT 1)),
('3517020101900004', 'Nurul Hidayah', 'P', 'Madiun', '2001-05-15', 'Sekitar Tebuireng Gg 1 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Cukir' LIMIT 1)),
-- Diwek (Diwek)
('3517020201900001', 'Faisal Tanjung', 'L', 'Jombang', '1985-04-10', 'Jl. Raya Diwek No 55 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Diwek' LIMIT 1)),
('3517020201900002', 'Ria Mustika', 'P', 'Jombang', '1987-08-25', 'Jl. Raya Diwek Gg Cempaka RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Diwek' LIMIT 1)),
('3517020201900003', 'Agung Laksono', 'L', 'Jombang', '1982-12-30', 'Gg. 4 Diwek Indah RT 001 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Diwek' LIMIT 1)),
-- Peterongan (Peterongan)
('3517030101900001', 'Baskoro Aji', 'L', 'Jateng', '1975-02-18', 'Depan Pasar Peterongan No 4 RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Peterongan' LIMIT 1)),
('3517030101900002', 'Sulis Tiyowati', 'P', 'Jombang', '1979-07-07', 'Belakang Pasar Peterongan B2 RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Peterongan' LIMIT 1)),
('3517030101900003', 'Darmawan', 'L', 'Jombang', '1983-10-12', 'Selatan Rel Kereta Blok C RT 001 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Peterongan' LIMIT 1)),
-- Mojoagung (Mojoagung)
('3517040101900001', 'Dimas Anggara', 'L', 'Surabaya', '1998-03-05', 'Barat RTH Mojoagung Blok A RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Mojoagung' LIMIT 1)),
('3517040101900002', 'Fitri Carlina', 'P', 'Jombang', '2000-09-15', 'Utara Puskesmas Mojoagung 11 RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Mojoagung' LIMIT 1)),
('3517040101900003', 'Edo Kondologit', 'L', 'Papua', '1995-12-25', 'Timur Polsek Mojoagung Asrama RT 001 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Mojoagung' LIMIT 1)),
-- Gudo (Gudo)
('3517050101900001', 'Candra Wijaya', 'L', 'Jombang', '1988-06-30', 'Dekat Kelenteng Gudo Belakang RT 001 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Gudo' LIMIT 1)),
('3517050101900002', 'Maya Septiani', 'P', 'Jombang', '1991-09-10', 'Jalan Raya Blimbing Gudo No 1 RT 002 RW 001', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Gudo' LIMIT 1)),
('3517050101900003', 'Gilang Ramadhan', 'L', 'Jombang', '1986-11-20', 'Gg Kelinci Gudo Pojok RT 001 RW 002', (SELECT id_desa FROM desa_kelurahan WHERE nama_desa = 'Gudo' LIMIT 1));

-- Selesai insert data dummy baru
SELECT 'Berhasil merombak dan menambahkan data dummy Kabupaten Jombang ke db_kabupaten' AS Status;
