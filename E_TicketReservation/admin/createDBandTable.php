<?php
require_once '../includes/DbConnect.php';
include_once '../includes/Constants.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    mysqli_query(mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD), "CREATE DATABASE IF NOT EXISTS etiket");
    $db = new DbConnect();
    $con = $db->connect();
    mysqli_select_db($con, "etiket");
    mysqli_query($con, "CREATE TABLE IF NOT EXISTS passenger (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(100) NOT NULL,
        password VARCHAR(30) NOT NULL,
        email VARCHAR(50) NOT NULL
        )");
    mysqli_query($con, "CREATE TABLE IF NOT EXISTS bus_and_shedual (
        bus_no INT AUTO_INCREMENT PRIMARY KEY,
        starting_point VARCHAR(100) NOT NULL,
        ending_point VARCHAR(100) NOT NULL,
        starting_time datetime NOT NULL,
        arrival_time datetime NOT NULL,
        seat_available INT NOT NULL,
        t_price float NOT NULL,
        row int NOT NULL
        )");
    mysqli_query($con, "CREATE TABLE IF NOT EXISTS booked_seats (
        id INT AUTO_INCREMENT PRIMARY KEY,
        bus_no INT NOT NULL,
        p_id INT NOT NULL,
        seat VARCHAR(10) NOT NULL,
        booked_date	 datetime NOT NULL,
        validity datetime NOT NULL,
        FOREIGN KEY (bus_no) REFERENCES bus_and_shedual(bus_no),
        FOREIGN KEY (p_id) REFERENCES passenger(id)
        )");
    mysqli_query($con, "CREATE TABLE IF NOT EXISTS admins (
        id INT AUTO_INCREMENT PRIMARY KEY,
        email VARCHAR(100) NOT NULL,
        password VARCHAR(100) NOT NULL
        )");
    $result = mysqli_query($con, "INSERT INTO `admins` (`id`, `email`, `password`) VALUES (NULL, 'admin', 'admin');");

    if ($result === TRUE) {
        $response['error'] = false;
        $response['message'] = "Successfully Create Database and Table";
    } else {
        $response['error'] = true;
        $response['message'] = "can not create database and table";
    }
}

echo json_encode($response);
