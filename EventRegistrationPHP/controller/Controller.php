<?php
require_once __DIR__.'\..\controller\InputValidator.php';
require_once __DIR__.'\..\persistence\PersistenceEventRegistration.php';
require_once __DIR__.'\..\model\Event.php';
require_once __DIR__.'\..\model\Registration.php';
require_once __DIR__.'\..\model\RegistrationManager.php';
require_once __DIR__.'\..\model\Participant.php';




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
		$name = InputValidator::validate_input($event_name);
		$error = ""; 
		if ($name == null || strlen($name) == 0) {
			$error .= "@1Event name cannot be empty! ";
		} 
		if ($event_name == null || strlen($event_date) != 10) {
			$error .= "@2Event date must be specified correctly (YYYY-MM-DD)! ";
		}
		if($starttime == null || strlen($starttime) != 5) {
			$error .= "@3Event start time must be specified correctly (HH:MM)! ";
		}
		if($endtime == null || strlen($endtime) != 5) {
			$error .= "@4Event end time must be specified correctly (HH:MM)!";
		} 
		if($starttime != null && $endtime != null && strcmp($starttime, $endtime) > 0) {
			$error .= "@4Event end time cannot be before event start time!";
		}
		if(strlen($error) > 0) {
			throw new Exception($error); 
		
		} else {
				// 2. Load all the data
				$pm = new PersistenceEventRegistration();
				$rm = $pm->loadDataFromStore();
				
				// 3. Add the new event
				$event = new Event($name, $event_date, $starttime, $endtime);
				$rm->addEvent($event); 
				
				// 4. Write all of the data
				$pm->writeDataToStore($rm);
			}	
			
	}
	
	public function register($aParticipant, $aEvent) {
		// 1. Load all of the data
		$pm = new PersistenceEventRegistration(); 
		$rm = $pm->loadDataFromStore(); 
		
		// 2. Find the participant
		$myparticipant = NULL;
		foreach ($rm->getParticipants() as $participant) {
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
			if ($myevent == NULL) {
				$error .= "@2Event ";
				if ($aEvent != NULL) {
					$error .= $aEvent;
				}
				$error .= " not found! ";
			}
			throw new Exception(trim($error)); 
		}
		
	}
}