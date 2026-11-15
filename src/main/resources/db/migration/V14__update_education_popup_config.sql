-- The field list/labels/mandatory flags now come from GET /retriveform/education_details
-- (NXTGEN_FORM_MASTER/NXTGEN_FORM_FIELDS_MASTER). STATIC_JSON keeps only the popup's own
-- presentation config plus per-field UI hints (placeholder/maxLength/pattern) that the
-- generic form-fields table does not model, keyed by FIELD_CODE.
UPDATE NXTGEN_STATIC_CONFIG
SET STATIC_JSON = '{
    "title": "Education Details",
    "description": "Add or update your academic qualifications.",
    "addButtonLabel": "Add Education",
    "editButtonLabel": "Edit",
    "deleteButtonLabel": "Delete",
    "saveButtonLabel": "Save Changes",
    "cancelButtonLabel": "Cancel",
    "emptyStateText": "No education records added yet.",
    "columnsPerRow": 2,
    "formCode": "education_details",
    "fieldMeta": {
      "DEGREE": { "placeholder": "e.g. Bachelor of Technology", "maxLength": 100 },
      "INSTITUTION": { "placeholder": "e.g. National Institute of Technology", "maxLength": 150 },
      "UNIVERSITY": { "placeholder": "e.g. NIT", "maxLength": 150 },
      "YEAR_OF_PASSING": { "placeholder": "e.g. 2014", "maxLength": 4, "pattern": "^(19|20)\\d{2}$" },
      "PERCENTAGE": { "placeholder": "e.g. 82% or A+", "maxLength": 10 }
    }
  }',
    UPD_BY = 'SYSTEM',
    UPD_DATE = CURRENT_TIMESTAMP
WHERE CONF_CODE = 'EDIT_EDU_DETAILS';
