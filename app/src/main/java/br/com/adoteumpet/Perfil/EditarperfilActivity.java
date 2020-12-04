package br.com.adoteumpet.Perfil;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.adoteumpet.MainActivity;
import br.com.adoteumpet.PerfilActivity;
import br.com.adoteumpet.PetchActivity;
import br.com.adoteumpet.R;

public class EditarperfilActivity extends AppCompatActivity {
    //TextView
    private TextView mtvProfileusername, mtvProfileuserbio, mtvInfoeditprofilepet;

    //Image View
    private ImageView mivProfileimage;

    //EditText
    private EditText metProfileUsername, metProfileUserbio;

    //Buttons
    private Button mbttnConfirm, mbttnEditpetprofile;

    //ProgressBar
    private ProgressBar mProgressBarLoad;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase, mUserDatabaseBio;

    //Strings
    private String sUserId, sProfileImageurl, sProfileusername, sProfileuserbio, sProfileusertypeaccount;

    //Toasts
    private Toast mtoastfields;

    //Uri
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editarperfil);

        //TextView
        mtvProfileusername = (TextView) findViewById(R.id.tvProfileusername);

        //TextView
        mtvProfileuserbio = (TextView) findViewById(R.id.tvProfileuserbio);

        //TextView
        mtvInfoeditprofilepet = (TextView) findViewById(R.id.tvInfoeditprofilepet);

        //Edittext Username
        metProfileUsername = (EditText) findViewById(R.id.etProfileUsername);

        //Edittext Bio
        metProfileUserbio = (EditText) findViewById(R.id.etProfileUserbio);

        //Button Confirm
        mbttnConfirm = (Button) findViewById(R.id.bttnConfirm);

        //ProgressBar Button Confirm
        mProgressBarLoad = (ProgressBar) findViewById(R.id.ProgressBarLoad);

        //Button Edit Profile Pet
        mbttnEditpetprofile = (Button) findViewById(R.id.bttnEditpetprofile);

        //ImageView
        mivProfileimage = (ImageView) findViewById(R.id.ivProfileImage);

        //Definindo o mAuth
        mAuth = FirebaseAuth.getInstance();

        //Pegar o Uid do usuario atual e salvar na string userId
        sUserId = mAuth.getCurrentUser().getUid();

        //Definindo o banco de dados
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(sUserId);

        //Definindo o banco de dados do adopt
        mUserDatabaseBio = FirebaseDatabase.getInstance().getReference().child("Users").child(sUserId).child("Adopt");

        //Pegar as informações do Usuario
        getUserInfo();

        //Ao clicar no botão confirmar
        mbttnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Strings
                String sProfileusername = metProfileUsername.getText().toString();
                String sProfileuserbio = metProfileUserbio.getText().toString();

                if(sProfileusertypeaccount.equals("Adopt")){
                    if(!sProfileuserbio.isEmpty() && !sProfileusername.isEmpty()){
                        //Salvar as informações
                        saveUserInformation(sProfileusername, sProfileuserbio);

                        new CountDownTimer(1000, 200) {
                            @Override
                            //No Inicio do contador
                            public void onTick(long millisUntilFinished) {
                                //Deixar o PrgressBar visivel
                                mProgressBarLoad.setVisibility(View.VISIBLE);

                                //Desativar o botão de confirmar
                                mbttnConfirm.setEnabled(false);
                            }
                            @Override
                            //Quando acabar o contador
                            public void onFinish() {
                                //Deixar o PrgressBar invisivel
                                mProgressBarLoad.setVisibility(View.INVISIBLE);

                                //Ativar o botão de confirmar
                                mbttnConfirm.setEnabled(true);

                                //Toasts
                                Toast.makeText(EditarperfilActivity.this, "Dados Alterados.", Toast.LENGTH_SHORT).show();

                                //Int
                                final Intent intent = new Intent(EditarperfilActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }.start();
                    } else {
                        //Toast para mensagem de preencher todos os campos
                        mtoastfields = Toast.makeText(EditarperfilActivity.this, "Não deixe os campos sem nada!", Toast.LENGTH_SHORT);

                        //Contador para mostrar o Toast
                        new CountDownTimer(1000, 200) {
                            @Override
                            //No Inicio do contador
                            public void onTick(long millisUntilFinished) {
                                //Mostrar o toast
                                mtoastfields.show();
                            }
                            @Override
                            //Quando acabar o contador
                            public void onFinish() {
                                //Cancelar a visualização do toast
                                mtoastfields.cancel();
                            }
                        }.start();
                    }
                } else {
                    if(!sProfileusername.isEmpty()){
                        //Salvar as informações
                        saveUserInformation(sProfileusername, sProfileuserbio);

                        new CountDownTimer(1000, 200) {
                            @Override
                            //No Inicio do contador
                            public void onTick(long millisUntilFinished) {
                                //Deixar o PrgressBar visivel
                                mProgressBarLoad.setVisibility(View.VISIBLE);

                                //Desativar o botão de confirmar
                                mbttnConfirm.setEnabled(false);
                            }
                            @Override
                            //Quando acabar o contador
                            public void onFinish() {
                                //Deixar o PrgressBar invisivel
                                mProgressBarLoad.setVisibility(View.INVISIBLE);

                                //Ativar o botão de confirmar
                                mbttnConfirm.setEnabled(true);

                                //Toasts
                                Toast.makeText(EditarperfilActivity.this, "Dados Alterados.", Toast.LENGTH_SHORT).show();

                                //Int
                                final Intent intent = new Intent(EditarperfilActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }.start();
                    } else {
                        //Toast para mensagem de preencher todos os campos
                        mtoastfields = Toast.makeText(EditarperfilActivity.this, "Não deixe os campos sem nada!", Toast.LENGTH_SHORT);

                        //Contador para mostrar o Toast
                        new CountDownTimer(1000, 200) {
                            @Override
                            //No Inicio do contador
                            public void onTick(long millisUntilFinished) {
                                //Mostrar o toast
                                mtoastfields.show();
                            }
                            @Override
                            //Quando acabar o contador
                            public void onFinish() {
                                //Cancelar a visualização do toast
                                mtoastfields.cancel();
                            }
                        }.start();
                    }
                }
            }
        });

        //Ao clicar na imagem
        mivProfileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Iniciar a intent de galeria
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

          mbttnEditpetprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarperfilActivity.this, EditarPerfilPetActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0) {
                    //Conectar com o banco de dados e pegar a bio do Profile e salvar no objeto nomeData
                    Object ProfileBioData = snapshot.child("Adopt").child("AdoptBio").getValue();

                    //Map
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    //Pegar o nome no database
                    if(map.get("ProfileUsername") != null){
                        sProfileusername = map.get("ProfileUsername").toString();
                        metProfileUsername.setText(sProfileusername);
                        mtvProfileusername.setText(sProfileusername);
                    }

                    //Se o campo for igual a null ou o campo estiver vazio
                    if (ProfileBioData != null) {
                        //Definir o valor de ProfileBioData no sProfileuserbio
                        sProfileuserbio = ProfileBioData.toString();

                        //Definir a bio no et metProfileUserbio
                        metProfileUserbio.setText(sProfileuserbio);

                        //Caso conrario
                    } else {
                        //Deixar o campo de bio invisivel
                        metProfileUserbio.setVisibility(View.INVISIBLE);

                        //Deixar o tv invisivel
                        mtvProfileuserbio.setVisibility(View.INVISIBLE);
                    }
                    //Pegar o Tipo de conta do usuario no firebase
                    if(map.get("ProfileTypeaccount") != null){
                        sProfileusertypeaccount = map.get("ProfileTypeaccount").toString();

                        //Se o tipo de conta for igual a DONATE
                        if(sProfileusertypeaccount.equals("Adopt")){
                            //Deixar o text de informação invisivel
                            mtvInfoeditprofilepet.setVisibility(View.INVISIBLE);

                            //Deixar o botão de clique aqui invisivel
                            mbttnEditpetprofile.setVisibility(View.INVISIBLE);

                            //Caso Contrairo
                        } else {
                            //Deixar o texto de informação visivel
                            mtvInfoeditprofilepet.setVisibility(View.VISIBLE);

                            //Deixar o botão de clique aqui visivel
                            mbttnEditpetprofile.setVisibility(View.VISIBLE);

                        }
                    }
                    //Pegar o valor que esta no firebase e salvar na string ImageData
                    if(map.get("ProfileImageurl") != null){
                        sProfileImageurl = map.get("ProfileImageurl").toString();
                        Glide.with(getApplication()).load(sProfileImageurl).into(mivProfileimage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInformation(String sProfileusername, String sProfileuserbio) {
        //Definindo o map
        Map userInfo = new HashMap();

        //Colocando os dados no map
        userInfo.put("ProfileUsername", sProfileusername);

        //Dando upadate nos dados
        mUserDatabase.updateChildren(userInfo);

        if(!sProfileuserbio.isEmpty()){
            //Definindo o map da bio
            Map userInfoBio = new HashMap();

            //Colocando os dados no map
            userInfoBio.put("AdoptBio", sProfileuserbio);

            //Dando upadate nos dados
            mUserDatabaseBio.updateChildren(userInfoBio);
        }

        //Dados da Imagem
        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("ProfilesImages").child(sUserId);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
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
                    StorageReference filePath = FirebaseStorage.getInstance().getReference().child("ProfilesImages").child(sUserId);
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            Map newImage = new HashMap();
                            newImage.put("ProfileImageurl", downloadUrl.toString());
                            mUserDatabase.updateChildren(newImage);

                        }
                    });

                }
            });
        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mivProfileimage.setImageURI(resultUri);
        }
    }

    public void irParaMain(View view) {
        Intent intent = new Intent(EditarperfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void irParaPerfil(View view) {
        Intent intent = new Intent(EditarperfilActivity.this, PerfilActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void irParaChat(View view) {
        Intent intent = new Intent(EditarperfilActivity.this, PetchActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}