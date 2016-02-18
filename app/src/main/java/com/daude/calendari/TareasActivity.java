package com.daude.calendari;

import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class TareasActivity extends ActionBarActivity {

    private TextView txtLlistaTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        txtLlistaTareas = (TextView)findViewById(R.id.txtLlistaTareas);

        setTitle("Lista de Tareas");


        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();

        //Construimos el titulo

        ArrayList<String> eventos = new ArrayList();

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
                    eventos.add(displayName);

            } while (calCursor.moveToNext());
        }

        String listaReuniones = new String();
        String listaTareas = new String();
        for(int i = 0; i < eventos.size(); i++){
            String aux = eventos.get(i).split(" ")[0];

            if(aux.equals("Tarea")){
                int inicio = eventos.get(i).indexOf(" ");
                listaTareas += (eventos.get(i).substring(inicio + 1) + "\n");
            }
        }

        txtLlistaTareas.setText(listaTareas);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tareas, menu);
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
            Intent intent = new Intent(TareasActivity.this, PopUpActivity.class);

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
            Intent intent = new Intent(TareasActivity.this, PopUpActivity.class);

            //Informacion a passar entre actividades
            Bundle b = new Bundle();
            b.putString("ERROR", "En la vista de Tareas puede visualizar todos los eventos del tipo Tarea que se han programado usando esta aplicación.\n\n" +
                    "Pulse el botón “Atrás” de su dispositivo para volver a la visión semanal.");
            intent.putExtras(b);

            //Iniciamos la nueva actividad
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
