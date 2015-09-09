# collegeTwitterProject
-----------------------
You need to have mysql installed on your local machine to run this program or you must have a database created on
a remote server.
The database address, username and password are in TweetDatabasePersistor class. 
Please create a database with two tables using the following queries:

CREATE TABLE `user` (
  `username` varchar(100) NOT NULL,
  `country` varchar(50) NOT NULL,
  PRIMARY KEY (`username`)) ENGINE=InnoDB DEFAULT CHARSET=latin1

CREATE TABLE `tweet` (
  `username` varchar(100) NOT NULL,
  `tweet` varchar(140) NOT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`username`,`tweet`,`date`),  
  CONSTRAINT `fktweet` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=latin1
