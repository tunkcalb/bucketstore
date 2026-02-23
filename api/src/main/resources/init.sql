-- 1. 상품 데이터 (products)
INSERT INTO products (id, product_code) VALUES (1, '11101JS505');
INSERT INTO products (id, product_code) VALUES (2, '82193SRK52');
INSERT INTO products (id, product_code) VALUES (3, 'M31E5AC014');

-- 2. 상품별 상세 옵션 및 재고 데이터 (product_items)
-- 11101JS505 (WH, BK / 95, 100, 105)
INSERT INTO product_items (product_id, color, size, stock) VALUES (1, 'WH', '95', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (1, 'WH', '100', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (1, 'WH', '105', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (1, 'BK', '95', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (1, 'BK', '100', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (1, 'BK', '105', 10);

-- 82193SRK52 (BK, GY / 1, 2, 3, 4)
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'BK', '1', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'BK', '2', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'BK', '3', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'BK', '4', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'GY', '1', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'GY', '2', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'GY', '3', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (2, 'GY', '4', 10);

-- M31E5AC014 (OR, IV / FREE)
INSERT INTO product_items (product_id, color, size, stock) VALUES (3, 'OR', 'FREE', 10);
INSERT INTO product_items (product_id, color, size, stock) VALUES (3, 'IV', 'FREE', 10);