package com.WF360.repository;

import com.WF360.mapper.WF360FormFieldDataMapper;
import com.WF360.model.WF360FormField;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360FormRepository {

    private static final String FIND_ACTIVE_FIELDS = """
            SELECT FIELD.ID, FIELD.FIELD_CODE, FIELD.FIELD_NAME, FIELD.FIELD_TYPE, FIELD.IS_MANDATORY
            FROM WF_FORM_MASTER FORM
            INNER JOIN WF_FORM_FIELDS_MASTER FIELD ON FIELD.FORM_ID = FORM.ID
            WHERE FORM.FORM_CODE = ? AND FORM.IS_ACTIVE = 'Y' AND FIELD.IS_ACTIVE = 'Y'
            ORDER BY FIELD.ID
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final WF360FormFieldDataMapper mapper = new WF360FormFieldDataMapper();

    public List<WF360FormField> findActiveFields(String formCode) {
        return jdbcTemplate.query(FIND_ACTIVE_FIELDS, mapper, formCode);
    }
}
