package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.adoteumpet.Chat.ChatAdapter;
import br.com.adoteumpet.Chat.ChatObject;
import br.com.adoteumpet.Chat.VerPerfilActivity;

public class ChatActivity extends AppCompatActivity {
    //Recycler View
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    //Edit Text
    private EditText mEtMessage;

    //Image Button
    private ImageButton mIbttnSendMessage;

    //Image View
    private ImageView mIvProfileImageCover;

    //Text View
    private TextView mTvProfileNameCover;

    //Strings
    private String currentUserID, petchId, chatId, profileImageUrlOtherUser = "", sOtherUserId;

    //Database
    DatabaseReference mDatabaseUser, mDatabaseChat, mDatabaseOtherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Recuperar/pegar o petchId da intent petch
        petchId = getIntent().getExtras().getString("petchId");

        //Declarando o id do usuario atual
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Declarando o campo EditText de mensagem
        mEtMessage = (EditText) findViewById(R.id.etMessage);
        mEtMessage.setTextIsSelectable(true);

        //Declarando o campo TextView de nome da capa
        mTvProfileNameCover = (TextView) findViewById(R.id.tvProfileNameCover);

        //Declarando o botão de enviar mensagem
        mIbttnSendMessage = (ImageButton) findViewById(R.id.ibSendMessage);

        //Declanrando o Image View
        mIvProfileImageCover = (ImageView) findViewById(R.id.ivProfileCover);

        //Firebase
        mDatabaseOtherUser = FirebaseDatabase.getInstance().getReference().child("Users").child(petchId);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Connections").child("itsPetches").child(petchId);
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        //Chamando a função para recuperar/pegar as informações do usuario(Como Nome, e foto, id)
        getProfileInfo();

        //Chamando a função para recuperar/pegar o id do chat
        getChatId();

