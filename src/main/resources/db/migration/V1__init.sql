DROP INDEX IF EXISTS clothes_name_idx;
DROP TABLE IF EXISTS clothes;
DROP TABLE IF EXISTS outfits;
DROP TABLE IF EXISTS categories;

CREATE TABLE outfits
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT      UNIQUE NOT NULL
);

CREATE TABLE categories
(
	id   BIGSERIAL PRIMARY KEY,
	name TEXT      UNIQUE NOT NULL
);

CREATE TABLE clothes
(
	id          BIGSERIAL PRIMARY KEY,
	name        TEXT      UNIQUE NOT NULL,
	category_id BIGINT    REFERENCES categories(id),
	outfit_id   BIGINT    REFERENCES outfits(id)
);

CREATE INDEX clothes_name_idx ON clothes ((lower(name)));
