-- usersテーブルのicon_imageをicon_image_urlにリネームし、byteaからvarcharに変更
ALTER TABLE users
RENAME COLUMN icon_image TO icon_image_url;

ALTER TABLE users
ALTER COLUMN icon_image_url TYPE varchar;

-- doctorsテーブルのdoctor_iconをdoctor_icon_urlにリネームし、byteaからvarcharに変更
ALTER TABLE doctors
RENAME COLUMN doctor_icon TO doctor_icon_url;

ALTER TABLE doctors
ALTER COLUMN doctor_icon_url TYPE varchar;

-- family_emailsテーブルのicon_imageをicon_image_urlにリネームし、byteaからvarcharに変更
ALTER TABLE family_emails
RENAME COLUMN icon_image TO icon_image_url;

ALTER TABLE family_emails
ALTER COLUMN icon_image_url TYPE varchar;