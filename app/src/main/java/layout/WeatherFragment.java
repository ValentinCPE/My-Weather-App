package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import lyon1.iut.weatherapp.AsyncTask;
import lyon1.iut.weatherapp.CitySaved;
import lyon1.iut.weatherapp.R;


public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    TextView champVille;
    TextView champMAJ;
    TextView champDetail;
    TextView champTemp;
    TextView IconeMeteo;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        champVille = (TextView)rootView.findViewById(R.id.city_field);
        champMAJ = (TextView)rootView.findViewById(R.id.updated_field);
        champDetail = (TextView)rootView.findViewById(R.id.details_field);
        champTemp = (TextView)rootView.findViewById(R.id.current_temperature_field);
        IconeMeteo = (TextView)rootView.findViewById(R.id.weather_icon);

        IconeMeteo.setTypeface(weatherFont);
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weatherIcon.ttf");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        majMeteo(new CitySaved(getActivity()).getCity());
       // changeCity("Villeurbanne, FR");
    }

    public void changeCity(String city){
        majMeteo(city);
    }

    private void majMeteo(final String city){
        new AsyncTask().execute(city,champVille,champMAJ,champDetail,champTemp,IconeMeteo,getActivity());
    }
}
