create type "AccountType" as ENUM (
  'C',
  'E'
);

create type "AccountStatus" as ENUM (
  'ACTIVE',
  'CREATING',
  'INACTIVE'
);

CREATE TABLE "accounts" (
  "number" bigint PRIMARY KEY,
  "client_id" varchar(14) NOT NULL,
  "agency" varchar(8) NOT NULL,
  "type" "AccountType" NOT NULL,
  "status" "AccountStatus" NOT NULL DEFAULT 'CREATING',
  "created_at" timestamp,
  "updated_at" timestamp,
  unique (client_id, agency, status)
);

CREATE CAST (CHARACTER VARYING as "AccountType") WITH INOUT AS IMPLICIT;
CREATE CAST (CHARACTER VARYING as "AccountStatus") WITH INOUT AS IMPLICIT;
