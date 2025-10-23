package com.example.bank.app;

import com.example.bank.tables.CustomerTable;
import com.example.bank.tables.AccountTable;
import com.example.bank.tables.TransactionTable;

public class BankApp {
    public static void main(String[] args) {
        System.out.println(" Starting Bank Simulation Project...");

        CustomerTable.createTable();
        AccountTable.createTable();
        TransactionTable.createTable();

        System.out.println("All Tables Created Successfully!");
    }
}