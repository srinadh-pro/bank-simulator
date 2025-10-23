package com.simulator.bank.dao;

import com.simulator.bank.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AccountDAO {

    Account findById(int id) throws SQLException;

    List<Account> findByCustomer(int customerId) throws SQLException;

    int create(Account a) throws SQLException;

    boolean updateBalance(int id, BigDecimal bal) throws SQLException;

    List<Account> listAll() throws SQLException;

    Optional<Account> findByAccountNumber(String accNum) throws SQLException;

    boolean deleteByAccountNumber(String accNum) throws SQLException;

    /** Update all attributes of an account (by id) */
    boolean updateAccount(int id, Account a) throws SQLException;
}
