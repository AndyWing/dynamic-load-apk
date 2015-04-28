package com.dynamic.load.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy Wing on 15-4-23.
 */
public final class ApkLayoutInflater {
    private static Set<String> sClassPrefixSet = new HashSet<String>(1);

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<String, Constructor<? extends View>>();

    private Context mContext;

    public ApkLayoutInflater(Context context) {
        mContext = context;
    }

    public static void removeCachedConstructor(String pkg) {
        sConstructorMap.clear();
        sClassPrefixSet.add(pkg);
    }

    public View onCreateView(String name, AttributeSet attrs) {
        if(sClassPrefixSet.isEmpty()) return null;

        if(name.startsWith("java.") ||
                name.startsWith("javax.") ||
                name.startsWith("android.") ||
                name.startsWith("android.support.")) {
            return null;
        }

        boolean contains = false;

        for (String pkg : sClassPrefixSet) {
            if (name.startsWith(pkg)) {
                contains = true;
                break;
            }
        }

        if (!contains) return null;

        Constructor<? extends View> constructor = sConstructorMap.get(name);
        Class<? extends View> clazz;

        try {
            if (constructor == null) {
                clazz = mContext.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mContext, attrs);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
