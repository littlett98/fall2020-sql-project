insert into customers values('C001', 'trevor', 'password', 'home', '1234567890', 'email@gmail.com', null);

select count(customer_id) from customers;

insert into ingredients values ('I0001', 'Sugar', 1.99, 2000, 'Gram');
insert into ingredients values ('I0002', 'Milk', 1.99, 5000, 'Millilitre');
insert into ingredients values ('I0003', 'Cream', 2.29, 5000, 'Millilitre');
insert into ingredients values ('I0004', 'Brown Sugar', 2.49, 2000, 'Gram');
insert into ingredients values ('I0005', 'Coffee Beans', 14.99, 10000, 'Gram');
insert into ingredients values ('I0006', 'Instant Coffee', 10.99, 10000, 'Gram');
insert into ingredients values ('I0007', 'Prepackaged Blueberry Muffin', 1.99, 10, 'Muffin');
insert into ingredients values ('I0008', 'Bread', 4.99, 100, 'Slice');
insert into ingredients values ('I0009', 'Eggs', 7.99, 96, 'Egg');
insert into ingredients values ('I0010', 'Cheddar Cheese', 9.99, 110, 'Slice');
insert into ingredients values ('I0011', 'Butter', 9.99, 10000, 'Gram');
insert into ingredients values ('I0012', 'Vanilla Extract', 19.99, 1000, 'Millilitre');

-- Latte
insert into recipes values ('R0001', 'I0001', 20);
insert into recipes values ('R0001', 'I0002', 100);
insert into recipes values ('R0001', 'I0005', 50);
-- Vanilla Latte
insert into recipes values ('R0002', 'I0001', 20);
insert into recipes values ('R0002', 'I0002', 100);
insert into recipes values ('R0002', 'I0005', 50);
insert into recipes values ('R0002', 'I0012', 5);
-- Espresso
insert into recipes values ('R0003', 'I0006', 50);
-- Breakfast Sandwhich
insert into recipes values ('R0004', 'I0008', 2);
insert into recipes values ('R0004', 'I0009', 2);
insert into recipes values ('R0004', 'I0010', 2);
insert into recipes values ('R0004', 'I0011', 5);
-- Blueberry Muffin
insert into recipes values ('R0005', 'I0007', 1);
-- Regular Coffee
insert into recipes values ('R0006', 'I0006', 50);
insert into recipes values ('R0006', 'I0001', 10);
insert into recipes values ('R0006', 'I0002', 25);
-- Instant Coffee
insert into recipes values ('R0007', 'I0005', 50);
insert into recipes values ('R0007', 'I0001', 10);
insert into recipes values ('R0007', 'I0002', 25);

insert into products values('P0001', 'Latte', 4.99, 'R0001');
insert into products values('P0002', 'Vanilla Latte', 5.99, 'R0002');
insert into products values('P0003', 'Espresso', 2.99, 'R0003');
insert into products values('P0004', 'Breakfast Sandwhich', 4.99, 'R0004');
insert into products values('P0005', 'Blueberry Muffin', 1.99, 'R0005');
insert into products values('P0006', 'Regular Coffee', 2.49, 'R0006');
insert into products values('P0007', 'Instant Coffee', 1.99, 'R0007');