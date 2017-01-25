package ca.mcgill.ecse321.eventregistrationandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

import ca.mcgill.ecse321.eventregistration.controller.EventRegistrationController;
import ca.mcgill.ecse321.eventregistration.controller.InvalidInputException;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;
import ca.mcgill.ecse321.eventregistration.persistence.PersistenceXStream;

public class MainActivity extends AppCompatActivity {

    private RegistrationManager rm = null;
    private String fileName;
    String error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Initialize file name and XStream
        fileName = getFilesDir().getAbsolutePath() + "/eventregistration.xml";
        rm = PersistenceXStream.initializeModelManager(fileName);
        refreshData();
    }

    private void refreshData() {
        TextView tv = (TextView) findViewById(R.id.newparticipant_name);
        tv.setText("");
        // Initialize the data in the participant spinner
        Spinner spinner = (Spinner) findViewById(R.id.participantspinner);
        ArrayAdapter<CharSequence> participantAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        participantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Participant p: rm.getParticipants() ) {
            participantAdapter.add(p.getName());
        }
        spinner.setAdapter(participantAdapter);
        // Initialize the data in the event spinner
        Spinner spinner_ev = (Spinner) findViewById(R.id.eventspinner);
        ArrayAdapter<CharSequence> eventAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Event e: rm.getEvents() ) {
            eventAdapter.add(e.getName());
        }
        spinner_ev.setAdapter(eventAdapter);

        TextView errTv = (TextView) findViewById(R.id.error_message);
        errTv.setText(error);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addParticipant(View v) {
        TextView tv = (TextView) findViewById(R.id.newparticipant_name);
        EventRegistrationController pc = new EventRegistrationController(rm);
        try {
            pc.createParticipant(tv.getText().toString());
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }
        refreshData();
    }

    public void addEvent(View v) {
        EventRegistrationController pc = new EventRegistrationController(rm);

        TextView name_tv = (TextView) findViewById(R.id.newevent_name);
        TextView date_tv = (TextView) findViewById(R.id.newevent_date);
        TextView start_time_tv = (TextView) findViewById(R.id.newevent_start_time);
        TextView end_time_tv = (TextView) findViewById(R.id.newevent_end_time);
        String name = name_tv.getText().toString();
        String[] date_tab = date_tv.getText().toString().split("-");
        String[] start_time_tab = start_time_tv.getText().toString().split(":");
        String[] end_time_tab = end_time_tv.getText().toString().split(":");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MONTH, Integer.parseInt(date_tab[0]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date_tab[1]));
        calendar.set(Calendar.YEAR, Integer.parseInt(date_tab[2]));
        calendar.set(Calendar.HOUR, Integer.parseInt(start_time_tab[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(start_time_tab[1]));
        Time startTime = new Time(calendar.getTime().getTime());
        Date date = new Date(calendar.getTime().getTime());
        calendar.set(Calendar.HOUR, Integer.parseInt(end_time_tab[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(end_time_tab[1]));
        Time endTime = new Time(calendar.getTime().getTime());

        try {
            pc.createEvent(name, date, startTime, endTime);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }
        refreshData();
    }

    public void addRegistration(View v){

        EventRegistrationController pc = new EventRegistrationController(rm);

        Spinner nameSpinner = (Spinner) findViewById(R.id.participantspinner);
        Spinner eventSpinner = (Spinner) findViewById(R.id.eventspinner);
        String participantName = nameSpinner.getSelectedItem().toString();
        String eventName = eventSpinner.getSelectedItem().toString();
        Participant participant = null;
        Event event = null;
        for ( Participant part : rm.getParticipants()){
            if (participant.getName() == participantName){
                participant = part;
            }
        }
        for (Event ev : rm.getEvents()){
            if (ev.getName() == eventName){
                event = ev;
            }
        }

        try {
            pc.register(participant, event);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }
        refreshData();
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText());
        args.putInt("id", v.getId());

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText());
        args.putInt("id", v.getId());

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private Bundle getTimeFromLabel(CharSequence text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;

        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }

        rtn.putInt("hour", hour);
        rtn.putInt("minute", minute);

        return rtn;
    }

    private Bundle getDateFromLabel(CharSequence text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");
        int day = 1;
        int month = 1;
        int year = 1;

        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }

        rtn.putInt("day", day);
        rtn.putInt("month", month-1);
        rtn.putInt("year", year);

        return rtn;
    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d", h, m));
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));
    }
}
