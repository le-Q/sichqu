package no.hiof.sichqu.sichqu;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Quang on 21.11.2018.
 */

public class SearchViewAdapter extends SimpleCursorAdapter {

    private static final String tag=SearchViewAdapter.class.getName();
    private Context context=null;
    public SearchViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context=context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView=(ImageView)view.findViewById(R.id.icon_feed);
        TextView textView=(TextView)view.findViewById(R.id.produkt_navn);
        /*
        ImageTagFactory imageTagFactory = ImageTagFactory.newInstance(context, R.drawable.rss_icon);
        imageTagFactory.setErrorImageId(R.drawable.rss_icon);
        ImageTag tag = imageTagFactory.build(cursor.getString(2),context);
        imageView.setTag(tag);
        FeedReaderApplication.getImageManager().getLoader().load(imageView);
        */
        textView.setText(cursor.getString(4) + " : " + cursor.getString(1));


    }
}
