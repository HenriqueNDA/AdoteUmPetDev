package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueceuasenhaActivity extends AppCompatActivity {
    private Button bttnResetarSenha;
    private EditText etEmailUsuario;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceuasenha);

        //Declarando botão
        bttnResetarSenha = (Button) findViewById(R.id.bttnResetarSenha);

        //Declarando EditText
        etEmailUsuario = (EditText) findViewById(R.id.etEmailUsuario);

        //Definindo firebase
        mAuth = FirebaseAuth.getInstance();

        //Ao Cliccar no botão ResetarSenha
        bttnResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmailUsuario.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(EsqueceuasenhaActivity.this, "Escreva primeiro o seu endereço de e-mail válido", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EsqueceuasenhaActivity.this, "Por favor verifique seu Email.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EsqueceuasenhaActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                String messagem = task.getException().getMessage();
                                Toast.makeText(EsqueceuasenhaActivity.this, "Ocorreu um erro: " + messagem, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}