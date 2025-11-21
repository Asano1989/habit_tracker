SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('learning_subjects_id_seq', (SELECT MAX(id) FROM learning_subjects));
SELECT setval('records_id_seq', (SELECT MAX(id) FROM records));
