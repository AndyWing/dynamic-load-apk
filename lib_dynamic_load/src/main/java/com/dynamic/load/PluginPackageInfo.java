package com.dynamic.load;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by Andy Wing on 15-4-22.
 *
 * PluginPackageInfo for user get some info in target apk.
 */
public class PluginPackageInfo {
    public int themeId;

    public String apkPath;

    public PackageInfo targetPackageInfo;

    public Resources targetResource;
    public AssetManager targetAssetManager;
    public Resources.Theme targetTheme;
    public String targetPkgName;
    public ClassLoader targetClassLoader;
}
