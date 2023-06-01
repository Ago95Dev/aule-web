-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 29, 2023 at 11:20 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aule_web`
--

-- Database creation
DROP DATABASE IF EXISTS aule_web;
CREATE DATABASE IF NOT EXISTS aule_web;
USE aule_web;
-- --------------------------------------------------------

--
-- Table structure for table `classroom`
--

CREATE TABLE `classroom` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `position_id` int(11) NOT NULL,
  `capacity` smallint(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `number_socket` smallint(6) DEFAULT NULL,
  `number_ethernet` smallint(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `classroom`
--

INSERT INTO `classroom` (`id`, `name`, `position_id`, `capacity`, `email`, `number_socket`, `number_ethernet`, `note`) VALUES
(1, 'A 0.1', 1, 40, 'segreteria@email.com', 6, 2, ''),
(2, 'A 0.2', 1, 50, 'segreteria@email.com', 6, 2, ''),
(3, 'A 1.3', 2, 60, 'segreteria@email.com', 10, 2, 'Una piccola descrizione');

-- --------------------------------------------------------

--
-- Table structure for table `classroom_has_equipment`
--

CREATE TABLE `classroom_has_equipment` (
  `id` int(11) NOT NULL,
  `classroom_id` int(11) NOT NULL,
  `equipment_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `classroom_has_equipment`
--

INSERT INTO `classroom_has_equipment` (`id`, `classroom_id`, `equipment_id`) VALUES
(1, 1, 1),
(2, 1, 3),
(3, 1, 4),
(4, 1, 5),
(5, 2, 1),
(6, 2, 4),
(7, 2, 5),
(8, 3, 1),
(9, 3, 2);

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`id`, `name`) VALUES
(1, 'Corso di sviluppo web');

-- --------------------------------------------------------

--
-- Table structure for table `equipment`
--

CREATE TABLE `equipment` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `equipment`
--

INSERT INTO `equipment` (`id`, `name`) VALUES
(1, 'Proiettore'),
(2, 'Schermo Motorizzato'),
(3, 'Schermo Manuale'),
(4, 'Impianto Audio'),
(5, 'PC Fisso'),
(6, 'Microfono Cablato'),
(7, 'Microfono Wireless'),
(8, 'WIFI');

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

CREATE TABLE `event` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type` enum('LEZIONE','SEMINARIO','PARZIALE','RIUNIONE','LAUREE','ALTRO') NOT NULL,
  `email` varchar(255) NOT NULL,
  `course_id` int(11) DEFAULT NULL,
  `classroom_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `event_has_recurrent`
--

