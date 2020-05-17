CREATE TABLE IF NOT EXISTS artists (
    id UUID NOT NULL,
    platform_id INTEGER NULL,
    amg_id INTEGER NULL,
    name VARCHAR(191) NOT NULL,
    primary_genre_name VARCHAR(191) NOT NULL,
    top_albums_fetched_at TIMESTAMP,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS albums (
    id UUID NOT NULL,
    title VARCHAR(191),
    platform_id INTEGER NULL,
    track_count INTEGER,
    release_date DATE,
    artist_id UUID NOT NULL,

    FOREIGN KEY (artist_id) REFERENCES artists(id)
);

CREATE TABLE IF NOT EXISTS users_favorite_artists (
    user_id UUID NOT NULL,
    artist_id UUID NOT NULL,

    PRIMARY KEY (user_id),
    UNIQUE (user_id, artist_id),
    FOREIGN KEY (artist_id) REFERENCES artists(id)
);

CREATE INDEX artists_platform_id_idx ON artists(platform_id);