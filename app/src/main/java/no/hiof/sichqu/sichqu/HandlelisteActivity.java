package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hiof.sichqu.sichqu.Products.Products;
import no.hiof.sichqu.sichqu.Products.Produkt;

public class HandlelisteActivity extends AppCompatActivity {

    String iste = "Iste grønn te lime";
    String cider = "Grevens cider skogsbær";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    SearchView searchBar;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ChildEventListener childEventListener;

    private TextView productTitleTextView;

    private List<Products> productList;
    private List<String> productListKeys;

    private Button logOutButton;
    private ImageButton addNewButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handleliste_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    finish();
                    startActivity(new Intent(HandlelisteActivity.this, LoginActivity.class));
                }
            }
        };

        logOutButton = (Button) findViewById(R.id.logOut);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
            }
        });

        //New item button
        addNewButton = (ImageButton) findViewById(R.id.addNewBtn);

        //productList.add(new Products("Melk"));
        //productList.add(new Products("Salt"));

        productList = new ArrayList<>();
        productListKeys = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // Query til database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("produkter");


        productTitleTextView = findViewById(R.id.textViewTitle);
        
        recycleSetup();
    }

    private void recycleSetup() {
        mRecyclerView = findViewById(R.id.recyleViewListe);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
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
                getResponse(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // Teste ved å legge til produkter da man trykker knappen
    public void hentAPI(View view) {
        getResponse(cider);
        //getName(iste);
    }

    private void getResponse(String produktNavn) {
        String URL = "https://kolonial.no/api/v1/search/?q="+produktNavn;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Produkt produkt = gson.fromJson(response.toString(), Produkt.class);
                        productList.add(produkt.getProducts()[0]);
                        Log.e("Check Error", response.toString());
                        mAdapter = new ProductAdapter(HandlelisteActivity.this, productList);
                        mRecyclerView.setAdapter(mAdapter);
                        Log.e("Check Error", produkt.getProducts()[0].getImages()[0].getThumbnail().getUrl());
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
                map.put("User-Agent", "QuangLe_Test");
                map.put("X-Client-Token", "Pcd4FnKoER3Bfk2trn38DHV8umdCYkteOlp55CH9q0PoCDa2xc");
                return map;
            }
        };
        //request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        queue.add(request);
    }

    public void addNewItem(View v) {
        if (v == addNewButton) {
            Intent intent = new Intent(this, addNewItem.class);
            startActivity(intent);
        }
    }

    /* SKU kode henter
    private void getName(String skuKode) {
        String URL = "https://api.upcdatabase.org/product/"+skuKode+"/9E29B3CE1CA7A534EF90DCEC796F94AD";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //textView.setText("Response : " + response.toString());
                        Log.e("Check Error", response.toString());
                        Gson gson = new Gson();
                        UPC_data upc_produkt = gson.fromJson(response.toString(), UPC_data.class);
                        Log.e("Check Error", upc_produkt.getUpcnumber());
                        getResponse(upc_produkt.getTitle());

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
                map.put("X-Client-Token", "9E29B3CE1CA7A534EF90DCEC796F94AD");
                return map;
            }
        };
        //request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        queue.add(request);
    }
    */
}
