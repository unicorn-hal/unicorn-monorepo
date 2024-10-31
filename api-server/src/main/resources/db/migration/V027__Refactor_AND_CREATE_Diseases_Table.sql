CREATE TABLE diseases
(
    "disease_id" SERIAL PRIMARY KEY,
    "disease_name" VARCHAR(255) NOT NULL,
    "disease_ruby" VARCHAR(255) NOT NULL,
    "is_famous" BOOLEAN NOT NULL
);

CREATE INDEX idx_disease_id ON diseases(disease_id);

ALTER TABLE chronic_diseases
    RENAME COLUMN disease_id TO chronic_disease_id;

ALTER TABLE chronic_diseases
DROP COLUMN disease_name;

ALTER TABLE chronic_diseases
    ADD COLUMN disease_id integer NOT NULL,
    ADD CONSTRAINT fk_disease
        FOREIGN KEY (disease_id) REFERENCES diseases (disease_id);