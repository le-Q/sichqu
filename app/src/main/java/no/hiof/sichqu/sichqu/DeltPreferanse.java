package no.hiof.sichqu.sichqu;

import android.content.Context;
import android.content.SharedPreferences;

public class DeltPreferanse {
    SharedPreferences mySharedPref;
    public DeltPreferanse(Context context) {
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }
    // Lagre nightmode
    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }
    // Load NightMode state
    public Boolean loadNightModeState() {
        Boolean state = mySharedPref.getBoolean("NightMode", false);
        return state;
    }
}
