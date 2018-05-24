package com.example.mathe.matchandplay.BD;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mathe.matchandplay.ClassesObjetos.Jogo;

import java.util.ArrayList;

public class JogoRepositorio {

    private SQLiteDatabase conexao;

    public JogoRepositorio(SQLiteDatabase conexao){
        //
        this.conexao = conexao;
    }

    public ArrayList<Jogo> buscarTodos(){
        ArrayList<Jogo> jogos = new ArrayList<Jogo>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM jogo;");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            do{
                Jogo jogo = new Jogo();

                jogo.setIdjogo(resultado.getInt( resultado.getColumnIndexOrThrow("idjogo")));
                jogo.setNomejogo(resultado.getString(resultado.getColumnIndexOrThrow("nomejogo")));
                jogos.add(jogo);

            }while(resultado.moveToNext());
        }
        return jogos;
    }
}
