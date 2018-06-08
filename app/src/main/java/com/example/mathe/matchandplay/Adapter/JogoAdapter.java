package com.example.mathe.matchandplay.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.mathe.matchandplay.R;

import java.util.List;

public class JogoAdapter extends BaseAdapter{
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



}