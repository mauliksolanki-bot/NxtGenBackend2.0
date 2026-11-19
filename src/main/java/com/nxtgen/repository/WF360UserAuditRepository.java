package com.WF360.repository;

import com.WF360.model.WF360UserAudit;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360UserAuditRepository {
  @Autowired private JdbcTemplate jdbc;

  public void save(
      String empId, String category, String action, String details, String status, String actor) {
    jdbc.update(
        "INSERT INTO WF_USER_AUDIT_MASTER (ID,EMP_ID,ACTIVITY_CATEGORY,ACTIVITY_ACTION,ACTIVITY_DETAILS,ACTIVITY_STATUS,CRE_BY) VALUES (?,?,?,?,?,?,?)",
        UUID.randomUUID().toString(),
        empId,
        category,
        action,
        details,
        status,
        actor);
  }

  private static final String SEARCH_FILTER =
      " WHERE (LOWER(COALESCE(USER_MASTER.EMP_FNAME || ' ' || USER_MASTER.EMP_LNAME, AUDIT_ENTRY.EMP_ID, '')) LIKE ? OR LOWER(COALESCE(AUDIT_ENTRY.EMP_ID, '')) LIKE ? OR LOWER(COALESCE(AUDIT_ENTRY.ACTIVITY_CATEGORY, '')) LIKE ? OR LOWER(COALESCE(AUDIT_ENTRY.ACTIVITY_ACTION, '')) LIKE ? OR LOWER(COALESCE(AUDIT_ENTRY.ACTIVITY_DETAILS, '')) LIKE ?) ";
  private static final String FROM =
      " FROM WF_USER_AUDIT_MASTER AUDIT_ENTRY LEFT JOIN WF_APP_USER_MASTER USER_MASTER ON USER_MASTER.EMP_ID=AUDIT_ENTRY.EMP_ID ";

  private Object[] searchParameters(String search) {
    if (search == null || search.isBlank()) {
      return new Object[0];
    }
    String term = "%" + search.trim().toLowerCase() + "%";
    return new Object[] {term, term, term, term, term};
  }

  public List<WF360UserAudit> findPage(String search, int page, int pageSize) {
    Object[] searchParameters = searchParameters(search);
    Object[] parameters = java.util.Arrays.copyOf(searchParameters, searchParameters.length + 2);
    parameters[searchParameters.length] = (page - 1) * pageSize;
    parameters[searchParameters.length + 1] = pageSize;
    return jdbc.query(
        "SELECT AUDIT_ENTRY.ID,AUDIT_ENTRY.EMP_ID,COALESCE(USER_MASTER.EMP_FNAME || ' ' || USER_MASTER.EMP_LNAME,AUDIT_ENTRY.EMP_ID) EMPLOYEE_NAME,AUDIT_ENTRY.ACTIVITY_CATEGORY,AUDIT_ENTRY.ACTIVITY_ACTION,AUDIT_ENTRY.ACTIVITY_DETAILS,AUDIT_ENTRY.ACTIVITY_STATUS,AUDIT_ENTRY.ACTIVITY_AT"
            + FROM
            + (searchParameters.length == 0 ? " " : SEARCH_FILTER)
            + "ORDER BY AUDIT_ENTRY.ACTIVITY_AT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
        (rs, row) -> {
          WF360UserAudit item = new WF360UserAudit();
          item.setId(rs.getString(1));
          item.setEmpId(rs.getString(2));
          item.setEmployeeName(rs.getString(3));
          item.setActivityCategory(rs.getString(4));
          item.setActivityAction(rs.getString(5));
          item.setActivityDetails(rs.getString(6));
          item.setActivityStatus(rs.getString(7));
          item.setActivityAt(rs.getObject(8, OffsetDateTime.class));
          return item;
        },
        parameters);
  }

  public long count(String search) {
    return jdbc.queryForObject(
        "SELECT COUNT(*)" + FROM + (searchParameters(search).length == 0 ? " " : SEARCH_FILTER),
        Long.class,
        searchParameters(search));
  }
}
