INSERT INTO users(id, account_id, password, name, created_at, updated_at) VALUES (1, 'admin', '$2a$10$3Ig0SBonbQXik.IUrxNMh.2By/JmZiQvNhvO/.aFCrjv4Ad3Omp8m', 'admin', NOW(), NOW());
INSERT INTO users(id, account_id, password, name, created_at, updated_at) VALUES (2, 'asano69a', '$2a$10$m9FxtHPwoZFDyCyNPjJd4OpoBHFavlbhWhB/fDTT5UI3Rvde2ocnK', 'あさの', NOW(), NOW());
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('learning_subjects_id_seq', (SELECT MAX(id) FROM learning_subjects));
SELECT setval('records_id_seq', (SELECT MAX(id) FROM records));
