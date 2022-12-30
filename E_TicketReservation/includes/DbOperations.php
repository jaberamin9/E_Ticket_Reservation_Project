<?php

class DbOperations
{

	private $con;

	function __construct()
	{

		require_once dirname(__FILE__) . '/DbConnect.php';

		$db = new DbConnect();

		$this->con = $db->connect();
	}

	/*CRUD -> C -> CREATE */

	public function createUser($username, $pass, $email)
	{
		if ($this->isUserExist($email)) {
			return 0;
		} else {
			$stmt = $this->con->prepare("INSERT INTO `passenger` (`id`, `username`, `password`, `email`) VALUES (NULL, ?, ?, ?);");
			$stmt->bind_param("sss", $username, $pass, $email);

			if ($stmt->execute()) {
				return 1;
			} else {
				return 2;
			}
		}
	}

	public function userLogin($email, $pass)
	{
		$stmt = $this->con->prepare("SELECT id FROM passenger WHERE email = ? AND password = ?");
		$stmt->bind_param("ss", $email, $pass);
		$stmt->execute();
		$stmt->store_result();
		return $stmt->num_rows > 0;
	}

	public function getUserByEmail($email)
	{
		$stmt = $this->con->prepare("SELECT * FROM passenger WHERE email = ?");
		$stmt->bind_param("s", $email);
		$stmt->execute();
		return $stmt->get_result()->fetch_assoc();
	}


	private function isUserExist($email)
	{
		$stmt = $this->con->prepare("SELECT id FROM passenger WHERE email = ?");
		$stmt->bind_param("s", $email);
		$stmt->execute();
		$stmt->store_result();
		return $stmt->num_rows > 0;
	}
	public function getBusList($orderBy, $type)
	{
		$result = mysqli_query($this->con, "SELECT * FROM bus_and_shedual ORDER BY $orderBy $type");

		$data = array(array());
		$cnt = 0;
		if (mysqli_num_rows($result) > 0) {
			while ($row = mysqli_fetch_assoc($result)) {
				$temp = array();
				$temp["bus_no"] = $row["bus_no"];
				$temp["starting_point"] = $row["starting_point"];
				$temp['ending_point'] = $row['ending_point'];
				$temp['starting_time'] = $row['starting_time'];
				$temp['arrival_time'] = $row['arrival_time'];
				$temp['seat_available'] = $row['seat_available'];
				$temp['t_price'] = $row['t_price'];
				$temp['row'] = $row['row'];
				$data[$cnt] = $temp;
				$cnt = $cnt + 1;
			}
			return $data;
		} else {
			return $data;
		}
	}

	public function getBusTiket($bus_no, $p_id, $seat)
	{
		$result = mysqli_query($this->con, "SELECT seat_available FROM bus_and_shedual WHERE bus_no = $bus_no");
		$row = mysqli_fetch_assoc($result);

		if ($row["seat_available"] != 0) {
			$row["seat_available"] = $row["seat_available"] - 1;
			$uv = $row["seat_available"];
			mysqli_query($this->con, "UPDATE bus_and_shedual SET seat_available = $uv WHERE bus_no = $bus_no");

			$stmt = $this->con->prepare("INSERT INTO `booked_seats` (`id`, `bus_no`, `p_id`, `seat`, `booked_date`, `validity`) VALUES (NULL, ?, ?, ?, ?, ?);");
			date_default_timezone_set("asia/dhaka");
			$ctime = date("Y-m-d h:i:sa");
			$date = new DateTime($ctime);
			$date->modify('+1 day');
			$date = $date->format("Y-m-d h:i:sa");
			$stmt->bind_param("sssss", $bus_no, $p_id, $seat, $ctime, $date);

			if ($stmt->execute()) {
				return 1;
			} else {
				return 2;
			}
		} else {
			return 3;
		}
	}

	public function getBookedSeat($bus_no)
	{
		$result = mysqli_query($this->con, "SELECT * FROM booked_seats WHERE bus_no = $bus_no");
		$data = array();
		if (mysqli_num_rows($result) > 0) {
			while ($row = mysqli_fetch_assoc($result)) {
				array_push($data, $row["seat"]);
			}
			return $data;
		} else {
			return $data;
		}
	}
	public function getBookedSeatByUser($p_id, $bus_no)
	{
		$result = mysqli_query($this->con, "SELECT * FROM booked_seats WHERE p_id = $p_id and bus_no = $bus_no");
		$data = array();
		if (mysqli_num_rows($result) > 0) {
			while ($row = mysqli_fetch_assoc($result)) {
				array_push($data, $row["seat"]);
			}
			return $data;
		} else {
			return $data;
		}
	}

	public function getLoginUserData($email)
	{

		$result = mysqli_query($this->con, "SELECT * FROM passenger WHERE email = '$email'");
		$data = array();
		if (mysqli_num_rows($result) > 0) {
			while ($row = mysqli_fetch_assoc($result)) {
				array_push($data, $row["id"]);
				array_push($data, $row["username"]);
			}
			return $data;
		} else {
			return $data;
		}
	}

	public function getBookedHistoryByUser($p_id)
	{

		$result = mysqli_query(
			$this->con,
			"SELECT * FROM bus_and_shedual LEFT JOIN booked_seats ON
			bus_and_shedual.bus_no = booked_seats.bus_no WHERE booked_seats.p_id = $p_id
			UNION
			SELECT * FROM bus_and_shedual RIGHT JOIN booked_seats ON
			bus_and_shedual.bus_no = booked_seats.bus_no WHERE booked_seats.p_id = $p_id
			ORDER BY booked_date ASC"
		);

		$data = array(array());
		$cnt = 0;
		if (mysqli_num_rows($result) > 0) {
			while ($row = mysqli_fetch_assoc($result)) {
				$temp = array();
				$temp["bus_no"] = $row["bus_no"];
				$temp["starting_point"] = $row["starting_point"];
				$temp['ending_point'] = $row['ending_point'];
				$temp['t_price'] = $row['t_price'];
				$temp['seat'] = $row['seat'];
				$temp['booked_date'] = $row['booked_date'];
				$temp['validity'] = $row['validity'];
				$data[$cnt] = $temp;
				$cnt = $cnt + 1;
			}
			return $data;
		} else {
			return $data;
		}
	}
}
