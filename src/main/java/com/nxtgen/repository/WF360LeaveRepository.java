package com.WF360.repository;

import com.WF360.model.WF360Holiday;
import com.WF360.model.WF360LeaveBalance;
import com.WF360.model.WF360LeaveCategory;
import com.WF360.model.WF360LeaveDocument;
import com.WF360.model.WF360LeaveHistory;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class WF360LeaveRepository {
  @Autowired private JdbcTemplate jdbcTemplate;

  public List<WF360LeaveCategory> categories() {
    return jdbcTemplate.query(
        "SELECT LEAVE_TYPE, DEFAULT_LEAVE FROM WF_LEAVE_CATEGORY WHERE IS_ACTIVE='Y' ORDER BY LEAVE_TYPE",
        (rs, row) -> {
          WF360LeaveCategory item = new WF360LeaveCategory();
          item.setLeaveType(rs.getString(1));
          item.setDefaultLeave(rs.getInt(2));
          return item;
        });
  }

  public List<WF360Holiday> holidays() {
    return jdbcTemplate.query(
        "SELECT HOLIDAY_NAME, ON_DATE FROM WF_HOLIDAY_CALENDAR WHERE IS_ACTIVE='Y' ORDER BY ON_DATE",
        (rs, row) -> {
          WF360Holiday item = new WF360Holiday();
          item.setHolidayName(rs.getString(1));
          item.setOnDate(rs.getDate(2).toLocalDate());
          return item;
        });
  }

  public void ensureBalances(String empId) {
    for (WF360LeaveCategory category : categories())
      jdbcTemplate.update(
          "INSERT INTO WF_LEAVE_MASTER (EMP_ID, LEAVE_TYPE, TOTAL_LEAVE, AVAILABLE_LEAVE) SELECT ?, ?, ?, ? FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM WF_LEAVE_MASTER WHERE EMP_ID=? AND LEAVE_TYPE=?)",
          empId,
          category.getLeaveType(),
          category.getDefaultLeave(),
          category.getDefaultLeave(),
          empId,
          category.getLeaveType());
  }

  public List<WF360LeaveBalance> balances(String empId) {
    return jdbcTemplate.query(
        "SELECT LEAVE_TYPE, TOTAL_LEAVE, AVAILABLE_LEAVE FROM WF_LEAVE_MASTER WHERE EMP_ID=? ORDER BY LEAVE_TYPE",
        (rs, row) -> {
          WF360LeaveBalance item = new WF360LeaveBalance();
          item.setLeaveType(rs.getString(1));
          item.setTotalLeave(rs.getInt(2));
          item.setAvailableLeave(rs.getInt(3));
          return item;
        },
        empId);
  }

  public int available(String empId, String type) {
    return jdbcTemplate.queryForObject(
        "SELECT AVAILABLE_LEAVE FROM WF_LEAVE_MASTER WHERE EMP_ID=? AND LEAVE_TYPE=?",
        Integer.class,
        empId,
        type);
  }

  public boolean holiday(LocalDate day) {
    return Boolean.TRUE.equals(
        jdbcTemplate.queryForObject(
                "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM WF_HOLIDAY_CALENDAR WHERE ON_DATE=? AND IS_ACTIVE='Y'",
                Integer.class,
                Date.valueOf(day))
            == 1);
  }

  public void deduct(String empId, String type, int days) {
    jdbcTemplate.update(
        "UPDATE WF_LEAVE_MASTER SET AVAILABLE_LEAVE=AVAILABLE_LEAVE-? WHERE EMP_ID=? AND LEAVE_TYPE=?",
        days,
        empId,
        type);
  }

  public void credit(String empId, String type, int days) {
    jdbcTemplate.update(
        "UPDATE WF_LEAVE_MASTER SET AVAILABLE_LEAVE=AVAILABLE_LEAVE+? WHERE EMP_ID=? AND LEAVE_TYPE=?",
        days,
        empId,
        type);
  }

  public boolean overlaps(String empId, LocalDate from, LocalDate to) {
    return jdbcTemplate.queryForObject(
            "SELECT CASE WHEN COUNT(*)>0 THEN 1 ELSE 0 END FROM WF_LEAVE_APPLICATIONS WHERE EMP_ID=? AND STATUS IN ('PENDING','APPROVED','AUTO_APPROVED') AND FROM_DATE<=? AND TO_DATE>=?",
            Integer.class,
            empId,
            Date.valueOf(to),
            Date.valueOf(from))
        == 1;
  }

  public Long saveDocument(String empId, String name, String mimeType, byte[] bytes, String email) {
    KeyHolder keys = new GeneratedKeyHolder();
    jdbcTemplate.update(
        connection -> {
          var ps =
              connection.prepareStatement(
                  "INSERT INTO WF_DOCUMENT_CENTER (EMP_ID,DOC_TYPE,DOC_NAME,MIME_TYPE,DOC_CONTENT,CRE_BY) VALUES (?,?,?,?,?,?)",
                  new String[] {"ID"});
          ps.setString(1, empId);
          ps.setString(2, "Leave Document");
          ps.setString(3, name);
          ps.setString(4, mimeType);
          ps.setBytes(5, bytes);
          ps.setString(6, email);
          return ps;
        },
        keys);
    return keys.getKey().longValue();
  }

  public void saveApplication(
      String empId,
      String type,
      LocalDate from,
      LocalDate to,
      String reason,
      Long documentId,
      String email,
      String approver,
      boolean autoApproved,
      int days) {
    jdbcTemplate.update(
        "INSERT INTO WF_LEAVE_APPLICATIONS (EMP_ID,LEAVE_TYPE,FROM_DATE,TO_DATE,REASON,DOCUMENT_ID,CRE_BY,IS_APPROVED,APPROVED_BY,STATUS,LEAVE_DAYS) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
        empId,
        type,
        Date.valueOf(from),
        Date.valueOf(to),
        reason,
        documentId,
        email,
        autoApproved ? "Y" : "N",
        autoApproved ? "SYSTEM" : approver,
        autoApproved ? "AUTO_APPROVED" : "PENDING",
        days);
  }

  public List<WF360LeaveHistory> history(String empId) {
    return jdbcTemplate.query(
        "SELECT ID,LEAVE_TYPE,FROM_DATE,TO_DATE,REASON,STATUS,APPROVED_BY,CAST(CRE_DATE AS DATE) APPLIED_ON,CASE WHEN DOCUMENT_ID IS NULL THEN 'N' ELSE 'Y' END DOCUMENT_ATTACHED FROM WF_LEAVE_APPLICATIONS WHERE EMP_ID=? ORDER BY CRE_DATE DESC",
        (rs, row) -> {
          WF360LeaveHistory item = new WF360LeaveHistory();
          item.setId(rs.getLong("ID"));
          item.setLeaveType(rs.getString("LEAVE_TYPE"));
          item.setFromDate(rs.getDate("FROM_DATE").toLocalDate());
          item.setToDate(rs.getDate("TO_DATE").toLocalDate());
          item.setReason(rs.getString("REASON"));
          item.setStatus(rs.getString("STATUS"));
          item.setApprovedBy(rs.getString("APPROVED_BY"));
          item.setAppliedOn(rs.getDate("APPLIED_ON").toLocalDate());
          item.setDocumentAttached("Y".equals(rs.getString("DOCUMENT_ATTACHED")));
          return item;
        },
        empId);
  }

  public void cancel(long id, String empId, String email) {
    var rows =
        jdbcTemplate.query(
            "SELECT LEAVE_TYPE,LEAVE_DAYS FROM WF_LEAVE_APPLICATIONS WHERE ID=? AND EMP_ID=? AND STATUS='PENDING'",
            (rs, row) -> new Object[] {rs.getString(1), rs.getInt(2)},
            id,
            empId);
    if (rows.isEmpty())
      throw new IllegalArgumentException("Only pending leave requests can be cancelled.");
    jdbcTemplate.update(
        "UPDATE WF_LEAVE_APPLICATIONS SET STATUS='CANCELLED',UPD_BY=?,UPD_DATE=? WHERE ID=?",
        email,
        Timestamp.from(Instant.now()),
        id);
    credit(empId, (String) rows.getFirst()[0], (Integer) rows.getFirst()[1]);
  }

  public List<WF360LeaveHistory> approvalHistory(String manager) {
    return jdbcTemplate.query(
        "SELECT ID,LEAVE_TYPE,FROM_DATE,TO_DATE,REASON,STATUS,APPROVED_BY,CAST(CRE_DATE AS DATE) APPLIED_ON,CASE WHEN DOCUMENT_ID IS NULL THEN 'N' ELSE 'Y' END DOCUMENT_ATTACHED FROM WF_LEAVE_APPLICATIONS WHERE APPROVED_BY=? ORDER BY CASE STATUS WHEN 'PENDING' THEN 1 WHEN 'AUTO_APPROVED' THEN 2 WHEN 'CANCELLED' THEN 4 ELSE 3 END, CRE_DATE DESC",
        (rs, row) -> {
          WF360LeaveHistory item = new WF360LeaveHistory();
          item.setId(rs.getLong(1));
          item.setLeaveType(rs.getString(2));
          item.setFromDate(rs.getDate(3).toLocalDate());
          item.setToDate(rs.getDate(4).toLocalDate());
          item.setReason(rs.getString(5));
          item.setStatus(rs.getString(6));
          item.setApprovedBy(rs.getString(7));
          item.setAppliedOn(rs.getDate(8).toLocalDate());
          item.setDocumentAttached("Y".equals(rs.getString(9)));
          return item;
        },
        manager);
  }

  public WF360LeaveDocument documentForManager(long applicationId, String managerEmpId) {
    List<WF360LeaveDocument> documents =
        jdbcTemplate.query(
            "SELECT DOCUMENT.DOC_NAME,DOCUMENT.MIME_TYPE,DOCUMENT.DOC_CONTENT FROM WF_LEAVE_APPLICATIONS APPLICATION JOIN WF_DOCUMENT_CENTER DOCUMENT ON DOCUMENT.ID=APPLICATION.DOCUMENT_ID WHERE APPLICATION.ID=? AND APPLICATION.APPROVED_BY=? AND DOCUMENT.IS_DELETED='N'",
            (rs, row) -> new WF360LeaveDocument(rs.getString(1), rs.getString(2), rs.getBytes(3)),
            applicationId,
            managerEmpId);
    if (documents.isEmpty()) {
      throw new IllegalArgumentException(
          "No supporting document is available for this leave request.");
    }
    return documents.getFirst();
  }

  public void decide(long id, String manager, String status) {
    int changed =
        jdbcTemplate.update(
            "UPDATE WF_LEAVE_APPLICATIONS SET STATUS=?,IS_APPROVED=?,UPD_BY=?,UPD_DATE=? WHERE ID=? AND APPROVED_BY=? AND STATUS='PENDING'",
            status,
            "APPROVED".equals(status) ? "Y" : "N",
            manager,
            Timestamp.from(Instant.now()),
            id,
            manager);
    if (changed == 0)
      throw new IllegalArgumentException("This leave request is no longer pending your approval.");
  }
}
