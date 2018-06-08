package com.example.mathe.matchandplay;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Conta extends AppCompatActivity {

    EditText nome;
    EditText senha;
    Button cancelar;
    Button salvar;
    private FirebaseAuth usuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        setTitle("Minha Conta");

        usuarioFirebase = ConfiguracaoFireBase.getFirebaseAutenticacao();

        nome = findViewById(R.id.edtAlterarNome);
        senha = findViewById(R.id.edtAlterarSenha);
        cancelar = findViewById(R.id.btnCancelar);
        salvar = findViewById(R.id.btnSalvar);

        //PREENCHENDO OS DADOS
        String emailSelecionado = usuarioFirebase.getCurrentUser().getEmail();
        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("email").equalTo(emailSelecionado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Usuario user = issue.getValue(Usuario.class);
                        nome.setHint(user.getNomeusuario());
                        senha.setHint(user.getSenha());
                        String password = "";
                        for(int i=0; i<(user.getSenha().length()); i++){
                            password += "•";
                        }
                        senha.setHint(password);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cancelar.setEnabled(false);
        salvar.setEnabled(false);

        nome.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                cancelar.setEnabled(true);
                cancelar.setBackgroundResource(R.drawable.formatobotao);
                salvar.setEnabled(true);
                salvar.setBackgroundResource(R.drawable.formatobotao);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        senha.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                cancelar.setEnabled(true);
                salvar.setEnabled(true);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }
            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });


    }

    public void salvarAlteracao(View v){
        Toast.makeText(this, "Ainda vai salvar!", Toast.LENGTH_SHORT).show();

    }

    public void cancelarAlteracao(View v){
        Toast.makeText(this, "Ainda vai cancelar", Toast.LENGTH_SHORT).show();

    }

    public void irParaAlterarFoto(View view) {
    }
}
