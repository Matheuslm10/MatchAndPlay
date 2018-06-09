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
import android.widget.TextView;

import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.R;

import java.util.ArrayList;
import java.util.List;

public class JogoAdapter extends ArrayAdapter<Jogo> {

    private ArrayList<Jogo> jogos;
    private Context context;

    public JogoAdapter(Context c, ArrayList<Jogo> objects) {
        super(c, 0, objects);

        this.context = c;
        this.jogos = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        for(Jogo j: jogos){
            System.out.println("jogo do array: "+j.getNome());
        }

        if (jogos != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.checkbox_item, parent, false);

            Jogo jogo = jogos.get(position);

            CheckBox nome = view.findViewById(R.id.checkbox_space);
            nome.setText(jogo.getNome());
            System.out.println("NOME DO JOGO CHECKBOX: "+jogo.getNome());
            nome.setTextSize(18);

        }

        return view;
    }
    /*
    private final List<String> jogos;
    private final Activity act;

    public JogoAdapter(List<String> jogos, Activity act) {
        this.jogos = jogos;
        this.act = act;
    }

    @Override
    public int getCount() {
        return jogos.size();
    }

    @Override
    public Object getItem(int position) {
        return jogos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = act.getLayoutInflater()
                .inflate(R.layout.checkbox_item, parent, false);


        String jogo = jogos.get(position);

        CheckBox nome =
                view.findViewById(R.id.txt_lan2);


        nome.setText(jogo);
        nome.setTextSize(18);


        return view;
    }

    */



}