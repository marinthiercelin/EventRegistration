package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;
import ca.mcgill.ecse321.eventregistration.persistence.PersistenceXStream;

public class EventRegistrationController {
	private RegistrationManager rm;

	public EventRegistrationController(RegistrationManager rm){
		this.rm = rm;
	}

	public void createParticipant(String name) throws InvalidInputException {
		if (name == null || name.trim().length() == 0) {
			throw new InvalidInputException("Participant name cannot be empty!");
		}
		Participant p = new Participant(name);
		rm.addParticipant(p);
		PersistenceXStream.saveToXMLwithXStream(rm);
	}
	
	public void createEvent(String name, Date date, Time startTime, Time endTime) throws InvalidInputException {
		Event e = new Event(name, date, startTime, endTime);
	    rm.addEvent(e);
	    PersistenceXStream.saveToXMLwithXStream(rm);
	}
}
