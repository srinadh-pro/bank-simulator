package com.simulator.bank.validation;

import com.simulator.bank.model.Transaction;
import java.math.BigDecimal;

public class TransactionValidator {

    public static boolean isValidUTR(String utr) {
        return utr != null && utr.length() <= 50;
    }

    public static boolean isValidAmount(BigDecimal amt) {
        return amt != null && amt.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValidDescription(String desc) {
        return desc == null || desc.length() <= 255;
    }

    public static boolean isValidTxnType(String type) {
        return type != null && (type.equalsIgnoreCase("DEBIT") || type.equalsIgnoreCase("CREDIT"));
    }

    public static boolean isValidMode(String mode) {
        return mode != null && (
                mode.equalsIgnoreCase("UPI")
                        || mode.equalsIgnoreCase("NEFT")
                        || mode.equalsIgnoreCase("IMPS")
                        || mode.equalsIgnoreCase("CASH")
        );
    }

    // Add this
    public static void validate(Transaction tx) {
        if (tx == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        if (!isValidUTR(tx.getUtrNumber())) {
            throw new IllegalArgumentException("Invalid UTR number");
        }
        if (!isValidAmount(tx.getTransactionAmount())) {
            throw new IllegalArgumentException("Amount must be > 0");
        }
        if (!isValidDescription(tx.getDescription())) {
            throw new IllegalArgumentException("Description too long");
        }
        if (!isValidTxnType(tx.getTransactionType())) {
            throw new IllegalArgumentException("Transaction type must be CREDIT or DEBIT");
        }
        if (!isValidMode(tx.getModeOfTransaction())) {
            throw new IllegalArgumentException("Invalid transaction mode");
        }
    }
}
