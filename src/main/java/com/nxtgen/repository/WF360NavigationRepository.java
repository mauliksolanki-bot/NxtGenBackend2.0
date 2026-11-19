package com.WF360.repository;

import com.WF360.mapper.WF360NavigationDataMapper;
import com.WF360.model.WF360Navigation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360NavigationRepository {

    private static final String FIND_ACTIVE_BY_USER_ID = """
            SELECT NAVIGATION.ID, NAVIGATION.CODE, NAVIGATION.DISPLAY_NAME
            FROM WF_NAVIGATION_MASTER NAVIGATION
            INNER JOIN WF_NAV_MENU_ACCESS MENU_ACCESS
              ON MENU_ACCESS.NAV_ID = NAVIGATION.ID
            WHERE MENU_ACCESS.USR_ID = ?
              AND MENU_ACCESS.IS_ACTIVE = 'Y'
              AND NAVIGATION.IS_ACTIVE = 'Y'
            ORDER BY NAVIGATION.DISPLAY_ORDER
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final WF360NavigationDataMapper navigationDataMapper = new WF360NavigationDataMapper();

    public List<WF360Navigation> findActiveByUserId(String userId) {
        return jdbcTemplate.query(FIND_ACTIVE_BY_USER_ID, navigationDataMapper, userId);
    }
}
