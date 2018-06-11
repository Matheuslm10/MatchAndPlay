package com.example.mathe.matchandplay;

import java.util.Comparator;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;

public class SortBasedOnName implements Comparator
{
    public int compare(Object o1, Object o2)
    {

        Usuario dd1 = (Usuario)o1;
        Usuario dd2 = (Usuario)o2;
        return dd1.getNomeusuario().compareToIgnoreCase(dd2.getNomeusuario());
    }

}