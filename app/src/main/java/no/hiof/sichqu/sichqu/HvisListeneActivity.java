package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HvisListeneActivity extends AppCompatActivity {
    private EditText handleListeNavn;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hvis_listene);

        getSupportActionBar().setTitle("Handlelister");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.AlleListene);
        handleListeNavn = findViewById(R.id.txt_listeNavn);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("produkter");
        firebaseDatabase = FirebaseDatabase.getInstance();


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HvisListeneActivity.this, "Du trykket på: "+position+" "+arrayList.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HvisListeneActivity.this, HandlelisteActivity.class);

                startActivity(intent);
            }
        });

        handleListeNavn.requestFocus();
    }

    public void leggTilListe(View view) {
        user = firebaseAuth.getCurrentUser();
        String listenavn = handleListeNavn.getText().toString();
        //Map<String, String> listActive = new HashMap<>();
        //listActive.put("Active", "true");
        databaseReference.child(user.getUid()).child(listenavn).setValue(0);
        dataRead();
    }

    public void dialogAddListe() {

    }


public void dataRead() {
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            arrayList.clear();
                for(DataSnapshot nameListShot : dataSnapshot.getChildren()){
                    arrayList.add(nameListShot.getKey());
                }
            }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };
    databaseReference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataRead();
    }
}
