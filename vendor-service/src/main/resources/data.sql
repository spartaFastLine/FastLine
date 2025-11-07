CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS addressIsUnique
    ON p_vendor (city, district, "road_name", "zip_code")
    WHERE deleted_at IS NULL;