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

/**
 * Created by jeremy on 10/03/17.
 */

public class AsyncTask extends android.os.AsyncTask<Object,Void,JSONObject> {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/forecast?q=%s&APPID=910f0c05f62e5508a3428198252eed06&units=metric";

    String chaine, chaine2;
    BufferedReader in;
    URL url = null;

    JSONObject jsonobj;

    TextView champVille;
    TextView champMAJ;
    TextView champDetail;
    TextView champTemp;
    TextView IconeMeteo;
    Activity weatherFragmentActivity;

    @Override
    protected JSONObject doInBackground(Object... params) {
        chaine = (String) params[0];
        weatherFragmentActivity = (Activity) params[1];
        champVille = (TextView) params[2];
        champMAJ = (TextView) params[3];
        champDetail = (TextView) params[4];
        champTemp = (TextView) params[5];
        IconeMeteo = (TextView) params[6];


        try {
                url = new URL(String.format(OPEN_WEATHER_MAP_API, chaine));

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream() ) );

                    StringBuffer json = new StringBuffer(1024);

                    while((chaine2=in.readLine())!=null)
                        json.append(chaine2).append("\n");

                    in.close();

                    jsonobj = new JSONObject(json.toString());


                    if(jsonobj.getInt("cod") != 200){
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

                            //new object for new forecast
                            weatherData = new Weather();

                            weatherData.setUpdateTime(oneValueListJSON.getLong("dt"));

                            weatherData.setHumidity(main.getString("humidity"));
                            weatherData.setPressure(main.getString("pressure"));

                            weatherData.setTemperature(main.getDouble("temp"));
                            weatherData.setIcone(details.getInt("id"),weatherFragmentActivity);

                            previsionsVille.add(weatherData);
                        }

                        Forecast.addForecast(chaine,previsionsVille);


                        //using of the class Forecast with the only useful date
                            ArrayList<Weather> forecastsReader = Forecast.getWeatherList(chaine);

                            champVille.setText(chaine);
                            champMAJ.setText(forecastsReader.get(0).getUpdateTimeToString());
                            champDetail.setText("Humidity : " + forecastsReader.get(0).getHumidity()
                                    + "%\nPressure : " + forecastsReader.get(0).getPressure() + " Pa");
                            champTemp.setText(String.valueOf(forecastsReader.get(0).getTemperature()) + " °C");
                            IconeMeteo.setText(forecastsReader.get(0).getIcone());

                          /*  for(int i = 0; i < forecastsReader.size(); i++) {
                                Log.d("testHour", forecastsReader.get(i).getUpdateTimeToString());
                            } */
                    } catch (JSONException e) {
                        Toast.makeText(weatherFragmentActivity,
                                weatherFragmentActivity.getString(R.string.error_fetch_data),
                                Toast.LENGTH_LONG).show();
                        System.err.println(e.getMessage());
                    }
                       /* try {
                            champVille.setText(jsonobj.getString("name").toUpperCase(Locale.FRANCE) +
                                    ", " +
                                    jsonobj.getJSONObject("sys").getString("country"));

                            JSONObject details = jsonobj.getJSONArray("weather").getJSONObject(0);
                            JSONObject main = jsonobj.getJSONObject("main");
                            champDetail.setText(
                                    details.getString("description").toUpperCase(Locale.FRANCE) +
                                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

                            champTemp.setText(
                                    String.format("%.1f", main.getDouble("temp"))+ " ℃");

                            DateFormat df = DateFormat.getDateTimeInstance();
                            String updatedOn = df.format(new Date(jsonobj.getLong("dt")*1000));
                            champMAJ.setText("Last update: " + updatedOn);

                            setWeatherIcon(details.getInt("id"),
                                    jsonobj.getJSONObject("sys").getLong("sunrise") * 1000,
                                    jsonobj.getJSONObject("sys").getLong("sunset") * 1000);

                        }catch(Exception e){
                            Log.e("weather", "data not found in the JSON");
                            Log.e("weather", e.getMessage());
                        } */
                }
    }

}
