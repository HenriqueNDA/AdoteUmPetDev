package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class localizacaoActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    private Button vbttnLocalizaçao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        vbttnLocalizaçao = (Button) findViewById(R.id.bttnlocalizao);

        if(ContextCompat.checkSelfPermission(localizacaoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            irparaTela();
        } else{
            vbttnLocalizaçao.setEnabled(true);
        }


        vbttnLocalizaçao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(localizacaoActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                } else{
                    requsertLocationPermission();
                }
            }
        });

    }

    private void requsertLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(localizacaoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(localizacaoActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
        } else{
            ActivityCompat.requestPermissions(localizacaoActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(localizacaoActivity.this, "Acesso liberado", Toast.LENGTH_LONG).show();
                irparaTela();
            } else {
                Toast.makeText(localizacaoActivity.this, "é preciso permitir o aplicativo acessar sua localização", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void irparaTela() {
        Intent intent = new Intent(localizacaoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}