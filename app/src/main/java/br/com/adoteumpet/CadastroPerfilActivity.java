package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import br.com.adoteumpet.Cadastrar.CadastrarFotoActivity;

public class CadastroPerfilActivity extends AppCompatActivity {
    //EditText
    private EditText metProfileUsername, metProfileUseremail, metProfileUserpassword, metProfileUserpasswordconfirm;

    //Checkbox
    private CheckBox mCheckBoxProfileUserprivpolicy, mCheckBoxProfileUsernewsletter;

    //Button
    private Button mbttnConfirm;

    //Toats
    private Toast mtoasterror, mtoastfields, mtoastprivpolicy, mtoastcheckpasswords;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        //EditText
        metProfileUsername = (EditText) findViewById(R.id.etProfileUsername);
        metProfileUseremail = (EditText) findViewById(R.id.etProfileUseremail);
        metProfileUserpassword = (EditText) findViewById(R.id.etProfileUserpassword);
        metProfileUserpasswordconfirm = (EditText) findViewById(R.id.etProfileUserpasswordconfirm);

        //CheckBox
        mCheckBoxProfileUserprivpolicy = (CheckBox) findViewById(R.id.CheckBoxProfileUserprivpolicy);
        mCheckBoxProfileUsernewsletter = (CheckBox) findViewById(R.id.CheckBoxProfileUsernewsletter);

        //ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Button
        mbttnConfirm = (Button) findViewById(R.id.bttnConfirm);

        //Pegar o id do current user e salvar na string user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        //Logar Automaticamente
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        mbttnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Passar os textos dos campos para a string
                final String sUsername = metProfileUsername.getText().toString();
                final String sUseremail = metProfileUseremail.getText().toString();
                String sUserpassword = metProfileUserpassword.getText().toString();
                String sUserpasswordConfirm = metProfileUserpasswordconfirm.getText().toString();

