package com.dynamic.load.internal;

import android.content.Context;
import android.content.Intent;

import com.dynamic.load.ProxyActivity;

public final class PluginIntent extends Intent {

    public static final String EXTRA_PACKAGE = "extra_package";
    public static final String EXTRA_CLASS_NAME = "extra_class_name";

    public PluginIntent(Context context, String pkg, String className) {
        super();
        putExtra(EXTRA_PACKAGE, pkg);
        putExtra(EXTRA_CLASS_NAME, className.startsWith(".") ? pkg + className : className);
        setClass(context, ProxyActivity.class);
    }
}
