package com.example.mathe.matchandplay.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.example.mathe.matchandplay.R;

import java.util.ArrayList;

public class IDAdapter extends ArrayAdapter<String> {

    private ArrayList<String> ids;
    private Context context;

    public IDAdapter(Context c, ArrayList<String> objects) {
        super(c, 0, objects);

        this.context = c;
        this.ids = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (ids != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.formato_item_id, parent, false);

            TextView textViewNome = (TextView) view.findViewById(R.id.textViewId);

            String ids2 = ids.get(position);
            textViewNome.setText(ids2);

        }

        return view;
    }
}