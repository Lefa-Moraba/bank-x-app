CREATE TABLE customers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           email VARCHAR(100) UNIQUE NOT NULL,
                           phone VARCHAR(20) UNIQUE NOT NULL,
                           dob DATE NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          customer_id BIGINT NOT NULL,
                          account_number VARCHAR(20) UNIQUE NOT NULL,
                          account_type VARCHAR(10) NOT NULL,
                          balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              from_account_number VARCHAR(20) NULL,
                              to_account_number VARCHAR(20) NULL,
                              amount DECIMAL(15,2) NOT NULL,
                              transaction_type VARCHAR(20) NOT NULL,
                              external_reference VARCHAR(50) NULL UNIQUE,
                              status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (from_account_number) REFERENCES accounts(account_number) ON DELETE SET NULL,
                              FOREIGN KEY (to_account_number) REFERENCES accounts(account_number) ON DELETE NO ACTION
);

CREATE TABLE bankz_transactions (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    customer_id BIGINT NOT NULL,
                                    from_account_number VARCHAR(20) NULL,
                                    to_account_number VARCHAR(20) NULL,
                                    external_reference VARCHAR(50) UNIQUE NOT NULL,
                                    amount DECIMAL(15,2) NOT NULL,
                                    transaction_type VARCHAR(20) NOT NULL,
                                    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                    reconciliation_status VARCHAR(20) NOT NULL DEFAULT 'NOT_RECONCILED',
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (customer_id) REFERENCES customers(id),
                                    FOREIGN KEY (from_account_number) REFERENCES accounts(account_number) ON DELETE SET NULL,
                                    FOREIGN KEY (to_account_number) REFERENCES accounts(account_number) ON DELETE NO ACTION
);

CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               customer_id BIGINT NOT NULL,
                               transaction_id BIGINT NOT NULL,
                               message TEXT NOT NULL,
                               sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
                               FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE SET NULL
);
