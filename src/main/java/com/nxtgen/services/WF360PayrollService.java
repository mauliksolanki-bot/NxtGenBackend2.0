package com.WF360.services;
import com.WF360.model.WF360BankDetailsGrid;
import com.WF360.repository.WF360BankDetailRepository;
import com.WF360.repository.WF360UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
@Service
public class WF360PayrollService {
 @Autowired private WF360BankDetailRepository bankDetails; @Autowired private WF360UserRepository users;
 public WF360BankDetailsGrid bankGrid(String email) { var user=users.findByEmailIgnoreCase(email).orElseThrow(() -> new AccessDeniedException("Administrator access is required.")); if (!"Administrator".equals(user.getRoleName())) throw new AccessDeniedException("Administrator access is required."); return new WF360BankDetailsGrid(bankDetails.gridMetadata(), bankDetails.gridColumns(), bankDetails.findAll()); }
}
