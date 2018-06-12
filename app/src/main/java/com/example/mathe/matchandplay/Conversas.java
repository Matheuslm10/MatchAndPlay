package com.example.mathe.matchandplay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mathe.matchandplay.Adapter.UsuarioAdapterChat;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Conversas extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<Usuario> arrayListUsuarios = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    Usuario encontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setTitle("Minhas Conversas");

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        pd = new ProgressDialog(Conversas.this);
        pd.setMessage("Loading...");
        pd.show();

        //PESQUISAR OS CHILDS QUE COMECEM COM O ID DO USUARIO LOGADO, e mostrar isso numa lista.
        String url = "https://matchandplay-ce8a9.firebaseio.com/mensagens.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Conversas.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDadosChat.idChatWith = arrayListUsuarios.get(position).getIdusuario();
                UserDadosChat.nomeChatWith = arrayListUsuarios.get(position).getNomeusuario();
                startActivity(new Intent(Conversas.this, Chat.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                //SE A STRING CONTER O ID DO LOGADO, ADICIONA NA LISTA DE COVERSA O USUARIO DO ID QUE ESTÁ à DIREITA DO ID DO LOGADO
                if(key.contains(UserDadosChat.idLogado)) {
                    String idChatWith = key.replace(UserDadosChat.idLogado+"_", "");
                    encontraUsuario(idChatWith);//esse método encontra  E ADICIONA NA LISTA
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void encontraUsuario(String id){
        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("idusuario").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        encontrado = issue.getValue(Usuario.class);
                        if(encontrado!=null){
                            arrayListUsuarios.add(encontrado);
                            totalUsers++;
                        }

                    }
                    Collections.sort(arrayListUsuarios, new SortBasedOnName(1));
                    if(totalUsers <1){
                        noUsersText.setVisibility(View.VISIBLE);
                        usersList.setVisibility(View.GONE);
                    }
                    else{
                        noUsersText.setVisibility(View.GONE);
                        usersList.setVisibility(View.VISIBLE);
                        usersList.setAdapter(new UsuarioAdapterChat(Conversas.this, arrayListUsuarios));
                    }

                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}