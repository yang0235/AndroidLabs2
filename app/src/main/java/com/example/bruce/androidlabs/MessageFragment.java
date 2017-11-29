package com.example.bruce.androidlabs;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by bruce on 2017-11-28.
 */

public class MessageFragment extends Fragment {

        private TextView Message, MessageId;
        private Button DeleteButton;
        private SQLiteDatabase db;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
            View v;
            v = inflater.inflate(R.layout.fragment,container,false);
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Message = getView().findViewById(R.id.detailMessage);
            MessageId = getView().findViewById(R.id.messageID);
            DeleteButton = getView().findViewById(R.id.dButton);

            Message.setText(getArguments().getString("Message"));
            MessageId.setText(getArguments().getString("MessageId"));
            final ChatDatabaseHelper db = new ChatDatabaseHelper(getActivity());


            DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getArguments().getBoolean("isTablet") == true) {
                        db.DeleteMessage(getArguments().getString("MessageId"));
                        ((ChatWindow)getActivity()).notifyChange();
                        getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                    }else{
                    Intent result = new Intent();
                    result.putExtra("MessageId",getArguments().getString("MessageId"));
                    getActivity().setResult(10,result);
                    getActivity().finish();}
                }
            });
        }
}
