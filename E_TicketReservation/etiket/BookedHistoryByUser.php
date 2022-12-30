<?php
require_once '../includes/DbOperations.php';

$response = [];
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['p_id'])) {
        $db = new DbOperations();
        $list = $db->getBookedHistoryByUser($_POST['p_id']);
        if (sizeof($list, 1) > 1) {
            $response['error'] = false;
            $cnt = 0;
            foreach ($list as $y) {
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
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
}
echo json_encode($response);
