package com.example.mathe.matchandplay.ClassesObjetos;

import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mathe on 22/05/2018.
 */

public class Usuario  implements Serializable {
    private int idusuario;
    private String nomeusuario;
    private String email;
    private String senha;

    public Usuario(){
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFireBase.getFireBase();
        referenciaFirebase.child("usuario").child(String.valueOf(getIdusuario())).setValue(this);
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> hasMapUsuario = new HashMap<>();

        hasMapUsuario.put("idusuario",getIdusuario());
        hasMapUsuario.put("nomeusuario",getNomeusuario());
        hasMapUsuario.put("email",getEmail());
        hasMapUsuario.put("senha",getSenha());

        return hasMapUsuario;

    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getNomeusuario() {
        return nomeusuario;
    }

    public void setNomeusuario(String nomeusuario) {
        this.nomeusuario = nomeusuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
