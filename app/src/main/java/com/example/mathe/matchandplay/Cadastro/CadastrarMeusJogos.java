package com.example.mathe.matchandplay.Cadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mathe.matchandplay.Adapter.JogoAdapter;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.example.mathe.matchandplay.MainActivity;
import com.example.mathe.matchandplay.R;
import com.example.mathe.matchandplay.SortBasedOnName;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CadastrarMeusJogos extends AppCompatActivity {

    //CheckList
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerJogo;
    private ArrayList<Jogo> arrayListJogos;
    private JogoAdapter adapter;
    private ListView checklistMeusJogosEditar;
    private Usuario usuarioAtual;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_meus_jogos);
        setTitle("Meus Jogos");

        firebase = ConfiguracaoFireBase.getFireBase().child("jogo");
        arrayListJogos = new ArrayList<>();
        Intent it = getIntent();
        usuarioAtual =  (Usuario)it.getSerializableExtra("user_logado");
        adapter = new JogoAdapter(this, arrayListJogos, usuarioAtual.getMeusjogos());
        progressBar = findViewById(R.id.progressbarCadMJ);
        progressBar.setVisibility(View.VISIBLE);


        //CheckList
        checklistMeusJogosEditar = findViewById(R.id.lista_meusJogos_editar);
        checklistMeusJogosEditar.setAdapter(adapter);

        valueEventListenerJogo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListJogos.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Jogo arrayListJogoNovo = dados.getValue(Jogo.class);

                    arrayListJogos.add(arrayListJogoNovo);
                }
                Collections.sort(arrayListJogos, new SortBasedOnName(2));
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    public void editarMeusJogos(View v){
        usuarioAtual.setMeusjogos(adapter.getJogosSelecionados());
        for(String jogo: usuarioAtual.getJogosdesejados()){
            if(adapter.getJogosSelecionados().contains(jogo)){
                if(usuarioAtual.getJogosdesejados().size() == 1){
                    usuarioAtual.getJogosdesejados().add("");
                    usuarioAtual.getJogosdesejados().remove(jogo);
                }else{
                    usuarioAtual.getJogosdesejados().remove(jogo);
                }

            }
        }
        //verifica se está vazio
        if(adapter.getJogosSelecionados().size()<1 || adapter.getJogosSelecionados().isEmpty()){
            adapter.getJogosSelecionados().add("");
            System.out.println("CONTEUDO DO ADAPTER: "+adapter.getJogosSelecionados());
        }
        usuarioAtual.salvar();
        Toast.makeText(this, "Alterações realizadas com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }


    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerJogo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerJogo);
    }

}
