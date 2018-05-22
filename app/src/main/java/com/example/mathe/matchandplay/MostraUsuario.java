package com.example.mathe.matchandplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mathe.matchandplay.ClassesObjetos.Usuario;

public class MostraUsuario extends AppCompatActivity {
    Usuario selectedFunc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_usuario);
        Intent it = getIntent();
        selectedFunc = (Usuario) it.getSerializableExtra("chave_pessoa");
    }
}
