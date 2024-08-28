INSERT INTO tb_user (cpf, name, email, password, phone_number, address, role, active) VALUES
('12345678901', 'John Doe', 'johndoe@example.com', '$2a$10$k78P8KT643f8qVBZHslo7OsxjPPl/4hA9rio6metjRBcJCX4JXcaO', '(11)99999-9999', '123 Main St', 'ADMIN', TRUE),
('12345678902', 'Jane Smith', 'janesmith@example.com', '$2a$10$moQ5abmIlMOTK3Q3DgPyeeBTs5yeZ2gdr77XgEzJ4ufKSkt748URG', '(12)99999-9999', '456 Side St', 'CLIENT', TRUE);

INSERT INTO tb_product_stock (name, quantity, unit_price, category, active) VALUES
('Smartphone', 5, 299.99, 'Electronics', TRUE),
('T-Shirt', 20, 19.99, 'Clothing', TRUE),
('Chair', 10, 39.99, 'Furniture', TRUE);

INSERT INTO tb_sale (instant, customer_cpf, status) VALUES
('2024-07-28 10:30:00', '12345678902', 'COMPLETED'),
('2024-08-22 11:30:00', '12345678902', 'PENDING');

INSERT INTO tb_sale_item (sale_id, product_id, quantity, price, discount) VALUES
(1, 1, 1, 299.99, 0.00),
(1, 2, 2, 19.99, 0.05),
(2, 2, 4, 19.99, 0.10),
(2, 3, 1, 39.99, 0.00);
