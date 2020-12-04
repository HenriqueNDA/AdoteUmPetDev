package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.adoteumpet.Adicional.PrimeirousoActivity;
import br.com.adoteumpet.Main.Cards.arrayAdapter;
import br.com.adoteumpet.Main.Cards.cards;
import br.com.adoteumpet.Main.MainAcivityInfoPerfil;
import br.com.adoteumpet.Main.MainActivityPetch;

public class MainActivity extends AppCompatActivity {
    //Cards
    private cards cards_data[];

    //Array Adapter
    private br.com.adoteumpet.Main.Cards.arrayAdapter arrayAdapter;
    private int i;

    //Strings
    private String sCurrentUId, sUserAdopt, sUserOpossittype;

    //List
    ListView listView;
    List<cards> rowItems;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definindo o uid do usuario atual
        mAuth = FirebaseAuth.getInstance();
        sCurrentUId = mAuth.getCurrentUser().getUid();

        //Diretório  do Banco de dados
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        //Checar o tipo da conta do usuario
        checkUserAccountType();
        rowItems = new ArrayList<cards>();

        //Tela de intro
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean fistUse = prefs.getBoolean("firstUse", true);
        if (fistUse){
            Introapp();
        }

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("Connections").child("Notinterest").child(sCurrentUId).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("Connections").child("possiblePetches").child(sCurrentUId).setValue(true);
                isConnectPetch(userId);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) { }

            @Override
            public void onScroll(float scrollProgressPercent) { }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                cards obj = (cards) dataObject;
                String sOtherUserId = obj.getUserId();
                Intent intent = new Intent(MainActivity.this, MainAcivityInfoPerfil.class);
                intent.putExtra("sOtherUserId", sOtherUserId);
                startActivity(intent);
            }
        });
    }

    private void isConnectPetch(final String userId) {
        //Definindo o banco de dados do Usuario Atual
        DatabaseReference currentUserConnectionsDb = usersDb.child(sCurrentUId).child("Connections").child("possiblePetches").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Se o dado existers
                if(snapshot.exists()){
                    //Chamar a função de PetchShow passando os dados do userId e do currentUId
                    PetchShow(userId, sCurrentUId);

                    //Salvando a chave para chat
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    //Salvar o UserId do card no banco de dados do app
                    usersDb.child(snapshot.getKey()).child("Connections").child("itsPetches").child(sCurrentUId).child("ChatId").setValue(key);

                    //Salvar o UserId do card no banco de dados do app
                    usersDb.child(sCurrentUId).child("Connections").child("itsPetches").child(snapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void PetchShow(String userUid, String CurrentUserUid){
        //Definindo a intent
        Intent intent = new Intent(MainActivity.this, MainActivityPetch.class);

        //Colocando o userUid como extra na intent
        intent.putExtra("userUid", userUid);

        //Colocando o CurrentUserUid como extra na intent
        intent.putExtra("CurrentUserUid", CurrentUserUid);

        //Iniciando a intent
        startActivity(intent);

        //Finalizando e dando return
        finish();
        return;
    }

    public void checkUserAccountType(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("ProfileTypeaccount").getValue() !=null){
                        sUserAdopt = snapshot.child("ProfileTypeaccount").getValue().toString();
                        switch (sUserAdopt){
                            case "Adopt":
                                sUserOpossittype = "Donate";
                                break;
                            case "Donate":
                                sUserOpossittype = "Adopt";
                                break;
                        }
                        getOppositOptUsers();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getOppositOptUsers(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("ProfileTypeaccount").getValue() != null) {
                    if(dataSnapshot.child("ProfileTypeaccount").getValue().toString().equals(sUserOpossittype)){
                        //Pegar o valor do banco de dados ProfileTypeaccount e salvar no objeto TypeAccountData
                        Object TypeAccountData = dataSnapshot.child("ProfileTypeaccount").getValue();

                        //Passar o Obj TypeAccountData para a string ProfileTypeaccount
                        String ProfileTypeaccount = TypeAccountData.toString();

                        //Se o ProfileTypeaccount estiver igual a "Adopt"
                        if(ProfileTypeaccount.equals("Adopt")){
                            if(dataSnapshot.exists() && !dataSnapshot.child("Connections").child("Notinterest").hasChild(sCurrentUId) && !dataSnapshot.child("Connections").child("possiblePetches").hasChild(sCurrentUId)){
                                //Conectar com o banco de dados e pegar o link do ProfileImageurl e salvar no objeto ImageData
                                Object ImageData = dataSnapshot.child("ProfileImageurl").getValue();

                                //Conectar com o banco de dados e pegar o nome do Profile e salvar no objeto nomeData
                                Object nomeData = dataSnapshot.child("ProfileUsername").getValue();

                                //Strings
                                String ImageDefault = "default";
                                String nameDefault = "Sem Nome";

                                //Se a Imagem for igual a null ou o campo estiver vazio
                                if (ImageData == null || ImageData.equals("")) {
                                    //Definir ImageDefault como a imagem default
                                    ImageDefault = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/ProfilesImages%2Ficonefotoindisponivel.png?alt=media&token=6f9cb1e9-c062-4a9a-80ff-8b599a87f0d0";

                                    //Caso contrario
                                } else {
                                    //Definir o valor de ImageDefault no ImageData
                                    ImageDefault = ImageData.toString();
                                }

                                //Se o campo for igual a null ou o campo estiver vazio
                                if (nomeData != null) {
                                    //Definir o valor de ImageDefault no ImageData
                                    nameDefault = nomeData.toString();
                                }

                                //Declarando o item do cards com o nome e imagem
                                cards item = new cards(dataSnapshot.getKey(), nameDefault, ImageDefault);

                                //Caso PetnameDefault estive igual "Sem Nome"
                                if (nameDefault.equals("Sem Nome")) {
                                    //Remover Cards
                                    rowItems.remove(item);

                                    //Caso contrario
                                } else {
                                    //Adicionar Card
                                    rowItems.add(item);

                                    //Notificar o Arrayadapter
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                            //Se o ProfileTypeaccount estiver igual a "Donate"
                        } else if (ProfileTypeaccount.equals("Donate")){
                            if(dataSnapshot.exists() && !dataSnapshot.child("Connections").child("Notinterest").hasChild(sCurrentUId) && !dataSnapshot.child("Connections").child("possiblePetches").hasChild(sCurrentUId)){
                                //Conectar com o banco de dados e pegar o link do ProfileImageurl e salvar no objeto ImageData
                                Object ImageData = dataSnapshot.child("Donate").child("PetProfileImageUrl").getValue();

                                //Conectar com o banco de dados e pegar o nome do Profile e salvar no objeto nomeData
                                Object nomeData = dataSnapshot.child("Donate").child("Petname").getValue();

                                //Strings
                                String ImageDefault = "default";
                                String PetnameDataDefault  = "Sem Nome";

                                //Se a Imagem for igual a null ou o campo estiver vazio
                                if (ImageData == null || ImageData.equals("")) {
                                    //Definir ImageDefault como a imagem default
                                    ImageDefault = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/ProfilesImages%2Ficonefotoindisponivel.png?alt=media&token=6f9cb1e9-c062-4a9a-80ff-8b599a87f0d0";

                                    //Caso contrario
                                } else {
                                    //Definir o valor de ImageDefault no ImageData
                                    ImageDefault = ImageData.toString();
                                }

                                //Se o campo for igual a null ou o campo estiver vazio
                                if (nomeData != null) {
                                    //Definir o valor de ImageDefault no ImageData
                                    PetnameDataDefault = nomeData.toString();
                                }

                                ////Declarando o item do cards com o nome e imagem
                                cards item = new cards(dataSnapshot.getKey(), PetnameDataDefault, ImageDefault);

//                              //Caso PetnameDefault estive igual "Sem Nome"
                                if (PetnameDataDefault.equals("Sem Nome")) {
                                    //
                                    rowItems.remove(item);
                                } else {
                                    //Adicionar Card
                                    rowItems.add(item);

                                    //Notificar o Arrayadapter
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot,String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void Introapp() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstUse", false);
        editor.apply();
        Intent intent = new Intent(MainActivity.this, PrimeirousoActivity.class);
        startActivity(intent);
    }

    public void irParaPerfil(View view) {
        Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
        startActivity(intent);
    }

    public void irParaChat(View view) {
        Intent intent = new Intent(MainActivity.this, PetchActivity.class);
        startActivity(intent);
    }
}