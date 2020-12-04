package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.adoteumpet.Petchs.PetchAdapter;
import br.com.adoteumpet.Petchs.PetchObject;

public class PetchActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mPetchAdapter;
    private RecyclerView.LayoutManager mPetchLayoutManager;

    //Firebase Database
    DatabaseReference petchDb;

    //Strings
    private String sCurrentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petch);

        //Declarando o id do usuario atual
        sCurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Firebase
        petchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(sCurrentUserID).child("Connections").child("itsPetches");

        //Funçar para pegar a identificação do petch
        getUserPetchId();

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewPetch);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mPetchLayoutManager = new LinearLayoutManager(PetchActivity.this);
        mRecyclerView.setLayoutManager(mPetchLayoutManager);
        mPetchAdapter = new PetchAdapter(getDataSetPetch(), PetchActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mPetchAdapter);

    }


    private void getUserPetchId() {
        petchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot petches : snapshot.getChildren()){
                        FetchPetchInformation(petches.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
    private void FetchPetchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //String
                    String sUserId = snapshot.getKey();
                    String sOtherUserTypeaccount = "";
                    String sOherProfileImageuser = "";
                    String sOtherUsername = "Sem Nome";
                    String sResponsiblePetOtherUser = "Sem Nome";
                    String sOtherUserImageData = "";

                    //Pegar o Tipo da conta do Usuario
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    //Pegar o Tipo da conta no database
                    if(map.get("ProfileTypeaccount") != null){
                        sOtherUserTypeaccount = map.get("ProfileTypeaccount").toString();
                    }

                    //Se o Tipo Da Conta for igual Adopt
                    if(sOtherUserTypeaccount.equals("Donate")){
                        if(map.get("ProfileUsername") != null){
                            sResponsiblePetOtherUser = map.get("ProfileUsername").toString();
                        }
                        //Conectar com o banco de dados e pegar o link do ProfileImageurl e salvar no objeto ImageData
                        Object ProfilePetImageData = snapshot.child("Donate").child("PetProfileImageUrl").getValue();

                        //Conectar com o banco de dados e pegar o nome do Profile e salvar no objeto nomeData
                        Object ProfilePetNameData = snapshot.child("Donate").child("Petname").getValue();

                        //Se a Imagem for igual a null ou o campo estiver vazio
                        if (ProfilePetImageData == null || ProfilePetImageData.equals("")) {
                            //Definir ImageDefault como a imagem default
                            sOherProfileImageuser = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/ProfilesImages%2Ficonefotoindisponivel.png?alt=media&token=6f9cb1e9-c062-4a9a-80ff-8b599a87f0d0";

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

                    } else if(sOtherUserTypeaccount.equals("Adopt")){
                        //Pegar o nome no database
                        if(map.get("ProfileUsername") != null){
                            sOtherUsername = map.get("ProfileUsername").toString();
                        }

                        //Pegar o valor que esta no firebase e salvar na string ImageData
                        if(snapshot.child("ProfileImageurl").getValue() != null){
                            sOtherUserImageData = snapshot.child("ProfileImageurl").getValue().toString();
                        }
                        //Se ImageData estiver sem nada escrito
                        if(sOtherUserImageData.equals("")){
                            //Salvar a string sProfileImageUrl com o url de um usuario sem foto no perfil
                            sOherProfileImageuser = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/ProfilesImages%2Ficonefotoindisponivel.png?alt=media&token=6f9cb1e9-c062-4a9a-80ff-8b599a87f0d0";

                            //Caso contrario
                        } else {
                            ///Salvar a url de ImageData e salvar no sProfileImageUrl
                            sOherProfileImageuser = sOtherUserImageData.toString();
                        }

                    }

                    //Definindo o obj com o userid, nome do outro usuario, nome do pet, e imagem do outro usuario
                    PetchObject obj = new PetchObject(sUserId, sOtherUsername, sResponsiblePetOtherUser,sOherProfileImageuser);

                    //Se o nome for Igual a "Sem Nome"
                    if(sOtherUsername.equals("Sem Nome")){
                        //Remover obj
                        resultsPetchs.remove(obj);

                        //Caso Contrario
                    } else {
                        //Adioncar obj
                        resultsPetchs.add(obj);

                        //Atualizar o RecyclerView com os novos dados
                        mPetchAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private final ArrayList<PetchObject> resultsPetchs = new ArrayList<PetchObject>();
    private List<PetchObject> getDataSetPetch() {
        return resultsPetchs;
    }

    //Voltar para a tela de perfil
    public void irParaPerfil(View view) {
        Intent intent = new Intent(PetchActivity.this, PerfilActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    //Voltar para a tela de Main
    public void irParaMain(View view) {
        Intent intent = new Intent(PetchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}