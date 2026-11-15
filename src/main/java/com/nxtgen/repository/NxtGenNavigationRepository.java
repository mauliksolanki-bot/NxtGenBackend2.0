package com.nxtgen.repository;

import com.nxtgen.mapper.NxtGenNavigationDataMapper;
import com.nxtgen.model.NxtGenNavigation;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NxtGenNavigationRepository {

    private static final String FIND_ACTIVE_BY_USER_ID = """
            SELECT NAVIGATION.ID, NAVIGATION.CODE, NAVIGATION.DISPLAY_NAME
            FROM NXTGEN_NAVIGATION_MASTER NAVIGATION
            INNER JOIN NXTGEN_NAV_MENU_ACCESS MENU_ACCESS
              ON MENU_ACCESS.NAV_ID = NAVIGATION.ID
            WHERE MENU_ACCESS.USR_ID = ?
              AND MENU_ACCESS.IS_ACTIVE = 'Y'
              AND NAVIGATION.IS_ACTIVE = 'Y'
            ORDER BY NAVIGATION.ID
            """;

    private final JdbcTemplate jdbcTemplate;
    private final NxtGenNavigationDataMapper navigationDataMapper = new NxtGenNavigationDataMapper();

    public NxtGenNavigationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<NxtGenNavigation> findActiveByUserId(String userId) {
        return jdbcTemplate.query(FIND_ACTIVE_BY_USER_ID, navigationDataMapper, userId);
    }
}
