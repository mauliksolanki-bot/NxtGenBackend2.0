-- Switches the Employee ID display/storage format from "NxtGen00001" to
-- "WF00001" (a short "WF" prefix + zero-padded number). Also adds a UNIQUE
-- constraint on EMP_ID so future employee IDs can never collide/repeat.
ALTER TABLE NXTGEN_APP_USER_MASTER
DROP CONSTRAINT NXTGEN_APP_USER_EMP_ID_CK;

UPDATE NXTGEN_APP_USER_MASTER
SET EMP_ID = 'WF00001',
    UPD_BY = 'SYSTEM',
    UPD_DATE = SYSTIMESTAMP
WHERE LOWER(EMP_EMAIL) = 'administrator@nxtgen.com';

ALTER TABLE NXTGEN_APP_USER_MASTER
    ADD CONSTRAINT NXTGEN_APP_USER_EMP_ID_CK
        CHECK (REGEXP_LIKE(EMP_ID, '^WF[0-9]{5,}$'));

ALTER TABLE NXTGEN_APP_USER_MASTER
    ADD CONSTRAINT NXTGEN_APP_USER_EMP_ID_UK UNIQUE (EMP_ID);
