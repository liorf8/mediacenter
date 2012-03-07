CREATE USER client PASSWORD 'max';
CREATE ROLE client_role;
GRANT INSERT, SELECT, UPDATE ON TABLE Users TO client_role;
GRANT INSERT, DELETE, SELECT, UPDATE ON TABLE Media TO client_role;
GRANT client_role TO client;