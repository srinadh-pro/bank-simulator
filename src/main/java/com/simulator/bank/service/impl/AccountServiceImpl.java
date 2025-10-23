package com.simulator.bank.service.impl;

import com.simulator.bank.dao.AccountDAO;
import com.simulator.bank.model.Account;
import com.simulator.bank.service.AccountService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO dao) {
        this.accountDAO = dao;
    }

    @Override
    public Account createAccount(Account account) throws Exception {
        int id = accountDAO.create(account);
        if (id <= 0) throw new SQLException("Failed to create account");
        return account;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) throws Exception {
        return accountDAO.findByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> listAll() throws Exception {
        return accountDAO.listAll();
    }

    @Override
    public boolean updateAccount(Account account) throws Exception {
        // pass the accountId along with the object
        return accountDAO.updateAccount(account.getAccountId(), account);
    }

    @Override
    public boolean deleteByAccountNumber(String accountNumber) throws Exception {
        return accountDAO.deleteByAccountNumber(accountNumber);
    }
}
