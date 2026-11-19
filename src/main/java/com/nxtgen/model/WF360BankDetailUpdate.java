package com.WF360.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WF360BankDetailUpdate {

    @NotBlank
    @Pattern(regexp = "^\\d{9,18}$", message = "Account number must contain 9 to 18 digits.")
    private String accountNumber;

    @NotBlank
    private String repeatAccountNumber;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "IFSC code must be 11 characters, for example HDFC0001234.")
    private String ifscCode;

    @NotBlank
    private String bankName;

    @NotBlank
    private String branchName;

    @NotBlank
    @Pattern(regexp = "^(Salary Account|Savings Account|Current Account)$", message = "Select a valid account type.")
    private String accountType;

    @AssertTrue(message = "Account numbers do not match.")
    public boolean isAccountNumberConfirmed() {
        return accountNumber != null && accountNumber.equals(repeatAccountNumber);
    }
}
