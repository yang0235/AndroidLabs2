package com.example.bruce.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.sax.RootElement;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.os.Build.ID;
import static com.example.bruce.androidlabs.ChatDatabaseHelper.KEY_ID;
import static com.example.bruce.androidlabs.ChatDatabaseHelper.KEY_MESSAGE;
import static com.example.bruce.androidlabs.ChatDatabaseHelper.MESSAGE_FIELDS;
import static com.example.bruce.androidlabs.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {

    private Button btnSend;
    private EditText chatText;
    private ListView chatView;
    private ArrayList<String>  storedMessage;
    protected static final String ACTIVITY_NAME = "ChatWindow";
    private ChatAdapter messageAdapter;

    private ChatDatabaseHelper cDbhelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        btnSend = (Button)findViewById(R.id.buttonSend);
        chatText = (EditText)findViewById(R.id.chatText);
        chatView = (ListView)findViewById(R.id.chatView);
        storedMessage = new ArrayList<String>();

        messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);
        cDbhelper= new ChatDatabaseHelper(this);
        db = cDbhelper.getWritableDatabase();

        if (findViewById(R.id.listDetail_Tablet) != null){
            Log.i(ACTIVITY_NAME, "RUN ON TABLET.");
        }else{
            Log.i(ACTIVITY_NAME,"RUN ON PHONE.");
        }

        chatView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view, int position, long id){

                String dmessage = parent.getItemAtPosition(position).toString();
                Long messID = parent.getItemIdAtPosition(position);
                if(findViewById(R.id.listDetail_Tablet) != null){
                    Bundle bundle = new Bundle();
                    bundle.putString("Message",dmessage);
                    bundle.putString("MessageId",messID+"");
                    bundle.putBoolean("isTablet",true);
                    MessageFragment mFragment = new MessageFragment();
                    mFragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.listDetail_Tablet,mFragment).commit();
                }else {
                Intent intent = new Intent(ChatWindow.this,MessageDetails.class);
                intent.putExtra("Message",dmessage);
                intent.putExtra("MessageId",messID+"");
                startActivityForResult(intent,10);}
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String input = chatText.getText().toString();
            storedMessage.add(input);
            messageAdapter.notifyDataSetChanged();
            chatText.setText("");

            ContentValues initial = new ContentValues();
            initial.put(KEY_MESSAGE,input);
            db.insert(TABLE_NAME, "", initial);
        }});

        cursor = db.query( false, TABLE_NAME, MESSAGE_FIELDS, null, null, null, null, null, null);
//        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME ,null);
        int numResults = cursor.getCount();
        int numColumns = cursor.getColumnCount();

        cursor.moveToFirst();//resets the iteration of results
        while(!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            storedMessage.add(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME,"Cursor's column count = " + cursor.getColumnCount());

        cursor.moveToFirst();
        for(int i=0;i<numResults;i++){
            Log.i(ACTIVITY_NAME,"The " + i + " row is " + cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            cursor.moveToNext();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10){
            String id = data.getStringExtra("MessageId");
            cDbhelper.DeleteMessage(id);
        }
    }

    private class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }

        public int getCount(){
            return storedMessage.size();
        }

        public String getItem(int postion){
            return storedMessage.get(postion);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result=null;
            if(position%2==0)
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing,null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(KEY_ID));
        }
    }

    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
        notifyChange();
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
        db.close();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

    public void notifyChange(){

        storedMessage = new ArrayList<String>();
        chatView.setAdapter(messageAdapter);
        cDbhelper= new ChatDatabaseHelper(this);
        db = cDbhelper.getWritableDatabase();

        cursor = db.query( false, TABLE_NAME, MESSAGE_FIELDS, null, null, null, null, null, null);
//        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME ,null);
        int numResults = cursor.getCount();
        int numColumns = cursor.getColumnCount();

        cursor.moveToFirst();//resets the iteration of results
        while(!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            storedMessage.add(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME,"Cursor's column count = " + cursor.getColumnCount());

        cursor.moveToFirst();
        for(int i=0;i<numResults;i++){
            Log.i(ACTIVITY_NAME,"The " + i + " row is " + cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            cursor.moveToNext();
        }
    }
}
