package com.dynamic.load.internal;

/**
 * Created by Andy Wing on 15-4-28.
 */
public class PluginException extends RuntimeException {

    private static final long serialVersionUID = -6789031775836380075L;

    public PluginException(String detailMessage) {
        super(detailMessage);
    }

    public PluginException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
