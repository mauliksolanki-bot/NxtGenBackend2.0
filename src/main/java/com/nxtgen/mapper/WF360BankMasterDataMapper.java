package com.WF360.mapper;

import com.WF360.model.WF360BankMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class WF360BankMasterDataMapper implements RowMapper<WF360BankMaster> {

  @Override
  public WF360BankMaster mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    WF360BankMaster bank = new WF360BankMaster();
    bank.setId(resultSet.getLong("ID"));
    bank.setBankName(resultSet.getString("BANK_NAME"));
    bank.setAccountNumberLength(resultSet.getInt("ACCOUNT_NUMBER_LENGTH"));
    return bank;
  }
}
