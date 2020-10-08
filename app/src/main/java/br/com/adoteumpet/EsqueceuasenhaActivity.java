package br.com.adoteumpet;

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

public class EsqueceuasenhaActivity extends AppCompatActivity {
    private Button bttnResetarSenha;
    private EditText etEmailUsuario;
    private FirebaseAuth mAuth;
    private ProgressBar vProgressBarCarregar;
    private Toast vtoastError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceuasenha);

        //Declarando botão
        bttnResetarSenha = (Button) findViewById(R.id.bttnResetarSenha);

        //Declarando EditText
        etEmailUsuario = (EditText) findViewById(R.id.etEmailUsuario);

        //Declarando ProgressBar
        vProgressBarCarregar = (ProgressBar) findViewById(R.id.ProgressBarCarregar);

        //Definindo firebase
        mAuth = FirebaseAuth.getInstance();

        //Ao Cliccar no botão ResetarSenha
        bttnResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Coloca o que o usuario digitou no editText para a string Email
                String email = etEmailUsuario.getText().toString();

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
                                //Desativar botão e aparecer um Progress bar
                                vProgressBarCarregar.setVisibility(View.VISIBLE);
                                bttnResetarSenha.setEnabled(false);

                                //Tempo de duração do Toast
                                new CountDownTimer(1500, 200) {
                                    @Override
                                        public void onTick(long millisUntilFinished) {
                                            vProgressBarCarregar.setVisibility(View.VISIBLE);
                                            bttnResetarSenha.setEnabled(false);
                                        }
                                    @Override
                                        public void onFinish() {
                                            //Enviar alerta e ir para tela de home
                                            Toast.makeText(EsqueceuasenhaActivity.this, "Foi enviado um Link para seu e-mail para redefinição da senha.", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(EsqueceuasenhaActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                }.start();

                            //Caso de algum erro durante esse processo ativar um alerta informando o erro
                            } else {
                                //Desativar botão e aparecer um Progress bar
                                vProgressBarCarregar.setVisibility(View.VISIBLE);
                                bttnResetarSenha.setEnabled(false);

                                //Caso de erro informe o erro
                                String messagem = task.getException().getMessage();
                                vtoastError = Toast.makeText(EsqueceuasenhaActivity.this, "Ocorreu um erro: " + messagem, Toast.LENGTH_LONG);

                                //Tempo de duração do Toast
                                new CountDownTimer(1000, 200) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        vtoastError.show();
                                    }
                                    @Override
                                    public void onFinish() {
                                        vProgressBarCarregar.setVisibility(View.INVISIBLE);
                                        bttnResetarSenha.setEnabled(true);
                                        vtoastError.cancel();
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