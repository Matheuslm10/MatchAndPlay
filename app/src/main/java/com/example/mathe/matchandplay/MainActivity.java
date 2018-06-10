package com.example.mathe.matchandplay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    ArrayList<Usuario> arrayListUsuario;
    ArrayList<Usuario> allUsersArray;
    ArrayList<Usuario> arrayListMatches;
    ArrayList<String> mjLogado = new ArrayList<>();
    ArrayList<String> jdLogado = new ArrayList<>();

    private FirebaseAuth usuarioFirebase;
    FirebaseAuth.AuthStateListener listener;
    Usuario logado = new Usuario();
    String currentEmail = "";
    private DatabaseReference firebase;

    //dados no menu lateral
    private TextView nomeUsuario;
    private TextView emailUsuario;
    public ImageView fotoPerfil;

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

        preencheVetoresJogosLogado();

        Query consulta = firebase;
        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListUsuario.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario novoUsuario = dados.getValue(Usuario.class);

                    arrayListUsuario.add(novoUsuario);
                }
                atualizarListaMatches();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Trata o clique em um item da lista de match's
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

        //Trata o menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void atualizarListaMatches(){
        allUsersArray = arrayListUsuario;
        arrayListMatches = retornaListaDeMatchs();
        adapter = new UsuarioAdapter(this, arrayListMatches);
        usuarioListView.setAdapter(adapter);
    }

    public void preencheVetoresJogosLogado(){
        currentEmail = usuarioFirebase.getCurrentUser().getEmail().toString();
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

        //se o logado tiver pelo menos 1 jogo em "meus jogos" faz isso...
        if(mjLogado!=null){
            //verifica quem quer jogar os jogos do logado
            for(String jogoX : mjLogado){
                for(Usuario user : allUsersArray) {
                    //se o usuario a ser comparado tiver pelo menos 1 jogo seus "jogos desejados" faz isso...
                    if(user.getJogosdesejados()!=null){
                        if(user.getJogosdesejados().contains(jogoX)){
                            if(!matches.contains(user)){
                                user.setInteressado(true);
                                matches.add(user);
                            }

                        }
                    }
                }

            }
        }

        //se o logado tiver pelo menos 1 jogo em "jogos desejados" faz isso...
        if(jdLogado!=null){
            //verifica quem possui os jogos que o logado deseja
            for(String jogoX : jdLogado){
                for(Usuario user :allUsersArray) {
                    //se o usuario a ser comparado tiver pelo menos 1 jogo seus "meus jogos" faz isso...
                    if(user.getMeusjogos()!=null){
                        if(user.getMeusjogos().contains(jogoX)){
                            if(!matches.contains(user)){
                                user.setProprietario(true);
                                matches.add(user);
                            }
                        }
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

        if (id == R.id.nav_mj) {
            Intent it = new Intent(this, MeusJogos.class);
            it.putExtra("email_user_selected_mj", logado.getEmail());
            startActivity(it);

        } else if (id == R.id.nav_jd) {
            Intent it = new Intent(this, JogosDesejados.class);
            it.putExtra("email_user_selected_jd", logado.getEmail());
            startActivity(it);

        } else if (id == R.id.nav_conta) {
            Intent it = new Intent(this, Conta.class);
            startActivity(it);

        }  else if (id == R.id.nav_info) {
            Intent it = new Intent(this, Faq.class);
            startActivity(it);

        }  else if (id == R.id.nav_sair) {
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

    private void atualizaMenuLateral(){
        //colocando os dados do usuario logado no menu lateral
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.textViewNomeUsuario);
        emailUsuario = header.findViewById(R.id.textViewEmailUsuario);
        fotoPerfil = header.findViewById(R.id.imageViewFotoUsuario);
        currentEmail = usuarioFirebase.getCurrentUser().getEmail();

        /*setando a foto a partir da autenticação
        if (usuarioFirebase != null) {

            if (usuarioFirebase.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(MainActivity.this)
                        .load(usuarioFirebase.getCurrentUser().getPhotoUrl().toString())
                        .into(fotoPerfil);
                //passar como extra no bundle pro menu lateral.
            }
        }
        */
        Query query = firebase.orderByChild("email").equalTo(currentEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        logado = issue.getValue(Usuario.class);
                        nomeUsuario.setText(logado.getNomeusuario());
                        emailUsuario.setText(logado.getEmail());
                        Glide.with(MainActivity.this).load(logado.getUrlFotoPerfil()).into(fotoPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        atualizaMenuLateral();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
