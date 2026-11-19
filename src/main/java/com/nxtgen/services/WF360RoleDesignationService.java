package com.WF360.services;

import com.WF360.model.WF360RoleDesignationGrid;
import com.WF360.model.WF360RoleDesignationUpdate;
import com.WF360.repository.WF360RoleDesignationRepository;
import com.WF360.repository.WF360UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class WF360RoleDesignationService {
  @Autowired private WF360RoleDesignationRepository repository;
  @Autowired private WF360UserRepository users;

  public WF360RoleDesignationGrid grid(String email, String roleGroup, int page, int pageSize) {
    requireAdministrator(email);
    int safePage = Math.max(page, 1);
    int safePageSize = Math.min(Math.max(pageSize, 1), 100);
    return new WF360RoleDesignationGrid(
        repository.metadata(),
        repository.columns(),
        repository.findPage(roleGroup, safePage, safePageSize),
        repository.count(roleGroup),
        safePage,
        safePageSize);
  }

  public java.util.List<String> groups(String email) {
    requireAdministrator(email);
    return repository.findGroups();
  }

  public void update(String email, long id, WF360RoleDesignationUpdate update) {
    requireAdministrator(email);
    repository.update(id, update, email);
  }

  public void delete(String email, long id) {
    requireAdministrator(email);
    repository.delete(id);
  }

  private void requireAdministrator(String email) {
    var user =
        users
            .findByEmailIgnoreCase(email)
            .orElseThrow(() -> new AccessDeniedException("Administrator access is required."));
    if (!"Administrator".equals(user.getRoleName()))
      throw new AccessDeniedException("Administrator access is required.");
  }
}
