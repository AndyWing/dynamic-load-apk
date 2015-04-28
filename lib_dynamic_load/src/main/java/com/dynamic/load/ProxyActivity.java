package com.dynamic.load;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.dynamic.load.internal.Proxy;

/**
 * Created by Andy Wing on 15-4-22.
 *
 * ProxyActivity for wrapper target apk activity.<br/>
 * normally do not modify it
 *
 */
public final class ProxyActivity extends Activity {

    private Proxy mProxy = new Proxy(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxy.onCreate(savedInstanceState, getIntent());
    }

    @Override
    public Resources getResources() {
        return mProxy.getResources() == null ? super.getResources() : mProxy.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mProxy.getAssets() == null ? super.getAssets() : mProxy.getAssets();
    }

    @Override
    public Resources.Theme getTheme() {
        return mProxy.getTheme() == null ? super.getTheme() : mProxy.getTheme();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mProxy.getClassLoader();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = mProxy.onCreateView(name, context, attrs);
        if (view == null) {
            view = super.onCreateView(name, context, attrs);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mProxy.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mProxy.onResume();
    }

    @Override
    public void onPause() {
        mProxy.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mProxy.onStop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mProxy.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mProxy.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mProxy.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        mProxy.onAttachFragment(fragment);
        super.onAttachFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        mProxy.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mProxy.onTouchEvent(event) ||
                super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mProxy.onKeyDown(keyCode, event) ||
                super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mProxy.onKeyUp(keyCode, event) ||
                super.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mProxy.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        mProxy.onWindowAttributesChanged(params);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mProxy.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        mProxy.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return mProxy.onMenuItemSelected(featureId, item) ||
                super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mProxy.onCreateOptionsMenu(menu) ||
                super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return mProxy.onPrepareOptionsMenu(menu) ||
                super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mProxy.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        mProxy.onActionModeStarted(mode);
        super.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        mProxy.onActionModeFinished(mode);
        super.onActionModeFinished(mode);
    }

    @Override
    public void onContentChanged() {
        mProxy.onContentChanged();
        super.onContentChanged();
    }

    @Override
    public void onUserInteraction() {
        mProxy.onUserInteraction();
        super.onUserInteraction();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mProxy.dispatchKeyEvent(event) ||
                super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mProxy.dispatchTouchEvent(ev) ||
                super.dispatchTouchEvent(ev);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        mProxy.onOptionsMenuClosed(menu);
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return mProxy.onContextItemSelected(item) ||
                super.onContextItemSelected(item);
    }
}
