package in.abmulani.yahooweather.webAsyncs;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.abmulani.yahooweather.interfaces.OnLocationNameReader;
import timber.log.Timber;

/**
 * Created by aabid on 6/8/16.
 */
public class GeocoderAsync extends AsyncTask<String, String, String> {
    private Activity activity;
    private double latitude;
    private double longitude;
    private OnLocationNameReader onLocationNameReader;

    public GeocoderAsync(Activity activity, double latitude, double longitude, OnLocationNameReader onLocationNameReader) {
        this.activity = activity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.onLocationNameReader = onLocationNameReader;
    }

    @Override
    protected String doInBackground(String... params) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            Timber.e("Addresses", "-->" + addresses);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                if (address.getLocality() != null)
                    return address.getLocality();
                if (address.getSubLocality() != null)
                    return address.getSubLocality();
                if (address.getCountryName() != null)
                    return address.getCountryName();
            }
            return null;
        } catch (IOException e) {
            Timber.e(Log.getStackTraceString(e));
            return null;
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            onLocationNameReader.onCityNameFound(result);
        } else {
            onLocationNameReader.onCityNameReadError("Something went wrong while reading location name");
        }
    }
}
