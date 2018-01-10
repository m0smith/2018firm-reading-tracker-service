CREATE TABLE user_info (
       user_info_id int NOT NULL AUTO_INCREMENT,
       user_id VARCHAR(100),
       ward VARCHAR(20),
       user_type VARCHAR(10),
       PRIMARY KEY (user_info_id)	
);


CREATE TABLE user_chapters (
       user_chapters_id int NOT NULL AUTO_INCREMENT,
       user__id VARCHAR(100),
       chapter VARCHAR(20),
       PRIMARY KEY (user_chapters_id)
);
