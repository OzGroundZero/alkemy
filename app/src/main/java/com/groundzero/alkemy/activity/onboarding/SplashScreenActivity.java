package com.groundzero.alkemy.activity.onboarding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.groundzero.alkemy.R;
import com.groundzero.alkemy.services.myo.MyoService;

/**
 * Invisible Activity as the launcher activity as to not display when Seen broadcasts data
 */
public class SplashScreenActivity extends Activity {
    private static final String TAG = "SplashScreenActivity";
    Context mActivityContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mActivityContext = this;
        //launch the Lab activity here. Our real main activity
        Intent runService = new Intent(mActivityContext,MyoService.class);
        startService(runService);
        Log.v("Splash_MyoService", "Opened Alkemy!!! Finishing Splash");
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
