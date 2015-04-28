package com.dynamic.load.internal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.dynamic.load.PluginManager;
import com.dynamic.load.PluginPackageInfo;

/**
 * Created by Andy Wing on 15-4-22.
 */
public final class Proxy {
    private Activity mActivity;

    private PluginInterface mPluginInterface;
    private PluginPackageInfo mPluginPackageInfo;

    private ApkLayoutInflater mApkLayoutInflater;

    public Proxy(Activity mActivity) {
        this.mActivity = mActivity;
    }

    private boolean ensureInterface() {
        return mPluginInterface != null;
    }

    public boolean onCreate(Bundle savedInstanceState, Intent intent) {
        String targetPackageName = intent.getStringExtra(PluginIntent.EXTRA_PACKAGE);
        String targetClassName = intent.getStringExtra(PluginIntent.EXTRA_CLASS_NAME);

        if (TextUtils.isEmpty(targetPackageName)) {
            targetPackageName = intent.getComponent().getPackageName();
            targetClassName = intent.getComponent().getClassName();
        }
        if (TextUtils.isEmpty(targetClassName)) {
            targetClassName = intent.getComponent().getClassName();
        }

        PluginManager pluginManager = PluginManager.getInstance(mActivity);
        mPluginPackageInfo = pluginManager.getPluginPackageInfo(targetPackageName);
        mPluginInterface = pluginManager.loadPluginInterface(mPluginPackageInfo, targetClassName);

        if (ensureInterface()) {
            mPluginInterface.attachParent(mActivity, mPluginPackageInfo.targetPkgName);

            if (mPluginPackageInfo.themeId > 0) {
                mActivity.setTheme(mPluginPackageInfo.themeId);
                mPluginPackageInfo.targetTheme.setTo(mActivity.getTheme());
                try {
                    mPluginPackageInfo.targetTheme.applyStyle(mPluginPackageInfo.themeId, true);
                } catch (Exception e) {
                    //
                }
            }

            mPluginInterface.onActivityCreated(mActivity, savedInstanceState);
        }
        return ensureInterface();
    }

    public Resources getResources() {
        if (ensureInterface()) {
            return mPluginPackageInfo.targetResource;
        }
        return null;
    }

    public AssetManager getAssets() {
        if (ensureInterface()) {
            return mPluginPackageInfo.targetAssetManager;
        }
        return null;
    }

    public Resources.Theme getTheme() {
        if (ensureInterface()) {
            return mPluginPackageInfo.targetTheme;
        }
        return null;
    }

    public ClassLoader getClassLoader() {
        return mPluginPackageInfo.targetClassLoader;
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if (mApkLayoutInflater == null) {
            mApkLayoutInflater = new ApkLayoutInflater(context);
        }
        return mApkLayoutInflater.onCreateView(name, attrs);
    }

    public void onStart() {
        if (ensureInterface()) {
            mPluginInterface.onActivityStarted(mActivity);
        }
    }

    public void onResume() {
        if (ensureInterface()) {
            mPluginInterface.onActivityResumed(mActivity);
        }
    }

    public void onPause() {
        if (ensureInterface()) {
            mPluginInterface.onActivityPaused(mActivity);
        }
    }

    public void onStop() {
        if (ensureInterface()) {
            mPluginInterface.onActivityStopped(mActivity);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        if (ensureInterface()) {
            mPluginInterface.onActivitySaveInstanceState(mActivity, outState);
        }
    }

    public void onDestroy() {
        if (ensureInterface()) {
            mPluginInterface.onActivityDestroyed(mActivity);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (ensureInterface()) {
            mPluginInterface.onConfigurationChanged(newConfig);
        }
    }

    public void onAttachFragment(Fragment fragment) {
        if (ensureInterface()) {
            mPluginInterface.onAttachFragment(fragment);
        }
    }

    public void onBackPressed() {
        if (ensureInterface()) {
            mPluginInterface.onBackPressed();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return ensureInterface() &&
                mPluginInterface.onTouchEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return ensureInterface() &&
                mPluginInterface.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return ensureInterface() &&
                mPluginInterface.onKeyUp(keyCode, event);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (ensureInterface()) {
            mPluginInterface.onWindowFocusChanged(hasFocus);
        }
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (ensureInterface()) {
            mPluginInterface.onWindowAttributesChanged(params);
        }
    }

    public void onAttachedToWindow() {
        if (ensureInterface()) {
            mPluginInterface.onAttachedToWindow();
        }
    }

    public void onDetachedFromWindow() {
        if (ensureInterface()) {
            mPluginInterface.onDetachedFromWindow();
        }
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return ensureInterface() &&
                mPluginInterface.onMenuItemSelected(featureId, item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return ensureInterface() &&
                mPluginInterface.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return ensureInterface() &&
                mPluginInterface.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return ensureInterface() &&
                mPluginInterface.onOptionsItemSelected(item);
    }

    public void onActionModeStarted(ActionMode mode) {
        if (ensureInterface()) {
            mPluginInterface.onActionModeStarted(mode);
        }
    }

    public void onActionModeFinished(ActionMode mode) {
        if (ensureInterface()) {
            mPluginInterface.onActionModeFinished(mode);
        }
    }

    public void onContentChanged() {
        if (ensureInterface()) {
            mPluginInterface.onContentChanged();
        }
    }

    public void onUserInteraction() {
        if (ensureInterface()) {
            mPluginInterface.onUserInteraction();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return ensureInterface() &&
                mPluginInterface.dispatchKeyEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return ensureInterface() &&
                mPluginInterface.dispatchTouchEvent(ev);
    }

    public void onOptionsMenuClosed(Menu menu) {
        if (ensureInterface()) {
            mPluginInterface.onOptionsMenuClosed(menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        return ensureInterface() &&
                mPluginInterface.onContextItemSelected(item);
    }
}
