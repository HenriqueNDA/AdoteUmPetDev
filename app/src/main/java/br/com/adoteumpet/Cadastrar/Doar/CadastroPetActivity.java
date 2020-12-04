package br.com.adoteumpet.Cadastrar.Doar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

import br.com.adoteumpet.Cadastrar.CadastrarFotoActivity;
import br.com.adoteumpet.R;

public class CadastroPetActivity extends AppCompatActivity {
    //Spiiner
    private Spinner mactvSpeciePet , mactvAgePet, mactvSizePet;
    //Button
    private Button mbttnConfirm;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Toast
    private Toast mtoatscantback;

    //Fireasbe
    private FirebaseAuth mAuth;

    @Override
    //Precionar botão voltar
    public void onBackPressed()
    {
        //Configurar a mensagem do toast mtoatscantback
        mtoatscantback = Toast.makeText(CadastroPetActivity.this, "Não é possivel voltar.", Toast.LENGTH_LONG);

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
        setContentView(R.layout.activity_cadastropet);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Declarar Spiner Specie do Pet
        mactvSpeciePet = (Spinner) findViewById(R.id.actvSpeciePet);
        //ArrayAdapter Porte
        ArrayAdapter adapterPorte = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_ItemsSpecie,
                R.layout.color_spinner_layout
        );
        mactvSpeciePet.setAdapter(adapterPorte);

        //Declarar Spiner Age do Pet
        mactvAgePet = (Spinner) findViewById(R.id.actvAgePet);
        //ArrayAdapter Idade Pet
        ArrayAdapter adapterIdade = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_ItemsAge,
                R.layout.color_spinner_layout
        );
        mactvAgePet.setAdapter(adapterIdade);

        //Declarar Spiner Size do Pet
        mactvSizePet = (Spinner) findViewById(R.id.actvSizePet);
        //ArrayAdapter Tamanho
        ArrayAdapter adapterTipo = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_ItemsSize,
                R.layout.color_spinner_layout
        );
        mactvSizePet.setAdapter(adapterTipo);

        //Declarar Botão
        mbttnConfirm = (Button) findViewById(R.id.bttnConfirm);

        //Declarar ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Ao clicar no botão
        mbttnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passar o texto para String
                final String sSpeciePet = mactvSpeciePet.getSelectedItem().toString();
                final String sAgePet = mactvAgePet.getSelectedItem().toString();
                final String sSizePet = mactvSizePet.getSelectedItem().toString();


                //Se o sSpeciePet, sAgePet , sSizePet for igual "Selecione uma opção"
                if(sSpeciePet.equals("Selecione uma opção") || sAgePet.equals("Selecione uma opção") || sSizePet.equals("Selecione uma opção")){
                    //Enviar uma alerta para preencher todos os campos
                    Toast.makeText(CadastroPetActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();

                    //Caso Contrario
                } else {
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
                            userInfo.put("PetSpecie", sSpeciePet);
                            userInfo.put("PetAge", sAgePet);
                            userInfo.put("PetSize", sSizePet);

                            //Atualizar o banco de dados com o os dados do Map
                            currentUserDb.updateChildren(userInfo);
                        }
                        @Override
                        public void onFinish() {
                            //Fim do tempo
                            Intent intent = new Intent(CadastroPetActivity.this, CadastroPetinfoActivity.class);
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