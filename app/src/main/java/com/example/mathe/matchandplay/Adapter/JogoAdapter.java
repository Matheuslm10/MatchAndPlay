package com.example.mathe.matchandplay.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.R;

import java.util.ArrayList;
import java.util.List;

public class JogoAdapter extends ArrayAdapter<Jogo> {

    private ArrayList<Jogo> jogos;
    private Context context;
    ArrayList<String> jogosSelecionados = new ArrayList<String>();
    ArrayList<String> jogosDoLogado;

    public JogoAdapter(Context c, ArrayList<Jogo> objects, ArrayList<String> myGames) {
        super(c, 0, objects);

        this.context = c;
        this.jogos = objects;
        this.jogosDoLogado = myGames;
    }

    public ArrayList<String> getJogosSelecionados(){
        return jogosSelecionados;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (jogos != null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.checkbox_item, parent, false);


            final Jogo jogo = jogos.get(position);

            CheckBox nome = view.findViewById(R.id.checkbox_space);
            //caso seja na tela de editar, se o usuário já tiver o jogo, tem q deixar marcado já.
            if(jogosDoLogado!=null){
                if(jogosDoLogado.contains(jogo.getNome())){
                    nome.setChecked(true);
                    jogosSelecionados.add(jogo.getNome());
                }
            }
            nome.setText(jogo.getNome());
            nome.setTextSize(18);
            nome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        jogosSelecionados.add(jogo.getNome());
                    }else{
                        jogosSelecionados.remove(jogo.getNome());
                    }

                }
            });


        }

        return view;
    }
}