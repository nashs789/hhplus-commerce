SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE orders;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO orders (id, original_price, order_status, order_ship_status, member_id) VALUES (1, 20000, "NOT_PAYED", "NOT_DEPARTURE", 1);