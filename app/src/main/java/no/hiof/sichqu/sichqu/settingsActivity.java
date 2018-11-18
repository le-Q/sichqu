package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.CompoundButton;
import android.widget.Switch;

public class settingsActivity extends AppCompatActivity {

    private Switch myswitch;
    private DeltPreferanse sharedpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedpref = new DeltPreferanse(this);
        //if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
        if(sharedpref.loadNightModeState()){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myswitch=findViewById(R.id.myswitch);
        //if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
        if(sharedpref.loadNightModeState()){
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedpref.setNightModeState(true);
                    restartApp();
                }
                else{
                   //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedpref.setNightModeState(false);
                   restartApp();
                }
            }
        });


    }

    public void restartApp (){
        Intent i = new Intent(getApplicationContext(), settingsActivity.class);
        startActivity(i);
        finish();
    }

}
