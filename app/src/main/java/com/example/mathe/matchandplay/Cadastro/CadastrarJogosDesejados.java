package com.example.mathe.matchandplay.Cadastro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.mathe.matchandplay.R;

public class CadastrarJogosDesejados extends AppCompatActivity {

    private CheckBox xadrez, truco, uno, dama, banco_imobiliario, jogo_vida, war, detetive, imagem_acao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_jogos_desejados);
        setTitle("Jogos Desejados");

        xadrez = findViewById(R.id.xadrez);
        truco = findViewById(R.id.truco);
        uno = findViewById(R.id.uno);
        dama = findViewById(R.id.dama);
        banco_imobiliario = findViewById(R.id.banco_imobiliario);
        jogo_vida = findViewById(R.id.jogo_vida);
        war = findViewById(R.id.war);
        detetive = findViewById(R.id.detetive);
        imagem_acao = findViewById(R.id.imagem_acao);
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.truco:
                if (checked) {
                    // Put some meat on the sandwich
                } else
                    // Remove the meat
                    break;
            case R.id.xadrez:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;
            case R.id.dama:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;
            case R.id.detetive:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;
            case R.id.war:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;
            case R.id.banco_imobiliario:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;
            case R.id.jogo_vida:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;
            case R.id.uno:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;
            case R.id.imagem_acao:
                if (checked) {
                    // Cheese me
                } else
                    // I'm lactose intolerant
                    break;

                //TODO default
        }
    }

}
