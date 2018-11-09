package no.hiof.sichqu.sichqu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import no.hiof.sichqu.sichqu.Products.Products;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Products> productList;
    private LayoutInflater inflater;

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
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        Products product = productList.get(i);
        productViewHolder.setData(product);

        productViewHolder.textViewTitle.setText(product.getName());
        if (product.getImages() != null)
        Picasso.get().load(product.getImages()[0].getThumbnail().getUrl()).into(productViewHolder.thumbnails);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        ImageView thumbnails;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnails = itemView.findViewById(R.id.thumb);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }

        public void setData(Products currentProd) {
            this.textViewTitle.setText(currentProd.getName());

            thumbnails.setImageResource(R.drawable.poster_placeholder);
        }
    }
}
