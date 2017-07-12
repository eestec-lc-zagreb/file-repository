CREATE TABLE file_metadata (
  id               VARCHAR(40)   NOT NULL,
  name             VARCHAR(255)  NOT NULL,
  type             VARCHAR(32)   NOT NULL,
  location         VARCHAR(255)  NULL,
  created_at       TIMESTAMP     NOT NULL,
  CONSTRAINT pk_document PRIMARY KEY (id)
);