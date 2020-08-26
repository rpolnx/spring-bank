CREATE TYPE "PersonType" AS ENUM (
  'PF',
  'PJ'
);

CREATE TABLE "clients" (
  "document_number" varchar(14) PRIMARY KEY UNIQUE,
  "full_name" varchar(100) NOT NULL,
  "person_type" "PersonType",
  "score" integer,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE CAST (CHARACTER VARYING as "PersonType") WITH INOUT AS IMPLICIT;