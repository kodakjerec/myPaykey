package com.scb.mb.tw;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.paykey.PayKey;
import org.paykey.PayKeyAnalyticsProxy;
import org.paykey.analytics.AnalyticsEventTracker;
import org.paykey.analytics.Event;
import org.paykey.keyboard.sample.PayKeyApiResultCallStrategy;
import org.paykey.keyboard.sample.PayKeyClient;
import org.paykey.keyboard.sample.PayKeyDelegateImpl;
import org.paykey.keyboard.sample.Settings;
import org.paykey.util.Logger;

/**
 * Created by alexkogan on 13/01/2016.
 */
public class SampleApp extends Application {

    private Settings mSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.setMinLogLevel(Log.VERBOSE);

        final Context context = getApplicationContext();
        mSettings = new Settings(context, new PayKeyApiResultCallStrategy());
        PayKey.init(context, new PayKeyDelegateImpl(context));
        PayKeyClient.init();

        if (addAnalyticsTracker()) {
            PayKey.addAnalyticsTracker(new AnalyticsEventTracker() {
                @Override
                public void onEvent(Event event) {
                    Log.d("UI Tracker", "Type '" + event.getType() + "' => " + event.getData());
                }
            });
        }

        if (addProxyTracker()) {
            // keep this for PayKey Analytics library
            PayKeyAnalyticsProxy.subscribe(new PayKeyAnalyticsProxy(this));
        }
    }

    protected boolean addAnalyticsTracker() {
        return true;
    }

    private boolean addProxyTracker() {
        return false;
    }

    public Settings getSettings() {
        return mSettings;
    }
}
