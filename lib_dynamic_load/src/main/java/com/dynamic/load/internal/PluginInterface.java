package com.dynamic.load.internal;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public interface PluginInterface extends Application.ActivityLifecycleCallbacks {

    void attachParent(Activity activity, String packageName);


    void onConfigurationChanged(Configuration newConfig);

    void onAttachFragment(Fragment fragment);

    void onBackPressed();

    boolean onTouchEvent(MotionEvent event);

    boolean onKeyDown(int keyCode, KeyEvent event);

    boolean onKeyUp(int keyCode, KeyEvent event);

    void onWindowFocusChanged(boolean hasFocus);

    void onWindowAttributesChanged(WindowManager.LayoutParams params);

    void onAttachedToWindow();

    void onDetachedFromWindow();

    boolean onMenuItemSelected(int featureId, MenuItem item);

    boolean onCreateOptionsMenu(Menu menu);

    boolean onPrepareOptionsMenu(Menu menu);

    boolean onOptionsItemSelected(MenuItem item);

    void onActionModeStarted(ActionMode mode);

    void onActionModeFinished(ActionMode mode);

    void onContentChanged();

    void onUserInteraction();

    boolean dispatchKeyEvent(KeyEvent event);

    boolean dispatchTouchEvent(MotionEvent ev);

    void onOptionsMenuClosed(Menu menu);

    boolean onContextItemSelected(MenuItem item);
}
