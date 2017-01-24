package ca.mcgill.ecse321.eventregistration.view;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import ca.mcgill.ecse321.eventregistration.controller.EventRegistrationController;
import ca.mcgill.ecse321.eventregistration.controller.InvalidInputException;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;

public class ParticipantPage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6398301441623263485L;
	// attributes for GUI elements
	private JTextField participantNameTextField;
	private JLabel participantNameLabel;
	private JButton addParticipantButton;
	
	private String error = null; 
	private JLabel errorMessage; 
	
	private RegistrationManager rm;

	/** Creates new form ParticipantPage */
	public ParticipantPage(RegistrationManager rm) {
	    this.rm = rm;
	    initComponents();
	}
	
	private void initComponents() {
		this.participantNameLabel = new JLabel();
		this.participantNameTextField = new JTextField();
		this.addParticipantButton = new JButton();

		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Event Registration");
		participantNameLabel.setText("Name: ");
		addParticipantButton.setText("Add Participant");
		
		addParticipantButton.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent evt) {
		        addParticipantButtonActionPerformed();
		    }
		});
		
		 // layout
	    GroupLayout layout = new GroupLayout(getContentPane());
	    getContentPane().setLayout(layout);
	    layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);

	    layout.setHorizontalGroup(
	            layout.createParallelGroup()
	            .addComponent(errorMessage)
	            .addGroup(layout.createSequentialGroup()
	            .addComponent(participantNameLabel)
	            .addGroup(layout.createParallelGroup()
	                .addComponent(participantNameTextField, 200, 200, 400)
	                .addComponent(addParticipantButton))
	            ));

	    layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] {addParticipantButton, participantNameTextField});

	    layout.setVerticalGroup(
	            layout.createSequentialGroup()
	            .addComponent(errorMessage)
	            .addGroup(layout.createParallelGroup()
	                .addComponent(participantNameLabel)
	                .addComponent(participantNameTextField))
	            .addComponent(addParticipantButton)
	            );

	    pack();
	}
	
	private void refreshData() {
		errorMessage.setText(error); 
		if(error == null || error.length() == 0) {
			participantNameTextField.setText("");
		}
		pack();
	}
	
	private void addParticipantButtonActionPerformed() {
		// create and call the controller
		EventRegistrationController erc = new EventRegistrationController(rm);
		error = null; 
		try {
			erc.createParticipant(participantNameTextField.getText());
		} catch (InvalidInputException e) {
			error = e.getMessage(); 
		}
		refreshData();
	}
}