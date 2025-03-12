INSERT INTO customers (first_name, last_name, email, phone, dob, created_at) VALUES
('John', 'Doe', 'john.doe@example.com', '0712345678', '1990-05-15', CURRENT_TIMESTAMP),
('Jane', 'Smith', 'jane.smith@example.com', '0723456789', '1985-09-25', CURRENT_TIMESTAMP),
('Alice', 'Johnson', 'alice.johnson@example.com', '0734567890', '1993-07-10', CURRENT_TIMESTAMP),
('Bob', 'Brown', 'bob.brown@example.com', '0745678901', '1988-11-30', CURRENT_TIMESTAMP);


INSERT INTO accounts (customer_id, account_number, account_type, balance, created_at) VALUES
(1, '1111111111', 'CURRENT', 1000.00, CURRENT_TIMESTAMP),
(1, '2222222222', 'SAVINGS', 500.00, CURRENT_TIMESTAMP),
(2, '3333333333', 'CURRENT', 2000.00, CURRENT_TIMESTAMP),
(2, '4444444444', 'SAVINGS', 800.00, CURRENT_TIMESTAMP),
(3, '5555555555', 'CURRENT', 1500.00, CURRENT_TIMESTAMP),
(3, '6666666666', 'SAVINGS', 900.00, CURRENT_TIMESTAMP),
(4, '7777777777', 'CURRENT', 1200.00, CURRENT_TIMESTAMP),
(4, '8888888888', 'SAVINGS', 600.00, CURRENT_TIMESTAMP);


INSERT INTO transactions (from_account_number, to_account_number, amount, transaction_type, external_reference, status, created_at) VALUES
(NULL, '1111111111', 500.00, 'DEPOSIT', 'REF1001', 'COMPLETED', '2025-03-12 10:00:00'),
('1111111111', '2222222222', 200.00, 'TRANSFER', 'REF1002', 'COMPLETED', '2025-03-12 12:00:00'),
('3333333333', NULL, 50.00, 'FEE', 'REF1003', 'COMPLETED', '2025-03-12 14:00:00'),
(NULL, '4444444444', 1000.00, 'DEPOSIT', 'REF1004', 'COMPLETED', '2025-03-12 09:30:00'),
('5555555555', '6666666666', 250.00, 'TRANSFER', 'REF1005', 'PENDING', '2025-03-12 13:15:00'),
('2222222222', NULL, 20.00, 'INTEREST', 'REF1006', 'COMPLETED', '2025-03-12 15:00:00'),
('7777777777', '8888888888', 150.00, 'TRANSFER', 'REF1007', 'COMPLETED', '2025-03-12 16:30:00'),
('8888888888', NULL, 30.00, 'FEE', 'REF1008', 'COMPLETED', '2025-03-12 17:00:00');


INSERT INTO bankz_transactions (customer_id, from_account_number, to_account_number, external_reference, amount, transaction_type, status, reconciliation_status, created_at) VALUES
(1, '1111111111', '3333333333', 'EXTREF2001', 500.00, 'TRANSFER', 'PROCESSED', 'RECONCILED', '2025-03-12 16:00:00'),
(2, '3333333333', '4444444444', 'EXTREF2002', 100.00, 'PAYMENT', 'PENDING', 'NOT_RECONCILED', '2025-03-12 17:00:00'),
(4, '7777777777', '8888888888', 'EXTREF2004', 150.00, 'TRANSFER', 'FAILED', 'DISCREPANCY', '2025-03-12 19:00:00');


INSERT INTO notifications (customer_id, transaction_id, message, sent_at) VALUES
(1, 2, 'Transfer of 200.00 from Current to Savings completed.', CURRENT_TIMESTAMP),
(2, 3, 'Fee of 50.00 deducted from Current Account.', CURRENT_TIMESTAMP),
(3, 5, 'Transfer of 250.00 from Current to Savings pending.', CURRENT_TIMESTAMP),
(3, 6, 'Interest of 20.00 credited to Savings.', CURRENT_TIMESTAMP),
(4, 4, 'Deposit of 1000.00 into Savings completed.', CURRENT_TIMESTAMP),
(4, 8, 'Transaction fee of 30.00 deducted from Savings.', CURRENT_TIMESTAMP);
