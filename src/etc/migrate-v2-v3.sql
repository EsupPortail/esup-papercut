ALTER TABLE paybox_papercut_transaction_log RENAME TO pay_papercut_transaction_log;
ALTER TABLE pay_papercut_transaction_log DROP COLUMN version, DROP COLUMN signature, DROP COLUMN papercut_ws_call_status, DROP COLUMN auto, DROP COLUMN erreur;
ALTER TABLE pay_papercut_transaction_log ALTER COLUMN montant TYPE integer USING (montant::integer);
ALTER TABLE pay_papercut_transaction_log RENAME COLUMN paper_cut_context TO papercut_context;
ALTER TABLE pay_papercut_transaction_log ADD COLUMN pay_mode VARCHAR(255);
UPDATE pay_papercut_transaction_log SET pay_mode = 'PAYBOX';
