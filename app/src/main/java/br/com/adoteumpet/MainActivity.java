package br.com.adoteumpet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUId;

    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        checkUserSex();
        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );




        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userOpossitOpt).child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "Esquerda!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userOpossitOpt).child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectMatch(userId);
                //Toast.makeText(MainActivity.this, "Direita!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }


            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void isConnectMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(userAdotar).child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(MainActivity.this, "Um novo petch <3", Toast.LENGTH_LONG).show();
                    usersDb.child(userOpossitOpt).child(snapshot.getKey()).child("connections").child("matches").child(currentUId).setValue(true);
                    usersDb.child(userAdotar).child(currentUId).child("connections").child("matches").child(snapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String userAdotar;
    private String userOpossitOpt;
    public void checkUserSex(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference AdotarDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Adotar");
        AdotarDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    userAdotar = "Adotar";
                    userOpossitOpt = "Doar";
                    getOppositOptUsers();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot,String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference DoarDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Doar");
        DoarDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    userAdotar = "Doar";
                    userOpossitOpt = "Adotar";
                    getOppositOptUsers();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot,String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void getOppositOptUsers(){
        DatabaseReference OppositOptDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userOpossitOpt);
        OppositOptDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
               if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUId)){
                   cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("nome").getValue().toString());
                   rowItems.add(item);
                   arrayAdapter.notifyDataSetChanged();
               }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot,String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void irParaPerfil(View view) {
        Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}