package com.example.mathe.matchandplay;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MostraUsuario extends AppCompatActivity {
    String emailSelecionado;
    private TextView nomeUsuario;
    private TextView emailUsuario;
    ListView listViewMeusJogos;
    ListView listViewJogosDesejados;
    ArrayAdapter<String> adapterMJ;
    ArrayAdapter<String> adapterJD;
    private ImageView fotoPerfil;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_usuario);
        setTitle("Perfil");
        Intent it = getIntent();
        emailSelecionado =  it.getStringExtra("email_user_selected");

        nomeUsuario = (TextView) findViewById(R.id.txtMostraNome);
        emailUsuario = (TextView) findViewById(R.id.txtMostraEmail);
        listViewMeusJogos = findViewById(R.id.lvMeusJogos);
        listViewJogosDesejados = findViewById(R.id.lvJogosDesejados);
        fotoPerfil = findViewById(R.id.imgUsuario);
        progressBar = findViewById(R.id.progressbarMostraUsuario);
        progressBar.setVisibility(View.VISIBLE);

        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("email").equalTo(emailSelecionado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Usuario users = issue.getValue(Usuario.class);
                        nomeUsuario.setText(users.getNomeusuario());
                        emailUsuario.setText(users.getEmail());
                        Glide.with(MostraUsuario.this).load(users.getUrlFotoPerfil()).into(fotoPerfil);
                        adapterMJ = new ArrayAdapter<String>(MostraUsuario.this, android.R.layout.simple_list_item_1, users.getMeusjogos());
                        listViewMeusJogos.setAdapter(adapterMJ);
                        setListViewHeightBasedOnItems(listViewMeusJogos);
                        adapterJD = new ArrayAdapter<String>(MostraUsuario.this, android.R.layout.simple_list_item_1, users.getJogosdesejados());
                        listViewJogosDesejados.setAdapter(adapterJD);
                        setListViewHeightBasedOnItems(listViewJogosDesejados);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Um hora vai ter! ;D", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
