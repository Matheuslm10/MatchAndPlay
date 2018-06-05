package com.example.mathe.matchandplay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathe.matchandplay.Adapter.IDAdapter;
import com.example.mathe.matchandplay.Adapter.UsuarioAdapter;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView usuarioListView;
    private ArrayAdapter<Usuario> adapter;
    private ArrayAdapter<Usuario> adapterMatches;
    private ArrayAdapter<String> adapterIds;
    ArrayList<Usuario> arrayListUsuario;
    ArrayList<Usuario> arrayListMatches;
    ArrayList<String> mjLogado = new ArrayList<>();
    ArrayList<String> jdLogado = new ArrayList<>();

    private DatabaseReference firebase;
    private DatabaseReference meusjogos;
    private ValueEventListener valueEventListenerUsuarios;
    private ValueEventListener valueEventListenerMeusJogos;
    private ValueEventListener listenerUsuarioLogado;
    private FirebaseAuth usuarioFirebase;
    Usuario logado = new Usuario();
    String currentEmail = "";

    //dados no menu lateral
    private TextView nomeUsuario;
    private TextView emailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Match's");

        firebase = ConfiguracaoFireBase.getFireBase().child("usuario");
        usuarioFirebase = ConfiguracaoFireBase.getFirebaseAutenticacao();

        //relacionando a ListView com o Adapter de Usuarios
        arrayListUsuario = new ArrayList<>();
        arrayListMatches = new ArrayList<>();
        usuarioListView = findViewById(R.id.usuariosList);
        arrayListUsuario.clear();
        adapter = new UsuarioAdapter(this, arrayListUsuario);
        usuarioListView.setAdapter(adapter);
        //adapterMatches = new UsuarioAdapter(this, arrayListMatches);


        //colocando os dados do usuario logado no menu lateral
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        nomeUsuario = (TextView) header.findViewById(R.id.textViewNomeUsuario);
        emailUsuario = (TextView) header.findViewById(R.id.textViewEmailUsuario);
        currentEmail = usuarioFirebase.getCurrentUser().getEmail();
        Query query = firebase.orderByChild("email").equalTo(currentEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        logado = issue.getValue(Usuario.class);
                        Toast.makeText(MainActivity.this, logado.getNomeusuario(), Toast.LENGTH_SHORT).show();
                        nomeUsuario.setText(logado.getNomeusuario());
                        emailUsuario.setText(logado.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //preenchendo a lista dos match's
        valueEventListenerUsuarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListUsuario.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario novosUsuarios = dados.getValue(Usuario.class);
                    //Toast.makeText(MainActivity.this, novosUsuarios.getNomeusuario(), Toast.LENGTH_SHORT).show();
                    arrayListUsuario.add(novosUsuarios);
                }
                //////////////////////////////////////////////
                preencheVetoresJogosLogado();
                ArrayList<Usuario> matches = new ArrayList<>();

                //verifica quem quer jogar os jogos do logado
                for(String jogoX : mjLogado){
                    for(Usuario user :arrayListUsuario) {
                        if(user.getJogosdesejados().contains(jogoX)){
                            user.setInteressado(true);
                            matches.add(user);
                            Toast.makeText(MainActivity.this, "match: "+user.getNomeusuario(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                //verifica quem possui os jogos que o logado deseja
                for(String jogoX : jdLogado){
                    for(Usuario user :arrayListUsuario) {
                        if(user.getMeusjogos().contains(jogoX)){
                            user.setProprietario(true);
                            if(!matches.contains(user)){
                                matches.add(user);
                                Toast.makeText(MainActivity.this, "match: "+user.getNomeusuario(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                arrayListUsuario.clear();
                arrayListUsuario = matches;
                //////////////////////////////////////////////
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };



        /*
        //preenchendo a lista dos match's CORRETO
        valueEventListenerUsuarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListUsuario.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario novoUsuario = dados.getValue(Usuario.class);
                    arrayListUsuario.add(novoUsuario);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        */

        usuarioListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Usuario usuarioSelecionado = adapter.getItem(position);
                Intent it = new Intent(MainActivity.this, MostraUsuario.class);
                it.putExtra("email_user_selected", usuarioSelecionado.getEmail());
                startActivity(it);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void preencheVetoresJogosLogado(){
        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("email").equalTo(currentEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Usuario users = issue.getValue(Usuario.class);
                        mjLogado = users.getMeusjogos();
                        jdLogado = users.getJogosdesejados();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public ArrayList<Usuario> retornaListaDeMatchs(){
        ArrayList<Usuario> matches = new ArrayList<>();

        //verifica quem quer jogar os jogos do logado
        for(String jogoX : mjLogado){
            for(Usuario user :arrayListUsuario) {
                if(user.getJogosdesejados().contains(jogoX)){
                    user.setInteressado(true);
                    matches.add(user);
                    Toast.makeText(MainActivity.this, "match: "+user.getNomeusuario(), Toast.LENGTH_SHORT).show();

                }
            }
        }

        //verifica quem possui os jogos que o logado deseja
        for(String jogoX : jdLogado){
            for(Usuario user :arrayListUsuario) {
                if(user.getMeusjogos().contains(jogoX)){
                    user.setProprietario(true);
                    if(!matches.contains(user)){
                        matches.add(user);
                        Toast.makeText(MainActivity.this, "match: "+user.getNomeusuario(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        return matches;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent it = new Intent(this, MeusJogos.class);
            startActivity(it);

        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(this, JogosDesejados.class);
            startActivity(it);

        } else if (id == R.id.nav_slideshow) {
            Intent it = new Intent(this, Faq.class);
            startActivity(it);

        } else if (id == R.id.nav_manage) {
            deslogarUsuario();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deslogarUsuario() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerUsuarios);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerUsuarios);
    }


}
