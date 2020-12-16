-- This Procedure adds a new Customer to the database
CREATE OR REPLACE PROCEDURE addCustomer(vID IN VARCHAR2, vUser VARCHAR2, vAddress IN VARCHAR2, vPhone IN VARCHAR2, vEmail IN VARCHAR2, vREFID IN VARCHAR2)
AS
BEGIN
  INSERT INTO CUSTOMERS
  VALUES (vID, vUser, vAddress, vPhone, vEmail, vREFID);
  EXCEPTION
  WHEN DUP_VAL_ON_INDEX THEN
    dbms_output.put_line('Duplicate Values');
END;

-- This Procedure adds a new users password to the database
CREATE OR REPLACE PROCEDURE newUser(vUser IN VARCHAR2, vSalt VARCHAR2, vHash IN RAW)
AS
BEGIN
  INSERT INTO USERPASS
  VALUES (vUser, vSalt, vHash);
  EXCEPTION
  WHEN DUP_VAL_ON_INDEX THEN
    dbms_output.put_line('Duplicate Values');
END;

-- this function calculates the total cost of a product times the quantity requested, and returns those values.
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

-- This Procedure adds a new order to the database
CREATE OR REPLACE PROCEDURE NEWORDER(vOrder# IN VARCHAR2, vCust# IN VARCHAR2, vAddress IN VARCHAR2)
AS
BEGIN
  INSERT INTO ORDERS_COFFEE VALUES(vOrder#, vCust#, vAddress, sysdate, sysdate);
END;

-- This procedure adds an orderitem to the database (which is called after completing an order in JDBC)
CREATE OR REPLACE PROCEDURE ADDORDERITEM(vOrder# IN VARCHAR2, vProduct# IN VARCHAR2, vQTY IN NUMBER)
AS
BEGIN
  INSERT INTO ORDER_ITEMS VALUES(vOrder#, vProduct#, vQTY);
END;

-- This procedure updates a users address
CREATE OR REPLACE PROCEDURE UPDATEADDRESS(vCust# IN VARCHAR2, vNewAddress IN VARCHAR2)
AS
BEGIN
  UPDATE CUSTOMERS SET ADDRESS = vNewAddress WHERE CUSTOMER_ID LIKE vCust#;
END;

-- This procedure checks if there is enough stock of the ingredients to make the products desired by the user
CREATE OR REPLACE PROCEDURE checkProductInStock(vProduct# IN VARCHAR2, vQuantity IN NUMBER)
AS
  vStock INGREDIENTS.STOCK%TYPE;
  vName INGREDIENTS.NAME%TYPE;
  vIngredientID INGREDIENTS.INGREDIENT_ID%TYPE;
  vIngredientQTY RECIPES.QUANTITY%TYPE;
  no_more_stock EXCEPTION;
  PRAGMA EXCEPTION_INIT (no_more_stock, -20001);
  not_enough_stock EXCEPTION;
  PRAGMA EXCEPTION_INIT (not_enough_stock, -20002);
  
  CURSOR getIngredients IS
    SELECT STOCK, NAME, INGREDIENT_ID FROM INGREDIENTS
    JOIN RECIPES USING (INGREDIENT_ID)
    JOIN PRODUCTS USING (RECIPE_ID)
    WHERE PRODUCTS.PRODUCT_ID LIKE vProduct#;
BEGIN
  OPEN getIngredients;
    LOOP
      BEGIN
        FETCH getIngredients INTO vStock, vName, vIngredientID;
        IF (vStock <= 0) THEN
          raise_application_error(-20001,'No more in stock');
        ELSE
          SELECT QUANTITY INTO vIngredientQTY FROM RECIPES
          JOIN PRODUCTS USING (RECIPE_ID)
          WHERE PRODUCTS.PRODUCT_ID LIKE vProduct# AND RECIPES.INGREDIENT_ID LIKE vIngredientID;
          IF (vIngredientQTY * vQuantity > vStock) THEN
            raise_application_error(-20002,'Not Enough Ingredients in stock to make the quantity you requested');
          END IF;
        END IF;
        EXIT WHEN getIngredients%notfound;
      END;
    END LOOP;
  CLOSE getIngredients;
END;

--This trigger updates the total amount of ingredients after an order is made
CREATE OR REPLACE TRIGGER updateIngredientTotal
    AFTER INSERT ON ORDER_ITEMS
    FOR EACH ROW
BEGIN
    updateIngredientStock2(:NEW.PRODUCT_ID, :NEW.QUANTITY);
END;

-- This Procedure Updates the amount of an ingredient the store has in stock
CREATE OR REPLACE PROCEDURE updateIngredientStock2(vProdID IN VARCHAR2, vQTYofProduct IN NUMBER)
AS
  vOldStock INGREDIENTS.STOCK%TYPE;
  vNewStock INGREDIENTS.STOCK%TYPE;
  vIngredID INGREDIENTS.INGREDIENT_ID%TYPE;
  vQTYinRecipe RECIPES.QUANTITY%TYPE;
  
  CURSOR ingredientsInRecipe IS
    SELECT INGREDIENT_ID, QUANTITY FROM RECIPES
    JOIN PRODUCTS USING (RECIPE_ID)
    WHERE PRODUCTS.PRODUCT_ID LIKE vProdID;

BEGIN
  OPEN ingredientsInRecipe;
    LOOP
      BEGIN
        FETCH ingredientsInRecipe INTO vIngredID, vQTYinRecipe;
          
          SELECT STOCK INTO VOLDSTOCK
          FROM INGREDIENTS
          WHERE INGREDIENT_ID LIKE VINGREDID;
          
        vNewStock := vOldStock - (vQTYinRecipe * vQTYofProduct);
        UPDATE INGREDIENTS SET STOCK = vNewStock
        WHERE INGREDIENT_ID LIKE vIngredID;
        EXIT WHEN ingredientsInRecipe%notfound;
      END;
    END LOOP;
  CLOSE ingredientsInRecipe;
END;

-- adds a row in the usersreferral tble whenever a new customer is added to the database
CREATE OR REPLACE TRIGGER addToUserRef
  AFTER INSERT ON CUSTOMERS
  FOR EACH ROW
BEGIN
  INSERT INTO USERSREFERRAL VALUES(:NEW.username,0,0);
END;

-- This procedure adds a credit to a user whenever they referred someone to the coffee shop
CREATE OR REPLACE PROCEDURE addNewUserRef(vRefID IN CUSTOMERS.REFERRAL_ID%TYPE)
AS
  vUsername CUSTOMERS.USERNAME%TYPE;
  vOldCredits USERSREFERRAL.REFERRAL_CREDITS%TYPE;
  vNewCredits USERSREFERRAL.REFERRAL_CREDITS%TYPE;
  vOldCreditsRemaining USERSREFERRAL.REFERRAL_CREDITS_REMAINING%TYPE;
  vNewCreditsRemaining USERSREFERRAL.REFERRAL_CREDITS_REMAINING%TYPE;
BEGIN
  SELECT username into vUsername 
  from customers
  where customer_id LIKE vRefID;
  
  SELECT referral_credits, referral_credits_remaining INTO vOldCredits, vOldCreditsRemaining
  FROM USERSREFERRAL
  WHERE username LIKE vUsername;
  
  IF (vOldCredits < 3) THEN
    vNewCredits := vOldCredits + 1;
    vNewCreditsRemaining := vOldCreditsRemaining + 1;
    UPDATE USERSREFERRAL SET REFERRAL_CREDITS = vNewCredits, REFERRAL_CREDITS_REMAINING = vNewCreditsRemaining
    WHERE USERNAME LIKE vUsername;
  END IF;
END;

-- This procedure removes a credit whenever the user makes an order ($5 credit off their order)
CREATE OR REPLACE PROCEDURE updateCreditsRemaining(vCustID IN CUSTOMERS.CUSTOMER_ID%TYPE)
AS
  vUsername CUSTOMERS.USERNAME%TYPE;
  vOldCreditsRemaining USERSREFERRAL.REFERRAL_CREDITS_REMAINING%TYPE;
  vNewCreditsRemaining USERSREFERRAL.REFERRAL_CREDITS_REMAINING%TYPE;
BEGIN
  SELECT username into vUsername 
  from customers
  where customer_id LIKE vCustID;

  SELECT referral_credits_remaining INTO vOldCreditsRemaining
  FROM USERSREFERRAL
  WHERE username LIKE vUsername;
  
  IF (vOldCreditsRemaining > 0) THEN
    vNewCreditsRemaining := vOldCreditsRemaining - 1;
    UPDATE USERSREFERRAL SET REFERRAL_CREDITS_REMAINING = vNewCreditsRemaining
    WHERE USERNAME LIKE vUsername;
  END IF;
END;