CREATE TABLE "overdrafts"(
"account_id" bigint PRIMARY KEY,
"remaining_limit" float,
"score_categories_id" bigint,
"created_at" timestamp,
"updated_at" timestamp,
"deleted_on" timestamp,
CONSTRAINT fk_score_id FOREIGN KEY("score_categories_id")REFERENCES "score_categories"("id"),
UNIQUE(account_id, deleted_on)
);