                //Se os campos sUsername, sUseremail, sUserpassword, sUserpasswordConfirm estiverem vazios
                if(sUsername.isEmpty() || sUseremail.isEmpty() || sUserpassword.isEmpty() || sUserpasswordConfirm.isEmpty()){
                    //Toast para mensagem de preencher todos os campos
                    mtoastfields = Toast.makeText(CadastroPerfilActivity.this, "Preencha todos os campos ", Toast.LENGTH_SHORT);

                    //Contador para mostrar o Toast
                    new CountDownTimer(1000, 200) {
                        @Override
                        //No Inicio do contador
                        public void onTick(long millisUntilFinished) {
                            //Mostrar o toast
                            mtoastfields.show();
                        }
                        @Override
                        //Quando acabar o contador
                        public void onFinish() {
                            //Cancelar a visualização do toast
                            mtoastfields.cancel();
                        }
                    }.start();
                    //Se os campos estivem preenchidos
                } else{
                    //Se o Check Box de termos estiver selecionado
                    if(mCheckBoxProfileUserprivpolicy.isChecked()){
                        //Se sUserpassword estiver igual sUserpasswordConfirm
                        if(sUserpassword.equals(sUserpasswordConfirm)){
                            mAuth.createUserWithEmailAndPassword(sUseremail, sUserpassword).addOnCompleteListener(CadastroPerfilActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Se der algum erro na hora de cadastrar
                                    if(!task.isSuccessful()){
                                        //Mostrar o ProgressBar
                                        mProgressBarLoad.setVisibility(View.VISIBLE);

                                        //Desativar o botão confirm
                                        mbttnConfirm.setEnabled(false);

                                        //Salvar o erro na hora de cadastro na string serrorsignup
                                        String serrorsingup = task.getException().getMessage();
                                        mtoasterror = Toast.makeText(CadastroPerfilActivity.this, "Ocorreu um erro: " + serrorsingup, Toast.LENGTH_LONG);

                                        //Tempo de duração do Toast
                                        new CountDownTimer(1800, 400) {
                                            @Override
                                            //No Inicio do contador
                                            public void onTick(long millisUntilFinished) {
                                                //Mostrar o toast
                                                mtoasterror.show();
                                            }
                                            @Override
                                            //Quando acabar o contador
                                            public void onFinish() {
                                                //Deixar o ProgressBar invisivel
                                                mProgressBarLoad.setVisibility(View.INVISIBLE);

                                                //Ativar o botão confirm
                                                mbttnConfirm.setEnabled(true);

                                                //Cancelar a visualização do toast
                                                mtoasterror.cancel();
                                            }
                                        }.start();
                                        //Caso não de erro na hora de cadastrar
                                    } else {
                                        //Salvar o nome do usuario no firebase
                                        userProfile();

                                        //Iniciar um contador para cadastrar as informações no database
                                        new CountDownTimer(1500, 200) {
                                            @Override
                                            //No Inicio do contador
                                            public void onTick(long millisUntilFinished) {
                                                //Mostrar o ProgressBar
                                                mProgressBarLoad.setVisibility(View.VISIBLE);

                                                //Desativar o botão confirm
                                                mbttnConfirm.setEnabled(false);
                                            }
                                            @Override
                                            //Quando acabar o contador
                                            public void onFinish() {
                                                //Id do usuario atual
                                                String userId = mAuth.getCurrentUser().getUid();

                                                //Definindo o database e informando as info
                                                DatabaseReference currentUserDb1 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                                Map userInfo = new HashMap<>();
                                                userInfo.put("ProfileUsername", sUsername);
                                                if (mCheckBoxProfileUsernewsletter.isChecked()){
                                                    userInfo.put("ProfileNewsletter", "True");
                                                } else {
                                                    userInfo.put("ProfileNewsletter", "False");
                                                }

                                                //Salvar as informações no banco de addos
                                                currentUserDb1.updateChildren(userInfo);

                                                //Definindo a intent para ir para tela "TAL"
                                                Intent intent = new Intent(CadastroPerfilActivity.this, CadastrarFotoActivity.class);

                                                //Iniciar intent
                                                startActivity(intent);
                                                finish();
                                                return;
                                            }
                                        }.start();
                                    }
                                }
                            });
                            //Se as senhas não estiverem iguais
                        } else{
                            //Toast para mensagem de conferir a senha
                            mtoastcheckpasswords = Toast.makeText(CadastroPerfilActivity.this, "Senhas digitadas não conferem !", Toast.LENGTH_LONG);

                            //Tempo de duração do Toast
                            new CountDownTimer(1800, 400) {
                                @Override
                                //No Inicio do contador
                                public void onTick(long millisUntilFinished) {
                                    //Mostrar o toast
                                    mtoastcheckpasswords.show();
                                }
                                @Override
                                //Quando acabar o contador
                                public void onFinish() {
                                    //Cancelar a visualização do toast
                                    mtoastcheckpasswords.cancel();
                                }
                            }.start();
                        }
                        //Caso o termo de adesão não estiver selecionado
                    } else {
                        //Mostrar uma mensagem de erro
                        mtoastprivpolicy = Toast.makeText(CadastroPerfilActivity.this, "Você deve aceitar os termos de uso.", Toast.LENGTH_LONG);

                        //Tempo de duração do Toast
                        new CountDownTimer(1800, 400) {
                            @Override
                            //No Inicio do contador
                            public void onTick(long millisUntilFinished) {
                                //Mostrar o toast
                                mtoastprivpolicy.show();
                            }
                            @Override
                            //Quando acabar o contador
                            public void onFinish() {
                                //Cancelar a visualização do toast
                                mtoastprivpolicy.cancel();
                            }
                        }.start();
                    }
                }
            }
        });
    }

    private void userProfile()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(metProfileUsername.getText().toString().trim())
                    //.setPhotoUri(Uri.parse(""))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) { }});
        }
    }

    public void irParaTermo(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/uni9.edu.br/adote-um-pet/termo-de-adesao"));
        startActivity(intent);
    }

    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}