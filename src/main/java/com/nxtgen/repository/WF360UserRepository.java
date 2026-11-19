package com.WF360.repository;

import com.WF360.mapper.WF360UserDataMapper;
import com.WF360.model.WF360User;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360UserRepository {

  private static final String FIND_BY_EMAIL =
      """
            SELECT USER_MASTER.ID, USER_MASTER.EMP_ID, USER_MASTER.EMP_FNAME,
                   USER_MASTER.EMP_LNAME, USER_MASTER.EMP_EMAIL, USER_MASTER.HASH_PASSWORD,
                   USER_MASTER.PRIMARY_ROLE_ID, USER_MASTER.IS_ACTIVE, USER_MASTER.CRE_BY,
                   USER_MASTER.CRE_DATE, USER_MASTER.UPD_BY, USER_MASTER.UPD_DATE,
                   USER_MASTER.EDUCATION, USER_MASTER.PROFILE_IMAGE, USER_MASTER.PHONE_NUMBER,
                   USER_MASTER.DATE_OF_JOINING, USER_MASTER.WORK_LOCATION,
                   COALESCE(MANAGER.EMP_FNAME || ' ' || MANAGER.EMP_LNAME, USER_MASTER.REPORTING_MANAGER) AS REPORTING_MANAGER, USER_MASTER.DESIGNATION,
                   USER_MASTER.PAN_NUMBER, USER_MASTER.AADHAR_NUMBER, ROLE_MASTER.ROLE_NAME,
                   ROLE_MASTER.ROLE_LEVEL
            FROM WF_APP_USER_MASTER USER_MASTER
            INNER JOIN WF_ROLE_MASTER ROLE_MASTER ON ROLE_MASTER.ID = USER_MASTER.PRIMARY_ROLE_ID
            LEFT JOIN WF_APP_USER_MASTER MANAGER ON MANAGER.EMP_ID = USER_MASTER.REPORTING_MANAGER
            WHERE LOWER(USER_MASTER.EMP_EMAIL) = LOWER(?)
            """;

  @Autowired private JdbcTemplate jdbcTemplate;
  private final WF360UserDataMapper userDataMapper = new WF360UserDataMapper();

  public Optional<WF360User> findByEmailIgnoreCase(String email) {
    return jdbcTemplate.query(FIND_BY_EMAIL, userDataMapper, email).stream().findFirst();
  }

  public String reportingManagerEmployeeId(String email) {
    return jdbcTemplate.queryForObject(
        "SELECT REPORTING_MANAGER FROM WF_APP_USER_MASTER WHERE LOWER(EMP_EMAIL)=LOWER(?)",
        String.class,
        email);
  }

  public void updateEducation(String userId, String education, String updatedBy) {
    // Bound as a JDBC parameter (instead of the Oracle-only SYSTIMESTAMP
    // SQL function) so this statement runs unchanged against any
    // configured database, including the local SQLite profile.
    jdbcTemplate.update(
        "UPDATE WF_APP_USER_MASTER SET EDUCATION = ?, UPD_BY = ?, UPD_DATE = ? WHERE ID = ?",
        education,
        updatedBy,
        Timestamp.from(Instant.now()),
        userId);
  }

  public void updateProfileImage(String userId, String profileImage, String updatedBy) {
    jdbcTemplate.update(
        "UPDATE WF_APP_USER_MASTER SET PROFILE_IMAGE = ?, UPD_BY = ?, UPD_DATE = ? WHERE ID = ?",
        profileImage,
        updatedBy,
        Timestamp.from(Instant.now()),
        userId);
  }

  public void updatePhoneNumber(String userId, String phoneNumber, String updatedBy) {
    jdbcTemplate.update(
        "UPDATE WF_APP_USER_MASTER SET PHONE_NUMBER = ?, UPD_BY = ?, UPD_DATE = ? WHERE ID = ?",
        phoneNumber,
        updatedBy,
        Timestamp.from(Instant.now()),
        userId);
  }
}
