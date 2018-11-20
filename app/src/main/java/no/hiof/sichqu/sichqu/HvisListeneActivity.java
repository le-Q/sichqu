package no.hiof.sichqu.sichqu;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.Objects;

public class HvisListeneActivity extends AppCompatActivity {
    private EditText handleListeNavn;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    ListView listView;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private ImageButton newListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hvis_listene);

        getSupportActionBar().setTitle("Handlelister");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.AlleListene);
        handleListeNavn = findViewById(R.id.listName);
        newListBtn = findViewById(R.id.addNewFloatBtn);
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

//        handleListeNavn.requestFocus();
    }

    public void leggTilListe(View view) {
        if (view == newListBtn) {
        /*user = firebaseAuth.getCurrentUser();
        String listenavn = handleListeNavn.getText().toString();
        //Map<String, String> listActive = new HashMap<>();
        //listActive.put("Active", "true");
        databaseReference.child(user.getUid()).child(listenavn).setValue(0);
        dataRead();*/

        leggTilListeDialog();
        }
    }

    public void leggTilListeDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HvisListeneActivity.this);
        final View viewDialog = getLayoutInflater().inflate(R.layout.addlist_dialog, null);
        Button leggTil = (Button) viewDialog.findViewById(R.id.addListBtn);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();

        leggTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editName = (EditText) viewDialog.findViewById(R.id.listName);
                final String listenavn = editName.getText().toString();

                if (!TextUtils.isEmpty(listenavn)) {
                    databaseReference.child(firebaseAuth.getUid()).child(listenavn).setValue(0);
                    Toast.makeText(HvisListeneActivity.this, "Listen " + listenavn + " ble laget", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                } else {
                    Toast.makeText(HvisListeneActivity.this, "Skriv inn navn på handleliste", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void dataRead() {
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot nameListShot : dataSnapshot.getChildren()){
                    arrayList.add(nameListShot.getKey());
                    Log.e("Historikk", "->" + nameListShot.getKey());
                }
                Log.e("Historikk", "-> " + arrayList);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
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
    databaseReference.child(Objects.requireNonNull(user.getUid())).addListenerForSingleValueEvent(valueEventListener);
    */
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataRead();
    }
}
