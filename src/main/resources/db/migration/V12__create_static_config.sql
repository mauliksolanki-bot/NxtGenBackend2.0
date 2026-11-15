CREATE TABLE NXTGEN_STATIC_CONFIG (
                                      ID NUMBER(19) PRIMARY KEY, CONF_CODE VARCHAR2(80 CHAR) NOT NULL UNIQUE, CONF_CATEGORY VARCHAR2(80 CHAR) NOT NULL,
                                      STATIC_JSON CLOB NOT NULL CONSTRAINT NXTGEN_STATIC_CONFIG_JSON_CK CHECK (STATIC_JSON IS JSON),
                                      IS_ACTIVE CHAR(1 CHAR) DEFAULT 'Y' NOT NULL CHECK (IS_ACTIVE IN ('Y','N')),
  CRE_BY VARCHAR2(120 CHAR) NOT NULL, CRE_DATE TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
  UPD_BY VARCHAR2(120 CHAR), UPD_DATE TIMESTAMP WITH TIME ZONE
);

INSERT INTO NXTGEN_STATIC_CONFIG (ID, CONF_CODE, CONF_CATEGORY, STATIC_JSON, IS_ACTIVE, CRE_BY) VALUES (
                                                                                                           1, 'EDIT_EDU_DETAILS', 'UPDATE_DETAILS',
                                                                                                           '{
                                                                                                             "title": "Education Details",
                                                                                                             "description": "Add or update your academic qualifications.",
                                                                                                             "addButtonLabel": "Add Education",
                                                                                                             "editButtonLabel": "Edit",
                                                                                                             "deleteButtonLabel": "Delete",
                                                                                                             "saveButtonLabel": "Save Changes",
                                                                                                             "cancelButtonLabel": "Cancel",
                                                                                                             "emptyStateText": "No education records added yet.",
                                                                                                             "columnsPerRow": 2,
                                                                                                             "fields": [
                                                                                                               { "code": "degree", "label": "Degree / Qualification", "type": "TEXT", "mandatory": true, "placeholder": "e.g. Bachelor of Technology", "maxLength": 100 },
                                                                                                               { "code": "institution", "label": "Institution", "type": "TEXT", "mandatory": true, "placeholder": "e.g. National Institute of Technology", "maxLength": 150 },
                                                                                                               { "code": "university", "label": "University / Board", "type": "TEXT", "mandatory": false, "placeholder": "e.g. NIT", "maxLength": 150 },
                                                                                                               { "code": "yearOfPassing", "label": "Year of Passing", "type": "TEXT", "mandatory": true, "placeholder": "e.g. 2014", "maxLength": 4, "pattern": "^(19|20)\\d{2}$" },
                                                                                                               { "code": "percentage", "label": "Percentage / Grade", "type": "TEXT", "mandatory": true, "placeholder": "e.g. 82% or A+", "maxLength": 10 }
                                                                                                             ]
                                                                                                           }',
                                                                                                           'Y', 'SYSTEM'
                                                                                                       );
