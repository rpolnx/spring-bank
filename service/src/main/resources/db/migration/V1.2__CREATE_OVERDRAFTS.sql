CREATE TABLE "overdrafts"(
"account_id" bigint PRIMARY KEY,
"remaining_limit" float,
"score_categories_id" bigint,
 CONSTRAINT fk_score_id FOREIGN KEY("score_categories_id") REFERENCES "score_categories"("id")
);
