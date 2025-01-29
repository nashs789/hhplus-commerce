SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE product;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO product (id, name, price, image) VALUES (1, "상품 A", 1000, "이미지 1");
INSERT INTO product (id, name, price, image) VALUES (2, "상품 B", 1500, "이미지 2");
INSERT INTO product (id, name, price, image) VALUES (3, "상품 C", 3000, "이미지 3");
INSERT INTO product (id, name, price, image) VALUES (4, "상품 D", 4500, "이미지 4");
INSERT INTO product (id, name, price, image) VALUES (5, "상품 E", 7500, "이미지 5");