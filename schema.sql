-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2020-12-04 18:00:55.176

-- tables
-- Table: Customers
CREATE TABLE Customers (
    customer_id varchar2(10)  NOT NULL,
    username varchar2(15)  NOT NULL,
    password varchar2(128)  NOT NULL,
    address varchar2(255)  NOT NULL,
    phone varchar2(10)  NULL,
    email varchar2(255)  NOT NULL,
    referral_id varchar2(10)  NULL,
    CONSTRAINT Customers_pk PRIMARY KEY (customer_id)
) ;

-- Table: Ingredients
CREATE TABLE Ingredients (
    ingredient_id varchar2(10)  NOT NULL,
    name varchar2(255)  NOT NULL,
    restock_price number(6,2)  NOT NULL,
    stock number(4)  NOT NULL,
    units varchar2(25)  NOT NULL,
    CONSTRAINT Ingredients_pk PRIMARY KEY (ingredient_id)
) ;

-- Table: Order_Items
CREATE TABLE Order_Items (
    order_id varchar2(10)  NOT NULL,
    product_id varchar2(10)  NOT NULL,
    quantity number(3)  NOT NULL
) ;

-- Table: Orders
CREATE TABLE Orders (
    order_id varchar2(10)  NOT NULL,
    customer_id varchar2(10)  NOT NULL,
    address varchar2(255)  NOT NULL,
    order_placed date  NOT NULL,
    order_complete date  NOT NULL,
    CONSTRAINT Orders_pk PRIMARY KEY (order_id)
) ;

-- Table: Products
CREATE TABLE Products (
    product_id varchar2(10)  NOT NULL,
    product_name varchar2(255)  NOT NULL,
    retail_price number(6,2)  NOT NULL,
    recipe_id varchar2(10)  NOT NULL,
    CONSTRAINT Products_pk PRIMARY KEY (product_id)
) ;

-- Table: Recipes
CREATE TABLE Recipes (
    recipe_id varchar2(10)  NOT NULL,
    ingredient_id varchar2(10)  NOT NULL,
    quantity number(6,2)  NOT NULL,
    CONSTRAINT Recipes_pk PRIMARY KEY (recipe_id)
) ;

-- foreign keys
-- Reference: Customers_Customers (table: Customers)
ALTER TABLE Customers ADD CONSTRAINT Customers_Customers
    FOREIGN KEY (referral_id)
    REFERENCES Customers (customer_id);

-- Reference: Order_Items_Orders (table: Order_Items)
ALTER TABLE Order_Items ADD CONSTRAINT Order_Items_Orders
    FOREIGN KEY (order_id)
    REFERENCES Orders (order_id);

-- Reference: Order_Items_Products (table: Order_Items)
ALTER TABLE Order_Items ADD CONSTRAINT Order_Items_Products
    FOREIGN KEY (product_id)
    REFERENCES Products (product_id);

-- Reference: Orders_Customers (table: Orders)
ALTER TABLE Orders ADD CONSTRAINT Orders_Customers
    FOREIGN KEY (customer_id)
    REFERENCES Customers (customer_id);

-- Reference: Recipes_Ingredients (table: Recipes)
ALTER TABLE Recipes ADD CONSTRAINT Recipes_Ingredients
    FOREIGN KEY (ingredient_id)
    REFERENCES Ingredients (ingredient_id);

-- Reference: Recipes_Products (table: Products)
ALTER TABLE Products ADD CONSTRAINT Recipes_Products
    FOREIGN KEY (recipe_id)
    REFERENCES Recipes (recipe_id);

-- End of file.

