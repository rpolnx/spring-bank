CREATE TABLE "score_categories"(
"id" bigserial PRIMARY KEY,
"lower_score_limit" integer NOT NULL,
"higher_score_limit" integer NOT NULL,
"credit_card_limit" float,
"overdraft_limit" float,
"created_at" timestamp,
"updated_at" timestamp,
"deleted_on" timestamp
);
