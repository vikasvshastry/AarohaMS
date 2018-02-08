package com.aaroha.aaroha;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            //Start login activity
            finish();
            startActivity(new Intent(this, LogBeforeMainActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        CardView notificationsCard = (CardView)findViewById(R.id.notifications_card);
        CardView shruthiCard = (CardView)findViewById(R.id.shruthi_card);
        CardView notesCard = (CardView)findViewById(R.id.notes_card);

        final Bundle bundle = new Bundle();

        notificationsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FrameActivity.class);
                intent.putExtra("id","notifications");
                MainActivity.this.startActivity(intent);
            }
        });

        shruthiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FrameActivity.class);
                intent.putExtra("id","shruthi");
                MainActivity.this.startActivity(intent);
            }
        });

        notesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FrameActivity.class);
                intent.putExtra("id","notes");
                MainActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_settings) {
            Fragment fragment = new SettingsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment , "settings");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            updateTitleAndDrawer(fragment);
            return true;
        }*/

        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LogBeforeMainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}