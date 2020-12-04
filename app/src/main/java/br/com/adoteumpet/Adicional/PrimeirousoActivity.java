package br.com.adoteumpet.Adicional;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import br.com.adoteumpet.MainActivity;
import br.com.adoteumpet.R;


public class PrimeirousoActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Slide 1
        addSlide(AppIntroFragment.newInstance("Se interessou pelo perfil?", "Deslize a foto para a direita para demonstrar interrese.", R.drawable.iconeinteresse, ContextCompat.getColor(getApplicationContext(),R.color.backgroundprimeirouso)));

        //Slide 2
        addSlide(AppIntroFragment.newInstance("Não interessou pelo perfil?", "Deslize a foto para a esquerda para continuar visualizando outros perfis.", R.drawable.iconeseminterrese, ContextCompat.getColor(getApplicationContext(),R.color.backgroundprimeirouso)));

        //Slide 3
        addSlide(AppIntroFragment.newInstance("Deu Petch?", "Inicie uma conversa para continuar com o processo de adoção/doação", R.drawable.iconedeupetch, ContextCompat.getColor(getApplicationContext(),R.color.backgroundprimeirouso)));

        //Slide 4
        addSlide(AppIntroFragment.newInstance("Deseja ler as informações do perfil?", "Clique na foto para ver todas as informações", R.drawable.iconelerinfo, ContextCompat.getColor(getApplicationContext(),R.color.backgroundprimeirouso)));
    }

    //Quando Pressionar o botão Skip
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        //Ir para a tela MainActivity
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    //Quando Pressionar o botão Done
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        //Ir para a tela MainActivity
        Intent intent = new Intent(PrimeirousoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}