package com.winlator.cmod.psp;
import android.net.Uri;
public final class PspGame {
    public final String title;
    public final Uri uri;
    public PspGame(String title, Uri uri) { this.title=title; this.uri=uri; }
    public static boolean supports(String name) {
        String n=name.toLowerCase();
        return n.endsWith(".iso") || n.endsWith(".cso") || n.endsWith(".pbp") || n.endsWith(".chd");
    }
}
