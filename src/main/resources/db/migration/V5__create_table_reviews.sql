CREATE TABLE reviews (

  id BIGSERIAL PRIMARY KEY,
  rating SMALLINT NOT NULL,
  title VARCHAR (35) NOT NULL,
  description VARCHAR(300) NOT NULL,
  created_at DATE NOT NULL,

  -- chaves estrangeiras
  book_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,

  -- constraints
  CONSTRAINT fk_reviews_book FOREIGN KEY (book_id) REFERENCES books(id),
  CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id)
)
