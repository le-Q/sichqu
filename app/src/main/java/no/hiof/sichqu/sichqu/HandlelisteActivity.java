package no.hiof.sichqu.sichqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hiof.sichqu.sichqu.Products.Products;
import no.hiof.sichqu.sichqu.Products.Produkt;
import no.hiof.sichqu.sichqu.Products.UPC_data;

public class HandlelisteActivity extends AppCompatActivity {
    private static final String TAG = HandlelisteActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1;
    //implements NavigationView.OnNavigationItemSelectedListener

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    String cider = "Grevens cider skogsbær";
    String testHandleliste ="Handleliste";
    private ArrayList<String> arrayHandleliste = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProductAdapter productAdapter;
    private ChildEventListener childEventListener;
    private DatabaseReference databaseReference;
    private DatabaseReference databasePictureReference;
    private FirebaseDatabase firebaseDatabase;
    private List<Products> productList;
    private List<String> productListKeys;
    private ImageButton addNewButton, removeButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String id;
    private IntentIntegrator skuScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DeltPreferanse sharedpref = new DeltPreferanse(this);
        if(sharedpref.loadNightModeState())
            setTheme(R.style.darktheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.handleliste_activity);


        firebaseAuth = FirebaseAuth.getInstance();

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
                    case R.id.nav_db:
                        startActivity(new Intent(HandlelisteActivity.this, HvisListeneActivity.class));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(HandlelisteActivity.this, settingsActivity.class));
                        break;
                    case R.id.nav_logOut:
                        firebaseAuth.signOut();
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

        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Sjekker om en bruker er logget inn, hvis ingen brukere er logget inn blir man sendt til loginactivty
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                    startActivity(new Intent(HandlelisteActivity.this, LoginActivity.class));
                }
            }
        };


        //New item button
        addNewButton = (ImageButton) findViewById(R.id.addNewFloat);
        removeButton = (ImageButton) findViewById(R.id.removeProd);

        productList = new ArrayList<>();
        productListKeys = new ArrayList<>();

        // Query til database
        firebaseDatabase = FirebaseDatabase.getInstance();

        databasePictureReference = firebaseDatabase.getReference("bilder").child(firebaseAuth.getUid());

        productAdapter = new ProductAdapter(getApplicationContext(), productList);

        skuScan = new IntentIntegrator(this);
        recycleSetup();

        goSpinner();
        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(testHandleliste);
    }

    private void goSpinner() {
        // Spinner
        // Hente handlelister
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar22);
        firebaseDatabase.getReference().child("produkter").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayHandleliste.clear();
                for(DataSnapshot nameListShot : dataSnapshot.getChildren()){
                    arrayHandleliste.add(nameListShot.getKey());
                }

                ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_drop_down_item, arrayHandleliste);
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
                Spinner navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
                navigationSpinner.setAdapter(spinnerAdapter);
                toolbar.addView(navigationSpinner, 0);

                navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(HandlelisteActivity.this, "you selected: " + arrayHandleliste.get(position), Toast.LENGTH_SHORT).show();
                        testHandleliste = arrayHandleliste.get(position);
                        Log.e("GetA",testHandleliste+ " <- " + arrayHandleliste.get(position));

                        firebaseDatabase.getReference().child("produkter").child(firebaseAuth.getUid()).child(testHandleliste).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snap) {
                                Log.e("Spinner",snap.getKey() + " -> " + snap.getChildren());

                                ArrayList<Products> lister = new ArrayList<>();
                                for(DataSnapshot nameListShot : snap.getChildren()){
                                    Products product = nameListShot.getValue(Products.class);
                                    lister.add(product);
                                    //Log.e("Spinner3", nameListShot.getChildren().toString());

                                }
                                testHandleliste = snap.getKey();
                                productAdapter.setListData(lister);
                                databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(testHandleliste);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

<<<<<<< HEAD
=======
        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(testHandleliste);

        firebaseDatabase.getReference().child("produkter").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        arrayHandleliste.clear();
        for(DataSnapshot nameListShot : dataSnapshot.getChildren()){
            arrayHandleliste.add(nameListShot.getKey());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("View", "Hva er det?" + v);
                Toast.makeText(HandlelisteActivity.this, "Hva er det?" + v, Toast.LENGTH_SHORT).show();
            }
        });
