package com.simulator.bank.validation;

import com.simulator.bank.model.Account;
import java.math.BigDecimal;

public class AccountValidator {

    public static boolean isValidAccountType(String type) {
        return type != null && (type.equalsIgnoreCase("SAVINGS") || type.equalsIgnoreCase("CURRENT"));
    }

    public static boolean isValidBankName(String bank) {
        return bank != null && !bank.trim().isEmpty();
    }

    public static boolean isValidBalance(BigDecimal bal) {
        return bal != null && bal.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean isValidAccountNumber(String acc) {
        return acc != null && !acc.trim().isEmpty() && acc.length() <= 50;
    }

    public static boolean isValidIFSC(String ifsc) {
        return ifsc != null && ifsc.matches("^[A-Z]{4}0[A-Z0-9]{6}$");
    }

    public static boolean isValidPhoneLinked(String phone) {
        return phone == null || phone.matches("\\d{10}");
    }

    public static boolean isValidStatus(String status) {
        return status == null || status.matches("ACTIVE|INACTIVE|BLOCKED");
    }

    //  Add this
    public static void validate(Account acc) {
        if (acc == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        if (!isValidAccountType(acc.getAccountType())) {
            throw new IllegalArgumentException("Invalid account type");
        }
        if (!isValidBankName(acc.getBankName())) {
            throw new IllegalArgumentException("Bank name is required");
        }
        if (!isValidBalance(acc.getBalance())) {
            throw new IllegalArgumentException("Balance must be >= 0");
        }
        if (!isValidAccountNumber(acc.getAccountNumber())) {
            throw new IllegalArgumentException("Invalid account number");
        }
        if (acc.getIfscCode() != null && !isValidIFSC(acc.getIfscCode())) {
            throw new IllegalArgumentException("Invalid IFSC code");
        }
        if (!isValidPhoneLinked(acc.getPhoneLinked())) {
            throw new IllegalArgumentException("Phone linked must be 10 digits");
        }
        if (!isValidStatus(acc.getStatus())) {
            throw new IllegalArgumentException("Invalid account status");
        }
    }
}
