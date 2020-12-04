package br.com.adoteumpet.Cadastrar.Adotar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import br.com.adoteumpet.Cadastrar.VerificarEmailActivity;
import br.com.adoteumpet.R;

public class CadastrarAdotarhistoriaActivity extends AppCompatActivity {
    //EditText
    private EditText metProfileAdoptbio;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Button
    private Button mbttnConfirm;

    //Toats
    private Toast mtoatscantback;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    //Precionar botão voltar
    public void onBackPressed()
    {
        //Configurar a mensagem do toast mtoatscantback
        mtoatscantback = Toast.makeText(CadastrarAdotarhistoriaActivity.this, "Não é possivel voltar.", Toast.LENGTH_LONG);

        //Iniciador contador
        new CountDownTimer(1000,200){
            @Override
            //Inicio do tempo
            public void onTick(long millisUntilFinished) {
                //Iniciar visualização do tost
                mtoatscantback.show();
            }
            @Override
            //Fim do tempo
            public void onFinish() {
                //Cancelar a visualização do tost
                mtoatscantback.cancel();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrarhistoria);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Declarar EditText Historia
        metProfileAdoptbio = (EditText) findViewById(R.id.etProfilePetbio);

        //Declarar Botão
        mbttnConfirm = (Button) findViewById(R.id.bttnConfirm);

        //Declarar ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Ao clicar no botão
        mbttnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passar o texto para String
                final String sAdoptbio = metProfileAdoptbio.getText().toString();

                if(sAdoptbio.isEmpty()){
                    //Alerta
                    Toast.makeText(CadastrarAdotarhistoriaActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else {
                    //Contador
                    new CountDownTimer(1500, 200) {
                        @Override
                        //No Inicio do cotnador
                        public void onTick(long millisUntilFinished) {
                            //Deixar o progressbar Visivel
                            mProgressBarLoad.setVisibility(View.VISIBLE);

                            //Desativar o botão Confirmar
                            mbttnConfirm.setEnabled(false);

                            //Salvar o Uid do usuario atual na string userId
                            String userId = mAuth.getCurrentUser().getUid();

                            //Definindo o caminho do banco de dados
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Adopt");

                            //Salvando os dados no database
                            Map userInfo = new HashMap<>();
                            userInfo.put("AdoptBio", sAdoptbio);
                            currentUserDb.updateChildren(userInfo);
                        }
                        @Override
                        //No Fim do contador
                        public void onFinish() {
                            //Iniciar nova tela
                            Intent intent = new Intent(CadastrarAdotarhistoriaActivity.this, VerificarEmailActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }.start();
                }
            }
        });

    }
}