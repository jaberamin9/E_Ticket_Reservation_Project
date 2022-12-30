<?php
require_once '../includes/DbOperationsAdmin.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $db = new DbOperationsAdmin();
    $check = $db->DeletePassenger($_POST['id']);
    if ($check == 1) {
        $response['error'] = false;
        $response['message'] = "Successfully delete";
    } else {
        $response['error'] = true;
        $response['message'] = "can not delete successfully";
    }
}

echo json_encode($response);
