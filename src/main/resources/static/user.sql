; create mysql user

CREATE USER 'atid'@'%' IDENTIFIED BY 'wjdgid0103';
GRANT ALL PRIVILEGES ON *.* TO 'atid'@'%';
FLUSH PRIVILEGES;

; list mysql user
SELECT User, Host FROM mysql.user;
