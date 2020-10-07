package br.com.adoteumpet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AtivarLocalizacaoActivity extends AppCompatActivity {


    TextView txtPermissao;
    Button btAtivar;

    String[] appPermissoes = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final int CODIGO_PERMISSOES_REQUERIDAS = 1;



    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativarlocalizacao);
        btAtivar = findViewById(R.id.btAtivar);


        if (verificarPermissoes()) {

        }



    }

    public boolean verificarPermissoes(){
        List<String> permissoesRequeridas = new ArrayList<>();
        for (String permissao: appPermissoes){
            if(ContextCompat.checkSelfPermission(this, permissao)
                    != PackageManager.PERMISSION_GRANTED){
                permissoesRequeridas.add(permissao);
            }
        }
        if(!permissoesRequeridas.isEmpty()){
            ActivityCompat.requestPermissions(this, permissoesRequeridas.toArray(new  String[permissoesRequeridas.size()]),
                    CODIGO_PERMISSOES_REQUERIDAS);
            return false;
        }
        return true;
    }



}
