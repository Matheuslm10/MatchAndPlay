package com.example.mathe.matchandplay.Cadastro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mathe.matchandplay.Adapter.JogoAdapter;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class CadastrarJogosDesejados extends AppCompatActivity {
    //CheckList
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerJogo;
    private ArrayList<Jogo> arrayListJogos;
    private ArrayAdapter<Jogo> adapter;
    private ListView checklistJogosDesejadosEditar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_jogos_desejados);
        setTitle("Jogos Desejados");

        firebase = ConfiguracaoFireBase.getFireBase().child("jogo");
        arrayListJogos = new ArrayList<>();
        adapter = new JogoAdapter(this, arrayListJogos);

        //CheckList
        checklistJogosDesejadosEditar = (ListView) findViewById(R.id.lista_jogosDesejados_editar);
        checklistJogosDesejadosEditar.setAdapter(adapter);
        setListViewHeightBasedOnItems(checklistJogosDesejadosEditar);

        valueEventListenerJogo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListJogos.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Jogo arrayListJogoNovo = dados.getValue(Jogo.class);

                    arrayListJogos.add(arrayListJogoNovo);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

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

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerJogo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerJogo);
    }

}
