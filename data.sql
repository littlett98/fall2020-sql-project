insert into customers values('C001', 'trevor', 'password', 'home', '1234567890', 'email@gmail.com', null);

select count(customer_id) from customers;

insert into ingredients values ('I001', 'sugar', 1.99, 5, 'grams');

insert into recipes values ('R001', 'I001', 1);
insert into recipes values ('R002', 'I001', 1);

insert into products values('P001', 'Latte', 4.99, 'R001');
insert into products values('P002', 'Vanilla Latte', 5.99, 'R002');