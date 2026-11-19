package com.WF360.repository;

import com.WF360.mapper.WF360BankMasterDataMapper;
import com.WF360.model.WF360BankMaster;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WF360BankMasterRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  private final WF360BankMasterDataMapper mapper = new WF360BankMasterDataMapper();

  public List<WF360BankMaster> findAllActive() {
    return jdbcTemplate.query(
        "SELECT ID, BANK_NAME, ACCOUNT_NUMBER_LENGTH FROM WF_BANK_MASTER WHERE IS_ACTIVE = 'Y' ORDER BY ID",
        mapper);
  }

  public Optional<WF360BankMaster> findActiveByBankName(String bankName) {
    return jdbcTemplate
        .query(
            """
                                SELECT ID, BANK_NAME, ACCOUNT_NUMBER_LENGTH
                                FROM WF_BANK_MASTER
                                WHERE BANK_NAME = ? AND IS_ACTIVE = 'Y'
                                """,
            mapper,
            bankName)
        .stream()
        .findFirst();
  }
}
