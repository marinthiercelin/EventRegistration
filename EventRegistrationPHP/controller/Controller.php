<?php
require_once__DIR__.'\..\controller\InputValidator.php';
require_once__DIR__.'\..\persistence\PersistenceEventRegistration.php';
require_once__DIR__.'\..\model\Event.php';
require_once__DIR__.'\..\model\Registration.php';
require_once__DIR__.'\..\model\RegistrationManager.php';
require_once__DIR__.'\..\model\Participant.php';




class Controller
{
	public function __construct()
	{
	}
	
	public function createParticipant($participant_name) {
		$name = InputValidator::validate_input($participant_name); 
		if ($name == null || strlen($name) == 0) {
			throw new Exception("Participant name cannot be empty!");
		} else {
			// 2. Load all the data
			$pm = new PersistenceEventRegistration();
			$rm = $pm->loadDataFromStore(); 
			
			// 3. Add the new participant
			$participant = new Participant($name); 
			$rm->addParticipant($participant); 
			
			// 4. Write all of the data
			$pm->writeDataToStore($rm); 
		}
	}
	
	public function createEvent($event_name, $event_date, $starttime, $endtime) {
		
	}
	
	public function register($aParticipant, $aEvent) {
		// 1. Load all of the data
		$pm = new PersistenceEventRegistration(); 
		$rm = $pm->loadDataFromStore(); 
		
		// 2. Find the participant
		$myparticipant = NULL;
		foreach ($rm->getParticipant() as $participant) {
			if (strcmp($participant->getName(), $aParticipant) == 0) {
				$myparticipant = $participant; 
				break; 
			}
		}
		
		// 3. Find the event
		$myevent = NULL; 
		foreach ($rm->getEvents() as $event) {
			if (strcmp($event->getName(), $aEvent) == 0) {
				$myevent = $event; 
				break; 
			}
		}
		
		// 4. Register for the event
		$error = ""; 
		if ($myparticipant != NULL && $myevent != NULL) {
			$myregistration = new Registration($myparticipant, $myevent); 
			$rm->addRegistration($myregistration); 
			$pm->writeDataToStore($rm); 
		} else {
			if ($myparticipant == NULL) {
				$error .= "@1Participant "; 
				if ($aParticipant != NULL) {
					$error .= $aParticipant; 
				}
				$error .= " not found! ";
			}
			throw new Exception(trim($error)); 
		}
		
	}
}