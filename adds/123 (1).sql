-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: 07 يونيو 2020 الساعة 14:18
-- إصدار الخادم: 10.4.11-MariaDB
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `123`
--

-- --------------------------------------------------------

--
-- بنية الجدول `ambulance`
--

CREATE TABLE `ambulance` (
  `platenumber` varchar(12) NOT NULL,
  `hospital` int(11) NOT NULL,
  `driver` int(11) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `status` varchar(16) NOT NULL,
  `equipment` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- إرجاع أو استيراد بيانات الجدول `ambulance`
--

INSERT INTO `ambulance` (`platenumber`, `hospital`, `driver`, `latitude`, `longitude`, `status`, `equipment`) VALUES
('car134', 1, 1, 20.1, 100.2, 'available', 'equipped');

-- --------------------------------------------------------

--
-- بنية الجدول `driver`
--

CREATE TABLE `driver` (
  `id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `hospital` int(11) NOT NULL,
  `name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- إرجاع أو استيراد بيانات الجدول `driver`
--

INSERT INTO `driver` (`id`, `username`, `password`, `hospital`, `name`) VALUES
(1, 'test', 'test', 1, 'test driver');

-- --------------------------------------------------------

--
-- بنية الجدول `er`
--

CREATE TABLE `er` (
  `id` int(11) NOT NULL,
  `hospital` int(11) NOT NULL,
  `status` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- بنية الجدول `hospital`
--

CREATE TABLE `hospital` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `phone_number` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- إرجاع أو استيراد بيانات الجدول `hospital`
--

INSERT INTO `hospital` (`id`, `name`, `latitude`, `longitude`, `phone_number`) VALUES
(1, 'test hospital', 100, 100, 1000000000);

-- --------------------------------------------------------

--
-- بنية الجدول `manager`
--

CREATE TABLE `manager` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `hospital` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- إرجاع أو استيراد بيانات الجدول `manager`
--

INSERT INTO `manager` (`id`, `name`, `username`, `password`, `hospital`) VALUES
(1, 'testUserName', 'test', 'test', 1);

-- --------------------------------------------------------

--
-- بنية الجدول `request`
--

CREATE TABLE `request` (
  `id` int(11) NOT NULL,
  `time` date NOT NULL DEFAULT current_timestamp(),
  `sender_id` bigint(14) NOT NULL,
  `record` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `origin_latitude` float NOT NULL,
  `origin_longitude` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- إرجاع أو استيراد بيانات الجدول `request`
--

INSERT INTO `request` (`id`, `time`, `sender_id`, `record`, `image`, `origin_latitude`, `origin_longitude`) VALUES
(1, '2020-05-10', 12345678901234, 'upload/requests/records/12345678901234---1589116265.mp3', 'upload/requests/images/12345678901234---1589116265.png', 100.1, 100.2),
(6, '2020-05-31', 1234, 'http//10.0.2.2/upload/requests/records/1234.3gp', 'http//10.0.2.2/upload/requests/images/1234.jpeg', 37.422, -122.084),
(7, '2020-06-07', 123456789, 'http//10.0.2.2/upload/requests/records/123456789.3gp', 'http//10.0.2.2/upload/requests/images/123456789.jpeg', 37.422, -122.084);

-- --------------------------------------------------------

--
-- بنية الجدول `request_response`
--

CREATE TABLE `request_response` (
  `id` int(11) NOT NULL,
  `request_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `response` varchar(32) NOT NULL,
  `time` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- إرجاع أو استيراد بيانات الجدول `request_response`
--

INSERT INTO `request_response` (`id`, `request_id`, `receiver_id`, `response`, `time`) VALUES
(1, 1, 1, 'Reject', '2020-05-10');

-- --------------------------------------------------------

--
-- بنية الجدول `user`
--

CREATE TABLE `user` (
  `id` bigint(14) NOT NULL,
  `name` varchar(64) NOT NULL,
  `phone_number` text NOT NULL,
  `id_photo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- إرجاع أو استيراد بيانات الجدول `user`
--

INSERT INTO `user` (`id`, `name`, `phone_number`, `id_photo`) VALUES
(1234, 'nada', '01048798', 'http//10.0.2.2/upload/userImages/1234.jpeg'),
(123456789, 'nedoz', '014794676859', 'http//10.0.2.2/upload/userImages/123456789.jpeg'),
(12345678901234, 'mostafa', '01234567891', 'upload/userImages/12345678901234.png');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ambulance`
--
ALTER TABLE `ambulance`
  ADD PRIMARY KEY (`platenumber`),
  ADD KEY `driver` (`driver`),
  ADD KEY `hospital` (`hospital`);

--
-- Indexes for table `driver`
--
ALTER TABLE `driver`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `hospital` (`hospital`);

--
-- Indexes for table `er`
--
ALTER TABLE `er`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `hospital` (`hospital`);

--
-- Indexes for table `hospital`
--
ALTER TABLE `hospital`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `manager`
--
ALTER TABLE `manager`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `hospital` (`hospital`);

--
-- Indexes for table `request`
--
ALTER TABLE `request`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sender_id` (`sender_id`);

--
-- Indexes for table `request_response`
--
ALTER TABLE `request_response`
  ADD PRIMARY KEY (`request_id`,`receiver_id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `receiver_id` (`receiver_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `driver`
--
ALTER TABLE `driver`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `hospital`
--
ALTER TABLE `hospital`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `manager`
--
ALTER TABLE `manager`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `request`
--
ALTER TABLE `request`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `request_response`
--
ALTER TABLE `request_response`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- قيود الجداول المحفوظة
--

--
-- القيود للجدول `ambulance`
--
ALTER TABLE `ambulance`
  ADD CONSTRAINT `ambulance_ibfk_1` FOREIGN KEY (`driver`) REFERENCES `driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `ambulance_ibfk_2` FOREIGN KEY (`hospital`) REFERENCES `hospital` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- القيود للجدول `driver`
--
ALTER TABLE `driver`
  ADD CONSTRAINT `driver_ibfk_1` FOREIGN KEY (`hospital`) REFERENCES `hospital` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- القيود للجدول `er`
--
ALTER TABLE `er`
  ADD CONSTRAINT `er_ibfk_1` FOREIGN KEY (`hospital`) REFERENCES `hospital` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- القيود للجدول `manager`
--
ALTER TABLE `manager`
  ADD CONSTRAINT `manager_ibfk_1` FOREIGN KEY (`hospital`) REFERENCES `hospital` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- القيود للجدول `request`
--
ALTER TABLE `request`
  ADD CONSTRAINT `request_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`);

--
-- القيود للجدول `request_response`
--
ALTER TABLE `request_response`
  ADD CONSTRAINT `request_response_ibfk_1` FOREIGN KEY (`receiver_id`) REFERENCES `driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `request_response_ibfk_2` FOREIGN KEY (`request_id`) REFERENCES `request` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
