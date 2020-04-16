-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 28, 2019 at 04:56 AM
-- Server version: 5.7.26-0ubuntu0.18.04.1-log
-- PHP Version: 7.3.6-1+ubuntu18.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dnetdummytable`
--

-- --------------------------------------------------------

--
-- Table structure for table `adminlevel`
--

CREATE TABLE `adminlevel` (
  `id` int(11) NOT NULL,
  `adminlevel` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `admin_list`
--

CREATE TABLE `admin_list` (
  `id` int(11) NOT NULL,
  `random_code` varchar(28) NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(28) NOT NULL,
  `adminlevel` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `attribute`
--

CREATE TABLE `attribute` (
  `id` int(11) NOT NULL,
  `random_code` varchar(28) NOT NULL,
  `organization_id` int(11) NOT NULL,
  `attribute_name` varchar(28) NOT NULL,
  `type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attribute`
--

INSERT INTO `attribute` (`id`, `random_code`, `organization_id`, `attribute_name`, `type_id`) VALUES
(1, 'sdvsvs', 1, 'name', 1);

-- --------------------------------------------------------

--
-- Table structure for table `attribute_value_map`
--

CREATE TABLE `attribute_value_map` (
  `id` int(11) NOT NULL,
  `attribute_id` int(11) NOT NULL,
  `value_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `random_code` varchar(28) NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `random_code`, `username`, `password`, `email`) VALUES
(3, 'JSMPiuyT', 'xyz', 'oNp1el30', 'xyz');

-- --------------------------------------------------------

--
-- Table structure for table `customer_org_map`
--

CREATE TABLE `customer_org_map` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `organization_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer_org_map`
--

INSERT INTO `customer_org_map` (`id`, `user_id`, `organization_id`) VALUES
(1, 2, 2),
(3, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `customer_value_map`
--

CREATE TABLE `customer_value_map` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `organization_id` int(11) NOT NULL,
  `attribute_id` int(11) NOT NULL,
  `value_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer_value_map`
--

INSERT INTO `customer_value_map` (`id`, `user_id`, `organization_id`, `attribute_id`, `value_id`) VALUES
(1, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `fixed_data_types`
--

CREATE TABLE `fixed_data_types` (
  `id` int(11) NOT NULL,
  `type` varchar(28) NOT NULL,
  `format` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fixed_data_types`
--

INSERT INTO `fixed_data_types` (`id`, `type`, `format`) VALUES
(1, 'string', '{\"string\": \"value\"}');

-- --------------------------------------------------------

--
-- Table structure for table `organization`
--

CREATE TABLE `organization` (
  `id` int(11) NOT NULL,
  `random_code` varchar(28) NOT NULL,
  `username` varchar(128) NOT NULL,
  `password` varchar(28) NOT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `organization`
--

INSERT INTO `organization` (`id`, `random_code`, `username`, `password`, `address`, `email`) VALUES
(2, 'sdvewdsv', 'xyz', 'xyz', 'sdvsdvsfvsdvsdvd', 'scdscdsc'),
(3, 'svsvsv', 'pqr', 'pqr', ' xc xc xc xc ', 'zcsdc'),
(4, 'dfsgrdgsd', 'dvgasgerhfhrf', 'afwsaffwawf', 'fAWFEAF', 'AFWAF');

-- --------------------------------------------------------

--
-- Table structure for table `org_admin_map`
--

CREATE TABLE `org_admin_map` (
  `id` int(11) NOT NULL,
  `organization_id` int(11) NOT NULL,
  `admin_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `session`
--

CREATE TABLE `session` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `currentstatus` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `valueTbl`
--

CREATE TABLE `valueTbl` (
  `id` int(11) NOT NULL,
  `random_code` varchar(28) NOT NULL,
  `value` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `valueTbl`
--

INSERT INTO `valueTbl` (`id`, `random_code`, `value`) VALUES
(1, 'sgrsdgs', '{\"string\": \"John\"}');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_list`
--
ALTER TABLE `admin_list`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `attribute`
--
ALTER TABLE `attribute`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `attribute_value_map`
--
ALTER TABLE `attribute_value_map`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer_org_map`
--
ALTER TABLE `customer_org_map`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer_value_map`
--
ALTER TABLE `customer_value_map`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fixed_data_types`
--
ALTER TABLE `fixed_data_types`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `organization`
--
ALTER TABLE `organization`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `org_admin_map`
--
ALTER TABLE `org_admin_map`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `session`
--
ALTER TABLE `session`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `valueTbl`
--
ALTER TABLE `valueTbl`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin_list`
--
ALTER TABLE `admin_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `attribute`
--
ALTER TABLE `attribute`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `attribute_value_map`
--
ALTER TABLE `attribute_value_map`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `customer_org_map`
--
ALTER TABLE `customer_org_map`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `customer_value_map`
--
ALTER TABLE `customer_value_map`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `fixed_data_types`
--
ALTER TABLE `fixed_data_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `organization`
--
ALTER TABLE `organization`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `org_admin_map`
--
ALTER TABLE `org_admin_map`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `session`
--
ALTER TABLE `session`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `valueTbl`
--
ALTER TABLE `valueTbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
