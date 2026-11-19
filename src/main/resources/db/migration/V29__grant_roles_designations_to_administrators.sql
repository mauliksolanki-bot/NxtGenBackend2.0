-- Navigation is entirely data-driven. Each active Administrator receives a
-- concrete access row for the Roles & Designations menu.
INSERT INTO WF_NAV_MENU_ACCESS (ID, NAV_ID, USR_ID, IS_ACTIVE, CRE_BY)
SELECT
  (SELECT NVL(MAX(existing_access.ID), 0) FROM WF_NAV_MENU_ACCESS existing_access)
      + ROW_NUMBER() OVER (ORDER BY user_account.ID),
  navigation.ID,
  user_account.ID,
  'Y',
  'SYSTEM'
FROM WF_NAVIGATION_MASTER navigation
INNER JOIN WF_APP_USER_MASTER user_account ON user_account.IS_ACTIVE = 'Y'
INNER JOIN WF_ROLE_MASTER role_master ON role_master.ID = user_account.PRIMARY_ROLE_ID
WHERE navigation.CODE = 'ROLES_DESIGNATIONS'
  AND role_master.ROLE_NAME = 'Administrator'
  AND NOT EXISTS (
    SELECT 1
    FROM WF_NAV_MENU_ACCESS existing_access
    WHERE existing_access.NAV_ID = navigation.ID
      AND existing_access.USR_ID = user_account.ID
  );
