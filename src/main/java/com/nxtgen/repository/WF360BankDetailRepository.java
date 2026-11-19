package com.WF360.repository;

import com.WF360.mapper.WF360BankDetailDataMapper;
import com.WF360.model.WF360BankDetail;
import com.WF360.model.WF360BankDetailUpdate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.WF360.model.WF360DataGridColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360BankDetailRepository {

    private static final String FIND_BY_EMPLOYEE_ID = """
            SELECT ID, EMP_ID, ACCOUNT_NUMBER, IFSC_CODE, BANK_NAME, BRANCH_NAME, ACCOUNT_TYPE
            FROM WF_USR_BANK_MASTER
            WHERE EMP_ID = ?
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final WF360BankDetailDataMapper mapper = new WF360BankDetailDataMapper();

    public Optional<WF360BankDetail> findByEmployeeId(String employeeId) {
        return jdbcTemplate.query(FIND_BY_EMPLOYEE_ID, mapper, employeeId).stream().findFirst();
    }

    public List<WF360BankDetail> findAll() {
        return jdbcTemplate.query("SELECT ID, EMP_ID, ACCOUNT_NUMBER, IFSC_CODE, BANK_NAME, BRANCH_NAME, ACCOUNT_TYPE FROM WF_USR_BANK_MASTER ORDER BY EMP_ID", mapper);
    }

    public Map<String, String> gridMetadata() {
        return jdbcTemplate.query("SELECT METADATA_KEY, METADATA_VALUE FROM WF_DATAGRID_METADATA WHERE DATAGRIDID = (SELECT ID FROM WF_DATAGRID WHERE GRID_NAME = 'PAYROLL_BANK_DETAILS')", (rs, n) -> Map.entry(rs.getString(1), rs.getString(2))).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<WF360DataGridColumn> gridColumns() {
        return jdbcTemplate.query("SELECT COLUMN_NAME, COLUMN_ORDER, UIDISPLAY_NAME, WIDTH, SORTABLE, ISHYPERLINK, TYPE, SHOWFILTER FROM WF_DATAGRID_DATAMAPPING WHERE DATAGRIDID = (SELECT ID FROM WF_DATAGRID WHERE GRID_NAME = 'PAYROLL_BANK_DETAILS') ORDER BY COLUMN_ORDER", (rs, n) -> {
            WF360DataGridColumn column = new WF360DataGridColumn();
            column.setColumnName(rs.getString("COLUMN_NAME")); column.setColumnOrder(rs.getInt("COLUMN_ORDER")); column.setUiDisplayName(rs.getString("UIDISPLAY_NAME")); column.setWidth(rs.getString("WIDTH")); column.setSortable(rs.getString("SORTABLE")); column.setIsHyperlink(rs.getString("ISHYPERLINK")); column.setType(rs.getString("TYPE")); column.setShowFilter(rs.getString("SHOWFILTER")); return column;
        });
    }

    public void save(String employeeId, WF360BankDetailUpdate detail, String updatedBy) {
        int updated = jdbcTemplate.update(
                """
                        UPDATE WF_USR_BANK_MASTER
                        SET ACCOUNT_NUMBER = ?, IFSC_CODE = ?, BANK_NAME = ?, BRANCH_NAME = ?, ACCOUNT_TYPE = ?,
                            UPD_BY = ?, UPD_DATE = ?
                        WHERE EMP_ID = ?
                        """,
                detail.getAccountNumber(), detail.getIfscCode(), detail.getBankName().trim(), detail.getBranchName().trim(),
                detail.getAccountType(), updatedBy, Timestamp.from(Instant.now()), employeeId);

        if (updated == 0) {
            jdbcTemplate.update(
                    """
                            INSERT INTO WF_USR_BANK_MASTER
                              (EMP_ID, ACCOUNT_NUMBER, IFSC_CODE, BANK_NAME, BRANCH_NAME, ACCOUNT_TYPE, CRE_BY)
                            VALUES (?, ?, ?, ?, ?, ?, ?)
                            """,
                    employeeId, detail.getAccountNumber(), detail.getIfscCode(), detail.getBankName().trim(),
                    detail.getBranchName().trim(), detail.getAccountType(), updatedBy);
        }
    }
}
