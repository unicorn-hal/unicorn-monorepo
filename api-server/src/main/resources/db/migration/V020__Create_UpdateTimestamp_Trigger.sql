-- updated_at を自動で設定するトリガー関数の作成
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updatedAt = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 各テーブルにトリガーを追加
CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON "Users"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_doctors_updated_at
BEFORE UPDATE ON "Doctors"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_healthCheckups_updated_at
BEFORE UPDATE ON "HealthCheckups"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_chatSupportHours_updated_at
BEFORE UPDATE ON "ChatSupportHours"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_callSupportHours_updated_at
BEFORE UPDATE ON "CallSupportHours"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_familyEmails_updated_at
BEFORE UPDATE ON "FamilyEmails"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_callReservations_updated_at
BEFORE UPDATE ON "CallReservations"
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();