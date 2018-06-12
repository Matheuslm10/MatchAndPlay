package com.example.mathe.matchandplay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.example.mathe.matchandplay.R;

import java.util.ArrayList;

public class UsuarioAdapterChat extends ArrayAdapter<Usuario> {

    private ArrayList<Usuario> usuarios;
    private Context context;


    public UsuarioAdapterChat(Context c, ArrayList<Usuario> objects) {
        super(c, 0, objects);

        this.context = c;
        this.usuarios = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (usuarios != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.formato_chat_usuario, parent, false);

            TextView textViewNome = (TextView) view.findViewById(R.id.textViewNomeChat);
            ImageView fotoPerfil = view.findViewById(R.id.fotoChat);

            Usuario usuarios2 = usuarios.get(position);


            textViewNome.setText(usuarios2.getNomeusuario());
            Glide.with(context).load(usuarios2.getUrlFotoPerfil()).into(fotoPerfil);

        }else{
            System.out.println("A LISTA DE USUARIO PASSADA PARA O ADAPTER ESTÁ NULA.");
        }

        return view;
    }
}
