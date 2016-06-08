package in.abmulani.yahooweather.interfaces;

/**
 * Created by aabid on 6/8/16.
 */
public interface OnLocationNameReader {

    void onCityNameFound(String cityName);

    void onCityNameReadError(String errMsg);

}
