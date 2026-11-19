-- Bank Details follows the same database-driven form definition pattern as
-- General Information. The UI retrieves these fields through
-- GET /form/getformdetails?formCode=bank_details.
DECLARE
  v_form_id NXTGEN_FORM_MASTER.ID%TYPE;
  v_field_id NXTGEN_FORM_FIELDS_MASTER.ID%TYPE;

  PROCEDURE add_field(
    p_field_code NXTGEN_FORM_FIELDS_MASTER.FIELD_CODE%TYPE,
    p_field_name NXTGEN_FORM_FIELDS_MASTER.FIELD_NAME%TYPE,
    p_field_type NXTGEN_FORM_FIELDS_MASTER.FIELD_TYPE%TYPE,
    p_is_mandatory NXTGEN_FORM_FIELDS_MASTER.IS_MANDATORY%TYPE
  ) IS
    v_count NUMBER;
  BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM NXTGEN_FORM_FIELDS_MASTER
    WHERE FORM_ID = v_form_id AND FIELD_CODE = p_field_code;

    IF v_count = 0 THEN
      SELECT NVL(MAX(ID), 0) + 1
      INTO v_field_id
      FROM NXTGEN_FORM_FIELDS_MASTER;

      INSERT INTO NXTGEN_FORM_FIELDS_MASTER (
        ID, FORM_ID, FIELD_CODE, FIELD_NAME, FIELD_TYPE, IS_MANDATORY, IS_ACTIVE, CRE_BY
      ) VALUES (
        v_field_id, v_form_id, p_field_code, p_field_name, p_field_type, p_is_mandatory, 'Y', 'SYSTEM'
      );
    END IF;
  END;
BEGIN
  BEGIN
    SELECT ID
    INTO v_form_id
    FROM NXTGEN_FORM_MASTER
    WHERE FORM_CODE = 'bank_details';
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      SELECT NVL(MAX(ID), 0) + 1
      INTO v_form_id
      FROM NXTGEN_FORM_MASTER;

      INSERT INTO NXTGEN_FORM_MASTER (ID, FORM_CODE, FORM_NAME, IS_ACTIVE, CRE_BY)
      VALUES (v_form_id, 'bank_details', 'Bank Details', 'Y', 'SYSTEM');
  END;

  add_field('ACCOUNT_NUMBER', 'Account Number', 'TEXTBOX', 'Y');
  add_field('REPEAT_ACCOUNT_NUMBER', 'Repeat Account Number', 'TEXTBOX', 'Y');
  add_field('IFSC_CODE', 'IFSC Code', 'TEXTBOX', 'Y');
  add_field('BANK_NAME', 'Bank Name', 'DROPDOWN', 'Y');
  add_field('BRANCH_NAME', 'Branch Name', 'TEXTBOX', 'Y');
  add_field('ACCOUNT_TYPE', 'Account Type', 'DROPDOWN', 'Y');
END;
/
