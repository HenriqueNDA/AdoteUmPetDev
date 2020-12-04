package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.adoteumpet.Adicional.localizacaoActivity;
import br.com.adoteumpet.Home.EsqueceuasenhaActivity;

public class HomeActivity extends AppCompatActivity {
    //Button
    private Button mbttnLogin;

    //Toasts
    private Toast mtoastError, mtoastfields;

    //EditText
    private EditText mtxUseremail, mtxUserpassword;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    //Se o usuario apertar o botão de voltar
    public void onBackPressed()
    {
        //Finalizar aplicativo
        finishAffinity();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Botão
        mbttnLogin = (Button) findViewById(R.id.bttnLogar);

        //EditTexts
        mtxUseremail = (EditText) findViewById(R.id.txUseremail);
        mtxUserpassword = (EditText) findViewById(R.id.txUserpassword);

        //ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    user.reload();
                    if (user.isEmailVerified()){
                        Intent intent = new Intent(HomeActivity.this, localizacaoActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };

        //Ao Clicar no botão Logar
        mbttnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passar o texto para String
                final String email = mtxUseremail.getText().toString();
                final String senha = mtxUserpassword.getText().toString();

                //Verificar se os campos E-mail/Senha estão preenchidos
                if (email.isEmpty() || senha.isEmpty()) {
                    //Se os campos estiverem vazio envie uma alerta
                    mtoastfields = Toast.makeText(HomeActivity.this, "Preencha todos os campos ", Toast.LENGTH_SHORT);

                    //Tempo de duração da alerta
                    new CountDownTimer(1000, 200) {
                        @Override
                        //No Começo do contador
                        public void onTick(long millisUntilFinished) {
                            //Mostrar Toast
                            mtoastfields.show();
                        }
                        @Override
                        //No fim do contador
                        public void onFinish() {
                            //Cancelar o Toast
                            mtoastfields.cancel();
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
                                mtoastError = Toast.makeText(HomeActivity.this, "Ocorreu um erro: " + mensagem, Toast.LENGTH_LONG);

                                //Tempo de duração do Toast
                                new CountDownTimer(1500, 200) {
                                    @Override
                                    //No começo do contador
                                    public void onTick(long millisUntilFinished) {
                                        //Mostrar toast de erro
                                        mtoastError.show();

                                        //Deixar o progressbar visivel
                                        mProgressBarLoad.setVisibility(View.VISIBLE);

                                        //Desativar o botão login
                                        mbttnLogin.setEnabled(false);
                                    }
                                    @Override
                                    //No fim do contador
                                    public void onFinish() {
                                        //Deixar o progressBar invisivel
                                        mProgressBarLoad.setVisibility(View.INVISIBLE);

                                        //Ativar o botão de login
                                        mbttnLogin.setEnabled(true);

                                        //Cancelar o toast
                                        mtoastError.cancel();
                                    }
                                }.start();

                            } else {
                                //Se o usuario atual estiver com o email verificado
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    //Ir para a tela de localização
                                    Intent intent = new Intent(HomeActivity.this, localizacaoActivity.class);
                                    startActivity(intent);

                                    //Caso Contrario
                                } else{
                                    //Mostar um alerta
                                    Toast.makeText(HomeActivity.this, "Verifique seu e-mail, é necessário verificar a conta", Toast.LENGTH_LONG).show();

                                    //Enviar email para verificação da conta
                                    mAuth.getCurrentUser().sendEmailVerification();
                                }
                            }
                        }
                    });
                }
            }
        });


    }

    //ir para a tela de cadastro
    public void gotoSignup(View view) {
        Intent intent = new Intent(HomeActivity.this, CadastroPerfilActivity.class);
        startActivity(intent);
    }

    //Ir para de de esqueceu a senha
    public void gotoLastpassword(View view) {
        Intent intent = new Intent(HomeActivity.this, EsqueceuasenhaActivity.class);
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