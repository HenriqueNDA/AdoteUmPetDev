package br.com.adoteumpet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class CadastropetActivity extends AppCompatActivity {
    private EditText vtxNome;
    private Spinner vActvPortePet;
    private Spinner vActvIdadePet;
    private Spinner vActvTipoPet;
    private Button vbttnConfirmar;
    private FirebaseAuth mAuth;
    private ProgressBar vProgressBarCarregar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastropet);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Declarar EditText Nome Pet
        vtxNome = (EditText) findViewById(R.id.txNomePet);

        //Declarar Spiner Porte Pet
        vActvPortePet = (Spinner) findViewById(R.id.ActvTipoPet);

        //Declarar Spiner Idade Pet
        vActvIdadePet = (Spinner) findViewById(R.id.ActvIdadePet);

        //Declarar Spiner Tipo Pet
        vActvTipoPet = (Spinner) findViewById(R.id.ActvTipoPet);

        //Declarar Botão
        vbttnConfirmar = (Button) findViewById(R.id.bttnConfirmarPet);

        //Declarar ProgressBar
        vProgressBarCarregar = (ProgressBar) findViewById(R.id.ProgressBarCarregar);

        //
        ArrayAdapter adapterPorte = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_ItemsPorte,
                R.layout.color_spinner_layout
        );
        vActvPortePet.setAdapter(adapterPorte);

        //
        ArrayAdapter adapterIdade = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_ItemsIdade,
                R.layout.color_spinner_layout
        );
        vActvIdadePet.setAdapter(adapterIdade);

        //
        ArrayAdapter adapterTipo = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_ItemsTipo,
                R.layout.color_spinner_layout
        );
        vActvTipoPet.setAdapter(adapterTipo);


        //Ao clicar no botão
        vbttnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Passar o texto para String
                final String portepet = vActvPortePet.getSelectedItem().toString();
                final String idadepet = vActvIdadePet.getSelectedItem().toString();
                final String tipopet = vActvTipoPet.getSelectedItem().toString();
                final String nomepet =  vtxNome.getText().toString();

                //Se o Nome Pet ou Porte Pet ou Idade Pet estiver com a opção "Selecione uma opção" seleciona envie um alerta
                if(nomepet.isEmpty()|| portepet.equals("Selecione uma opção") || idadepet.equals("Selecione uma opção") || tipopet.equals("Selecione uma opção")){
                    //Alerta
                    Toast.makeText(CadastropetActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();

                    //Caso Contrario
                } else {
                    new CountDownTimer(1500, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //Desativar botão e ativar Progressbar
                            vProgressBarCarregar.setVisibility(View.VISIBLE);
                            vbttnConfirmar.setEnabled(false);
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Doar").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("nome", nomepet);
                            userInfo.put("Pet Porte", portepet);
                            userInfo.put("Pet Idade", idadepet);
                            userInfo.put("Pet", tipopet);
                            currentUserDb.updateChildren(userInfo);
                        }
                        @Override
                        public void onFinish() {
                            Intent intent = new Intent(CadastropetActivity.this, VerificaremlActivity.class);
                            startActivity(intent);
                        }
                    }.start();
                }
            }
        });

    }
}