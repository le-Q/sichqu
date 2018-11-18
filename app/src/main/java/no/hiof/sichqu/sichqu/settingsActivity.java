package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class settingsActivity extends AppCompatActivity {

    private Switch myswitch;
    private Button removedata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myswitch=findViewById(R.id.myswitch);
        removedata=findViewById(R.id.removedata);

        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                else{
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                   restartApp();
                }
            }
        });

        removedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fjernprodukter(); //(produkter)
            }
        });


    }

    private void fjernprodukter() { //(String produkter)
        //funker nok ikke ennå bare en test
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("handleliste").child("id");
        databaseReference.removeValue();

        Toast.makeText(this, "Data deleted!", Toast.LENGTH_SHORT).show();
    }

    public void restartApp (){
        Intent i = new Intent(getApplicationContext(), settingsActivity.class);
        startActivity(i);
        finish();
    }

}
