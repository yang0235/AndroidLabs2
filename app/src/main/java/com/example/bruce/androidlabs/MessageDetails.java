package com.example.bruce.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import static com.example.bruce.androidlabs.WeatherForecast.ACTIVITY_NAME;

public class MessageDetails extends Activity {

    private static final String ACTIVITY_NAME = "MessageDetails";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle bundle = new Bundle();
        bundle.putString("Message",getIntent().getStringExtra("Message"));
        bundle.putString("MessageId",getIntent().getStringExtra("MessageId"));

        Fragment mFragment = new MessageFragment();
        mFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.listDetail_Phone,mFragment);
        fragmentTransaction.commit();
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
