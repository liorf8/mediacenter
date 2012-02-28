CREATE USER client PASSWORD 'max';
CREATE ROLE client_role;
GRANT DELETE, INSERT, SELECT, UPDATE ON TABLE Users TO client_role;
GRANT client_role TO client;