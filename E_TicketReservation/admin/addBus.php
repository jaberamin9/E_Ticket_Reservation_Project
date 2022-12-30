<?php
require_once '../includes/DbOperationsAdmin.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['starting_point']) and isset($_POST['ending_point']) and isset($_POST['starting_time']) and isset($_POST['arrival_time']) and isset($_POST['seat_available']) and isset($_POST['t_price'])) {
        $db = new DbOperationsAdmin();
        $check = $db->AddBusAndShedual($_POST['starting_point'], $_POST['ending_point'], $_POST['starting_time'], $_POST['arrival_time'], $_POST['seat_available'], $_POST['t_price'], $_POST['row']);
        if ($check == 1) {
            $response['error'] = false;
            $response['message'] = "Successfully add";
        } else {
            $response['error'] = true;
            $response['message'] = "can not add successfully";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
}

echo json_encode($response);
