package com.dynamic.load;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.dynamic.load.internal.ApkLayoutInflater;
import com.dynamic.load.internal.PluginException;
import com.dynamic.load.internal.PluginIntent;
import com.dynamic.load.internal.PluginInterface;
import com.dynamic.load.util.LogUtil;
import com.dynamic.load.util.Md5Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by Andy Wing on 15-4-24.
 *
 * PluginManager for load and start target apk
 */
public abstract class PluginManager {

    private static PluginManager sInstance;

    public static PluginManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManagerImpl(context);
                }
            }
        }
        return sInstance;
    }

    public abstract void checkAllApksMd5();

    /**
     * @param packageName
     * @return
     *          return apk info with pkg name
     *          (if loaded apk, it will check apk content diff, and try reload it)
     */
    public abstract PluginPackageInfo getPluginPackageInfo(String packageName);

    public String loadAPkTest(){
        String apkPath = Environment.getExternalStorageDirectory() + "/sample_child-debug.apk";
        return loadAPk(apkPath);
    }

    /**
     * @param apkPath
     *          apk file path
     * @return
     *        apk package name or null(failed)
     */
    public abstract String loadAPk(String apkPath);

    /**
     * @param packageInfo
     *          packageInfo load by mgr
     * @param className
     *          activity in target apk file
     * @return
     *          instance is target apk activity
     */
    public abstract PluginInterface loadPluginInterface(PluginPackageInfo packageInfo, String className);

    /**
     * start activity in parent apk activity
     * @param context
     * @param pkg
     *          loaded target apk pkg name
     * @param className
     *          loaded target apk activity name which u need start
     */
    public abstract void startActivity(Activity context, String pkg, String className);

    /**
     * same as {@linkplain #startActivity(Activity, String, String)}<br/>
     * add requestCode only
     *
     * @param context
     * @param pkg
     * @param className
     * @param requestCode
     */
    public abstract void startActivityForResult(Activity context,
                                                String pkg,
                                                String className,
                                                int requestCode);

    /**
     * same as {@linkplain #startActivity(Activity, String, String)}<br/>
     * but it will be invoked in fragment
     *
     * @param context
     * @param pkg
     * @param className
     * @param fragment
     * @param requestCode
     */
    public abstract void startActivityFromFragment(Activity context,
                                          String pkg,
                                          String className,
                                          Fragment fragment,
                                          int requestCode);


    /////////////////////internal api///////////////////////////////////////
    private static final class PluginManagerImpl extends PluginManager {

        private static final Map<String, PluginPackageInfo> PLUGIN_CACHE
                = new HashMap<String, PluginPackageInfo>();

        private static final PluginPackageInfo EMPTY_PKG_INFO = new PluginPackageInfo();

        private Context mAppContext;

        protected PluginManagerImpl(Context context) {
            mAppContext = context;
        }

        public void checkAllApksMd5() {
            List<String> needReloadApkList = new ArrayList<String>(1);
            Iterator<PluginPackageInfo> iterator = PLUGIN_CACHE.values().iterator();
            while (iterator.hasNext()) {
                PluginPackageInfo packageInfo = iterator.next();
                String md5 = Md5Util.md5To32(new File(packageInfo.apkPath));
                if (md5 == null || !isApkMd5Same(packageInfo.targetPkgName, md5)) {
                    LogUtil.d("___Line:[61]___checkExitsApkMd5___apk md5 changed");
                    needReloadApkList.add(packageInfo.apkPath);
                    iterator.remove();
                }
            }

            for (String apkPath : needReloadApkList) {
                LogUtil.d("___Line:[68]___checkExitsApkMd5___reload apk when md5 changed");
                loadAPk(apkPath);
            }
        }

        private boolean checkAndReloadSingleApkMd5(PluginPackageInfo packageInfo) {
            return packageInfo != null &&
                    checkAndReloadSingleApkMd5(packageInfo.apkPath,
                            packageInfo.targetPkgName);
        }

        private boolean checkAndReloadSingleApkMd5(String apkPath, String pkg) {
            String md5 = Md5Util.md5To32(new File(apkPath));
            if (md5 == null || !isApkMd5Same(pkg, md5)) {
                LogUtil.d("___Line:[68]___apk modified reload apk ");
                PLUGIN_CACHE.remove(pkg);
                ApkLayoutInflater.removeCachedConstructor(pkg);
                return loadAPk(apkPath) != null;
            }
            return false;
        }

        public PluginPackageInfo getPluginPackageInfo(String packageName) {
            PluginPackageInfo packageInfo = PLUGIN_CACHE.get(packageName);
            if(checkAndReloadSingleApkMd5(packageInfo)) {
                packageInfo = PLUGIN_CACHE.get(packageName);
            }
            return packageInfo == null ? EMPTY_PKG_INFO : packageInfo;
        }

        /**
         * @param apkPath
         * @return
         *        apk package name or null(failed)
         */
        public String loadAPk(String apkPath) {
            PackageInfo packageArchiveInfo = mAppContext.getPackageManager().getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            if (packageArchiveInfo == null) return null;

            String packageNameForPlugin = packageArchiveInfo.packageName;
            if (PLUGIN_CACHE.containsKey(packageNameForPlugin)) {
                return packageNameForPlugin;
            }

            try {
                PluginPackageInfo packageInfo = new PluginPackageInfo();


                DexClassLoader loader = createDexClassLoader(apkPath);
                AssetManager assetManagerForPlugin /*= new AssetManager() //hide api*/;
                /*assetManagerForPlugin.addAssetPath(apkPath); //hide api*/

                assetManagerForPlugin = AssetManager.class.getDeclaredConstructor().newInstance();
                assetManagerForPlugin
                        .getClass()
                        .getDeclaredMethod("addAssetPath", String.class)
                        .invoke(assetManagerForPlugin, apkPath)
                ;

                Resources appResource = mAppContext.getResources();
                Resources resourceForPlugin = new Resources(assetManagerForPlugin,
                        appResource.getDisplayMetrics(),
                        appResource.getConfiguration());

                Resources.Theme theme = mAppContext.getTheme();
                Resources.Theme themeForPlugin = resourceForPlugin.newTheme();
                themeForPlugin.setTo(theme);

                packageInfo.apkPath = apkPath;
                packageInfo.targetClassLoader = loader;
                packageInfo.targetAssetManager = assetManagerForPlugin;
                packageInfo.targetResource = resourceForPlugin;
                packageInfo.targetTheme = themeForPlugin;
                packageInfo.targetPkgName = packageNameForPlugin;
                packageInfo.targetPackageInfo = packageArchiveInfo;

                savePluginApkMd5(packageNameForPlugin, new File(apkPath));

                PLUGIN_CACHE.put(packageNameForPlugin, packageInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return packageNameForPlugin;
        }

        private DexClassLoader createDexClassLoader(String dexPath) {
            File dexOutputDir = mAppContext.getDir("dex", Context.MODE_PRIVATE);
            String dexOutputPath = dexOutputDir.getAbsolutePath();
            return new DexClassLoader(dexPath, dexOutputPath, null, mAppContext.getClassLoader());
        }

        private int fixTheme(PluginPackageInfo pluginPackageInfo, String className) {
            int theme = android.R.style.Theme;
            PackageInfo packageInfo = pluginPackageInfo.targetPackageInfo;
            if (packageInfo.activities != null && packageInfo.activities.length > 0) {
                if (className == null) {
                    className = packageInfo.activities[0].name;
                }

                int defaultTheme = packageInfo.applicationInfo.theme;
                for (ActivityInfo a : packageInfo.activities) {
                    if (TextUtils.equals(a.name, className)) {
                        if (a.theme == 0) {
                            if (defaultTheme != 0) {
                                theme = defaultTheme;
                            } else {
                                if (Build.VERSION.SDK_INT >= 14) {
                                    theme = android.R.style.Theme_DeviceDefault;
                                } else {
                                    theme = android.R.style.Theme;
                                }
                            }
                        } else {
                            theme = a.theme;
                        }
                    }
                }
            }
            return theme;
        }

        public PluginInterface loadPluginInterface(PluginPackageInfo packageInfo, String className) {
            if(packageInfo == null) return null;

            Class<?> clazz = loadClass(packageInfo.targetClassLoader, className);

            if(clazz == null) {
                throw new PluginException("Activity can not find in target apk with name " +
                        className);
            }

            if(!PluginInterface.class.isAssignableFrom(clazz)) {
                throw new PluginException("Activity class "+ className +
                        " not extends with PluginActivity in Plugin Apk!");
            }

            PluginInterface pi = null;
            try {
                pi = (PluginInterface) clazz.newInstance();
                if(packageInfo.themeId <= 0){
                    packageInfo.themeId = fixTheme(packageInfo, className);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return pi;
        }

        public void startActivity(Activity context, String pkg, String className) {
            startActivityForResult(context, pkg, className, -1);
        }

        public void startActivityForResult(Activity context, String pkg, String className, int requestCode) {
            PluginPackageInfo info = getPluginPackageInfo(pkg);
            PluginIntent intent = new PluginIntent(context,
                    info.targetPkgName,
                    className);
            context.startActivityForResult(intent, requestCode);
        }

        public void startActivityFromFragment(Activity context,
                                              String pkg,
                                              String className,
                                              Fragment fragment,
                                              int requestCode) {
            PluginPackageInfo info = getPluginPackageInfo(pkg);
            PluginIntent intent = new PluginIntent(context,
                    info.targetPkgName,
                    className);
            context.startActivityFromFragment(fragment, intent, requestCode);
        }

        private Class<?> loadClass(ClassLoader loader, String className) {
            try {
                return Class.forName(className, true, loader);
            } catch (ClassNotFoundException e) {
                //
                e.printStackTrace();
            }
            return null;
        }

        private static final String PLUGIN_APK_MD5 = "plugin_apk_md5_";
        private static final String PLUGIN_APK_MD5_UN_KNOW = "-1";
        private void savePluginApkMd5(String pkg, File apkPath) {
            String md5 = Md5Util.md5To32(apkPath);
            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(mAppContext)
                    .edit();
            editor.putString(PLUGIN_APK_MD5 + pkg, md5)
                    .apply();
        }

        private String getPluginApkMd5(String pkg) {
            return PreferenceManager
                    .getDefaultSharedPreferences(mAppContext)
                    .getString(PLUGIN_APK_MD5 + pkg, PLUGIN_APK_MD5_UN_KNOW);
        }

        private boolean isApkMd5Same(String pkg, String checkedMd5) {
            String saveMd5 = getPluginApkMd5(pkg);
            return TextUtils.equals(checkedMd5, saveMd5);
        }
    }
}
