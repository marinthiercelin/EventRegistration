<?php
$inc_path = get_include_path();
print "string " + $inc_path;

require_once __DIR__.'\..\persistence\PersistenceEventRegistration.php';
require_once __DIR__.'\..\model\RegistrationManager.php';
require_once __DIR__.'\..\model\Participant.php';
		
class PersistenceEventRegistrationTest extends PHPUnit_Framework_TestCase 
{
	protected $pm; 
	
	protected function setUp() 
	{
		$this->pm = new PersistenceEventRegistration(); 
	}
	
	protected function tearDown() 
	{
		
	}
	
	public function testPersistence() 
	{
		$rm = new RegistrationManager(); 
		$participant = new Participant("Frank");
		$rm->addParticipant($participant);
		
		$this->pm->writeDataToStore($rm); 
		
		$rm->delete(); 
		$this->assertEquals(0, count($rm->getParticipants()));
		
		$rm = $this->pm->loadDataFromStore(); 
		
		$this->assertEquals(1, count($rm->getParticipants()));
		$myParticipant = $rm->getParticipant_index(0); 
		$this->assertEquals("Frank", $myParticipant->getName()); 
	}
}

?>