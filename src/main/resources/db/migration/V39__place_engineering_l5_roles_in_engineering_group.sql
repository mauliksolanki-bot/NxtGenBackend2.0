UPDATE WF_ROLE_MASTER
SET ROLE_GROUP = 'Engineering & Technology',
    UPD_BY = 'SYSTEM',
    UPD_DATE = SYSTIMESTAMP
WHERE ROLE_NAME IN ('General Manager', 'Senior Manager')
  AND ROLE_LEVEL = 'L5';
