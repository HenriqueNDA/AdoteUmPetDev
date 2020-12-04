package br.com.adoteumpet.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import br.com.adoteumpet.ChatActivity;
import br.com.adoteumpet.MainActivity;
import br.com.adoteumpet.R;

public class MainActivityPetch extends AppCompatActivity {
    //TextView
    private TextView mtvShowOtherUsername;

    //ImageView
    private ImageView mivCurrentProfileImage, mivOtherProfileImage;

    //Button
    private Button mbttnContinue;
    private Button mbttnSendmessage;

    //ProgressBar
    private ProgressBar mProgressBarLoadmessage;
    private ProgressBar mProgressBarLoadcontinue;

    //Firebase
    private DatabaseReference mUserUidDatabase, mCurrentUserDatabase;

    //Strings
    private String sUserUid, sCurrentUserUid, sOtherUsername, sCurrentProfileImageUrl, sOherProfileImageuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_petch);

        //Declarando userUid e definindo dos extras no MainAcvity
        sUserUid = getIntent().getExtras().getString("userUid");

        //Declarando CurrentUserUid e definindo dos extras no MainAcvity
        sCurrentUserUid = getIntent().getExtras().getString("CurrentUserUid");

        //Declando o TextView Mostrar Nome Usuaruio
        mtvShowOtherUsername = (TextView) findViewById(R.id.tvShowOtherUsername);
        
        //Declarando ImageView do usuario usuario
        mivCurrentProfileImage = (ImageView) findViewById(R.id.ivCurrentProfileImage);

        //Declarando ImageView Segundo usuario
        mivOtherProfileImage = (ImageView) findViewById(R.id.ivOtherProfileImage);

        //Declarando o botão de continuar
        mbttnContinue = (Button) findViewById(R.id.bttnConfirm);

        //Declarando o ProgressBar do botão de continuar
        mProgressBarLoadcontinue = (ProgressBar) findViewById(R.id.ProgressBarLoad);
        mProgressBarLoadcontinue.setVisibility(View.INVISIBLE);

        //Declarando o botão de enviar mensagem
        mbttnSendmessage = (Button) findViewById(R.id.bttnSendmessage);

        //Declarando o ProgressBar do botão de enviar mensagem
        mProgressBarLoadmessage = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Chamando a função para Pegar informações do usuario Atual e defidindo seu Uid
        getInfoCurrentUser(sCurrentUserUid);

        //Chamando a função para Pegar informações do usuario secundario e defidindo seu Uid
        getInfoUserUid(sUserUid);

        //Ao clicar no botão Confirmar Petch voltar para o MainActivity
        mbttnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iniciar o contador
                new CountDownTimer(800, 200) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //Desativar botão e aparecer um Progress bar
                        mProgressBarLoadcontinue.setVisibility(View.VISIBLE);

                        //Inativar o botão de confirmar
                        mbttnContinue.setEnabled(false);
                    }
                    @Override

                    //No Final do contador
                    public void onFinish() {
                        //Ir para a intent MainActivity
                        Intent intent = new Intent(MainActivityPetch.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }.start();
            }
        });

        //Ao clicar no botão Mandar mensagem ir para a tela de chat
        mbttnSendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iniciar o contador
                new CountDownTimer(800, 200) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //ProgressarBar
                        mProgressBarLoadmessage.setVisibility(View.VISIBLE);
                        mProgressBarLoadcontinue.setVisibility(View.INVISIBLE);

                        //Inativar o botão de confirmar
                        mbttnSendmessage.setEnabled(false);
                    }
                    @Override

                    //No Final do contador
                    public void onFinish() {
                        //Ir para a intent ChatActivity
                        Intent intent = new Intent(MainActivityPetch.this, ChatActivity.class);
                        intent.putExtra("petchId", sUserUid);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }.start();
            }
        });

    }


    private void getInfoCurrentUser(String CurrentUserUid) {
        mCurrentUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserUid);
        mCurrentUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    //Strings
                    String sCurrentUserTypeaccount = "";
                    String ProfileImageData = "";

                    //Pegar o nome no database
                    if(map.get("ProfileTypeaccount") != null){
                        sCurrentUserTypeaccount = map.get("ProfileTypeaccount").toString();
                    }

                    //Caso ProfileTypeaccount seja igual adotar
                    if(sCurrentUserTypeaccount.equals("Adopt")){
                        //Pegar o valor que esta no firebase e salvar na string ImageData
                        if(snapshot.child("ProfileImageurl").getValue() != null){
                            ProfileImageData = snapshot.child("ProfileImageurl").getValue().toString();
                        }
                        //Se ImageData estiver sem nada escrito
                        if(ProfileImageData.equals("")){
                            //Salvar a string sProfileImageUrl com o url de um usuario sem foto no perfil
                            sCurrentProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/profileImages%2Ficonefotoindisponivel.png?alt=media&token=ee25d247-6f0a-4ab0-95ab-7c47025e82f6";

                            //Caso contrario
                        } else {
                            ///Salvar a url de ImageData e salvar no sProfileImageUrl
                            sCurrentProfileImageUrl = ProfileImageData.toString();
                        }

                        //Carregar o url da imagem no Glide dentro do mivCurrentProfileImage
                        Glide.with(getApplication()).load(sCurrentProfileImageUrl).into(mivCurrentProfileImage);

                    } else if(sCurrentUserTypeaccount.equals("Donate")){
                        //Conectar com o banco de dados e pegar o link do ProfileImageurl e salvar no objeto ImageData
                        Object ProfilePetImageData = snapshot.child("Donate").child("PetProfileImageUrl").getValue();

                        //Se a Imagem for igual a null ou o campo estiver vazio
                        if (ProfilePetImageData == null || ProfilePetImageData.equals("")) {
                            //Definir ImageDefault como a imagem default
                            sCurrentProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/profileImages%2Ficonefotoindisponivel.png?alt=media&token=ee25d247-6f0a-4ab0-95ab-7c47025e82f6";

                            //Caso contrario
                        } else {
                            //Definir o valor de ImageDefault no ImageData
                            sCurrentProfileImageUrl = ProfilePetImageData.toString();
                        }

                        //Carregar o url da imagem no Glide dentro do mivCurrentProfileImage
                        Glide.with(getApplication()).load(sCurrentProfileImageUrl).into(mivCurrentProfileImage);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getInfoUserUid(String userUid) {
        mUserUidDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid);
        mUserUidDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    //Strings
                    String sOtherUserTypeaccount = "";
                    String sOtherUserImageData = "";

                    //Pegar o nome no database
                    if(map.get("ProfileTypeaccount") != null){
                        sOtherUserTypeaccount = map.get("ProfileTypeaccount").toString();
                    }

                    //Caso ProfileTypeaccount seja igual adotar
                    if(sOtherUserTypeaccount.equals("Adopt")){
                        //Pegar o nome no database
                        if(map.get("ProfileUsername") != null){
                            sOtherUsername = map.get("ProfileUsername").toString();
                            mtvShowOtherUsername.setText(sOtherUsername + " se Interessou por você tambem!");
                        }

                        //Pegar o valor que esta no firebase e salvar na string ImageData
                        if(snapshot.child("ProfileImageurl").getValue() != null){
                            sOtherUserImageData = snapshot.child("ProfileImageurl").getValue().toString();
                        }
                        //Se ImageData estiver sem nada escrito
                        if(sOtherUserImageData.equals("")){
                            //Salvar a string sProfileImageUrl com o url de um usuario sem foto no perfil
                            sOherProfileImageuser = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/profileImages%2Ficonefotoindisponivel.png?alt=media&token=ee25d247-6f0a-4ab0-95ab-7c47025e82f6";

                            //Caso contrario
                        } else {
                            ///Salvar a url de ImageData e salvar no sProfileImageUrl
                            sOherProfileImageuser = sOtherUserImageData.toString();
                        }

                        //Carregar o url da imagem no Glide dentro do mivOtherProfileImage
                        Glide.with(getApplication()).load(sOherProfileImageuser).into(mivOtherProfileImage);

                    } else if (sOtherUserTypeaccount.equals("Donate")) {
                        //Conectar com o banco de dados e pegar o link do ProfileImageurl e salvar no objeto ImageData
                        Object ProfilePetImageData = snapshot.child("Donate").child("PetProfileImageUrl").getValue();

                        //Conectar com o banco de dados e pegar o nome do Profile e salvar no objeto nomeData
                        Object ProfilePetNameData = snapshot.child("Donate").child("Petname").getValue();

                        //Se a Imagem for igual a null ou o campo estiver vazio
                        if (ProfilePetImageData == null || ProfilePetImageData.equals("")) {
                            //Definir ImageDefault como a imagem default
                            sOherProfileImageuser = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/profileImages%2Ficonefotoindisponivel.png?alt=media&token=ee25d247-6f0a-4ab0-95ab-7c47025e82f6";

                            //Caso contrario
                        } else {
                            //Definir o valor de ImageDefault no ImageData
                            sOherProfileImageuser = ProfilePetImageData.toString();

                            //Carregar o url da imagem no Glide dentro do mivCurrentProfileImage
                            Glide.with(getApplication()).load(sOherProfileImageuser).into(mivOtherProfileImage);
                        }

                        //Se o campo for igual a null ou o campo estiver vazio
                        if (ProfilePetNameData != null) {
                            //Definir o valor de ImageDefault no ImageData
                            sOtherUsername = ProfilePetNameData.toString();
                            mtvShowOtherUsername.setText(sOtherUsername + " se Interessou por você tambem!");
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}