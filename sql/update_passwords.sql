-- SQL Script to update plain text passwords to SHA-256 hashes
-- SHA-256('admin123') = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9

-- Update admin password
UPDATE users 
SET password = '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9' 
WHERE username = 'admin' AND password = 'admin123';

-- Update bupati password (using admin123 as default)
UPDATE users 
SET password = '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9' 
WHERE username = 'bupati';

-- Update guest1 password (using guest123 hash if needed)
-- SHA-256('guest123') = 217215c32726ed16bf49da4d1d1f05a0d33e50664448377e8093d50f588c7401
UPDATE users 
SET password = '217215c32726ed16bf49da4d1d1f05a0d33e50664448377e8093d50f588c7401' 
WHERE username = 'guest1';
