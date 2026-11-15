package com.nxtgen.repository;

import com.nxtgen.mapper.NxtGenStaticConfigDataMapper;
import com.nxtgen.model.NxtGenStaticConfig;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NxtGenStaticConfigRepository {

    private static final String FIND_ACTIVE_BY_CODE = """
            SELECT ID, CONF_CODE, CONF_CATEGORY, STATIC_JSON, IS_ACTIVE
            FROM NXTGEN_STATIC_CONFIG
            WHERE CONF_CODE = ? AND IS_ACTIVE = 'Y'
            """;

    private final JdbcTemplate jdbcTemplate;
    private final NxtGenStaticConfigDataMapper mapper = new NxtGenStaticConfigDataMapper();

    public NxtGenStaticConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<NxtGenStaticConfig> findActiveByCode(String confCode) {
        List<NxtGenStaticConfig> results = jdbcTemplate.query(FIND_ACTIVE_BY_CODE, mapper, confCode);
        return results.stream().findFirst();
    }
}
