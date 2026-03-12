-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 22, 2025 at 10:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `train_reservation`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_book_ticket` (IN `p_username` VARCHAR(50), IN `p_train_no` INT, IN `p_class` VARCHAR(10), IN `p_no_of_seats` INT, IN `p_date_of_journey` DATE)   BEGIN
    DECLARE seatFare INT;
    DECLARE totalFare INT;
    DECLARE pnr_val VARCHAR(20);

    
    SET pnr_val = CONCAT('PNR', FLOOR(RAND() * 1000000));

    
    SELECT 
        CASE 
            WHEN p_class = 'first' THEN fAC
            WHEN p_class = 'second' THEN sAC
            WHEN p_class = 'third' THEN tAC
        END
    INTO seatFare
    FROM train
    WHERE tnum = p_train_no AND doj = p_date_of_journey;

    SET totalFare = seatFare * p_no_of_seats;

    
    INSERT INTO ticket_info(pnr_no, username, train_no, class, no_of_seats, date_of_journey, total_price)
    VALUES(pnr_val, p_username, p_train_no, p_class, p_no_of_seats, p_date_of_journey, totalFare);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_cancel_ticket` (IN `p_pnr_no` VARCHAR(20), IN `p_username` VARCHAR(50))   BEGIN
    DECLARE ticket_seats INT;
    DECLARE ticket_train INT;

    SELECT no_of_seats, train_no INTO ticket_seats, ticket_train
    FROM ticket_info
    WHERE pnr_no = p_pnr_no AND username = p_username;

    DELETE FROM ticket_info WHERE pnr_no = p_pnr_no AND username = p_username;

    UPDATE train SET seats = seats + ticket_seats WHERE tnum = ticket_train;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `sno` int(11) NOT NULL,
  `uname` varchar(100) NOT NULL,
  `pass` varchar(50) NOT NULL,
  `age` int(11) NOT NULL,
  `g` varchar(10) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`sno`, `uname`, `pass`, `age`, `g`, `timestamp`) VALUES
(109, 'Priyal', '12priyal34', 18, 'Female', '2025-08-07 19:33:54'),
(123, 'Nisarg', '12nisarg34', 18, 'Male', '2025-08-07 19:29:57'),
(125, 'Jayveer', '12jayveer34', 18, 'Male', '2025-08-07 19:30:31');

-- --------------------------------------------------------

--
-- Table structure for table `passengers`
--

