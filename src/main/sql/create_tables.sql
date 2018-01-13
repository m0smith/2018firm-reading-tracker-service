CREATE TABLE user_info (
       user_info_id int NOT NULL AUTO_INCREMENT,
       user_id VARCHAR(100),
       ward VARCHAR(20),
       user_type VARCHAR(10),
       PRIMARY KEY (user_info_id),
       INDEX `user_info_FI_1` (`user_id`)
);


CREATE TABLE user_chapters (
       user_chapters_id int NOT NULL AUTO_INCREMENT,
       user_id VARCHAR(100),
       chapter VARCHAR(20),
       PRIMARY KEY (user_chapters_id),
       INDEX `user_chapters_FI_1` (`user_id`)
);
