INSERT INTO WF_STATIC_CONFIG (ID, CONF_CODE, CONF_CATEGORY, STATIC_JSON, IS_ACTIVE, CRE_BY)
SELECT NVL(MAX(ID), 0) + 1,
       'DELETE_CONFIRM',
       'CONFIRMATION_POPUP',
       '{"title":"Delete Role & Designation","description":"Are you sure you want to delete?","yesButtonLabel":"Yes","noButtonLabel":"No"}',
       'Y',
       'SYSTEM'
FROM WF_STATIC_CONFIG
WHERE NOT EXISTS (
  SELECT 1 FROM WF_STATIC_CONFIG WHERE CONF_CODE = 'DELETE_CONFIRM'
);
