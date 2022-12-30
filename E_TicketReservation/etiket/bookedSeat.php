<?php
require_once '../includes/DbOperations.php';

$response = [];
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $db = new DbOperations();
    $list = $db->getBookedSeat($_POST['bus_no']);
    // if (sizeof($list, 1) > 0) {
    //     $response['error'] = false;
    $response["seat"] =  $list;
    // } else {
    //     $response['error'] = true;
    //     $response['message'] = "No data found";
    // }
}
echo json_encode($response);
