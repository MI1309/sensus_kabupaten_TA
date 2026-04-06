USE db_kabupaten;

-- ==========================================
-- DUMMY DATA UNTUK TABEL FASILITAS
-- ==========================================

INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES
('RSUD Kabupaten Jombang', 'Kesehatan', 'Dinas Kesehatan', 'Jl. KH. Wahid Hasyim No. 52, Jombang', 'Rumah sakit umum daerah tipe B dengan fasilitas lengkap.'),
('Puskesmas Peterongan', 'Kesehatan', 'Dinas Kesehatan', 'Jl. Raya Peterongan No. 10, Peterongan', 'Pusat kesehatan masyarakat melayani rawat inap dan jalan.'),
('Pasar Pon Jombang', 'Ekonomi', 'Dinas Perdagangan', 'Jl. KH. Wahid Hasyim, Jombang', 'Pasar tradisional terbesar di pusat kota Jombang.'),
('Pasar Mojoagung', 'Ekonomi', 'Dinas Perdagangan', 'Jl. Raya Mojoagung, Mojoagung', 'Pusat perdagangan utama di wilayah timur Jombang.'),
('SMAN 1 Jombang', 'Pendidikan', 'Dinas Pendidikan', 'Jl. Pattimura No. 1, Jombang', 'Sekolah menengah atas favorit di Kabupaten Jombang.'),
('SMPN 1 Diwek', 'Pendidikan', 'Dinas Pendidikan', 'Jl. KH. Hasyim Asy\'ari, Diwek', 'Sekolah menengah pertama negeri di wilayah Diwek.'),
('Polres Jombang', 'Keamanan', 'Kepolisian RI', 'Jl. KH. Wahid Hasyim No. 62, Jombang', 'Markas Kepolisian Resor Jombang.'),
('Polsek Gudo', 'Keamanan', 'Kepolisian RI', 'Jl. Raya Gudo No. 5, Gudo', 'Kantor polisi sektor wilayah Gudo.'),
('Kantor Bupati Jombang', 'Pemerintahan', 'Sekretariat Daerah', 'Jl. KH. Wahid Hasyim No. 137, Jombang', 'Pusat administrasi pemerintahan Kabupaten Jombang.'),
('Dinas Kependudukan dan Catatan Sipil', 'Pemerintahan', 'Dispendukcapil', 'Jl. KH. Wahid Hasyim, Jombang', 'Layanan pengurusan KTP, KK, dan Akta Kelahiran.'),
('Alun-Alun Jombang', 'Publik', 'Dinas Lingkungan Hidup', 'Jl. Diponegoro, Jombang', 'Ruang terbuka hijau dan pusat kegiatan masyarakat.'),
('Stasiun Jombang', 'Transportasi', 'PT Kereta Api Indonesia', 'Jl. Jombang - Babat, Jombang', 'Stasiun kereta api utama yang menghubungkan berbagai kota.'),
('Terminal Mojoagung', 'Transportasi', 'Dinas Perhubungan', 'Jl. Raya Mojoagung, Mojoagung', 'Terminal bus tipe B yang melayani rute antar kota.'),
('GOR Merdeka Jombang', 'Olahraga', 'Dinas Kepemudaan dan Olahraga', 'Jl. Gus Dur, Jombang', 'Gedung olahraga untuk berbagai ajang pertandingan.'),
('Stadion Merdeka', 'Olahraga', 'Dinas Kepemudaan dan Olahraga', 'Jl. Gus Dur, Jombang', 'Stadion sepak bola utama di Kabupaten Jombang.');

-- Selesai insert data dummy fasilitas
SELECT 'Berhasil menambahkan 15 data dummy ke tabel fasilitas' AS Status;
