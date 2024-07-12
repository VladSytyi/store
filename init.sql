drop schema if exists store cascade;
create schema if not exists store;

drop table if exists store.items;

create table store.items (
  id bigint primary key,
  name varchar(100) not null,
  brand varchar(100),
  description varchar(255),
  category varchar(100) not null,
  price decimal(10, 2) not null
);

insert into store.items (name, brand, description, category, price) values ('Laptop', 'Apple', 'Apple Macbook Pro', 'Electronics', 1999.99);
insert into store.items (name, description, category, price) values ('Book', 'The Hitchhiker''s Guide to the Galaxy', 'Books', 9.99);
insert into store.items (name, brand, description, category, price) values ('T-shirt', 'Vans' , 'Cotton T-shirt', 'Apparel', 14.99);
insert into store.items (name, brand, description, category, price) values ('Shoes', 'Adidas',  'Running Shoes', 'Apparel', 59.99);
insert into store.items (name, brand, description, category, price) values ('Headphones', 'Sony' , 'Wireless Bluetooth Headphones', 'Electronics', 99.99);
insert into store.items (name, brand, description, category, price) values ('Sunglasses', 'Ray Ban', 'Polarized Sunglasses', 'Apparel', 29.99);
insert into store.items (name, brand, description, category, price) values ('Backpack', 'Columbia', 'Waterproof Backpack', 'Apparel', 39.99);
insert into store.items (name, brand, description, category, price) values ('Smartwatch', 'Garmin', 'Fitness Tracker Smartwatch', 'Electronics', 49.99);
insert into store.items (name, brand, description, category, price) values ('Sneakers', 'New Balance', 'Casual Sneakers', 'Apparel', 49.99);
insert into store.items (name, brand, description, category, price) values ('Tablet', 'Samsung', 'Android Tablet', 'Electronics', 149.99);
insert into store.items (name, brand, description, category, price) values ('Screen', 'Samsung', '32 inches', 'Electronics', 149.99);