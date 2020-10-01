package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private Button bttnCriarConta, bttnLogar, bttnEsqueceuSenha;
    private Toast toastVoltar, toastError, toastcampos;
    private EditText txEmail, txSenha;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    public void onBackPressed()
    {
        toastVoltar = Toast.makeText(HomeActivity.this, "Não é possivel voltar.", Toast.LENGTH_LONG);
        toastVoltar.show();
        new CountDownTimer(1000,200){
            @Override
            public void onTick(long millisUntilFinished) {
                toastVoltar.show();
            }
            @Override
            public void onFinish() {
                toastVoltar.cancel();
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Declarar Botoes
        bttnLogar = (Button) findViewById(R.id.bttnLogar);
        bttnCriarConta = (Button) findViewById(R.id.bttnCriarConta);
        bttnEsqueceuSenha = (Button) findViewById(R.id.bttnEsqueceuSenha);

        //Declarar Textos
        txEmail = (EditText) findViewById(R.id.txEmail);
        txSenha = (EditText) findViewById(R.id.txSenha);

        //Declarar Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    user.reload();
                    if (user.isEmailVerified()){
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };

        //Ao clicar no botão Cadastrar-se
        bttnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CadastrarActivity.class);
                startActivity(intent);
            }
        });

        //Ao clicar no botão Esqueceu a senha
        bttnEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EsqueceuasenhaActivity.class);
                startActivity(intent);
            }
        });

        //Ao Clicar no botão Logar
        bttnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Passar o texto para String
                final String email = txEmail.getText().toString();
                final String senha = txSenha.getText().toString();

                //Verificar se os campos E-mail/Senha estão preenchidos
                if (email.isEmpty() || senha.isEmpty()) {
                    //Se os campos estiverem vazio envie uma alerta
                    toastcampos = Toast.makeText(HomeActivity.this, "Complete todos os campos", Toast.LENGTH_SHORT);

                    //Tempo de duração da alerta
                    new CountDownTimer(1000, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) { toastcampos.show();
                        }
                        @Override
                        public void onFinish() {
                            toastcampos.cancel();
                        }
                    }.start();

                    //Se os campos estivem preenchidos
                } else {
                    //Logar com o email e senha informado
                    mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(HomeActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //Caso não encontre o login informado
                            if (!task.isSuccessful()) {

                                //Enviar um alerta de erro
                                String mensagem = task.getException().getMessage();
                                toastError = Toast.makeText(HomeActivity.this, "Ocorreu um erro: " + mensagem, Toast.LENGTH_LONG);

                                //Tempo de duração do Toast
                                new CountDownTimer(1000, 200) {
                                    @Override
                                    public void onTick(long millisUntilFinished) { toastError.show();
                                    }
                                    @Override
                                    public void onFinish() {
                                        toastError.cancel();
                                    }
                                }.start();

                            } else {
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    Toast.makeText(HomeActivity.this, "Logado", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else{
                                    Toast.makeText(HomeActivity.this, "Sua conta ainda não foi verificada! Favor verificar seu email!", Toast.LENGTH_LONG).show();
                                    mAuth.getCurrentUser().sendEmailVerification();
                                }
                            }
                        }
                    });
                }
            }
        });


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