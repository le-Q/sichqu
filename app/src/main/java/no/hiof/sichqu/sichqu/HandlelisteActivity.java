package no.hiof.sichqu.sichqu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
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
import android.view.LayoutInflater;
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

    String cider = "Grevens cider skogsbær";
    String testHandleliste = "Handleliste";
    private ArrayList<String> arrayHandleliste = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProductAdapter productAdapter;
    private ChildEventListener childEventListener;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private List<Products> productList;
    private List<String> productListKeys;
    private ImageButton addNewButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private IntentIntegrator skuScan;

    private ArrayList<String> lister = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DeltPreferanse sharedpref = new DeltPreferanse(this);
        if(sharedpref.loadNightModeState())
            setTheme(R.style.darktheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.handleliste_activity);

        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("HuskMat!");

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

        productList = DataHolder.getInstance().currentProducts;
        productListKeys = new ArrayList<>();

        // Query til database
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Nåværende bruker
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databasePictureReference = firebaseDatabase.getReference("bilder").child(firebaseAuth.getUid());
        //databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(testHandleliste);

        productAdapter = new ProductAdapter(getApplicationContext(), productList);

        skuScan = new IntentIntegrator(this);
        recycleSetup();


        goSpinner();
        // Hente handlelisten
        firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()) {
                    lister.add(snap.getKey());
                }

                if (!lister.isEmpty()) {
                    databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(lister.get(0));
                    Log.e("Handel", " Test -> " + lister.get(0) + testHandleliste + " " + databaseReference.toString());
                } else {
                    firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid());

                    startActivity(new Intent(HandlelisteActivity.this, HvisListeneActivity.class));
                }

                if (!lister.isEmpty()) {
                    databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(lister.get(0));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid());


    }

    private void goSpinner(){
        // Spinner
        // Hente handlelister
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar22);
        firebaseDatabase.getReference().child("produkter").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayHandleliste.clear();
                for(DataSnapshot nameListShot : dataSnapshot.getChildren()){
                    arrayHandleliste.add(nameListShot.getKey());

                        databaseReference.child(nameListShot.getKey()).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Products product = dataSnapshot.getValue(Products.class);
                                String productKey = dataSnapshot.getKey();
                                if (product != null) {
                                    product.setId(productKey);
                                }

                                if (!productList.contains(product)) {
                                    productList.add(product);
                                    productListKeys.add(productKey);
                                    productAdapter.notifyItemChanged(productList.size() - 1);
                                }
                                Log.d(TAG, "OnChildAdded fired");
                            }
                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            }
                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                Products removedProduct = dataSnapshot.getValue(Products.class);
                                String producktKey = dataSnapshot.getKey();
                                removedProduct.setId(producktKey);

                                int position = productListKeys.indexOf(producktKey);
                                productList.remove(removedProduct);
                                productListKeys.remove(position);
                                productAdapter.notifyItemRemoved(position);
                            }
                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
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

        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid());

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
    }

    private void recycleSetup() {
        mRecyclerView = findViewById(R.id.recyleViewListe);
        productAdapter = new ProductAdapter(this, productList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(productAdapter);

        productAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = mRecyclerView.getChildAdapterPosition(v);
                Toast.makeText(HandlelisteActivity.this, "Delete " + position, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(HandlelisteActivity.this);
                builder.setTitle("Are you sure about this?");
                builder.setMessage("Deletion is permanent..");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(lister.get(0)).child(productListKeys.get(position));
                        databaseReference.removeValue();
                        //Toast.makeText(HandlelisteActivity.this, "Produkter å slette: " + databaseReference, Toast.LENGTH_SHORT).show();
                        productAdapter.notifyItemRemoved(position);
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
        productAdapter.notifyDataSetChanged();
        Log.e("Adapter", " - > " + productList);
    }

    protected void onPause() {
        super.onPause();

        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
        //productList.clear();
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

    // Teste ved å legge til produkter da man trykker knappen
    public void hentAPI(View view) {
        getProdukt(cider, true);
        //getSKU(iste);
        Log.e("Adapter", productList.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                user = firebaseAuth.getCurrentUser();
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
            dialogAddNew();
        }
    }

    public void dialogAddNew() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HandlelisteActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.leggtilvare_dialog, null);
        Button leggTil = (Button) view.findViewById(R.id.leggTilBtn);
        final EditText editName = (EditText) view.findViewById(R.id.productName);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        builder.setView(view);


        leggTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString().trim();
                Products product = new Products(name);
                String id = databaseReference.push().getKey();

                if (!TextUtils.isEmpty(name)) {
                    addNewItem(product);
                } else {
                    Toast.makeText(HandlelisteActivity.this, "Skriv inn navn på produkt", Toast.LENGTH_LONG).show();
                }
                dialog.cancel();
            }

        });

        dialog.show();
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
}
