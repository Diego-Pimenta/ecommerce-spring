CREATE TABLE tb_product_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE tb_user (
    cpf VARCHAR(11) PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'CLIENT') NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE tb_sale (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    instant TIMESTAMP(6) NOT NULL,
    status ENUM('CANCELED', 'COMPLETED', 'DENIED', 'PAID', 'PENDING') NOT NULL,
    customer_cpf VARCHAR(11) NOT NULL,
    FOREIGN KEY (customer_cpf) REFERENCES tb_user(cpf)
);

CREATE TABLE tb_sale_item (
    sale_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(5, 2) NOT NULL,
    PRIMARY KEY (sale_id, product_id),
    FOREIGN KEY (sale_id) REFERENCES tb_sale(id),
    FOREIGN KEY (product_id) REFERENCES tb_product_stock(id)
);
