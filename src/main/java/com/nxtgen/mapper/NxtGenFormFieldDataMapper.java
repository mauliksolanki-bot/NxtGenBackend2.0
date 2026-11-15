package com.nxtgen.mapper;

import com.nxtgen.model.NxtGenFormField;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class NxtGenFormFieldDataMapper implements RowMapper<NxtGenFormField> {

    @Override
    public NxtGenFormField mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        NxtGenFormField field = new NxtGenFormField();
        field.setId(resultSet.getLong("ID"));
        field.setFieldCode(resultSet.getString("FIELD_CODE"));
        field.setFieldName(resultSet.getString("FIELD_NAME"));
        field.setFieldType(resultSet.getString("FIELD_TYPE"));
        field.setIsMandatory(resultSet.getString("IS_MANDATORY"));
        return field;
    }
}
