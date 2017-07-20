package ru.sgu.csiit.sgu17;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Juli on 18.07.2017.
 */

public class LoadImage extends AsyncTask<String, Void, Bitmap> {
    String src;
    ImageView imageView;

    public LoadImage(String src, ImageView imageView) {
        this.src = src;
        this.imageView = imageView;
    }
    @Override
    protected void onPostExecute(Bitmap result){
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
       try{
           URL url = new URL(src);
           InputStream inputStream = url.openConnection().getInputStream();
           Bitmap bmp = BitmapFactory.decodeStream(inputStream);
           return  bmp;
       }
       catch (MalformedURLException e){
           e.printStackTrace();
       }
       catch (IOException e){
           e.printStackTrace();
       }
       return null;
    }
}
