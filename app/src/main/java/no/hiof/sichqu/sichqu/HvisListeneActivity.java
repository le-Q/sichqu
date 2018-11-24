package no.hiof.sichqu.sichqu;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;



import no.hiof.sichqu.sichqu.Products.Products;

public class HvisListeneActivity extends AppCompatActivity {
    private DatabaseReference productDatabaseReference;

    private FirebaseAuth firebaseAuth;
    ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private DrawerLayout mDrawerlayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(!isOnline()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet access").setMessage("You don't have internet! This app doesn't work without internet right now. We're deeply sorry.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            firebaseAuth.signOut();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        DeltPreferanse sharedpref = new DeltPreferanse(this);
        if(sharedpref.loadNightModeState())
            setTheme(R.style.darktheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hvis_listene);


        //Lager navigation drawer
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerlayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_settings:
                                startActivity(new Intent(HvisListeneActivity.this, settingsActivity.class));
                                break;
                            case R.id.nav_logOut:
                                firebaseAuth.signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                break;
                            case R.id.nav_share:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                startActivity(Intent.createChooser(intent, "Share using"));

                                //Toast.makeText(HandlelisteActivity.this, "share", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Handlelister");

        listView = findViewById(R.id.AlleListene);
        EditText handleListeNavn = findViewById(R.id.listName);
        ImageButton newListBtn = findViewById(R.id.addNewFloatBtn);

        //prøve å endre email til innloggede bruker

        View headerView = navigationView.getHeaderView(0);
        TextView userEmail = headerView.findViewById(R.id.email);




        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
        if (user != null) {
            productDatabaseReference = databaseReference.getReference("produkter").child(user.getUid());
            String brukerEpost = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            userEmail.setText(brukerEpost);
        } else {
            productDatabaseReference = databaseReference.getReference();
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        goToList();

      /* //Notification
        notification();*/


    }

    /*private void notification() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 47);
        calendar.set(Calendar.SECOND, 10);

        Intent intent = new Intent(HvisListeneActivity.this, NotificationActivity.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(HvisListeneActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }*/

    // Hentet fra nettet, sender en ping til Google. Skal fungere på de fleste enheter.
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public void goToList() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(HvisListeneActivity.this, "Du trykket på: "+position+" "+arrayList.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HvisListeneActivity.this, HandlelisteActivity.class);

                String productList = arrayList.get(position);
                intent.putExtra(HandlelisteActivity.LIST_UID, productList);

                Log.e("productList: ", "*** - productlist+arrayList.get(position) = " + productList);
                //intent.putExtra()

                startActivity(intent);
            }
        });
    }

    public void leggTilListe(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HvisListeneActivity.this);
        final View viewDialog = getLayoutInflater().inflate(R.layout.addlist_dialog, null);
        Button leggTil = (Button) viewDialog.findViewById(R.id.addListBtn);

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        dialog.setTitle("Add shoppinglist");
        dialog.show();

        leggTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editName = (EditText) viewDialog.findViewById(R.id.listName);
                final String listenavn = editName.getText().toString();

                if (!TextUtils.isEmpty(listenavn)) {
                    productDatabaseReference.child(listenavn).setValue(0);
                    //Toast.makeText(HvisListeneActivity.this, "Listen " + listenavn + " ble laget", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                } else {
                    Toast.makeText(HvisListeneActivity.this, "Skriv inn navn på handleliste", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void dataRead() {
        productDatabaseReference.addValueEventListener(new ValueEventListener() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataRead();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Gir en handling til knappene inne i draweren
            case android.R.id.home:
                mDrawerlayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_settings:
                startActivity(new Intent(HvisListeneActivity.this, settingsActivity.class));
                break;
            case R.id.nav_logOut:
                firebaseAuth.signOut();
                break;
            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share using"));

                //Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


}
