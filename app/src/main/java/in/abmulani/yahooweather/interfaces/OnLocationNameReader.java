package in.abmulani.yahooweather.interfaces;

/**
 * Callback interface from the Geocoder Async to get City Name
 */
public interface OnLocationNameReader {

    void onCityNameFound(String cityName);

    void onCityNameReadError(String errMsg);

}
