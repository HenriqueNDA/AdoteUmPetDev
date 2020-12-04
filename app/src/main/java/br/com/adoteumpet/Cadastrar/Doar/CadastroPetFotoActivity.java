package br.com.adoteumpet.Cadastrar.Doar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.adoteumpet.Cadastrar.CadastrarFotoActivity;
import br.com.adoteumpet.Cadastrar.CadastrarTipocontaActivity;
import br.com.adoteumpet.Cadastrar.VerificarEmailActivity;
import br.com.adoteumpet.R;

public class CadastroPetFotoActivity extends AppCompatActivity {
    //ImageView
    private ImageView mivProfileuserimage;

    //Buton
    private Button mbttnContinue;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Toasts
    private Toast mtoatscantback;

    //Uri
    private Uri mresultUri;

    //Stings
    private String sUserId, sprofileImageUrl;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;

    @Override
    //Precionar botão voltar
    public void onBackPressed()
    {
        //Configurar a mensagem do toast mtoatscantback
        mtoatscantback = Toast.makeText(CadastroPetFotoActivity.this, "Não é possivel voltar.", Toast.LENGTH_LONG);

        //Iniciador contador
        new CountDownTimer(1000,200){
            @Override
            //Inicio do tempo
            public void onTick(long millisUntilFinished) {
                //Iniciar visualização do tost
                mtoatscantback.show();
            }
            @Override
            //Fim do tempo
            public void onFinish() {
                //Cancelar a visualização do tost
                mtoatscantback.cancel();
            }
        }.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet_foto);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Salvar o uid do usuario no sUserId
        sUserId = mAuth.getCurrentUser().getUid();

        //Database
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(sUserId).child("Donate");

        //ImageView
        mivProfileuserimage = (ImageView) findViewById(R.id.ivProfileuserimage);

        //ProgressBar
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Botão Confirm
        mbttnContinue = (Button) findViewById(R.id.bttnConfirm);

        //Ao clicar na imagem
        final String[] sSelectimage = {"NoSelectImage"};
        mivProfileuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSelectimage[0] = "SelectedImage";
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        //Ao Clicar no botão de confirmar
        mbttnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se a String sSelectimage estiver igual NoSelectImage
                if(sSelectimage[0].equals("NoSelectImage")){
                    //Enviar um Toast informando para o usuario Adicionar uma foto ao perfil
                    Toast.makeText(CadastroPetFotoActivity.this, "Voce precisa adicionar uma foto ao seu perfil", Toast.LENGTH_LONG).show();

                    //Caso o usuario tenha adicionado uma foto ao perfil
                } else  if (sSelectimage[0].equals("SelectedImage")){
                    //Salvar a imamge no banco de dados
                    saveUserInformation();

                    //Contador
                    new CountDownTimer(1000, 200) {
                        @Override
                        //Inicio do contador
                        public void onTick(long millisUntilFinished) {
                            //Deixar o ProgressBar Visivel
                            mProgressBarLoad.setVisibility(View.VISIBLE);

                            //Desativar o botão de continuar
                            mbttnContinue.setEnabled(false);
                        }
                        @Override
                        public void onFinish() {
                            //Deixar o ProgressBar invisivel
                            mProgressBarLoad.setVisibility(View.INVISIBLE);

                            //Deixar o botão continuar ativo
                            mbttnContinue.setEnabled(true);

                            //Iniciar nova intent
                            Intent intent = new Intent(CadastroPetFotoActivity.this, VerificarEmailActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }.start();
                }

            }
        });
    }

    private void saveUserInformation() {
        if(mresultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PetImages").child(sUserId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), mresultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference filePath = FirebaseStorage.getInstance().getReference().child("PetImages").child(sUserId);
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {

                            Map newImage = new HashMap();
                            newImage.put("PetProfileImageUrl", downloadUrl.toString());
                            mCustomerDatabase.updateChildren(newImage);
                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            mresultUri = imageUri;
            mivProfileuserimage.setImageURI(mresultUri);
        }
    }
}