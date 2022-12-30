<?php
require_once '../includes/DbOperations.php';

$response = [];
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $db = new DbOperations();
    $list = $db->getBusList($_POST['orderBy'], $_POST['type']);
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
}
echo json_encode($response);
