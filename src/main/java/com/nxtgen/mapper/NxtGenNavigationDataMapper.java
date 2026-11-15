package com.nxtgen.mapper;

import com.nxtgen.model.NxtGenNavigation;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class NxtGenNavigationDataMapper implements RowMapper<NxtGenNavigation> {

    @Override
    public NxtGenNavigation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        NxtGenNavigation navigation = new NxtGenNavigation();
        navigation.setId(resultSet.getLong("ID"));
        navigation.setCode(resultSet.getString("CODE"));
        navigation.setDisplayName(resultSet.getString("DISPLAY_NAME"));
        return navigation;
    }
}
