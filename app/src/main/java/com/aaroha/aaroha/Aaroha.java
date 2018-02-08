package com.aaroha.aaroha;

import com.firebase.client.Firebase;

/**
 * Created by vikas on 11-Jan-18.
 */

public class Aaroha extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}