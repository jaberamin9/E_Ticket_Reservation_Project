<?php
require_once '../includes/DbOperations.php';

$response = [];
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['bus_no']) and isset($_POST['p_id']) and isset($_POST['seat'])) {
        $db = new DbOperations();
        $ticket = $db->getBusTiket($_POST['bus_no'], $_POST['p_id'], $_POST['seat']);
        if ($ticket == 1) {
            $response['error'] = false;
            $response['message'] = "Successfully buy a ticket";
        } else if ($ticket == 2) {
            $response['error'] = true;
            $response['message'] = "can not buy a ticket";
        } else {
            $response['error'] = true;
            $response['message'] = "no more seat available";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
}
echo json_encode($response);
