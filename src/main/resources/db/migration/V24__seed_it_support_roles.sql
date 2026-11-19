-- IT support designations and their organisation levels.
DECLARE
  v_role_id NXTGEN_ROLE_MASTER.ID%TYPE;

  PROCEDURE add_role(
    p_role_name NXTGEN_ROLE_MASTER.ROLE_NAME%TYPE,
    p_role_level NXTGEN_ROLE_MASTER.ROLE_LEVEL%TYPE
  ) IS
  BEGIN
    UPDATE NXTGEN_ROLE_MASTER
    SET ROLE_DESC = p_role_name,
        ROLE_LEVEL = p_role_level,
        IS_ACTIVE = 'Y',
        UPD_BY = 'SYSTEM',
        UPD_DATE = SYSTIMESTAMP
    WHERE ROLE_NAME = p_role_name;

    IF SQL%ROWCOUNT = 0 THEN
      SELECT NVL(MAX(ID), 0) + 1
      INTO v_role_id
      FROM NXTGEN_ROLE_MASTER;

      INSERT INTO NXTGEN_ROLE_MASTER (
        ID, ROLE_NAME, ROLE_DESC, ROLE_LEVEL, IS_ACTIVE, CRE_BY
      ) VALUES (
        v_role_id, p_role_name, p_role_name, p_role_level, 'Y', 'SYSTEM'
      );
    END IF;
  END;
BEGIN
  add_role('IT Support Engineer', 'L11');
  add_role('IT Support Analyst', 'L10');
  add_role('IT Senior Support Analyst', 'L9');
  add_role('IT Support Consultant', 'L8');
  add_role('IT Support Lead', 'L7');
  add_role('IT Support Manager', 'L6');
  add_role('IT Senior Support Manager', 'L5');
  add_role('IT Support Director', 'L4');
END;
/
