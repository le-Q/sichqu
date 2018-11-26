package no.hiof.sichqu.sichqu;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.SimpleAdapter;
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
    private int listposition;
    public static final String LIST_UID = "list_uid";
    public static String LIST_POS = "list_pos";

    String cider = "Grevens cider skogsbær";
    String testHandleliste = "Handleliste";

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
    private SearchView searchView;

    private ArrayList<String> lister = new ArrayList<>();
    private ArrayList<String> arrayHandleliste = new ArrayList<>();

    public static String[] columns = new String[]{"_id", "PRODUKT_navn", "PRODUKT_img"};
    private Produkt produktinfo;
    private List<Map<String, String>> produktinfodisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DeltPreferanse sharedpref = new DeltPreferanse(this);
        if (sharedpref.loadNightModeState())
            setTheme(R.style.darktheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.handleliste_activity);

        firebaseAuth = FirebaseAuth.getInstance();

        String productUid = getIntent().getStringExtra(LIST_UID);
        listposition = getIntent().getIntExtra(LIST_POS, 0);
        getSupportActionBar().setTitle(productUid);

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
        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(testHandleliste);

        productAdapter = new ProductAdapter(getApplicationContext(), productList);

        skuScan = new IntentIntegrator(this);
        recycleSetup();


        goSpinner();
        // Hente handlelisten
        firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    lister.add(snap.getKey());
                }

                if (!lister.isEmpty()) {
                    databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(lister.get(0));
                } else {
                    firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid());
                    startActivity(new Intent(HandlelisteActivity.this, HvisListeneActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid());


    }

    private void goSpinner() {
        // Spinner
        // Hente handlelister
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar22);
        firebaseDatabase.getReference().child("produkter").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayHandleliste.clear();
                for (DataSnapshot nameListShot : dataSnapshot.getChildren()) {
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
                if (listposition > 0)
                navigationSpinner.setSelection(listposition);

                navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        testHandleliste = arrayHandleliste.get(position);
                        getSupportActionBar().setTitle(arrayHandleliste.get(position));

                        firebaseDatabase.getReference().child("produkter").child(firebaseAuth.getUid()).child(testHandleliste).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snap) {

                                ArrayList<Products> lister = new ArrayList<>();
                                for (DataSnapshot nameListShot : snap.getChildren()) {
                                    Products product = nameListShot.getValue(Products.class);
                                    lister.add(product);
                                }
                                testHandleliste = snap.getKey();
                                productAdapter.setListData(lister);
                                databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(testHandleliste);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
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
                for (DataSnapshot nameListShot : dataSnapshot.getChildren()) {
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
                builder.setTitle("Vil du slette?");

                builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference = firebaseDatabase.getReference("produkter").child(firebaseAuth.getUid()).child(lister.get(0)).child(productListKeys.get(position));
                        databaseReference.removeValue();
                        productAdapter.notifyItemRemoved(position);
                    }
                });

                builder.setNegativeButton("Nei", new DialogInterface.OnClickListener() {
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

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.searchmenu);
        searchView = (SearchView) menuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getProdukt(query, true, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                /*if (s.length() > 3) {
                    getProdukt(s, true);
                }*/
                return true;
            }

        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                String place = cursor.getString(1);
                cursor.close();
                searchView.setQuery(place, false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                String produkt = cursor.getString(4);
                searchView.setQuery(produkt, true);
                searchView.clearFocus();
                return true;
            }
        });
        //return super.onCreateOptionsMenu(menu);
        return true;
    }


    private MatrixCursor convertToCursor(Produkt produkter) {
        MatrixCursor cursor = new MatrixCursor(columns);
        int i = 0;
        for (Products produktet : produkter.getProducts()) {
            String[] temp = new String[3];
            i = i + 1;
            temp[0] = Integer.toString(i);

            String produktUrl = produktet.getThumbnail();
            if (produktUrl == null)
                produktUrl = "https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.dirtyapronrecipes.com%2Fwp-content%2Fuploads%2F2015%2F10%2Ffood-placeholder.png&f=1";
            temp[1] = produktet.getName();
            temp[2] = produktUrl;
            cursor.addRow(temp);
        }
        return cursor;
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
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned " + result.getContents(), Toast.LENGTH_LONG).show();
                getProdukt(result.getContents(), false, false);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Knapp for floating action button
    public void scanItem(View v) {
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
        final EditText editName = view.findViewById(R.id.productName);


        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setTitle("Legg til nytt produkt");
        builder.setView(view);

        String[] from={"produkt_image","produkt_navn"};
        int[] to={R.id.produkt_bilde,R.id.produkt_navn};
        SimpleAdapter listadapter=new SimpleAdapter(HandlelisteActivity.this,produktinfodisplay,R.layout.search_layout,from,to);


        leggTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString().trim();
                Products product = new Products(name);

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

    private void getProdukt(String produktKode, final Boolean kolonial, final Boolean firstArray) {
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
                            produktinfo = gson.fromJson(response.toString(), Produkt.class);
                            convertToCursor(produktinfo);
                            /*for(Products item : produktinfo.getProducts()) {
                                Map<String, String> nyProdukt = new HashMap<>();
                                nyProdukt.put(item.getThumbnail(),item.getName());
                                produktinfodisplay.add(nyProdukt);
                            }*/


                            if(firstArray) {
                                Products nyProdukt = produktinfo.getProducts()[0];
                                nyProdukt.setThumbnail(nyProdukt.getImages()[0].getThumbnail().getUrl());
                                addNewItem(nyProdukt);
                            }
                        } else {
                            UPC_data upc_produkt = gson.fromJson(response.toString(), UPC_data.class);
                            if (upc_produkt.getUpcnumber().equals("7038010001215"))
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
