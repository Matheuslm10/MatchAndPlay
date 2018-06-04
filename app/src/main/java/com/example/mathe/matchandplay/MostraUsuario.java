package com.example.mathe.matchandplay;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MostraUsuario extends AppCompatActivity {
    String emailSelecionado;
    private TextView nomeUsuario;
    private TextView emailUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_usuario);
        Intent it = getIntent();
        emailSelecionado =  it.getStringExtra("email_user_selected");

        nomeUsuario = (TextView) findViewById(R.id.txtMostraNome);
        emailUsuario = (TextView) findViewById(R.id.txtMostraEmail);
        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("email").equalTo(emailSelecionado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Usuario users = issue.getValue(Usuario.class);
                        nomeUsuario.setText(users.getNomeusuario());
                        emailUsuario.setText(users.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Um hora vai ter! ;D", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
}
