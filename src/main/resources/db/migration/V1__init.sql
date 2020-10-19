DROP INDEX IF EXISTS clothes_lower_name_idx;
DROP TABLE IF EXISTS clothes;
DROP TABLE IF EXISTS outfits;
DROP TABLE IF EXISTS categories;

CREATE TABLE outfits
(
    name TEXT PRIMARY KEY
);

CREATE TABLE categories
(
	name TEXT PRIMARY KEY
);

CREATE TABLE clothes
(
	name     TEXT PRIMARY KEY,
	category TEXT REFERENCES categories(name),
	outfit   TEXT REFERENCES outfits(name)
);

CREATE INDEX clothes_lower_name_idx ON clothes ((lower(name)));
