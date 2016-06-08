package in.abmulani.yahooweather.entityModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aabid on 6/8/16.
 */
public class Results {

    @SerializedName("channel")
    @Expose
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }
}
