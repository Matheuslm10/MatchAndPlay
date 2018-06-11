package com.example.mathe.matchandplay;

import java.util.Comparator;

import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;

public class SortBasedOnName implements Comparator {

    private int tipo;

    public SortBasedOnName(int tipo){
        this.tipo = tipo;
    }
    public int compare(Object o1, Object o2){

        int result = -2;
        if(tipo == 1){
            Usuario dd1 = (Usuario)o1;
            Usuario dd2 = (Usuario)o2;
            result = dd1.getNomeusuario().compareToIgnoreCase(dd2.getNomeusuario());
        }else if(tipo == 2){
            Jogo dd1 = (Jogo)o1;
            Jogo dd2 = (Jogo)o2;
            result = dd1.getNome().compareToIgnoreCase(dd2.getNome());
        }

        return result;
    }

}