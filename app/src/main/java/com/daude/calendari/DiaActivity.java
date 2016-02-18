package com.daude.calendari;

import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class DiaActivity extends ActionBarActivity {

    private TextView txtLlistaEventos;
    private TextView txtLlistaTareas;
    private String dataActual;
    private String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia);

        txtLlistaEventos = (TextView)findViewById(R.id.txtLlistaEventos);
        txtLlistaTareas = (TextView)findViewById(R.id.txtLlistaTareas);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        //Construimos el titulo
        setTitle(bundle.getString("DIA") + "-" + bundle.getString("NUMERO") + "-" + bundle.getString("MES"));
        dataActual = bundle.getString("NUMERO") + "/" + bundle.getString("NUMMES") + "/" + bundle.getString("ANO");

        ArrayList<String> eventos = new ArrayList();
        ArrayList<String> diaeventos = new ArrayList();

        String[] projection =
                new String[]{
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Events.CONTENT_URI,
                                projection,
                                CalendarContract.Events.VISIBLE + " = 1",
                                null,
                                CalendarContract.Events._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {

                String displayName = calCursor.getString(0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(calCursor.getString(2)));
                String diaSelec = formatter.format(calendar.getTime());
                if(dataActual.equals(diaSelec)) {   //Si el evento es del dia actual lo añado a la lista
                    eventos.add(displayName);
                    diaeventos.add(diaSelec);
                }

            } while (calCursor.moveToNext());
        }

        String listaReuniones = new String();
        String listaTareas = new String();
        for(int i = 0; i < eventos.size(); i++){
            String aux = eventos.get(i).split(" ")[0];
            if(aux.equals("Reunión")){
                int inicio = eventos.get(i).indexOf(" ");
                listaReuniones += (eventos.get(i).substring(inicio + 1) + "\n");
            }
            else if(aux.equals("Tarea")){
                int inicio = eventos.get(i).indexOf(" ");
                listaTareas += (eventos.get(i).substring(inicio + 1) + "\n");
            }
        }

        txtLlistaEventos.setText(listaReuniones);
        txtLlistaTareas.setText(listaTareas);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(DiaActivity.this, PopUpActivity.class);

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
            Intent intent = new Intent(DiaActivity.this, PopUpActivity.class);

            //Informacion a passar entre actividades
            Bundle b = new Bundle();
            b.putString("ERROR", "En esta vista puede visualizar todos los eventos programados para el día seleccionado.\n\n" +
                    "Pulse el botón “Atrás” de su dispositivo para volver a la visión semanal.\n");
            intent.putExtras(b);

            //Iniciamos la nueva actividad
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
