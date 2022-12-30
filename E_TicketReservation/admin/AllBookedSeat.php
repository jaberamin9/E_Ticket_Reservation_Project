<?php
require_once '../includes/DbOperationsAdmin.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $db = new DbOperationsAdmin();
    $user = $db->GetAllBookedSeat();
    if (sizeof($user, 1) > 1) {
        $response['error'] = false;
        $cnt = 0;
        foreach ($user as $y) {
            $temp = [];
            foreach ($y as $x => $x_value) {
                $temp[$x] = $x_value;
            }
            $response[$cnt] =  $temp;
            $cnt = $cnt + 1;
        }
    } else {
        $response['error'] = true;
        $response['message'] = "No data found";
    }
}

echo json_encode($response);
