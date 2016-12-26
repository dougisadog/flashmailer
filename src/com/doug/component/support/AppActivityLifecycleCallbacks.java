package com.doug.component.support;

import java.util.concurrent.ScheduledFuture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

/**
 * 所有atitivy生命周期管理
 * @author Doug
 * 
 */
@SuppressLint("NewApi")
public class AppActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

    private static final int SCHEDULE_DELAY_MILLS = 3000;

    private static final int FETCH_PERIOD_SECONDS = 30;

    private int visibleActivityCount = 0;

    private Handler uiHandler = new Handler(Looper.getMainLooper());


    private ScheduledFuture<?> scheduledFuture;


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (visibleActivityCount == 0) {
//            startFetchUnread();
        }
        visibleActivityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        visibleActivityCount--;
        if (visibleActivityCount == 0) {
//            stopFetchUnread();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    private void startFetchUnread() {
        uiHandler.postDelayed(scheduleFetchUnreadRunnable, SCHEDULE_DELAY_MILLS);
        uiHandler.removeCallbacks(unScheduleFetchUnreadRunnable);
    }

    private void stopFetchUnread() {
        uiHandler.postDelayed(unScheduleFetchUnreadRunnable, SCHEDULE_DELAY_MILLS);
        uiHandler.removeCallbacks(scheduleFetchUnreadRunnable);
    }

    private Runnable scheduleFetchUnreadRunnable = new Runnable() {
        @Override
        public void run() {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    };

    private Runnable unScheduleFetchUnreadRunnable = new Runnable() {
        @Override
        public void run() {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    };

}


