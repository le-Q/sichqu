package no.hiof.sichqu.sichqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import no.hiof.sichqu.sichqu.Products.Products;

public class settingsActivity extends AppCompatActivity {

    private Switch myswitch;
    private Button removedata;
    private DeltPreferanse sharedpref;
    private Products produkt;
    private FirebaseDatabase db;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        produkt = (Products) getIntent().getSerializableExtra("produkter");

        sharedpref = new DeltPreferanse(this);
        //if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
        if (sharedpref.loadNightModeState()) {
            setTheme(R.style.darktheme);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        myswitch = findViewById(R.id.myswitch);
        removedata = findViewById(R.id.removedata);
            //if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            if (sharedpref.loadNightModeState()) {
                myswitch.setChecked(true);
            }
            myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        sharedpref.setNightModeState(true);
                        restartApp();
                    } else {
                        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        sharedpref.setNightModeState(false);
                        restartApp();
                    }
                }
            });

            removedata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(settingsActivity.this);
                    builder.setTitle("Are you sure about this?");
                    builder.setMessage("Deletion is permanent..");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fjernprodukter();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.show();
                }
            });

        }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), settingsActivity.class);
        startActivity(i);
        finish();
    }

    private void fjernprodukter() {
        //funker nok ikke ennå bare en test
        //databaseReference = FirebaseDatabase.getInstance().getReference("produkter").child(user.getUid());
        //databaseReference.removeValue();
        FirebaseDatabase.getInstance().getReference("produkter").child(FirebaseAuth.getInstance().getUid()).setValue(null);

        Toast.makeText(this, "Data deleted!", Toast.LENGTH_SHORT).show();
    }
}
