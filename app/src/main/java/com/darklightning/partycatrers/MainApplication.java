package com.darklightning.partycatrers;

import android.app.Application;
import android.util.Log;

import com.darklightning.partycatrers.Constants.MyConstants;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * Created by rikki on 6/25/17.
 */

public class MainApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(MyConstants.TWITTER_CONSUMER_KEY, MyConstants.TWITTER_CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }
}
