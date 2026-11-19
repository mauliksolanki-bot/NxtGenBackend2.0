package com.WF360.repository;

import com.WF360.mapper.WF360StaticConfigDataMapper;
import com.WF360.model.WF360StaticConfig;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360StaticConfigRepository {

  private static final String FIND_ACTIVE_BY_CODE =
      """
            SELECT ID, CONF_CODE, CONF_CATEGORY, STATIC_JSON, IS_ACTIVE
            FROM WF_STATIC_CONFIG
            WHERE CONF_CODE = ? AND IS_ACTIVE = 'Y'
            """;

  @Autowired private JdbcTemplate jdbcTemplate;
  private final WF360StaticConfigDataMapper mapper = new WF360StaticConfigDataMapper();

  public Optional<WF360StaticConfig> findActiveByCode(String confCode) {
    List<WF360StaticConfig> results = jdbcTemplate.query(FIND_ACTIVE_BY_CODE, mapper, confCode);
    return results.stream().findFirst();
  }
}
