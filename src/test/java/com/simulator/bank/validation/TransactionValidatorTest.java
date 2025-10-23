package com.simulator.bank.validation;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionValidatorTest {

    @Test
    void testUTR() {
        assertTrue(TransactionValidator.isValidUTR("UTR12345"));
        assertFalse(TransactionValidator.isValidUTR(null));
    }

    @Test
    void testAmount() {
        assertTrue(TransactionValidator.isValidAmount(new BigDecimal("100")));
        assertFalse(TransactionValidator.isValidAmount(new BigDecimal("-1")));
    }

    @Test
    void testDescription() {
        assertTrue(TransactionValidator.isValidDescription("Payment for rent"));
    }

    @Test
    void testTxnTypeAndMode() {
        assertTrue(TransactionValidator.isValidTxnType("DEBIT"));
        assertFalse(TransactionValidator.isValidTxnType("TRANSFER"));

        assertTrue(TransactionValidator.isValidMode("UPI"));
        assertFalse(TransactionValidator.isValidMode("CARD"));
    }
}
