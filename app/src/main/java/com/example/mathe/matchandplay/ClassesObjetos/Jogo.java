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
import java.util.List;

public class Jogo  implements Serializable {
    private int idjogo;
    private String nomejogo;


    public int getIdjogo() {
        return idjogo;
    }

    public void setIdjogo(int idjogo) {
        this.idjogo = idjogo;
    }

    public String getNomejogo() {
        return nomejogo;
    }

    public void setNomejogo(String nomejogo) {
        this.nomejogo = nomejogo;
    }
}

