package no.hiof.sichqu.sichqu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.hiof.sichqu.sichqu.Products.Produkt;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    String iste = "Iste grønn te lime";
    String cider = "Grevens cider skogsbær";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    List<Product> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyleViewListe);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        productList.add(
                new Product(
                        1,
                        "Melk",
                        "Melk fra Tine",
                        27));
        productList.add(
                new Product(1,
                        "Salt",
                        "Fancy salt",
                        15));

        mAdapter = new ProductAdapter(this, productList);
        mRecyclerView.setAdapter(mAdapter);
        textView = findViewById(R.id.textView);
    }

    public void hentAPI(View view) {
        getResponse();
        //getName(iste);
    }

    private void getResponse() {
        String URL = "https://kolonial.no/api/v1/search/?q=";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Produkt produkt = gson.fromJson(response.toString(), Produkt.class);
                        textView.setText("Response : " + produkt.getProducts()[0].getFull_name());
                        Log.e("Check Error", response.toString());
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
