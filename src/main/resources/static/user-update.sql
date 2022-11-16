
UPDATE user SET role = "ROLE_ADMIN" WHERE id = 2;
commit;
UPDATE user SET role = "ROLE_MANAGER" WHERE id = 3;
commit;