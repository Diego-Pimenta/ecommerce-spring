CREATE TABLE tb_product_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(8,2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    active BIT NOT NULL,
) ENGINE=InnoDB;

CREATE TABLE tb_sale (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    moment DATETIME(6) NOT NULL,
    status ENUM ('CANCELLED', 'DONE', 'PAID', 'SHIPPED', 'WAITING_PAYMENT') NOT NULL,
    customer_cpf VARCHAR(11) NOT NULL,
    FOREIGN KEY (customer_cpf) REFERENCES tb_user(cpf)
) ENGINE=InnoDB;

CREATE TABLE tb_sale_item (
    sale_id BIGINT PRIMARY KEY,
    product_id BIGINT PRIMARY KEY,
    quantity INTEGER NOT NULL,
    price DECIMAL(8,2) NOT NULL,
    discount DECIMAL(3,2) NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES tb_sale(id),
    FOREIGN KEY (product_id) REFERENCES tb_product_stock(id)
) ENGINE=InnoDB;

CREATE TABLE tb_user (
    cpf VARCHAR(11) PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address VARCHAR(100) NOT NULL,
    role ENUM ('ADMIN', 'CLIENT') NOT NULL,
    active BIT NOT NULL,
) ENGINE=InnoDB;
