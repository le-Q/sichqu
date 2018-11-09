package no.hiof.sichqu.sichqu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.util.List;

import no.hiof.sichqu.sichqu.Products.Products;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private LayoutInflater inflater;

    public ProductAdapter(Context mCtx, List<Product> productList) {
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
        Product product = productList.get(i);
        productViewHolder.setData(product);

<<<<<<< HEAD
        productViewHolder.textViewTitle.setText(product.getFull_name());
        if (product.getImages() != null)
        Picasso.get().load(product.getImages()[0].getThumbnail().getUrl()).into(productViewHolder.thumbnails);
=======
        //productViewHolder.textViewTitle.setText(product.getFull_name());
        //Picasso.get().load(product.getImages()[0].getThumbnail().getUrl()).into(productViewHolder.thumbnails);
>>>>>>> NewItem
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

        public void setData(Product currentProd) {
            this.textViewTitle.setText(currentProd.getTitle());

            thumbnails.setImageResource(R.drawable.poster_placeholder);
        }
    }



    //Sender inn en url og gjør det om til en bitmap
    public Bitmap ImageLoadTask(String url) {
        try {
            URL urlConnection = new URL(url);
            //HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            //connection.setDoInput(true);
            //connection.connect();
            //InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(urlConnection.openConnection().getInputStream());
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
