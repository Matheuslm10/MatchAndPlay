package com.example.mathe.matchandplay.Cadastro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathe.matchandplay.Adapter.JogoAdapter;
import com.example.mathe.matchandplay.R;

import java.util.Arrays;

public class CadastrarJogosDesejados extends AppCompatActivity {
    //CheckList
    private TextView naotenho_editar;
    private ListView listaDeJogosDesejados_editar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_jogos_desejados);
        setTitle("Jogos Desejados");

        //CheckList
        listaDeJogosDesejados_editar = (ListView) findViewById(R.id.lista_jogosDesejados_editar);
        String[] items ={"Dama", "Truco", "Xadrez", "Jogo da Vida","Banco Imobiliário", "Baralho", "Detetive", "Perfil", "War"};
        JogoAdapter adapter = new JogoAdapter(Arrays.asList(items), this);
        listaDeJogosDesejados_editar.setAdapter(adapter);
        setListViewHeightBasedOnItems(listaDeJogosDesejados_editar);

    }
    public boolean setListViewHeightBasedOnItems(ListView listView) {
        listView.setDivider(null);
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *  (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = (totalItemsHeight + totalDividersHeight);
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

}
