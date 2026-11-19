package com.WF360.mapper;

import com.WF360.model.WF360RoleDesignation;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class WF360RoleDesignationDataMapper implements RowMapper<WF360RoleDesignation> {
  @Override
  public WF360RoleDesignation mapRow(ResultSet rs, int rowNum) throws SQLException {
    WF360RoleDesignation role = new WF360RoleDesignation();
    role.setId(rs.getLong("ID"));
    role.setRoleName(rs.getString("ROLE_NAME"));
    role.setRoleDesc(rs.getString("ROLE_DESC"));
    role.setRoleLevel(rs.getString("ROLE_LEVEL"));
    role.setRoleGroup(rs.getString("ROLE_GROUP"));
    role.setIsActive(rs.getString("IS_ACTIVE"));
    return role;
  }
}
