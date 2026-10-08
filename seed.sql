-- ============================================================
--  SMART CANTEEN - sample data (run AFTER schema.sql)
--  Run:   mysql -u root -p smart_canteen < seed.sql
--
--  DEMO LOGINS (for the college project only):
--     Student : STU001  / student123      Student : STU002 / student123
--     Admin   : ADMIN001 / admin123
--  The passwords below are stored as salted PBKDF2 hashes, not as plain text.
--  INSERT IGNORE makes it safe to run this file twice.
-- ============================================================

USE smart_canteen;

INSERT IGNORE INTO users (name, college_id, email, password, role) VALUES
('Rahul Sharma',   'STU001',   'rahul.sharma@college.edu',  '65536:ugzLYM+fb1Tzl5+k1r5GUg==:PVYbc6Ku6ALjU39bujeSD6ETM0AfaamDT+txD/7WslM=', 'STUDENT'),
('Priya Patel',    'STU002',   'priya.patel@college.edu',   '65536:1yN+PiaZX2mGRXIrX2UnVw==:CCKANWrgL+bxG8oYoGZEo6ifOxiLBD2veud/ucJTCmU=', 'STUDENT'),
('Canteen Admin',  'ADMIN001', 'canteen.admin@college.edu', '65536:FM+wB4qwoAuZAYMS91smTw==:fgNK9xNvDKv0wiR06E3bvDgne325jHUoeMv9vZ7XkWY=', 'ADMIN');

INSERT IGNORE INTO food_items (name, description, category, price, image, available) VALUES
('Idli',             '2 soft steamed idlis with sambar and coconut chutney',    'Breakfast', 40.00, '', TRUE),
('Masala Dosa',      'Crispy dosa filled with spiced potato, served with chutney', 'Breakfast', 60.00, '', TRUE),
('Poha',             'Light flattened rice with peanuts, onion and lemon',      'Breakfast', 30.00, '', TRUE),
('Veg Sandwich',     'Grilled sandwich with fresh vegetables and green chutney', 'Snacks',    50.00, '', TRUE),
('Samosa',           'Crispy pastry stuffed with spiced potato and peas',       'Snacks',    20.00, '', TRUE),
('Vada Pav',         'Mumbai-style potato fritter in a soft pav bun',           'Snacks',    25.00, '', TRUE),
('Veg Fried Rice',   'Rice stir-fried with mixed vegetables and soy sauce',     'Meals',     80.00, '', TRUE),
('Veg Thali',        'Roti, rice, dal, one sabzi, salad and papad',             'Meals',     90.00, '', TRUE),
('Chole Bhature',    'Spicy chickpea curry with two fluffy bhature (sold out today)', 'Meals', 70.00, '', FALSE),
('Tea',              'Hot masala chai',                                         'Beverages', 15.00, '', TRUE),
('Coffee',           'Hot milk coffee',                                         'Beverages', 20.00, '', TRUE),
('Cold Drink',       'Chilled soft drink (300 ml)',                             'Beverages', 30.00, '', TRUE);
