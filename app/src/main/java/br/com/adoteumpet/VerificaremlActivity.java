package br.com.adoteumpet;

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

public class VerificaremlActivity extends AppCompatActivity {
    Button mbttnContinuarEmail;
    TextView tvUserEmail;
    private ProgressBar mProgressBarCarregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificareml);

        mProgressBarCarregar = (ProgressBar) findViewById(R.id.ProgressBarCarregar);

        mbttnContinuarEmail = (Button) findViewById(R.id.bttnContinuarEmail);

        tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);


        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    setInfo();
                } else {
                    String mensagem = task.getException().getMessage();
                    Toast.makeText(VerificaremlActivity.this, "Ocorreu um erro: " + mensagem, Toast.LENGTH_LONG).show();
                }
            }
        });

        mbttnContinuarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CountDownTimer(1500, 200) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mProgressBarCarregar.setVisibility(View.VISIBLE);
                        mbttnContinuarEmail.setEnabled(false);
                    }
                    @Override
                    public void onFinish() {
                        mProgressBarCarregar.setVisibility(View.INVISIBLE);
                        mbttnContinuarEmail.setEnabled(true);
                        Intent intent = new Intent(VerificaremlActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }.start();
            }
        });


    }
    private void setInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvUserEmail.setText(new StringBuffer("").append(user.getEmail()));
    }

}