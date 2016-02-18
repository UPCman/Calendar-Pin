package com.daude.calendari;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



public class SetmanaActivity extends ActionBarActivity {


    private String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    long idSelecCal;

    private Date fecha;
    private int dia;                  //1 = lunes ... 7 = domingo
    private int numDiasMes;
    private ListView lstSetmana;
    private int numMesLunes;
    private int numLunes;
    private boolean control;

    private int despCount;

    private DiaSetmana[] datos =
            new DiaSetmana[]{
                new DiaSetmana("Lunes", "28", "Diciembre"),
                new DiaSetmana("Martes", "29", "Diciembre"),
                new DiaSetmana("Miércoles", "30", "Diciembre"),
                new DiaSetmana("Jueves", "31", "Diciembre"),
                new DiaSetmana("Viernes", "1", "Enero"),
                new DiaSetmana("Sábado", "2", "Enero"),
                new DiaSetmana("Domingo", "3", "Enero")};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmana);

        despCount = 0;
        actualizarFechas(despCount); //obtiene la fecha actual y actualiza los datos de la visualizacion del calendario
        datos[dia -1].setColor(Color.rgb(215,255,215));

        //Seleccio de Calendaris Disponibles
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                idSelecCal = id;
            } while (calCursor.moveToNext());
        }
        else {  //No existe ninguna cuenta google
            Intent intent = new Intent(SetmanaActivity.this, PopUpActivity.class);

            //Informacion a passar entre actividades
            Bundle b = new Bundle();
            b.putString("ERROR", "No hay ninguna cuenta de Google vinculada al sistema.\n" +
                        "\n" +
                        "Vincule una cuenta de Google desde el menú Ajustes y luego reinicie la aplicación.");
            intent.putExtras(b);

            //Iniciamos la nueva actividad
            startActivity(intent);
        }


        AdaptadorDiaSetmana adaptador =
                new AdaptadorDiaSetmana(this, datos);
        lstSetmana = (ListView)findViewById(R.id.LstSetmana);
        lstSetmana.setAdapter(adaptador);

        lstSetmana.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                String opcionSeleccionada =
                        ((DiaSetmana)a.getItemAtPosition(position)).getNom();
                Intent intent = new Intent(SetmanaActivity.this, DiaActivity.class);

                //Informacion a passar entre actividades
                Bundle b = new Bundle();
                b.putString("DIA", opcionSeleccionada);
                b.putLong("IDCAL", idSelecCal);
                b.putString("NUMERO", datos[position].getNumero());
                b.putString("MES", datos[position].getMes());
                b.putString("ANO", getTitle().toString());

                String numMes = datos[position].getMes();
                for ( int i = 0; i < meses.length; i++){
                    if(meses[i].equals(numMes)) numMes = Integer.toString(i + 1);
                }

                //Guarrada pero estic sense temps
                if(numMes.equals("1")) numMes = "01";
                else if(numMes.equals("2")) numMes = "02";
                else if(numMes.equals("3")) numMes = "03";
                else if(numMes.equals("4")) numMes = "04";
                else if(numMes.equals("5")) numMes = "05";
                else if(numMes.equals("6")) numMes = "06";
                else if(numMes.equals("7")) numMes = "07";
                else if(numMes.equals("8")) numMes = "08";
                else if(numMes.equals("9")) numMes = "09";
                b.putString("NUMMES", numMes);

                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);

            }
        });



        // force the device to show the actionbar overflow button (4.0+ only...)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            try {
                ViewConfiguration config = ViewConfiguration.get(this);
                Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
                if (menuKeyField != null) {
                    menuKeyField.setAccessible(true);
                    menuKeyField.setBoolean(config, false);
                }
            } catch (Exception ex) {
                // ignore
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setmana, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_navigation_arrow_forward:
                despCount += 7;
                actualizarFechas(despCount);
                refrescaValors();
                return true;
            case R.id.action_navigation_arrow_back:
                despCount -= 7;
                actualizarFechas(despCount);
                refrescaValors();
                ;
                return true;
            case R.id.action_plus:
                Intent intent = new Intent(SetmanaActivity.this, AnadirEventoActivity.class);

                //Informacion a passar entre actividades
                Bundle b = new Bundle();
                b.putLong("IDCAL", idSelecCal);
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
                return true;
            case R.id.action_about:
                intent = new Intent(SetmanaActivity.this, PopUpActivity.class);

                //Informacion a passar entre actividades
                b = new Bundle();
                b.putString("ERROR", "Calendar Pin [2016]\n\n" +
                        "Desarrollado con Android Studio 1.0.1 para Android 4.3 o superior.\n\n" +
                        "Desarrollado por Sergi Sánchez Centelles para la práctica de Android para la asignatura IDI en la FIB de la UPC.");
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
                return true;
            case R.id.action_help:
                intent = new Intent(SetmanaActivity.this, PopUpActivity.class);

                //Informacion a passar entre actividades
                b = new Bundle();
                b.putString("ERROR", "En la lista semanal se remarca de color verde el día actual.\n\n" +
                        "Se puede interactuar pulsando encima de cualquier día para ver los eventos programados en él.\n\n" +
                        "Para navegar por las diferentes semanas usar los iconos en forma de flecha de la barra superior. En esa misma barra también se encuentra con el icono ‘+’ un botón para añadir eventos y un desplegable con más opciones.\n");
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
                return true;
            case R.id.action_events:
                intent = new Intent(SetmanaActivity.this, TareasActivity.class);

                //Informacion a passar entre actividades
                b = new Bundle();
                b.putLong("IDCAL", idSelecCal);
                b.putString("ANO", getTitle().toString());

                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getFechaActual(int n)
    //Obtiene la fecha actual del telefono (mas n dias), y la devuelve convertida en string
    {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, n);
        fecha = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(fecha);
        dia = (cal.get(Calendar.DAY_OF_WEEK) - 1); //lo convertimos en 1 lunes, 7 domingo
        if(dia == 0) dia = 7;

        cal.add(Calendar.DATE, -(dia -1));
        fecha = cal.getTime();
        String formatteDateLunes = df.format(fecha);
        numLunes  = Integer.parseInt(formatteDateLunes.split("-")[2]);

        numMesLunes = Integer.parseInt(formatteDateLunes.split("-")[1]) -1;

        numDiasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        setTitle(formatteDateLunes.split("-")[0]);
        return formatteDate;

    }

    private void actualizarFechas(int n){
        String[] parts = getFechaActual(n).split("-");
        control = false;
        datos =
                new DiaSetmana[]{
                        new DiaSetmana("Lunes", Integer.toString(numLunes), meses[numMesLunes]),
                        new DiaSetmana("Martes", obtenerNumDiaN(numLunes, 1), meses[numMesLunes]),
                        new DiaSetmana("Miércoles", obtenerNumDiaN(numLunes, 2), meses[numMesLunes]),
                        new DiaSetmana("Jueves", obtenerNumDiaN(numLunes, 3), meses[numMesLunes]),
                        new DiaSetmana("Viernes", obtenerNumDiaN(numLunes, 4), meses[numMesLunes]),
                        new DiaSetmana("Sábado", obtenerNumDiaN(numLunes, 5), meses[numMesLunes]),
                        new DiaSetmana("Domingo", obtenerNumDiaN(numLunes, 6),meses[numMesLunes])};
        control = false;

    }

    private String obtenerNumDiaN(int dia, int n){
        // Devuelve el Numero del Dia dado mas N dias en un mes con MAX dias
        int d = (dia + n)%(numDiasMes +1);
        if (d < dia) {
            if(!control) {
                numMesLunes++;
                if (numMesLunes > 11) numMesLunes = 0;
            }
            control = true;
            d++;
        }
        return Integer.toString(d);
    }

    private void refrescaValors(){
        if(despCount ==0 )datos[dia -1].setColor(Color.rgb(215,255,215));
        AdaptadorDiaSetmana adaptador =
                new AdaptadorDiaSetmana(this, datos);
        lstSetmana.setAdapter(adaptador);
    }
}
