package com.example.mathe.matchandplay.ClassesObjetos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mathe.matchandplay.Cadastro.CadastroUsuario;
import com.example.mathe.matchandplay.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jogo  implements Serializable {

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