CREATE TABLE `event_has_recurrent` (
  `id` int(11) NOT NULL,
  `recurrent_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

CREATE TABLE `group` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`id`, `name`, `description`) VALUES
(1, 'DICEA', 'Dipartimento di Ingegneria civile, edile - architettura e ambientale'),
(2, 'DISIM', 'Dipartimento di Ingegneria e scienze dell\'informazione e matematica'),
(3, 'DIIIE', 'Dipartimento di Ingegneria industriale e dell\'informazione e di economia'),
(4, 'MESVA', 'Dipartimento di Medicina clinica, sanità pubblica, scienze della vita e dell\'ambiente'),
(5, 'DISCAB', 'Dipartimento di Scienze cliniche applicate e biotecnologiche'),
(6, 'DSFC', 'Dipartimento di Scienze fisiche e chimiche'),
(7, 'DSU', 'Dipartimento di Scienze umane');

-- --------------------------------------------------------

--
-- Table structure for table `group_has_classroom`
--

CREATE TABLE `group_has_classroom` (
  `id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `classroom_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `group_has_classroom`
--

INSERT INTO `group_has_classroom` (`id`, `group_id`, `classroom_id`) VALUES
(1, 2, 1),
(2, 2, 2),
(3, 6, 3);

-- --------------------------------------------------------

--
-- Table structure for table `position`
--

CREATE TABLE `position` (
  `id` int(11) NOT NULL,
  `location` varchar(255) NOT NULL,
  `building` varchar(255) NOT NULL,
  `floor` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `position`
--

INSERT INTO `position` (`id`, `location`, `building`, `floor`) VALUES
(1, 'Coppito', '0', '0'),
(2, 'Coppito', '0', '1'),
(3, 'Coppito', '0', '2'),
(4, 'Coppito', '1', '0'),
(5, 'Coppito', '1', '1'),
(6, 'Coppito', '1', '2'),
(7, 'Coppito', '2', '0'),
(8, 'Coppito', '2', '1'),
(9, 'Roio', 'A', '2'),
(10, 'Roio', 'A', '1'),
(11, 'Roio', 'B', '0'),
(12, 'Roio', 'B', '1');

-- --------------------------------------------------------

--
-- Table structure for table `recurrent`
--

CREATE TABLE `recurrent` (
  `id` int(11) NOT NULL,
  `until_date` date NOT NULL,
  `typeOfRecurrency` enum('DAILY', 'WEEKLY', 'MONTHLY') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `email`, `password`, `token`) VALUES
(1, 'admin@gmail.com', 'password', NULL),
(2, 'gianluca@email.com', 'password', NULL),
(3, 'agostino@email.com', 'password', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `classroom`
--
ALTER TABLE `classroom`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name_classroom_UNIQUE` (`name`),
  ADD KEY `fk_position` (`position_id`);

--
-- Indexes for table `classroom_has_equipment`
--
ALTER TABLE `classroom_has_equipment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `par_ind` (`classroom_id`),
  ADD KEY `fk_equipment` (`equipment_id`);

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name_course_UNIQUE` (`name`);

--
-- Indexes for table `equipment`
--
ALTER TABLE `equipment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_course` (`course_id`);


--
-- Indexes for table `event_has_recurrent`
--
ALTER TABLE `event_has_recurrent`
  ADD PRIMARY KEY (`id`),
  ADD KEY `par_ind` (`recurrent_id`),
  ADD KEY `fk_event_recurrent` (`event_id`);

--
-- Indexes for table `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `group_has_classroom`
--
ALTER TABLE `group_has_classroom`
  ADD PRIMARY KEY (`id`),
  ADD KEY `par_ind` (`group_id`),
  ADD KEY `fk_classroom_group` (`classroom_id`);

--
-- Indexes for table `position`
--
ALTER TABLE `position`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `recurrent`
--
ALTER TABLE `recurrent`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `classroom`
--
ALTER TABLE `classroom`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `classroom_has_equipment`
--
ALTER TABLE `classroom_has_equipment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `course`
--
ALTER TABLE `course`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `equipment`
--
ALTER TABLE `equipment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `event`
--
ALTER TABLE `event`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;


--
-- AUTO_INCREMENT for table `event_has_recurrent`
--
ALTER TABLE `event_has_recurrent`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `group_has_classroom`
--
ALTER TABLE `group_has_classroom`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `position`
--
ALTER TABLE `position`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `recurrent`
--
ALTER TABLE `recurrent`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `classroom`
--
ALTER TABLE `classroom`
  ADD CONSTRAINT `fk_position` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`);

--
-- Constraints for table `classroom_has_equipment`
--
ALTER TABLE `classroom_has_equipment`
  ADD CONSTRAINT `fk_classroom_equipment` FOREIGN KEY (`classroom_id`) REFERENCES `classroom` (`id`),
  ADD CONSTRAINT `fk_equipment` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `event`
--
ALTER TABLE `event`
  ADD CONSTRAINT `fk_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  ADD CONSTRAINT `fk_classroom` FOREIGN KEY (`classroom_id`) REFERENCES `classroom` (`id`);
  ;

--
-- Constraints for table `event_has_recurrent`
--
ALTER TABLE `event_has_recurrent`
  ADD CONSTRAINT `fk_event_recurrent` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_recurrent` FOREIGN KEY (`recurrent_id`) REFERENCES `recurrent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `group_has_classroom`
--
ALTER TABLE `group_has_classroom`
  ADD CONSTRAINT `fk_classroom_group` FOREIGN KEY (`classroom_id`) REFERENCES `classroom` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_group` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
