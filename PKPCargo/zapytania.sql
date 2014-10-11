CREATE DATABASE IF NOT EXISTS PKPCargoDMS;

use PKPCargoDMS;

delimiter $$

CREATE TABLE `documents` (
  `document_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `signature` varchar(100) NOT NULL,
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `file_path` varchar(100) NOT NULL,
  `considered` boolean NOT NULL,
  `date_of_considered` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`document_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8$$

CREATE TABLE `users` (
    `user_id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `password` varchar(100) NOT NULL,
	`role` varchar (10) NOT NULL,
    PRIMARY KEY (`user_id`)
)  ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8$$

INSERT INTO users VALUES (default, 'sekretarka', 'qweqwe', 'user');
INSERT INTO users VALUES (default, 'dyrektor', '123123', 'admin');