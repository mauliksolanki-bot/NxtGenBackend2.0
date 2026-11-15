package com.nxtgen.mapper;

import com.nxtgen.model.NxtGenUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

/** Maps a NXTGEN_APP_USERS database row to the NxtGenUser domain model. */
public class NxtGenUserDataMapper implements RowMapper<NxtGenUser> {

    public NxtGenUserDataMapper() {
    }

    @Override
    public NxtGenUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        NxtGenUser user = new NxtGenUser();
        user.setId(UUID.fromString(resultSet.getString("ID")));
        user.setEmpId(resultSet.getString("EMP_ID"));
        user.setEmpFirstName(resultSet.getString("EMP_FNAME"));
        user.setEmpLastName(resultSet.getString("EMP_LNAME"));
        user.setEmpEmail(resultSet.getString("EMP_EMAIL"));
        user.setHashPassword(resultSet.getString("HASH_PASSWORD"));
        user.setPrimaryRoleId(resultSet.getLong("PRIMARY_ROLE_ID"));
        user.setRoleName(resultSet.getString("ROLE_NAME"));
        user.setIsActive(resultSet.getString("IS_ACTIVE"));
        user.setCreatedBy(resultSet.getString("CRE_BY"));
        user.setCreatedDate(resultSet.getObject("CRE_DATE", OffsetDateTime.class));
        user.setUpdatedBy(resultSet.getString("UPD_BY"));
        user.setUpdatedDate(resultSet.getObject("UPD_DATE", OffsetDateTime.class));
        user.setEducation(resultSet.getString("EDUCATION"));
        user.setProfileImage(resultSet.getString("PROFILE_IMAGE"));
        user.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        user.setDateOfJoining(resultSet.getObject("DATE_OF_JOINING", java.time.LocalDate.class));
        user.setWorkLocation(resultSet.getString("WORK_LOCATION"));
        user.setReportingManager(resultSet.getString("REPORTING_MANAGER"));
        user.setDesignation(resultSet.getString("DESIGNATION"));
        user.setPanNumber(resultSet.getString("PAN_NUMBER"));
        user.setAadharNumber(resultSet.getString("AADHAR_NUMBER"));
        return user;
    }
}
