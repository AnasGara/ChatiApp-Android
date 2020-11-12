package com.anas.chatiApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private FirebaseAuth myAuth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth
    myAuth = FirebaseAuth.getInstance();

    }

    // Executed when Sign in button pressed
    // bel XM fama porprioté esemha on lcik
    //android:onClick="signInExistingUser"
    public void signInExistingUser(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.anas.chatiApp.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {


        // TODO: Use FirebaseAuth to sign in with email & password
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.equals("")||password.equals(""))
        {
            return;
        }
        Toast.makeText(this, "Logging in is in progress", Toast.LENGTH_SHORT).show();

        myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("ChatiApp","Sign in: OnComplete:" + task.isSuccessful());
                if(!task.isSuccessful())
                {
                    Log.d("ChatiApp","Error Signing in: "+task.getException());

                    showErrorDialog("There was a singing in problem");
                }else{
                    Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });



    }

    // TODO: Show error on screen with an alert dialog

private void showErrorDialog(String msg){
    new AlertDialog.Builder(this)
            .setTitle("Oops").setMessage(msg).setPositiveButton(android.R.string.ok, null)
            .setIcon(android.R.drawable.ic_dialog_alert).show();
}

}