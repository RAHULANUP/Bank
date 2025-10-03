# Bank Application

# Schema

## Customer schema

CREATE TABLE customer (

    customer_id         BIGINT PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    address            VARCHAR(255) NOT NULL,
    aadhar_number      VARCHAR(255) UNIQUE NOT NULL,
    age                INTEGER NOT NULL

);

### Fields

- customer_id : Primary Key (Auto generated)
- name: Full name (NOT NULL)
- aadhar_number: Aadhar number (Unique, NOT NULL)
- age: Age (NOT NULL)

## Account Schema

CREATE TABLE account (

    account_number        BIGINT PRIMARY KEY,
    customer_account_no   BIGINT REFERENCES customer(customer_id),
    balance              DECIMAL(19,2) DEFAULT 0.00
);

### Fields

- account_number - Primary Key (Auto-generated)
- customer_account_no - Foreign Key to Customer table
- balance - Account balance (BigDecimal, defaults to 0.00)

## Transaction Schema

CREATE TABLE transaction (

    id                       BIGINT PRIMARY KEY,
    amount                   DECIMAL(19,2) NOT NULL,
    type                     VARCHAR(20) NOT NULL,
    transaction_date         TIMESTAMP NOT NULL,
    customer_account_number  BIGINT NOT NULL REFERENCES customer(customer_id),
    account_id              BIGINT REFERENCES account(account_number)
);

### Fields

- id - Primary Key (Auto-generated)
- amount - Transaction amount (NOT NULL)
- type - Transaction type (ENUM: DEPOSIT, WITHDRAWAL, TRANSFER)
- transaction_date - When transaction occurred (NOT NULL)
- customer_account_number - Foreign Key to Customer
- account_id - Foreign Key to Account








