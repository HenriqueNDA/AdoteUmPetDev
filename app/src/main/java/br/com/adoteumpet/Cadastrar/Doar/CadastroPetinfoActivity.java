package br.com.adoteumpet.Cadastrar.Doar;

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

import br.com.adoteumpet.R;

public class CadastroPetinfoActivity extends AppCompatActivity {
    //EditText
    private EditText metProfilePetname, metProfilePetbio;

    //Toast
    private Toast mtoatscantback;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Botão
    private Button mbttnConfirm;

    //Fireasbe
    private FirebaseAuth mAuth;
    @Override
    //Precionar botão voltar
    public void onBackPressed()
    {
        //Configurar a mensagem do toast mtoatscantback
        mtoatscantback = Toast.makeText(CadastroPetinfoActivity.this, "Não é possivel voltar.", Toast.LENGTH_LONG);

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
        setContentView(R.layout.activity_cadastro_petinfo);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //EditText Nome pet
        metProfilePetname = (EditText) findViewById(R.id.etProfilePetname);

        //EditText Historia pet
        metProfilePetbio = (EditText) findViewById(R.id.etProfilePetbio);

        //Declarar ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Declarar Botão
        mbttnConfirm = (Button) findViewById(R.id.bttnConfirm);

        //Ao clicar no botão confirmar
        mbttnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sPetname = metProfilePetname.getText().toString();
                final String sPetbio = metProfilePetbio.getText().toString();

                //Se o campo metProfilePetname ou metProfilePethistory estiverem vazio enviar um alerta de erro
                if(sPetname.isEmpty() || sPetbio.isEmpty()){
                    //Enviar uma alerta para preencher todos os campos
                    Toast.makeText(CadastroPetinfoActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();

                    //Caso Contrario
                } else{
                    //Contador
                    new CountDownTimer(1500, 200) {
                        @Override
                        //Inicio do tempo
                        public void onTick(long millisUntilFinished) {
                            //Deixar o ProgressBar visivel
                            mProgressBarLoad.setVisibility(View.VISIBLE);

                            //Desativar o botão confirm
                            mbttnConfirm.setEnabled(false);

                            //Salvando o Uid do usuario atual na string userId
                            String userId = mAuth.getCurrentUser().getUid();

                            //Definindo o caminho do banco de dados
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Donate");

                            //Inputando os dados em um Map
                            Map userInfo = new HashMap<>();
                            userInfo.put("Petname", sPetname);
                            userInfo.put("PetBio", sPetbio);

                            //Atualizar o banco de dados com o os dados do Map
                            currentUserDb.updateChildren(userInfo);
                        }
                        @Override
                        public void onFinish() {
                            //Fim do tempo
                            Intent intent = new Intent(CadastroPetinfoActivity.this, CadastroPetFotoActivity.class);
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