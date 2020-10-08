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
import android.widget.ProgressBar;
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
    private Button vbttnConfirmar;
    private EditText vtxNomeCompleto, vtxEmail, vtxSenha, vtxConfirmarSenha;
    private CheckBox vCheckBoxTermoAdesao;
    private Toast vtoastError, vtoastcampos, vtoasttermo;
    private RadioGroup vrdAdotarDoar;
    private ProgressBar vProgressBarCarregar;

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
        vbttnConfirmar = (Button) findViewById(R.id.bttnConfirmar);

        //Declarar Textos
        vtxNomeCompleto = (EditText) findViewById(R.id.txNomeCompleto);
        vtxEmail = (EditText) findViewById(R.id.txEmail);
        vtxSenha = (EditText) findViewById(R.id.txSenha);
        vtxConfirmarSenha = (EditText) findViewById(R.id.txConfirmarSenha);

        //Declarar Checkbox de adesao
        vCheckBoxTermoAdesao = (CheckBox) findViewById(R.id.CheckBoxTermoAdesao);

        //Declarar Radio Grupo
        vrdAdotarDoar = (RadioGroup) findViewById(R.id.radioGroup);

        //Declarar ProgressBar
        vProgressBarCarregar = (ProgressBar) findViewById(R.id.ProgressBarCarregar);

        //Ao Clicar no botão confirmar
        vbttnConfirmar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

       int selectId = vrdAdotarDoar.getCheckedRadioButtonId();
       final RadioButton radioButton = (RadioButton) findViewById(selectId);


       //Passar o texto para String
       final String nomecompleto = vtxNomeCompleto.getText().toString();
       final String email = vtxEmail.getText().toString();
       final String senha = vtxSenha.getText().toString();
       final String confirmarsenha = vtxConfirmarSenha.getText().toString();

        //Vefificar se todos os campos estão preenchidos
        if (nomecompleto.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarsenha.isEmpty() || radioButton == null) {
           //Se os campos estiverem vazio envie uma alerta
           vtoastcampos = Toast.makeText(CadastrarActivity.this, "Preencha todos os campos ", Toast.LENGTH_SHORT);

           //Tempo de duração da alerta
            new CountDownTimer(1000, 200) {
                @Override
                 public void onTick(long millisUntilFinished) {
                    vtoastcampos.show();
                 }
                 @Override
                  public void onFinish() {
                     vtoastcampos.cancel();
                  }
            }.start();
            //Se os campos estivem preenchidos
        } else {
          // Verificar se o Termo de adesso esta marcado
          if(vCheckBoxTermoAdesao.isChecked()) {

                //Verificar se a senhas sao iguais
                if (senha.equals(confirmarsenha)) {

                    //Cadastrar a conta usando o email e senha digitado
                    mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastrarActivity.this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {

                         //Caso algum dado não seja valido/ou de erro no cadastro
                         if (!task.isSuccessful()) {
                             //Desativar botão e ativar Progressbar
                             vProgressBarCarregar.setVisibility(View.VISIBLE);
                             vbttnConfirmar.setEnabled(false);

                             //Enviar um Toast de erro
                             String mensagem = task.getException().getMessage();
                             vtoastError = Toast.makeText(CadastrarActivity.this, "Ocorreu um erro: " + mensagem, Toast.LENGTH_LONG);

                             //Tempo de duração do Toast
                             new CountDownTimer(1500, 200) {
                              @Override
                              public void onTick(long millisUntilFinished) {

                                  vtoastError.show();
                              }
                               @Override
                               public void onFinish() {
                                      vProgressBarCarregar.setVisibility(View.INVISIBLE);
                                      vbttnConfirmar.setEnabled(true);
                                      vtoastError.cancel();
                               }
                               }.start();
                                //Caso o cadastro seja sucedido
                                 } else {
                                     //Seta userId com o Uid do usuario
                                     String userId = mAuth.getCurrentUser().getUid();

                                     //Salva os dados do User, No Nome
                                     DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId).child("nome");
                                     currentUserDb.setValue(nomecompleto);

                                     //Tempo de duração do Toast
                                     new CountDownTimer(1500, 200) {
                                         @Override
                                         public void onTick(long millisUntilFinished) {
                                             //Desativar botão e ativar Progressbar
                                             vProgressBarCarregar.setVisibility(View.VISIBLE);
                                             vbttnConfirmar.setEnabled(false);
                                         }
                                         @Override
                                         public void onFinish() {
                                             //Vai para tela de verificar email
                                             Intent intent = new Intent(CadastrarActivity.this, VerificaremlActivity.class);
                                             startActivity(intent);
                                         }
                                     }.start();
                                 }
                         }
                     });

                     //Caso as senhas não sejam iguais
                     } else {
                       //Envie um alerta
                       vtoasttermo = Toast.makeText(CadastrarActivity.this, "Senhas digitadas não conferem !", Toast.LENGTH_LONG);

                       //Tempo de duração do Toast
                       new CountDownTimer(1800, 400) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                vtoasttermo.show();
                            }
                            @Override
                            public void onFinish() {
                                vtoasttermo.cancel();
                             }
                        }.start();
                     }
                    } else {
                        //Se os campos estiverem vazio envie um Tost
                        vtoasttermo = Toast.makeText(CadastrarActivity.this, "Você deve aceitar os termos de uso.", Toast.LENGTH_LONG);

                        //Tempo de duração do Toast
                        new CountDownTimer(1800, 400) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                vtoasttermo.show(); }
                            @Override
                            public void onFinish() {
                                vtoasttermo.cancel();
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