-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 15, 2026 at 10:14 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_film_review`
--

-- --------------------------------------------------------

--
-- Table structure for table `animated_film`
--

CREATE TABLE `animated_film` (
  `id_film` int NOT NULL,
  `pengisi_suara` varchar(255) NOT NULL,
  `karakter_animasi` varchar(255) NOT NULL,
  `gaya_animasi` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `animated_film`
--

INSERT INTO `animated_film` (`id_film`, `pengisi_suara`, `karakter_animasi`, `gaya_animasi`) VALUES
(2, 'Faza Meonk', 'Si Juki', '2D Animation'),
(4, 'Prince Gabriel', 'Don', '3D Animation');

-- --------------------------------------------------------

--
-- Table structure for table `feature_film`
--

CREATE TABLE `feature_film` (
  `id_film` int NOT NULL,
  `pemeran_utama` varchar(255) NOT NULL,
  `karakter_live_action` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `feature_film`
--

INSERT INTO `feature_film` (`id_film`, `pemeran_utama`, `karakter_live_action`) VALUES
(1, 'Chicco Kurniawan', 'Oscar');

-- --------------------------------------------------------

--
-- Table structure for table `film`
--

CREATE TABLE `film` (
  `id_film` int NOT NULL,
  `judul` varchar(255) NOT NULL,
  `sutradara` varchar(255) NOT NULL,
  `tahun_rilis` int NOT NULL,
  `genre` varchar(100) NOT NULL,
  `durasi` int NOT NULL,
  `tipe_film` enum('FEATURE','ANIMATED') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `film`
--

INSERT INTO `film` (`id_film`, `judul`, `sutradara`, `tahun_rilis`, `genre`, `durasi`, `tipe_film`) VALUES
(1, '13 Bom di Jakarta', 'Angga Dwimas Sasongko', 2023, 'Action/Thriller', 144, 'FEATURE'),
(2, 'Si Juki the Movie', 'Faza Meonk', 2017, 'Animation/Comedy', 100, 'ANIMATED'),
(4, 'Jumbo', 'Ryan Adriandhy', 2023, 'Animation/Family', 90, 'ANIMATED');

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `id_review` int NOT NULL,
  `id_film` int NOT NULL,
  `rating` double NOT NULL,
  `ulasan` text NOT NULL,
  `is_rewatch` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`id_review`, `id_film`, `rating`, `ulasan`, `is_rewatch`) VALUES
(1, 1, 4.5, 'Keren!', 0),
(4, 2, 4, 'Oke bgt', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `animated_film`
--
ALTER TABLE `animated_film`
  ADD PRIMARY KEY (`id_film`);

--
-- Indexes for table `feature_film`
--
ALTER TABLE `feature_film`
  ADD PRIMARY KEY (`id_film`);

--
-- Indexes for table `film`
--
ALTER TABLE `film`
  ADD PRIMARY KEY (`id_film`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id_review`),
  ADD KEY `id_film` (`id_film`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `film`
--
ALTER TABLE `film`
  MODIFY `id_film` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `id_review` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `animated_film`
--
ALTER TABLE `animated_film`
  ADD CONSTRAINT `animated_film_ibfk_1` FOREIGN KEY (`id_film`) REFERENCES `film` (`id_film`) ON DELETE CASCADE;

--
-- Constraints for table `feature_film`
--
ALTER TABLE `feature_film`
  ADD CONSTRAINT `feature_film_ibfk_1` FOREIGN KEY (`id_film`) REFERENCES `film` (`id_film`);

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`id_film`) REFERENCES `film` (`id_film`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
