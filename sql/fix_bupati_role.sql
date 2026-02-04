-- Fix role for bupati user
UPDATE users 
SET role = 'bupati', 
    nama_lengkap = 'Bapak Bupati' 
WHERE username = 'bupati';
