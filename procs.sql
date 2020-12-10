CREATE OR REPLACE PROCEDURE addCustomer(vID IN VARCHAR2, vUser VARCHAR2, vAddress IN VARCHAR2, vPhone IN VARCHAR2, vEmail IN VARCHAR2, vREFID IN VARCHAR2)
AS
BEGIN
  INSERT INTO CUSTOMERS
  VALUES (vID, vUser, vPassword, vAddress, vPhone, vEmail, vREFID);
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

