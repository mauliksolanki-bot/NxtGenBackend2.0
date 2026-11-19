-- Human resources and recruitment designations with their organisation levels.
DECLARE
  v_role_id WF_ROLE_MASTER.ID%TYPE;

  PROCEDURE add_role(
    p_role_name WF_ROLE_MASTER.ROLE_NAME%TYPE,
    p_role_level WF_ROLE_MASTER.ROLE_LEVEL%TYPE
  ) IS
  BEGIN
    UPDATE WF_ROLE_MASTER
    SET ROLE_DESC = p_role_name,
        ROLE_LEVEL = p_role_level,
        IS_ACTIVE = 'Y',
        UPD_BY = 'SYSTEM',
        UPD_DATE = SYSTIMESTAMP
    WHERE ROLE_NAME = p_role_name;

    IF SQL%ROWCOUNT = 0 THEN
      SELECT NVL(MAX(ID), 0) + 1
      INTO v_role_id
      FROM WF_ROLE_MASTER;

      INSERT INTO WF_ROLE_MASTER (
        ID, ROLE_NAME, ROLE_DESC, ROLE_LEVEL, IS_ACTIVE, CRE_BY
      ) VALUES (
        v_role_id, p_role_name, p_role_name, p_role_level, 'Y', 'SYSTEM'
      );
    END IF;
  END;
BEGIN
  add_role('HR Associate', 'L11');
  add_role('Recruitment Coordinator', 'L11');
  add_role('HR Executive', 'L10');
  add_role('Talent Acquisition Specialist', 'L10');
  add_role('Senior HR Executive', 'L9');
  add_role('Senior Recruiter', 'L9');
  add_role('HR Business Partner', 'L8');
  add_role('Senior HR Consultant', 'L8');
  add_role('HR Lead', 'L7');
  add_role('Recruitment Lead', 'L7');
  add_role('HR Manager', 'L6');
  add_role('Talent Acquisition Manager', 'L6');
  add_role('Senior HR Manager', 'L5');
  add_role('HR Director', 'L4');
END;
/
