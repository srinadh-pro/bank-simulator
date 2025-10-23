package com.simulator.bank.dao;

import com.simulator.bank.model.Transaction;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JDBC implementation for Transactions with automatic account balance update.
 */
public class TransactionJdbcDao {

    private final DataSource dataSource;

    public TransactionJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** -------------------- Deposit -------------------- */
    public boolean deposit(int accountId, BigDecimal amount,
                           String description, String mode)
            throws SQLException {

        String updateSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        String insertSql = "INSERT INTO transactions " +
                "(utr_number, transaction_amount, account_id, balance_amount, description, transaction_type, mode_of_transaction) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement upd = conn.prepareStatement(updateSql);
                 PreparedStatement ins = conn.prepareStatement(insertSql)) {

                // 1) Update balance
                upd.setBigDecimal(1, amount);
                upd.setInt(2, accountId);
                int rows = upd.executeUpdate();
                if (rows == 0) throw new SQLException("Account not found");

                BigDecimal newBalance = getBalance(conn, accountId);

                // 2) Insert transaction row
                ins.setString(1, generateUTR());
                ins.setBigDecimal(2, amount);
                ins.setInt(3, accountId);
                ins.setBigDecimal(4, newBalance);
                ins.setString(5, description);
                ins.setString(6, "DEPOSIT");
                ins.setString(7, mode);
                ins.executeUpdate();

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /** -------------------- Withdraw -------------------- */
    public boolean withdraw(int accountId, BigDecimal amount,
                            String description, String mode)
            throws SQLException {

        String selectBalance = "SELECT balance FROM accounts WHERE account_id = ? FOR UPDATE";
        String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
        String insertSql = "INSERT INTO transactions " +
                "(utr_number, transaction_amount, account_id, balance_amount, description, transaction_type, mode_of_transaction) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement sel = conn.prepareStatement(selectBalance);
                 PreparedStatement upd = conn.prepareStatement(updateSql);
                 PreparedStatement ins = conn.prepareStatement(insertSql)) {

                // Lock row & check balance
                sel.setInt(1, accountId);
                BigDecimal current;
                try (ResultSet rs = sel.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Account not found");
                    current = rs.getBigDecimal(1);
                }
                if (current.compareTo(amount) < 0) {
                    throw new SQLException("Insufficient balance");
                }

                // 1) Subtract amount
                upd.setBigDecimal(1, amount);
                upd.setInt(2, accountId);
                upd.executeUpdate();

                BigDecimal newBalance = current.subtract(amount);

                // 2) Insert transaction row
                ins.setString(1, generateUTR());
                ins.setBigDecimal(2, amount);
                ins.setInt(3, accountId);
                ins.setBigDecimal(4, newBalance);
                ins.setString(5, description);
                ins.setString(6, "WITHDRAW");
                ins.setString(7, mode);
                ins.executeUpdate();

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    /** -------------------- Get all by account -------------------- */
    public List<Transaction> listByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /** -------------------- Helper Methods -------------------- */
    private BigDecimal getBalance(Connection conn, int accountId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT balance FROM accounts WHERE account_id = ?")) {
            ps.setInt(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
                throw new SQLException("Account not found");
            }
        }
    }

    private String generateUTR() {
        return "UTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setUtrNumber(rs.getString("utr_number"));
        t.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        t.setTransactionAmount(rs.getBigDecimal("transaction_amount"));
        t.setDebitedDate(rs.getTimestamp("debited_date") != null ?
                rs.getTimestamp("debited_date").toLocalDateTime() : null);
        t.setAccountId(rs.getInt("account_id"));
        t.setBalanceAmount(rs.getBigDecimal("balance_amount"));
        t.setDescription(rs.getString("description"));
        t.setModifiedBy(rs.getString("modified_by"));
        t.setReceiver(rs.getString("receiver"));
        t.setTransactionType(rs.getString("transaction_type"));
        t.setModeOfTransaction(rs.getString("mode_of_transaction"));
        return t;
    }
}
