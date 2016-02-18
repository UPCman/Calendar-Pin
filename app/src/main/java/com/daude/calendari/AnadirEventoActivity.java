package com.daude.calendari;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AnadirEventoActivity extends ActionBarActivity {

    private DatePicker datePicker;
    private TimePicker timePickerInici;
    private TimePicker timePickerFi;
    private RadioButton reunionButton;
    private RadioButton tareaButton;
    private Button buttonAceptar;
    private EditText tituloText;
    private EditText descripcioText;
    private CheckBox checkRecursivoSemanal;
    private CheckBox checkRecursivoDiario;

    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_evento);

        //Recuperamos la información pasada en el intent
       bundle = this.getIntent().getExtras();

        //Obtener referencias
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        timePickerInici= (TimePicker)findViewById(R.id.timePickerInici);
        timePickerFi= (TimePicker)findViewById(R.id.timePickerFi);
        reunionButton = (RadioButton)findViewById(R.id.reunionButton);
        tareaButton = (RadioButton)findViewById(R.id.tareaButton);
        buttonAceptar = (Button)findViewById(R.id.buttonAceptar);
        tituloText = (EditText)findViewById(R.id.tituloText);
        descripcioText = (EditText)findViewById(R.id.descripcionText);
        checkRecursivoSemanal = (CheckBox)findViewById(R.id.checkRecursivoSemanal);
        checkRecursivoDiario = (CheckBox)findViewById(R.id.checkRecursivoDiario);

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAceptar();
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anadir_evento, menu);
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
            Intent intent = new Intent(AnadirEventoActivity.this, PopUpActivity.class);

            //Informacion a passar entre actividades
            Bundle b = new Bundle();
            b.putString("ERROR", "Calendar Pin [2016]\n\n" +
                    "Desarrollado con Android Studio 1.0.1 para Android 4.3 o superior.\n\n" +
                    "Desarrollado por Sergi Sánchez Centelles para la práctica de Android para la asignatura IDI en la FIB de la UPC.");
            intent.putExtras(b);

            //Iniciamos la nueva actividad
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_help) {
            Intent intent = new Intent(AnadirEventoActivity.this, PopUpActivity.class);

            //Informacion a passar entre actividades
            Bundle b = new Bundle();
            b.putString("ERROR", "En el Menú de Añadir Evento puede seleccionar todos los atributos del evento que desea programar de forma intuitiva.\n" +
                    "Opciones de Recursividad:\n\n" +
                    "-\tRepetición Diaria: Repetirá evento cada día durante una semana.\n\n" +
                    "-\tRepetir una semana: Repetirá evento una vez a la semana 10 veces.\n\n" +
                    "Pulse el botón “Atrás” de su dispositivo para volver a la visión semanal.");
            intent.putExtras(b);

            //Iniciamos la nueva actividad
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickAceptar(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePickerInici.getCurrentHour(), timePickerInici.getCurrentMinute(), 0);
        long timeStart  = calendar.getTimeInMillis();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePickerFi.getCurrentHour(), timePickerFi.getCurrentMinute(), 0);
        long timeEnd  = calendar.getTimeInMillis();

        Editable descripcio = descripcioText.getText();
        Editable titulo = tituloText.getText();
        String nombre;

        if(reunionButton.isChecked()){
            nombre = "Reunión " + titulo.toString();
        }
        else nombre = "Tarea " + titulo.toString();

        if(!checkRecursivoDiario.isChecked() && !checkRecursivoSemanal.isChecked()) { // Añadir Evento puntual
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, timeStart);
            values.put(CalendarContract.Events.DTEND, timeEnd);
            values.put(CalendarContract.Events.TITLE, nombre);
            values.put(CalendarContract.Events.DESCRIPTION, descripcio.toString());
            values.put(CalendarContract.Events.CALENDAR_ID, bundle.getLong("IDCAL"));
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }
        if(checkRecursivoSemanal.isChecked()){
            String rrule = "FREQ=WEEKLY;COUNT=" + 4;
            int duracionMinutos = (int) (1000 / (1000 * 60));
            String duracion = "P" + duracionMinutos + "M";
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, timeStart);
            values.put(CalendarContract.Events.DURATION, duracion);
            values.put(CalendarContract.Events.TITLE, nombre);
            values.put(CalendarContract.Events.DESCRIPTION, descripcio.toString());
            values.put(CalendarContract.Events.CALENDAR_ID, bundle.getLong("IDCAL"));
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");
            values.put(CalendarContract.Events.RRULE, rrule);
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }
        if(checkRecursivoDiario.isChecked()){
            String rrule = "FREQ=DAILY;COUNT=" + 7;
            int duracionMinutos = (int) (1000 / (1000 * 60));
            String duracion = "P" + duracionMinutos + "M";
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, timeStart);
            values.put(CalendarContract.Events.DURATION, duracion);
            values.put(CalendarContract.Events.TITLE, nombre);
            values.put(CalendarContract.Events.DESCRIPTION, descripcio.toString());
            values.put(CalendarContract.Events.CALENDAR_ID, bundle.getLong("IDCAL"));
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");
            values.put(CalendarContract.Events.RRULE, rrule);
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }

    }


}
