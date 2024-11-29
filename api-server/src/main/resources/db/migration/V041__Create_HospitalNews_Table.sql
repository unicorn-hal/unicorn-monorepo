CREATE TABLE hospital_news
(
    "hospital_news_id"  uuid PRIMARY KEY,
    "hospital_id"       uuid NOT NULL REFERENCES "hospitals" ("hospital_id"),
    "title"             varchar NOT NULL,
    "contents"          varchar NOT NULL,
    "notice_image_url"  varchar,
    "related_url"       varchar,
    "posted_date"       timestamp WITHOUT TIME ZONE,
    "created_at"        timestamp DEFAULT CURRENT_TIMESTAMP,
    "deleted_at"        timestamp DEFAULT null
)