<?php
require_once '../includes/DbOperations.php';

$response = [];
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['email'])) {
        $db = new DbOperations();
        $list = $db->getLoginUserData($_POST['email']);
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
