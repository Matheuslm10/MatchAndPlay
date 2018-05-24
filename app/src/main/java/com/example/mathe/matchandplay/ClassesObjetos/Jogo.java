package com.example.mathe.matchandplay.ClassesObjetos;

import java.io.Serializable;

public class Jogo  implements Serializable {
    private int idjogo;
    private String nomejogo;

    public int getIdjogo() {
        return idjogo;
    }

    public void setIdjogo(int idjogo) {
        this.idjogo = idjogo;
    }

    public String getNomejogo() {
        return nomejogo;
    }

    public void setNomejogo(String nomejogo) {
        this.nomejogo = nomejogo;
    }
}
