INSERT INTO tb_user (cpf, name, email, password, phone_number, address, role, active) VALUES
('12345678901', 'John Doe', 'johndoe@example.com', '$2a$10$wSBKah/fWqwNb8sSsQ0Rc.AQN8BEI73bEyNDEYqakpL1fdmachqhi', '(11)99999-9999', '123 Main St', 'ADMIN', TRUE),
('12345678902', 'David Black', 'davidb@example.com', '$2a$10$9iZon.mSuSBLtPXcwTB9juq2RHaCyn/ET.S1jScAcwLSSFhOuAF.S', '(12)99999-9999', '456 Side St', 'CLIENT', TRUE),
('12345678903', 'Alice Johnson', 'alicej@example.com', '$2a$10$sTM9CfT4tG4pnXD18PG/guRugYJsTB76uMuraIl8PtnnMJ49A54v.', '(13)99999-9999', '789 Elm St', 'CLIENT', TRUE);

INSERT INTO tb_product_stock (id, name, quantity, unit_price, category, active) VALUES
(1, 'Smartphone', 5, 299.99, 'Electronics', TRUE),
(2, 'T-Shirt', 20, 19.99, 'Clothing', TRUE),
(3, 'Chair', 10, 39.99, 'Furniture', TRUE),
(4, 'Creatine', 15, 24.99, 'Health', TRUE);

INSERT INTO tb_sale (id, moment, customer_cpf, status) VALUES
(1, '2024-07-28 10:30:00', '12345678901', 'DONE'),
(2, '2024-08-22 11:30:00', '12345678902', 'SHIPPED'),
(3, '2024-09-01 00:00:00', '12345678903', 'WAITING_PAYMENT');

INSERT INTO tb_sale_item (sale_id, product_id, quantity, price, discount) VALUES
(1, 1, 1, 299.99, 0.00),
(2, 2, 2, 19.99, 0.05),
(2, 4, 1, 24.99, 0.00),
(3, 3, 4, 39.99, 0.10);
