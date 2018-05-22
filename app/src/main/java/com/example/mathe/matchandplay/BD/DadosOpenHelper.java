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

        sql.append("	CREATE TABLE IF NOT EXISTS usuario(            		        	             ");
        sql.append("		idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,       		                 ");
        sql.append("		nomeusuario VARCHAR (200) NOT NULL DEFAULT (''),                         ");
        sql.append("		email       VARCHAR (200) NOT NULL DEFAULT ('') UNIQUE ON CONFLICT FAIL, ");                                                    sql.append("		senha       VARCHAR (200) NOT NULL    		                             ");
        sql.append("	)                                                   		      	         ");

        return sql.toString();
    }

    public String getCreateTableJogo(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS jogo(            		                 ");
        sql.append("		idjogo   INTEGER PRIMARY KEY AUTOINCREMENT,       		         ");
        sql.append("		nomejogo VARCHAR (200) NOT NULL DEFAULT ('')                     ");
        sql.append("	)                                                   		      	 ");

        return sql.toString();
    }


    public String getCreateTableMeusJogos(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS meusjogos(                                ");
        sql.append("		idusuario INTEGER NOT NULL REFERENCES usuario (idusuario),       ");
        sql.append("		idjogo    INTEGER REFERENCES jogo (idjogo) NOT NULL              ");
        sql.append("	)                                                                    ");

        return sql.toString();
    }

    public String getCreateTableJogosDesejados(){
        StringBuilder sql = new StringBuilder();

        sql.append("	CREATE TABLE IF NOT EXISTS jogosdesejados(                           ");
        sql.append("		idusuario INTEGER NOT NULL REFERENCES usuario (idusuario),       ");
        sql.append("		idjogo    INTEGER REFERENCES jogo (idjogo) NOT NULL              ");
        sql.append("	)                                                                    ");

        return sql.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTableJogo());
        db.execSQL(getCreateTableUsuario());
        db.execSQL(getCreateTableMeusJogos());
        db.execSQL(getCreateTableJogosDesejados());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}