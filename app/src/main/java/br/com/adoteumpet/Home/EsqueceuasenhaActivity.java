package br.com.adoteumpet.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.com.adoteumpet.HomeActivity;
import br.com.adoteumpet.R;

public class EsqueceuasenhaActivity extends AppCompatActivity {
    //Buton
    private Button mbttnResetpassword;

    //EditText
    private EditText metUseremail;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Toast
    private Toast mtoastError;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceuasenha);

        //Declarando botão
        mbttnResetpassword = (Button) findViewById(R.id.bttnResetpassoword);

        //Declarando EditText
        metUseremail = (EditText) findViewById(R.id.etUseremail);

        //Declarando ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Definindo firebase
        mAuth = FirebaseAuth.getInstance();

        //Ao Clicar no botão ResetarSenha
        mbttnResetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Coloca o que o usuario digitou no editText para a string Email
                String email = metUseremail.getText().toString();

                //Caso a string email esteja sem texto ira enviar um alerta.
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(EsqueceuasenhaActivity.this, "E-mail digitado não é válido !", Toast.LENGTH_LONG).show();

                //Caso contraio
                } else {

                    //Enviar o email de resetar a senha para o email digitado na String
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //Caso o email senha valido e deu tudo certo neste envio ativar um alerta para verificar o email e ir para tela Home
                            if(task.isSuccessful()){
                                //Deixar o Progressbar Visivel
                                mProgressBarLoad.setVisibility(View.VISIBLE);

                                //Desativar o botão mbttnResetpassword
                                mbttnResetpassword.setEnabled(false);

                                //Tempo de duração do Toast
                                new CountDownTimer(1500, 200) {
                                    @Override
                                        //Inicio do contador
                                        public void onTick(long millisUntilFinished) {
                                        //Deixar o progressBar Visivel
                                        mProgressBarLoad.setVisibility(View.VISIBLE);

                                        //Desativar o botão mbttnResetpassword
                                        mbttnResetpassword.setEnabled(false);
                                        }
                                    @Override
                                        //Fim do contador
                                        public void onFinish() {
                                            //Enviar alerta
                                            Toast.makeText(EsqueceuasenhaActivity.this, "Foi enviado um Link para seu e-mail para redefinição da senha.", Toast.LENGTH_LONG).show();

                                            //Ir para tela de home
                                            Intent intent = new Intent(EsqueceuasenhaActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                }.start();

                            //Caso de algum erro durante esse processo ativar um alerta informando o erro
                            } else {
                                //Deixar o progressBar Visivel
                                mProgressBarLoad.setVisibility(View.VISIBLE);

                                //Desativar o botão mbttnResetpassword
                                mbttnResetpassword.setEnabled(false);

                                //Caso de erro informe o erro
                                String messagem = task.getException().getMessage();
                                mtoastError = Toast.makeText(EsqueceuasenhaActivity.this, "Ocorreu um erro: " + messagem, Toast.LENGTH_LONG);

                                //Tempo de duração do Toast
                                new CountDownTimer(1000, 200) {
                                    @Override
                                    //Inicio do contador
                                    public void onTick(long millisUntilFinished) {
                                        //Mostrar a mensagem de erro
                                        mtoastError.show();
                                    }
                                    @Override
                                    //Fim do contador
                                    public void onFinish() {
                                        //Deixar o ProgressBar invisivel
                                        mProgressBarLoad.setVisibility(View.INVISIBLE);

                                        //Ativar o botão mbttnResetpassword
                                        mbttnResetpassword.setEnabled(true);

                                        //Cancelar a exibição do Toast de erro
                                        mtoastError.cancel();
                                    }
                                }.start();
                            }
                        }
                    });
                }
            }
        });
    }
}