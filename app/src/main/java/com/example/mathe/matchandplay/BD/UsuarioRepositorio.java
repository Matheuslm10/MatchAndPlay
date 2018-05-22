package com.example.mathe.matchandplay.BD;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mathe on 22/05/2018.
 */

public class UsuarioRepositorio {

    private SQLiteDatabase conexao;

    public UsuarioRepositorio(SQLiteDatabase conexao){
        //
        this.conexao = conexao;
    }

}
