SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_detail;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO order_detail (id, order_id, product_id, quantity) VALUES (1, 1, 1, 5);
INSERT INTO order_detail (id, order_id, product_id, quantity) VALUES (2, 1, 2, 10);