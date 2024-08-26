CREATE TABLE tb_product_stock (
    id BIGINT AUTO_INCREMENT,
    inactive BOOLEAN NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    name VARCHAR(80) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE tb_sale (
    id BIGINT AUTO_INCREMENT,
    instant TIMESTAMP(6) NOT NULL,
    customer_cpf VARCHAR(11) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tb_sale_item (
    discount DECIMAL(5,2) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    product_id BIGINT NOT NULL,
    sale_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, sale_id)
);

CREATE TABLE tb_user (
    cpf VARCHAR(11) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    password VARCHAR(200) NOT NULL,
    name VARCHAR(80) NOT NULL,
    address VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('ADMIN', 'CLIENT') NOT NULL,
    PRIMARY KEY (cpf)
);

ALTER TABLE tb_sale
    ADD CONSTRAINT fk_sale_customer
    FOREIGN KEY (customer_cpf)
    REFERENCES tb_user(cpf);

ALTER TABLE tb_sale_item
    ADD CONSTRAINT fk_sale_item_product
    FOREIGN KEY (product_id)
    REFERENCES tb_product_stock(id);

ALTER TABLE tb_sale_item
    ADD CONSTRAINT fk_sale_item_sale
    FOREIGN KEY (sale_id)
    REFERENCES tb_sale(id);
