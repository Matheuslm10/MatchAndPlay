package com.example.mathe.matchandplay.Cadastro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mathe.matchandplay.BD.Criaconexao;
import com.example.mathe.matchandplay.BD.JogoRepositorio;
import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.R;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;

public class CadastroUsuario extends AppCompatActivity implements OnItemSelectedListener {
    Criaconexao conectorDoBD = new Criaconexao();
    String jogoSelecionado = "";


    private Spinner sp;
    ArrayList<String> jogos = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    //private JogoRepositorio jogoRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        /////preenchendo spinner (menu dropdown)
        sp = (Spinner) findViewById(R.id.spinner1);
        sp.setOnItemSelectedListener(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,jogos);
        conectorDoBD.criarConexao(this, 2);
        ArrayList<Jogo> arrayJogos = conectorDoBD.jogoRepositorio.buscarTodos();
        for(Jogo game : arrayJogos) {
            jogos.add(game.getNomejogo());
        }
        sp.setAdapter(adapter);
        /////
    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String jogo = (String) parent.getItemAtPosition(pos);
        this.jogoSelecionado = jogo;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
