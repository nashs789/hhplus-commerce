SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE product_inventory;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO product_inventory (id, stock, product_id) VALUES (1, 10, 1);
INSERT INTO product_inventory (id, stock, product_id) VALUES (2, 10, 2);
INSERT INTO product_inventory (id, stock, product_id) VALUES (3, 10, 3);
INSERT INTO product_inventory (id, stock, product_id) VALUES (4, 10, 4);
INSERT INTO product_inventory (id, stock, product_id) VALUES (5, 10, 5);