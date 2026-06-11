ALTER TABLE payment_transaction
    MODIFY COLUMN raw_request_payload LONGTEXT NULL,
    MODIFY COLUMN raw_response_payload LONGTEXT NULL;
