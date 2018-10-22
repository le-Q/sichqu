package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.SearchView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hiof.sichqu.sichqu.Products.Products;
import no.hiof.sichqu.sichqu.Products.Produkt;

public class HandlelisteActivity extends AppCompatActivity {

    private DrawerLayout mDrawerkayout;
    private ActionBarDrawerToggle mToggle;

    TextView textView;
    String iste = "Iste grønn te lime";
    String cider = "Grevens cider skogsbær";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    SearchView searchBar;

    List<Products> productList;

    private Button logOutButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handleliste_activity);

        //Lager navigation drawer
        mDrawerkayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerkayout, R.string.open, R.string.close);
        mDrawerkayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                    startActivity(new Intent(HandlelisteActivity.this, LoginActivity.class));
                }
            }
        };

        logOutButton = (Button) findViewById(R.id.logOut);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        //RecycleView
        productList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyleViewListe);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        //productList.add(new Products("Melk"));
        //productList.add(new Products("Salt"));

        mAdapter = new ProductAdapter(this, productList);
        mRecyclerView.setAdapter(mAdapter);
        textView = findViewById(R.id.textView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
            switch (item.getItemId()) {
                case R.id.db:
                    //Bare la til noe, skal vel endres senere til noe annet?
                    return true;
                case R.id.search:
                    //Få lagt inn search mulighet her også?
                    return true;
                case R.id.settings:
                    //Bare la til noe, skal vel endres senere til noe annet?
                    return true;
                case R.id.logOut:
                    //skal få den til å logge ut, men ikke funnet ut hvordan ennå
                    mAuth.signOut();
                    return true;
         default:
        return super.onOptionsItemSelected(item);
    }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
