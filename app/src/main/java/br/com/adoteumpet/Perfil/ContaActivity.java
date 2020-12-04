package br.com.adoteumpet.Perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.adoteumpet.CadastroPerfilActivity;
import br.com.adoteumpet.HomeActivity;
import br.com.adoteumpet.MainActivity;
import br.com.adoteumpet.PetchActivity;
import br.com.adoteumpet.R;

public class ContaActivity extends AppCompatActivity {
    //Button
    private Button mbttnDeletaccount;

    //Toast
    private Toast mtoasterror;

    //Strings
    private String sCurrentUId;

    //Firebase
    private FirebaseAuth FirebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_conta);

        //Definindo o mAuth
        FirebaseAuth = FirebaseAuth.getInstance();

        //Pegar o do usuario atual e salvar na string userId
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Pegar o Uid do usuario
        sCurrentUId = FirebaseAuth.getCurrentUser().getUid();


        //Botão
        mbttnDeletaccount = (Button) findViewById(R.id.bttnDeletaccount);

        //Ao Clicar no botão de deletar
        mbttnDeletaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ContaActivity.this, R.style.AlertDialogCustom);
                dialog.setTitle("Deseja excluir sua conta do AdoteUmPet ?");
                dialog.setMessage("Ao clicar no botão deletar sua conta do AdoteUmPet sera exlcuida permanentemente, e seus conteudos, dados e conexões não poderão ser recuperados.");
                dialog.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Chamar a função para deletar a conta do database
                        deleteProfiledb(sCurrentUId);

                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Aparecer Toast de conta deletada
                                    Toast.makeText(ContaActivity.this, "Conta deletada", Toast.LENGTH_LONG).show();

                                    //Ir para a main
                                    Intent intent = new Intent(ContaActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                } else {
                                    //Salvar o erro na hora de cadastro na string serrorsignup
                                    String serrorsingup = task.getException().getMessage();
                                    mtoasterror = Toast.makeText(ContaActivity.this, "Ocorreu um erro: " + serrorsingup, Toast.LENGTH_LONG);
                                    mtoasterror.show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }

    public void deleteProfiledb(String sCurrentUId){
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(sCurrentUId);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //Ir para a tela Main
    public void irParaMain(View view) {
        Intent intent = new Intent(ContaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    //Ir para tela de petch
    public void irParaChat(View view) {
        Intent intent = new Intent(ContaActivity.this, PetchActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}