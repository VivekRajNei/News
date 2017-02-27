package es.esy.vivekrajendran.news.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class NetworkChecker {

    private Context context;

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) return false;
        NetworkInfo.State networkState = networkInfo.getState();
        return (networkState == NetworkInfo.State.CONNECTED || networkState == NetworkInfo.State.CONNECTING);
    }

    private NetworkChecker(Context context) {
        this.context = context;
    }

    public static NetworkChecker getInstance(Context context) {
        return new NetworkChecker(context);
    }
}
