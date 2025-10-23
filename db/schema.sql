-- db/schema.sql

CREATE DATABASE IF NOT EXISTS bankdb;
USE bankdb;

CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    aadhar_number VARCHAR(20),
    permanent_address VARCHAR(255),
    state VARCHAR(50),
    country VARCHAR(50),
    city VARCHAR(50),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    status VARCHAR(20),
    dob DATE,
    age INT,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    gender VARCHAR(10),
    father_name VARCHAR(100),
    mother_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    account_type VARCHAR(30),
    bank_name VARCHAR(100),
    branch VARCHAR(100),
    balance DECIMAL(15,2) DEFAULT 0,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    account_number VARCHAR(50) UNIQUE,
    ifsc_code VARCHAR(20),
    name_on_account VARCHAR(100),
    phone_linked VARCHAR(20),
    saving_amount DECIMAL(15,2) DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    utr_number VARCHAR(50),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_amount DECIMAL(15,2),
    debited_date TIMESTAMP NULL,
    account_id INT,
    balance_amount DECIMAL(15,2),
    description VARCHAR(255),
    modified_by VARCHAR(100),
    receiver VARCHAR(100),
    transaction_type VARCHAR(30),
    mode_of_transaction VARCHAR(30),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);
