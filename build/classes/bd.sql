-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jun 28, 2025 at 12:30 PM
-- Server version: 8.2.0
-- PHP Version: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestion_eglise`
--

-- --------------------------------------------------------

--
-- Table structure for table `eglise`
--

DROP TABLE IF EXISTS `eglise`;
CREATE TABLE IF NOT EXISTS `eglise` (
  `ideglise` varchar(50) NOT NULL,
  `Design` varchar(100) DEFAULT NULL,
  `Solde` int DEFAULT NULL,
  PRIMARY KEY (`ideglise`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `eglise`
--

INSERT INTO `eglise` (`ideglise`, `Design`, `Solde`) VALUES
('EG001', 'm', 1),
('EG004', 'd', 0),
('EG003', 'cs', 5),
('EG005', 'v', 5),
('EG006', 'e', 0),
('EG007', 's', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
