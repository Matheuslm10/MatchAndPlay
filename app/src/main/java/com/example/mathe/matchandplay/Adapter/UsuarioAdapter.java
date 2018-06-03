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

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> usuarios;
    private Context context;

    public UsuarioAdapter(Context c, ArrayList<Usuario> objects) {
        super(c, 0, objects);

        this.context = c;
        this.usuarios = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (usuarios != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.formato_item_usuario, parent, false);

            TextView textViewNome = (TextView) view.findViewById(R.id.textViewNome);

            Usuario usuarios2 = usuarios.get(position);
            textViewNome.setText(usuarios2.getNomeusuario());

        }

        return view;
    }
}
