package com.venmo.api;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;
import com.squareup.picasso.UrlConnectionDownloader;
import java.io.IOException;

public class Pablo {

    private Pablo() {}

    public static boolean ENABLE_DEBUGGER = false;
    private static final String TAG = Pablo.class.getSimpleName();
    private static Picasso CUSTOM_PABLO;

    public static synchronized Picasso with(Context context) {
        if (CUSTOM_PABLO == null) {
            CUSTOM_PABLO = new Picasso.Builder(context)
                    .downloader(new UrlConnectionDownloader(context) {
                        @Override
                        public Response load(Uri uri, boolean localCacheOnly)
                                throws IOException {
                            long start = SystemClock.currentThreadTimeMillis();
                            Response superResult = super.load(uri, localCacheOnly);
                            return superResult;
                        }
                    }).build();
        }
        return CUSTOM_PABLO;
    }

    private static String uriToString(Uri uri) {
        return uri.getScheme() + ":" + uri.getSchemeSpecificPart();
    }
}
