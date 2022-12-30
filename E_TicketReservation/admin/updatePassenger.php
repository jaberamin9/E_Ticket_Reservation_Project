<?php
require_once '../includes/DbOperationsAdmin.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $db = new DbOperationsAdmin();
    $check = $db->UpdatePassenger($_POST['id'], $_POST['username'], $_POST['email'], $_POST['password']);
    if ($check == 1) {
        $response['error'] = false;
        $response['message'] = "Successfully update";
    } else {
        $response['error'] = true;
        $response['message'] = "can not update successfully";
    }
}

echo json_encode($response);
