INSERT INTO artists(id, platform_id, amg_id, name, primary_genre_name, top_albums_fetched_at)
VALUES ('f08f87f0-3b21-47c1-b2fe-e8d34a15ec3f', 12345, 67789, 'David Guetta', 'EDM', CURRENT_DATE);

INSERT INTO albums(id, title, platform_id, track_count, release_date, artist_id)
VALUES ('ad0b5a9a-c9fa-4681-a89a-1b51e40e951a', 'Album #1', 1234, 12, '2011-01-12', 'f08f87f0-3b21-47c1-b2fe-e8d34a15ec3f');

INSERT INTO albums(id, title, platform_id, track_count, release_date, artist_id)
VALUES ('81acad62-28b3-415c-b8be-6e221a57d680', 'Album #2', 1235, 12, '2011-01-13', 'f08f87f0-3b21-47c1-b2fe-e8d34a15ec3f');

INSERT INTO albums(id, title, platform_id, track_count, release_date, artist_id)
VALUES ('a3c11bb2-8226-41df-be45-62035942ca1b', 'Album #3', 1236, 12, '2011-01-14', 'f08f87f0-3b21-47c1-b2fe-e8d34a15ec3f');

INSERT INTO albums(id, title, platform_id, track_count, release_date, artist_id)
VALUES ('0b84c178-832e-46e6-9b55-77ce07db7330', 'Album #4', 1237, 12, '2011-01-15', 'f08f87f0-3b21-47c1-b2fe-e8d34a15ec3f');

INSERT INTO albums(id, title, platform_id, track_count, release_date, artist_id)
VALUES ('efff25ad-13cb-4a0d-90bd-e68a1d2670f6', 'Album #5', 1238, 12, '2011-01-16', 'f08f87f0-3b21-47c1-b2fe-e8d34a15ec3f');

INSERT INTO users_favorite_artists(user_id, artist_id)
VALUES ('35bf6b94-3f93-4223-85c9-ab78b738c024', 'f08f87f0-3b21-47c1-b2fe-e8d34a15ec3f');