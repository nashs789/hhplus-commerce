SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE cart_product;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO cart_product (id, product_id, quantity, cart_id) VALUES (1, 1, 5, 1);
INSERT INTO cart_product (id, product_id, quantity, cart_id) VALUES (2, 2, 10, 1);