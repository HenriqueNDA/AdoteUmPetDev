package br.com.adoteumpet.Cadastrar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import br.com.adoteumpet.Cadastrar.Adotar.CadastrarAdotarhistoriaActivity;
import br.com.adoteumpet.Cadastrar.Doar.CadastroPetActivity;
import br.com.adoteumpet.R;

public class CadastrarTipocontaActivity extends AppCompatActivity {
    //TextView
    private TextView mtvProfiletypeaccount;

    //Radio Groupd
    private RadioGroup mradioGroupTypeaccount;

    //Radio Button
    private RadioButton mrbAdopt, mrbDonate;

    //Botão
    private Button mbttnConfirm;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Toasts
    private Toast mtoatscantback, mtoastfields;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;

    @Override
    //Precionar botão voltar
    public void onBackPressed()
    {
        //Configurar a mensagem do toast mtoatscantback
        mtoatscantback = Toast.makeText(CadastrarTipocontaActivity.this, "Não é possivel voltar.", Toast.LENGTH_LONG);

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
        setContentView(R.layout.activity_cadastrar_tipoconta);

        //TextView
        mtvProfiletypeaccount = (TextView) findViewById(R.id.tvProfiletypeaccount);

        //Radio group
        mradioGroupTypeaccount = (RadioGroup) findViewById(R.id.radioGroup);

        //Radio Button
        mrbAdopt = (RadioButton) findViewById(R.id.rbAdopt);
        mrbDonate = (RadioButton) findViewById(R.id.rbDonate);

        //ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Botão Confirm
        mbttnConfirm = (Button) findViewById(R.id.bttnConfirm);

        mAuth = FirebaseAuth.getInstance();

        mrbAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtvProfiletypeaccount.setText("Adotar");
            }
        });

        mrbDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtvProfiletypeaccount.setText("Doar");
            }
        });

        //Ao clicar no botão confirm
        mbttnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int selectId = mradioGroupTypeaccount.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectId);;

                if(!(radioButton == null)){
                    //Tempo de duração da alerta
                    new CountDownTimer(1000, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            //Deixar o ProgressBar Visivel
                            mProgressBarLoad.setVisibility(View.VISIBLE);

                            //Desativar o botão de confirm
                            mbttnConfirm.setEnabled(false);

                            //Salvar o Uid do usuario atual na string userId
                            String userId = mAuth.getCurrentUser().getUid();

                            //Definindo o caminho do banco de dados
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            //Salvando os dados no database
                            Map userInfo = new HashMap<>();
                            userInfo.put("ProfileTypeaccount", radioButton.getText().toString());
                            currentUserDb.updateChildren(userInfo);
                        }
                        @Override
                        public void onFinish() {
                            //Deixar o ProgressBar invisivel
                            mProgressBarLoad.setVisibility(View.INVISIBLE);

                            //Caso o tipo da conta seja igual "Adotar" ir para a tela de cadastrar historia
                            if(radioButton.getText().toString().equals("Adopt")){
                                Intent intent = new Intent(CadastrarTipocontaActivity.this, CadastrarAdotarhistoriaActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                                //Caso o tipo da conta seja igual "Doar" ir para a tela cadastrar pet
                            } else {
                                Intent intent = new Intent(CadastrarTipocontaActivity.this, CadastroPetActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }

                        }
                    }.start();


                } else {
                    //Se os campos estiverem vazio envie uma alerta
                    mtoastfields = Toast.makeText(CadastrarTipocontaActivity.this, "Escolha uma opção", Toast.LENGTH_SHORT);

                    //Tempo de duração da alerta
                    new CountDownTimer(1000, 200) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mtoastfields.show();
                        }
                        @Override
                        public void onFinish() {
                            mtoastfields.cancel();
                        }
                    }.start();
                }
            }
        });



    }

    public void GotoFaq(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/uni9.edu.br/adote-um-pet/faq"));
        startActivity(intent);
    }
}