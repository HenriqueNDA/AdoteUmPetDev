package br.com.adoteumpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import br.com.adoteumpet.Cadastrar.Doar.CadastroPetinfoActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Definir a tela da splash creen como fullscren
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     
        //Iniciar um contador
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Apos o delay iniciar a Activity Home
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
                finish();
            }
        }, 1600);
    }
}