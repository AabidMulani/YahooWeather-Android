package in.abmulani.yahooweather.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.abmulani.yahooweather.BaseActivity;
import in.abmulani.yahooweather.BaseApplication;
import in.abmulani.yahooweather.R;
import in.abmulani.yahooweather.entityModels.Channel;
import in.abmulani.yahooweather.entityModels.ResponseSet;
import in.abmulani.yahooweather.interfaces.OnLocationNameReader;
import in.abmulani.yahooweather.utils.NetworkUtility;
import in.abmulani.yahooweather.utils.Utils;
import in.abmulani.yahooweather.webAsyncs.GeocoderAsync;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/**
 * Map Screen
 * 1) Checks for permission for Location Reading on Android L and above
 * 2) Moves to your current location
 * 3) Reads City Name of your current pointed location
 * 4) Loads location information from yahoo weather API
 * 5) If you pan the Map the Camera Centre location will be used and fresh weather information will be displayed
 */

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnLocationNameReader,
        GoogleMap.OnCameraChangeListener {

    private static final int PERMISSION_LOCATION_REQUEST = 2323;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.progressTextView)
    TextView progressTextView;
    @Bind(R.id.infoResultMsg)
    TextView infoResultMsg;
    @Bind(R.id.mainResultMsgLayout)
    LinearLayout mainResultMsgLayout;
    @Bind(R.id.dataLayout)
    RelativeLayout dataLayout;
    @Bind(R.id.windSpeedTextView)
    TextView windSpeedTextView;
    @Bind(R.id.windSpeedLayout)
    LinearLayout windSpeedLayout;
    @Bind(R.id.humidityTextView)
    TextView humidityTextView;
    @Bind(R.id.humidityLayout)
    LinearLayout humidityLayout;
    @Bind(R.id.pressureTextView)
    TextView pressureTextView;
    @Bind(R.id.pressureLayout)
    LinearLayout pressureLayout;
    @Bind(R.id.sunriseTextView)
    TextView sunriseTextView;
    @Bind(R.id.sunriseLayout)
    LinearLayout sunriseLayout;
    @Bind(R.id.sunsetTextView)
    TextView sunsetTextView;
    @Bind(R.id.sunsetLayout)
    LinearLayout sunsetLayout;
    @Bind(R.id.foreCastTextView)
    TextView foreCastTextView;
    @Bind(R.id.foreCastLayout)
    LinearLayout foreCastLayout;
    @Bind(R.id.temperatureTextView)
    TextView temperatureTextView;
    @Bind(R.id.temperatureLayout)
    LinearLayout temperatureLayout;
    @Bind(R.id.holderLayout)
    RelativeLayout holderLayout;


    private GoogleMap googleMap;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private boolean firstLocationRead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        firstLocationRead = false;

        // set up toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Welcome");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(20 * 1000)
                .setFastestInterval(10 * 1000);


    }


    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setStatusMsg("Map Initialized");

        this.googleMap.getUiSettings().setAllGesturesEnabled(true);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
        this.googleMap.getUiSettings().setScrollGesturesEnabled(true);
        this.googleMap.setOnCameraChangeListener(this);

        // check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION_REQUEST);
        } else {
            this.googleMap.setMyLocationEnabled(true);
        }

    }

    // indicates the current status or action on the UI (Top Left Orange TextView)
    private void setStatusMsg(String msg) {
        progressTextView.setText(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // if permission not granted exit the application
                    Utils.showThisMsg(activity, "We require location permissions to proceed with this application. Please restart application and grant location permissions", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                } else {
                    googleMap.setMyLocationEnabled(true);
                }
            }

        }
    }


    // ---------- Location Related Logic -----------

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.showSnackBar(activity, "Location Permission Not Granted");
            setStatusMsg("Location Permission Not Granted");
        } else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } else {
                if (!firstLocationRead) {
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    setStatusMsg("Reading Current Location..");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 14), 2000, null);
                    if (NetworkUtility.isNetworkAvailable(activity)) {
                        // GeoCoder call to get the location name
                        new GeocoderAsync(activity, currentLatitude, currentLongitude, this).execute();
                    } else {
                        Utils.showSnackBar(activity, "No Network");
                    }
                    firstLocationRead = true;
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("onLocationChanged");
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        if (!firstLocationRead) {
            setStatusMsg("Reading Current Location..");
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 14), 2000, null);
            if (NetworkUtility.isNetworkAvailable(activity)) {
                // GeoCoder call to get the location name
                new GeocoderAsync(activity, currentLatitude, currentLongitude, this).execute();
            } else {
                Utils.showSnackBar(activity, "No Network");
            }
            firstLocationRead = true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.d("onConnectionFailed");
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Timber.e(Log.getStackTraceString(e));
            }
        } else {
            Timber.e("onConnectionFailed:: " + connectionResult.getErrorCode());
        }
    }


    // ---------- Retrieve Current City Logic -----------

    @Override
    public void onCityNameFound(String cityName) {
        Timber.d("onCityNameFound");
        Timber.e(cityName);
        setStatusMsg("Loading information for " + cityName);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(cityName);

        if (NetworkUtility.isNetworkAvailable(activity)) {
            getWeatherDataForThisCity(cityName);
        } else {
            Utils.showSnackBar(activity, "No Network");
        }


    }

    @Override
    public void onCityNameReadError(String errMsg) {
        Timber.d("onCityNameReadError");
        dataLayout.setVisibility(View.GONE);
        holderLayout.setBackgroundColor(Color.WHITE);
    }


    // ---------- Panning Map and Camera Changed -----------

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        setStatusMsg("Reading Panned Location..");
        dataLayout.setVisibility(View.GONE);
        if (NetworkUtility.isNetworkAvailable(activity)) {
            new GeocoderAsync(activity, cameraPosition.target.latitude, cameraPosition.target.longitude, this).execute();
        } else {
            Utils.showSnackBar(activity, "No Network");
        }
    }


    // ---------- Load Weather Information -----------

    /**
     * @param cityName The text name of the location this will be sent in the '?q=' param of the API call
     *                 <p/>
     *                 This method will call the weather api using the Retrofit library on success it will display the relevant weather information on the UI
     */
    private void getWeatherDataForThisCity(String cityName) {
        try {
            String generatedYML = getYML(cityName);

            Call<ResponseSet> responseSetCall = ((BaseApplication) getApplication()).getOTPUMWebServiceInterface().getWeatherInformation(generatedYML, "json");

            responseSetCall.enqueue(new Callback<ResponseSet>() {
                @Override
                public void onResponse(Call<ResponseSet> call, Response<ResponseSet> response) {
                    Timber.e(response.body().toString());
                    setStatusMsg("Success");
                    ResponseSet responseSet = response.body();
                    dataLayout.setVisibility(View.VISIBLE);
                    if (responseSet.getQuery() != null && responseSet.getQuery().getResults() != null) {
                        infoResultMsg.setVisibility(View.GONE);
                        mainResultMsgLayout.setVisibility(View.VISIBLE);
                        displayFormattedData(responseSet.getQuery().getResults().getChannel());

                    } else {
                        infoResultMsg.setVisibility(View.VISIBLE);
                        mainResultMsgLayout.setVisibility(View.GONE);
                        holderLayout.setBackgroundColor(Color.WHITE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseSet> call, Throwable t) {
                    setStatusMsg("Error while reading weather data");
                }
            });

        } catch (Exception ex) {
            Timber.e(Log.getStackTraceString(ex));
        }
    }


    /**
     * Parse and display the Weather Data on the UI
     */
    private void displayFormattedData(Channel channel) {
        if (channel.getAstronomy() != null) {
            sunriseLayout.setVisibility(View.VISIBLE);
            sunsetLayout.setVisibility(View.VISIBLE);
            sunriseTextView.setText("Sunrise: " + channel.getAstronomy().getSunrise());
            sunsetTextView.setText("Sunset: " + channel.getAstronomy().getSunset());
        } else {
            sunriseLayout.setVisibility(View.GONE);
            sunsetLayout.setVisibility(View.GONE);
        }


        if (channel.getAtmosphere() != null) {
            humidityLayout.setVisibility(View.VISIBLE);
            pressureLayout.setVisibility(View.VISIBLE);

            humidityTextView.setText("Humidity: " + channel.getAtmosphere().getHumidity());
            pressureTextView.setText("Pressure: " + channel.getAtmosphere().getPressure());

        } else {
            humidityLayout.setVisibility(View.GONE);
            pressureLayout.setVisibility(View.GONE);
        }

        if (channel.getWind() != null) {
            windSpeedLayout.setVisibility(View.VISIBLE);
            windSpeedTextView.setText("Wind Speed: " + channel.getWind().getSpeed());
        } else {
            windSpeedLayout.setVisibility(View.GONE);
        }

        if (channel.getItem() != null && channel.getItem().getCondition() != null) {
            temperatureLayout.setVisibility(View.VISIBLE);
            foreCastLayout.setVisibility(View.VISIBLE);

            temperatureTextView.setText("Temperature: " + channel.getItem().getCondition().getTemp().concat(" °F"));
            foreCastTextView.setText("Forecast: " + channel.getItem().getCondition().getText());

            updateBackgroundColorIndicator(channel.getItem().getCondition().getTemp());

        } else {
            temperatureLayout.setVisibility(View.GONE);
            foreCastLayout.setVisibility(View.GONE);
            holderLayout.setBackgroundColor(Color.WHITE);
        }

    }

    private void updateBackgroundColorIndicator(String temp) {
        try {
            double temperature = Double.parseDouble(temp);

            if (temperature < 42) {
                holderLayout.setBackgroundColor(Color.WHITE);
            } else if (temperature < 52) {
                holderLayout.setBackgroundColor(Color.parseColor("#44ff0000"));
            } else if (temperature < 62) {
                holderLayout.setBackgroundColor(Color.parseColor("#55ff0000"));
            } else if (temperature < 72) {
                holderLayout.setBackgroundColor(Color.parseColor("#66ff0000"));
            } else if (temperature < 82) {
                holderLayout.setBackgroundColor(Color.parseColor("#77ff0000"));
            } else if (temperature < 92) {
                holderLayout.setBackgroundColor(Color.parseColor("#88ff0000"));
            } else if (temperature < 102) {
                holderLayout.setBackgroundColor(Color.parseColor("#99ff0000"));
            } else if (temperature < 112) {
                holderLayout.setBackgroundColor(Color.parseColor("#aaff0000"));
            } else {
                holderLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            }

        } catch (Exception ex) {
            Timber.e(Log.getStackTraceString(ex));
            holderLayout.setBackgroundColor(Color.WHITE);
        }
    }


    /**
     * @param cityName
     * @return the formatted QML for Yahoo Weather API
     */
    private String getYML(String cityName) {
        return "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + cityName + "\")";
    }


}
