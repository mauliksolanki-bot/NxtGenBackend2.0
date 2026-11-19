package com.WF360.services;

import com.WF360.model.WF360UserAuditPage;
import com.WF360.repository.WF360UserAuditRepository;
import com.WF360.repository.WF360UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class WF360UserAuditService {
  @Autowired private WF360UserAuditRepository audits;
  @Autowired private WF360UserRepository users;

  public void log(
      String empId, String category, String action, String details, String status, String actor) {
    audits.save(empId, category, action, details, status, actor);
  }

  public WF360UserAuditPage all(String email, String search, int page, int pageSize) {
    var user = users.findByEmailIgnoreCase(email).orElseThrow();
    if (!"Administrator".equals(user.getRoleName()))
      throw new AccessDeniedException("Administrator access is required.");
    int safePage = Math.max(page, 1);
    int safePageSize = Math.min(Math.max(pageSize, 1), 100);
    return new WF360UserAuditPage(
        audits.findPage(search, safePage, safePageSize),
        audits.count(search),
        safePage,
        safePageSize);
  }
}
