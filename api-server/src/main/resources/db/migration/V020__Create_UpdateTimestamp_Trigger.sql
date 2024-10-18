CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 各テーブルにトリガーを追加
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_doctors_updated_at
    BEFORE UPDATE ON doctors
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_health_checkups_updated_at
    BEFORE UPDATE ON health_checkups
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_chat_support_hours_updated_at
    BEFORE UPDATE ON chat_support_hours
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_call_support_hours_updated_at
    BEFORE UPDATE ON call_support_hours
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_family_emails_updated_at
    BEFORE UPDATE ON family_emails
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_call_reservations_updated_at
    BEFORE UPDATE ON call_reservations
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();