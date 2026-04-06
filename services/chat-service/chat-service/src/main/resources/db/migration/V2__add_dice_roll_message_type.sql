ALTER TABLE messages DROP CONSTRAINT IF EXISTS chk_message_type;
ALTER TABLE messages ADD CONSTRAINT chk_message_type CHECK (message_type IN ('TEXT', 'SYSTEM', 'IMAGE', 'DICE_ROLL'));
