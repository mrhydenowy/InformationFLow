CREATE DATABASE IF NOT EXISTS PKPCargoDMS;

use PKPCargoDMS;

CREATE TABLE documents(
`document_id` int(11) NOT NULL AUTO_INCREMENT,
`name` varchar(100) NOT NULL,
`signature` varchar(100) NOT NULL,
`create_date` timestamp NULL DEFAULT NOW(),
`file_path` varchar(100) NOT NULL,
PRIMARY KEY (`document_id`)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8;