package com.mendeley.api;

import android.content.Context;

import com.mendeley.api.impl.DefaultMendeleySdk;

/**
 * Convenience class used to obtain a handle to the proper MendeleySdk.
 */
public class MendeleySdkFactory {
    /**
     * Return the MendeleySdk singleton.
     */
    public static MendeleySdk getInstance() {
        return DefaultMendeleySdk.getInstance();
    }

    /**
     * Return a version of the MendeleySdk singleton providing blocking calls.
     */
    public static BlockingSdk getBlockingInstance() {
        return DefaultMendeleySdk.getInstance();
    }

    /**
     * Return the SDK version name.
     */
    public static String getSdkVersion(Context context) {
        return context.getResources().getString(R.string.version_name);
    }
}
