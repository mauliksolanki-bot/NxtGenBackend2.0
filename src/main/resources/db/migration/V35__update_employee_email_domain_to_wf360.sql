-- Use the Workforce360 email domain for every existing workforce account.
UPDATE WF_APP_USER_MASTER user_account
SET EMP_EMAIL = REGEXP_REPLACE(LOWER(EMP_EMAIL), '@wf\.com$', '@wf360.com'),
    UPD_BY = 'SYSTEM',
    UPD_DATE = SYSTIMESTAMP
WHERE REGEXP_LIKE(LOWER(EMP_EMAIL), '@wf\.com$')
  AND NOT EXISTS (
    SELECT 1
    FROM WF_APP_USER_MASTER existing_account
    WHERE LOWER(existing_account.EMP_EMAIL) = REGEXP_REPLACE(LOWER(user_account.EMP_EMAIL), '@wf\.com$', '@wf360.com')
      AND existing_account.ID <> user_account.ID
  );
