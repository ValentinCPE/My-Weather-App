package lyon1.iut.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

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
        try
        {
            videoElem = (VideoView) findViewById(R.id.myvideo);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.iut_intro);
            videoElem.setVideoURI(video);
            videoElem.setZOrderOnTop(true);
            videoElem.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp) {
                    start();
                }});
            videoElem.start();

        } catch(Exception ex) {
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
}
