<?php

require_once '../includes/DbOperationsAdmin.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['email']) and isset($_POST['password'])) {
        $db = new DbOperationsAdmin();
        if ($db->admin_login($_POST['email'], $_POST['password'])) {
            $response['error'] = false;
            $response['message'] = "Successfully login";
        } else {
            $response['error'] = true;
            $response['message'] = "Invalid username or password";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
}

echo json_encode($response);
