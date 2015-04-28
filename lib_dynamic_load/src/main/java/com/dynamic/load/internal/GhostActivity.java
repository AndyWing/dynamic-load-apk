package com.dynamic.load.internal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.dynamic.load.PluginHostCallback;
import com.dynamic.load.PluginHostCallbackManager;
import com.dynamic.load.PluginManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Andy Wing on 15-4-22.
 */
public abstract class GhostActivity extends Activity implements PluginInterface {

    protected boolean mInPluginApk;
    protected Activity that;
    protected PluginHostCallback mPluginHostCallback;
    protected String mPackageName;
    protected PluginManager mPluginManager;

    private boolean mCalledAttach;

    public GhostActivity() {
    }

    @Override
    public void attachParent(Activity activity, String packageName) {
        mCalledAttach = true;
        mInPluginApk = true;
        that = activity;
        mPackageName = packageName;
        mPluginHostCallback = PluginHostCallbackManager.getInstance().getHostCallback();
        mPluginManager = PluginManager.getInstance(activity);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        onCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!mCalledAttach) {
            attachParent(this, getPackageName());
        }

        if (that == null || that == this) {
            mInPluginApk = false;
            that = this;
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (!mInPluginApk) {
            super.setContentView(layoutResID);
        } else {
            that.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (!mInPluginApk) {
            super.setContentView(view);
        } else {
            that.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (!mInPluginApk) {
            super.setContentView(view, params);
        } else {
            that.setContentView(view, params);
        }
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (!mInPluginApk) {
            super.addContentView(view, params);
        } else {
            that.addContentView(view, params);
        }
    }

    @Override
    public void setTheme(int resid) {
        if (!mInPluginApk) {
            super.setTheme(resid);
        } else {
            that.setTheme(resid);
        }
    }

    @Override
    public View findViewById(int id) {
        if (!mInPluginApk) {
            return super.findViewById(id);
        }
        return that.findViewById(id);
    }

    @Override
    public Window getWindow() {
        if (!mInPluginApk) {
            return super.getWindow();
        }
        return that.getWindow();
    }

    @Override
    public WindowManager getWindowManager() {
        if (!mInPluginApk) {
            return super.getWindowManager();
        }
        return that.getWindowManager();
    }

    @Override
    public ActionBar getActionBar() {
        if (!mInPluginApk) {
            return super.getActionBar();
        }
        return that.getActionBar();
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if (!mInPluginApk) {
            return super.getLayoutInflater();
        }
        return that.getLayoutInflater();
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (!mInPluginApk) {
            return super.getMenuInflater();
        }
        return that.getMenuInflater();
    }

    @Override
    public void startActivity(Intent intent) {
        if (!mInPluginApk) {
            super.startActivity(intent);
        } else {
            that.startActivity(intent);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (!mInPluginApk) {
            super.startActivityForResult(intent, requestCode);
        } else {
            that.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        if (!mInPluginApk) {
            super.startActivityFromFragment(fragment, intent, requestCode);
        } else {
            that.startActivityFromFragment(fragment, intent, requestCode);
        }
    }

    public void startGhostActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        if (mInPluginApk) {
            ComponentName componentName = intent.getComponent();
            mPluginManager.startActivityFromFragment(that,
                    componentName.getPackageName(),
                    componentName.getClassName(),
                    fragment,
                    requestCode);
        }
    }

    public void startGhostActivity(Intent intent) {
        startGhostActivityForResult(intent, -1);
    }

    public void startGhostActivityForResult(Intent intent, int requestCode) {
        if (mInPluginApk) {
            ComponentName componentName = intent.getComponent();
            mPluginManager.startActivityForResult(that,
                    componentName.getPackageName(),
                    componentName.getClassName(),
                    requestCode);
        }
    }

    @Override
     public ActionMode startActionMode(ActionMode.Callback callback) {
        if (!mInPluginApk) {
            return super.startActionMode(callback);
        } else {
            return that.startActionMode(callback);
        }
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        if (!mInPluginApk) {
            return super.openOrCreateDatabase(name, mode, factory);
        } else {
            return that.openOrCreateDatabase(name, mode, factory);
        }
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        if (!mInPluginApk) {
            return super.openOrCreateDatabase(name, mode, factory, errorHandler);
        } else {
            return that.openOrCreateDatabase(name, mode, factory, errorHandler);
        }
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        if (!mInPluginApk) {
            return super.openFileInput(name);
        } else {
            return that.openFileInput(name);
        }
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        if (!mInPluginApk) {
            return super.openFileOutput(name, mode);
        } else {
            return that.openFileOutput(name, mode);
        }
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        if (!mInPluginApk) {
            super.overridePendingTransition(enterAnim, exitAnim);
        } else {
            that.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    @Override
    public void finish() {
        if (!mInPluginApk) {
            super.finish();
        } else {
            that.finish();
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (!mInPluginApk) {
            return super.getSystemService(name);
        }
        return that.getSystemService(name);
    }

    @Override
    public Resources getResources() {
        if (!mInPluginApk) {
            return super.getResources();
        }
        return that.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (!mInPluginApk) {
            return super.getAssets();
        }
        return that.getAssets();
    }

    @Override
    public Context getApplicationContext() {
        if (!mInPluginApk) {
            return super.getApplicationContext();
        }
        return that.getApplicationContext();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (!mInPluginApk) {
            return super.getClassLoader();
        }
        return that.getClassLoader();
    }

    @Override
    public String getPackageName() {
        if (!mInPluginApk) {
            return super.getPackageName();
        }
        return mPackageName;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        if (!mInPluginApk) {
            return super.getSharedPreferences(name, mode);
        }
        return that.getSharedPreferences(name, mode);
    }

    @Override
    public FragmentManager getFragmentManager() {
        if (!mInPluginApk) {
            return super.getFragmentManager();
        } else {
            return that.getFragmentManager();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (!mInPluginApk) {
            super.onCreateContextMenu(menu, v, menuInfo);
        } else {
            that.onCreateContextMenu(menu, v, menuInfo);
        }
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if (!mInPluginApk) {
            super.sendBroadcast(intent);
        } else {
            that.sendBroadcast(intent);
        }
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (!mInPluginApk) {
            return super.registerReceiver(receiver, filter);
        }
        return that.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        if (!mInPluginApk) {
            super.unregisterReceiver(receiver);
        } else {
            that.unregisterReceiver(receiver);
        }
    }

    @Override
    public LoaderManager getLoaderManager() {
        if (!mInPluginApk) {
            return super.getLoaderManager();
        } else {
            return that.getLoaderManager();
        }
    }

    @Override
    public View getCurrentFocus() {
        if (!mInPluginApk) {
            return super.getCurrentFocus();
        } else {
            return that.getCurrentFocus();
        }
    }

    @Override
    public boolean hasWindowFocus() {
        if (!mInPluginApk) {
            return super.hasWindowFocus();
        } else {
            return that.hasWindowFocus();
        }
    }

    @Override
    public void invalidateOptionsMenu() {
        if (!mInPluginApk) {
            super.invalidateOptionsMenu();
        } else {
            that.invalidateOptionsMenu();
        }
    }

    @Override
    public String getCallingPackage() {
        if (!mInPluginApk) {
            return super.getCallingPackage();
        } else {
            return that.getCallingPackage();
        }
    }

    @Override
    public boolean isFinishing() {
        if (!mInPluginApk) {
            return super.isFinishing();
        } else {
            return that.isFinishing();
        }
    }

    @Override
    public void recreate() {
        if (!mInPluginApk) {
            super.recreate();
        } else {
            that.recreate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mInPluginApk) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (!mInPluginApk) {
            super.setRequestedOrientation(requestedOrientation);
        } else {
            that.setRequestedOrientation(requestedOrientation);
        }
    }

    @Override
    public int getRequestedOrientation() {
        if (!mInPluginApk) {
            return super.getRequestedOrientation();
        } else {
            return that.getRequestedOrientation();
        }
    }

    @Override
    public boolean moveTaskToBack(boolean nonRoot) {
        if (!mInPluginApk) {
            return super.moveTaskToBack(nonRoot);
        } else {
            return that.moveTaskToBack(nonRoot);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!mInPluginApk) {
            super.setTitle(title);
        } else {
            that.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (!mInPluginApk) {
            super.setTitle(titleId);
        } else {
            that.setTitle(titleId);
        }
    }

    @Override
    public void setTitleColor(int textColor) {
        if (!mInPluginApk) {
            super.setTitleColor(textColor);
        } else {
            that.setTitleColor(textColor);
        }
    }

    @Override
    public void sendBroadcast(Intent intent, String receiverPermission) {
        if (!mInPluginApk) {
            super.sendBroadcast(intent, receiverPermission);
        } else {
            that.sendBroadcast(intent, receiverPermission);
        }
    }

    @Override
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        if (!mInPluginApk) {
            super.sendOrderedBroadcast(intent, receiverPermission);
        } else {
            that.sendOrderedBroadcast(intent, receiverPermission);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if (!mInPluginApk) {
            return super.startService(service);
        } else {
            return that.startService(service);
        }
    }

    @Override
    public boolean stopService(Intent name) {
        if (!mInPluginApk) {
            return super.stopService(name);
        } else {
            return that.stopService(name);
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if (!mInPluginApk) {
            return super.bindService(service, conn, flags);
        } else {
            return that.bindService(service, conn, flags);
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        if (!mInPluginApk) {
            super.unbindService(conn);
        } else {
            that.unbindService(conn);
        }
    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        if (!mInPluginApk) {
            return super.createPackageContext(packageName, flags);
        } else {
            return that.createPackageContext(packageName, flags);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return !mInPluginApk &&
                super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !mInPluginApk &&
                super.dispatchTouchEvent(ev);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (!mInPluginApk) {
            super.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (!mInPluginApk) {
            super.onAttachFragment(fragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mInPluginApk) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !mInPluginApk &&
                super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return !mInPluginApk &&
                super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return !mInPluginApk &&
                super.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!mInPluginApk) {
            super.onWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (!mInPluginApk) {
            super.onWindowAttributesChanged(params);
        }
    }

    @Override
    public void onAttachedToWindow() {
        if (!mInPluginApk) {
            super.onAttachedToWindow();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        if (!mInPluginApk) {
            super.onDetachedFromWindow();
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return !mInPluginApk && super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return !mInPluginApk && super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return !mInPluginApk && super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return !mInPluginApk && super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (!mInPluginApk) {
            super.onActionModeStarted(mode);
        }
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        if (!mInPluginApk) {
            super.onActionModeFinished(mode);
        }
    }

    @Override
    public void onContentChanged() {
        if (!mInPluginApk) {
            super.onContentChanged();
        }
    }

    @Override
    public void onUserInteraction() {
        if (!mInPluginApk) {
            super.onUserInteraction();
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        if (!mInPluginApk) {
            super.onOptionsMenuClosed(menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return !mInPluginApk && super.onContextItemSelected(item);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        onStart();
    }

    @Override
    protected void onStart() {
        if (!mInPluginApk) {
            super.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        onResume();
    }

    @Override
    protected void onResume() {
        if (!mInPluginApk) {
            super.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        onPause();
    }

    @Override
    protected void onPause() {
        if (!mInPluginApk) {
            super.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        onStop();
    }

    @Override
    protected void onStop() {
        if (!mInPluginApk) {
            super.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        onSaveInstanceState(outState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!mInPluginApk) {
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        if (!mInPluginApk) {
            super.onDestroy();
        }
    }
}