>>>>>>> master
    }

    /*private void leggtilSlettDialog() {

        final EditText editName = (EditText) findViewById(R.id.productName);
        Button addBtn = (Button) findViewById(R.id.addName);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        id = databaseReference.push().getKey();

        AlertDialog.Builder builder = new AlertDialog.Builder(HandlelisteActivity.this);
        builder.setTitle("Legg til vare");
        AlertDialog b = builder.create();
        b.show();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                Products product = new Products(name);

                if (!TextUtils.isEmpty(name)) {
                    databaseReference.child(user.getUid()).child(id).setValue(product);
                    Toast.makeText(HandlelisteActivity.this, "Varen lagt til..", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HandlelisteActivity.this, "Skriv inn navn på produkt", Toast.LENGTH_LONG).show();
                }
            }
        });


    }*/

    private void databaseRead(){
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Products product = dataSnapshot.getValue(Products.class);
                String productKey = dataSnapshot.getKey();
                product.setId(productKey);

                if (!productList.contains(product)) {
                    productList.add(product);
                    productListKeys.add(productKey);
                    productAdapter.notifyItemChanged(productList.size()-1);
                }
                Log.d(TAG, "OnChildAdded fired");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Products product = dataSnapshot.getValue(Products.class);
                String productKey = dataSnapshot.getKey();
                product.setId(productKey);

                int position = productListKeys.indexOf(productKey);
                Log.d(TAG, "OnChildChanged fired"+" -> " + testHandleliste + " " + product);


                productList.set(position, product);
                productAdapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                for (Products products : productList) {
                    if (key.equals(products.getId())) {
                        productList.remove(products);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child(testHandleliste).addChildEventListener(childEventListener);
        Log.d(TAG, "OnChi fired : "+ testHandleliste);
    }


    private void recycleSetup() {
        mRecyclerView = findViewById(R.id.recyleViewListe);
        productAdapter = new ProductAdapter(this, productList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        databaseRead();

    }

   protected void onPause() {
        super.onPause();

        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
        productList.clear();
        productListKeys.clear();
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchmenu);
        final SearchView searchView = (SearchView)menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getProdukt(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 3){
                    Toast.makeText(HandlelisteActivity.this, newText, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Gir en handling til knappene inne i draweren
            case android.R.id.home:
                mDrawerlayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_db:
                startActivity(new Intent(HandlelisteActivity.this, HvisListeneActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(HandlelisteActivity.this, settingsActivity.class));
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

            // Handling av actionbar knapper
            case R.id.scan:
                skuScan.setOrientationLocked(false);
                skuScan.initiateScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    // Teste ved å legge til produkter da man trykker knappen
    public void hentAPI(View view) {
        getProdukt(cider, true);
        //getSKU(iste);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(this, "Signed in as " + user.getDisplayName(), Toast.LENGTH_SHORT);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        // Scan knapp trykket
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null ) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned " + result.getContents(), Toast.LENGTH_LONG).show();
                getProdukt(result.getContents(), false);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Knapp for floating action button
    public void scanItem(View v){
            skuScan.setOrientationLocked(false);
            skuScan.initiateScan();
        }
    // Knapp for floating action button
    public void addNewItem(View v) {
        if (v == addNewButton) {
            dialogAddnew();
        }
    }

    public void dialogAddnew() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HandlelisteActivity.this);
        View view = getLayoutInflater().inflate(R.layout.leggtilvare_dialog, null);
        EditText mEditText = (EditText) findViewById(R.id.productName);
        Button leggTil = (Button) findViewById(R.id.buttonAdd);

        leggTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        builder.setView(view);
        AlertDialog ad = builder.create();
        ad.show();
    }

    public void removeItem(View v) {
        if (v == removeButton) {

        }
    }

    private void getProdukt(String produktKode, final Boolean kolonial) {
        String URL;
        if (kolonial) {
            URL = "https://kolonial.no/api/v1/search/?q=" + produktKode;
        } else {
            URL = "https://api.upcdatabase.org/product/" + produktKode + "/D12DA15919D28F8FD6C146D1F14268EA";
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        if (kolonial) {
                            Produkt produkt = gson.fromJson(response.toString(), Produkt.class);
                            Products nyProdukt = produkt.getProducts()[0];
                            nyProdukt.setThumbnail(nyProdukt.getImages()[0].getThumbnail().getUrl());
                            addNewItem(nyProdukt);
                        } else {
                            UPC_data upc_produkt = gson.fromJson(response.toString(), UPC_data.class);
                            if(upc_produkt.getUpcnumber().equals("7038010001215"))
                                addNewItem(new Products("Iste Lime", "https://kolonial.no/media/uploads/public/169/385/968385-1896e-product_list.jpg", "iste"));
                            else
                                addNewItem(new Products(upc_produkt.getUpcnumber(), upc_produkt.getTitle()));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Check Error", "Error");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "application/json");
                if (kolonial) {
                    map.put("User-Agent", "QuangLe_Test");
                    map.put("X-Client-Token", "Pcd4FnKoER3Bfk2trn38DHV8umdCYkteOlp55CH9q0PoCDa2xc");
                } else {
                    map.put("X-Client-Token", "D12DA15919D28F8FD6C146D1F14268EA");
                }
                return map;
            }
        };
        queue.add(request);
    }

    private void addNewItem(Products produkt) {
        String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(produkt);

        Toast.makeText(this, "Varen lagt til..", Toast.LENGTH_LONG).show();
    }

    private void removeItem(Products produkt) {
        databaseReference.child(produkt.getId()).removeValue();
    }
}
