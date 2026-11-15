package com.nxtgen.repository;

import com.nxtgen.mapper.NxtGenUserDataMapper;
import com.nxtgen.model.NxtGenUser;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NxtGenUserRepository {

    private static final String FIND_BY_EMAIL = """
            SELECT USER_MASTER.ID, USER_MASTER.EMP_ID, USER_MASTER.EMP_FNAME,
                   USER_MASTER.EMP_LNAME, USER_MASTER.EMP_EMAIL, USER_MASTER.HASH_PASSWORD,
                   USER_MASTER.PRIMARY_ROLE_ID, USER_MASTER.IS_ACTIVE, USER_MASTER.CRE_BY,
                   USER_MASTER.CRE_DATE, USER_MASTER.UPD_BY, USER_MASTER.UPD_DATE,
                   USER_MASTER.EDUCATION, USER_MASTER.PROFILE_IMAGE, USER_MASTER.PHONE_NUMBER,
                   USER_MASTER.DATE_OF_JOINING, USER_MASTER.WORK_LOCATION,
                   USER_MASTER.REPORTING_MANAGER, USER_MASTER.DESIGNATION,
                   USER_MASTER.PAN_NUMBER, USER_MASTER.AADHAR_NUMBER, ROLE_MASTER.ROLE_NAME
            FROM NXTGEN_APP_USER_MASTER USER_MASTER
            INNER JOIN NXTGEN_ROLE_MASTER ROLE_MASTER ON ROLE_MASTER.ID = USER_MASTER.PRIMARY_ROLE_ID
            WHERE LOWER(USER_MASTER.EMP_EMAIL) = LOWER(?)
            """;

    private final JdbcTemplate jdbcTemplate;
    private final NxtGenUserDataMapper userDataMapper = new NxtGenUserDataMapper();

    public NxtGenUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<NxtGenUser> findByEmailIgnoreCase(String email) {
        return jdbcTemplate.query(FIND_BY_EMAIL, userDataMapper, email).stream().findFirst();
    }

    public void updateEducation(String userId, String education, String updatedBy) {
        jdbcTemplate.update("UPDATE NXTGEN_APP_USER_MASTER SET EDUCATION = ?, UPD_BY = ?, UPD_DATE = SYSTIMESTAMP WHERE ID = ?", education, updatedBy, userId);
    }

    public void updateProfileImage(String userId, String profileImage, String updatedBy) {
        jdbcTemplate.update("UPDATE NXTGEN_APP_USER_MASTER SET PROFILE_IMAGE = ?, UPD_BY = ?, UPD_DATE = SYSTIMESTAMP WHERE ID = ?", profileImage, updatedBy, userId);
    }
}
