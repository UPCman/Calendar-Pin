package com.daude.calendari;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorDiaSetmana extends ArrayAdapter<DiaSetmana> {


    private DiaSetmana[] datos;

    public AdaptadorDiaSetmana(Context context, DiaSetmana[] dm) {
        super(context, R.layout.listitem_diasetmana, dm);
        datos = dm;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.listitem_diasetmana, null);

        TextView lblNombre = (TextView)item.findViewById(R.id.LblNombre);
        lblNombre.setText(datos[position].getNom());

        TextView lblNumero = (TextView)item.findViewById(R.id.LblNumero);
        lblNumero.setText(datos[position].getNumero());

        TextView lblMes = (TextView)item.findViewById(R.id.LblMes);
        lblMes.setText(datos[position].getMes());

        item.setBackgroundColor(datos[position].getColor());

        return(item);
    }
}
