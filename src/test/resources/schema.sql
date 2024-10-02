CREATE TABLE tb_product_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(8,2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    active BIT NOT NULL
);
