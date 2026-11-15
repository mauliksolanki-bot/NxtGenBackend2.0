package com.nxtgen.repository;

import com.nxtgen.mapper.NxtGenFormFieldDataMapper;
import com.nxtgen.model.NxtGenFormField;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NxtGenFormRepository {

    private static final String FIND_ACTIVE_FIELDS = """
            SELECT FIELD.ID, FIELD.FIELD_CODE, FIELD.FIELD_NAME, FIELD.FIELD_TYPE, FIELD.IS_MANDATORY
            FROM NXTGEN_FORM_MASTER FORM
            INNER JOIN NXTGEN_FORM_FIELDS_MASTER FIELD ON FIELD.FORM_ID = FORM.ID
            WHERE FORM.FORM_CODE = ? AND FORM.IS_ACTIVE = 'Y' AND FIELD.IS_ACTIVE = 'Y'
            ORDER BY FIELD.ID
            """;

    private final JdbcTemplate jdbcTemplate;
    private final NxtGenFormFieldDataMapper mapper = new NxtGenFormFieldDataMapper();

    public NxtGenFormRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<NxtGenFormField> findActiveFields(String formCode) {
        return jdbcTemplate.query(FIND_ACTIVE_FIELDS, mapper, formCode);
    }
}
