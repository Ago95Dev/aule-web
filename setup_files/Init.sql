-- Database creation
DROP DATABASE IF EXISTS aule_web;
CREATE DATABASE IF NOT EXISTS aule_web;
USE aule_web;

-- Create Position Table
CREATE TABLE IF NOT EXISTS `user`(
	`id` int NOT NULL AUTO_INCREMENT,
	`email` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `token` varchar(255),
	PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Position Table
CREATE TABLE IF NOT EXISTS `position`(
	`id` int NOT NULL AUTO_INCREMENT,
	`location` varchar(255) NOT NULL,
    `building` varchar(255) NOT NULL,
    `floor` varchar(255) NOT NULL,
	PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Group Table
CREATE TABLE IF NOT EXISTS `group`(
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	`description` varchar(255) NOT NULL,
	PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Event Coordinator Group Table
CREATE TABLE IF NOT EXISTS `event_cordinator`(
	`id` int NOT NULL AUTO_INCREMENT,
	`email` varchar(255) NOT NULL,
	UNIQUE KEY `email_event_coordinator_UNIQUE` (`email`),
	PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Course Coordinator Group Table
CREATE TABLE IF NOT EXISTS `course`(
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	UNIQUE KEY `name_course_UNIQUE` (`name`),
	PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Classroom Table
CREATE TABLE IF NOT EXISTS `classroom`(
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
    `position_id` int NOT NULL,
    `capacity` smallint,
    `email` varchar(255) NOT NULL,
    `number_socket` smallint,
    `number_ethernet` smallint,
    `note`varchar(255),
    UNIQUE KEY `name_classroom_UNIQUE` (`name`),
	PRIMARY KEY(`id`),
    CONSTRAINT fk_position FOREIGN KEY (position_id)
    REFERENCES `position`(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Equipment Table
CREATE TABLE IF NOT EXISTS `equipment`(
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Recurrent Table 
CREATE TABLE IF NOT EXISTS `recurrent`(
	`id` int NOT NULL AUTO_INCREMENT,
    `until_date` date NOT NULL,
    `typeOfRecurrency`enum("DAILY, WEEKLEY, MONTHLY"),
	 PRIMARY KEY(`id`)
);

-- Create Event Table
CREATE TABLE IF NOT EXISTS `event`(
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
    `date` date NOT NULL,
    `start_time` time NOT NULL,
	`end_time` time NOT NULL,
    `description` varchar(255),
	`type` enum('LEZIONE','SEMINARIO','PARZIALE','RIUNIONE','LAUREE','ALTRO') NOT NULL,
	`event_cordinator_id` int NOT NULL,
	`course_id` int,
	PRIMARY KEY(`id`),
    CONSTRAINT fk_event_cordinator FOREIGN KEY (event_cordinator_id)
    REFERENCES `event_cordinator`(id),
	CONSTRAINT fk_course FOREIGN KEY (course_id)
    REFERENCES `course`(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Classroom - Equipment Table
CREATE TABLE `classroom_has_equipment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `classroom_id` int NOT NULL,
  `equipment_id` int NOT NULL,
  PRIMARY KEY (`id`),
  INDEX par_ind (classroom_id),  
  CONSTRAINT fk_classroom_equipment FOREIGN KEY (classroom_id)
  REFERENCES classroom(id),
  CONSTRAINT fk_equipment FOREIGN KEY (equipment_id)  
  REFERENCES equipment(id)  
  ON DELETE CASCADE  
  ON UPDATE CASCADE  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Group - Classroom Pivot Table
CREATE TABLE `group_has_classroom`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` int NOT NULL,
  `classroom_id` int NOT NULL,
  PRIMARY KEY (`id`),
  INDEX par_ind (group_id),  
  CONSTRAINT fk_group FOREIGN KEY (group_id)  
  REFERENCES `group`(id)
  ON DELETE CASCADE  
  ON UPDATE CASCADE ,
  CONSTRAINT fk_classroom_group FOREIGN KEY (classroom_id)
  REFERENCES classroom(id)
  ON DELETE CASCADE  
  ON UPDATE CASCADE  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Create Group - Classroom Pivot Table
CREATE TABLE `event_has_recurrent`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `recurrent_id` int NOT NULL,
  `event_id` int NOT NULL,
  PRIMARY KEY (`id`),
  INDEX par_ind (recurrent_id),  
  CONSTRAINT fk_recurrent FOREIGN KEY (recurrent_id)  
  REFERENCES `recurrent`(id)
  ON DELETE CASCADE  
  ON UPDATE CASCADE ,
  CONSTRAINT fk_event_recurrent FOREIGN KEY (event_id)
  REFERENCES `event`(id)
  ON DELETE CASCADE  
  ON UPDATE CASCADE  
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `aule_web`.`equipment`(`name`) VALUES("Proiettore");
INSERT INTO `aule_web`.`equipment`(`name`) VALUES("Schermo Motorizzato");
INSERT INTO `aule_web`.`equipment`(`name`) VALUES("Schermo Manuale");
INSERT INTO `aule_web`.`equipment`(`name`) VALUES("Impianto Audio");
INSERT INTO `aule_web`.`equipment`(`name`) VALUES("PC Fisso");
INSERT INTO `aule_web`.`equipment`(`name`) VALUES("Microfono Cablato");
INSERT INTO `aule_web`.`equipment`(`name`) VALUES("Microfono Wireless");
INSERT INTO `aule_web`.`equipment`(`name`) VALUES("WIFI");

INSERT INTO `user` ( `email`, `password`) VALUES ( 'admin@gmail.com', 'password');
-- WITH TOKEN FOR POSTMAN TESTING
INSERT INTO `user` (`email`, `password`,`token`) VALUES ( 'gianluca@email.com', 'password','83e31cea-d651-4deb-a90a-8a68be156c73');  

INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','0','0'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','0','1'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','0','2'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','1','0'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','1','1'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','1','2'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','2','0'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Coppito','2','1'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Roio','A','2'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Roio','A','1'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Roio','B','0'); 
INSERT INTO `position`(`location`,`building`,`floor`) VALUES('Roio','B','1'); 

INSERT INTO `aule_web`.`classroom`(`name`,`position_id`,`capacity`,`email`,`number_socket`,`number_ethernet`,`note`) VALUES("A 0.1",1,40,"segreteria@email.com",6,2,"");
INSERT INTO `aule_web`.`classroom`(`name`,`position_id`,`capacity`,`email`,`number_socket`,`number_ethernet`,`note`) VALUES("A 0.2",1,50,"segreteria@email.com",6,2,"");
INSERT INTO `aule_web`.`classroom`(`name`,`position_id`,`capacity`,`email`,`number_socket`,`number_ethernet`,`note`) VALUES("A 1.3",2,60,"segreteria@email.com",10,2,"Una piccola descrizione");

INSERT INTO `aule_web`.`group`(`name`,`description`)VALUES("DICEA","Dipartimento di Ingegneria civile, edile - architettura e ambientale");
INSERT INTO `aule_web`.`group`(`name`,`description`)VALUES("DISIM","Dipartimento di Ingegneria e scienze dell'informazione e matematica");
INSERT INTO `aule_web`.`group`(`name`,`description`)VALUES("DIIIE","Dipartimento di Ingegneria industriale e dell'informazione e di economia");
INSERT INTO `aule_web`.`group`(`name`,`description`)VALUES("MESVA","Dipartimento di Medicina clinica, sanità pubblica, scienze della vita e dell'ambiente");
INSERT INTO `aule_web`.`group`(`name`,`description`)VALUES("DISCAB","Dipartimento di Scienze cliniche applicate e biotecnologiche");
INSERT INTO `aule_web`.`group`(`name`,`description`)VALUES("DSFC","Dipartimento di Scienze fisiche e chimiche");
INSERT INTO `aule_web`.`group`(`name`,`description`)VALUES("DSU","Dipartimento di Scienze umane");

INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(1,1);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(1,3);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(1,4);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(1,5);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(2,1);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(2,4);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(2,5);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(3,1);
INSERT INTO `aule_web`.`classroom_has_equipment`(`classroom_id`,`equipment_id`)VALUES(3,2);

INSERT INTO `aule_web`.`group_has_classroom`(`group_id`,`classroom_id`)VALUES(2,1);
INSERT INTO `aule_web`.`group_has_classroom`(`group_id`,`classroom_id`)VALUES(2,2);
INSERT INTO `aule_web`.`group_has_classroom`(`group_id`,`classroom_id`)VALUES(6,3);