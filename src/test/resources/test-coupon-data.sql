SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE coupon;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO coupon (id, code, rate, total_quantity, published_quantity, expired_at)
VALUES (1, 'COUPON1', 10, 30, 0, '2025-12-31 23:59:59');
INSERT INTO coupon (id, code, rate, total_quantity, published_quantity, expired_at)
VALUES (2, 'COUPON2', 5, 30, 0, '2025-12-31 23:59:59');
INSERT INTO coupon (id, code, rate, total_quantity, published_quantity, expired_at)
VALUES (3, 'COUPON3', 3, 30, 0, '2025-12-31 23:59:59');