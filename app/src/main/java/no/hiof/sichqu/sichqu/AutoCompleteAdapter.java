package no.hiof.sichqu.sichqu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import no.hiof.sichqu.sichqu.Products.Products;
import no.hiof.sichqu.sichqu.Products.Produkt;

/**
 * Created by Quang on 23.11.2018.
 */

public class AutoCompleteAdapter extends ArrayAdapter<Products> {
    private List<Products> produktListeFull;

    public AutoCompleteAdapter(@NonNull Context context,@NonNull Produkt produktArray) {
        super(context, 0, produktArray.getProducts());
        for (Products etProdukt : produktArray.getProducts())
            produktListeFull.add(etProdukt);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return produktFilter;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.search_layout, parent, false
            );
        }

        TextView produktNavn = convertView.findViewById(R.id.produkt_navn);
        ImageView produktBilde = convertView.findViewById(R.id.produkt_bilde);

        Products produktItem = getItem(position);
        if (produktItem != null) {
            produktNavn.setText(produktItem.getName());
            if (produktItem.getThumbnail() != null)
                Picasso.get().load(produktItem.getThumbnail()).into(produktBilde);
        }

        return convertView;
    }

    private Filter produktFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<Products> suggestions = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                suggestions.addAll(produktListeFull);
            } else {
                String filterpattern = charSequence.toString().toLowerCase().trim();
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Products) resultValue).getName();
        }
    };
}
