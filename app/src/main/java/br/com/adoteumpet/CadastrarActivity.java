package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CadastrarActivity extends AppCompatActivity {
    private Button bttnConfirmar;
    private EditText txNomeCompleto, txEmail, txSenha, txConfirmarSenha;
    private CheckBox cbTermoAdesao, cvNovidades;
    private Toast toastError, toastcampos, toasttermo;
    private RadioGroup rdAdotarDoar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        //Declarar Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        //Declarar Botão
        bttnConfirmar = (Button) findViewById(R.id.bttnConfirmar);

        //Declarar Textos
        txNomeCompleto = (EditText) findViewById(R.id.txNomeCompleto);
        txEmail = (EditText) findViewById(R.id.txEmail);
        txSenha = (EditText) findViewById(R.id.txSenha);
        txConfirmarSenha = (EditText) findViewById(R.id.txConfirmarSenha);

        //Declarar Checkbox de adesao
        cbTermoAdesao = (CheckBox) findViewById(R.id.cbTermoAdesao);

        //Declarar Radio Grupo
        rdAdotarDoar = (RadioGroup) findViewById(R.id.radioGroup);

        //Ao Clicar no botão confirmar
        bttnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectId = rdAdotarDoar.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectId);


                //Passar o texto para String
                final String nomecompleto = txNomeCompleto.getText().toString();
                final String email = txEmail.getText().toString();
                final String senha = txSenha.getText().toString();
                final String confirmarsenha = txConfirmarSenha.getText().toString();



                //Vefificar se todos os campos estão preenchidos
                if (nomecompleto.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarsenha.isEmpty() || radioButton == null) {

                    //Se os campos estiverem vazio envie uma alerta
                    toastcampos = Toast.makeText(CadastrarActivity.this, "Complete todos os campos", Toast.LENGTH_SHORT);

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
                    // Verificar se o Termo de adesso esta marcado
                    if(cbTermoAdesao.isChecked()) {

                        //Verificar se a senhas sao iguais
                        if (senha.equals(confirmarsenha)) {

                                //Cadastrar a conta usando o email e senha digitado
                                mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastrarActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        //Caso algum dado não seja valido/ou de erro no cadastro
                                        if (!task.isSuccessful()) {

                                            //Enviar um Toast de erro
                                            String mensagem = task.getException().getMessage();
                                            toastError = Toast.makeText(CadastrarActivity.this, "Ocorreu um erro: " + mensagem, Toast.LENGTH_LONG);

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

                                            //Caso o cadastro seja sucedido
                                        } else {
                                            String userId = mAuth.getCurrentUser().getUid();
                                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId).child("nome");
                                            currentUserDb.setValue(nomecompleto);
                                            Intent intent = new Intent(CadastrarActivity.this, VerificaremlActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });

                            //Caso as senhas não sejam iguais
                        } else {
                            //Envie um alerta
                            toasttermo = Toast.makeText(CadastrarActivity.this, "As senhas não estão iguais!!", Toast.LENGTH_LONG);

                            //Tempo de duração do Toast
                            new CountDownTimer(1800, 400) {
                                @Override
                                public void onTick(long millisUntilFinished) { toasttermo.show();
                                }
                                @Override
                                public void onFinish() {
                                    toasttermo.cancel();
                                }
                            }.start();
                        }
                    } else {
                        //Se os campos estiverem vazio envie um Tost
                        toasttermo = Toast.makeText(CadastrarActivity.this, "Você deve aceitar com os termos de adesão e uso.", Toast.LENGTH_LONG);

                        //Tempo de duração do Toast
                        new CountDownTimer(1800, 400) {
                            @Override
                            public void onTick(long millisUntilFinished) { toasttermo.show();
                            }
                            @Override
                            public void onFinish() {
                                toasttermo.cancel();
                            }
                        }.start();
                    }
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