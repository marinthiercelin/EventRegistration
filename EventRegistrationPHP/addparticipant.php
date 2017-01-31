<?php
require_once 'controller/Controller.php';

session_start ();

$c = new Controller ();
try {
	$c->createParticipant ( $_POST ['participant_name'] );
	$_SESSION ["errorParticipantName"] = "";
} catch ( Exception $e ) {
	$_SESSION ["errorParticipantName"] = $e->getMessage ();
}
?>

<!DOCTYPEhtml>
<html>
	<head>
		<meta http-equiv="refresh" content="0; url=/EventRegistrationPHP/" />
	</head>
</html>