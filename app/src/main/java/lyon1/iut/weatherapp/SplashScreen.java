package lyon1.iut.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import layout.WeatherFragment;

/**
 * Created by valentin on 29/03/17.
 */

public class SplashScreen extends Activity{
    VideoView videoElem;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);


        majMeteo(new CitySaved(SplashScreen.this).getCity());

        try
        {
            videoElem = (VideoView) findViewById(R.id.myvideo);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.iut_intro);
            videoElem.setVideoURI(video);
            videoElem.setZOrderOnTop(true);
            videoElem.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp) {
                    testConnection();
                }});
            videoElem.start();

        } catch(Exception ex) {
            start();
        }

    }

    private void testConnection()
    {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            quit();
        }else{
            start();
        }
    }

    private void start()
    {
        if(isFinishing())
            return;
        startActivity(new Intent(this, WeatherActivity.class));
        finish();
    }

    private void quit()
    {
        Toast.makeText(SplashScreen.this,
                "No connection, App closed !",
                Toast.LENGTH_LONG).show();
        finish();
    }

    public void majMeteo(final String city){
        new AsyncTask().execute(city,SplashScreen.this);
    }
}
