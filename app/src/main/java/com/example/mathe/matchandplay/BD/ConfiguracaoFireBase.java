package com.example.mathe.matchandplay.BD;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFireBase {

    private static DatabaseReference referenciaFireBase;
    private static FirebaseAuth autenicacao;

    public static DatabaseReference getFireBase(){
        if(referenciaFireBase == null){
            referenciaFireBase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFireBase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenicacao == null){
            autenicacao = FirebaseAuth.getInstance();
        }
        return autenicacao;
    }
}
