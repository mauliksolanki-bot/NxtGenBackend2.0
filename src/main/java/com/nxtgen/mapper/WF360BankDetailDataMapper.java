package com.WF360.mapper;

import com.WF360.model.WF360BankDetail;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class WF360BankDetailDataMapper implements RowMapper<WF360BankDetail> {

  @Override
  public WF360BankDetail mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    WF360BankDetail detail = new WF360BankDetail();
    detail.setId(resultSet.getLong("ID"));
    detail.setEmpId(resultSet.getString("EMP_ID"));
    detail.setAccountNumber(resultSet.getString("ACCOUNT_NUMBER"));
    detail.setIfscCode(resultSet.getString("IFSC_CODE"));
    detail.setBankName(resultSet.getString("BANK_NAME"));
    detail.setBranchName(resultSet.getString("BRANCH_NAME"));
    detail.setAccountType(resultSet.getString("ACCOUNT_TYPE"));
    return detail;
  }
}
