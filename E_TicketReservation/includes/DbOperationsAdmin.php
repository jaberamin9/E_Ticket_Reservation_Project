<?php

use function PHPSTORM_META\map;

class DbOperationsAdmin
{
    private $con;
    function __construct()
    {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }
    public function admin_login($email, $pass)
    {
        $res = $this->con->prepare("SELECT id FROM admins WHERE email = ? AND password = ?");
        $res->bind_param("ss", $email, $pass);
        $res->execute();
        $res->store_result();
        return $res->num_rows > 0;
    }

    public function AddBusAndShedual($starting_point, $ending_point, $starting_time, $arrival_time, $seat_available, $t_price, $row)
    {
        $res = $this->con->prepare("INSERT INTO `bus_and_shedual` (`bus_no`, `starting_point`, `ending_point`, `starting_time`, `arrival_time`, `seat_available`, `t_price`, `row`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?);");
        $res->bind_param("sssssss", $starting_point, $ending_point, $starting_time, $arrival_time, $seat_available, $t_price, $row);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }

    public function UpdateAddBusAndShedual($bus_no, $starting_point, $ending_point, $starting_time, $arrival_time, $seat_available, $t_price, $row)
    {
        $res = $this->con->prepare("UPDATE bus_and_shedual SET starting_point = ?, ending_point = ?, starting_time = ?, arrival_time = ? , seat_available = ? , t_price = ? , row = ? WHERE bus_no = $bus_no ;");
        $res->bind_param("sssssss", $starting_point, $ending_point, $starting_time, $arrival_time, $seat_available, $t_price, $row);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }
    public function DeleteAddBusAndShedual($bus_no)
    {
        $res = $this->con->prepare("DELETE FROM bus_and_shedual WHERE bus_no = ? ;");
        $res->bind_param("s", $bus_no);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }

    public function UpdatePassenger($id, $username, $email, $password)
    {
        $res = $this->con->prepare("UPDATE passenger SET username = ?, email = ?, password = ? WHERE id = $id ;");
        $res->bind_param("sss", $username, $email, $password);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }
    public function DeletePassenger($id)
    {
        $res = $this->con->prepare("DELETE FROM passenger WHERE id = ? ;");
        $res->bind_param("s", $id);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }
    public function GetAllPassenger()
    {
        $result = mysqli_query($this->con, "SELECT * FROM passenger");

        $data = array(array());
        $cnt = 0;
        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                $temp = array();
                $temp["id"] = $row["id"];
                $temp["username"] = $row["username"];
                $temp['email'] = $row['email'];
                $temp['password'] = $row['password'];
                $data[$cnt] = $temp;
                $cnt = $cnt + 1;
            }
            return $data;
        } else {
            return $data;
        }
    }

    public function GetAllBookedSeat()
    {
        $result = mysqli_query($this->con, "SELECT * FROM booked_seats");

        $data = array(array());
        $cnt = 0;
        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                $temp = array();
                $temp["id"] = $row["id"];
                $temp["bus_no"] = $row["bus_no"];
                $temp['p_id'] = $row['p_id'];
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
    public function updateBookedSeat($id, $bus_no, $p_id, $seat, $booked_date, $validity)
    {
        $res = $this->con->prepare("UPDATE booked_seats SET bus_no = ?, p_id = ?, seat = ?, booked_date = ? , validity = ? WHERE id = $id ;");
        $res->bind_param("sssss", $bus_no, $p_id, $seat, $booked_date, $validity);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }

    public function deleteBookedSeat($id, $bus_no)
    {

        $result = mysqli_query($this->con, "SELECT seat_available FROM bus_and_shedual WHERE bus_no = $bus_no");
        $row = mysqli_fetch_assoc($result);
        $seat = $row["seat_available"];
        $seat += 1;
        mysqli_query($this->con, "UPDATE `bus_and_shedual` SET `seat_available`= $seat WHERE bus_no = $bus_no");

        $res = $this->con->prepare("DELETE FROM booked_seats WHERE id = ? ;");
        $res->bind_param("s", $id);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }

    public function deleteBookedSeatByPassenger($p_id)
    {
        $resultBus = mysqli_query($this->con, "SELECT bus_no FROM booked_seats WHERE p_id = $p_id");

        if (mysqli_num_rows($resultBus) > 0) {
            $data = array();
            while ($rowBus = mysqli_fetch_assoc($resultBus)) {
                array_push($data, $rowBus["bus_no"]);
            }

            $map = array_count_values($data);
            for ($i = 1; $i <= count($map); $i++) {
                $result = mysqli_query($this->con, "SELECT seat_available FROM bus_and_shedual WHERE bus_no = $i");
                $row = mysqli_fetch_assoc($result);
                $seat = $row["seat_available"];
                $seat += $map[$i];
                mysqli_query($this->con, "UPDATE `bus_and_shedual` SET `seat_available`= $seat WHERE bus_no = $i");
            }
        }

        $res = $this->con->prepare("DELETE FROM booked_seats WHERE p_id = ? ;");
        $res->bind_param("s", $p_id);
        if ($res->execute()) {
            return 1;
        } else {
            return 2;
        }
    }

    public function deleteBookedSeatByBus($bus_no)
    {
        $resultBus = mysqli_query($this->con, "SELECT bus_no FROM booked_seats WHERE bus_no = $bus_no");
        if (mysqli_num_rows($resultBus) > 0) {
            $result = mysqli_query($this->con, "SELECT seat_available FROM bus_and_shedual WHERE bus_no = $bus_no");
            $row = mysqli_fetch_assoc($result);
            $seat = $row["seat_available"];
            $seat += mysqli_num_rows($resultBus);
            mysqli_query($this->con, "UPDATE `bus_and_shedual` SET `seat_available`= $seat WHERE bus_no = $bus_no");
        }

        $res = $this->con->prepare("DELETE FROM booked_seats WHERE bus_no = ? ;");
        $res->bind_param("s", $bus_no);
        if ($stmt->execute()) {
            return 1;
        } else {
            return 2;
        }
    }
}
