package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {

        String uv;
        String min;
        String max;
        String currentTemperature;
        String iconName;
        Bitmap image = null;

        @Override
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String jsonURL = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("temperature"))
                            {
                                min = xpp.getAttributeValue(null, "min");
                                publishProgress(25);
                                max = xpp.getAttributeValue(null, "max");
                                publishProgress(50);
                                currentTemperature = xpp.getAttributeValue(null, "value");
                                publishProgress(75);

                            }

                            else if(tagName.equals("weather")){
                                iconName = xpp.getAttributeValue(null, "icon");

                            }
                            break;
                        /*case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:
                            String tagName = xpp.getName();
                            if(tagName.equals("temperature"))
                            {
                                xpp.next();
                                String country = xpp.getText();

                            }//This is text between tags < ... > Hello world </ ... >
                            break;*/
                    }
                    xpp.next(); // move the pointer to next XML element
                }
                //Bitmap image = null;
                URL urlIcon = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                HttpURLConnection connection = (HttpURLConnection) urlIcon.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();







                if(fileExistance(iconName + ".png")){ // this if was originally below the if after it
                    FileInputStream fis = null;
                    try {    fis = openFileInput(iconName + ".png");
                        Log.i("Image found", "Image found :)");}
                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                    image = BitmapFactory.decodeStream(fis);
                    Log.i("Image not found:", "Image not found :(");

                }
                else {
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        publishProgress(100);

                        FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE); // starting from here, normally outside if
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                }
                //JSON reading:
                //Build the entire string response:
                URL json = new URL(jsonURL);
                HttpURLConnection jsonConn = (HttpURLConnection) json.openConnection();
                InputStream response = jsonConn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                JSONObject jObject = new JSONObject(result);
                float uvInt = (float) jObject.getDouble("value");
                uv = Float.toString(uvInt);

            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}
            catch (JSONException e) { e.printStackTrace(); }
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
            //ProgressBar progressBar = findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView minTemp = findViewById(R.id.minTemperature);
            minTemp.setText(minTemp.getText().toString() + min);
            TextView maxTemp = findViewById(R.id.maxTemperature);
            maxTemp.setText(maxTemp.getText().toString() + max);
            TextView currentTemp = findViewById(R.id.currentTemperature);
            currentTemp.setText(currentTemp.getText().toString() + currentTemperature);
            ImageView image = findViewById(R.id.currentWeather);
            image.setImageBitmap(this.image);
            TextView uvValue = findViewById(R.id.uvRating);
            uvValue.setText(uvValue.getText().toString() + uv);

        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }

    }
}
