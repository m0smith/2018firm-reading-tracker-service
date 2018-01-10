CREATE TABLE user_info (
       user_info_id int NOT NULL AUTO_INCREMENT,
       user_id VARCHAR(30),
       ward VARCHAR(20),
       user_type VARCHAR(10)
);


CREATE TABLE user_chapters (
       user_chapters_id int NOT NULL AUTO_INCREMENT,
       user_info_id VARCHAR(20),
       chapter VARCHAR(20),
       PRIMARY KEY (user_chapters_id)
);
