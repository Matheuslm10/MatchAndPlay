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
    public SQLiteDatabase conexao;
    public UsuarioRepositorio usuarioRepositorio;
    public JogoRepositorio jogoRepositorio;

    // "qual" = 1 para usuarioRepositorio, e 2 para jogoRepositorio
    public void criarConexao(Context context, int qual){
        try{
            dadosOpenHelper = new DadosOpenHelper(context);
            conexao = dadosOpenHelper.getWritableDatabase();
            if(qual==1){
                usuarioRepositorio = new UsuarioRepositorio(conexao);
            }else if(qual==2){
                jogoRepositorio = new JogoRepositorio(conexao);
            }

        }catch (SQLException ex){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }
}
