-- Demo CEO account. The supplied password `demo1234` is stored as a BCrypt hash.
-- PAN and Aadhaar values are synthetic test data and must be replaced before production use.
INSERT INTO NXTGEN_APP_USER_MASTER (
  ID, EMP_ID, EMP_FNAME, EMP_LNAME, EMP_EMAIL, HASH_PASSWORD,
  PRIMARY_ROLE_ID, IS_ACTIVE, CRE_BY, PAN_NUMBER, AADHAR_NUMBER
)
SELECT
  '00000000-0000-0000-0000-000000000002',
  'WF00002',
  'Kartik',
  'Patel',
  'kartik.patel@wf.com',
  '$2a$10$t9Xtqrp4cDMySjYXWM7Rn.uVeyEzI.wmGLlQjb6.pm7/otclXnNO2',
  role.ID,
  'Y',
  'SYSTEM',
  'AAAAA0000A',
  '999988887777'
FROM NXTGEN_ROLE_MASTER role
WHERE role.ROLE_NAME = 'CEO'
  AND NOT EXISTS (
    SELECT 1
    FROM NXTGEN_APP_USER_MASTER user_account
    WHERE LOWER(user_account.EMP_EMAIL) = 'kartik.patel@wf.com'
  );
