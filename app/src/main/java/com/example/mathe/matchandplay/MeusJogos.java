package com.example.mathe.matchandplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.Cadastro.CadastrarMeusJogos;
import com.example.mathe.matchandplay.Cadastro.CadastroUsuario;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MeusJogos extends AppCompatActivity {

    String emailSelecionado;
    ListView listViewMeusJogos;
    TextView msg;
    FloatingActionButton botaoMJ;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_jogos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent it = getIntent();
        emailSelecionado =  it.getStringExtra("email_user_selected_mj");

        listViewMeusJogos = findViewById(R.id.list_meusJogos);
        msg = findViewById(R.id.msgMJ);
        botaoMJ = findViewById(R.id.fab_mj);

        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("email").equalTo(emailSelecionado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Usuario user = issue.getValue(Usuario.class);
                        ArrayList<String> arraylistMJ = user.getMeusjogos();
                        if(arraylistMJ.get(0).equals("") ){
                            msg.setText("Você não possui jogos nesta lista. Adicione novos jogos clicando no botão abaixo!");
                            botaoMJ.setImageResource(R.drawable.baseline_add_24);
                        }else {
                            adapter = new ArrayAdapter<String>(MeusJogos.this, android.R.layout.simple_list_item_1, user.getMeusjogos());
                            listViewMeusJogos.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_mj);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarMeusJogos(view);
            }
        });
    }

    public void cadastrarMeusJogos (View v){
        Intent it = new Intent(this, CadastrarMeusJogos.class);
        startActivity(it);

    }

}
