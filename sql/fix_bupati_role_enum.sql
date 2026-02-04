-- 1. Modify the ENUM column to include 'bupati'
ALTER TABLE users 
MODIFY COLUMN role ENUM('admin', 'operator', 'guest', 'bupati') NOT NULL DEFAULT 'guest';

-- 2. Now we can safely update the user role
UPDATE users 
SET role = 'bupati', 
    nama_lengkap = 'Bapak Bupati' 
WHERE username = 'bupati';
