package com.WF360.mapper;

import com.WF360.model.WF360StaticConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/** Maps a WF360_STATIC_CONFIG database row to the WF360StaticConfig domain model. */
public class WF360StaticConfigDataMapper implements RowMapper<WF360StaticConfig> {

  private static final Logger LOGGER = LoggerFactory.getLogger(WF360StaticConfigDataMapper.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public WF360StaticConfig mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    WF360StaticConfig config = new WF360StaticConfig();
    config.setId(resultSet.getLong("ID"));
    config.setConfCode(resultSet.getString("CONF_CODE"));
    config.setConfCategory(resultSet.getString("CONF_CATEGORY"));
    config.setStaticJson(readStaticJson(resultSet.getString("STATIC_JSON")));
    config.setIsActive(resultSet.getString("IS_ACTIVE"));
    return config;
  }

  /**
   * STATIC_JSON stores arbitrary, feature-specific UI configuration as JSON text (enforced valid by
   * a database check constraint). This deserializes it into a {@link JsonNode} so API consumers
   * receive structured data instead of a raw JSON string; malformed content degrades to an empty
   * object rather than failing the whole read.
   */
  private static JsonNode readStaticJson(String staticJsonText) {
    if (staticJsonText == null || staticJsonText.isBlank()) {
      return NullNode.getInstance();
    }
    try {
      return OBJECT_MAPPER.readTree(staticJsonText);
    } catch (Exception malformedJson) {
      LOGGER.warn(
          "Could not parse STATIC_JSON column as JSON; returning an empty object.", malformedJson);
      return OBJECT_MAPPER.createObjectNode();
    }
  }
}