        //RecycleyView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);
        DividerItemDecoration linhaDividir = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        linhaDividir.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_recyclerviewchat));
        mRecyclerView.addItemDecoration(linhaDividir);

        //Ao foco do campo de digitar a mensagem mudar
        mEtMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //Mover o scroll para a ultima mensagem do recyclerview
                mRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        });

        //Ao clicar no campo de digitar a mensagem
        mEtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mover o scroll para a ultima mensagem do recyclerview
                mRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        });

        //Ao Clicar no botão de enviar a mensagem
        mIbttnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chamar a função de enviar a mensagem
                sendMessage();
            }
        });
    }

    private void getProfileInfo(){
        mDatabaseOtherUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String
                String sUserId = snapshot.getKey();
                String sOtherUserTypeaccount = "";
                String sOtherUsername = "Sem Nome";
                String sOtherUserImageData = "";

                //Pegar o Tipo da conta do Usuario
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                //Pegar o Tipo da conta no database
                if(map.get("ProfileTypeaccount") != null){
                    sOtherUserTypeaccount = map.get("ProfileTypeaccount").toString();
                }

                //Se o Tipo Da Conta for igual Adopt
                if(sOtherUserTypeaccount.equals("Donate")){
                    //Conectar com o banco de dados e pegar o link do ProfileImageurl e salvar no objeto ImageData
                    Object ProfilePetImageData = snapshot.child("Donate").child("PetProfileImageUrl").getValue();

                    //Conectar com o banco de dados e pegar o nome do Profile e salvar no objeto nomeData
                    Object ProfilePetNameData = snapshot.child("Donate").child("Petname").getValue();

                    //Se a Imagem for igual a null ou o campo estiver vazio
                    if (ProfilePetImageData == null || ProfilePetImageData.equals("")) {
                        //Definir ImageDefault como a imagem default
                        profileImageUrlOtherUser = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/ProfilesImages%2Ficonefotoindisponivel.png?alt=media&token=6f9cb1e9-c062-4a9a-80ff-8b599a87f0d0";

                        //Caso contrario
                    } else {
                        //Definir o valor de ImageDefault no ImageData
                        profileImageUrlOtherUser = ProfilePetImageData.toString();

                    }

                    //Se o campo for igual a null ou o campo estiver vazio
                    if (ProfilePetNameData != null) {
                        //Definir o valor de ImageDefault no ImageData
                        sOtherUsername = ProfilePetNameData.toString();

                    }

                    //Carregar o url da imagem no Glide dentro do mivCurrentProfileImage
                    Glide.with(getApplication()).load(profileImageUrlOtherUser).into(mIvProfileImageCover);

                } else if(sOtherUserTypeaccount.equals("Adopt")){
                    //Pegar o nome no database
                    if(map.get("ProfileUsername") != null){
                        sOtherUsername = map.get("ProfileUsername").toString();
                    }

                    //Pegar o valor que esta no firebase e salvar na string ImageData
                    if(snapshot.child("ProfileImageurl").getValue() != null){
                        sOtherUserImageData = snapshot.child("ProfileImageurl").getValue().toString();
                    }
                    //Se ImageData estiver sem nada escrito
                    if(sOtherUserImageData.equals("")){
                        //Salvar a string sProfileImageUrl com o url de um usuario sem foto no perfil
                        profileImageUrlOtherUser = "https://firebasestorage.googleapis.com/v0/b/adoteumpet-7afd7.appspot.com/o/ProfilesImages%2Ficonefotoindisponivel.png?alt=media&token=6f9cb1e9-c062-4a9a-80ff-8b599a87f0d0";

                        //Caso contrario
                    } else {
                        ///Salvar a url de ImageData e salvar no sProfileImageUrl
                        profileImageUrlOtherUser = sOtherUserImageData.toString();
                    }

                    //Carregar o url da imagem no Glide dentro do mivCurrentProfileImage
                    Glide.with(getApplication()).load(profileImageUrlOtherUser).into(mIvProfileImageCover);
                }
                //Carregar Nome do Usuario na capa da mensagem
                mTvProfileNameCover.setText(sOtherUsername);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage() {
        //Declarando uma string e pegando o texto do campo de digitar a mensagem
        String sendMessageText = mEtMessage.getText().toString();

        //Se a mensagem for diferente de vazio
        if(!sendMessageText.isEmpty()){
            //Salvar a mensagem no firebase
            DatabaseReference newMessageDb = mDatabaseChat.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            newMessageDb.setValue(newMessage);
        }

        //Mover o scroll para a ultima mensagem enviada
        mRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);

        //E deixa o campo de "Digitar Mensagem" nulo
        mEtMessage.setText(null);
    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Pegar o texto que esta no "ChatId" e colocar no chatId
                    chatId = snapshot.getValue().toString();

                    //Definindo que mDatabaseChat + o chatId
                    mDatabaseChat = mDatabaseChat.child(chatId);

                    //Chamar a função de recuperar/pegar a mensagem
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    //Strings
                    String message = null;
                    String createdByUser = null;

                    //Pegar o texto que esta no firebase
                    if(snapshot.child("text").getValue() != null){
                        //Pegar o valor que esta escrito em "text" e salvar na string "message"
                        message = snapshot.child("text").getValue().toString();
                    }
                    //Pegar o id do user que criou a mensagem
                    if(snapshot.child("createdByUser").getValue() != null){
                        //Pegar o valor que esta escrito em "createdByUser" e salvar na string "createdByUser"
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }
                    //Se message e createdByUser forem diferentes de nulo
                    if(message != null && createdByUser != null){
                        //Definir a boolean currentUserBoolean como false
                        Boolean currentUserBoolean = false;

                        //Se a mensagem for igual o id do usuario atual Definir a boolean como true
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        //Criando o "campo" da mensagem definindo a message, currentUserBoolean, profileImageUrlOtherUser
                        ChatObject newMessage = new ChatObject(message, currentUserBoolean, profileImageUrlOtherUser);

                        //Adicionando a nova mensagem
                        resultsChat.add(newMessage);

                        //Atualizar o RecyclerView com os novos dados
                        mChatAdapter.notifyDataSetChanged();

                        //Atualizar a positação do scroll para a ultima mensage menviada
                        mRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    //Voltar para a tela de petch
    public void irParaPetch(View view) {
        Intent intent = new Intent(ChatActivity.this, PetchActivity.class);
        startActivity(intent);
        finish();
        return;
    }


    //Ir para a tela de ver perfil do outro usuario
    public void irParaVerPerfil(View view) {
        Intent intent = new Intent(ChatActivity.this, VerPerfilActivity.class);
        intent.putExtra("sOtherUserId", petchId);
        startActivity(intent);
        finish();
        return;
    }

    private final ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
}