package in.abmulani.yahooweather.entityModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aabid on 6/8/16.
 */
public class ItemData {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("pubDate")
    @Expose
    private String pubDate;
    @SerializedName("condition")
    @Expose
    private ConditionData condition;
    @SerializedName("forecast")
    @Expose
    private List<Forecast> forecast = new ArrayList<Forecast>();
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("guid")
    @Expose
    private Guid guid;

    public String getTitle() {
        return title;
    }

    public String getLat() {
        return lat;
    }

    public String get_long() {
        return _long;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public ConditionData getCondition() {
        return condition;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public String getDescription() {
        return description;
    }

    public Guid getGuid() {
        return guid;
    }
}
