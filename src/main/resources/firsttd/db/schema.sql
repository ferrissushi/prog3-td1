create table if not exists product (
  id int primary key,
  name varchar(255) not null,
  price numeric not null check (price > 0),
  creation_datetime timestamp not null default current_timestamp
);

create table if not exists product_category (
  id int primary key,
  name varchar(255) not null,
  product_id int not null,
  constraint fk_product_category foreign key (product_id) references product (id)
);
