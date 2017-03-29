package lyon1.iut.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by valentin on 29/03/17.
 */

public class CitySaved {
    SharedPreferences prefs;

    public CitySaved(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return prefs.getString("city", "Villeurbanne,fr");
    }

    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
