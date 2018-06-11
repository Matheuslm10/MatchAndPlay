package com.example.mathe.matchandplay;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.Cadastro.CadastrarJogosDesejados;
import com.example.mathe.matchandplay.Cadastro.CadastrarJogosDesejados;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JogosDesejados extends AppCompatActivity {

    String emailSelecionado;
    ListView listViewJogosDesejados;
    TextView msg;
    FloatingActionButton botaoJD;
    ArrayAdapter<String> adapter;
    Usuario user;
    //ProgressBar
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos_desejados);

        Intent it = getIntent();
        emailSelecionado =  it.getStringExtra("email_user_selected_jd");

        listViewJogosDesejados = findViewById(R.id.list_jogosDesejados);
        progressBar = findViewById(R.id.progressbarJD);
        progressBar.setVisibility(View.VISIBLE);
        msg = findViewById(R.id.msgJD);
        botaoJD = findViewById(R.id.fab_jd);

        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("email").equalTo(emailSelecionado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        user = issue.getValue(Usuario.class);
                        ArrayList<String> arraylistJD = user.getJogosdesejados();
                        if(arraylistJD.get(0).equals("")){
                            progressBar.setVisibility(View.GONE);
                            msg.setText("Você não possui jogos nesta lista. Adicione novos jogos clicando no botão abaixo!");
                            botaoJD.setImageResource(R.drawable.baseline_add_24);
                        }else{
                            adapter = new ArrayAdapter<String>(JogosDesejados.this, android.R.layout.simple_list_item_1, user.getJogosdesejados());
                            progressBar.setVisibility(View.GONE);
                            listViewJogosDesejados.setAdapter(adapter);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_jd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarJogosDesejados(view);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void cadastrarJogosDesejados (View v){
        Intent it = new Intent(this, CadastrarJogosDesejados.class);
        it.putExtra("user_logado", user);
        startActivity(it);

    }

}
