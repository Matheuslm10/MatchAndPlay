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

    public String populaJogos(){
        StringBuilder sql = new StringBuilder();


        //só está executando o primeiro comando sql, vai ter que colocar tudo isso num array e percorrer e ir preenchendo o bd
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Jogo da Vida');   ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Detetive');       ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('WAR I');          ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('WAR II');         ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Imagem & Ação');  ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Bozó');           ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Truco');          ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Poker');          ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Xadrez');         ");
        sql.append("INSERT INTO jogo (nomejogo) VALUES ('Stop');           ");

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
        db.execSQL(populaJogos());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}