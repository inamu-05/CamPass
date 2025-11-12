-- INSERTのみに使ってください。(既にデータが存在する場合は無視されます。)
-- UPDATEやDELETEは使用しないでください。


-- INSERT QUERIES:
DELETE IGNORE FROM course;
INSERT IGNORE INTO course (course_id, course_name) VALUES ('01', '情報');
INSERT IGNORE INTO course (course_id, course_name) VALUES ('02', '簿記');


-- DELETE IGNORE FROM user;
-- INSERT IGNORE INTO user (user_id, user_name, furigana, course_id, user_pass) VALUES ("admin", "asd", "asd", "01", "asd")
-- INSERT IGNORE INTO user (user_id, user_name, furigana, course_id, user_pass) VALUES ("S101", "asd", "asd", "01", "asd")
-- INSERT IGNORE INTO user (user_id, user_name, furigana, course_id, user_pass) VALUES ("S102", "asd", "asd", "01", "asd")

-- DELETE IGNORE FROM staff;
-- INSERT IGNORE INTO user (user_id, staff_category) VALUES ("admin", "0")

-- DELETE IGNORE FROM student;
-- INSERT IGNORE INTO student (user_id, birth, tel, mail, address, class_group_id, img, enrollment_status, entry_year, graduation_year, is_disabled) 
-- VALUES ("S101", "2021-02-02", "000-0000-0000", "sample@mail.com", "sample address", "1-1", "a/a/a.jpg", "1", "2022", "2023", FALSE)


DELETE IGNORE FROM subject;
INSERT IGNORE INTO subject (subject_id, subject_name, course_id, user_id) VALUES ('01', '情報', '01', 'admin');
INSERT IGNORE INTO subject (subject_id, subject_name, course_id, user_id) VALUES ('02', '簿記', '01', 'admin');
INSERT IGNORE INTO subject (subject_id, subject_name, course_id, user_id) VALUES ('03', 'ネットワーク', '01', 'admin');
INSERT IGNORE INTO subject (subject_id, subject_name, course_id, user_id) VALUES ('04', 'データベース', '01', 'admin');


DELETE IGNORE FROM subject_class;
INSERT IGNORE INTO subject_class (subject_id, user_id) VALUES ('01', 'S101');
INSERT IGNORE INTO subject_class (subject_id, user_id) VALUES ('01', 'S102');
INSERT IGNORE INTO subject_class (subject_id, user_id) VALUES ('02', 'S101');