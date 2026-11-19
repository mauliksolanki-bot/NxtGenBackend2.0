package com.WF360.mapper;

import com.WF360.model.WF360Navigation;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class WF360NavigationDataMapper implements RowMapper<WF360Navigation> {

  @Override
  public WF360Navigation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    WF360Navigation navigation = new WF360Navigation();
    navigation.setId(resultSet.getLong("ID"));
    navigation.setCode(resultSet.getString("CODE"));
    navigation.setDisplayName(resultSet.getString("DISPLAY_NAME"));
    navigation.setIsParent(resultSet.getString("IS_PARENT"));
    long parentNavigationId = resultSet.getLong("PARENT_NAV_ID");
    navigation.setParentNavigationId(resultSet.wasNull() ? null : parentNavigationId);
    return navigation;
  }
}
