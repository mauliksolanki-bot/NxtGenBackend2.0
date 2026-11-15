-- Administrator password: demo1234 (BCrypt hash)
UPDATE NXTGEN_APP_USER_MASTER
SET HASH_PASSWORD = '$2a$10$t9Xtqrp4cDMySjYXWM7Rn.uVeyEzI.wmGLlQjb6.pm7/otclXnNO2',
    UPD_BY = 'SYSTEM',
    UPD_DATE = SYSTIMESTAMP
WHERE EMP_EMAIL = 'administrator@nxtgen.com';
