package com.nxtgen.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nxtgen.model.NxtGenEducationDetail;
import com.nxtgen.model.NxtGenUser;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/** Maps a NXTGEN_APP_USERS database row to the NxtGenUser domain model. */
public class NxtGenUserDataMapper implements RowMapper<NxtGenUser> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NxtGenUserDataMapper.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<NxtGenEducationDetail>> EDUCATION_LIST_TYPE =
            new TypeReference<>() {
            };

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
        user.setCreatedDate(readOffsetDateTime(resultSet, "CRE_DATE"));
        user.setUpdatedBy(resultSet.getString("UPD_BY"));
        user.setUpdatedDate(readOffsetDateTime(resultSet, "UPD_DATE"));
        user.setEducation(readEducation(resultSet.getString("EDUCATION")));
        user.setProfileImage(resultSet.getString("PROFILE_IMAGE"));
        user.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        user.setDateOfJoining(readLocalDate(resultSet, "DATE_OF_JOINING"));
        user.setWorkLocation(resultSet.getString("WORK_LOCATION"));
        user.setReportingManager(resultSet.getString("REPORTING_MANAGER"));
        user.setDesignation(resultSet.getString("DESIGNATION"));
        user.setPanNumber(resultSet.getString("PAN_NUMBER"));
        user.setAadharNumber(resultSet.getString("AADHAR_NUMBER"));
        return user;
    }

    /**
     * The EDUCATION column stores a JSON array (default {@code '[]'}, enforced valid JSON by a
     * database check constraint). This deserializes it into typed rows so API consumers receive
     * structured data instead of a raw JSON string. Any unexpected/malformed content degrades to
     * an empty list rather than failing the whole profile read.
     */
    private static List<NxtGenEducationDetail> readEducation(String educationJson) {
        if (educationJson == null || educationJson.isBlank()) {
            return List.of();
        }
        try {
            return OBJECT_MAPPER.readValue(educationJson, EDUCATION_LIST_TYPE);
        } catch (Exception malformedJson) {
            LOGGER.warn("Could not parse EDUCATION column as a JSON array; returning an empty list.", malformedJson);
            return List.of();
        }
    }

    /**
     * Reads a timestamp column as {@link OffsetDateTime}. Oracle's JDBC driver supports the
     * typed {@code getObject(column, OffsetDateTime.class)} conversion natively, so that is
     * tried first. The SQLite driver (used for local/dev testing) does not implement that typed
     * conversion, so this falls back to the standard {@link ResultSet#getTimestamp(String)},
     * which every JDBC driver must support and which correctly handles however SQLite happens
     * to have stored the value (ISO text from a SQL default, or epoch millis from a bound
     * {@code java.sql.Timestamp} parameter).
     */
    private static OffsetDateTime readOffsetDateTime(ResultSet resultSet, String column) throws SQLException {
        try {
            return resultSet.getObject(column, OffsetDateTime.class);
        } catch (SQLException unsupportedByDriver) {
            Timestamp timestamp = resultSet.getTimestamp(column);
            return timestamp == null ? null : timestamp.toInstant().atOffset(ZoneOffset.UTC);
        }
    }

    /**
     * Reads a date column as {@link LocalDate}, with the same Oracle-first/SQLite-fallback
     * strategy as {@link #readOffsetDateTime(ResultSet, String)}.
     */
    private static LocalDate readLocalDate(ResultSet resultSet, String column) throws SQLException {
        try {
            return resultSet.getObject(column, LocalDate.class);
        } catch (SQLException unsupportedByDriver) {
            Date date = resultSet.getDate(column);
            return date == null ? null : date.toLocalDate();
        }
    }
}
