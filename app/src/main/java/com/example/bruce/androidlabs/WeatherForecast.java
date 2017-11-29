package com.example.bruce.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;

public class WeatherForecast extends Activity {

    static final String ACTIVITY_NAME = "Weather Forecast";
    ProgressBar pBar;
    TextView Current;
    TextView Min;
    TextView Max;
    ImageView Weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        pBar = (ProgressBar)findViewById(R.id.ProgressBar);
        Current = (TextView)findViewById(R.id.CurrentTemp);
        Min = (TextView)findViewById(R.id.MinTemp);
        Max = (TextView) findViewById(R.id.MaxTemp);
        Weather = (ImageView)findViewById(R.id.WeatherView);

        pBar.setVisibility(View.VISIBLE);
        ForecastQuery fq = new ForecastQuery();
        fq.execute();
    }

    private class ForecastQuery extends AsyncTask<String,Integer,String>{
        String currTemp;
        String minTemp;
        String maxTemp;
        String iconName;
        Bitmap weatherPic;

        @Override
        protected String doInBackground(String... params) {
            URL url;

            try {
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                XmlPullParser xpp = Xml.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(conn.getInputStream(),null);
                int event = xpp.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = xpp.getName();

                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (name.equalsIgnoreCase("temperature")) {
                                currTemp = xpp.getAttributeValue(null, "value") + "°C";
                                this.publishProgress(25);
                                minTemp =xpp.getAttributeValue(null, "min")+ "°C";
                                this.publishProgress(50);
                                maxTemp =xpp.getAttributeValue(null, "max")+ "°C";
                                this.publishProgress(75);
                            }
                            if (name.equalsIgnoreCase("weather")) {
                                iconName = xpp.getAttributeValue(null, "icon");
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        default:
                            break;
                    }
                    event = xpp.next();
                }
            } catch (IOException e) {
                return "";
//                Log.e("ERROR","IOException");
            } catch (XmlPullParserException e) {
                return "";
//                Log.e("ERROR","XmlPullParseException");
            }

            File file = getBaseContext().getFileStreamPath(iconName+".png");
            try{
                FileInputStream fis = openFileInput(iconName+".png");
                weatherPic = BitmapFactory.decodeStream(fis);
                this.publishProgress(100);
                Log.i(ACTIVITY_NAME, "Image already existed");
            }catch(FileNotFoundException e){
                Log.i(ACTIVITY_NAME, "Image not exist, need to be download first");
                try {
                    URL imageUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                    HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        weatherPic = BitmapFactory.decodeStream(connection.getInputStream());
                        this.publishProgress(100);
                        FileOutputStream outputStream = openFileOutput(iconName+".png", Context.MODE_PRIVATE);
                        weatherPic.compress(Bitmap.CompressFormat.PNG,80,outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                }catch(MalformedURLException g){
                    e.printStackTrace();
                }catch (IOException i) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer...value){
            super.onProgressUpdate(value);
        }

        @Override
        protected void onPostExecute(String result) {
            Current.setText(this.currTemp);
            Min.setText(this.minTemp);
            Max.setText(this.maxTemp);
            Weather.setImageBitmap(this.weatherPic);
            pBar.setVisibility(View.INVISIBLE);
        }
    }
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }

    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }
}
