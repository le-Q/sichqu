<<<<<<< HEAD
package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import no.hiof.sichqu.sichqu.Products.Products;

public class addNewItem extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String id;

    private EditText editName;
    private Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        id = databaseReference.push().getKey();

        databaseReference = FirebaseDatabase.getInstance().getReference("produkter");

        editName = (EditText) findViewById(R.id.addName);
        addItem = (Button) findViewById(R.id.addNewBtn);

        addItem.setOnClickListener(this);

    }

    private void saveNewItem() {

        String name = editName.getText().toString().trim();
        Products product = new Products(name);

        if (!TextUtils.isEmpty(name)) {
            databaseReference.child(user.getUid()).child(id).setValue(product);
            Toast.makeText(this, "Varen lagt til..", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Skriv inn navn på produkt", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == addItem) {
            saveNewItem();
            finish();
            startActivity(new Intent(this, HandlelisteActivity.class));
        }
    }
}
=======
package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import no.hiof.sichqu.sichqu.Products.Products;

public class addNewItem extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String id;

    private EditText editName;
    private Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        databaseReference = FirebaseDatabase.getInstance().getReference("produkter");
        id = databaseReference.push().getKey();

        editName = (EditText) findViewById(R.id.addName);
        addItem = (Button) findViewById(R.id.addNewBtn);

        addItem.setOnClickListener(this);

    }

    private void saveNewItem() {
        String name = editName.getText().toString().trim();

        Products product = new Products(name);

        databaseReference.child(user.getUid()).child("handleliste").child(id).setValue(product);

        Toast.makeText(this, "Varen lagt til..", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v == addItem) {
            saveNewItem();
            finish();
            startActivity(new Intent(this, HandlelisteActivity.class));
        }
    }
}
>>>>>>> 29b3b8a571ea27f93c62aeb642aee439bbda5137
