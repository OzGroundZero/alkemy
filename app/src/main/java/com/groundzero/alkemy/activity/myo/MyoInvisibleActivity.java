package com.groundzero.alkemy.activity.myo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.groundzero.alkemy.R;

import java.text.StringCharacterIterator;

public class MyoInvisibleActivity extends Activity {

    private Intent mOriginIntent;
    private Bundle mOriginIntentExtras;
    private Context mContext;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_service_invisible);


        mOriginIntent = getIntent();
        if (mOriginIntent != null) {
            mOriginIntentExtras = mOriginIntent.getExtras();
            if (mOriginIntentExtras != null) {

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_service_invisible, menu);
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

    public static class ACTION {
        public static final String SEEN_FOR_RESULT = "SEEN_FOR_RESULT";
    }
}
