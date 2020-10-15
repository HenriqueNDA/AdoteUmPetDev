package br.com.adoteumpet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PerfilActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView vtvUserNome;
    String s1[], s2[];
    int imagens[] = {R.drawable.iconeediteseuperfil, R.drawable.iconeminhaconfigs, R.drawable.iconeconvidaramigos, R.drawable.iconefaq, R.drawable.iconelogout};
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setInfo();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        s1 = getResources().getStringArray(R.array.botoes_opcoes);
        s2 = getResources().getStringArray(R.array.botoes_opcoes_descricoes);

        mAuth = FirebaseAuth.getInstance();

        MyAdapter myAdapter = new MyAdapter(this, s1, s2, imagens);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration linhaDividir = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        linhaDividir.setDrawable(ContextCompat.getDrawable(this, R.drawable.linha_recyclerviewperfil));
        recyclerView.addItemDecoration(linhaDividir);

    }
    private void setInfo(){
        vtvUserNome = (TextView) findViewById(R.id.tvUserNome);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        vtvUserNome.setText(new StringBuffer("").append(user.getDisplayName()));
    }

    public void irParaPetch(View view) {
        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}