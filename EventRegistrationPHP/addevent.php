<?php
require_once 'controller/Controller.php';

session_start ();
$_SESSION ["errorEventName"] = "";
$_SESSION ["errorEventDate"] = "";
$_SESSION ["errorStartTime"] = "";
$_SESSION ["errorEndTime"] = "";

$c = new Controller ();
try {
	$c->createEvent ( $_POST ['event_name'] , $_POST ['event_date'], $_POST ['starttime'], $_POST ['endtime'] );
} catch ( Exception $e ) {
	$errors = explode("@", $e->getMessage());
	
	foreach ($errors as $error) {
		if (substr($error, 0, 1) == 1) {
			$_SESSION ["errorEventName"] = substr($error, 1);
		}
		if (substr($error, 0, 1) == 2) {
			$_SESSION ["errorEventDate"] = substr($error, 1);
		}
		if (substr($error, 0, 1) == 3) {
			$_SESSION ["errorStartTime"] = substr($error, 1);
		}
		if (substr($error, 0, 1) == 4) {
			$_SESSION ["errorEndTime"] = substr($error, 1);
		}
		
	}
}
?>

<!DOCTYPEhtml>
<html>
	<head>
		<meta http-equiv="refresh" content="0; url=/EventRegistrationPHP/" />
	</head>
</html>