INSERT INTO NXTGEN_NAV_MENU_ACCESS (ID, NAV_ID, USR_ID, IS_ACTIVE, CRE_BY)
SELECT
  (SELECT NVL(MAX(existing_access.ID), 0) FROM NXTGEN_NAV_MENU_ACCESS existing_access)
      + ROW_NUMBER() OVER (ORDER BY navigation.ID),
  navigation.ID,
  user_account.ID,
  'Y',
  'SYSTEM'
FROM NXTGEN_NAVIGATION_MASTER navigation
CROSS JOIN NXTGEN_APP_USER_MASTER user_account
WHERE navigation.CODE IN ('DASHBOARD', 'PROFILE')
  AND LOWER(user_account.EMP_EMAIL) = 'kartik.patel@wf.com'
  AND NOT EXISTS (
    SELECT 1
    FROM NXTGEN_NAV_MENU_ACCESS existing_access
    WHERE existing_access.NAV_ID = navigation.ID
      AND existing_access.USR_ID = user_account.ID
  );
