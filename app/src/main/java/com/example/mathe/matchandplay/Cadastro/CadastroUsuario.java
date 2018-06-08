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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.mathe.matchandplay.Adapter.JogoAdapter;
import com.example.mathe.matchandplay.BD.Base64Custom;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.BD.Criaconexao;
import com.example.mathe.matchandplay.BD.JogoRepositorio;
import com.example.mathe.matchandplay.BD.PreferenciasAndroid;
import com.example.mathe.matchandplay.ClassesObjetos.Jogo;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.example.mathe.matchandplay.Login;
import com.example.mathe.matchandplay.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    private Button btnCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private TextView tenho;
    private TextView naotenho;
    private ListView listaDeMeusJogos;
    private ListView listaDeJogosDesejados;
    private static final int CHOOSE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        setTitle("Cadastro");

        imagemEnviada = findViewById(R.id.uploadFoto);
        progressBar = (ProgressBar) findViewById(R.id.progressbarCadastro);
        edtCadNome = (EditText) findViewById(R.id.edtCadNome);
        edtCadEmail = (EditText) findViewById(R.id.edtCadEmail);
        edtCadSenha = (EditText) findViewById(R.id.edtCadSenha);
        edtCadConfirmaSenha = (EditText) findViewById(R.id.edtCadConfirmaSenha);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        tenho = (TextView) findViewById(R.id.tenho);
        naotenho = (TextView) findViewById(R.id.naotenho);

        imagemEnviada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCadSenha.getText().toString().equals(edtCadConfirmaSenha.getText().toString())) {
                    usuario = new Usuario();
                    usuario.setNomeusuario(edtCadNome.getText().toString());
                    usuario.setEmail(edtCadEmail.getText().toString());
                    usuario.setSenha(edtCadSenha.getText().toString());
                    //pra meusjogos
                    //pra jogosdesejados

                    //saveUserInformation();
                    cadastrarUsuario();

                } else {
                    Toast.makeText(CadastroUsuario.this, "As senhas não são correspondentes", Toast.LENGTH_LONG).show();
                }
            }
        });

        //CheckList
        listaDeMeusJogos = (ListView) findViewById(R.id.lista_meusJogos);
        listaDeJogosDesejados = (ListView) findViewById(R.id.lista_jogosDesejados);
        String[] items ={"Dama", "Truco", "Xadrez", "Jogo da Vida","Banco Imobiliário", "Baralho", "Detetive", "Perfil", "War"};
        JogoAdapter adapter = new JogoAdapter(Arrays.asList(items), this);
        listaDeMeusJogos.setAdapter(adapter);
        listaDeJogosDesejados.setAdapter(adapter);
        setListViewHeightBasedOnItems(listaDeMeusJogos);
        setListViewHeightBasedOnItems(listaDeJogosDesejados);

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
                                            Toast.makeText(CadastroUsuario.this, "SALVOU A FOTO!!!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }else{
                        Toast.makeText(CadastroUsuario.this, "Não Salvou a foto!!!", Toast.LENGTH_LONG).show();
                    }
                    usuario.setUrlFotoPerfil(profileImageUrl);
                    usuario.setIdusuario(idenficadorUsuario);
                    usuario.salvar();

                    PreferenciasAndroid preferenciasAndroid = new PreferenciasAndroid(CadastroUsuario.this);
                    preferenciasAndroid.salvarUsuarioPrefencias(idenficadorUsuario, usuario.getNomeusuario());

                    abrirLoginUsuario();
                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = " Digite uma senha mais forte, contendo no mínimo 8 caracteres de letras e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = " O e-mail digitado é inválido, digite um novo e-mail.";
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
        finish();
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
