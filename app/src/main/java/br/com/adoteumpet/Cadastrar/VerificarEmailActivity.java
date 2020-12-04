package br.com.adoteumpet.Cadastrar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.adoteumpet.HomeActivity;
import br.com.adoteumpet.R;

public class VerificarEmailActivity extends AppCompatActivity {
    //Button
    private Button mbttnContinueEmail;

    //TextView
    private TextView tvShowUserEmail;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificareml);

        //Declanrando o Button
        mbttnContinueEmail = (Button) findViewById(R.id.bttnContinueEmail);

        //Declanrando o TextView
        tvShowUserEmail = (TextView) findViewById(R.id.tvShowUserEmail);

        //Declarando o Progressbar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);


        //Enviar o email de verificação
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Se a tarefar der certo
                if(task.isSuccessful()){
                    //Chamar a função setInfo
                    setInfo();

                    //Caso Contrario
                } else {
                    //Se der erro mostrar no Toast qual é a mensagem de erro
                    String mensagem = task.getException().getMessage();
                    Toast.makeText(VerificarEmailActivity.this, "Ocorreu um erro: " + mensagem, Toast.LENGTH_LONG).show();
                }
            }
        });

        //Ao clicar no botão de Continuae Email
        mbttnContinueEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iniciar um CountDownTimer
                new CountDownTimer(1500, 200) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //Deixar o progressbar de load visivel
                        mProgressBarLoad.setVisibility(View.VISIBLE);

                        //Inativar o botão de continuar
                        mbttnContinueEmail.setEnabled(false);
                    }
                    @Override
                    //Quando o tempo acabar
                    public void onFinish() {
                        //Deixar o progressBar invisivel
                        mProgressBarLoad.setVisibility(View.INVISIBLE);

                        //Ativar o botão Continuar
                        mbttnContinueEmail.setEnabled(true);

                        //Iniciar a intent HomeActivity
                        Intent intent = new Intent(VerificarEmailActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }.start();
            }
        });


    }
    private void setInfo(){
        //Mostrar o email do usuario na tela
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvShowUserEmail.setText(new StringBuffer("").append(user.getEmail()));
    }

}