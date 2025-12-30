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
    title varchar(36) UNIQUE NOT NULL,
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
    mgid SERIAL PRIMARY KEY,
    genreName TEXT UNIQUE NOT NULL
);

-- MEDIA ↔ GENRES (n:m) (Ein media kann merere genre haben, mehrere media können gleiche genre haben
CREATE TABLE media_genres (
    id SERIAL PRIMARY KEY,
    mediaId INT NOT NULL REFERENCES media(mediaId) ON DELETE CASCADE,
    genreId INT NOT NULL REFERENCES genres(mgid) ON DELETE CASCADE,
    UNIQUE (mediaId, genreId) -- Die kombinationen dürfen alle nur einmal vorkommen
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    userId INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mediaId INT NOT NULL REFERENCES media(mediaId) ON DELETE CASCADE,
    rating INT CHECK (rating BETWEEN 1 AND 5) NOT NULL,
    comment TEXT,
    commentConfirmed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
/*
 RETURNS TRIGGER = wird vom trigger ausgeführt? -> singlaisiert das funktion nicht ergebnis zurückgibt sonder als trigger abreitet?
 Alles zwischen $$ und $$ ist die fuktion
 TG_OP = Trigger Optionen (CREATE, DELETE, UPDATE)
 OLD = Zeile vor änderung (DELETE und UPDATE), NEW = Zeile nach änderung (INSERT und UPDATE)
 COALESCE(value, 0) ersetzt NULL durch 0. -> keine ratings dann 0 einsetzen
 :: Typ-Cast-Operator
 */
CREATE OR REPLACE FUNCTION updateAvgScMedia()
    RETURNS TRIGGER AS $$
DECLARE
    target_media_id INT;
BEGIN
    -- Prüfen, ob INSERT/UPDATE oder DELETE
    IF TG_OP = 'DELETE' THEN
        target_media_id := OLD.mediaid;
    ELSE
        target_media_id := NEW.mediaid;
    END IF;

    -- Durchschnitt für das betreffende Media aktualisieren
    UPDATE media
    SET average_score = (
        SELECT COALESCE(AVG(rating), 0)::DECIMAL(3,2)
        FROM ratings
        WHERE mediaid = target_media_id
    )
    WHERE mediaid = target_media_id;

    -- Für AFTER Trigger RETURN NULL bei DELETE, sonst RETURN NEW
    IF TG_OP = 'DELETE' THEN
        RETURN NULL;
    ELSE
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER updateAvgScMediaInsert
    AFTER INSERT ON ratings
    FOR EACH ROW
EXECUTE FUNCTION updateAvgScMedia();

CREATE TRIGGER updateAvgScMediaDelete
    AFTER DELETE ON ratings
    FOR EACH ROW
EXECUTE FUNCTION updateAvgScMedia();

CREATE TRIGGER updateAvgScMediaUpdate
    AFTER UPDATE OF rating ON ratings
    FOR EACH ROW
EXECUTE FUNCTION updateAvgScMedia();
