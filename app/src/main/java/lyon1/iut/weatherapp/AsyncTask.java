package lyon1.iut.weatherapp;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import layout.WeatherFragment;

/**
 * Created by jeremy on 10/03/17.
 */

public class AsyncTask extends android.os.AsyncTask<Object,Void,JSONObject> {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast?q=%s&APPID=910f0c05f62e5508a3428198252eed06&units=metric";

    String ville, chaine2;
    BufferedReader in;
    URL url = null;

    private static boolean first = false;

    JSONObject jsonobj;

    Activity weatherFragmentActivity;

    @Override
    protected JSONObject doInBackground(Object... params) {
        ville = (String) params[0];
        weatherFragmentActivity = (Activity) params[1];


        try {
                url = new URL(String.format(OPEN_WEATHER_MAP_API, ville));

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream() ) );

                    StringBuffer json = new StringBuffer(1024);

                    while((chaine2=in.readLine())!=null)
                        json.append(chaine2).append("\n");

                    in.close();

                    jsonobj = new JSONObject(json.toString());


                    if(jsonobj.getInt("cod") != 200){
                        Log.d("apitest", "api problem");
                        return null;
                    }


                    return jsonobj;
                }
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            } catch (JSONException e) {
                return null;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(JSONObject jsonobj) {
        if(jsonobj == null){
                    Toast.makeText(weatherFragmentActivity,
                            weatherFragmentActivity.getString(R.string.place_not_found),
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Weather weatherData;
                        ArrayList<Weather> previsionsVille = new ArrayList<>(); //list of objects Weather representing the forecast
                        JSONObject details, main, sys, oneValueListJSON;

                        JSONArray forecastsList = jsonobj.getJSONArray("list"); //forecast fetched in the API

                        for(int i = 0; i < forecastsList.length(); i++)
                        {
                            oneValueListJSON = forecastsList.getJSONObject(i);
                            details = oneValueListJSON.getJSONArray("weather").getJSONObject(0);
                            main = oneValueListJSON.getJSONObject("main");
                            sys = oneValueListJSON.getJSONObject("sys");

                            String date = oneValueListJSON.getString("dt_txt");
                            String hour = date.substring(11,13);

                            if(Integer.valueOf(hour) > 8 && Integer.valueOf(hour) < 20) {
                                //new object for new forecast
                                weatherData = new Weather();

                                weatherData.setUpdateTime(oneValueListJSON.getString("dt_txt"));

                                weatherData.setTimeDescription(details.getString("description"));
                                weatherData.setHumidity(main.getString("humidity"));
                                weatherData.setPressure(main.getString("pressure"));

                                weatherData.setTemperature(main.getDouble("temp"));
                                weatherData.setIcone(details.getInt("id"), weatherFragmentActivity);

                                previsionsVille.add(weatherData);
                            }
                        }

                        Forecast.addForecast(ville,previsionsVille);

                        if(first){
                            Log.d("maj", "champs changent");
                            WeatherFragment.majChamps();
                        }

                        first = true;

                        Log.d("testgps2", ville);

                    } catch (JSONException e) {
                        Toast.makeText(weatherFragmentActivity,
                                weatherFragmentActivity.getString(R.string.error_fetch_data),
                                Toast.LENGTH_LONG).show();
                        System.err.println(e.getMessage());
                    }
                }
    }

}
