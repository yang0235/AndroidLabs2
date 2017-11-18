package com.example.bruce.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
//    static final int PICK_ACTIVITY_REQUEST = 10;
//    static final int RESULT_OK = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        Button button = (Button)findViewById(R.id.button);
        Button buttonChat = (Button)findViewById(R.id.buttonChat);
        Button buttonWeather = (Button)findViewById(R.id.WeatherForecast);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this,ListItemsActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        buttonChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View e) {
                Intent intent = new Intent(StartActivity.this,ChatWindow.class);
                startActivity(intent);
                Log.i(ACTIVITY_NAME,"User clicked Start Chat");
            }
        });

        buttonWeather.setOnClickListener(new View.OnClickListener() {
            public void onClick(View w){
                Intent intent = new Intent(StartActivity.this,WeatherForecast.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data){
//        if (requestCode == 10){
//            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
//        }
//        if (responseCode == Activity.RESULT_OK){
//            String messagePassed = data.getStringExtra("Response");
//            CharSequence text = "ListItemActivity passed: ";
//            int duration = Toast.LENGTH_LONG;
//            Toast toast = Toast.makeText(getApplicationContext(), text+messagePassed, duration); //this is the ListActivity
//            toast.show();
//        }
        if ((requestCode == 10 && responseCode == Activity.RESULT_OK) || responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME,"Returned to StartActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            CharSequence text = "ListItemActivity passed: ";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), text+messagePassed, duration); //this is the ListActivity
            toast.show();
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