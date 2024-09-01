CREATE TABLE tb_product_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(8,2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    active BIT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE tb_user (
    cpf VARCHAR(11) PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address VARCHAR(100) NOT NULL,
    role ENUM ('ADMIN', 'CLIENT') NOT NULL,
    active BIT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE tb_sale (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    moment DATETIME(6) NOT NULL,
    status ENUM ('CANCELLED', 'DONE', 'PAID', 'SHIPPED', 'WAITING_PAYMENT') NOT NULL,
    customer_cpf VARCHAR(11) NOT NULL,
    FOREIGN KEY (customer_cpf) REFERENCES tb_user(cpf)
) ENGINE=InnoDB;

CREATE TABLE tb_sale_item (
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(8,2) NOT NULL,
    discount DECIMAL(3,2) NOT NULL,
    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY (sale_id) REFERENCES tb_sale(id),
    FOREIGN KEY (product_id) REFERENCES tb_product_stock(id)
) ENGINE=InnoDB;

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