CREATE TABLE `passengers` (
  `PassengerId` int(11) NOT NULL,
  `PassengerName` varchar(100) NOT NULL,
  `EmailId` varchar(100) NOT NULL,
  `Password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `passengers`
--

INSERT INTO `passengers` (`PassengerId`, `PassengerName`, `EmailId`, `Password`) VALUES
(1, 'nisarg', 'nisarg@gmail.com', 'nisarg@1234'),
(2, 'harsh', 'harsh@gmail.com', '12harsh@34'),
(3, 'jayveer', 'jayveer@gmail.com', 'jayveer123$'),
(4, 'Ravi Patel', 'ravi.patel@example.com', 'Ravi@123'),
(5, 'Suman Verma', 'suman.verma@example.com', 'Suman@123'),
(6, 'Amit Kumar', 'amit.kumar@example.com', 'Amit@123'),
(7, 'Priya Shah', 'priya.shah@example.com', 'Priya@123'),
(8, 'Rakesh Meena', 'rakesh.meena@example.com', 'Rakesh@123'),
(9, 'Kajal Sharma', 'kajal.sharma@example.com', 'Kajal@123'),
(10, 'Sunil Yadav', 'sunil.yadav@example.com', 'Sunil@123'),
(11, 'Meera Joshi', 'meera.joshi@example.com', 'Meera@123'),
(12, 'Anil Singh', 'anil.singh@example.com', 'Anil@123'),
(13, 'Sneha Rathi', 'sneha.rathi@example.com', 'Sneha@123'),
(14, 'Raj', 'raj@gmail.com', 'rajpatel#123'),
(15, 'vedant', 'vedant@gmail.com', '12ved_45'),
(16, 'dhruv', 'dhruv@gmail.com', 'dhruv@1234');

-- --------------------------------------------------------

--
-- Table structure for table `ticket_info`
--

CREATE TABLE `ticket_info` (
  `pnr_no` varchar(20) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `train_no` int(11) DEFAULT NULL,
  `class` varchar(20) DEFAULT NULL,
  `no_of_seats` int(11) DEFAULT NULL,
  `date_of_journey` date DEFAULT NULL,
  `total_price` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ticket_info`
--

INSERT INTO `ticket_info` (`pnr_no`, `username`, `train_no`, `class`, `no_of_seats`, `date_of_journey`, `total_price`) VALUES
('', 'nisarg', 12297, 'third', 16, '2025-07-22', 54400),
('PNR284179', 'Rakesh Meena', 11003, 'first', 55, '2025-07-27', 291775),
('PNR377953', 'nisarg', 12010, 'first', 5, '2025-07-22', 21000),
('PNR436218', 'raj', 12297, 'first', 2, '2025-07-22', 5000),
('PNR707354', 'nisarg', 11456, 'second', 23, '2025-07-29', 98555),
('PNR801692', 'harsh', 12297, 'third', 5, '2025-07-22', 17000),
('PNR809272', 'nisarg', 12010, 'second', 3, '2025-07-22', 7500),
('PNR912003', 'Suman Verma', 12202, 'second', 7, '2025-07-30', 17500),
('PNR989758', 'nisarg', 12297, 'second', 93, '2025-07-22', 418500);

--
-- Triggers `ticket_info`
--
DELIMITER $$
CREATE TRIGGER `trg_after_ticket_booking` AFTER INSERT ON `ticket_info` FOR EACH ROW BEGIN
    UPDATE train 
    SET seats = seats - NEW.no_of_seats
    WHERE tnum = NEW.train_no;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `train`
--

CREATE TABLE `train` (
  `tnum` int(11) NOT NULL,
  `tname` varchar(100) DEFAULT NULL,
  `seats` int(11) DEFAULT NULL,
  `bp` varchar(50) DEFAULT NULL,
  `dp` varchar(50) DEFAULT NULL,
  `fAC` int(11) DEFAULT NULL,
  `sAC` int(11) DEFAULT NULL,
  `tAC` int(11) DEFAULT NULL,
  `sc` int(11) DEFAULT NULL,
  `doj` date DEFAULT NULL,
  `dtime` varchar(10) DEFAULT NULL,
  `atime` varchar(10) DEFAULT NULL,
  `sno` int(11) DEFAULT NULL,
  `stations` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `train`
--

INSERT INTO `train` (`tnum`, `tname`, `seats`, `bp`, `dp`, `fAC`, `sAC`, `tAC`, `sc`, `doj`, `dtime`, `atime`, `sno`, `stations`) VALUES
(11003, 'Mangala Express', 690, 'Goa', 'Delhi', 5305, 4285, 3125, 0, '2025-07-27', '10:00', '08:00', 19, 'Goa, Ratnagiri, Panvel, Vasai Road, Surat, Vadodara, Ratlam, Kota, Mathura, Delhi'),
(11411, 'Amritsar–Mumbai Express', 800, 'Amritsar', 'Mumbai', 5305, 4285, 3125, 0, '2025-07-29', '16:00', '04:00', 71, 'Amritsar, Jalandhar, Ludhiana, Ambala, Delhi, Jaipur, Kota, Vadodara, Surat, Mumbai'),
(11456, 'Delhi–Amritsar SF Express', 627, 'Amritsar', 'New Delhi', 5305, 4285, 3125, 0, '2025-07-29', '23:00', '06:00', 73, 'Amritsar, Jalandhar, Ludhiana, Ambala, New Delhi'),
(11457, 'Amritsar–Kolkata Express', 700, 'Amritsar', 'Kolkata', 5305, 4285, 3125, 0, '2025-07-29', '07:00', '22:00', 74, 'Amritsar, Jalandhar, Ludhiana, Ambala, Kanpur, Patna, Dhanbad, Asansol, Kolkata'),
(11720, 'Amritsar–Haridwar Express', 600, 'Amritsar', 'Haridwar', 4000, 3000, 2000, 0, '2025-07-29', '08:00', '18:00', 72, 'Amritsar, Jalandhar, Ludhiana, Ambala, Saharanpur, Haridwar'),
(11728, 'Amritsar–Lucknow Express', 650, 'Amritsar', 'Lucknow', 4300, 3000, 2000, 0, '2025-07-29', '09:00', '22:00', 75, 'Amritsar, Jalandhar, Ludhiana, Ambala, Kanpur, Lucknow'),
(12010, 'Mumbai–Ahmedabad Shatabdi', 492, 'Ahmedabad', 'Mumbai', 4200, 2500, 3000, 0, '2025-07-22', '15:10', '21:45', 4, 'Ahmedabad, Surat, Vadodara, Nadiad, Bharuch, Vapi, Mumbai'),
(12051, 'Goa Express', 600, 'Goa', 'Vasco', 3000, 1500, 1100, 0, '2025-07-26', '16:00', '22:00', 17, 'Goa, Madgaon, Vasco'),
(12121, 'Indore–Bhopal Intercity', 300, 'Bhopal', 'Indore', 2900, 1500, 1200, 0, '2025-07-25', '07:00', '10:00', 21, 'Bhopal, Sehore, Shujalpur, Ujjain, Indore'),
(12122, 'Habibganj SF Express', 400, 'Bhopal', 'Bilaspur', 5305, 4285, 3125, 0, '2025-07-25', '09:00', '19:00', 23, 'Bhopal, Jabalpur, Raipur, Bilaspur'),
(12135, 'Patna SF Express', 800, 'Mumbai Central', 'Patna', 5000, 3000, 2000, 0, '2025-07-24', '20:00', '15:00', 15, 'Mumbai, Surat, Vadodara, Kota, Kanpur, Patna'),
(12202, 'Lucknow–New Delhi AC SF', 643, 'Delhi', 'Lucknow', 3000, 2500, 1300, 0, '2025-07-30', '19:00', '23:00', 38, 'Delhi, Ghaziabad, Aligarh, Kanpur, Lucknow'),
(12215, 'Garib Rath Express', 800, 'Delhi', 'Chennai', 2100, 900, 700, 0, '2025-07-30', '22:00', '10:00', 36, 'Delhi, Agra, Bhopal, Nagpur, Vijayawada, Chennai'),
(12217, 'Konkan Kanya Express', 500, 'Goa', 'Mumbai', 4000, 3000, 2000, 0, '2025-07-26', '17:00', '05:00', 16, 'Goa, Ratnagiri, Panvel, Vasai Road, Mumbai'),
(12281, 'Goa Mail', 600, 'Goa', 'Chennai', 1000, 3000, 2000, 0, '2025-07-27', '14:00', '08:00', 20, 'Goa, Ratnagiri, Panvel, Vasai Road, Pune, Bangalore, Chennai'),
(12297, 'Pune Duronto (via ADI)', 490, 'Ahmedabad', 'Mumbai', 2500, 4500, 3400, 0, '2025-07-22', '22:30', '03:45', 5, 'Ahmedabad, Surat, Vadodara, Mumbai'),
(13243, 'gujarat mail', 456, 'ahmedabad', 'goa', 2350, 1575, 1100, NULL, '2025-08-23', '6:00', '12:50', NULL, 'surat,mumbai,madgaon'),
(120000, 'satabadi expresss', 89, 'ahmedabad', 'goa', 1200, 1500, 2000, NULL, '2025-09-01', '12:00', '11:30', NULL, 'Ahmedabad, Surat, Ratnagiri, Goa');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`sno`);

--
-- Indexes for table `passengers`
--
ALTER TABLE `passengers`
  ADD PRIMARY KEY (`PassengerId`),
  ADD UNIQUE KEY `EmailId` (`EmailId`);

--
-- Indexes for table `ticket_info`
--
ALTER TABLE `ticket_info`
  ADD PRIMARY KEY (`pnr_no`),
  ADD KEY `train_no` (`train_no`);

--
-- Indexes for table `train`
--
ALTER TABLE `train`
  ADD PRIMARY KEY (`tnum`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `sno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=126;

--
-- AUTO_INCREMENT for table `passengers`
--
ALTER TABLE `passengers`
  MODIFY `PassengerId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ticket_info`
--
ALTER TABLE `ticket_info`
  ADD CONSTRAINT `ticket_info_ibfk_1` FOREIGN KEY (`train_no`) REFERENCES `train` (`tnum`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
