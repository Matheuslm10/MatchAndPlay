package com.example.mathe.matchandplay;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mathe.matchandplay.BD.Base64Custom;
import com.example.mathe.matchandplay.BD.ConfiguracaoFireBase;
import com.example.mathe.matchandplay.Cadastro.CadastroUsuario;
import com.example.mathe.matchandplay.ClassesObjetos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class Conta extends AppCompatActivity {

    private EditText nome;
    private EditText senha;
    private ImageView fotoPerfil;
    private Uri uriFotoPerfil;
    private Button cancelar;
    private Button salvar;
    private ProgressBar progressBar;
    private FirebaseAuth usuarioFirebase;
    private String profileImageUrl;
    private static final int CHOOSE_IMAGE = 101;
    private DatabaseReference firebase;
    private AlertDialog alerta;
    private Usuario usuarioAlterado;
    private boolean trocouFoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        setTitle("Minha Conta");

        usuarioFirebase = ConfiguracaoFireBase.getFirebaseAutenticacao();

        nome = findViewById(R.id.edtAlterarNome);
        senha = findViewById(R.id.edtAlterarSenha);
        fotoPerfil = findViewById(R.id.imgUsuario);
        cancelar = findViewById(R.id.btnCancelar);
        salvar = findViewById(R.id.btnSalvar);
        progressBar =findViewById(R.id.progressbarConta);


        //PREENCHENDO OS DADOS
        final String emailSelecionado = usuarioFirebase.getCurrentUser().getEmail();
        Query query = ConfiguracaoFireBase.getFireBase().child("usuario").orderByChild("email").equalTo(emailSelecionado);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        usuarioAlterado = issue.getValue(Usuario.class);

                        //setando a foto:
                        if (usuarioFirebase != null) {
                            if (usuarioFirebase.getCurrentUser().getPhotoUrl() != null) {
                                Glide.with(Conta.this)
                                        .load(usuarioFirebase.getCurrentUser().getPhotoUrl().toString())
                                        .into(fotoPerfil);
                            }
                        }
                        //setando nome e email
                        nome.setHint(usuarioAlterado.getNomeusuario());
                        senha.setHint(usuarioAlterado.getSenha());
                        String password = "";
                        for(int i=0; i<(usuarioAlterado.getSenha().length()); i++){
                            password += "•";
                        }
                        senha.setHint(password);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        cancelar.setEnabled(false);
        salvar.setEnabled(false);

        nome.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                cancelar.setEnabled(true);
                cancelar.setBackgroundResource(R.drawable.formatobotao);
                salvar.setEnabled(true);
                salvar.setBackgroundResource(R.drawable.formatobotao);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        senha.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                cancelar.setEnabled(true);
                cancelar.setBackgroundResource(R.drawable.formatobotao);
                salvar.setEnabled(true);
                salvar.setBackgroundResource(R.drawable.formatobotao);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }
            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });




    }

    public void salvarAlteracao(View v){
        String conteudoNome = nome.getText().toString();
        if(conteudoNome.length()> 0){
            usuarioAlterado.setNomeusuario(conteudoNome);
        }
        String conteudoSenha = senha.getText().toString();
        if(conteudoSenha.length()> 0){
            usuarioAlterado.setSenha(conteudoSenha);
        }

        salvarImagem();
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);

    }

    public void cancelarAlteracao(View v){
        finish();
        Toast.makeText(this, "Alteração cancelada!", Toast.LENGTH_SHORT).show();

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriFotoPerfil = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriFotoPerfil);
                fotoPerfil.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
                trocouFoto = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriFotoPerfil != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriFotoPerfil)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                            cancelar.setEnabled(true);
                            cancelar.setBackgroundResource(R.drawable.formatobotao);
                            salvar.setEnabled(true);
                            salvar.setBackgroundResource(R.drawable.formatobotao);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Conta.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }




    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void salvarImagem() {
        if (usuarioFirebase != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            usuarioFirebase.getCurrentUser().updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            }
                        }
                    });
            usuarioAlterado.setUrlFotoPerfil(profileImageUrl);
            usuarioAlterado.salvar();
            Toast.makeText(this, "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show();
        }else{
            if(trocouFoto){
                Toast.makeText(Conta.this, "Erro: a imagem não foi salva.", Toast.LENGTH_SHORT).show();
            }
            usuarioAlterado.salvar();
        }

    }

    private void aposDelecaoConta(){
        finish();
        Intent it = new Intent(this, Login.class);
        startActivity(it);
    }

    public void deletarContaUsuario(View view){
        //cria o gerador do alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Conta.this);

        //Define o título
        builder.setTitle("Atenção!");

        //define uma mensagem
        builder.setMessage("Você tem certeza que deseja deletar sua conta?");

        //define botão sim
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usuarioFirebase.getCurrentUser().delete();
                String chave = Base64Custom.codificarBase64(usuarioFirebase.getCurrentUser().getEmail());
                firebase = ConfiguracaoFireBase.getFireBase().child("usuario");
                firebase.child(chave).removeValue();

                Toast.makeText(Conta.this, "A sua conta foi deletada com sucesso!", Toast.LENGTH_LONG).show();
                aposDelecaoConta();
            }
        });

        //define o botão não

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

}
