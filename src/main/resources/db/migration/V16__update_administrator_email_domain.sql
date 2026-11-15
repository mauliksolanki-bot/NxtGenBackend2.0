-- Updates the seeded administrator's email domain from nxtgen.com to wf.com
-- to match the new "WF" branding (Employee ID already switched to the
-- WF00001 format in V15).
UPDATE NXTGEN_APP_USER_MASTER
SET EMP_EMAIL = 'administrator@wf.com',
    UPD_BY = 'SYSTEM',
    UPD_DATE = SYSTIMESTAMP
WHERE LOWER(EMP_EMAIL) = 'administrator@nxtgen.com';
