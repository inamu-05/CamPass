-- INSERTのみに使ってください。(既にデータが存在する場合は無視されます。)
-- UPDATEやDELETEは使用しないでください。


-- INSERT QUERIES:
DELETE IGNORE FROM course;
INSERT IGNORE INTO course (course_id, course_name) VALUES ('01', '情報');

INSERT IGNORE INTO course (course_id, course_name) VALUES ('02', '簿記');