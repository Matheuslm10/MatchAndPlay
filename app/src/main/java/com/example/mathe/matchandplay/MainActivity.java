package com.example.mathe.matchandplay;

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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mathe.matchandplay.Adapter.UsuarioAdapter;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.BD.Criaconexao;
import com.example.mathe.matchandplay.BD.UsuarioRepositorio;
import com.example.mathe.matchandplay.Cadastro.CadastroUsuario;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Criaconexao conectorDoBD = new Criaconexao();
    LinearLayout layoutContentLista;
    ListView usuarioListView;
    private ArrayAdapter<Usuario> adapter;
    ArrayList<Usuario> arrayListUsuario;
    ArrayAdapter<Usuario> arrayAdapterUsuario;

    private FirebaseAuth usuarioFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuarioFirebase = ConfiguracaoFireBase.getFirebaseAutenticacao();

        //relacionando a ListView com o Adapter de Usuarios
        usuarioListView = findViewById(R.id.usuariosList);
        adapter = new UsuarioAdapter(this, arrayListUsuario);
        usuarioListView.setAdapter(adapter);





        usuarioListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int
                    position, long id) {
                Usuario userEnviado = arrayAdapterUsuario.getItem(position);
                Intent it = new Intent(MainActivity.this, MostraUsuario.class);
                it.putExtra("chave_pessoa", userEnviado);
                startActivity(it);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, MostraUsuario.class);
                startActivity(it);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    public void preencheLista(){
        int idusuario = 0; //trocar
        arrayListUsuario = conectorDoBD.usuarioRepositorio.retornaInteressadosEProprietarios(idusuario);

        if(usuarioListView!=null) {
            arrayAdapterUsuario = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, arrayListUsuario);
            usuarioListView.setAdapter(arrayAdapterUsuario);
        }
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


}
