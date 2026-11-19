package com.WF360.repository;

import com.WF360.mapper.WF360RoleDesignationDataMapper;
import com.WF360.model.WF360DataGridColumn;
import com.WF360.model.WF360RoleDesignation;
import com.WF360.model.WF360RoleDesignationUpdate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360RoleDesignationRepository {
  @Autowired private JdbcTemplate jdbcTemplate;
  private final WF360RoleDesignationDataMapper roleMapper = new WF360RoleDesignationDataMapper();

  public List<WF360RoleDesignation> findPage(String roleGroup, int page, int pageSize) {
    return jdbcTemplate.query(
        "SELECT ID, ROLE_NAME, ROLE_DESC, ROLE_LEVEL, ROLE_GROUP, IS_ACTIVE FROM WF_ROLE_MASTER WHERE ROLE_GROUP = ? OR (? = 'Engineering & Technology' AND ROLE_GROUP = 'Executive & Leadership' AND (ROLE_LEVEL IN ('L3', 'L4') OR (ROLE_LEVEL = 'L2' AND ROLE_NAME = 'CTO'))) OR (? = 'Human Resources & Recruitment' AND ROLE_GROUP = 'Executive & Leadership' AND (ROLE_LEVEL = 'L3' OR (ROLE_LEVEL = 'L2' AND ROLE_NAME = 'CHRO'))) OR (? = 'IT Support' AND ROLE_GROUP = 'Executive & Leadership' AND (ROLE_LEVEL = 'L3' OR (ROLE_LEVEL = 'L2' AND ROLE_NAME IN ('CTO', 'CISO', 'COO')))) OR (? = 'Finance & Accounts' AND ROLE_GROUP = 'Executive & Leadership' AND ROLE_LEVEL = 'L3' AND ROLE_NAME IN ('Vice President', 'Senior Vice President')) ORDER BY TO_NUMBER(SUBSTR(ROLE_LEVEL, 2)), ROLE_NAME OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
        roleMapper,
        roleGroup,
        roleGroup,
        roleGroup,
        roleGroup,
        roleGroup,
        (page - 1) * pageSize,
        pageSize);
  }

  public long count(String roleGroup) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM WF_ROLE_MASTER WHERE ROLE_GROUP = ? OR (? = 'Engineering & Technology' AND ROLE_GROUP = 'Executive & Leadership' AND (ROLE_LEVEL IN ('L3', 'L4') OR (ROLE_LEVEL = 'L2' AND ROLE_NAME = 'CTO'))) OR (? = 'Human Resources & Recruitment' AND ROLE_GROUP = 'Executive & Leadership' AND (ROLE_LEVEL = 'L3' OR (ROLE_LEVEL = 'L2' AND ROLE_NAME = 'CHRO'))) OR (? = 'IT Support' AND ROLE_GROUP = 'Executive & Leadership' AND (ROLE_LEVEL = 'L3' OR (ROLE_LEVEL = 'L2' AND ROLE_NAME IN ('CTO', 'CISO', 'COO')))) OR (? = 'Finance & Accounts' AND ROLE_GROUP = 'Executive & Leadership' AND ROLE_LEVEL = 'L3' AND ROLE_NAME IN ('Vice President', 'Senior Vice President'))",
        Long.class,
        roleGroup,
        roleGroup,
        roleGroup,
        roleGroup,
        roleGroup);
  }

  public List<String> findGroups() {
    return jdbcTemplate.queryForList(
        "SELECT DISTINCT ROLE_GROUP FROM WF_ROLE_MASTER WHERE ROLE_GROUP IS NOT NULL ORDER BY ROLE_GROUP",
        String.class);
  }

  public Map<String, String> metadata() {
    return jdbcTemplate
        .query(
            "SELECT METADATA_KEY, METADATA_VALUE FROM WF_DATAGRID_METADATA WHERE DATAGRIDID = (SELECT ID FROM WF_DATAGRID WHERE GRID_NAME = 'ROLE_DESIGNATION')",
            (rs, n) -> Map.entry(rs.getString(1), rs.getString(2)))
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public List<WF360DataGridColumn> columns() {
    return jdbcTemplate.query(
        "SELECT COLUMN_NAME, COLUMN_ORDER, UIDISPLAY_NAME, WIDTH, SORTABLE, ISHYPERLINK, TYPE, SHOWFILTER FROM WF_DATAGRID_DATAMAPPING WHERE DATAGRIDID = (SELECT ID FROM WF_DATAGRID WHERE GRID_NAME = 'ROLE_DESIGNATION') ORDER BY COLUMN_ORDER",
        (rs, n) -> {
          WF360DataGridColumn column = new WF360DataGridColumn();
          column.setColumnName(rs.getString("COLUMN_NAME"));
          column.setColumnOrder(rs.getInt("COLUMN_ORDER"));
          column.setUiDisplayName(rs.getString("UIDISPLAY_NAME"));
          column.setWidth(rs.getString("WIDTH"));
          column.setSortable(rs.getString("SORTABLE"));
          column.setIsHyperlink(rs.getString("ISHYPERLINK"));
          column.setType(rs.getString("TYPE"));
          column.setShowFilter(rs.getString("SHOWFILTER"));
          return column;
        });
  }

  public void update(long id, WF360RoleDesignationUpdate update, String updatedBy) {
    int changed =
        jdbcTemplate.update(
            "UPDATE WF_ROLE_MASTER SET ROLE_NAME=?, ROLE_DESC=?, ROLE_LEVEL=?, IS_ACTIVE=?, UPD_BY=?, UPD_DATE=? WHERE ID=?",
            update.getRoleName().trim(),
            update.getRoleDesc(),
            update.getRoleLevel(),
            update.getIsActive() == null ? "Y" : update.getIsActive(),
            updatedBy,
            Timestamp.from(Instant.now()),
            id);
    if (changed == 0) throw new IllegalArgumentException("Role not found.");
  }

  public void delete(long id) {
    if (jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM WF_APP_USER_MASTER WHERE PRIMARY_ROLE_ID=?", Integer.class, id)
        > 0)
      throw new IllegalArgumentException("This role is assigned to a user and cannot be deleted.");
    if (jdbcTemplate.update("DELETE FROM WF_ROLE_MASTER WHERE ID=?", id) == 0)
      throw new IllegalArgumentException("Role not found.");
  }
}
