CREATE TABLE IF NOT EXISTS product (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price NUMERIC NOT NULL CHECK (price > 0),
  creation_datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  product_id INT NOT NULL,
  CONSTRAINT fk_product_category FOREIGN KEY (product_id) REFERENCES product (id)
);
