package com.winlator.cmod.psp;

import android.content.Context;
import android.content.SharedPreferences;
import com.winlator.cmod.contents.AdrenotoolsManager;

/**
 * Single source of truth for native Android emulator GPU selection.
 * PC and PSP frontends store/use the same AdrenoTools driver id.
 */
public final class SharedGpuDriver {
    private static final String PREFS = "shared_gpu";
    private static final String KEY = "driver_id";
    private SharedGpuDriver() {}

    public static void select(Context context, String driverId) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, driverId).apply();
    }

    public static String selected(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "System");
    }

    public static DriverInfo resolve(Context context) {
        String id = selected(context);
        if ("System".equals(id)) return new DriverInfo(id, "", "System Vulkan");
        AdrenotoolsManager mgr = new AdrenotoolsManager(context);
        return new DriverInfo(id, mgr.getDriverPath(id), mgr.getLibraryName(id));
    }

    public static final class DriverInfo {
        public final String id, path, library;
        DriverInfo(String id, String path, String library) {
            this.id=id; this.path=path; this.library=library;
        }
    }
}
