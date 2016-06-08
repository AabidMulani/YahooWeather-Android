package in.abmulani.yahooweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Check Network connectivity
 */
public class NetworkUtility {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        NetworkInfo.State network = networkInfo.getState();
        return network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING;
    }

}
