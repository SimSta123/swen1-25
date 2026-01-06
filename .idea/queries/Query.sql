SELECT * FROM users
SELECT * FROM media WHERE creator_id = 1
SELECT * FROM genres
SELECT * FROM media_genres
SELECT * FROM ratings
SELECT * FROM rating_likes
SELECT * FROM favorites
SELECT m.title, m.description, m.mediaType, m.releaseYear, m.agerestriction, m.average_score, m.mediaid, m.creator_id, mg.id, g.genrename, g.mgid, u.id, u.username
FROM media m
JOIN media_genres mg ON mg.mediaid = m.mediaid
JOIN users u ON m.creator_id = u.id
JOIN genres g ON g.mgid = mg.genreid

SELECT DISTINCT m.title, m.description, m.mediaType, m.releaseYear, m.agerestriction, m.average_score, m.mediaid, m.creator_id, mg.id, g.genrename, g.mgid
FROM media m
JOIN media_genres mg ON mg.mediaid = m.mediaid
JOIN genres g ON g.mgid = mg.genreid
WHERE m.title ILIKE title

SELECT title from media
SELECT '"' || title || '"' AS title_display, LENGTH(title) FROM media;
SELECT * FROM media WHERE TRIM(title) ILIKE '%icep%';
SELECT * FROM media WHERE title ILIKE 'Inception';
SELECT '"' || title || '"' FROM media;

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

UPDATE media m
SET average_score = (
    SELECT COALESCE(AVG(r.rating), 0)::DECIMAL(3,2)
    FROM ratings r
    WHERE r.mediaid = m.mediaid
);

ALTER TABLE genres RENAME COLUMN id TO mgid;

SELECT DISTINCT
    m.mediaid AS media_id,
    m.title,
    m.description,
    m.mediatype,
    m.releaseyear,
    m.agerestriction,
    m.average_score,
    m.creator_id,
    g.mgid AS genre_id,
    g.genrename,
    u.id AS user_id,
    u.username
FROM media m
         JOIN media_genres mg ON mg.mediaid = m.mediaid
         JOIN genres g ON g.mgid = mg.genreid
         JOIN users u ON m.creator_id = u.id
WHERE (m.title ILIKE '%inception%')
  AND (g.genrename = 'sci-fi')
  AND (m.mediatype = 'movie')
  AND (m.releaseyear = 2010)
  AND (m.agerestriction >= 12)
  AND (m.average_score >= 4)
ORDER BY title DESC;






