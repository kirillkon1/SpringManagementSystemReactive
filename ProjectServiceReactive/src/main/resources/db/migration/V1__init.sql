CREATE TABLE IF NOT EXISTS project (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   description VARCHAR(255),
   location VARCHAR(255),
   start_date TIMESTAMP(6),
   end_date TIMESTAMP(6),
   budget DOUBLE PRECISION,
   created_at TIMESTAMP(6),
   updated_at TIMESTAMP(6)
);