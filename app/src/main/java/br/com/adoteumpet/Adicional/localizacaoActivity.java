package br.com.adoteumpet.Adicional;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.adoteumpet.MainActivity;
import br.com.adoteumpet.R;

public class localizacaoActivity extends AppCompatActivity {
    //Permissão Storage
    private int STORAGE_PERMISSION_CODE = 1;

    //Button
    private Button mbttnActivateLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        //Declarando o botão de ativar a localização
        mbttnActivateLocation = (Button) findViewById(R.id.bttnActivateLocation);

        //Verficicar se o usuario deu permissão para utilizar a localização do celular
        if(ContextCompat.checkSelfPermission(localizacaoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Se a permissão estiver concedida chamar a função irparaTela
            gotoMainActivity();

            //Caso Contrario
        } else{

            //Ativar o botão para "Ativar a localização"
            mbttnActivateLocation.setEnabled(true);
        }

        //Ao Clicar no botão mbttnActivateLocation
        mbttnActivateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///Se a permissão for concedida
                if(ContextCompat.checkSelfPermission(localizacaoActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //Não fazer nada

                    //Caso contrario
                } else{
                    //Chamar a função para requesitar a permissão de localização
                    requsertLocationPermission();
                }
            }
        });

    }

    //Função para requisitar a permissão
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
                //Se a permissão estiver concedida chamar a função irparaTela
                gotoMainActivity();

                //Caso contrario
            } else {
                //Enviar um Toast de aviso
                Toast.makeText(localizacaoActivity.this, "é preciso permitir o aplicativo acessar sua localização", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Ir para tela MainActivity
    private void gotoMainActivity() {
        Intent intent = new Intent(localizacaoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}