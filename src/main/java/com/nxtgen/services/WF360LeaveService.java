package com.WF360.services;

import com.WF360.model.WF360Holiday;
import com.WF360.model.WF360LeaveBalance;
import com.WF360.model.WF360LeaveCategory;
import com.WF360.model.WF360LeaveDocument;
import com.WF360.model.WF360LeaveHistory;
import com.WF360.repository.WF360LeaveRepository;
import com.WF360.repository.WF360UserRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WF360LeaveService {
  private static final List<String> ALLOWED_TYPES =
      List.of(
          "image/jpeg",
          "image/png",
          "application/pdf",
          "application/msword",
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
  @Autowired private WF360LeaveRepository leaves;
  @Autowired private WF360UserRepository users;
  @Autowired private WF360UserAuditService audits;

  public List<WF360LeaveCategory> categories() {
    return leaves.categories();
  }

  public List<WF360Holiday> holidays() {
    return leaves.holidays();
  }

  public List<WF360LeaveBalance> balances(String email) {
    String empId = user(email).getEmpId();
    leaves.ensureBalances(empId);
    return leaves.balances(empId);
  }

  @Transactional
  public void apply(
      String email,
      String leaveType,
      LocalDate from,
      LocalDate to,
      String reason,
      MultipartFile document) {
    if (from.isAfter(to) || from.isBefore(LocalDate.now()))
      throw new IllegalArgumentException("Select a valid leave date range.");
    if (reason == null || reason.trim().isEmpty())
      throw new IllegalArgumentException("Reason is required.");
    var employee = user(email);
    String empId = employee.getEmpId();
    leaves.ensureBalances(empId);
    int days = workingDays(from, to);
    if (days == 0) throw new IllegalArgumentException("Select at least one working day.");
    if (leaves.overlaps(empId, from, to))
      throw new IllegalArgumentException(
          "Leave has already been applied for one or more selected dates.");
    if (leaves.available(empId, leaveType) < days)
      throw new IllegalArgumentException("Insufficient leave balance for the selected dates.");
    Long documentId = null;
    boolean documentRequired = days > 3 || "Sick Leave".equals(leaveType);
    if (documentRequired && (document == null || document.isEmpty()))
      throw new IllegalArgumentException(
          "Supporting document is required for Sick Leave and leave requests longer than 3 days.");
    if (document != null && !document.isEmpty()) {
      if (document.getSize() > 5 * 1024 * 1024
          || !ALLOWED_TYPES.contains(document.getContentType()))
        throw new IllegalArgumentException("Upload JPG, PNG, PDF, DOC, or DOCX files up to 5 MB.");
      try {
        documentId =
            leaves.saveDocument(
                empId,
                document.getOriginalFilename(),
                document.getContentType(),
                document.getBytes(),
                email);
      } catch (java.io.IOException exception) {
        throw new IllegalArgumentException("The document could not be uploaded.");
      }
    }
    boolean autoApproved = "Sick Leave".equals(leaveType) && days <= 3;
    leaves.deduct(empId, leaveType, days);
    leaves.saveApplication(
        empId,
        leaveType,
        from,
        to,
        reason.trim(),
        documentId,
        email,
        users.reportingManagerEmployeeId(email),
        autoApproved,
        days);
    audits.log(
        empId,
        "Leave Management",
        autoApproved ? "Leave auto approved" : "Leave applied",
        leaveType + " from " + from + " to " + to,
        autoApproved ? "SUCCESS" : "PENDING",
        email);
  }

  public List<WF360LeaveHistory> history(String email) {
    return leaves.history(user(email).getEmpId());
  }

  @Transactional
  public void cancel(String email, long id) {
    var employee = user(email);
    leaves.cancel(id, employee.getEmpId(), email);
    audits.log(
        employee.getEmpId(),
        "Leave Management",
        "Leave cancelled",
        "Leave request " + id + " cancelled.",
        "SUCCESS",
        email);
  }

  public List<WF360LeaveHistory> approvals(String email) {
    return leaves.approvalHistory(user(email).getEmpId());
  }

  public WF360LeaveDocument document(String email, long applicationId) {
    return leaves.documentForManager(applicationId, user(email).getEmpId());
  }

  public void decide(String email, long id, String status) {
    var manager = user(email);
    leaves.decide(id, manager.getEmpId(), status);
    audits.log(
        manager.getEmpId(),
        "Leave Management",
        "Leave " + status.toLowerCase(),
        "Leave request " + id + " was " + status.toLowerCase() + ".",
        "SUCCESS",
        email);
  }

  private int workingDays(LocalDate from, LocalDate to) {
    int count = 0;
    for (LocalDate day = from; !day.isAfter(to); day = day.plusDays(1))
      if (day.getDayOfWeek() != DayOfWeek.SATURDAY
          && day.getDayOfWeek() != DayOfWeek.SUNDAY
          && !leaves.holiday(day)) count++;
    return count;
  }

  private com.WF360.model.WF360User user(String email) {
    return users
        .findByEmailIgnoreCase(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
  }
}
