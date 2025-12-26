-- Tabellen löschen, falls vorhanden
--DROP TABLE IF EXISTS rating_likes CASCADE;
--DROP TABLE IF EXISTS recommendations CASCADE;
--DROP TABLE IF EXISTS favorites CASCADE;
--DROP TABLE IF EXISTS ratings CASCADE;
--DROP TABLE IF EXISTS media_genres CASCADE;
--DROP TABLE IF EXISTS genres CASCADE;
--DROP TABLE IF EXISTS media CASCADE;
--DROP TABLE IF EXISTS users CASCADE;
--DROP TABLE IF EXISTS todos CASCADE;

CREATE TABLE IF NOT EXISTS todos (
    id VARCHAR(36) PRIMARY KEY,
    description TEXT NOT NULL,
    done BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username varchar(36) NOT NULL UNIQUE, --varchar hier besser, weil limit angeben kann
    password varchar(36) NOT NULL,
    UUID TEXT
);

CREATE TABLE IF NOT EXISTS media(
    title varchar(36) NOT NULL,
    description TEXT,
    mediaType TEXT CHECK (mediaType IN ('movie','series','game')),
    releaseYear INT,
    ageRestriction INT CHECK (ageRestriction BETWEEN 0 AND 18),
    average_score DECIMAL(3,2) DEFAULT 0.0,
    isDone boolean,
    mediaID SERIAL PRIMARY KEY,
    creator_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE -- wenn ein userId gelöscht wird, werden alle media von dem gelöscht
);

CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    genreName TEXT UNIQUE NOT NULL
);

-- MEDIA ↔ GENRES (n:m) (Ein media kann merere genre haben, mehrere media können gleiche genre haben
CREATE TABLE media_genres (
    id SERIAL PRIMARY KEY,
    mediaId INT NOT NULL REFERENCES media(mediaId) ON DELETE CASCADE,
    genreId INT NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
    UNIQUE (mediaId, genreId) -- Die kombinationen dürfen alle nur einmal vorkommen
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    userId INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mediaId INT NOT NULL REFERENCES media(mediaId) ON DELETE CASCADE,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    commentConfirmed BOOLEAN DEFAULT FALSE,
    UNIQUE (userId, mediaId) -- ein rating pro user pro media
);

CREATE TABLE rating_likes (
    userId INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ratingId INT NOT NULL REFERENCES ratings(id) ON DELETE CASCADE,
    PRIMARY KEY (userId, ratingId)
);

CREATE TABLE favorites (
    userId INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mediaId INT NOT NULL REFERENCES media(mediaId) ON DELETE CASCADE,
    PRIMARY KEY (userId, mediaId)
);

CREATE TABLE recommendations (
    userId INT REFERENCES users(id) ON DELETE CASCADE,
    recommendedMediaId INT REFERENCES media(mediaId) ON DELETE CASCADE,
    similarityScore DECIMAL(4,3) DEFAULT 0.0,
    PRIMARY KEY (userId, recommendedMediaId)
);

-- TRIGGERS / Notizen
-- 1. Update media.averagScore wen neues/edites rating
-- 2. generete recommendations???