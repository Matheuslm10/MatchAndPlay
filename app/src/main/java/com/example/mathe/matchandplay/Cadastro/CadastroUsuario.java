package com.example.mathe.matchandplay.Cadastro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mathe.matchandplay.Adapter.JogoAdapter;
import com.example.mathe.matchandplay.BD.Base64Custom;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.BD.PreferenciasAndroid;
import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.example.mathe.matchandplay.Login;
import com.example.mathe.matchandplay.R;
import com.example.mathe.matchandplay.SortBasedOnName;
import com.example.mathe.matchandplay.UserDadosChat;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CadastroUsuario extends AppCompatActivity {

    ImageView imagemEnviada;
    Uri uriImagemPerfil;
    String profileImageUrl;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private EditText edtCadNome;
    private EditText edtCadEmail;
    private EditText edtCadSenha;
    private EditText edtCadConfirmaSenha;
    private ListView checklistMeusJogos;
    private ListView checklistJogosDesejados;
    private LinearLayout linearLayoutMJ;
    private LinearLayout linearLayoutJD;
    private Button btnCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private static final int CHOOSE_IMAGE = 101;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerJogo;
    private ArrayList<Jogo> arrayListJogos;
    private JogoAdapter adapterMJ;
    private JogoAdapter adapterJD;
    private int qtdJogos = 0;
    private ArrayList<String> arrayVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        setTitle("Cadastro");

        mAuth = ConfiguracaoFireBase.getFirebaseAutenticacao();
        mAuth.signInAnonymously();

        firebase = ConfiguracaoFireBase.getFireBase().child("jogo");
        arrayListJogos = new ArrayList<>();
        arrayVazio = new ArrayList<>();
        arrayVazio.add("");
        adapterMJ = new JogoAdapter(CadastroUsuario.this, arrayListJogos, null);
        adapterJD = new JogoAdapter(CadastroUsuario.this, arrayListJogos, null);

        imagemEnviada = findViewById(R.id.uploadFoto);
        progressBar = (ProgressBar) findViewById(R.id.progressbarCadastro);
        edtCadNome = (EditText) findViewById(R.id.edtCadNome);
        edtCadEmail = (EditText) findViewById(R.id.edtCadEmail);
        edtCadSenha = (EditText) findViewById(R.id.edtCadSenha);
        edtCadConfirmaSenha = (EditText) findViewById(R.id.edtCadConfirmaSenha);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        checklistMeusJogos = (ListView) findViewById(R.id.lista_meusJogos);
        checklistJogosDesejados = (ListView) findViewById(R.id.lista_jogosDesejados);
        linearLayoutMJ =findViewById(R.id.linearLayoutMJ);
        linearLayoutJD = findViewById(R.id.linearLayoutJD);
        checklistMeusJogos.setAdapter(adapterMJ);
        checklistJogosDesejados.setAdapter(adapterJD);

        valueEventListenerJogo = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListJogos.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Jogo jogoNovo = dados.getValue(Jogo.class);

                    arrayListJogos.add(jogoNovo);
                }
                qtdJogos = arrayListJogos.size();
                setLinearLayoutHeightBasedOnItems();
                Collections.sort(arrayListJogos, new SortBasedOnName(2));
                adapterMJ.notifyDataSetChanged();
                adapterJD.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        imagemEnviada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCadSenha.getText().toString().equals(edtCadConfirmaSenha.getText().toString())
                        && edtCadSenha.getText().toString().length() > 0
                        && edtCadNome.getText().length() > 0
                        && edtCadEmail.getText().length() > 0
                        )
                {
                    usuario = new Usuario();
                    usuario.setNomeusuario(edtCadNome.getText().toString());
                    usuario.setEmail(edtCadEmail.getText().toString());
                    usuario.setSenha(edtCadSenha.getText().toString());
                    if(adapterMJ.getJogosSelecionados().isEmpty()){
                        usuario.setMeusjogos(arrayVazio);
                    }else{
                        usuario.setMeusjogos(adapterMJ.getJogosSelecionados());
                    }

                    //--------------------------
                    boolean existeRepeticao = false;
                    for(String jogo: usuario.getMeusjogos()){
                        if(adapterJD.getJogosSelecionados().contains(jogo)){
                            existeRepeticao = true;
                            break;
                        }
                    }
                    if(existeRepeticao){
                        Toast.makeText(CadastroUsuario.this, "Por favor, desmarque os jogos que você tem, na lista dos que você não tem.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(adapterJD.getJogosSelecionados().isEmpty()){
                            usuario.setMeusjogos(arrayVazio);
                        }else{
                            usuario.setJogosdesejados(adapterJD.getJogosSelecionados());
                        }
                        cadastrarUsuario();
                    }
                    //--------------------------

                } else {
                    if(edtCadSenha.getText().toString().length()<=0
                            || edtCadNome.getText().length() <= 0
                            || edtCadEmail.getText().length() <= 0
                            )
                    {
                        edtCadNome.requestFocus();
                        Toast.makeText(CadastroUsuario.this, "Há campos vazios", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(CadastroUsuario.this, "As senhas não são correspondentes!", Toast.LENGTH_LONG).show();
                        edtCadSenha.requestFocus();
                    }

                }
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImagemPerfil = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImagemPerfil);
                imagemEnviada.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriImagemPerfil != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriImagemPerfil)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                            System.out.println("URL DA FOTO SELECIONADA"+profileImageUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CadastroUsuario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void cadastrarUsuario() {

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(CadastroUsuario.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    String idenficadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    //aqui atualiza a foto de perfil do usuario firebase
                    if (usuarioFirebase != null && profileImageUrl != null) {
                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(profileImageUrl))
                                .build();

                        usuarioFirebase.updateProfile(profile)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //
                                        }
                                    }
                                });

                    }else{
                        //
                    }
                    usuario.setUrlFotoPerfil(profileImageUrl);
                    usuario.setIdusuario(idenficadorUsuario);
                    usuario.salvar();

                    PreferenciasAndroid preferenciasAndroid = new PreferenciasAndroid(CadastroUsuario.this);
                    preferenciasAndroid.salvarUsuarioPrefencias(idenficadorUsuario, usuario.getNomeusuario());

                    //deletando a autenticacao anonima q foi criada para fazer upload da foto de perfil
                    if(mAuth!=null){
                        if(mAuth.getCurrentUser().getEmail()==null){
                            mAuth.getCurrentUser().delete();
                        }
                    }
                    abrirLoginUsuario();

                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres de letras e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastrado no sistema.";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuario.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroUsuario.this, Login.class);
        startActivity(intent);
        mAuth.signOut();
        finish();

    }


    //ESSE METODO FOI ALTERADO PARA SETAR A ALTURA DO LINEAR LAYOUT, E NAO DO LISTVIEW
    public void setLinearLayoutHeightBasedOnItems() {

        ViewGroup.LayoutParams params = linearLayoutMJ.getLayoutParams();

        params.height = (qtdJogos * 80);
        linearLayoutMJ.setLayoutParams(params);
        linearLayoutJD.setLayoutParams(params);
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
