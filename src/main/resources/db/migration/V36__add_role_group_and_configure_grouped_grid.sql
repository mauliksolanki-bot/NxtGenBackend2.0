ALTER TABLE WF_ROLE_MASTER ADD (ROLE_GROUP VARCHAR2(120 CHAR));

UPDATE WF_ROLE_MASTER
SET ROLE_GROUP = CASE
  WHEN ROLE_NAME IN ('Finance Associate','Accounts Executive','Financial Analyst','Accountant','Senior Financial Analyst','Financial Controller','Finance Consultant','Finance Lead','Finance Manager','Accounts Manager','Senior Finance Manager','Finance Director','CFO') THEN 'Finance & Accounts'
  WHEN ROLE_NAME IN ('HR Associate','Recruitment Coordinator','HR Executive','Talent Acquisition Specialist','Senior HR Executive','Senior Recruiter','HR Business Partner','Senior HR Consultant','HR Lead','Recruitment Lead','HR Manager','Talent Acquisition Manager','Senior HR Manager','HR Director','CHRO') THEN 'Human Resources & Recruitment'
  WHEN ROLE_NAME LIKE 'IT %' THEN 'IT Support'
  WHEN ROLE_NAME IN ('CEO','Managing Director','CTO','CIO','COO','CISO','Assistant Vice President','Vice President','Senior Vice President','Executive Vice President','Senior Director','Director','Associate Director','General Manager','Senior Manager') THEN 'Executive & Leadership'
  WHEN ROLE_NAME = 'Administrator' THEN 'Platform Administration'
  ELSE 'Engineering & Technology'
END,
UPD_BY = 'SYSTEM',
UPD_DATE = SYSTIMESTAMP;

ALTER TABLE WF_ROLE_MASTER MODIFY (ROLE_GROUP NOT NULL);

UPDATE WF_DATAGRID_METADATA
SET METADATA_VALUE = '{"default":100,"options":[10,25,50,100]}'
WHERE DATAGRIDID = (SELECT ID FROM WF_DATAGRID WHERE GRID_NAME = 'ROLE_DESIGNATION')
  AND METADATA_KEY = 'pageSize';
