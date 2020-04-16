-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 
-- 伺服器版本： 10.3.16-MariaDB
-- PHP 版本： 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `dnetdummytable`
--
CREATE DATABASE IF NOT EXISTS `dnetdummytable` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `dnetdummytable`;

-- --------------------------------------------------------

--
-- 資料表結構 `administrator`
--

CREATE TABLE `administrator` (
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `administrator`
--

INSERT INTO `administrator` (`username`, `password`) VALUES
('test', 'test');

-- --------------------------------------------------------

--
-- 資料表結構 `attribute`
--

CREATE TABLE `attribute` (
  `id` varchar(28) NOT NULL,
  `orgname` varchar(20) NOT NULL,
  `name` varchar(28) NOT NULL,
  `value` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `attribute`
--

INSERT INTO `attribute` (`id`, `orgname`, `name`, `value`) VALUES
('AGGNAWBb', 'test', 'school', 'A'),
('DmPNfFiQ', 'test', 'age', '10~20'),
('emhsHvUc', 'test', 'school', 'C'),
('IshuxLAt', 'test', 'school', 'B'),
('jOfZcgds', 'test', 'age', '21~30'),
('KJvdPVKP', 'test', 'age', '31~40'),
('KwfylkIS', 'test', 'department', 'ee'),
('LCZjnhuc', 'test', 'position', 'student'),
('PthrFkZN', 'test', 'position', 'teacher'),
('qVjQQXhW', 'test', 'gender', 'male'),
('rZIAiEgX', 'test', 'gender', 'female'),
('weWVXMNB', 'test', 'age', '41~50'),
('WwajeDPo', 'test', 'age', '51~60'),
('zoWsdFzA', 'test', 'department', 'cse');

-- --------------------------------------------------------

--
-- 資料表結構 `customer`
--

CREATE TABLE `customer` (
  `random_code` varchar(28) NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `customer`
--

INSERT INTO `customer` (`random_code`, `username`, `password`, `email`) VALUES
('UXKtFtfP', 'OOO', 'XXX', 'XXXX@XXXXX'),
('ZmngrZuw', 'test', 'test', 'richardwang1134@gmail.com'),
('LduKzxdq', 'test1', 'OKKzjqMX', 'richardwang1134@gmail.com');

-- --------------------------------------------------------

--
-- 資料表結構 `customer_attribute`
--

CREATE TABLE `customer_attribute` (
  `id` int(11) NOT NULL,
  `username` varchar(28) NOT NULL,
  `orgname` varchar(20) NOT NULL,
  `attribute_id` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `customer_attribute`
--

INSERT INTO `customer_attribute` (`id`, `username`, `orgname`, `attribute_id`) VALUES
(66, 'test', 'test', 'emhsHvUc'),
(67, 'test', 'test', 'KJvdPVKP'),
(68, 'test', 'test', 'zoWsdFzA'),
(69, 'test', 'test', 'PthrFkZN'),
(70, 'test', 'test', 'qVjQQXhW');

-- --------------------------------------------------------

--
-- 資料表結構 `organization`
--

CREATE TABLE `organization` (
  `username` varchar(128) NOT NULL,
  `password` varchar(28) NOT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(28) NOT NULL,
  `orgname` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- 傾印資料表的資料 `organization`
--

INSERT INTO `organization` (`username`, `password`, `address`, `email`, `orgname`) VALUES
('nsysu', 'nsysu', 'No. 70, Lianhai Rd., Gushan Dist., Kaohsiung City 804, Taiwan (R.O.C.)', 'nsysu@nsysu.edu.com', 'nsysu'),
('OOO', '2WPBF6H4', 'XXXX', 'XXXX@XXXXX', 'XXXXX'),
('test', 'test', 'test rd,test city, Taiwan ROC', 'richardwang1134@gmail.com', 'test');

-- --------------------------------------------------------

--
-- 資料表結構 `organization_customer`
--

CREATE TABLE `organization_customer` (
  `id` int(11) NOT NULL,
  `orgname` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(11) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 傾印資料表的資料 `organization_customer`
--

INSERT INTO `organization_customer` (`id`, `orgname`, `username`) VALUES
(18, 'test', 'test');

-- --------------------------------------------------------

--
-- 資料表結構 `organization_policy`
--

CREATE TABLE `organization_policy` (
  `name` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `orgname` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `option1` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `option2` varchar(28) COLLATE utf8_unicode_ci NOT NULL,
  `option3` varchar(28) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 傾印資料表的資料 `organization_policy`
--

INSERT INTO `organization_policy` (`name`, `orgname`, `option1`, `option2`, `option3`) VALUES
('age', 'test', '10~20', '41~50', '10~20'),
('department', 'test', 'ee', 'ee', 'ee'),
('gender', 'test', 'male', 'male', 'male'),
('position', 'test', 'student', 'student', 'student'),
('school', 'test', 'A', 'C', 'B');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `administrator`
--
ALTER TABLE `administrator`
  ADD PRIMARY KEY (`username`);

--
-- 資料表索引 `attribute`
--
ALTER TABLE `attribute`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`username`);

--
-- 資料表索引 `customer_attribute`
--
ALTER TABLE `customer_attribute`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `organization`
--
ALTER TABLE `organization`
  ADD PRIMARY KEY (`username`);

--
-- 資料表索引 `organization_customer`
--
ALTER TABLE `organization_customer`
  ADD PRIMARY KEY (`id`);

--
-- 資料表索引 `organization_policy`
--
ALTER TABLE `organization_policy`
  ADD PRIMARY KEY (`name`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `customer_attribute`
--
ALTER TABLE `customer_attribute`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `organization_customer`
--
ALTER TABLE `organization_customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
