package com.robbcocco.gwenttracker.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {

    private static final String TAG = "DownloadImageTask";
    private final WeakReference<ImageView> containerImageView;

    public DownloadImageTask(ImageView imageView) {
        this.containerImageView = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(URL... params) {
        URL imageURL = params[0];
        Bitmap downloadedBitmap = null;
        try {
            InputStream inputStream = imageURL.openStream();
            downloadedBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return downloadedBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        ImageView imageView = this.containerImageView.get();
        if (imageView != null) {
            imageView.setImageBitmap(result);
        }
    }
}