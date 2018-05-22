package com.example.mathe.matchandplay.BD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mathe.matchandplay.ClassesObjetos.Usuario;

import java.util.ArrayList;

/**
 * Created by mathe on 22/05/2018.
 */

public class UsuarioRepositorio {

    private SQLiteDatabase conexao;

    public UsuarioRepositorio(SQLiteDatabase conexao){
        //
        this.conexao = conexao;
    }

    public void inserir(Usuario usuario){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nomeusuario", usuario.nomeusuario);
        contentValues.put("email", usuario.email);
        contentValues.put("senha", usuario.senha);

        conexao.insertOrThrow("usuario", null, contentValues);
    }

    public void excluir(int idusuario){
        String[] parametros = new String[1];
        parametros[0] = String.valueOf(idusuario);

        conexao.delete("usuario", "idusuario = ?", parametros);
    }

    //futuramente poderia existir dois metodos desse, um que fosse exclusivo para a senha e o outro para o restante dos atributos
    public void alterar(Usuario usuario){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nomeusuario", usuario.nomeusuario);
        contentValues.put("email", usuario.email);
        contentValues.put("senha", usuario.senha);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(usuario.idusuario);

        conexao.update("usuario", contentValues, "idusuario = ?", parametros);
    }

    public ArrayList<Usuario> retornaInteressadosEProprietarios(){
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT usuario.*                            ");
        sql.append("FROM usuario                                         ");
        sql.append("WHERE idusuario IN                                   ");
        sql.append("	(SELECT DISTINCT jd.idusuario                    ");
        sql.append("	FROM jogosdesejados AS jd JOIN meusjogos AS mj   ");
        sql.append("	WHERE jd.idjogo=mj.idjogo AND mj.idusuario=1     ");
        sql.append("	                                                 ");
        sql.append("	UNION                                            ");
        sql.append("	                                                 ");
        sql.append("	SELECT DISTINCT	mj.idusuario                     ");
        sql.append("	FROM meusjogos AS mj JOIN jogosdesejados AS jd   ");
        sql.append("	WHERE mj.idjogo=jd.idjogo AND jd.idusuario=1);   ");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if(resultado.getCount() > 0){
            resultado.moveToFirst();
            do{
                Usuario user = new Usuario();

                user.idusuario = resultado.getInt( resultado.getColumnIndexOrThrow("idusuario"));
                user.nomeusuario = resultado.getString(resultado.getColumnIndexOrThrow("nomeusuario"));
                user.email = resultado.getString(resultado.getColumnIndexOrThrow("email"));
                user.senha = resultado.getString(resultado.getColumnIndexOrThrow("senha"));

                usuarios.add(user);
            }while(resultado.moveToNext());
        }
        return usuarios;
    }

}
