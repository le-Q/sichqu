package no.hiof.sichqu.sichqu;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Objects;

import no.hiof.sichqu.sichqu.Products.Products;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Products> productList;
    private LayoutInflater inflater;
    private ImageButton deleteItem;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private View.OnClickListener clickListener;

    public void setOnItemClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public ProductAdapter(Context mCtx, List<Products> productList) {
        this.productList = productList;
        this.inflater = LayoutInflater.from(mCtx);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.cardview, viewGroup, false);
        ProductViewHolder holder = new ProductViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, final int i) {
        final Products product = productList.get(i);
        productViewHolder.setData(product);

        if (clickListener != null) {
            productViewHolder.itemView.setOnClickListener(clickListener);
        }

        productViewHolder.textViewTitle.setText(product.getName());
        if (product.getThumbnail() != null)
        Picasso.get().load(product.getThumbnail()).into(productViewHolder.thumbnails);

        productViewHolder.thumbnails.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ChangeImage(productViewHolder);
                return true;
            }
        });
    }

    public void setListData(List<Products> newData) {
        this.productList.clear();
        productList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewTitle;
        ImageView thumbnails;
        CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnails = itemView.findViewById(R.id.thumb);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            cardView = itemView.findViewById(R.id.cardViewId);

            user = FirebaseAuth.getInstance().getCurrentUser();
        }

        public void setData(Products currentProd) {
            this.textViewTitle.setText(currentProd.getName());
            thumbnails.setImageResource(R.drawable.poster_placeholder);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void ChangeImage(@NonNull ProductViewHolder productViewHolder) {
        Picasso.get().load(R.drawable.poster_placeholder).into(productViewHolder.thumbnails);
    }

    public List<Products> getList() {
        return productList;
    }
}
