<?php
require_once '../includes/DbOperations.php';

$response = [];
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['p_id']) and isset($_POST['bus_no'])) {
        $db = new DbOperations();
        $list = $db->getBookedSeatByUser($_POST['p_id'], $_POST['bus_no']);
        if (sizeof($list, 1) > 0) {
            $response['error'] = false;
            $response["info"] =  $list;
        } else {
            $response['error'] = true;
            $response['message'] = "No data found";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
}
echo json_encode($response);
