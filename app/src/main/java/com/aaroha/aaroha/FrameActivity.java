package com.aaroha.aaroha;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class FrameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        String value = intent.getStringExtra("id");

        TextView tv = (TextView)mToolbar.findViewById(R.id.title);

        switch(value){
            case "notifications":
                tv.setText("Notifications");
                break;
            case "shruthi":
                tv.setText("Shruthi");
                break;
            case "notes":
                tv.setText("Notes");
                break;
        }

    }

}