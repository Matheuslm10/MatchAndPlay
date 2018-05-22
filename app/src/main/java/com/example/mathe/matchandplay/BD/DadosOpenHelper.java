package com.example.mathe.matchandplay.BD;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by mathe on 18/05/2018.
 */

public class DadosOpenHelper extends SQLiteOpenHelper {

    public DadosOpenHelper(Context context) {
        //
        super(context, "Empresa", null, 1);
    }



    public String getCreateTableUsuario(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS usuario(            		        	                                ");
        sql.append("		idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,       		                                    ");
        sql.append("		nomeUsuario VARCHAR(200) NOT NULL DEFAULT (''),                                             ");
        sql.append("		emailUsuario VARCHAR(200) NOT NULL DEFAULT (''),                                            ");
        sql.append("		idMeusJogos INTEGER REFERENCES meusjogos (idMeusJogos) NOT NULL DEFAULT (''),    		    ");
        sql.append("		idJogosDesejados INTEGER REFERENCES jogosdesejados (idJogosDesejados) NOT NULL DEFAULT ('') ");
        sql.append("	)                                                   		      	                            ");

        return sql.toString();
    }

    public String getCreateTableMeusJogos(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS meusjogos(                    ");
        sql.append("		idMeusJogos INTEGER PRIMARY KEY AUTOINCREMENT,       ");
        sql.append("		nomeDoJogo VARCHAR(200) NOT NULL DEFAULT ('')        ");
        sql.append("	)                                                        ");

        return sql.toString();
    }

    public String getCreateTableJogosDesejados(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS jogosdesejados(                    ");
        sql.append("		idJogosDesejados INTEGER PRIMARY KEY AUTOINCREMENT,       ");
        sql.append("		nomeDoJogo VARCHAR(200) NOT NULL DEFAULT ('')             ");
        sql.append("	)                                                             ");

        return sql.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTableMeusJogos());
        db.execSQL(getCreateTableJogosDesejados());
        db.execSQL(getCreateTableUsuario());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}