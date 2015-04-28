package com.dynamic.load;

import java.lang.ref.SoftReference;

/**
 * Created by Andy Wing on 15-4-24.
 *
 * PluginHostCallbackManager for mgr PluginHostCallback
 *
 * TODO need opt this mgr later
 */
public final class PluginHostCallbackManager {

    private static PluginHostCallbackManager sInstance;

    public static PluginHostCallbackManager getInstance() {
        if (sInstance == null) {
            synchronized (PluginHostCallbackManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginHostCallbackManager();
                }
            }
        }
        return sInstance;
    }

    private SoftReference<PluginHostCallback> mHostCallbackCache;

    public void registerPluginHostCallback(PluginHostCallback callback) {
        mHostCallbackCache = new SoftReference<PluginHostCallback>(callback);
    }

    public void unregisterPluginHostCallback() {
        if (mHostCallbackCache != null) {
            mHostCallbackCache.clear();
            mHostCallbackCache = null;
        }
    }

    public PluginHostCallback getHostCallback() {
        return mHostCallbackCache == null ? null : mHostCallbackCache.get();
    }
}
