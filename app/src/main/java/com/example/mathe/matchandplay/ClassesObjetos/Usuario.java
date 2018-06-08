package com.example.mathe.matchandplay.ClassesObjetos;

import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mathe on 22/05/2018.
 */

public class Usuario  implements Serializable {
    private String idusuario;
    private String nomeusuario;
    private String email;
    private String senha;
    private ArrayList<String> meusjogos;
    private ArrayList<String> jogosdesejados;
    private boolean interessado;
    private boolean proprietario;
    private String urlFotoPerfil;

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
        hasMapUsuario.put("meusjogos",getMeusjogos());
        hasMapUsuario.put("jogosdesejados",getJogosdesejados());
        hasMapUsuario.put("interessado", isInteressado());
        hasMapUsuario.put("proprietario", isProprietario());
        hasMapUsuario.put("urlFotoPerfil",getUrlFotoPerfil());

        return hasMapUsuario;

    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
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

    public ArrayList<String> getMeusjogos() {
        return meusjogos;
    }

    public void setMeusjogos(ArrayList<String> meusjogos) {
        this.meusjogos = meusjogos;
    }

    public ArrayList<String> getJogosdesejados() {
        return jogosdesejados;
    }

    public void setJogosdesejados(ArrayList<String> jogosdesejados) {
        this.jogosdesejados = jogosdesejados;
    }

    public boolean isInteressado() {
        return interessado;
    }

    public void setInteressado(boolean interessado) {
        this.interessado = interessado;
    }

    public boolean isProprietario() {
        return proprietario;
    }

    public void setProprietario(boolean proprietario) {
        this.proprietario = proprietario;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }
}
