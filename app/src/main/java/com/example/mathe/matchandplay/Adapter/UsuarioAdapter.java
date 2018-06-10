package com.example.mathe.matchandplay.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.example.mathe.matchandplay.MainActivity;
import com.example.mathe.matchandplay.MostraUsuario;
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
            System.out.println("ENTROU NO ADAPTER DOS USUARIOS");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.formato_item_usuario, parent, false);

            TextView textViewNome = (TextView) view.findViewById(R.id.textViewNome);
            ImageView fotoPerfil = view.findViewById(R.id.fotoMatch);
            ImageView balao = view.findViewById(R.id.ivBalao);
            ImageView mao = view.findViewById(R.id.ivMao);



            Usuario usuarios2 = usuarios.get(position);
            if(usuarios2.isInteressado()){
                balao.setImageResource(R.drawable.deseja_aceso);
            }else{
                balao.setImageResource(R.drawable.deseja_apagado);
            }

            if(usuarios2.isProprietario()){
                mao.setImageResource(R.drawable.possui_aceso);
            }else{
                mao.setImageResource(R.drawable.possui_apagado);
            }

            textViewNome.setText(usuarios2.getNomeusuario());
            Glide.with(context).load(usuarios2.getUrlFotoPerfil()).into(fotoPerfil);

        }else{
            System.out.println("A LISTA DE USUARIO PASSADA PARA O ADAPTER ESTÁ NULA.");
        }

        return view;
    }
}
