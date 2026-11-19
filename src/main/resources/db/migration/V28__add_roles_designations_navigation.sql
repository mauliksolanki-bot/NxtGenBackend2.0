-- This menu is granted by role in WF360NavigationRepository, rather than by a
-- per-user access row, so every current and future Administrator can use it.
INSERT INTO WF_NAVIGATION_MASTER (ID, CODE, DISPLAY_NAME, IS_ACTIVE, CRE_BY)
SELECT NVL(MAX(ID), 0) + 1, 'ROLES_DESIGNATIONS', 'Roles & Designations', 'Y', 'SYSTEM'
FROM WF_NAVIGATION_MASTER
WHERE NOT EXISTS (
  SELECT 1
  FROM WF_NAVIGATION_MASTER
  WHERE CODE = 'ROLES_DESIGNATIONS'
);
