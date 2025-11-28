-- Tell the MySQL client that the following commands are in UTF-8
SET NAMES utf8mb4; 

CREATE DATABASE IF NOT EXISTS campassdb
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

-- Use the database
USE campassdb;

-- Drop tables in reverse order of foreign key dependencies for a clean start (Good practice for development)
DROP TABLE IF EXISTS cert_manage;
DROP TABLE IF EXISTS certificate;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS subject_class;
DROP TABLE IF EXISTS subject;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS class_group;
DROP TABLE IF EXISTS course;

-- Create the Course table
CREATE TABLE course (
    course_id CHAR(2) NOT NULL PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB;


-- Create the Class table
CREATE TABLE class_group (
    class_group_id CHAR(2) NOT NULL PRIMARY KEY,
    class_group CHAR(3) NOT NULL
) ENGINE=InnoDB;


-- Create the User table
CREATE TABLE user (
    user_id VARCHAR(7) NOT NULL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    furigana VARCHAR(255) NOT NULL,
    course_id CHAR(2) NOT NULL,
    user_pass VARCHAR(255) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(course_id)
) ENGINE=InnoDB;



-- Create the Staff table
CREATE TABLE staff (
    user_id VARCHAR(7) NOT NULL PRIMARY KEY,
    -- staff_name VARCHAR(100) NOT NULL,
    -- course_id CHAR(2) NOT NULL,
    staff_category CHAR(1) NOT NULL,
    -- staff_pass VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB;


-- Create the Student table
CREATE TABLE student (
    user_id VARCHAR(7) NOT NULL PRIMARY KEY,
    -- stu_name VARCHAR(100) NOT NULL,
    birth DATE NOT NULL,
    tel CHAR(13) NOT NULL,
    mail VARCHAR(100) NOT NULL,
    -- course_id CHAR(2) NOT NULL,
    address VARCHAR(255) NOT NULL,
    class_group_id CHAR(2) NOT NULL,
    -- stu_pass VARCHAR(255) NOT NULL,
    img VARCHAR(255) NOT NULL,
    enrollment_status CHAR(1) NOT NULL,
    entry_year CHAR(4) NOT NULL,
    graduation_year CHAR(4),
    is_disabled BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (class_group_id) REFERENCES class_group(class_group_id)
) ENGINE=InnoDB;


-- Create the Subject table
CREATE TABLE subject (
    subject_id CHAR(2) NOT NULL PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    course_id CHAR(2) NOT NULL,
    user_id CHAR(7) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB;


-- Create the Attendance table 
CREATE TABLE attendance (
    subject_id CHAR(2) NOT NULL,
    user_id VARCHAR(7) NOT NULL,
    session_datetime DATETIME NOT NULL,
    attended_on DATETIME,
    is_attended BOOLEAN NOT NULL DEFAULT FALSE,
    remark CHAR(1) NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    PRIMARY KEY (subject_id, user_id, session_datetime)
) ENGINE=InnoDB;


-- Create the Certificate table
CREATE TABLE certificate (
    certificate_id CHAR(2) NOT NULL PRIMARY KEY,
    certificate_name VARCHAR(100) NOT NULL,
    price INT(4) NOT NULL
) ENGINE=InnoDB;


-- Create the CertManage table
CREATE TABLE cert_manage (
    application_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    certificate_id CHAR(2) NOT NULL,
    user_id VARCHAR(7) NOT NULL,
    quantity INT(1) NOT NULL,
    receive CHAR(1) NOT NULL,
    payment CHAR(1) NOT NULL,
    requested_on DATE NOT NULL,
    is_printed BOOLEAN NOT NULL DEFAULT FALSE,
    situation CHAR(1) NOT NULL,
    FOREIGN KEY (certificate_id) REFERENCES certificate(certificate_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB;


-- Create Junction Table: Links a Subject to a Class Group
CREATE TABLE subject_class (
    subject_id CHAR(2) NOT NULL,
    user_id CHAR(7) NOT NULL,
    -- class_group_id CHAR(3) NOT NULL,
    PRIMARY KEY (subject_id, user_id),
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
    -- FOREIGN KEY (class_group_id) REFERENCES student(class_group_id), -- Assuming you create a class_group table
) ENGINE=InnoDB;