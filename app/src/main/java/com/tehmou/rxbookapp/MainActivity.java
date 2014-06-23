package com.tehmou.rxbookapp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tehmou.rxbookapp.utils.TwitterClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import rx.Subscriber;
import twitter4j.Status;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BookFragment())
                    .commit();
        }

        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open("twitter.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            TwitterClient twitterClient = new TwitterClient(properties);
            twitterClient.getTweets()
                    .subscribe(new Subscriber<List<Status>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Error gettings tweets", e);
                        }

                        @Override
                        public void onNext(List<Status> statuses) {
                            Log.d(TAG, "Found " + statuses.size() + " tweets");
                        }
                    });
        } catch (IOException e) {
            Log.e(TAG, "Unable to read assets/twitter.properties", e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
