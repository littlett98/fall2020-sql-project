-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2020-12-04 18:00:55.176

-- foreign keys
ALTER TABLE Customers
    DROP CONSTRAINT Customers_Customers;

ALTER TABLE Order_Items
    DROP CONSTRAINT Order_Items_Orders;

ALTER TABLE Order_Items
    DROP CONSTRAINT Order_Items_Products;

ALTER TABLE Orders
    DROP CONSTRAINT Orders_Customers;

ALTER TABLE Recipes
    DROP CONSTRAINT Recipes_Ingredients;

ALTER TABLE Products
    DROP CONSTRAINT Recipes_Products;

-- tables
DROP TABLE Customers;

DROP TABLE Ingredients;

DROP TABLE Order_Items;

DROP TABLE Orders;

DROP TABLE Products;

DROP TABLE Recipes;

-- End of file.

