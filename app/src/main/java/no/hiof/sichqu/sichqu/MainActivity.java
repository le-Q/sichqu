package no.hiof.sichqu.sichqu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
                        R.drawable.ic_launcher_background,
                        27));

        mAdapter = new ProductAdapter(this, productList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
