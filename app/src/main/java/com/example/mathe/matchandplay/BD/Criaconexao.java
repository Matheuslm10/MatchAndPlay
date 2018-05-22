package com.example.mathe.matchandplay.BD;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mathe on 22/05/2018.
 */

/*Essa classe deverá ser instanciada a cada conexão que for feita com os repositorios*/
public class Criaconexao {

    DadosOpenHelper dadosOpenHelper;
    private SQLiteDatabase conexao;
    UsuarioRepositorio usuarioRepositorio;

    public void criarConexao(Context context){
        try{
            dadosOpenHelper = new DadosOpenHelper(context);
            conexao = dadosOpenHelper.getWritableDatabase();
            usuarioRepositorio = new UsuarioRepositorio(conexao);

        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }
}
