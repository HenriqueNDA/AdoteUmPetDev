package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import br.com.adoteumpet.Perfil.MyAdapter;


public class PerfilActivity extends AppCompatActivity {
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    //TextView
    private TextView mtvUsername;

    //ImageView
    private ImageView mivProfileImage;

    //Strings
    private String sUserId, sProfileImageUrl;
    String s1[], s2[];

    //RecyclerView
    RecyclerView recyclerView;
    int imagens[] = {R.drawable.iconeediteseuperfil, R.drawable.iconeminhaconfigs, R.drawable.iconeconta, R.drawable.iconeconvidaramigos, R.drawable.iconefaq, R.drawable.iconelogout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        sUserId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(sUserId);

        //Definindo TextView
        mtvUsername = (TextView) findViewById(R.id.tvUsername);

        //Definindo ImageView
        mivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        //Chamando a função de mostrar as informações do usuario
        setInfo();

        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        s1 = getResources().getStringArray(R.array.botoes_opcoes);
        s2 = getResources().getStringArray(R.array.botoes_opcoes_descricoes);
        MyAdapter myAdapter = new MyAdapter(this, s1, s2, imagens);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration linhaDividir = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        linhaDividir.setDrawable(ContextCompat.getDrawable(this, R.drawable.linha_recyclerviewperfil));
        recyclerView.addItemDecoration(linhaDividir);

    }
    private void setInfo(){
        //Conecntando com o Firebase
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    //String ImageData
                    String ImageData = "";
                    String sProfileusername = "Sem Nome";

                    //Pegar o nome no database
                    if(map.get("ProfileUsername") != null){
                        sProfileusername = map.get("ProfileUsername").toString();
                        mtvUsername.setText(sProfileusername);
                    }

                    //Pegar o valor que esta no firebase e salvar na string ImageData
                    if(snapshot.child("ProfileImageurl").getValue() != null){
                        ImageData = snapshot.child("ProfileImageurl").getValue().toString();
                    }
                    //Se ImageData estiver sem nada escrito
                    if(ImageData.equals("")){
                        //Salvar a string sProfileImageUrl com o url de um usuario sem foto no perfil
                        sProfileImageUrl = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/ProfilesImages%2Ficonefotoindisponivel.png?alt=media&token=6f9cb1e9-c062-4a9a-80ff-8b599a87f0d0";

                        //Caso contrario
                    } else {
                        ///Salvar a url de ImageData e salvar no sProfileImageUrl
                        sProfileImageUrl = ImageData.toString();
                    }
                    Glide.with(getApplication()).load(sProfileImageUrl).into(mivProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Ir para a tela Main
    public void irParaMain(View view) {
        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    //Ir para tela de petch
    public void irParaChat(View view) {
        Intent intent = new Intent(PerfilActivity.this, PetchActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}