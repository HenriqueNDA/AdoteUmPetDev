package br.com.adoteumpet.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import br.com.adoteumpet.MainActivity;
import br.com.adoteumpet.PerfilActivity;
import br.com.adoteumpet.PetchActivity;
import br.com.adoteumpet.R;

public class MainAcivityInfoPerfil extends AppCompatActivity {
   //Buttons
    private ImageButton mbttnNops, mbttnPetch;

    //TextView
    private TextView mtvProfileUsername, mtvPetInfoage, mtvPetInfosize, mtvProfilehistory;

    //ImageView
    private ImageView mivProfileimage;

    //Strings
    String sOtherUserId, profileImageUrlOtherUser, currentUId;

    //Firebase
    DatabaseReference usersDb, OthserUserDb;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info_perfil);

        //TextView
        mtvProfileUsername = (TextView) findViewById(R.id.tvProfileUsername);
        mtvPetInfoage = (TextView) findViewById(R.id.tvPetInfoage);
        mtvPetInfosize = (TextView) findViewById(R.id.tvPetInfosize);
        mtvProfilehistory = (TextView) findViewById(R.id.tvProfilehistory);

        //ImageView
        mivProfileimage = (ImageView) findViewById(R.id.ivbackground);

        //Puxar o dados da Intent de Chat
        sOtherUserId = getIntent().getExtras().getString("sOtherUserId");

        //Declarar button petch
        mbttnPetch = (ImageButton) findViewById(R.id.bttnPetch);

        //Declarar button petch
        mbttnNops = (ImageButton) findViewById(R.id.bttnNops);

        //Firebase
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        //Chamar a função para pegar os dados do usuario passando o id do usuario
        GetInfoProfile(sOtherUserId);

        //Se clicar no botão petch
        mbttnPetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chamar a função petch
                setPetch(currentUId, sOtherUserId);
            }
        });

        //Se clicar no botão petch
        mbttnNops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chamar a função petch
                setNotinterest(sOtherUserId);
            }
        });

    }

    private void setNotinterest(String sOtherUserId) {
        usersDb.child(sOtherUserId).child("Connections").child("Notinterest").child(currentUId).setValue(true);
        Intent intent = new Intent(MainAcivityInfoPerfil.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    private void setPetch(String currentUId, String sOtherUserId) {
        usersDb.child(sOtherUserId).child("Connections").child("possiblePetches").child(currentUId).setValue(true);
        isConnectPetch(sOtherUserId);
    }

    private void isConnectPetch(final String sOtherUserId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("Connections").child("possiblePetches").child(sOtherUserId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    PetchShow(sOtherUserId, currentUId);
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(snapshot.getKey()).child("Connections").child("itsPetches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("Connections").child("itsPetches").child(snapshot.getKey()).child("ChatId").setValue(key);
                } else {
                    Intent intent = new Intent(MainAcivityInfoPerfil.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void PetchShow(String userUid, String CurrentUserUid){
        Intent intent = new Intent(MainAcivityInfoPerfil.this, MainActivityPetch.class);
        intent.putExtra("userUid", userUid);
        intent.putExtra("CurrentUserUid", CurrentUserUid);
        startActivity(intent);
        finish();
        return;
    }

    private void GetInfoProfile(String sOtherUserId) {
        OthserUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(sOtherUserId);
        OthserUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    //String
                    String sOtherUserTypeaccount = "";
                    String sOherProfileImageuser = "";
                    String sOtherUsername = "Sem Nome";
                    String sOtherUserBio = "";
                    String sOtherUserPetSize = "";
                    String sOtherUserPetAge = "";
                    String sOtherUserImageData = "";

                    //Pegar o Tipo da conta do Usuario
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    //Pegar o Tipo da conta no database
                    if(map.get("ProfileTypeaccount") != null){
                        sOtherUserTypeaccount = map.get("ProfileTypeaccount").toString();
                    }

                    //Se o Tipo Da Conta for igual Adopt
                    if(sOtherUserTypeaccount.equals("Donate")){
                        //Conectar com o banco de dados e pegar o link do ProfileImageurl e salvar no objeto ImageData
                        Object ProfilePetImageData = snapshot.child("Donate").child("PetProfileImageUrl").getValue();

                        //Conectar com o banco de dados e pegar o nome do Profile e salvar no objeto nomeData
                        Object ProfilePetNameData = snapshot.child("Donate").child("Petname").getValue();

                        //Conectar com o banco de dados e pegar a bio do Profile e salvar no objeto nomeData
                        Object ProfilePetBioData = snapshot.child("Donate").child("PetBio").getValue();

                        //Conectar com o banco de dados e pegar o tamanho do pet no Profile e salvar no objeto nomeData
                        Object ProfilePetSizeData = snapshot.child("Donate").child("PetSize").getValue();

                        //Conectar com o banco de dados e pegar a idade do pet no Profile e salvar no objeto nomeData
                        Object ProfilePetAgeData = snapshot.child("Donate").child("PetAge").getValue();

                        //Se a Imagem for igual a null ou o campo estiver vazio
                        if (ProfilePetImageData == null || ProfilePetImageData.equals("")) {
                            //Definir ImageDefault como a imagem default
                            sOherProfileImageuser = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/profileImages%2Ficonefotoindisponivel.png?alt=media&token=ee25d247-6f0a-4ab0-95ab-7c47025e82f6";

                            //Caso contrario
                        } else {
                            //Definir o valor de ImageDefault no ImageData
                            sOherProfileImageuser = ProfilePetImageData.toString();

                        }

                        //Se o campo for igual a null ou o campo estiver vazio
                        if (ProfilePetNameData != null) {
                            //Definir o valor de ImageDefault no ImageData
                            sOtherUsername = ProfilePetNameData.toString();
                        }

                        //Se o campo for igual a null ou o campo estiver vazio
                        if (ProfilePetBioData != null) {
                            //Definir o valor de ImageDefault no ImageData
                            sOtherUserBio = ProfilePetBioData.toString();
                        }

                        //Se o campo for igual a null ou o campo estiver vazio
                        if (ProfilePetSizeData != null) {
                            //Definir o valor de ImageDefault no ImageData
                            sOtherUserPetSize = ProfilePetSizeData.toString();
                        }

                        //Se o campo for igual a null ou o campo estiver vazio
                        if (sOtherUserPetAge != null) {
                            //Definir o valor de ImageDefault no ImageData
                            sOtherUserPetAge = ProfilePetAgeData.toString();
                        }

                    } else if(sOtherUserTypeaccount.equals("Adopt")){
                        //Conectar com o banco de dados e pegar a bio do Profile e salvar no objeto nomeData
                        Object ProfileBioData = snapshot.child("Adopt").child("AdoptBio").getValue();

                        //Pegar o nome no database
                        if(map.get("ProfileUsername") != null){
                            sOtherUsername = map.get("ProfileUsername").toString();
                        }
                        //Se o campo for igual a null ou o campo estiver vazio
                        if (ProfileBioData != null) {
                            //Definir o valor de ImageDefault no ImageData
                            sOtherUserBio = ProfileBioData.toString();
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

                    }
                    SetInfoProfile(sOtherUsername, sOtherUserTypeaccount, sOtherUserBio, sOtherUserPetAge, sOtherUserPetSize, sOherProfileImageuser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SetInfoProfile(String sOtherUsername, String sOtherUserTypeaccount, String sOtherUserBio, String sOtherUserPetAge, String sOtherUserPetSize, String sOherProfileImageuser) {
        //Se a conta do usuario for do tipo Doar
        if (sOtherUserTypeaccount.equals("Donate")){
            //Definir a string username no mtvProfileUsername
            mtvProfileUsername.setText(sOtherUsername);

            //Definir a string userhistory no mtvProfilehistory
            mtvProfilehistory.setText(sOtherUserBio);

            //Definir a string userPetage no mtvPetInfoage
            mtvPetInfoage.setText(sOtherUserPetAge);

            //Definir a string userPetsize no mtvPetInfosize
            mtvPetInfosize.setText(sOtherUserPetSize);

            //Carregar a url da string profileImageUrlOtherUser no imageview mivProfileimage
            Glide.with(getApplication()).load(sOherProfileImageuser).fitCenter().override(15000, 900).into(mivProfileimage);

            //Caso contrario
        } else {
            //Definir a string username no mtvProfileUsername
            mtvProfileUsername.setText(sOtherUsername);

            //Definir a string userhistory no mtvProfilehistory
            mtvProfilehistory.setText(sOtherUserBio);

            //Deixar o textview mtvPetInfoage invisivel
            mtvPetInfoage.setVisibility(View.INVISIBLE);

            //Deixar o textview mtvPetInfoage invisivel
            mtvPetInfosize.setVisibility(View.INVISIBLE);

            //Carregar a url da string profileImageUrlOtherUser no imageview mivProfileimage
            Glide.with(getApplication()).load(sOherProfileImageuser).fitCenter().override(15000, 900).into(mivProfileimage);
        }
    }


    public void irParaMain(View view) {
        Intent intent = new Intent(MainAcivityInfoPerfil.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void irParaPerfil(View view) {
        Intent intent = new Intent(MainAcivityInfoPerfil.this, PerfilActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void irParaChat(View view) {
        Intent intent = new Intent(MainAcivityInfoPerfil.this, PetchActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}


