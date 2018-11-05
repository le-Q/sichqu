package no.hiof.sichqu.sichqu;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class addNewItem extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private EditText editName;
    private Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("produkter");

        editName = (EditText) findViewById(R.id.addName);
        addItem = (Button) findViewById(R.id.addNewBtn);

        addItem.setOnClickListener(this);

    }

    private void saveNewItem() {
        String name = editName.getText().toString().trim();

        String id = databaseReference.push().getKey();

        Product product = new Product(name);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).child(id).setValue(product);


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
