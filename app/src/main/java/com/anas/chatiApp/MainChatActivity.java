package com.anas.chatiApp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDataBaseReference;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_chat);

        // TODO: Set up the display name and get the Firebase reference
        setupDisplayName();
        mDataBaseReference = FirebaseDatabase.getInstance().getReference();
        //Reference == particular locaiton in data base

        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // TODO: Send the message when the "enter" button is pressed
         mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 sendMessage();
                 return true;
             }
         })
         ;///entrer fil clavier
         mSendButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                  sendMessage();
             }
         });
//entrer el boutton
        // TODO: Add an OnClickListener to the sendButton to send a message

    }

    // TODO: Retrieve the display name from the Shared Preferences
    private void setupDisplayName(){
        SharedPreferences prefs = getSharedPreferences( RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        mDisplayName= prefs.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
        if (mDisplayName==null)mDisplayName="Anonymous";

    }

    private void sendMessage() {

        // TODO: Grab the text the user typed in and push the message to Firebase
        Log.d("ChatiApp","MSG SENT");
        String input = mInputText.getText().toString();
        if (!input.equals("")){
            InstantMessage chat = new InstantMessage(input, mDisplayName);
            mDataBaseReference.child("messages").push().setValue(chat);
            //chil location where msg is stored
            //heavy lifting database ref object
            //setValue commits the data
            mInputText.setText("");
        }


    }

    // TODO: Override the onStart() lifecycle method. Setup the adapter here.

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDataBaseReference, mDisplayName);
        mChatListView.setAdapter(mAdapter);
        Log.d("ChatiApp","OnStart ");

    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("ChatiApp","OnStop ");
        // TODO: Remove the Firebase event listener on the adapter.
        mAdapter.cleanup();
    }

}
