package no.hiof.sichqu.sichqu;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by Quang on 21.11.2018.
 */

public class SearchViewAdapter extends SimpleCursorAdapter {

    private static final String tag = SearchViewAdapter.class.getName();
    private Context context = null;

    public SearchViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView=(ImageView)view.findViewById(R.id.produkt_bilde);
        TextView textView=(TextView)view.findViewById(R.id.produkt_navn);

        textView.setText(cursor.getString(1));
        Picasso.get().load(cursor.getString(2)).into(imageView);

    }
}
