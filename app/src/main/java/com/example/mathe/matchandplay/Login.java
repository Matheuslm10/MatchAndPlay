package com.example.mathe.matchandplay;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.Cadastro.CadastroUsuario;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnEntrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    //ProgressBar
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        progressBarLogin = findViewById(R.id.progressbarLogin);
        progressBarLogin.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ff6a00"), android.graphics.PorterDuff.Mode.MULTIPLY);


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {

                    usuario = new Usuario();
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());
                    progressBarLogin.setVisibility(View.VISIBLE);
                    validarLogin();

                } else {
                    Toast.makeText(Login.this, "Preencha os campos de e-mail e senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void validarLogin() {
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressBarLogin.setVisibility(View.GONE);
                    abrirTelaPrincipal();
                    Toast.makeText(Login.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();

                } else {
                    progressBarLogin.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void abrirTelaPrincipal() {
        finish();
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    public void irParaCadastro(View v){
        Intent it = new Intent(this, CadastroUsuario.class);
        startActivity(it);
    }


}
