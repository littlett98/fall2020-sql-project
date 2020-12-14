CREATE OR REPLACE PROCEDURE addCustomer(vID IN VARCHAR2, vUser VARCHAR2, vAddress IN VARCHAR2, vPhone IN VARCHAR2, vEmail IN VARCHAR2, vREFID IN VARCHAR2)
AS
BEGIN
  INSERT INTO CUSTOMERS
  VALUES (vID, vUser, vAddress, vPhone, vEmail, vREFID);
  EXCEPTION
  WHEN DUP_VAL_ON_INDEX THEN
    dbms_output.put_line('Duplicate Values');
END;

CREATE OR REPLACE PROCEDURE newUser(vUser IN VARCHAR2, vSalt VARCHAR2, vHash IN RAW)
AS
BEGIN
  INSERT INTO USERPASS
  VALUES (vUser, vSalt, vHash);
  EXCEPTION
  WHEN DUP_VAL_ON_INDEX THEN
    dbms_output.put_line('Duplicate Values');
END;

CREATE OR REPLACE FUNCTION CALCULATECOST(vName IN VARCHAR2, vQTY IN NUMBER, vTotalCost OUT NUMBER) RETURN NUMBER
AS
  vCost PRODUCTS.RETAIL_PRICE%TYPE;
BEGIN
  SELECT RETAIL_PRICE INTO vCost
  FROM PRODUCTS
  WHERE PRODUCT_NAME LIKE vName;
  vTotalCost := vCost * vQTY;
  RETURN vTotalCost;
END;

CREATE OR REPLACE PROCEDURE NEWORDER(vOrder# IN VARCHAR2, vCust# IN VARCHAR2, vAddress IN VARCHAR2)
AS
BEGIN
  INSERT INTO ORDERS_COFFEE VALUES(vOrder#, vCust#, vAddress, sysdate, sysdate);
END;

CREATE OR REPLACE PROCEDURE ADDORDERITEM(vOrder# IN VARCHAR2, vProduct# IN VARCHAR2, vQTY IN NUMBER)
AS
BEGIN
  INSERT INTO ORDER_ITEMS VALUES(vOrder#, vProduct#, vQTY);
END;

CREATE OR REPLACE PROCEDURE UPDATEADDRESS(vCust# IN VARCHAR2, vNewAddress IN VARCHAR2)
AS
BEGIN
  UPDATE CUSTOMERS SET ADDRESS = vNewAddress WHERE CUSTOMER_ID LIKE vCust#;
END;

CREATE OR REPLACE PROCEDURE checkProductInStock(vProduct# IN VARCHAR2)
AS
  vStock INGREDIENTS.STOCK%TYPE;
  
  CURSOR getIngredients IS
    SELECT STOCK FROM INGREDIENTS
    JOIN RECIPES USING (INGREDIENT_ID)
    JOIN PRODUCTS USING (RECIPE_ID)
    WHERE PRODUCTS.PRODUCT_ID LIKE vProduct#;
BEGIN
  OPEN getIngredients;
    LOOP
      BEGIN
        FETCH getIngredients INTO vStock;
        IF (vStock <= 0) THEN
          
        END IF;
        EXIT WHEN getBooks%notfound;
      END;
    END LOOP;
  CLOSE getIngredients;
END;

/*SELECT name, STOCK FROM INGREDIENTS
    JOIN RECIPES USING (INGREDIENT_ID)
    JOIN PRODUCTS USING (RECIPE_ID)
    WHERE PRODUCTS.PRODUCT_ID LIKE 'P001';
    
DECLARE
  total number;
BEGIN
dbms_output.put_line(CALCULATECOST('Latte', 2, total));
END;

BEGIN
dbms_output.put_line('hello');
END;*/

