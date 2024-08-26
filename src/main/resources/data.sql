INSERT INTO tb_user (cpf, phone_number, password, name, address, email, role) VALUES
('12345678901', '(11)99999-9999', '$2a$10$k78P8KT643f8qVBZHslo7OsxjPPl/4hA9rio6metjRBcJCX4JXcaO', 'John Doe', '123 Main St', 'johndoe@example.com', 'ADMIN'),
('12345678902', '(12)99999-9999', '$2a$10$moQ5abmIlMOTK3Q3DgPyeeBTs5yeZ2gdr77XgEzJ4ufKSkt748URG', 'Jane Smith', '456 Side St', 'janesmith@example.com', 'CLIENT');

INSERT INTO tb_product_stock (inactive, quantity, unit_price, category, name) VALUES
(FALSE, 5, 299.99, 'Electronics', 'Smartphone'),
(FALSE, 10, 39.99, 'Furniture', 'Chair'),
(FALSE, 20, 19.99, 'Clothing', 'T-Shirt');

INSERT INTO tb_sale (instant, customer_cpf) VALUES
('2024-08-22 10:30:00', '12345678902'),
(CURRENT_TIMESTAMP, '12345678902');

INSERT INTO tb_sale_item (discount, price, quantity, product_id, sale_id) VALUES
(0.00, 299.99, 1, 1, 1),
(0.05, 19.99, 2, 3, 1),
(0.10, 39.99, 4, 2, 2),
(0.00, 19.99, 1, 3, 2);
