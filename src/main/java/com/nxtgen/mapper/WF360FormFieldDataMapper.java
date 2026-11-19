package com.WF360.mapper;

import com.WF360.model.WF360FormField;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class WF360FormFieldDataMapper implements RowMapper<WF360FormField> {

  @Override
  public WF360FormField mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    WF360FormField field = new WF360FormField();
    field.setId(resultSet.getLong("ID"));
    field.setFieldCode(resultSet.getString("FIELD_CODE"));
    field.setFieldName(resultSet.getString("FIELD_NAME"));
    field.setFieldType(resultSet.getString("FIELD_TYPE"));
    field.setIsMandatory(resultSet.getString("IS_MANDATORY"));
    return field;
  }
}
