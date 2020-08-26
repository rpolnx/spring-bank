CREATE TYPE "PersonType" AS ENUM (
  'PF',
  'PJ'
);

CREATE TABLE "clients" (
  "document_number" varchar(14) PRIMARY KEY,
  "full_name" varchar(100) NOT NULL,
  "person_type" PersonType,
  "score" integer,
  "created_at" timestamp,
  "updated_at" timestamp
);
