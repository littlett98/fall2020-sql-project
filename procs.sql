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

CREATE OR REPLACE PROCEDURE checkProductInStock(vProduct# IN VARCHAR2, vQuantity IN NUMBER)
AS
  vStock INGREDIENTS.STOCK%TYPE;
  vName INGREDIENTS.NAME%TYPE;
  vIngredientID INGREDIENTS.INGREDIENT_ID%TYPE;
  vIngredientQTY RECIPES.QUANTITY%TYPE;
  no_more_stock EXCEPTION;
  not_enough_stock EXCEPTION;
  
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
          RAISE no_more_stock;
        ELSE
          SELECT QUANTITY INTO vIngredientQTY FROM RECIPES
          JOIN PRODUCTS USING (RECIPE_ID)
          WHERE PRODUCTS.PRODUCT_ID LIKE vProduct# AND RECIPES.INGREDIENT_ID LIKE vIngredientID;
          IF (vIngredientQTY * vQuantity > vStock) THEN
            RAISE not_enough_stock;
          END IF;
        END IF;
        EXIT WHEN getIngredients%notfound;
      END;
    END LOOP;
  CLOSE getIngredients;
EXCEPTION
  WHEN no_more_stock THEN
    dbms_output.put_line('No More Stock of ' || vName);
  WHEN not_enough_stock THEN
    dbms_output.put_line('Not enough Stock of ' || vName);
END;

CREATE OR REPLACE TRIGGER updateIngredientTotal
    AFTER INSERT ON ORDERS_COFFEE
BEGIN
    calcTotalSpent;
END;

CREATE OR REPLACE PROCEDURE updateIngredientStock(vIngredientID IN VARCHAR2, vRemove IN NUMBER)
AS
  vOldStock INGREDIENTS.STOCK%TYPE;
  vNewStock INGREDIENTS.STOCK%TYPE;

BEGIN
  SELECT STOCK INTO VOLDSTOCK
  FROM INGREDIENTS
  WHERE INGREDIENT_ID LIKE VINGREDIENTID;
  
  vNewStock := vOldStock - vRemove;
  UPDATE INGREDIENTS SET STOCK = VNEWSTOCK
  WHERE INGREDIENT_ID LIKE VINGREDIENTID;
END;

CREATE OR REPLACE TRIGGER addToUserRef
  AFTER INSERT ON CUSTOMERS
  FOR EACH ROW
BEGIN
  INSERT INTO USERSREFERRAL VALUES(:NEW.username,0,0);
END;

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
