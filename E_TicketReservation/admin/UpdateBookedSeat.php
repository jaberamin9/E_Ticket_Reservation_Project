<?php
require_once '../includes/DbOperationsAdmin.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $db = new DbOperationsAdmin();
    $check = $db->updateBookedSeat($_POST['id'], $_POST['bus_no'], $_POST['p_id'], $_POST['seat'], $_POST['booked_date'], $_POST['validity']);
    if ($check == 1) {
        $response['error'] = false;
        $response['message'] = "Successfully update";
    } else {
        $response['error'] = true;
        $response['message'] = "can not update successfully";
    }
}

echo json_encode($response);
