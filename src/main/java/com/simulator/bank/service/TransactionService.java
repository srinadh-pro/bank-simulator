package com.simulator.bank.service;

import com.simulator.bank.model.Transaction;
import java.util.List;

public interface TransactionService {
    boolean deposit(Transaction tx) throws Exception;
    boolean withdraw(Transaction tx) throws Exception;
    List<Transaction> listByAccount(int accountId) throws Exception;
}
