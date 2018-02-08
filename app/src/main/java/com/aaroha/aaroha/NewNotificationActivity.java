package com.aaroha.aaroha;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.DateFormat;
import java.util.Date;

import classes.Notification;

public class NewNotificationActivity extends AppCompatActivity {

    String subject,body;
    private Firebase rootRef = new Firebase("https://aaroha-50e98.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_new_notification);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView)mToolbar.findViewById(R.id.title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title.setText("Compose");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending...");

        final EditText editTextSubject = (EditText)findViewById(R.id.input_subject);
        final EditText editTextBody = (EditText)findViewById(R.id.input_body);

        final CheckBox blue = (CheckBox)findViewById(R.id.blueTick);
        final CheckBox yellow = (CheckBox)findViewById(R.id.yellowTick);
        final CheckBox red = (CheckBox)findViewById(R.id.redTick);

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellow.setChecked(false);
                red.setChecked(false);
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red.setChecked(false);
                blue.setChecked(false);
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blue.setChecked(false);
                yellow.setChecked(false);
            }
        });

        ImageView sendButton = (ImageView)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject = editTextSubject.getText().toString().trim();
                body = editTextBody.getText().toString().trim();
                if(subject.isEmpty()){
                    Toast.makeText(NewNotificationActivity.this, "Please fill in the subject field", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(body.isEmpty()){
                    Toast.makeText(NewNotificationActivity.this, "Please fill in the body field", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!(yellow.isChecked()||red.isChecked()||blue.isChecked())){
                    Toast.makeText(NewNotificationActivity.this, "Please select priority", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.show();
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Notification notification = new Notification();
                    notification.setBody(body);
                    notification.setHead(subject);
                    notification.setTime(currentDateTimeString.substring(12, currentDateTimeString.length()));
                    notification.setDate(currentDateTimeString.substring(0, 11));
                    if(blue.isChecked())
                        notification.setPriority("blue");
                    if(yellow.isChecked())
                        notification.setPriority("yellow");
                    if(red.isChecked())
                        notification.setPriority("red");

                    Firebase tempref = rootRef.child("Notifications").push();
                    notification.setNotifId(tempref.getKey());

                    tempref.setValue(notification, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if(firebaseError==null){
                                Toast.makeText(NewNotificationActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });

                }
            }
        });

    }
}
