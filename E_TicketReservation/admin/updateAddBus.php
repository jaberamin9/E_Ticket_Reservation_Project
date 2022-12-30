<?php
require_once '../includes/DbOperationsAdmin.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $db = new DbOperationsAdmin();
    $check = $db->UpdateAddBusAndShedual($_POST['bus_no'], $_POST['starting_point'], $_POST['ending_point'], $_POST['starting_time'], $_POST['arrival_time'], $_POST['seat_available'], $_POST['t_price'], $_POST['row']);
    if ($check == 1) {
        $response['error'] = false;
        $response['message'] = "Successfully update";
    } else {
        $response['error'] = true;
        $response['message'] = "can not update successfully";
    }
}

echo json_encode($response);
