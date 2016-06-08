package in.abmulani.yahooweather.entityModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aabid on 6/8/16.
 */
public class Units {

    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("pressure")
    @Expose
    private String pressure;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("temperature")
    @Expose
    private String temperature;

    public String getDistance() {
        return distance;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSpeed() {
        return speed;
    }

    public String getTemperature() {
        return temperature;
    }
}
