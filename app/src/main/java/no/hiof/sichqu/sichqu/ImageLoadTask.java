package no.hiof.sichqu.sichqu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private Bitmap bitmap;
    private String url;

    public ImageLoadTask(String url) {
        this.url = url;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {

        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}
