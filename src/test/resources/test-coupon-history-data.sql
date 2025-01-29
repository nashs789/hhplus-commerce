SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE coupon_history;
SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO coupon_history (coupon_id, member_id, status)
VALUES (1, 1, "NOT_USED");